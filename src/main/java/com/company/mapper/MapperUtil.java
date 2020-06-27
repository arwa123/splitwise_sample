package com.company.mapper;

import com.company.dto.EqualExpenseDto;
import com.company.dto.ExactExpenseDto;
import com.company.dto.PercentExpenseDto;
import com.company.entity.Expense;
import com.company.enums.ExpenseType;
import com.company.enums.TransactionType;

public class MapperUtil {

    public static Expense BuildExpense(ExactExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setAmount(expenseDto.getAmount());
        expense.setType(ExpenseType.EXACT);
        expense.setUserId(expenseDto.getFromUserId());
        expense.setToUserIds(expenseDto.getToUserId().toString());
        return expense;
    }

    public static Expense BuildExpense(PercentExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setAmount(expenseDto.getAmount());
        expense.setType(ExpenseType.PERCENTAGE);
        expense.setUserId(expenseDto.getFromUserId());
        expense.setToUserIds(expenseDto.getToUserId().toString());
        return expense;
    }

    public static Expense BuildExpense(EqualExpenseDto expenseDto) {

        Expense expense = new Expense();
        expense.setAmount(expenseDto.getAmount());
        expense.setType(ExpenseType.EQUAL);
        expense.setUserId(expenseDto.getFromUserId());
        expense.setToUserIds(expenseDto.getToUserIds());
        return expense;
    }
}
