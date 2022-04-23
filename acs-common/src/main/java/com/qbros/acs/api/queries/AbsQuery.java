package com.qbros.acs.api.queries;

import lombok.Data;

/**
 * Represents a query object that will be sent to {@link org.axonframework.queryhandling.QueryBus}
 * via {@link org.axonframework.queryhandling.QueryGateway}
 * and will be handled by {@link org.axonframework.queryhandling.QueryHandler}
 */

@Data
public abstract class AbsQuery implements Query {
}
