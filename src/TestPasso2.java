import businesslogic.CatERing;
import businesslogic.recipe.Recipe;
import businesslogic.service.Assignment;
import businesslogic.service.Event;
import businesslogic.service.Service;
import businesslogic.service.SummarySheet;

import java.util.List;

public class TestPasso2 {

    public static void main(String[] args) {
        try {
            CatERing instance = CatERing.getInstance();
            instance.getUserManager().fakeLogin("Tony");

            Service service = instance.getServiceManager().loadServiceById(6);
            Event event = service.getEvent();

            System.out.println("Summary sheet selected: ");
            SummarySheet summarySheet = instance.getServiceManager().openExistingSummarySheet(event, service);
            System.out.println(summarySheet);

            List<Recipe> recipeList = instance.getRecipeManager().getRecipes();

            System.out.print("\nRecipe selected:");
            Recipe recipe = recipeList.get(4);
            System.out.println(recipe);

            System.out.print("Added new assignment from selected recipe:");
            Assignment newAssignment = instance.getServiceManager().addAssignment(recipe, 5);
            System.out.println(newAssignment);

            System.out.println("\nUpdated summary sheet: " + summarySheet);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
