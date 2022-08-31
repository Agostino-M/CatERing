import businesslogic.CatERing;
import businesslogic.service.*;
import businesslogic.user.User;
import businesslogic.user.UserManager;

import java.util.List;

public class TestPasso5 {

    public static void main(String[] args) {
        try {
            CatERing instance = CatERing.getInstance();
            instance.getUserManager().fakeLogin("Tony");

            Service service = instance.getServiceManager().loadServiceById(6);
            Event event = service.getEvent();

            System.out.println("Summary sheet selected: ");
            SummarySheet summarySheet = instance.getServiceManager().openExistingSummarySheet(event, service);
            System.out.println(summarySheet);

            System.out.println("Assignment list:");
            List<Assignment> assignmentList = summarySheet.getAssignments();
            System.out.println(assignmentList);

            System.out.println("Assignment selected:");
            Assignment assignment = assignmentList.get(4);
            System.out.println(assignment);

            System.out.println("Setting selected assignment");
            User cook = User.loadUserById(1);
            Shift shift = Shift.loadShiftById(1);
            instance.getServiceManager().setAssignment(assignment, "2kg", shift, cook, 180);
            System.out.println(summarySheet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
