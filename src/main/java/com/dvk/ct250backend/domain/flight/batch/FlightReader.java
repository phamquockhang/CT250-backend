package com.dvk.ct250backend.domain.flight.batch;


import com.dvk.ct250backend.domain.flight.entity.Airplane;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.entity.Route;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlightReader implements ItemReader<Flight>, ItemStream {
    private final FlatFileItemReader<Flight> delegate;


    public FlightReader(Resource resource) {
        this.delegate = new FlatFileItemReader<>();
        this.delegate.setResource(resource);
        this.delegate.setLineMapper(flightLineMapper());
        this.delegate.setLinesToSkip(1);
    }

    @Override
    public Flight read() throws Exception {
        return delegate.read();
    }

    @Override
    public void open(@NonNull ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }

    private LineMapper<Flight> flightLineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("flight_id", "route_id", "departure_date_time",
                "arrival_date_time", "airplane_id");
        DefaultLineMapper<Flight> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(flightFieldSetMapper());
        return lineMapper;
    }

    private FieldSetMapper<Flight> flightFieldSetMapper() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fieldSet -> Flight.builder()
                .flightId(fieldSet.readString("flight_id"))
                .route(Route.builder().routeId(fieldSet.readInt("route_id")).build())
                .departureDateTime(LocalDateTime.parse(fieldSet.readString("departure_date_time"), dateTimeFormatter))
                .arrivalDateTime(LocalDateTime.parse(fieldSet.readString("arrival_date_time"), dateTimeFormatter))
                .airplane(Airplane.builder().airplaneId(fieldSet.readInt("airplane_id")).build())
                .build();
    }
}
