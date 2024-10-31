package com.dvk.ct250backend.domain.transaction.mapper;

import com.dvk.ct250backend.domain.transaction.dto.TransactionDTO;
import com.dvk.ct250backend.domain.transaction.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toTransactionDTO(Transaction transaction);
    Transaction toTransaction(TransactionDTO transactionDTO);
    void updateTransactionFromDTO(@MappingTarget Transaction transaction, TransactionDTO transactionDTO);
}
