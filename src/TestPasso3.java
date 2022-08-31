import businesslogic.CatERing;
import businesslogic.recipe.Recipe;
import businesslogic.service.Assignment;
import businesslogic.service.Event;
import businesslogic.service.Service;
import businesslogic.service.SummarySheet;

import java.util.ArrayList;
import java.util.List;

public class TestPasso3 {

    public static void main(String[] args) {
        try {
            CatERing instance = CatERing.getInstance();
            instance.getUserManager().fakeLogin("Tony");

            Service service = instance.getServiceManager().loadServiceById(5);
            Event event = service.getEvent();

            System.out.println("Summary sheet selected: ");
            SummarySheet summarySheet = instance.getServiceManager().openExistingSummarySheet(event, service);
            System.out.println(summarySheet);

            List<Assignment> assignmentList = summarySheet.getAssignments();

            System.out.print("\nAssignment selected:");
            Assignment assignment = assignmentList.get(4);
            System.out.println(assignment);

            int newPosition = 2;
            System.out.println("Moving selected assignment from its position to position " + newPosition);
            instance.getServiceManager().sortSummarySheet(assignment, newPosition);
            System.out.println("Updated summary sheet: " + summarySheet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
