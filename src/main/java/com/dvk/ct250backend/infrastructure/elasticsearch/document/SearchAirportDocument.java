package com.dvk.ct250backend.infrastructure.elasticsearch.document;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "airports")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchAirportDocument {

    @Id
    @Field(type = FieldType.Integer, name = "airport_id")
    Integer airportId;

    @Field(type = FieldType.Text, name = "airport_name", analyzer = "autocomplete")
    String airportName;

    @Field(type = FieldType.Keyword, name = "airport_code")
    String airportCode;

    @Field(type = FieldType.Text, name = "city_name", analyzer = "autocomplete")
    String cityName;

    @Field(type = FieldType.Keyword, name = "city_code")
    String cityCode;

    @Field( name = "created_at", type = FieldType.Date, format = DateFormat.strict_date_optional_time_nanos)
    LocalDateTime createdAt;

    @Field(name = "created_by")
    String createdBy;

    @Field( name = "updated_at", type = FieldType.Date, format = DateFormat.strict_date_optional_time_nanos)
    LocalDateTime updatedAt;

    @Field(name = "updated_by")
    String updatedBy;

    @Field(type = FieldType.Integer, name = "country_id")
    Integer countryId;

    @Field(type = FieldType.Text, name = "country_name")
    String countryName;

    @Field(type = FieldType.Integer, name = "country_code")
    Integer countryCode;

    @Field(type = FieldType.Keyword, name = "iso2_code")
    String iso2Code;

    @Field(type = FieldType.Keyword, name = "iso3_code")
    String iso3Code;
}