package com.dvk.ct250backend.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationDTO<T> {
    Meta meta;
    T result;

    @Getter
    @Setter
    public static class Meta {
        int page;
        int pageSize;
        int pages;
        long total;
    }
}
