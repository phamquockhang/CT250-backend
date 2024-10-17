package com.dvk.ct250backend.domain.flight.batch;

import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.entity.FlightPricing;
import com.dvk.ct250backend.domain.flight.entity.TicketClass;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class FlightPricingReader implements ItemReader<FlightPricing>, ItemStream {
    private final FlatFileItemReader<FlightPricing> delegate;


    public FlightPricingReader(Resource resource) {
        this.delegate = new FlatFileItemReader<>();
        this.delegate.setResource(resource);
        this.delegate.setLineMapper(flightPricingLineMapper());
        this.delegate.setLinesToSkip(1);
    }

    @Override
    public FlightPricing read() throws Exception {
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

    private LineMapper<FlightPricing> flightPricingLineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("flight_id", "ticket_class_id", "ticket_price", "valid_from", "valid_to");
        DefaultLineMapper<FlightPricing> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(flightPricingFieldSetMapper());
        return lineMapper;
    }

    private FieldSetMapper<FlightPricing> flightPricingFieldSetMapper() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fieldSet -> FlightPricing.builder()
                .flight(Flight.builder().flightId(fieldSet.readString("flight_id")).build())
                //.ticketClass(TicketClassEnum.valueOf(fieldSet.readString("ticket_class")))
                .ticketClass(TicketClass.builder().ticketClassId(fieldSet.readInt("ticket_class_id")).build())
                .ticketPrice(fieldSet.readDouble("ticket_price"))
                .validFrom(LocalDate.parse(fieldSet.readString("valid_from"), dateTimeFormatter))
                .validTo(LocalDate.parse(fieldSet.readString("valid_to"), dateTimeFormatter))
                .build();
    }
}
