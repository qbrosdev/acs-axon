package com.qbros.uithymeleaf.services.mappers;

import com.qbros.acs.api.commands.personnel.AddPersonnelCmd;
import com.qbros.acs.api.commands.personnel.DeletePersonnelCmd;
import com.qbros.acs.api.commands.personnel.EditPersonnelCmd;
import com.qbros.acs.api.queries.personnel.AllPersonnelSummaryQuery;
import com.qbros.acs.api.queries.personnel.PersonnelByIdQuery;
import com.qbros.acs.api.responses.personnel.PagedPersonnelSummary;
import com.qbros.acs.api.responses.personnel.PersonnelDetails;
import com.qbros.acs.api.sharedmodels.domain.E_mail;
import com.qbros.acs.api.sharedmodels.domain.Name;
import com.qbros.acs.api.sharedmodels.domain.PersonnelAggregateID;
import com.qbros.acs.api.sharedmodels.domain.PersonnelID;
import com.qbros.uithymeleaf.models.PersonnelModel;
import com.qbros.uithymeleaf.models.paging.PagedModel;
import com.qbros.uithymeleaf.models.paging.PagingNav;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonnelServiceMapper {

    public PagedModel<PersonnelModel> toPagedPersonnelModel(PagedPersonnelSummary pps) {
        List<PersonnelModel> content = pps.getContent().stream()
                .map(this::getPersonnelModel)
                .collect(Collectors.toList());
        return new PagedModel<>(content, PagingNav.of(pps.getTotalPages(), pps.getOffset() + 1, pps.getLimit()));
    }

    public AddPersonnelCmd toAddPerCommand(PersonnelModel model) {
        return new AddPersonnelCmd(new PersonnelAggregateID(model.getPersonnel_id()),
                new Name(model.getFirstName()),
                new Name(model.getLastName()),
                new E_mail(model.getEmail()));
    }

    public EditPersonnelCmd toEditPersonnelCommand(PersonnelModel pm) {
        return new EditPersonnelCmd(new PersonnelAggregateID(pm.getPersonnel_id()),
                new Name(pm.getFirstName()),
                new Name(pm.getLastName()),
                new E_mail(pm.getEmail()));
    }

    public PersonnelModel toPersonnelModel(PersonnelDetails personnelDetails) {
        return new PersonnelModel(personnelDetails.getPersonnelId().stringValue(),
                personnelDetails.getFirstName().getValue(),
                personnelDetails.getLastName().getValue(),
                personnelDetails.getEmail().getValue());
    }

    public AllPersonnelSummaryQuery toPersonnelSummaryQuery(int idx, int size) {
        return new AllPersonnelSummaryQuery(idx - 1, size);
    }

    public DeletePersonnelCmd toDeleteCommand(String perId) {
        return new DeletePersonnelCmd(new PersonnelAggregateID(perId));
    }

    public PersonnelByIdQuery toPersonnelByIdQuery(String idx) {
        return new PersonnelByIdQuery(new PersonnelID(idx));
    }

    private PersonnelModel getPersonnelModel(PersonnelDetails ps) {
        return new PersonnelModel(ps.getPersonnelId().stringValue(),
                ps.getFirstName().getValue(),
                ps.getLastName().getValue(),
                ps.getEmail().getValue());
    }
}
