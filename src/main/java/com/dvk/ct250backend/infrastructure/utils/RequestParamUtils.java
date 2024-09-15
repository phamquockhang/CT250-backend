package com.dvk.ct250backend.infrastructure.utils;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequestParamUtils {

    public List<Sort.Order> toSortOrders(String sortParams) {
        if(sortParams == null || sortParams.isEmpty()) {
            return new ArrayList<>();
        }
        String[] sortArr = sortParams.split(";");
        List<Sort.Order> sortOrders = new ArrayList<>();
        for (String s : sortArr) {
            String[] sortStr = s.split(",");
            Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortStr[1]), sortStr[0]);
            sortOrders.add(order);
        }
        return sortOrders;
    }
}
