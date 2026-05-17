package ph.maya.sendmoney.dto.api;

public record ErrorResponse(
    int status,
    String message
) {}
