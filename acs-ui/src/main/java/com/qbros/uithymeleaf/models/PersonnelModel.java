package com.qbros.uithymeleaf.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonnelModel extends ViewModel {

    private String personnel_id;
    private String firstName;
    private String lastName;
    private String email;
}
