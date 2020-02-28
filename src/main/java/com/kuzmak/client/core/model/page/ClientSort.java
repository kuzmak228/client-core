package com.kuzmak.client.core.model.page;

public class ClientSort {

    private Boolean unsorted;
    private Boolean sorted;
    private Boolean empty;

    public ClientSort() {
    }

    public ClientSort(final Boolean unsorted, final Boolean sorted, final Boolean empty) {
        this.unsorted = unsorted;
        this.sorted = sorted;
        this.empty = empty;
    }

    public Boolean getUnsorted() {
        return unsorted;
    }

    public void setUnsorted(final Boolean unsorted) {
        this.unsorted = unsorted;
    }

    public Boolean getSorted() {
        return sorted;
    }

    public void setSorted(final Boolean sorted) {
        this.sorted = sorted;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(final Boolean empty) {
        this.empty = empty;
    }
}
