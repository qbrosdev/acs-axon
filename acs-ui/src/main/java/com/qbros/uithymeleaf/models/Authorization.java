package com.qbros.uithymeleaf.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Authorization {

    private String personnelId;
    private String fullName;
    private boolean isAllowed;
}
