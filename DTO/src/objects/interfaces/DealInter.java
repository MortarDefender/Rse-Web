package objects.interfaces;

import objects.dto.UserDTO;

public interface DealInter extends Dto {
    int getRate();
    String getTime();
    int getAmount();
    String getSymbol();
    String getAction();
    String getStatus();
    int getTimeStamp();
    int getRevolution();
    UserDTO getPublisher();
    String getPublisherName();
}
