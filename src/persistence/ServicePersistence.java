package persistence;

import businesslogic.menu.Menu;
import businesslogic.service.Assignment;
import businesslogic.service.Service;
import businesslogic.service.ServiceEventReceiver;
import businesslogic.service.SummarySheet;

import java.util.List;

public class ServicePersistence implements ServiceEventReceiver {
    @Override
    public void updateSummarySheetCreated(Service service, SummarySheet summarySheet) {
        SummarySheet.saveNewSummarySheet(service, summarySheet);
        Service.updateService(service);
    }

    @Override
    public void updateAddedAssignment(SummarySheet summarySheet, Assignment assignment, int position) {
        if (position < summarySheet.getAssignments().size()) {
            List<Assignment> assignments = summarySheet.getAssignments();
            for (int i = assignments.size() - 1; i >= 0; i--) {
                Assignment a = assignments.get(i);
                if (a.getId() != assignment.getId() && a.getPosition() >= position) {
                    a.setPosition(a.getPosition() + 1);
                    Assignment.updateAssignment(summarySheet.getId(), a);
                }
            }
        }
        Assignment.saveNewAssignment(summarySheet.getId(), assignment, position);

    }

    @Override
    public void updateUpdatedAssignment(SummarySheet summarySheet, Assignment assignment) {
        Assignment.updateAssignment(summarySheet.getId(), assignment);
    }

    @Override
    public void updateDeleteAssignment(SummarySheet summarySheet, Assignment assignment) {
        Assignment.deleteAssignment(summarySheet.getId(), assignment);
    }

    @Override
    public void updateMovedAssignment(SummarySheet summarySheet, Assignment assignment, int position) {
        Assignment.deleteAssignment(summarySheet.getId(), assignment);
        assignment.setPosition(position);

        if (position < summarySheet.getAssignments().size()) {
            List<Assignment> assignments = summarySheet.getAssignments();
            for (int i = assignments.size() - 1; i >= 0; i--) {
                Assignment a = assignments.get(i);
                if (a.getId() != assignment.getId() && a.getPosition() >= position) {
                    a.setPosition(a.getPosition() + 1);
                    Assignment.updateAssignment(summarySheet.getId(), a);
                }
            }
        }
        Assignment.saveNewAssignment(summarySheet.getId(), assignment, position);
    }
}
