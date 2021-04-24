package objects.interfaces;

public interface TransactionInter extends Dto {
    int getSum();
    String getTime();
    String getSymbol();
    int getTimeStamp();
    String getActionType();
    int getAccountAfter();
    int getAccountBefore();
}
