package ph.maya.sendmoney.dto.api;

public record TransactionDetailsResponse(
    Long transactionId,
    String senderName,
    String receiverName,
    String amount,
    String type, // "SENT" or "RECEIVED"
    String status,
    String date,
    String currency
) {}
