package ph.maya.sendmoney.debug;

import lombok.Data;

// ==============================================
// This is just for DEBUGGING to see ledger
// ==============================================
@Data
public class LedgerList {
    private Long id;
    private Long transactionId;
    private Long accountId;
    private String type;
    private String description;
    private String amount;
    private String date;
}
