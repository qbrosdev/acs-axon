package com.qbros.uithymeleaf.controllers.manager;

import com.qbros.acs.api.commands.AbsCommand;
import com.qbros.acs.api.queries.gate.AllGatesSummaryQuery;
import com.qbros.acs.api.queries.personnel.AllPersonnelIdAndNameQuery;
import com.qbros.acs.api.responses.gate.GateDetails;
import com.qbros.acs.api.responses.gate.GateSyncCompletedSignal;
import com.qbros.acs.api.responses.personnel.PersonnelNameAndId;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.api.sharedmodels.domain.GateType;
import com.qbros.acs.api.sharedmodels.domain.SyncStatus;
import com.qbros.uithymeleaf.controllers.AbsController;
import com.qbros.uithymeleaf.models.GateDetailsModel;
import com.qbros.uithymeleaf.models.GateSimpleModel;
import com.qbros.uithymeleaf.models.SyncCompletedSignalModel;
import com.qbros.uithymeleaf.services.GatesService;
import com.qbros.uithymeleaf.services.PersonnelService;
import com.qbros.uithymeleaf.services.mappers.GateServiceMapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("manager/gates")
@RequiredArgsConstructor
@Slf4j
@SessionAttributes("gatesModel")
public class GatesMgntCtrl extends AbsController {

    private static final String PAGE = "gates";
    private static final String CONTENT_FAG = PAGE + "::content";
    private static final String EDIT_DIALOG_FRAG = PAGE + "::editDialog";
    private static final String GATES_MODEL = "gatesModel";
    private static final String NEW_GATE = "newGate";
    private static final String EDIT_GATE = "editGate";
    private static final String SYNC_SUB_KEY = "SYNC_SUB_KEY";
    private final GatesService gatesService;
    private final PersonnelService personnelService;
    private final GateServiceMapper gateServiceMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("")
    public String getPage() {
        return "gates";
    }

    @GetMapping("getById")
    public String gateDetails(Model model, @RequestParam String idx) {
        return Try.of(() -> {
                    GateDetails result = gatesService.getById(gateServiceMapper.toGateDetailsByIdQuery(idx));
                    List<PersonnelNameAndId> res2 = personnelService.getPersonnelSummaries(new AllPersonnelIdAndNameQuery());
                    return gateServiceMapper.toDetailedModel(result, res2);
                })
                .onSuccess(pm -> model.addAttribute(EDIT_GATE, pm))
                .onFailure(throwable -> addErrorMsg(model, throwable))
                .transform(voids -> EDIT_DIALOG_FRAG);
    }

    @GetMapping("list")
    public String getGates(Model model) {
        return handleFailures(Try.of(this::getGatesList), model);
    }

    @PostMapping("add")
    public String newGate(Model model, @ModelAttribute(NEW_GATE) GateDetailsModel viewModel) {
        return handleFailures(
                Try.of(() -> {
                    gatesService.executeSynchronousCommand(gateServiceMapper.toCreateGateCommand(viewModel));
                    return getGatesList();
                }), model);
        // return callSynchronousService(model, gateServiceMapper.toCreateGateCommand(viewModel));
    }

    @PostMapping("edit")
    public String editGate(Model model, @ModelAttribute(EDIT_GATE) GateDetailsModel viewModel) {
        return callSynchronousService(model, gateServiceMapper.toEditGateCommand(viewModel));
    }

    @PostMapping("delete")
    public String deleteGate(Model model, @ModelAttribute("gateId") String perId) {
        return callSynchronousService(model, gateServiceMapper.getCmd(perId));
    }

    @PostMapping("deactivate")
    public String deactivateGate(Model model, @ModelAttribute("gateId") String perId) {
        return callSynchronousService(model, gateServiceMapper.toInActivateGateCmd(perId));
    }

    @PostMapping("activate")
    public String activateGate(Model model, @ModelAttribute("gateId") String perId) {
        return callSynchronousService(model, gateServiceMapper.toActivateGateCmd(perId));
    }

    @PostMapping("sync")
    @ResponseBody
    public void syncGate(@ModelAttribute("gateId") String perId) {
        Try.of(() -> {
                    closePreviousSubscription();
                    return gatesService.registerForSyncUpdates();
                })
                .andThen(responseSub -> {
                    responseSub.updates().map(this::mape).subscribe(this::sendToUser);
                    sessionState.putValue(SYNC_SUB_KEY, responseSub);
                    gatesService.sync(gateServiceMapper.toSyncCmd(perId));
                })
                .getOrElseThrow(throwable -> new RuntimeException(String
                        .format("Exception in sync with message: %s", throwable.getMessage())));
    }

    @ModelAttribute(GATES_MODEL)
    protected List<GateSimpleModel> getGatesModel() {
        return Collections.emptyList();
    }

    @ModelAttribute(NEW_GATE)
    protected GateDetailsModel newGate() {
        List<PersonnelNameAndId> res2 = personnelService.getPersonnelSummaries(new AllPersonnelIdAndNameQuery());
        return new GateDetailsModel("",
                "",
                gateServiceMapper.buildMap(GateType.ENTRANCE),
                SyncStatus.SYNC,
                gateServiceMapper.toAuthorization(Collections.emptyList(), res2));
    }

    @ModelAttribute(EDIT_GATE)
    protected GateDetailsModel editGate() {
        return new GateDetailsModel();
    }

    private void closePreviousSubscription() {
        sessionState.getValue(SYNC_SUB_KEY)
                .ifPresent(subscription -> ((SubscriptionQueryResult<?, ?>) subscription).close());
    }

    private String callSynchronousService(Model model, AbsCommand<GateAggregateID> cmd) {
        return handleFailures(
                Try.of(() -> {
                    gatesService.executeSynchronousCommand(cmd);
                    return getGatesList();
                }), model);
    }

    private SyncCompletedSignalModel mape(GateSyncCompletedSignal signal) {
        return new SyncCompletedSignalModel(signal.getGateID().stringValue(), signal.getGateName());
    }

    private List<GateSimpleModel> getGatesList() {
        return gateServiceMapper.toGateSimpleModelList(gatesService.getAllSimpleSummary(new AllGatesSummaryQuery()));
    }

    private String handleFailures(Try<Object> tRy, Model model) {
        return tRy
                .onSuccess(viewModel -> model.addAttribute(GATES_MODEL, viewModel))
                .onFailure(throwable -> addErrorMsg(model, throwable))
                .transform(voids -> CONTENT_FAG);
    }

    private void sendToUser(SyncCompletedSignalModel signalModel) {
        log.info("Sending {} to UI", signalModel);
        messagingTemplate.convertAndSend("/live-update/syncCompleted", signalModel);
    }

}
