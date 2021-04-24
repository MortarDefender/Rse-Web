package objects.interfaces;

public interface StockInterDto extends Dto {
    int getRate();
    String getSymbol();
    int getQuantity();
    int getTotalDeals();
    int getRevolution();
    String getCompanyName();
}
