package com.dvk.ct250backend.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pagination<T> {
    Meta meta;
    List<T> result;

//    @Builder
//    public static class Meta implements Serializable {
//        int page;
//        int pageSize;
//        int pages;
//        long total;
//    }
}
