package com.RSE.interfaces;

import objects.dto.TransactionDTO;
import com.sun.istack.internal.NotNull;
import objects.interfaces.TransactionInterDto;

import java.time.Instant;
import java.io.Serializable;

public interface TransactionInter extends Serializable, Comparable<TransactionInter> {
    int getSum();
    int getAccount();
    String getTime();
    String toString();
    String getAction();
    String getSymbol();
    Instant getTimeStamp();
    TransactionDTO getDto();
    TransactionInterDto getInterDto();
    int compareTo(@NotNull TransactionInter o);
}
