package com.RSE;

import objects.dto.TransactionDTO;
import com.sun.istack.internal.NotNull;
import com.RSE.interfaces.TransactionInter;
import objects.interfaces.TransactionInterDto;

import java.util.Date;
import java.time.Instant;
import java.time.Duration;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Transaction implements Serializable, TransactionInter {  // Comparable<Transaction>,
    private final Instant timeStamp;
    private final String symbol, time;
    private final int transactionFee, accountBalance, actionType;      // 0 == Buy || 1 == Sell || 2 = Self Charge

    public Transaction(int transactionFee, int accountBalance) {
        this(2, transactionFee, accountBalance, new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()), Instant.now(), "--");
    }

    public Transaction(int actionType, int transactionFee, int accountBalance, String time, Instant timeStamp, String symbol) {
        this.time = time;
        this.symbol = symbol;
        this.timeStamp = timeStamp;
        this.actionType = actionType;
        this.transactionFee = transactionFee;
        this.accountBalance = accountBalance;
    }

    @Override
    public String getAction() {
        if (this.actionType == 0)
            return "Buy";
        if (this.actionType == 1)
            return "Sell";
        return "Self Charge";
    }

    @Override
    public String getTime() { return this.time; }

    @Override
    public String getSymbol() { return this.symbol; }

    @Override
    public int getSum() { return this.transactionFee; }

    @Override
    public int getAccount() { return this.accountBalance; }

    @Override
    public Instant getTimeStamp() { return this.timeStamp; }

    @Override
    public TransactionDTO getDto() { return new TransactionDTO(getAction(), transactionFee, accountBalance, time, symbol, timeStamp.getNano()); }

    @Override
    public TransactionInterDto getInterDto() { return new TransactionDTO(getAction(), transactionFee, accountBalance, time, symbol, timeStamp.getNano()); }

    @Override
    public String toString() {
        String res = "Time: " + time;
        if (actionType == 2)
            res = res.concat("\tAction Type: Charge");
        else
            res = res.concat( "\tAction Type: " + (actionType == 0 ? "Buy" : "Sell") + "\tStock Symbol: " + symbol);
        return res.concat("\tTransaction value: " + transactionFee + "\tAccount Balance Before: " + accountBalance + "\tAccount Balance After: " + (accountBalance + transactionFee));
    }

    /*@Override
    public int compareTo(@NotNull Transaction other) {
        if (Duration.between(timeStamp, other.timeStamp).getNano() < 0)
            return -1;
        return 1;
    }*/

    @Override
    public int compareTo(@NotNull TransactionInter o) {
        /* compare this object with another TransactionInter object */
        if (Duration.between(timeStamp, o.getTimeStamp()).getNano() < 0)
            return -1;
        return 1;
    }

}
