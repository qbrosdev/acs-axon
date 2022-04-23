package com.qbros.uithymeleaf.models.paging;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageItem {

    private PageItemType pageItemType;
    private int index;
    private boolean active;
}
