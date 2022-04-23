package com.qbros.uithymeleaf.controllers.sentry;

import com.qbros.acs.api.queries.gate.AllGatesSummaryQuery;
import com.qbros.acs.api.queries.report.GateTrafficQuery;
import com.qbros.acs.api.responses.reports.TrafficReport;
import com.qbros.acs.api.sharedmodels.domain.GateID;
import com.qbros.uithymeleaf.controllers.AbsController;
import com.qbros.uithymeleaf.models.GateSimpleModel;
import com.qbros.uithymeleaf.services.GatesService;
import com.qbros.uithymeleaf.services.SentryService;
import com.qbros.uithymeleaf.services.mappers.GateServiceMapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("sentry/monitor")
public class TrafficMonitorCtrl extends AbsController {

    private static final String TRAFFIC_LIST_MODEL = "trafficList";
    private static final String GATES_LIST = "gatesList";
    private static final String TRAFFIC_SUB_KEY = "TRAFFIC_SUB_KEY";
    private final SentryService sentryService;
    private final GatesService gatesService;
    private final GateServiceMapper gateServiceMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("")
    public String getPage() {
        return "sentryMonitor";
    }

    @GetMapping("list")
    public String getTrafficList(Model model,
                                 @RequestParam("count") int count,
                                 @RequestParam("targetGateID") String gateId) {
        return Try.of(() -> {
                    closePreviousSubscription();
                    return sentryService.gateTraffic(new GateTrafficQuery(count, new GateID(gateId)));
                })
                .mapTry(responseSub -> {
                    responseSub.updates().subscribe(this::sendToUser);
                    sessionState.putValue(TRAFFIC_SUB_KEY, responseSub);
                    return responseSub.initialResult().block();
                })
                .onSuccess(it -> model.addAttribute(TRAFFIC_LIST_MODEL, it))
                .onFailure(throwable -> addErrorMsg(model, throwable))
                .transform(voids -> "sentryMonitor::content");
    }

    @ModelAttribute(TRAFFIC_LIST_MODEL)
    protected List<TrafficReport> newPersonnel() {
        return Collections.emptyList();
    }

    @ModelAttribute(GATES_LIST)
    protected List<GateSimpleModel> gatesList() {
        return Try
                .of(() -> gatesService.getAllSimpleSummary(new AllGatesSummaryQuery()))
                .mapTry(gateServiceMapper::toGateSimpleModelList)
                .getOrElseGet(throwable -> Collections.emptyList());
    }

    private void closePreviousSubscription() {
        sessionState.getValue(TRAFFIC_SUB_KEY)
                .ifPresent(subscription -> ((SubscriptionQueryResult<?, ?>) subscription).close());
    }

    private void sendToUser(TrafficReport trafficReport) {
        log.info("Sending Traffic Report {} to User", trafficReport);
        messagingTemplate.convertAndSend("/live-update/periodic", trafficReport);
    }

}
