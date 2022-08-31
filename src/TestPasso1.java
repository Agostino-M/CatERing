import businesslogic.CatERing;
import businesslogic.service.Event;
import businesslogic.service.Service;
import businesslogic.service.SummarySheet;

public class TestPasso1 {

    public static void main(String[] args) {
        try {
            CatERing instance = CatERing.getInstance();
            instance.getUserManager().fakeLogin("Tony");
            System.out.println("Logged as " + instance.getUserManager().getCurrentUser());

            Service service = instance.getServiceManager().loadServiceById(5);
            Event event = service.getEvent();

            System.out.println("Event: " + event);
            System.out.println("Service: " + service);
            SummarySheet summarySheet = instance.getServiceManager().createSummarySheet(event, service);
            System.out.println(summarySheet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
