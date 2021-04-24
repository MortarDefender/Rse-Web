package ExceptionsType;

public enum ExpType {
    // generic type statements
    Successful, Failure,

    // File exception error Or Xml scheme error
    JXBError, FileNotFound,

    // Stock exception
    StockNotFound, StockSymbolDuplication, StockCompanyDuplication, StockQuantityNotPositive,

    // User Exception
    UserNotFound, UsernameDuplication, UserChargeAmountNotPositive,

    // Xml file Exception
    XMLStockSymbolDuplication, XMLStockCompanyNameDuplication, XMLStockPriceNotPositive,
    XMLUserHoldingStockSymbolDuplication, XMLUserHoldingStockCompanyNameDuplication,  XMLUserHoldingQuantityNotPositive,
    XMLUsernameDuplication,

    // Command Exception
    CommandUserOverSell, CommandUserBuyOverQuantity, CommandStockNotFound, CommandUserNotFound, CommandAmountNotPositive,
    CommandListNameNotFound,

    // Chat Exceptions
    ChatNotFound, ChatNameDuplication
}
