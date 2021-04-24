package objects.interfaces;

import objects.dto.TransactionDTO;

import java.util.List;
import java.util.Set;

public interface UserInter extends Dto {
    String getType();
    int getAccount();
    String getUsername();
    int getRevolution();
    Set<String> getStocksNames();
    List<TransactionDTO> getTransactions();
    int getStockQuantity(String stockName);
}
