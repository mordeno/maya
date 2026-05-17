package ph.maya.sendmoney.dto;

import java.math.BigDecimal;

public record AccountDTO(
    Long id,
    BigDecimal balance,
    BigDecimal dailyLimit,
    String currency
) {}
