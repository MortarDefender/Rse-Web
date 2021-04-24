package com.RSE;

import objects.dto.StockDTO;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Stock  implements Serializable {
    private String companyName, symbol;
    private final AtomicInteger rate, totalDeals, revolution, quantity;

    public Stock(Stock s) {
        this(s.companyName, s.symbol, s.rate.get(), s.quantity.get());
        this.setTotalDeals(s.totalDeals.get());
        this.setRevolution(s.revolution.get());
    }

    public Stock(String name, String symbol, int rate, int quantity) {
        this.companyName = name;
        this.symbol = symbol.toUpperCase();
        this.rate = new AtomicInteger(rate);
        this.quantity = new AtomicInteger(quantity);
        this.totalDeals = new AtomicInteger(0);
        this.revolution = new AtomicInteger(0);
    }

    public int getRate() { return this.rate.get(); }
    public String getSymbol() { return this.symbol; }
    public int getQuantity() { return this.quantity.get(); }
    public String getCompanyName() { return this.companyName; }
    public int getTotalDeals() { return this.totalDeals.get(); }
    public int getRevolution() { return this.revolution.get(); }
    public StockDTO getDto() { return new StockDTO(companyName, symbol, rate.get(), quantity.get(), totalDeals.get(), revolution.get()); }
    public StockDTO getDto(int quantity) { return new StockDTO(companyName, symbol, rate.get(), quantity, totalDeals.get(), revolution.get()); }

    public void setSymbol(String symbol) { this.symbol = symbol.toUpperCase(); }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setQuantity(int quantity) {
        if (quantity >= 0)
            this.quantity.set(quantity);
    }
    public void setRate(int rate) {
        if (rate >= 0)
            this.rate.set(rate);
    }
    public void setRevolution(int rev) { this.revolution.set(rev); }
    public void setTotalDeals() { this.totalDeals.incrementAndGet(); }
    public void setTotalDeals(int deals) { this.totalDeals.set(deals); }
    public void set(Stock s) {
        this.setSymbol(s.symbol);
        this.setRate(s.rate.get());
        this.setCompanyName(s.companyName);
        this.setTotalDeals(s.totalDeals.get());
        this.setRevolution(s.revolution.get());
    }

    public void addRevolution(int rev) { this.revolution.addAndGet(rev); }
    public void addQuantity(int quantity) { this.quantity.addAndGet(quantity); }

    @Override
    public String toString() {
        return "Company Name: " + companyName + "\nStock Symbol: " + symbol + "\nRate: " + rate + "\nQuantity: " + quantity
                + "\nTotal Deals Made: " + totalDeals + "\nRevolution: " + revolution + "\n";
    }
}
