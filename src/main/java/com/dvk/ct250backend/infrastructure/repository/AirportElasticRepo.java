package com.dvk.ct250backend.infrastructure.repository;



import com.dvk.ct250backend.domain.flight.entity.Airport;
import com.dvk.ct250backend.infrastructure.document.AirportDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportElasticRepo extends ElasticsearchRepository<AirportDocument, Integer> {

    List<AirportDocument> findByAirportName(String name);
}