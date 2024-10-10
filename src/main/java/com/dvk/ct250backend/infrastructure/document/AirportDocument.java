package com.dvk.ct250backend.infrastructure.document;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "airports")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportDocument {

    @Id
    @Field(type = FieldType.Integer)
    Integer airportId;

    @Field(type = FieldType.Text)
    String airportName;

    @Field(type = FieldType.Keyword)
    String airportCode;

    @Field(type = FieldType.Text)
    String cityName;

    @Field(type = FieldType.Keyword)
    String cityCode;
}
