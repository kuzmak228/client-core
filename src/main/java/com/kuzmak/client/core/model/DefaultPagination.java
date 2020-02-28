package com.kuzmak.client.core.model;

public class DefaultPagination {
    private int page = 1;
    private int limit = 10;
    private String order;
    private String filter;

    public DefaultPagination() {
    }

    public DefaultPagination(final int page, final int limit, final String order, final String filter) {
        this.page = page;
        this.limit = limit;
        this.order = order;
        this.filter = filter;
    }

    public int getPage() {
        return page;
    }

    public void setPage(final int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(final String order) {
        this.order = order;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(final String filter) {
        this.filter = filter;
    }
}
