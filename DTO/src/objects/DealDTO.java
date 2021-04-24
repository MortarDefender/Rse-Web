package objects;

public class DealDTO implements Dto {
    private final UserDTO publisher;
    private final int amount, rate, revolution;
    private final String symbol, time, action, status, publisherName;  // Buy || Sell

    public DealDTO(String symbol, String action, int amount, int rate, int revolution, UserDTO publisher, String time, String status) {
        this.time = time;
        this.rate = rate;
        this.status = status;
        this.symbol = symbol;
        this.action = action;
        this.amount = amount;
        this.publisher = publisher;
        this.revolution = revolution;
        this.publisherName = publisher.getUsername();
    }

    public int getRate() { return rate; }

    public String getTime() { return time; }

    public int getAmount() { return amount; }

    public String getSymbol() { return symbol; }

    public String getAction() { return action; }

    public String getStatus() { return status; }

    public int getRevolution() { return revolution; }

    public UserDTO getPublisher() { return publisher; }

    public String getPublisherName() { return publisherName; }
}
