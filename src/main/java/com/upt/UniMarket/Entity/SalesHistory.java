package com.upt.UniMarket.Entity;

import java.util.ArrayList;
import java.util.List;

public class SalesHistory {
    private List<Transaction> transactions = new ArrayList<>();

    public SalesHistory() {}

    public SalesHistory(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}