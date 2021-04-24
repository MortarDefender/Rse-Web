package objects.dto;

import objects.interfaces.DealInterDto;
import objects.interfaces.Dto;

public class DealDTO implements Dto, DealInterDto {
    private final UserDTO publisher;
    private final int amount, rate, revolution, timeStamp;
    private final String symbol, time, action, status, publisherName;  // Buy || Sell

    public DealDTO(String symbol, String action, int amount, int rate, int revolution, UserDTO publisher, String time, int timeStamp, String status) {
        this.time = time;
        this.rate = rate;
        this.status = status;
        this.symbol = symbol;
        this.action = action;
        this.amount = amount;
        this.publisher = publisher;
        this.timeStamp = timeStamp;
        this.revolution = revolution;
        this.publisherName = publisher.getUsername();
    }

    @Override
    public int getRate() { return rate; }

    @Override
    public String getTime() { return time; }

    @Override
    public int getAmount() { return amount; }

    @Override
    public String getSymbol() { return symbol; }

    @Override
    public String getAction() { return action; }

    @Override
    public String getStatus() { return status; }

    @Override
    public int getTimeStamp() { return timeStamp; }

    @Override
    public int getRevolution() { return revolution; }

    @Override
    public UserDTO getPublisher() { return publisher; }

    @Override
    public String getPublisherName() { return publisherName; }
}
