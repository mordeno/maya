package ph.maya.sendmoney.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDetailsDTO(
    Long id,
    BigDecimal amount,
    Long senderId,
    String senderName,
    Long receiverId,
    String receiverName,
    String status,
    LocalDateTime date
) {}
