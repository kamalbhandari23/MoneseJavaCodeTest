package com.moneseapp.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moneseapp.model.Account;

@Repository
public interface AccountsRepository extends CrudRepository<Account,Long> {}
