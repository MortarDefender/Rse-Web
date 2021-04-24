package objects;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDTO implements Dto {
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

    public String getType() { return type; }

    public int getAccount() { return account; }

    public String getUsername() { return username; }

    public int getRevolution() { return revolution; }

    public Set<String> getStocksNames() {return stocks.keySet();}

    public List<TransactionDTO> getTransactions() { return transactions; }

    public int getStockQuantity(String stockName) { return stocks.get(stockName); }
}
