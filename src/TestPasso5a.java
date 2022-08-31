import businesslogic.CatERing;
import businesslogic.service.Assignment;
import businesslogic.service.Event;
import businesslogic.service.Service;
import businesslogic.service.SummarySheet;

import java.util.List;

public class TestPasso5a {

    public static void main(String[] args) {
        try {
            CatERing instance = CatERing.getInstance();
            instance.getUserManager().fakeLogin("Tony");

            Service service = instance.getServiceManager().loadServiceById(6);
            Event event = service.getEvent();

            System.out.println("Summary sheet selected: ");
            SummarySheet summarySheet = instance.getServiceManager().openExistingSummarySheet(event, service);
            System.out.println(summarySheet);

            List<Assignment> assignmentList = summarySheet.getAssignments();

            System.out.print("\nAssignment selected:");
            Assignment assignment = assignmentList.get(4);
            System.out.println(assignment);

            System.out.println("\nDeleting selected assignment. Updated summary sheet: ");
            instance.getServiceManager().deleteAssignment(assignment);
            System.out.println(summarySheet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
