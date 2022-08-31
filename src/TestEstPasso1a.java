import businesslogic.CatERing;
import businesslogic.service.Event;
import businesslogic.service.Service;
import businesslogic.service.SummarySheet;

public class TestEstPasso1a {

    public static void main(String[] args) {
        try {
            CatERing instance = CatERing.getInstance();
            instance.getUserManager().fakeLogin("Tony");
            System.out.println("Logged as " + instance.getUserManager().getCurrentUser());

            Service service = instance.getServiceManager().loadServiceById(6);
            Event event = service.getEvent();

            System.out.println("Event: " + event);
            SummarySheet summarySheet = instance.getServiceManager().openExistingSummarySheet(event, service);
            System.out.println("Successfully opened Summary Sheet: " + summarySheet);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
