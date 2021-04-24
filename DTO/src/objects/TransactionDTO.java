package objects;

public class TransactionDTO implements Dto {
    private final String symbol, time, actionType;  // Buy || Sell || Self Charge
    private final int sum, accountBefore, accountAfter;

    public TransactionDTO(String actionType, int sum, int accountBefore, String time, String symbol) {
        this.sum = sum;
        this.time = time;
        this.symbol = symbol;
        this.actionType = actionType;
        this.accountBefore = accountBefore;
        this.accountAfter = accountBefore + sum;
    }

    public int getSum() { return sum; }

    public String getTime() { return time; }

    public String getSymbol() { return symbol; }

    public String getActionType() { return actionType; }

    public int getAccountAfter() { return accountAfter; }

    public int getAccountBefore() { return accountBefore; }
}
