package com.moneseapp.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.moneseapp.exception.CustomException;
import com.moneseapp.model.Account;
import com.moneseapp.model.Transaction;
import com.moneseapp.repositories.AccountsRepository;
import com.moneseapp.repositories.TransactionRepository;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	@Autowired
	AccountsRepository accountRepository;
	@Autowired
	TransactionRepository transactionRepository;
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	/**
     * {@inheritDoc}
     */
	@Override
	public synchronized Account deposit(long id, BigDecimal amount) {
		Account account = getAccountById(id);		
		try {
			account.setBalance(account.getBalance().add(amount));
			account = accountRepository.save(account);
			logger.info("Amount {} credited to account id {}", amount, account.getId());
			Transaction transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setAmount(amount);
			transaction.setBalance(account.getBalance());
			transaction.setTransactionDate(LocalDate.now());
			transaction.setTransactionType(TRANSACTION_TYPE_CREDIT);
			transactionRepository.save(transaction);
		} catch (Exception e) {
			logger.error("deposit: exception occured during deposit :", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());			 
		}
		return account;
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public synchronized Account withdraw(long id, BigDecimal amount) {
		Account account = getAccountById(id);
		BigDecimal accountBalance = account.getBalance();
		if (accountBalance == null || accountBalance.compareTo(amount) < 0) {
			throw new CustomException("Insufficient Funds");
		}
		try {						
			account.setBalance(account.getBalance().subtract(amount));
			account = accountRepository.save(account);
			logger.info("Amount {} debited from account id {}", amount, account.getId());
			Transaction transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setAmount(amount.negate());
			transaction.setBalance(account.getBalance());
			transaction.setTransactionDate(LocalDate.now());
			transaction.setTransactionType(TRANSACTION_TYPE__DEBIT);
			transactionRepository.save(transaction);
		} catch (Exception e) {
			logger.error("withdraw: exception occured during withdrawl :", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return account;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public Account transferFunds(long fromAccountId, long toAccountId, BigDecimal amount) {
		validateFields(fromAccountId, toAccountId, amount);
		Account account = withdraw(fromAccountId, amount);
		deposit(toAccountId, amount);
		return account;
	}
	/**
     * {@inheritDoc}
     */
	@Override
	public Account getAccountById(long id) throws CustomException {
		Optional<Account> findById = accountRepository.findById(id);
		if (findById.isPresent()) {
			return findById.get();
		} else {
			throw new CustomException("No account associated with this id");
		}
	}

	private void validateFields(long fromAccountId, long toAccountId, BigDecimal amount) {
		if (amount == null && fromAccountId <= 0 && toAccountId <= 0) {
			throw new CustomException("Empty request body");
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new CustomException("Invalid  amount");
		}
		if (fromAccountId > 0 && toAccountId > 0) {
			if (fromAccountId == toAccountId) {
				throw new CustomException("Fund transfer to same account not permitted");
			}
		}
		if (fromAccountId <= 0) {
			throw new CustomException("Invalid from Account id");
		}
		if (toAccountId <= 0) {
			throw new CustomException("Invalid to Account id");
		}
	}

}
