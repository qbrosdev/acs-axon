package com.qbros.acs.api.responses;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public abstract class PagedSummary<T> {

    private static final PagedSummary<? extends QueryResp> EMPTY_PAGE =
            new PagedSummary<>(Collections.emptyList(), 0, 0, 0, 0) {
                @Override
                public List<QueryResp> getContent() {
                    return super.getContent();
                }
            };
    protected final List<T> content;
    protected final int offset;
    protected final int limit;
    protected final long total;
    protected final int totalPages;

    public PagedSummary(List<T> content, int offset, int limit, long total, int totalPages) {
        this.content = content;
        this.offset = offset;
        this.limit = limit;
        this.total = total;
        this.totalPages = totalPages;
    }

    public static PagedSummary<? extends QueryResp> emptyPage() {
        return EMPTY_PAGE;
    }
}


