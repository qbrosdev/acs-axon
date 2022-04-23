package com.qbros;

import com.qbros.acs.api.sharedmodels.domain.Action;
import com.qbros.acs.api.sharedmodels.domain.ActionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("access")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("check")
    ActionResult checkAuthorization(@RequestParam String gateId,
                                    @RequestParam String personnelId,
                                    @RequestParam Action action) {

        return authenticationService.authenticate(gateId, personnelId, action);
    }
}
