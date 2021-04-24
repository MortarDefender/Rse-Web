package objects.dto;

import objects.interfaces.Dto;
import objects.interfaces.StockInterDto;

public class StockDTO implements Dto, StockInterDto {
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

    @Override
    public int getRate() { return rate; }

    @Override
    public String getSymbol() { return symbol; }

    @Override
    public int getQuantity() { return quantity; }

    @Override
    public int getTotalDeals() { return totalDeals; }

    @Override
    public int getRevolution() { return revolution; }

    @Override
    public String getCompanyName() { return companyName; }
}
