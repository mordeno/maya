package ph.maya.sendmoney.dto.api;

public record BalanceResponse(
    Long accountId,
    String balance,
    String dailyLimit,
    String currency
) {}
