package ph.maya.sendmoney.debug;

import lombok.Data;

// ==============================================
// This is just for DEBUGGING to see transactions
// ==============================================
@Data
public class TransactionList {
    private Long id;
    private String senderName;
    private String receiverName;
    private String amount;
    private String type; // "SENT" or "RECEIVED"
    private String status;
    private String date;
    private String currency;

}
