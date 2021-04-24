package com.RSE.interfaces;

import objects.dto.DealDTO;
import objects.dto.UserDTO;
import com.sun.istack.internal.NotNull;
import objects.interfaces.DealInterDto;

import java.util.Map;
import java.time.Instant;
import java.io.Serializable;

public interface DealInter extends Serializable, Comparable<DealInter> {
    int getRate();
    int getAmount();
    String getTime();
    String getSymbol();
    boolean getAction();
    int getRevolution();
    int getIntTimeStamp();
    Instant getTimeStamp();
    UserInter getPublisher();
    String getActionString();
    UserDTO getPublisherDTO(Map<String, StockInter> rseStocks);
    DealDTO getDto(String status, Map<String, StockInter> rseStocks);
    DealInterDto getInterDto(String status, Map<String, StockInter> rseStocks);

    String toString();
    void setAmount(int amount);
    void setRevolution(int revolution);
    int compareTo(@NotNull DealInter other);
}
