package objects;

public class StockDTO implements Dto {
    private final String companyName, symbol;
    private final int rate, totalDeals, revolution, quantity;

    public StockDTO(String name, String symbol, int rate, int quantity, int totalDeals, int revolution) {
        this.rate = rate;
        this.companyName = name;
        this.quantity = quantity;
        this.totalDeals = totalDeals;
        this.revolution = revolution;
        this.symbol = symbol.toUpperCase();
    }

    public int getRate() { return rate; }

    public String getSymbol() { return symbol; }

    public int getQuantity() { return quantity; }

    public int getTotalDeals() { return totalDeals; }

    public int getRevolution() { return revolution; }

    public String getCompanyName() { return companyName; }
}
