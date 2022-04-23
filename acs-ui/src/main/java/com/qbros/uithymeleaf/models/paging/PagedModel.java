package com.qbros.uithymeleaf.models.paging;

import com.qbros.uithymeleaf.models.ViewModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class PagedModel<T extends ViewModel> {

    private List<T> content;
    private PagingNav pagingNav;

    public static <T extends ViewModel> PagedModel<T> emptyPage(int defaultPageSize) {
        return new PagedModel<>(Collections.emptyList(), PagingNav.of(0, 0, defaultPageSize));
    }
}
