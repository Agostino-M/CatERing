import businesslogic.CatERing;
import businesslogic.menu.Menu;
import businesslogic.recipe.Recipe;
import businesslogic.service.Assignment;
import businesslogic.service.Event;
import businesslogic.service.Service;
import businesslogic.service.SummarySheet;

import java.util.List;

public class TestDsd1 {

    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing instance = CatERing.getInstance();
            instance.getUserManager().fakeLogin("Lidia");
            System.out.println(instance.getUserManager().getCurrentUser());

            int serviceId = 2;
            Service service = instance.getServiceManager().loadServiceById(serviceId);
            instance.getMenuManager().setCurrentMenu(service.getMenu());

            System.out.println("\nTEST GET EVENT INFO");
            Event event = instance.getServiceManager().getCurrentService().getEvent();
            System.out.println(event);
            for (Service s : event.getServices()) {
                System.out.println("\t" + s);
            }

            //SummarySheet summarySheet = instance.getServiceManager().createSummarySheet(event, service);
            SummarySheet summarySheet = instance.getServiceManager().openExistingSummarySheet(event, service);

            List<Recipe> recipes = instance.getMenuManager().getCurrentMenu().getRecipes();
            System.out.println("RICETTE");
            System.out.println(recipes);
            //
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
