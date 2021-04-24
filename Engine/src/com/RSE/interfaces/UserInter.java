package com.RSE.interfaces;

import objects.dto.UserDTO;
import objects.dto.TransactionDTO;
import objects.interfaces.UserInterDto;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.time.Instant;
import java.io.Serializable;

public interface UserInter extends Serializable {
    int getAccount();
    boolean getType();
    String getUsername();
    String getTypeString();
    Set<String> getStocks();
    Map<String, Integer> getStocksDTO();
    int getStockQuantity(String symbol);
    List<TransactionDTO> getTransactionsDTO();
    int getTotalRevolution(Map<String, StockInter> rseStocks);

    UserDTO getDto(Map<String, StockInter> rseStocks);
    UserInterDto getInterDto(Map<String, StockInter> rseStocks);

    void addStock(String stockName, int amount);
    void addTransaction(TransactionInter transaction);
    void increaseStockAmount(String stockName, int amount);
    void decreaseStockAmount(String stockName, int amount);
    void addAllStocks(Map<String, Integer> stocks, boolean flag);

    String toString();
    void addMoneyCharge(int amount);
    boolean checkStock(String stockName);
    int quantityOfStock(String stockName);
    void createTransactionEffect(boolean action, boolean sender, int sum, int amount, String time, Instant timeStamp, String stockName);
}
