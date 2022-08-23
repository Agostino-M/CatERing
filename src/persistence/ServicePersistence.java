package persistence;

import businesslogic.menu.Menu;
import businesslogic.service.Assignment;
import businesslogic.service.Service;
import businesslogic.service.ServiceEventReceiver;
import businesslogic.service.SummarySheet;

public class ServicePersistence implements ServiceEventReceiver {
    @Override
    public void updateSummarySheetCreated(Service service, SummarySheet summarySheet) {
        SummarySheet.saveNewSummarySheet(service, summarySheet);
    }

    @Override
    public void updateAddedAssignment(SummarySheet summarySheet, Assignment assignment, int position) {
        Assignment.saveNewAssignment(summarySheet.getId(), assignment, position);
    }

    @Override
    public void updateUpdatedAssignment(SummarySheet summarySheet, Assignment assignment) {
        Assignment.updateAssignment(summarySheet.getId(), assignment);
    }

    @Override
    public void updateDeleteAssignment(SummarySheet summarySheet, Assignment assignment) {
        Assignment.deleteAssignment(assignment);
    }

    @Override
    public void updateMovedAssignment(SummarySheet summarySheet) {
        SummarySheet.saveAssignmentOrder(summarySheet);
    }
}
