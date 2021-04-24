package com.RSE;

import java.io.InputStream;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import ExceptionsType.ExpType;
import objects.*;
import generated.*;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Engine implements Serializable, RSE {
    public enum Commands { LMT, MKT, FOK, IOC }
    private final Map<String, User> users;                   // users name   -> User
    private final Map<String, Stock> stocks;                 // stock symbol -> Stock
    private final Map<String, Map<String, List<Deal>>> db;   // stock symbol -> { buy -> [Deals], sell -> [Deals], approved -> [Deals] }
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generated";

    public Engine() {
        db = new HashMap<>();
        users = new HashMap<>();
        stocks = new HashMap<>();
    }

    @Override
    public void loadXml(String path) throws Exception { loadXml2(getDoc(path).getDocumentElement()); } // loads an xml using the xml2 scheme

    @Override
    public CommandAnswer<Dto, String> loadXml(String path, String username) {
        /* load an xml file using the path given */
        try {
            InputStream inputStream = new FileInputStream(path);
            /*rseDeserializeFrom(inputStream).ifPresent( r -> {
                addAllStocks(loadStocks(r.getRseStocks().getRseStock()));
                addAllUserHoldings(r.getRseHoldings().getRseItem(), username, false);
            });*/
            Optional<RizpaStockExchangeDescriptor> rse = rseDeserializeFrom(inputStream);
            if (rse.isPresent()) {
                CommandAnswer<Map<String, Stock>, String> ans = loadStocks(rse.get().getRseStocks().getRseStock());
                if (ans.isSuccessful()) {
                    addAllStocks(ans.getObject());
                    CommandAnswer<Dto, String> holdingAns = addAllUserHoldings(rse.get().getRseHoldings().getRseItem(), username, false);
                    if (!holdingAns.isSuccessful())
                        return holdingAns;
                }
                else
                    return new AnswerDto<>(null, ans.getExpMessage(), ans.getType());
            }
            this.updateStockQuantity();
        } catch (JAXBException e) { // JXBError
            return new AnswerDto<>(null, "the path given '" + path + "' is not in the scheme of the xsd file given.", ExpType.JXBError);
            // throw new InvalidParameterException("the path given '" + path + "' is not in the scheme of the xsd file given.");
        } catch (FileNotFoundException e) {  // FileNotFound
            return new AnswerDto<>(null, "the file in the path given '" + path + "' does not exist", ExpType.FileNotFound);
            // throw new InvalidParameterException("the file in the path given '" + path + "' does not exist");
        }
        return new AnswerDto<>();
    }

    // public void loadXml(String path, String username) throws Exception { loadXml3(getDoc(path).getDocumentElement(), username); } // loads an xml using the xml3 scheme

    @Override
    public void loadXml(InputStream stream, String username) throws Exception { loadXml3(getDoc(stream).getDocumentElement(), username); } // loads an xml using the xml3 scheme

    @Override
    public CommandAnswer<Dto, String> addStock(String companyName, String symbol, int rate, int quantity) {
        /* add a stock into the stocks list and create the appropriate list for the deals to come */
        if (this.stocks.containsKey(symbol)) // StockSymbolDuplication
            return new AnswerDto<>(null, "The symbol '" + symbol + "' is assigned to a different company", ExpType.StockSymbolDuplication);
        // throw new InvalidParameterException("The symbol '" + symbol + "' is assigned to a different company");
        for (Stock stock : this.stocks.values()) {  // StockCompanyDuplication
            if (stock.getCompanyName().equals(companyName))
                return new AnswerDto<>(null, "The stock of '" + companyName + "' exist in the list", ExpType.StockCompanyDuplication);
            // throw new InvalidParameterException("The stock of '" + companyName + "' exist in the list");
        }
        if (this.db.containsKey(symbol))  // StockSymbolDuplication
            return new AnswerDto<>(null, "The symbol '" + symbol + "' is assigned to a different company", ExpType.StockSymbolDuplication);
        // throw new InvalidParameterException("The symbol '" + symbol + "' is assigned to a different company");
        this.stocks.put(symbol, new Stock(companyName, symbol, rate, quantity));
        createDbStockList(symbol);
        return new AnswerDto<>();
    }

    @Override
    public CommandAnswer<Dto, String> addUser(String username, boolean type) {
        /* adds a new user with the name and the type given */
        if (this.users.containsKey(username))  // UsernameDuplication
            return new AnswerDto<>(null, "there is a user with the name '" + username + "' already", ExpType.UsernameDuplication);
        // throw new InvalidParameterException("there is a user with the name '" + username + "' already");
        this.users.put(username, new User(username, type));
        return new AnswerDto<>();
    }

    @Override
    public CommandAnswer<Dto, String> addAccountCharge(String username, int amount) {
        /* adds a new transaction with the amount given to the user with the given name */
        if (!this.users.containsKey(username))  // UserNotFound
            return new AnswerDto<>(null, "The user with the name '" + username + "' does not exist", ExpType.UserNotFound);
        // throw new InvalidParameterException("The user with the name '" + username + "' does not exist");
        // if (amount >= 0) -> // UserChargeAmountNotPositive
        this.users.get(username).addMoneyCharge(amount);
        return new AnswerDto<>();
    }

    @Override
    public boolean checkUser(String username) { return this.users.containsKey(username);  }  // check if a user exist in the system

    @Override
    public CommandAnswer<StockDTO, String> getStock(String symbol) {
        /* return a stock dto object about the information of the stock with the given name */
        if (!this.stocks.containsKey(symbol))  // StockNotFound
            return new AnswerDto<>(null, "There is no stock with the symbol " + symbol, ExpType.StockNotFound);
        // throw new InvalidParameterException("There is no stock with the symbol " + symbol);
        // return this.stocks.get(symbol).getDto();
        return new AnswerDto<>(this.stocks.get(symbol).getDto(), null, ExpType.Successful);
    }

    @Override
    public List<StockDTO> getStocks() {
        /* return a list of stock dto objects of all stocks */
        List<StockDTO> l = new ArrayList<>();
        this.stocks.values().forEach(stock -> l.add(stock.getDto()));
        return l;
    }

    @Override
    public List<StockDTO> getStocks(String username) {
        /* return a list of stock dto objects of only the stocks of the user with the given username */
        List<StockDTO> l = new ArrayList<>();
        this.users.get(username).getStocks().forEach(stockName -> l.add(this.stocks.get(stockName).getDto(this.users.get(username).getStockQuantity(stockName))));
        return l;
    }

    @Override
    public boolean checkStock(String stockName) { return this.stocks.containsKey(stockName); }  /* return true if the stock is in the system false otherwise */

    @Override
    public CommandAnswer<Integer, String> getUserStockAmount(String username, String symbol) {
        /* get the amount of stock ths user has of the stock with symbol given, zero is a default return */
        if (!this.users.containsKey(username))  // UserNotFound
            return new AnswerDto<>(null, "There is no user with the name: " + username, ExpType.UserNotFound);
        // throw new InvalidParameterException("There is no user with the name: " + username);
        if (!this.stocks.containsKey(symbol))  // StockNotFound
            return new AnswerDto<>(null, "There is no stock with the symbol " + symbol, ExpType.StockNotFound);
        // throw new InvalidParameterException("There is no stock with the symbol " + symbol);
        return new AnswerDto<>(this.users.get(username).getStockQuantity(symbol), null, ExpType.Successful);
        // return this.users.get(username).getStockQuantity(symbol);
    }

    @Override
    public CommandAnswer<List<DealDTO>, String> getStockDeals(String symbol) {
        /* return a list of deal dto object of all the approved deals with of the stock with the given name */
        if (!this.stocks.containsKey(symbol))  // StockNotFound
            return new AnswerDto<>(null, "There is no stock with the symbol " + symbol, ExpType.StockNotFound);
        // throw new InvalidParameterException("There is no stock with the symbol " + symbol);
        List<DealDTO> l = new ArrayList<>();
        this.db.get(symbol).get("Approved").forEach(deal -> l.add(deal.getDto("Approved", this.stocks)));
        return new AnswerDto<>(l, null, ExpType.Successful);
        // return l;
    }

    @Override
    public List<Integer> getStocksDifference() {
        /* return the difference from the two last deals  */
        List<Integer> diff = new ArrayList<>();
        for (String symbol : this.stocks.keySet()) {
            List<Deal> symDeals = this.db.get(symbol).get("Approved");
            if (symDeals.size() == 0)
                diff.add(0);
            else if (symDeals.size() == 1) {
                Deal last_deal = symDeals.get(0);
                diff.add((int) ((last_deal.getRate() / (double) this.stocks.get(symbol).getRate()) * 100 - 100));
            }
            else {
                Deal before_last_deal = symDeals.get(symDeals.size() - 2);
                Deal last_deal = symDeals.get(symDeals.size() - 1);
                diff.add((int) ((last_deal.getRate() / (double) before_last_deal.getRate()) * 100 - 100));
            }
        }
        return diff;
    }

    @Override
    public CommandAnswer<List<TransactionDTO>, String> getTransactions(String username) {
        /* return a list of transaction dto object of the user with the given username */
        if (!this.users.containsKey(username))  // UserNotFound
            return new AnswerDto<>(null, "There is no user with the name: " + username, ExpType.UserNotFound);
        // throw new InvalidParameterException("There is no user with the name: " + username);
        return new AnswerDto<>(this.users.get(username).getTransactionsDTO(), null, ExpType.Successful);
        // return this.users.get(username).getTransactionsDTO();
    }

    @Override
    public CommandAnswer<Integer, String> getRate(String symbol) {
        /* return the rate of a given stock */
        if (this.stocks.containsKey(symbol))
            return new AnswerDto<>(this.stocks.get(symbol).getRate(), null, ExpType.Successful);
        // return this.stocks.get(symbol).getRate();
        // throw new InvalidParameterException("There is no stock with the symbol: " + symbol);  // StockNotFound
        return  new AnswerDto<>(null, "There is no stock with the symbol: " + symbol, ExpType.StockNotFound);
    }

    @Override
    public List<UserDTO> getUsers() {
        /* return a list of user dto object of all users */
        List<UserDTO> l = new ArrayList<>();
        this.users.values().forEach(user -> l.add(user.getDto(this.stocks)));
        return l;
    }

    @Override
    public CommandAnswer<UserDTO, String> getUser(String username) {
        /* return a user dto object of a user with the given name */
        if (!this.users.containsKey(username))  // UserNotFound
            return new AnswerDto<>(null, "There is no user with the username: " + username, ExpType.UserNotFound);
        // throw new InvalidParameterException("There is no user with the username: " + username);
        return new AnswerDto<>(this.users.get(username).getDto(this.stocks), null, ExpType.Successful);
        // return this.users.get(username).getDto(this.stocks);
    }

    @Override
    public CommandAnswer<Integer, String> getUserRevolution(String username) {
        /* return the total revolution of the user with the username given */
        if (!this.users.containsKey(username))  // UserNotFound
            return new AnswerDto<>(null, "There is no user with the username: " + username, ExpType.UserNotFound);
        // throw new InvalidParameterException("There is no user with the username: " + username);
        return new AnswerDto<>(this.users.get(username).getTotalRevolution(this.stocks), null, ExpType.Successful);
        // return this.users.get(username).getTotalRevolution(this.stocks);
    }

    @Override
    public CommandAnswer<Map<String, List<String>>, String> siteGraph(String stockName) {
        /* creates a graph of time against price of a given stock */
        Map<String, List<String>> graph = new HashMap<>();
        List<String> xAxis = new ArrayList<>();
        List<String> yAxis = new ArrayList<>();
        if (!this.stocks.containsKey(stockName))  // StockNotFound
            return new AnswerDto<>(null, "There is no stock named '" + stockName + "' in the system", ExpType.StockNotFound);
        // throw new InvalidParameterException("There is no stock named '" + stockName + "' in the system");

        this.db.get(stockName).get("Approved").forEach(deal -> {
            xAxis.add(deal.getTime());
            yAxis.add(String.valueOf(deal.getRate()));
        });

        graph.put("xAxis", xAxis);
        graph.put("yAxis", yAxis);
        return new AnswerDto<>(graph, null, ExpType.Successful);
        // return graph;
    }

    @Override
    public CommandAnswer<Map<String, Integer>, String> apiGraph(String stockName) {
        Map<String, Integer> graph = new HashMap<>();
        if (!this.stocks.containsKey(stockName))  // StockNotFound
            return new AnswerDto<>(null, "There is no stock named '" + stockName + "' in the system", ExpType.StockNotFound);
        // throw new InvalidParameterException("There is no stock named '" + stockName + "' in the system");
        this.db.get(stockName).get("Approved").forEach(deal -> graph.put(deal.getTime(), deal.getRate()));
        return new AnswerDto<>(graph, null, ExpType.Successful);
        // return graph;
    }

    @Override
    public CommandAnswer<List<DealDTO>, String> getAdminList(String stockName, String listName) {
        /* return a list of deal dto objects of the internal commands of the stock and the list name  */
        if (!this.stocks.containsKey(stockName))
            return new AnswerDto<>(null, "There is no stock named '" + stockName + "' in the system", ExpType.CommandStockNotFound);
        if (!this.db.get(stockName).containsKey(listName))
            return new AnswerDto<>(null, "There is no list named '" + listName + "' in the system", ExpType.CommandListNameNotFound);
        String status = listName.equals("Approved") ? "Approved" : "Pending";
        ArrayList<DealDTO> res = new ArrayList<>();
        this.db.get(stockName).get(listName).forEach(deal -> res.add(deal.getDto(status, this.stocks)));
        return new AnswerDto<>(res, "", ExpType.Successful);
        // return res;
    }

    @Override
    public CommandAnswer<Dto, String> addUserStock(String companyName, String symbol, int totalValue, int quantity, String username) {
        /* create a new stock with the given data and append it to the user with the given username */
        int rate = totalValue / quantity;
        this.addStock(companyName, symbol, rate, quantity);
        if (!this.stocks.containsKey(symbol))  // StockNotFound
            return new AnswerDto<>(null, "The stock '" + symbol + "' is not in the stock db", ExpType.StockNotFound);
        // throw new InvalidParameterException("The stock '" + symbol + "' is not in the stock db");
        if (!this.users.containsKey(username))  // UserNotFound
            return new AnswerDto<>(null, "The user named '" + username + "' is not a user in the system", ExpType.UserNotFound);
        // throw new InvalidParameterException("The user named '" + username + "' is not a user in the system");
        if (quantity <= 0)                      // StockQuantityNotPositive
            return new AnswerDto<>(null, "The quantity of the stock the user can hold is greater than zero", ExpType.StockQuantityNotPositive);
        // throw new InvalidParameterException("The quantity of the stock the user can hold is greater than zero");
        this.users.get(username).addStock(symbol, quantity);
        return new AnswerDto<>();
    }

    @Override
    public CommandAnswer<List<DealDTO>, String> LMT(String symbol, boolean action, int amount, int rate, String username) throws InvalidParameterException {
        /* LIMIT command int objects.RSE */
        CommandAnswer<List<DealDTO>, String> ans = commandException(symbol, action, amount, username);
        if (ans.isSuccessful())
            return TradeCommand(Engine.Commands.LMT, symbol, action, amount, rate, this.users.get(username));
        return ans;
    }

    @Override
    public CommandAnswer<List<DealDTO>, String> MKT(String symbol, boolean action, int amount, String username) throws InvalidParameterException {
        /* MARKET command in objects.RSE */
        CommandAnswer<List<DealDTO>, String> ans = commandException(symbol, action, amount, username);
        if (ans.isSuccessful())
            return TradeCommand(Engine.Commands.MKT, symbol, action, amount, 0, this.users.get(username));
        return ans;
    }

    @Override
    public CommandAnswer<List<DealDTO>, String> FOK(String symbol, boolean action, int amount, int rate, String username) throws InvalidParameterException {
        /* Fill Or Kill command in objects.RSE */
        CommandAnswer<List<DealDTO>, String> ans = commandException(symbol, action, amount, username);
        if (ans.isSuccessful())
            return TradeCommand(Engine.Commands.FOK, symbol, action, amount, rate, this.users.get(username));
        return ans;
    }

    @Override
    public CommandAnswer<List<DealDTO>, String> IOC(String symbol, boolean action, int amount, int rate, String username) throws InvalidParameterException {
        /* Immediate Or Cancel in objects.RSE */
        CommandAnswer<List<DealDTO>, String> ans = commandException(symbol, action, amount, username);
        if (ans.isSuccessful())
            return TradeCommand(Engine.Commands.IOC, symbol, action, amount, rate, this.users.get(username));
        return ans;
    }

    private Optional<RizpaStockExchangeDescriptor> rseDeserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return Optional.ofNullable(((RizpaStockExchangeDescriptor) u.unmarshal(in)));
    }

    private Document getDoc(String path) throws Exception {
        /* return a Document object of the file with the given path */
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream xmlFileInputStream = new FileInputStream(path);
        return builder.parse(xmlFileInputStream);
    }

    private Document getDoc(InputStream stream) throws Exception {
        /* return a Document object of the file with the given path */
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(stream);
    }

    private CommandAnswer<Map<String, Stock>, String> loadStocks(List<RseStock> l) {
        /* load the stock from the xml file */
        Map<String, Stock> newStocks = new HashMap<>();
        Map<String, String> newStocksNames = new HashMap<>();
        for (RseStock item : l) {
            if (newStocks.containsKey(item.getRseSymbol()))  // XMLStockSymbolDuplication
                return new AnswerDto<>(null, "The symbol '" +  item.getRseSymbol() + "' is assigned to a different company", ExpType.XMLStockSymbolDuplication);
            // throw new InvalidParameterException("The symbol '" +  item.getRseSymbol() + "' is assigned to a different company");
            if (newStocksNames.containsKey(item.getRseCompanyName()))  // XMLStockCompanyNameDuplication
                return new AnswerDto<>(null, "The stock of '" + item.getRseCompanyName() + "' exist in the list at least twice", ExpType.XMLStockCompanyNameDuplication);
            // throw new InvalidParameterException("The stock of '" + item.getRseCompanyName() + "' exist in the list at least twice");
            if (item.getRsePrice() <= 0)  // XMLStockPriceNotPositive
                return new AnswerDto<>(null, "The stock price '" + item.getRsePrice() + "' is below zero", ExpType.XMLStockPriceNotPositive);
            // throw new InvalidParameterException("The stock price '" + item.getRsePrice() + "' is below zero");
            newStocks.put(item.getRseSymbol(), new Stock(item.getRseCompanyName(), item.getRseSymbol(), item.getRsePrice(), 0));
            newStocksNames.put(item.getRseCompanyName(), item.getRseSymbol());
        }
        return new AnswerDto<>(newStocks, null, ExpType.Successful);
        // return newStocks;
    }

    private Map<String, Stock> loadStocks(Element root) {
        /* loads the stocks from the root element given */
        Map<String, Stock> newStocks = new HashMap<>();
        Map<String, String> newStocksNames = new HashMap<>();
        NodeList stocks = root.getElementsByTagName("rse-stock");
        for (int i = 0; i < stocks.getLength(); i++) {
            Node stock = stocks.item(i);
            if (stock.getNodeType() == Node.ELEMENT_NODE) {
                Element item = (Element) stock;
                String symbol = item.getElementsByTagName("rse-symbol").item(0).getTextContent();
                String companyName = item.getElementsByTagName("rse-company-name").item(0).getTextContent();
                int price = Integer.parseInt(item.getElementsByTagName("rse-price").item(0).getTextContent());
                int amount = 0;  // quantity
                if (newStocks.containsKey(symbol))
                    throw new InvalidParameterException("The symbol '" + symbol + "' is assigned to a different company");
                if (newStocksNames.containsKey(companyName))
                    throw new InvalidParameterException("The stock of '" + companyName + "' exist in the list");

                newStocks.put(symbol, new Stock(companyName, symbol, price, amount));
                newStocksNames.put(companyName, symbol);
            }
        }
        return newStocks;
    }

    private Map<String, Integer> loadUsersHoldings(Element root, Map<String, Stock> stock) {
        /* loads users golding from the root element and the stocks map given */
        Map<String, Integer> userHoldings = new HashMap<>();
        NodeList holdings = root.getElementsByTagName("rse-item");
        for (int j = 0; j < holdings.getLength(); j++) {
            Node attr = holdings.item(j);
            if (attr.getNodeType() == Node.ELEMENT_NODE) {
                Element holdItem = (Element) attr;
                String symbol = holdItem.getAttribute("symbol");
                int quantity = Integer.parseInt(holdItem.getAttribute("quantity"));
                if (userHoldings.containsKey(symbol))
                    throw new InvalidParameterException("The symbol '" + symbol + "' is assigned to a different company");
                if (!stock.containsKey(symbol))
                    throw new InvalidParameterException("The stock '" + symbol + "' is not in the stock db");
                if (quantity <= 0)
                    throw new InvalidParameterException("quantity of a stock is above zero");
                userHoldings.put(symbol, quantity);
            }
        }
        return userHoldings;
    }

    private void loadXml2(Element root) {
        /* loads an xml using xml2 scheme */
        Map<String, Stock> stocks = loadStocks(root);
        Map<String, Map<String, Integer>> allUserHolding = new HashMap<>();
        NodeList xmlUsers = root.getElementsByTagName("rse-user");
        for (int i = 0; i < xmlUsers.getLength(); i++) {
            Node user = xmlUsers.item(i);
            if (user.getNodeType() == Node.ELEMENT_NODE) {
                Element item = (Element) user;
                String username = item.getAttribute("name");
                if (allUserHolding.containsKey(username))
                    throw new InvalidParameterException("The user with the name '" + username + "' has been entered twice");
                allUserHolding.put(username, loadUsersHoldings(item, stocks));
            }
        }

        this.addAllStocks(stocks);
        for (String username: allUserHolding.keySet()) {
            this.users.put(username, new User(username, true));
            this.addAllUserHoldings(allUserHolding.get(username), username, true);
        }
        this.updateStockQuantity();
    }

    private void loadXml3(Element root, String username) {
        /* loads an xml using xml3 scheme */
        Map<String, Stock> stocks = loadStocks(root);
        if (!this.users.containsKey(username))
            throw new InvalidParameterException("The user with the name '" + username + "' does not exist");
        Map<String, Integer> userHolding = loadUsersHoldings(root, stocks);
        this.addAllStocks(stocks);
        this.addAllUserHoldings(userHolding, username, false);
        this.updateStockQuantity();
    }

    private CommandAnswer<Dto, String> addAllUserHoldings(List<RseItem> holdings, String username, boolean flag) {
        Map<String, Integer> allStocksHolding = new HashMap<>();
        for (RseItem item : holdings) {
            if (item.getQuantity() <= 0)  // XMLUserHoldingQuantityNotPositive
                return new AnswerDto<>(null, "The stock '" + item.getSymbol() + "' has a quantity of " + item.getQuantity() + ". the quantity of a stock is a positive number", ExpType.XMLUserHoldingQuantityNotPositive);
            // throw new InvalidParameterException("The stock '" + item.getSymbol() + "' has a quantity of " + item.getQuantity() + ". the quantity of a stock is a positive number");
            allStocksHolding.put(item.getSymbol(), item.getQuantity());
        }
        this.users.get(username).addAllStocks(allStocksHolding, flag);
        return new AnswerDto<>();
    }

    private void addAllUserHoldings(Map<String, Integer> holdings, String username, boolean flag) {
        /* append thee holdings to the user with the given username */
        Map<String, Integer> allStocksHolding = new HashMap<>();
        holdings.keySet().forEach(symbol -> allStocksHolding.put(symbol, holdings.get(symbol)));
        this.users.get(username).addAllStocks(allStocksHolding, flag);
    }

    private void updateStockQuantity() {
        /* update all stock quantity according to the xml file */
        this.delAllQuantity();
        this.users.values().forEach(user ->
                user.getStocks().forEach(stock -> this.addStockQuantity(stock, user.quantityOfStock(stock))));
    }

    private void createDbStockList(String symbol) {
        /* creates the lists of deals for the stock with the given symbol */
        Map<String, List<Deal>> item = new HashMap<>(3);
        item.put("Buy", new ArrayList<>());
        item.put("Sell", new ArrayList<>());
        item.put("Approved", new ArrayList<>());
        this.db.put(symbol, item);
    }

    private void addStockQuantity(String stockName, int quantity) {
        /* increase or decrease the stock quantity with the given amount and symbol */
        if (!this.stocks.containsKey(stockName))  // StockNotFound
            throw new InvalidParameterException("stock " + stockName + " is not in the system. db error");  // bug in db
        this.stocks.get(stockName).addQuantity(quantity);
    }

    private void delAllQuantity() {
        /* clear the quantities of all stocks */
        this.stocks.values().forEach(stock -> stock.setQuantity(0));
    }

    private void addAllStocks(Map<String, Stock> stocks) {
        /* add all the stocks to the db */
        stocks.values().forEach(stock -> this.addStock(stock.getCompanyName(), stock.getSymbol(), stock.getRate(), stock.getQuantity()));
    }

    @Override
    public String toString() {
        /* return the buy, sell and approved list of all the stocks */
        int revenue;
        String res = "";
        if (this.stocks.size() == 0)
            return "There is no Stocks to show\n";
        for (String stock : this.db.keySet()) {
            res = res.concat("======== " + stock + " ========\n");
            for (String list : this.db.get(stock).keySet()) {
                revenue = 0;
                res = res.concat("List " + list + ":\n");
                if (this.db.get(stock).get(list).size() == 0)
                    res = res.concat("\t" + "There is no deals to show\n");
                for (Deal item : this.db.get(stock).get(list)) {
                    res = res.concat("\t" + item.toString()) + "\n";
                    revenue += item.getAmount() * item.getRate();
                }
                res = res.concat("Total revenue in the list " + list + " is: " + revenue + "\n");
            }
            res = res.concat(stock + " current rate is " + this.stocks.get(stock).getRate() + "\n");
            res = res.concat("=====================\n");
        }
        return res;
    }

    @Deprecated
    private String print(String name) throws InvalidParameterException {  // debug
        /* return the buy, sell and approved list of the stock specified */
        for(Stock stock : this.stocks.values()) {
            if (stock.getCompanyName().equals(name)) {
                System.out.println("======== " + stock + " ========");
                for (String list : this.db.get(stock.getSymbol()).keySet()) {
                    System.out.println("List " + list);
                    for (Deal item : this.db.get(stock.getSymbol()).get(list))
                        return item.toString();
                }
            }
        }
        throw new InvalidParameterException("There is no company with that name in objects.RSE");
    }

    @Deprecated
    private String printStock() {  // debug
        /* return the information about all the stocks  */
        String res = "";
        if (this.stocks.size() == 0)
            return "There is no Stocks to show\n";
        for(Stock stock : this.stocks.values()) {
            res = res.concat(stock.toString() + "\n");
        }
        return res;
    }

    @Deprecated
    private String printStock(String name) throws InvalidParameterException {  // debug
        /* return the information about the stock with the name provided */
        String res = "";
        if (!this.stocks.containsKey(name))
            throw new InvalidParameterException("There is no company with that name in objects.RSE");
        res = res.concat(this.stocks.get(name).toString() + "\nDeals:\n");
        ArrayList<Deal> allDeals = new ArrayList<>();
        for (String key : this.db.get(name).keySet())
            allDeals.addAll(this.db.get(name).get(key));
        allDeals.sort((Deal a, Deal b) -> (a.getTime().compareTo(b.getTime()) < 0) ? 1 : -1);
        for (Deal item : allDeals)
            res = res.concat(item.print() + "\n");
        return res;
    }

    private void insert(Deal deal) {
        /* insert a new deal to the pending list of its kind */
        String key = deal.getAction() ? "Buy" : "Sell";
        this.stocks.get(deal.getSymbol()).setTotalDeals();
        this.db.get(deal.getSymbol()).get(key).add(deal);
        this.db.get(deal.getSymbol()).get(key).sort(Deal::compareTo);
    }

    private CommandAnswer<List<DealDTO>, String> commandException(String symbol, boolean action, int amount, String username) {
        /* check exception before executing the command */
        if (amount <= 0) // CommandAmountNotPositive
            return new AnswerDto<>(null, "the amount of stocks most be positive", ExpType.CommandAmountNotPositive);
        // throw new InvalidParameterException("the amount of stocks most be positive");
        if (!this.stocks.containsKey(symbol))  // CommandStockNotFound
            return new AnswerDto<>(null, "there is no stock with that symbol", ExpType.CommandStockNotFound);
        // throw new InvalidParameterException("there is no stock with that symbol");
        if (!this.users.containsKey(username))  // CommandUserNotFound
            return new AnswerDto<>(null, "there is no user with that username", ExpType.CommandUserNotFound);
        // throw new InvalidParameterException("there is no user with that username");
        if (!action) {
            if (this.users.get(username).checkStock(symbol)) {
                if (this.users.get(username).quantityOfStock(symbol) < amount)  // CommandUserOverSell
                    return new AnswerDto<>(null, username + " does not have enough stock of " + symbol, ExpType.CommandUserOverSell);
                // throw new InvalidParameterException(username + " does not have enough stock of " + symbol);
            } else  // CommandUserOverSell -> can be changed
                return new AnswerDto<>(null, username + " does not have any stock of " + symbol, ExpType.CommandUserOverSell);
            // throw new InvalidParameterException(username + " does not have any stock of " + symbol);
        }
        /*else {
            if (this.users.get(username).quantityOfStock(symbol) + amount > this.eng.getMaxQuantity(symbol))  // CommandUserBuyOverQuantity
                return new AnswerDto<>(null, username + " can not buy more stock than the quantity of the company", ExpType.CommandUserBuyOverQuantity);
            // throw new InvalidParameterException(username + " can not buy more stock than the quantity of the company");
        }*/
        return new AnswerDto<>();
    }

    private void commandHelper(String symbol, boolean action, String time, Instant timeStamp, int rate, int amount, User sender, User receiver, ArrayList<Deal> approved, List<DealDTO> result) {
        /* create the new deal and creating the transaction effect */
        Deal new_deal = new Deal(symbol, action, amount, rate, sender, time, timeStamp);
        new_deal.setRevolution(amount * rate);
        sender.createTransactionEffect(action, true, amount * rate, amount, time, timeStamp, symbol);
        receiver.createTransactionEffect(action, false, amount * rate, amount, time, timeStamp, symbol);
        this.stocks.get(symbol).addRevolution(amount * rate);
        this.db.get(symbol).get("Approved").add(new_deal);
        approved.add(new_deal);
        result.add(new_deal.getDto("Approved", this.stocks));
        this.stocks.get(symbol).setRate(new_deal.getRate());
    }

    private CommandAnswer<List<DealDTO>, String> TradeCommand(Commands cm, String symbol, boolean action, int amount, int rate, User user)  throws InvalidParameterException {
        /* activate the command according to cm with the given data */
        // if (this.stocks.get(symbol).getQuantity() < amount) // CommandUserBuyOverQuantity
        //     return new AnswerDto<>(null, username + " can not buy more stock than the quantity of the company", ExpType.CommandUserBuyOverQuantity);
        //     // throw new InvalidParameterException("The amount of stocks is higher than the max amount of the stock\nThe deal has been canceled\n");
        if (cm.equals(Commands.MKT))
            rate = this.stocks.get(symbol).getRate();
        Deal deal = new Deal(symbol, action, amount, rate, user);
        DealDTO dealOrg = deal.getDto("Canceled", this.stocks);
        String oppositeKey = action ? "Sell" : "Buy";
        ArrayList<Deal> key_temp = new ArrayList<>(this.db.get(symbol).get(oppositeKey));
        ArrayList<Deal> approved_temp = new ArrayList<>(this.db.get(symbol).get("Approved"));
        ArrayList<Deal> new_approved = new ArrayList<>();
        Stock stock_temp = new Stock(this.stocks.get(symbol));
        List<DealDTO> result = new ArrayList<>();
        AtomicBoolean broke = new AtomicBoolean(false);

        while (deal.getAmount() > 0) {
            this.db.get(symbol).get(oppositeKey).stream().filter(item ->
                    ((action) && (item.getRate() <= deal.getRate())) || ((!action) && (deal.getRate() <= item.getRate()))).findFirst()
                    .ifPresent(item -> {
                        if (item.getAmount() <= deal.getAmount()) {
                            commandHelper(symbol, action, deal.getTime(), deal.getTimeStamp(), item.getRate(), item.getAmount(), user, item.getPublisher(), new_approved, result);
                            deal.setAmount(deal.getAmount() - item.getAmount());
                            this.db.get(symbol).get(oppositeKey).remove(item);
                        } else { // item.getAmount() > deal.getAmount()
                            commandHelper(symbol, action, deal.getTime(), deal.getTimeStamp(), item.getRate(), deal.getAmount(), user, item.getPublisher(), new_approved, result);
                            item.setAmount(item.getAmount() - deal.getAmount());
                            deal.setAmount(0);
                        }
                        broke.set(true);
                    });
            if (broke.get())
                broke.set(false);
            else {
                if (cm.equals(Commands.FOK) && deal.getAmount() > 0) {
                    this.db.get(symbol).get(oppositeKey).clear();
                    this.db.get(symbol).get(oppositeKey).addAll(key_temp);
                    this.db.get(symbol).get("Approved").clear();
                    this.db.get(symbol).get("Approved").addAll(approved_temp);
                    this.stocks.get(symbol).set(stock_temp);
                    result.clear();
                    result.add(dealOrg);
                    return new AnswerDto<>(result, null, ExpType.Successful);
                    // return result;  // "The deal has been canceled"
                }
                else {
                    if (!cm.equals(Commands.IOC)) {
                        result.add(deal.getDto("Pending", this.stocks));
                        this.insert(deal);
                    }
                    else
                        result.add(deal.getDto("Canceled", this.stocks));
                    break; // "The deal is pending"
                }
            }
        }
        return new AnswerDto<>(result, null, ExpType.Successful);
        // return result; //  "The deal has been approved
    }
}
