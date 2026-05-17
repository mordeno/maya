package ph.maya.sendmoney.dto.api;

public record TransactionResponse(
    Long transactionId,
    String name,
    String amount,
    String type, // "SENT" or "RECEIVED"
    String date,
    String currency
) {}
