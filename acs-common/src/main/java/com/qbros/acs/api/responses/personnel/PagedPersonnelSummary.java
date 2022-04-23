package com.qbros.acs.api.responses.personnel;

import com.qbros.acs.api.responses.PagedSummary;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.domain.Page;

@EqualsAndHashCode(callSuper = true)
@Value
public class PagedPersonnelSummary extends PagedSummary<PersonnelDetails> {

    public PagedPersonnelSummary(Page<PersonnelDetails> page) {
        super(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
