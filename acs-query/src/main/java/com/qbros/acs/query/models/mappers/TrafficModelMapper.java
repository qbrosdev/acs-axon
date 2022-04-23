package com.qbros.acs.query.models.mappers;

import com.qbros.acs.api.events.AbsEvent;
import com.qbros.acs.api.events.gate.GateAuthorizationFailEvent;
import com.qbros.acs.api.events.gate.GateAuthorizationSuccessEvent;
import com.qbros.acs.api.responses.reports.TrafficReport;
import com.qbros.acs.api.sharedmodels.domain.GateAggregateID;
import com.qbros.acs.query.models.TrafficReportEntity;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Component
public class TrafficModelMapper {

    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.US)
            .withZone(ZoneId.systemDefault());

    public TrafficReportEntity eventToSuccessEntity(GateAuthorizationSuccessEvent event) {
        return new TrafficReportEntity(getGateID(event),
                event.getPersonnelID().stringValue(),
                event.getResult().getAction(),
                true,
                event.getTimeStamp());
    }

    public TrafficReportEntity eventToFailEntity(GateAuthorizationFailEvent event) {
        return new TrafficReportEntity(getGateID(event),
                event.getPersonnelID().stringValue(),
                event.getResult().getAction(),
                false,
                event.getTimeStamp());
    }

    public TrafficReport toTrafficReport(TrafficReportEntity entity) {
        return new TrafficReport(entity.getGateID(),
                entity.getPersonnelID(),
                entity.getAction(),
                formatter.format(entity.getTimestamp()),
                entity.isSuccessfulPass());
    }

    private String getGateID(AbsEvent<GateAggregateID> event) {
        return event.getAggregateId().getGateID().stringValue();
    }
}
