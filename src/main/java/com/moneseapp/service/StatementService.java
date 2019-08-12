package com.moneseapp.service;

import org.springframework.stereotype.Service;

import com.moneseapp.exception.CustomException;
import com.moneseapp.model.Statement;
/**
* Statement service class to get the .
* <p>
* This class provide feature to get the statement of requested account. 
*/
@Service
public interface StatementService {

	/**
	 * This method returns the statement for the requested account
	 * @param id the account id for which statement is requested
	 * @return Statement object containing statement details
	 */
	Statement getAccountStatement(long id) throws CustomException;
}
