package ru.pushpyshev.testtaskparqour.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ShortenUrlDTO {
    @NotNull
    @Size(min = 5, max = 1024)
    private String url;
    private Integer counter;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
}
