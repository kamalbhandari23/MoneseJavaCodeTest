package com.moneseapp.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.moneseapp.exception.CustomException;
import com.moneseapp.model.Account;
import com.moneseapp.model.FundsTransfer;
import com.moneseapp.model.Statement;
import com.moneseapp.service.AccountService;
import com.moneseapp.service.StatementService;

/**
 * @author Kamal Bhandari
 * 
 * The controller class for this application.
 * <p>
 * This class exposes two rest web services to transfer funds between two accounts and to get the account statement.
 *
 */
@RestController
@RequestMapping("/api")
public class MoneseAppController {
	@Autowired
	private AccountService accountService;
	@Autowired
	private StatementService statementService;

	@PostMapping("/account/transfer")
	public ResponseEntity<?> transferFunds(@Valid @RequestBody FundsTransfer amount, UriComponentsBuilder ucBuilder) {
		try {
			Account debitAccount = accountService.transferFunds(amount.getFromAccountId(), amount.getToAccountId(), amount.getAmount());
			HttpHeaders headers = new HttpHeaders();			
			headers.setLocation(ucBuilder.path("api/account/{id}/statement").buildAndExpand(debitAccount.getId()).toUri());
			return new ResponseEntity<>(headers, HttpStatus.CREATED);
		} catch (CustomException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/account/{id}/statement")
	public Statement getStatement(@PathVariable("id") long id) {
		try {
			return statementService.getAccountStatement(id);
		} catch (CustomException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
