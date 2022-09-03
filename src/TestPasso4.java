import businesslogic.CatERing;
import businesslogic.service.*;

import java.util.List;

public class TestPasso4 {

    public static void main(String[] args) {
        try {
            CatERing instance = CatERing.getInstance();
            instance.getUserManager().fakeLogin("Tony");
            System.out.println("User: " + instance.getUserManager().getCurrentUser());

            Service service = instance.getServiceManager().loadServiceById(6);
            Event event = service.getEvent();

            System.out.println("Event: " + event);
            System.out.println("Service: " + service);
            ShiftBoard shiftBoard = instance.getServiceManager().getShiftBoard();
            System.out.println(shiftBoard);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
