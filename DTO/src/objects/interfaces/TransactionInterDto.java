package objects.interfaces;

public interface TransactionInterDto extends Dto {
    int getSum();
    String getTime();
    String getSymbol();
    int getTimeStamp();
    String getActionType();
    int getAccountAfter();
    int getAccountBefore();
}
