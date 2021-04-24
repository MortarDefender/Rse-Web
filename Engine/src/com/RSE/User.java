package com.RSE;

import ExceptionsType.ExpType;
import objects.dto.AnswerDto;
import objects.dto.UserDTO;
import objects.dto.TransactionDTO;
import com.RSE.interfaces.UserInter;
import com.RSE.interfaces.StockInter;
import objects.interfaces.CommandAnswer;
import objects.interfaces.Dto;
import objects.interfaces.UserInterDto;
import com.RSE.interfaces.TransactionInter;

import java.util.*;
import java.time.Instant;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicInteger;

public class User implements Serializable, UserInter {
    private final boolean type;    // stock broker == true || admin == false
    private final String username;
    private final AtomicInteger account;
    private Map<String, AtomicInteger> stocks;
    private final List<TransactionInter> transactions;

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

    @Override
    public boolean getType() { return type; }

    @Override
    public int getAccount() { return account.get(); }

    @Override
    public String getUsername() { return username; }

    @Override
    public Set<String> getStocks() { return this.stocks.keySet(); }

    @Override
    public String getTypeString() { return type ? "Stock Broker" : "Admin"; }

    @Override
    public int getTotalRevolution(Map<String, StockInter> rseStocks) { // calculate the total revolution of the user using the rseStock given
        return this.stocks.keySet().stream().mapToInt(sym -> this.stocks.get(sym).get() * rseStocks.get(sym).getRate()).sum();
    }

    @Override
    public Map<String, Integer> getStocksDTO() {
        /* return a map String to Integer that indicate on how many stock the user have and which stock he has */
        Map<String, Integer> l = new HashMap<>();
        this.stocks.forEach((k, v) -> l.put(k, v.get()));
        return l;
    }

    @Override
    public int getStockQuantity(String symbol) {
        if (this.stocks.containsKey(symbol))
            return this.stocks.get(symbol).get();
        return 0;
    }

    public List<TransactionInter> getTransactions() { return this.transactions; }

    @Override
    public List<TransactionDTO> getTransactionsDTO() {
        /* return a list of transaction Dto  */
        List<TransactionDTO> l = new ArrayList<>();
        this.transactions.forEach(transaction -> l.add(transaction.getDto()));
        return l;
    }

    @Override
    public UserDTO getDto(Map<String, StockInter> rseStocks) { return new UserDTO(username, getTypeString(), account.get(), getStocksDTO(), getTransactionsDTO(), getTotalRevolution(rseStocks)); }

    @Override
    public UserInterDto getInterDto(Map<String, StockInter> rseStocks) { return new UserDTO(username, getTypeString(), account.get(), getStocksDTO(), getTransactionsDTO(), getTotalRevolution(rseStocks)); }

    public void increaseAccount(int amount) { this.account .addAndGet(amount); }

    public void decreaseAccount(int amount) { this.account.addAndGet(-amount); }

    @Override
    public void addTransaction(TransactionInter transaction) { this.transactions.add(transaction); }

    @Override
    public CommandAnswer<Dto, String> addAllStocks(Map<String, Integer> stocks, boolean flag) {
        /* adds all the stocks given, if the flag is true than when the stocks are invalid return to the previous stocks */
        Map<String, AtomicInteger> stocksBackup = new HashMap<>(this.stocks);
        if (flag)
            this.stocks.clear();

        for (String name: stocks.keySet()) {
            CommandAnswer<Dto, String> ans = this.addStock(name, stocks.get(name));
            if (!ans.isSuccessful()) {
                if (flag)
                    this.stocks = stocksBackup;
                return ans;
            }
        }
        return new AnswerDto<>();
    }

    @Override
    public CommandAnswer<Dto, String> addStock(String stockName, int amount) {
        if (this.stocks.containsKey(stockName))
            return new AnswerDto<>(null, "The user '" + this.username + "' has the stock named '" + stockName + "' already", ExpType.StockSymbolDuplication);
        this.stocks.put(stockName, new AtomicInteger(amount));
        return new AnswerDto<>();
    }

    @Override
    public CommandAnswer<Dto, String> createTransactionEffect(boolean action, boolean sender, int sum, int amount, String time, Instant timeStamp, String stockName) {
        /* according to the action and the sender variables the actions will be to add the new transaction, decrease or increase the account and change the amount of stocks */
        if (!this.stocks.containsKey(stockName))
            return new AnswerDto<>(null, username + " does not have any stock of " + stockName, ExpType.StockNotFound);
        if ((action && sender) || (!action && !sender)) {
            addTransaction(new Transaction(0, -sum, account.get(), time, timeStamp, stockName));
            this.account.addAndGet(-sum);
            this.stocks.get(stockName).addAndGet(amount);
        } else {
            addTransaction(new Transaction(1, sum, account.get(), time, timeStamp, stockName));
            this.account.addAndGet(sum);
            this.stocks.get(stockName).addAndGet(-amount);
        }
        return new AnswerDto<>();
    }

    @Override
    public void addMoneyCharge(int amount) {
        // if (amount <= 0)   // amount needs to be positive ??
        this.transactions.add(new Transaction(amount, account.get()));
        this.account.addAndGet(amount);
    }

    @Override
    public boolean checkStock(String stockName) { return this.stocks.containsKey(stockName); }

    @Override
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
        for (TransactionInter transaction : this.transactions)
            res = res.concat("\t" + transaction);
        return res;
    }
}
