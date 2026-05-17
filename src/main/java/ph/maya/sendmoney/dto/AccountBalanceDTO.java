package ph.maya.sendmoney.dto;

import java.math.BigDecimal;

public record AccountBalanceDTO(
    Long id,
    BigDecimal balance,
    BigDecimal dailyLimit,
    String currency
) {}
