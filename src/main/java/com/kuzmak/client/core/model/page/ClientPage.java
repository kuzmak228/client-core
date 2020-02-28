package com.kuzmak.client.core.model.page;

import java.util.List;

public class ClientPage<T> {

    private List<T> content;
    private ClientPageable pageable;
    private ClientSort sort;
    private Long totalPages;
    private Long totalElements;
    private Boolean last;
    private Boolean first;
    private Long number;
    private Long numberOfElements;
    private Integer size;
    private Boolean empty;

    public ClientPage() {
    }

    public ClientPage(final List<T> content, final ClientPageable pageable, final ClientSort sort,
                      final Long totalPages, final Long totalElements, final Boolean last,
                      final Boolean first, final Long number, final Long numberOfElements,
                      final Integer size, final Boolean empty) {
        this.content = content;
        this.pageable = pageable;
        this.sort = sort;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.last = last;
        this.first = first;
        this.number = number;
        this.numberOfElements = numberOfElements;
        this.size = size;
        this.empty = empty;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(final List<T> content) {
        this.content = content;
    }

    public ClientPageable getPageable() {
        return pageable;
    }

    public void setPageable(final ClientPageable pageable) {
        this.pageable = pageable;
    }

    public ClientSort getSort() {
        return sort;
    }

    public void setSort(final ClientSort sort) {
        this.sort = sort;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(final Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(final Long totalElements) {
        this.totalElements = totalElements;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(final Boolean last) {
        this.last = last;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(final Boolean first) {
        this.first = first;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(final Long number) {
        this.number = number;
    }

    public Long getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(final Long numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(final Integer size) {
        this.size = size;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(final Boolean empty) {
        this.empty = empty;
    }
}
