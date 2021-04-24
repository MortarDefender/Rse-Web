package com.RSE;

import java.util.Map;
import java.util.Date;
import java.time.Instant;
import java.time.Duration;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import objects.dto.DealDTO;
import objects.dto.UserDTO;
import com.RSE.interfaces.UserInter;
import com.RSE.interfaces.DealInter;
import com.RSE.interfaces.StockInter;
import com.sun.istack.internal.NotNull;
import objects.interfaces.DealInterDto;

public class Deal implements Serializable, DealInter { // Comparable<Deal>
    private final int rate;
    private final boolean action;  // true == Buy || false == Sell
    private int amount, revolution;
    private final Instant timeStamp;
    private final String symbol, time;
    private final UserInter publisher;

    public Deal(String symbol, boolean action, int amount, int rate, UserInter publisher) {
        this(symbol, action, amount, rate, publisher, new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()), Instant.now());
    }

    public Deal(String symbol, boolean action, int amount, int rate, UserInter publisher, String time, Instant timeStamp) {
        this.time = time;
        this.rate = rate;
        this.revolution = 0;
        this.symbol = symbol;
        this.action = action;
        this.amount = amount;
        this.publisher = publisher;
        this.timeStamp = timeStamp;
    }

    @Override
    public int getRate() { return rate; }

    @Override
    public String getTime() { return time; }

    @Override
    public int getAmount() { return amount; }

    @Override
    public String getSymbol() { return symbol; }

    @Override
    public boolean getAction() { return action; }

    @Override
    public int getRevolution() { return revolution; }

    @Override
    public UserInter getPublisher() { return publisher; }

    @Override
    public Instant getTimeStamp() { return this.timeStamp; }

    @Override
    public int getIntTimeStamp() { return this.timeStamp.getNano(); }

    @Override
    public String getActionString() { return action ? "Buy" : "Sell"; }

    @Override
    public UserDTO getPublisherDTO(Map<String, StockInter> rseStocks) { return publisher.getDto(rseStocks); }

    @Override
    public DealDTO getDto(String status, Map<String, StockInter> rseStocks) { return new DealDTO(symbol, getActionString(), amount, rate, revolution, getPublisherDTO(rseStocks), time, timeStamp.getNano(), status); }

    @Override
    public DealInterDto getInterDto(String status, Map<String, StockInter> rseStocks) { return new DealDTO(symbol, getActionString(), amount, rate, revolution, getPublisherDTO(rseStocks), time, timeStamp.getNano(), status); }

    @Override
    public void setAmount(int amount) {
        if (amount >= 0)
            this.amount = amount;
    }
    @Override
    public void setRevolution(int revolution) { this.revolution = revolution; }

    public String print() {
        return "Time: " + time + "\tAmount: " + amount + "\tRate: " + rate + "\tPublisher: " + publisher.getUsername() + "\tTotal transaction value: " + revolution;
    }

    @Override
    public String toString() {
        return "Symbol: " + symbol + "\tType: " + (action ? "Buy" : "Sell") + "\tAmount: " + amount +
                "\tRate: " + rate + "\tTime: " + time + "\tPublisher: " + publisher.getUsername() + "\tRevolution: " + revolution;
    }

    /*@Override
    public int compareTo(@NotNull Deal other) {
        if ((action && rate < other.rate) || (!action && rate > other.rate))
            return 1;
        else {
            if (rate == other.rate) {
                if (Duration.between(timeStamp, other.timeStamp).getNano() < 0)
                    return -1;
                else
                    return 1;
            } else
                return -1;
        }
    }*/

    @Override
    public int compareTo(@NotNull DealInter other) {
        /* compare this object with another DealInter object */
        if ((action && rate < other.getRate()) || (!action && rate > other.getRate()))
            return 1;
        else {
            if (rate == other.getRate()) {
                if (Duration.between(timeStamp, other.getTimeStamp()).getNano() < 0)
                    return -1;
                else
                    return 1;
            } else
                return -1;
        }
    }
}
