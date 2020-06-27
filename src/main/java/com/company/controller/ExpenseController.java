package com.company.controller;

import javax.validation.Valid;
import com.company.dao.GroupRepository;
import com.company.dao.UserRepository;
import com.company.dto.EqualExpenseDto;
import com.company.dto.ExactExpenseDto;
import com.company.dto.PaymentDto;
import com.company.dto.PercentExpenseDto;
import com.company.entity.Expense;
import com.company.entity.Group;
import com.company.entity.User;
import com.company.enums.TransactionType;
import com.company.service.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class ExpenseController
{

   
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	ServiceImpl expenseService;



	@PostMapping(path = "/users")
	public User addUser(@Valid @RequestBody User user) {

		return userRepository.save(user);

	}

	@PostMapping(path = "/groups")
	public Group addGroup(@Valid @RequestBody Group group) {

		return groupRepository.save(group);

	}

	@PostMapping(path = "/expenses/equal")
	public Expense addEqualExpense(@Valid @RequestBody EqualExpenseDto expense) {

		return expenseService.addEqualExpense(expense);

	}

	@PostMapping(path = "/expenses/percent")
	public Expense addPercentExpense(@Valid @RequestBody PercentExpenseDto expense) {

		return expenseService.addPercentExpense(expense);

	}

	@PostMapping(path = "/expenses/exact")
	public Expense addExactExpense(@Valid @RequestBody ExactExpenseDto expense) {

		return expenseService.addExactExpense(expense);

	}


	@PostMapping(path = "/payments")
	public String addPayment(@Valid @RequestBody PaymentDto payment) {

		expenseService.addPayment(payment);

		return "success";

	}

	@GetMapping(path = "/user/{user_id}/balance")
	public List<String> getBalance(@PathVariable(value = "user_id") Long userId) {

		return expenseService.simplifyDebts(userId);

	}


}
