package objects.interfaces;

import objects.dto.DealDTO;
import objects.dto.StockDTO;
import objects.dto.TransactionDTO;
import objects.dto.UserDTO;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface RSE {
    CommandAnswer<Map<String, Integer>, String> apiGraph(String stockName);
    CommandAnswer<Map<String, List<String>>, String> siteGraph(String stockName);

    void loadXml(String path) throws Exception;
    CommandAnswer<Dto, String> loadXml(String path, String username);
    void loadXml(InputStream stream, String username) throws Exception;

    List<UserDTO> getUsers();
    boolean checkUser(String username);
    CommandAnswer<UserDTO, String> getUser(String username);
    CommandAnswer<Integer, String> getUserRevolution(String username);
    CommandAnswer<Dto, String> addUser(String username, boolean type);
    CommandAnswer<Dto, String> addAccountCharge(String username, int amount);
    CommandAnswer<Integer, String> getUserStockAmount(String username, String symbol);
    CommandAnswer<Dto, String> addUserStock(String companyName, String symbol, int rate, int quantity, String username);

    List<StockDTO> getStocks();
    List<Integer> getStocksDifference();
    boolean checkStock(String stockName);
    List<StockDTO> getStocks(String username);
    CommandAnswer<Integer, String> getRate(String symbol);
    CommandAnswer<StockDTO, String> getStock(String symbol);
    CommandAnswer<List<DealDTO>, String> getStockDeals(String symbol);
    CommandAnswer<List<TransactionDTO>, String> getTransactions(String username);
    CommandAnswer<List<DealDTO>, String> getAdminList(String stockName, String listName);
    CommandAnswer<Dto, String> addStock(String companyName, String symbol, int rate, int quantity);

    CommandAnswer<List<DealDTO>, String> MKT(String symbol, boolean action, int amount, String username);
    CommandAnswer<List<DealDTO>, String> LMT(String symbol, boolean action, int amount, int rate, String username);
    CommandAnswer<List<DealDTO>, String> FOK(String symbol, boolean action, int amount, int rate, String username);
    CommandAnswer<List<DealDTO>, String> IOC(String symbol, boolean action, int amount, int rate, String username);
}