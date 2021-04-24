package objects.dto;

import objects.interfaces.Dto;
import objects.interfaces.UserInterDto;

import java.util.Map;
import java.util.Set;
import java.util.List;

public class UserDTO implements Dto, UserInterDto {
    private final String username, type;  // stock broker || admin
    private final int account, revolution;
    private final Map<String, Integer> stocks;
    private final List<TransactionDTO> transactions;

    public UserDTO(String name, String type, int account, Map<String, Integer> stocks, List<TransactionDTO> trans, int revolution) {
        this.type = type;
        this.stocks = stocks;
        this.username = name;
        this.account = account;
        this.transactions = trans;
        this.revolution = revolution;
    }

    @Override
    public String getType() { return type; }

    @Override
    public int getAccount() { return account; }

    @Override
    public String getUsername() { return username; }

    @Override
    public int getRevolution() { return revolution; }

    @Override
    public Set<String> getStocksNames() {return stocks.keySet();}

    @Override
    public List<TransactionDTO> getTransactions() { return transactions; }

    @Override
    public int getStockQuantity(String stockName) { return stocks.get(stockName); }
}
