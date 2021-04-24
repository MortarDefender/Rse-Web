package com.RSE;

import java.util.Map;
import java.util.Date;
import java.time.Instant;
import java.time.Duration;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import objects.DealDTO;
import objects.UserDTO;
import com.sun.istack.internal.NotNull;

public class Deal  implements Serializable, Comparable<Deal> {
    private final int rate;
    private final boolean action;  // true == Buy || false == Sell
    private final User publisher;
    private int amount, revolution;
    private final String symbol, time;
    private final Instant timeStamp;

    public Deal(String symbol, boolean action, int amount, int rate, User publisher) {
        this(symbol, action, amount, rate, publisher, new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()), Instant.now());
    }

    public Deal(String symbol, boolean action, int amount, int rate, User publisher, String time, Instant timeStamp) {
        this.time = time;
        this.rate = rate;
        this.revolution = 0;
        this.symbol = symbol;
        this.action = action;
        this.amount = amount;
        this.publisher = publisher;
        this.timeStamp = timeStamp;
    }

    public int getRate() { return rate; }
    public String getTime() { return time; }
    public int getAmount() { return amount; }
    public String getSymbol() { return symbol; }
    public boolean getAction() { return action; }
    public User getPublisher() { return publisher; }
    public int getRevolution() { return revolution; }
    public int getIntTimeStamp() { return this.timeStamp.getNano(); }
    public Instant getTimeStamp() { return this.timeStamp; }
    public String getActionString() { return action ? "Buy" : "Sell"; }
    public UserDTO getPublisherDTO(Map<String, Stock> rseStocks) { return publisher.getDto(rseStocks); }
    public DealDTO getDto(String status, Map<String, Stock> rseStocks) { return new DealDTO(symbol, getActionString(), amount, rate, revolution, getPublisherDTO(rseStocks), time, status); }

    public void setAmount(int amount) {
        if (amount >= 0)
            this.amount = amount;
    }
    public void setRevolution(int revolution) { this.revolution = revolution; }

    public String print() {
        return "Time: " + time + "\tAmount: " + amount + "\tRate: " + rate + "\tPublisher: " + publisher.getUsername() + "\tTotal transaction value: " + revolution;
    }

    @Override
    public String toString() {
        return "Symbol: " + symbol + "\tType: " + (action ? "Buy" : "Sell") + "\tAmount: " + amount +
                "\tRate: " + rate + "\tTime: " + time + "\tPublisher: " + publisher.getUsername() + "\tRevolution: " + revolution;
    }

    @Override
    public int compareTo(@NotNull Deal other) {
        if ((action && rate < other.rate) || (!action && rate > other.rate))
            return 1;
        else {
            if (rate == other.rate) {
                // if (a.getTime().compareTo(b.getTime()) < 0)
                if (Duration.between(timeStamp, other.timeStamp).getNano() < 0)
                    return -1;
                else
                    return 1;
            } else
                return -1;
        }
    }
}
