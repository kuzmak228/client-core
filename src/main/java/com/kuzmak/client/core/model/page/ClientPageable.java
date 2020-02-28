package com.kuzmak.client.core.model.page;

public class ClientPageable {

    private ClientSort sort;
    private Integer pageSize;
    private Integer pageNumber;
    private Long offset;
    private Boolean paged;
    private Boolean unpaged;

    public ClientPageable() {
    }

    public ClientPageable(final ClientSort sort, final Integer pageSize, final Integer pageNumber,
                          final Long offset, final Boolean paged, final Boolean unpaged) {
        this.sort = sort;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.offset = offset;
        this.paged = paged;
        this.unpaged = unpaged;
    }

    public ClientSort getSort() {
        return sort;
    }

    public void setSort(final ClientSort sort) {
        this.sort = sort;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(final Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(final Long offset) {
        this.offset = offset;
    }

    public Boolean getPaged() {
        return paged;
    }

    public void setPaged(final Boolean paged) {
        this.paged = paged;
    }

    public Boolean getUnpaged() {
        return unpaged;
    }

    public void setUnpaged(final Boolean unpaged) {
        this.unpaged = unpaged;
    }
}
