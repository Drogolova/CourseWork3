package socks.shop.coursework3.models;

public enum OperationType {
    ACCEPTANCE("приёмка"),
    WRITEOFF("списание"),
    ISSUANCE("выдача");

    public final String operationType;

    OperationType(String operationType) {
        this.operationType = operationType;
    }
}
