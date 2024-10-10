package com.dvk.ct250backend.infrastructure.elasticsearch.document;

import com.dvk.ct250backend.domain.country.entity.Country;
import com.dvk.ct250backend.domain.flight.entity.Route;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Set;

@Document(indexName = "airports")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportDocument {

    @Id
    @Field(type = FieldType.Integer, name = "airport_id")
    Integer airportId;

    @Field(type = FieldType.Text, name = "airport_name")
    String airportName;

    @Field(type = FieldType.Keyword, name = "airport_code")
    String airportCode;

    @Field(type = FieldType.Text, name = "city_name")
    String cityName;

    @Field(type = FieldType.Keyword, name = "city_code")
    String cityCode;


//    @Field(type = FieldType.Long, name = "country_id")
//    Long countryId;
//
//    @Field(type = FieldType.Date, name = "created_at")
//    String createdAt;
}