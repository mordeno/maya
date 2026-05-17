package ph.maya.sendmoney.debug;

import lombok.Data;

// ==============================================
// This is just for DEBUGGING to see events
// ==============================================
@Data
public class TransactionEventsList {
    private Long id;
    private Long transactionId;
    private String eventType;
    private String date;
}
