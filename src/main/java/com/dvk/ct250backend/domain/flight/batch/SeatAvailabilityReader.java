package com.dvk.ct250backend.domain.flight.batch;

import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.entity.Seat;
import com.dvk.ct250backend.domain.flight.entity.SeatAvailability;
import com.dvk.ct250backend.domain.flight.enums.SeatAvailabilityStatus;
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

public class SeatAvailabilityReader implements ItemReader<SeatAvailability>, ItemStream {
    private final FlatFileItemReader<SeatAvailability> delegate;

    public SeatAvailabilityReader(Resource resource) {
        this.delegate = new FlatFileItemReader<>();
        this.delegate.setResource(resource);
        this.delegate.setLineMapper(seatAvailabilityLineMapper());
        this.delegate.setLinesToSkip(1);
    }

    @Override
    public SeatAvailability read() throws Exception {
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

    private LineMapper<SeatAvailability> seatAvailabilityLineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("flight_id", "seat_id", "status");
        DefaultLineMapper<SeatAvailability> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(seatAvailabilityFieldSetMapper());
        return lineMapper;
    }

    private FieldSetMapper<SeatAvailability> seatAvailabilityFieldSetMapper() {

        return fieldSet -> SeatAvailability.builder()
                .flight(Flight.builder().flightId(fieldSet.readString("flight_id")).build())
                .seat(Seat.builder().seatId(fieldSet.readInt("seat_id")).build())
                .status(SeatAvailabilityStatus.valueOf(fieldSet.readString("status")))
                .build();
    }
}
