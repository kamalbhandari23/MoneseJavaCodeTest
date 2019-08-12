package com.moneseapp.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moneseapp.exception.CustomException;
import com.moneseapp.model.Account;
/**
 * @author Kamal Bhandari
 * Account service class to perform account related operations.
 * <p>
 * This class provide features like money deposit,withdraw,getting all the accounts. 
 * deposits withdrawals and account transfers.
 * <p>
 * Currently only one method transferFunds is exposed via REST services.
 */
@Service
@Transactional
public interface AccountService {
	
	public static final String TRANSACTION_TYPE_CREDIT = "Credit";
    public static final String TRANSACTION_TYPE__DEBIT = "Debit";
   
	/**
	 * This method adds the required amount to the account balance
	 * @param id the account id where money amount is to be deposited
	 * @param amount the amount to be deposited.
	 * @return The Account object with updated balance.
	 */
	public Account deposit(long id, BigDecimal amount);

	/**
	 * This method subtracts required amount from the account balance
	 * @param id the account id from where money amount is to be withdrawn
	 * @param amount the amount to be withdrawn.
	 * @return The Account object with updated balance.
	 */
	public Account withdraw(long id, BigDecimal amount);

	/**
	 * This method transfer the amount from one account to another.
	 * @param fromAccountId the account from where the required amount is to be debit.
	 * @param toAccountId the account where the required amount is to be credit.
	 * @param amount the amount to be transfer.
	 * @return Account the updated account from where the money is debited.
	 */
	public Account transferFunds(long fromAccountId, long toAccountId, BigDecimal amount);

	/**
	 * This method returns the account details for a required account id. 
	 * @param id account id
	 * @return Account details of required account
	 * @throws CustomException
	 */
	public Account getAccountById(long id) throws CustomException;

}
