package com.maxwell.picpay_desafio_backend.transaction;

import org.springframework.data.repository.ListCrudRepository;

public interface TransactionRepository  extends ListCrudRepository<Transaction, Long> {
}
