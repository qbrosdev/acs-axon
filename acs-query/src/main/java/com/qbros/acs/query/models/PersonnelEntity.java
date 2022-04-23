package com.qbros.acs.query.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Personnel")
public class PersonnelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;
    private String personnelID;
    private String firstName;
    private String lastName;
    private String email;

    public PersonnelEntity(String personnelID, String firstName, String lastName, String email) {
        this.personnelID = personnelID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    protected PersonnelEntity() {

    }
}
