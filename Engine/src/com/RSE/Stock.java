package com.RSE;

import objects.dto.StockDTO;
import com.RSE.interfaces.StockInter;
import objects.interfaces.StockInterDto;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Stock implements Serializable, StockInter {
    private String companyName, symbol;
    private final AtomicInteger rate, totalDeals, revolution, quantity;

    public Stock(Stock s) {
        this(s.companyName, s.symbol, s.rate.get(), s.quantity.get());
        this.setTotalDeals(s.totalDeals.get());
        this.setRevolution(s.revolution.get());
    }

    public Stock(StockInter s) {
        this(s.getCompanyName(), s.getSymbol(), s.getRate(), s.getQuantity());
        this.setTotalDeals(s.getTotalDeals());
        this.setRevolution(s.getRevolution());
    }

    public Stock(String name, String symbol, int rate, int quantity) {
        this.companyName = name;
        this.symbol = symbol.toUpperCase();
        this.rate = new AtomicInteger(rate);
        this.quantity = new AtomicInteger(quantity);
        this.totalDeals = new AtomicInteger(0);
        this.revolution = new AtomicInteger(0);
    }

    @Override
    public int getRate() { return this.rate.get(); }

    @Override
    public String getSymbol() { return this.symbol; }

    @Override
    public int getQuantity() { return this.quantity.get(); }

    @Override
    public String getCompanyName() { return this.companyName; }

    @Override
    public int getTotalDeals() { return this.totalDeals.get(); }

    @Override
    public int getRevolution() { return this.revolution.get(); }

    @Override
    public StockDTO getDto() { return new StockDTO(companyName, symbol, rate.get(), quantity.get(), totalDeals.get(), revolution.get()); }

    @Override
    public StockDTO getDto(int quantity) { return new StockDTO(companyName, symbol, rate.get(), quantity, totalDeals.get(), revolution.get()); }

    @Override
    public StockInterDto getInterDto() { return new StockDTO(companyName, symbol, rate.get(), quantity.get(), totalDeals.get(), revolution.get()); }

    @Override
    public StockInterDto getInterDto(int quantity) { return new StockDTO(companyName, symbol, rate.get(), quantity, totalDeals.get(), revolution.get()); }

    @Override
    public void setSymbol(String symbol) { this.symbol = symbol.toUpperCase(); }

    @Override
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    @Override
    public void setQuantity(int quantity) {
        if (quantity >= 0)
            this.quantity.set(quantity);
    }

    @Override
    public void setRate(int rate) {
        if (rate >= 0)
            this.rate.set(rate);
    }

    @Override
    public void setRevolution(int rev) { this.revolution.set(rev); }

    @Override
    public void incTotalDeals() { this.totalDeals.incrementAndGet(); }

    @Override
    public void setTotalDeals(int deals) { this.totalDeals.set(deals); }

    public void set(Stock s) {
        this.setSymbol(s.symbol);
        this.setRate(s.rate.get());
        this.setCompanyName(s.companyName);
        this.setTotalDeals(s.totalDeals.get());
        this.setRevolution(s.revolution.get());
    }

    @Override
    public void set(StockInter s) {
        this.setSymbol(s.getSymbol());
        this.setRate(s.getRate());
        this.setCompanyName(s.getCompanyName());
        this.setTotalDeals(s.getTotalDeals());
        this.setRevolution(s.getRevolution());
    }

    @Override
    public void addRevolution(int rev) { this.revolution.addAndGet(rev); }

    @Override
    public void addQuantity(int quantity) { this.quantity.addAndGet(quantity); }

    @Override
    public String toString() {
        return "Company Name: " + companyName + "\nStock Symbol: " + symbol + "\nRate: " + rate + "\nQuantity: " + quantity
                + "\nTotal Deals Made: " + totalDeals + "\nRevolution: " + revolution + "\n";
    }
}
