package com.moneseapp.integratedtests;

import static org.junit.Assert.assertTrue;	
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneseapp.exception.CustomException;
import com.moneseapp.model.Account;
import com.moneseapp.model.FundsTransfer;
import com.moneseapp.model.Statement;
import com.moneseapp.service.AccountService;
import com.moneseapp.service.StatementService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MoneseAppTest {

	@Autowired
	AccountService accountService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	StatementService StatementService;

	@Test
	public void postTransfer_verifyBalance() {
		Account debitAccount = accountService.transferFunds(10l, 20l, BigDecimal.valueOf(10000));
		Account creditAccount = accountService.getAccountById(20l);
		assertTrue(debitAccount.getBalance().compareTo(BigDecimal.valueOf(10000)) == 0);
		assertTrue(creditAccount.getBalance().compareTo(BigDecimal.valueOf(40000)) == 0);
	}

	@Test(expected = CustomException.class)
	public void fundTransfer_invalidAmount() {

		accountService.transferFunds(10l, 20l, BigDecimal.valueOf(-10000));
	}

	@Test
	public void fundTransfer_unknownFromAccount() {
		try {
			accountService.transferFunds(100l, 20l, BigDecimal.valueOf(10000));
		} catch (CustomException e) {
			assertTrue(e.getMessage().equals("No account associated with this id"));
		}
	}

	@Test
	public void fundTransfer_unknownToAccount() {
		try {
			accountService.transferFunds(10l, 30l, BigDecimal.valueOf(10000));
		} catch (CustomException e) {
			assertTrue(e.getMessage().equals("No account associated with this id"));
		}
	}

	@Test
	public void fundTransfer_insufficientFunds() {
		try {
			accountService.transferFunds(10l, 20l, BigDecimal.valueOf(100000));
		} catch (CustomException e) {
			assertTrue(e.getMessage().equals("Insufficient Funds"));
		}
	}

	@Test
	public void fundTransfer_restricted() {
		try {
			accountService.transferFunds(10l, 10l, BigDecimal.valueOf(100000));
		} catch (CustomException e) {
			assertTrue(e.getMessage().equals("Fund transfer to same account not permitted"));
		}
	}

	@Test
	public void testGetStatement_whenValidAccount() {
		Statement statement = StatementService.getAccountStatement(10l);
		Assert.assertNotNull(statement);
	}

	@Test(expected = CustomException.class)
	public void testGetStatement_whenInValidAccount() {
		Statement statement = StatementService.getAccountStatement(-10l);
	}

	@Test
	public void testTransferFunds() throws Exception {
		FundsTransfer fundsTransfer = new FundsTransfer();
		fundsTransfer.setAmount(BigDecimal.valueOf(2000));
		fundsTransfer.setFromAccountId(10l);
		fundsTransfer.setToAccountId(20l);
		mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/account/transfer")
				.content(asJsonString(fundsTransfer)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost:8080/api/account/10/statement"));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
