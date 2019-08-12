package com.moneseapp.repositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moneseapp.model.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long>{}
