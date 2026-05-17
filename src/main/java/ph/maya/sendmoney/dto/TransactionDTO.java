package ph.maya.sendmoney.dto;

public record TransactionDTO(
    String name, // recipient name for SEND, sender name for RECEIVE
    String type, // "SEND" or "RECEIVE"
    String amount,
    String date
) {}
