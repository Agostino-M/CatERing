package businesslogic.service;

public interface ServiceEventReceiver {
    void updateSummarySheetCreated(Service service, SummarySheet summarySheet);

    void updateAddedAssignment(SummarySheet summarySheet, Assignment assignment);

    void updateModifiedAssignment(SummarySheet summarySheet, Assignment assignment);

    void updateDeleteAssignment(SummarySheet summarySheet, Assignment assignment);

    void updateMovedAssignment(SummarySheet summarySheet);
}
