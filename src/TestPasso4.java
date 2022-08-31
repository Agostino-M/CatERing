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
    /*
        67,0,"",1,0,,,1,39,4
        68,0,"",1,0,,,1,39,5
        69,0,"",1,0,,,4,39,7
        70,0,"",1,0,,,1,39,8
        71,0,"",1,0,,,5,39,6
        72,0,"",1,0,,,7,39,3
        73,0,"",1,0,,,1,39,2
        74,0,"",1,0,,,8,39,1
        75,0,"",1,0,,,1,39,0
        */

}
