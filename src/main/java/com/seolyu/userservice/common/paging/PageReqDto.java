package com.seolyu.userservice.common.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class PageReqDto {
    private static final Integer PAGE_DEFAULT_SIZE = 20;

    private Integer page;

    private Integer size;

    public Pageable generatePage() {
        return this.generatePage(PAGE_DEFAULT_SIZE);
    }

    public Pageable generatePage(Integer pageSize) {
        int page = validPage(this.page) ? this.page : 0;
        int size = validSize(this.size) ? this.size : pageSize;

        return PageRequest.of(page, size);
    }

    private boolean validPage(Integer page) {
        return page != null;
    }

    private boolean validSize(Integer size) {
        return !(size == null || size < 1);
    }
}