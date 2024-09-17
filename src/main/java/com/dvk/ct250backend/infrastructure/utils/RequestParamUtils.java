package com.dvk.ct250backend.infrastructure.utils;

import com.dvk.ct250backend.app.dto.request.SearchCriteria;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<SearchCriteria> getSearchCriteria(Map<String, String> params, String key) {
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        String value = params.get(key);
        if(value != null && !value.isEmpty()) {
            String[] valueArr = value.split(",");
            for (String searchValue : valueArr) {
                SearchCriteria searchCriteria = SearchCriteria.builder()
                        .key(key)
                        .operation("=")
                        .value(searchValue)
                        .build();
                searchCriteriaList.add(searchCriteria);
            }
        }
        return searchCriteriaList;
    }
}
