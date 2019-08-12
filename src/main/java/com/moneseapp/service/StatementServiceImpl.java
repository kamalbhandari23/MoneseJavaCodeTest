package com.moneseapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moneseapp.exception.CustomException;
import com.moneseapp.model.Account;
import com.moneseapp.model.Statement;
@Service
public class StatementServiceImpl implements StatementService {
	
	@Autowired
	AccountServiceImpl accountService;
	
	/**
     * {@inheritDoc}
     */
	@Override
	public Statement getAccountStatement(long id) throws CustomException
	{
		Account account = accountService.getAccountById(id);
		Statement statement = new Statement();		
		statement.setAccountId(account.getId());
		statement.setBalance(account.getBalance());
		statement.setTransactions(account.getTransactions());
		return statement;		
	}

}
