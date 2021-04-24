package objects.dto;

import objects.interfaces.Dto;
import objects.interfaces.TransactionInter;

public class TransactionDTO implements Dto, TransactionInter {
    private final String symbol, time, actionType;  // Buy || Sell || Self Charge
    private final int sum, accountBefore, accountAfter, timeStamp;

    public TransactionDTO(String actionType, int sum, int accountBefore, String time, String symbol, int timeStamp) {
        this.sum = sum;
        this.time = time;
        this.symbol = symbol;
        this.timeStamp = timeStamp;
        this.actionType = actionType;
        this.accountBefore = accountBefore;
        this.accountAfter = accountBefore + sum;
    }

    @Override
    public int getSum() { return sum; }

    @Override
    public String getTime() { return time; }

    @Override
    public String getSymbol() { return symbol; }

    @Override
    public int getTimeStamp() { return timeStamp; }

    @Override
    public String getActionType() { return actionType; }

    @Override
    public int getAccountAfter() { return accountAfter; }

    @Override
    public int getAccountBefore() { return accountBefore; }
}
