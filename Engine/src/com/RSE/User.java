package com.RSE;

import objects.dto.UserDTO;
import objects.dto.TransactionDTO;

import java.time.Instant;
import java.util.*;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicInteger;

public class User implements Serializable {
    private final boolean type;    // stock broker == true || admin == false
    private final String username;
    private final AtomicInteger account;
    private Map<String, AtomicInteger> stocks;
    private final ArrayList<Transaction> transactions;

    public User(String name, boolean type) {
        this(name, type, 0);
    }

    public User(String name, boolean type, int account) {
        this.type = type;
        this.username = name;
        this.stocks = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.account = new AtomicInteger(account);
    }

    public boolean getType() { return type; }
    public int getAccount() { return account.get(); }
    public String getUsername() { return username; }
    public String getTypeString() { return type ? "Stock Broker" : "Admin"; }

    public int getTotalRevolution(Map<String, Stock> rseStocks) {
        return this.stocks.keySet().stream().mapToInt(sym -> this.stocks.get(sym).get() * rseStocks.get(sym).getRate()).sum();
    }

    public Set<String> getStocks() { return this.stocks.keySet(); }

    public Map<String, Integer> getStocksDTO() {
        Map<String, Integer> l = new HashMap<>();
        this.stocks.forEach((k, v) -> l.put(k, v.get()));
        return l;
    }

    public int getStockQuantity(String symbol) {
        if (this.stocks.containsKey(symbol))
            return this.stocks.get(symbol).get();
        return 0;
    }

    public List<Transaction> getTransactions() { return this.transactions; }

    public List<TransactionDTO> getTransactionsDTO() {
        List<TransactionDTO> l = new ArrayList<>();
        this.transactions.forEach(transaction -> l.add(transaction.getDto()));
        return l;
    }

    public UserDTO getDto(Map<String, Stock> rseStocks) { return new UserDTO(username, getTypeString(), account.get(), getStocksDTO(), getTransactionsDTO(), getTotalRevolution(rseStocks)); }

    public void increaseAccount(int amount) { this.account .addAndGet(amount); }

    public void decreaseAccount(int amount) { this.account.addAndGet(-amount); }

    public void addTransaction(Transaction transaction) { this.transactions.add(transaction); }

    public void addAllStocks(Map<String, Integer> stocks, boolean flag) {
        Map<String, AtomicInteger> stocksBackup = new HashMap<>(this.stocks);
        if (flag)
            this.stocks.clear();

        stocks.keySet().forEach(name -> {
            try {
                this.addStock(name, stocks.get(name));
            } catch (InvalidParameterException e) {
                if (flag) {
                    this.stocks = stocksBackup;
                    throw e;
                }
            }
        });
    }

    public void addStock(String stockName, int amount) {
        if (this.stocks.containsKey(stockName))
            throw new InvalidParameterException("The user '" + this.username + "' has the stock named '" + stockName + "' already");
        this.stocks.put(stockName, new AtomicInteger(amount));
    }

    public void increaseStockAmount(String stockName, int amount) {
        if (this.stocks.containsKey(stockName))
            this.stocks.get(stockName).addAndGet(amount);
        else
            this.addStock(stockName, amount);
    }

    public void decreaseStockAmount(String stockName, int amount) {
        if (this.stocks.containsKey(stockName))
            this.stocks.get(stockName).addAndGet(-amount);
        else
            throw new InvalidParameterException(username + " does not have any stock of " + stockName);
    }

    public void createTransactionEffect(boolean action, boolean sender, int sum, int amount, String time, Instant timeStamp, String stockName) {
        if ((action && sender) || (!action && !sender)) {
            addTransaction(new Transaction(0, -sum, account.get(), time, timeStamp, stockName));
            this.account .addAndGet(-sum);
            increaseStockAmount(stockName, amount);
        } else {
            addTransaction(new Transaction(1, sum, account.get(), time, timeStamp, stockName));
            this.account .addAndGet(sum);
            decreaseStockAmount(stockName, amount);
        }
    }

    public void addMoneyCharge(int amount) {
        // if (amount <= 0)   // amount needs to be positive ??
        this.transactions.add(new Transaction(amount, account.get()));
        this.account.addAndGet(amount);
    }

    public boolean checkStock(String stockName) { return this.stocks.containsKey(stockName); }

    public int quantityOfStock(String stockName) throws InvalidParameterException {
        if (this.stocks.containsKey(stockName))
            return this.stocks.get(stockName).get();
        return 0;
    }

    @Override
    public String toString() {
        String res = "Username: " + username + "\nUser Type: " + (type ? "Dealer" : "Admin") + "\nAccount Balance: " + account + "\nStocks:\n";
        if (this.stocks.size() == 0)
            res = res.concat("\tThere is no stocks to show\n");
        for (String key: this.stocks.keySet())
            res = res.concat("\tStock Name: " + key + "\tAmount: " + this.stocks.get(key).get() + "\n");
        res = res.concat("Transactions: \n");
        if (this.transactions.size() == 0)
            res = res.concat("\tThere is no transactions to show\n");
        for (Transaction transaction : this.transactions)
            res = res.concat("\t" + transaction);
        return res;
    }
}
