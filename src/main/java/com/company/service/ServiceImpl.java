package com.company.service;

import com.company.dao.ExpenseRepository;
import com.company.dao.GroupRepository;
import com.company.dao.TransactionRepository;
import com.company.dao.UserRepository;
import com.company.dto.EqualExpenseDto;
import com.company.dto.ExactExpenseDto;
import com.company.dto.PaymentDto;
import com.company.dto.PercentExpenseDto;
import com.company.entity.Expense;
import com.company.entity.Group;
import com.company.entity.Transaction;
import com.company.entity.User;
import com.company.enums.TransactionType;
import com.company.mapper.MapperUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupRepository groupRepository;


    @Autowired
    private TransactionRepository transactionRepository;

    public Expense addEqualExpense(EqualExpenseDto expenseDto) {
        Expense expense = expenseRepository.save(MapperUtil.BuildExpense(expenseDto));
        List<String> userIds = Arrays.asList(expense.getToUserIds().split(","));
        Float amount = expense.getAmount();
        Float sharedAmount = amount / (userIds.size() + 1);
        List<Transaction> transactions = Lists.newArrayList();
        userIds.stream().forEach(userId -> {
            Long uId = Long.valueOf(userId);
            addDebitTransaction(expense, sharedAmount, uId);
            debitNetBalance(uId, sharedAmount);
            transactions.add(addDebitTransaction(expense, sharedAmount, uId));
        });

        creditNetBalance(expense.getUserId(), sharedAmount*(userIds.size()));
        transactionRepository.saveAll(transactions);

        return  expense;
    }

    public Expense addPercentExpense(PercentExpenseDto expenseDto) {
        Expense expense = expenseRepository.save(MapperUtil.BuildExpense(expenseDto));
        Long toUserId = expenseDto.getToUserId();
        Float amount = (expenseDto.getPercentage()/100)*expenseDto.getAmount();
        Transaction transaction = addDebitTransaction(expense, amount, toUserId);
        transactionRepository.save(transaction);
        debitNetBalance(toUserId, amount);
        creditNetBalance(expense.getUserId(), amount);

        return  expense;
    }

    public Expense addExactExpense(ExactExpenseDto expenseDto) {
        Expense expense = expenseRepository.save(MapperUtil.BuildExpense(expenseDto));
        Long toUserId = expenseDto.getToUserId();
        Transaction transaction = addDebitTransaction(expense, expense.getAmount(), toUserId);
        transactionRepository.save(transaction);
        debitNetBalance(toUserId, expense.getAmount());
        creditNetBalance(expense.getUserId(), expense.getAmount());
        return  expense;
    }


    public void addPayment(PaymentDto payment) {

        Transaction transaction = new Transaction();
        transaction.setAmount(payment.getAmount());
        transaction.setFromUserId(payment.getFromUserId());
        transaction.setToUserId(payment.getToUserId());
        transaction.setType(TransactionType.credit);
        transactionRepository.save(transaction);
        creditNetBalance(payment.getFromUserId(), payment.getAmount());
        debitNetBalance(payment.getToUserId(), payment.getAmount());
    }


    private void debitNetBalance(Long userId, Float amount) {
        User user = userRepository.findById(Long.valueOf(userId)).get();
        user.setNetBalance(user.getNetBalance() - amount);
        userRepository.save(user);
    }

    private void creditNetBalance(Long userId, Float amount) {
        User user = userRepository.findById(userId).get();
        user.setNetBalance(user.getNetBalance() + amount);
        userRepository.save(user);
    }

    private Transaction addDebitTransaction(Expense expense, Float amount, Long toUserId) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setFromUserId(expense.getUserId());
        transaction.setToUserId(toUserId);
        transaction.setType(TransactionType.debit);
       return transaction;
    }


    public List<String> simplifyDebts(Long userId){

        User user = userRepository.findById(userId).get();
        Group group = groupRepository.findById(1L).get();
        Float netAmount = user.getNetBalance();
        if(netAmount != 0) {
            TransactionType type = getTransactionType(netAmount);
            List<Long> userIds = Arrays.asList(group.getUserIds().trim().split(","))
                    .stream().distinct().map(str -> Long.valueOf(str))
                    .collect(Collectors.toList());
            switch (type){

                case debit:
                    List<User> creditUsers = userRepository.findAllById(userIds).stream()
.filter(u -> u.getNetBalance() != 0 && getTransactionType(u.getNetBalance())== TransactionType.credit).collect(Collectors.toList());
                    List<String> list = Lists.newArrayList();
                    list.add(user.getName() +" netAmount -> "+netAmount);
                    Float subAmount = netAmount;
                    for(User u : creditUsers){
                        Float amountTransferred = Math.min(u.getNetBalance(),Math.abs(subAmount));
                        subAmount -= amountTransferred;
                        list.add(user.getName()+" to "+u.getName()+" ->  "+amountTransferred);
                        if(subAmount == 0)
                            return list;

                    }
                    return list;
                case credit:
                    List<User> debitUsers = userRepository.findAllById(userIds).stream()
.filter(u -> u.getNetBalance() != 0 && getTransactionType(u.getNetBalance())== TransactionType.debit)
                            .collect(Collectors.toList());
                    List<String> list2 = Lists.newArrayList();
                    list2.add(user.getName() +" netAmount -> "+netAmount);
                    Float subAmount2 = netAmount;
                    for(User u : debitUsers){
                        Float amountTransferred2 = Math.min(Math.abs(u.getNetBalance()), subAmount2);
                        subAmount2 -=  amountTransferred2;
                        list2.add(u.getName()+" to "+user.getName()+" ->  "+amountTransferred2);
                        if(subAmount2 == 0)
                            return list2;

                    }
                    return list2;
                default:
                    return null;
            }


        }else
            return Arrays.asList("All Settled");
    }


    public TransactionType getTransactionType(Float netAmount){
        return netAmount > 0 ? TransactionType.credit : TransactionType.debit;
    }



}
