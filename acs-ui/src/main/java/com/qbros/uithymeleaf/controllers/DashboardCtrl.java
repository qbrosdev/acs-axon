package com.qbros.uithymeleaf.controllers;

import com.qbros.uithymeleaf.models.GateTypesModel;
import com.qbros.uithymeleaf.models.GatesHistoryModel;
import com.qbros.uithymeleaf.models.SystemSummaryModel;
import com.qbros.uithymeleaf.services.SystemMonitorService;
import com.qbros.uithymeleaf.services.mappers.SystemMonitorServiceMapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardCtrl extends AbsController {

    private static final String SYSTEM_SUMMARY_MODEL = "systemSummary";
    private static final String SYS_SUB_KEY = "SYS_SUB_KEY";
    private final SystemMonitorService systemMonitorService;
    private final SystemMonitorServiceMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("")
    public String getIndex() {
        return "dashboard";
    }

    @GetMapping("summary")
    public String summary(Model model) {
        return Try.of(() -> {
                    closePreviousSubscription();
                    return systemMonitorService.registerForSystemSummary();
                })
                .mapTry(responseSub -> {
                    responseSub.updates().map(mapper::toModel).subscribe(this::sendToUser);
                    sessionState.putValue(SYS_SUB_KEY, responseSub);
                    return responseSub.initialResult().map(mapper::toModel).block();
                })
                .onSuccess(it -> model.addAttribute(SYSTEM_SUMMARY_MODEL, it))
                .onFailure(throwable -> addErrorMsg(model, throwable))
                .transform(voids -> "dashboard::systemStatus");
    }

    @GetMapping("gatesTrafficSummary")
    @ResponseBody
    public GatesHistoryModel getGatesTraffic() {
        return Try.of(systemMonitorService::gatesSummary)
                .mapTry(mapper::getGatesSummaryModel)
                .getOrElseGet(throwable -> new GatesHistoryModel(Collections.emptyList(), Collections.emptyList(), "NA"));
    }

    @GetMapping("gateTypesSummary")
    @ResponseBody
    public GateTypesModel getGateTypes() {
        return Try.of(systemMonitorService::getGateTypes)
                .mapTry(mapper::toGateTypesModel)
                .getOrElseGet(throwable -> new GateTypesModel(Collections.emptyList(), Collections.emptyList(), "NA"));
    }

    @ModelAttribute(SYSTEM_SUMMARY_MODEL)
    protected SystemSummaryModel editPersonnel() {

        return new SystemSummaryModel(0, 0, 0, 0, 0, 0);
    }

    private void closePreviousSubscription() {
        sessionState.getValue(SYS_SUB_KEY)
                .ifPresent(subscription -> ((SubscriptionQueryResult<?, ?>) subscription).close());
    }

    private void sendToUser(SystemSummaryModel systemSummary) {
        log.info("Sending {} to UI", systemSummary);
        messagingTemplate.convertAndSend("/live-update/summary", systemSummary);
    }
}
