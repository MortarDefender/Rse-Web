package com.RSE.interfaces;

import objects.dto.StockDTO;
import objects.interfaces.StockInterDto;

import java.io.Serializable;

public interface StockInter extends Serializable {
    int getRate();
    StockDTO getDto();
    int getQuantity();
    String getSymbol();
    int getTotalDeals();
    int getRevolution();
    String getCompanyName();
    StockInterDto getInterDto();
    StockDTO getDto(int quantity);
    StockInterDto getInterDto(int quantity);

    void incTotalDeals();
    void set(StockInter s);
    void setRate(int rate);
    void setRevolution(int rev);
    void setTotalDeals(int deals);
    void setSymbol(String symbol);
    void setQuantity(int quantity);
    void setCompanyName(String companyName);

    String toString();
    void addRevolution(int rev);
    void addQuantity(int quantity);
}
