//package com.dvk.ct250backend.infrastructure.elasticsearch.repository;
//
//import com.dvk.ct250backend.infrastructure.elasticsearch.document.SearchAirportDocument;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.annotations.Query;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface AirportElasticRepo extends ElasticsearchRepository<SearchAirportDocument, Integer> {
//    @Query("{\"bool\": {\"should\": ["
//            + "{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"airport_code^3\", \"airport_name^2\", \"city_name^1.5\", \"city_code^3\", \"country_name\"], \"type\": \"best_fields\"}},"
//            + "{\"match\": {\"airport_code.keyword\": {\"query\": \"?0\", \"boost\": 3}}},"
//            + "{\"match\": {\"city_code.keyword\": {\"query\": \"?0\", \"boost\": 3}}},"
//            + "{\"match_phrase\": {\"airport_name\": {\"query\": \"?0\", \"boost\": 2}}},"
//            + "{\"match_phrase\": {\"city_name\": {\"query\": \"?0\", \"boost\": 1.5}}}"
//            + "], \"minimum_should_match\": 1}}")
//    Page<SearchAirportDocument> findAll(String searchText, Pageable pageable);
//}