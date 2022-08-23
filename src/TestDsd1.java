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
            System.out.println("\nCurrent service");
            System.out.println(instance.getServiceManager().getCurrentService());

            instance.getMenuManager().setCurrentMenu(service.getMenu()); //TODO Forse si potrebbe fare in automatico nel loadservice?
            System.out.println("\nMenu");
            System.out.println(instance.getMenuManager().getCurrentMenu());

            System.out.println("\nTEST GET EVENT INFO");
            Event event = instance.getServiceManager().getCurrentService().getEvent();
            System.out.println(event);
            for (Service s : event.getServices()) {
                System.out.println("\t" + s);
            }

            //SummarySheet summarySheet = instance.getServiceManager().createSummarySheet(event, service);
            SummarySheet summarySheet = instance.getServiceManager().openExistingSummarySheet(event, service);
            System.out.println("\nSummary Sheet");
            System.out.println(summarySheet);

            List<Recipe> recipes = instance.getMenuManager().getCurrentMenu().getRecipes();
            System.out.println("\nRICETTE");
            System.out.println(recipes);

            Assignment assignment = instance.getServiceManager().addAssigment(recipes.get(1), 1);
            System.out.println("Aggiungo un compito con la ricetta: " + recipes.get(1));
            System.out.println(summarySheet);

            instance.getServiceManager().sortSummarySheet(assignment, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
