import businesslogic.CatERing;
import businesslogic.recipe.Recipe;
import businesslogic.service.*;
import businesslogic.user.User;

import java.util.List;

public class TestPasso5b {

    public static void main(String[] args) {
        try {
            CatERing instance = CatERing.getInstance();
            instance.getUserManager().fakeLogin("Tony");

            Service service = instance.getServiceManager().loadServiceById(6);
            Event event = service.getEvent();

            System.out.println("Summary sheet selected: ");
            SummarySheet summarySheet = instance.getServiceManager().openExistingSummarySheet(event, service);
            System.out.println(summarySheet);

            System.out.println("Recipe list:");
            List<Recipe> recipeList = instance.getRecipeManager().getRecipes();
            System.out.println(recipeList);

            System.out.println("Recipe selected:");
            Recipe recipe = recipeList.get(0);
            System.out.println(recipe);

            System.out.print("Adding new ready assignment from selected recipe: ");
            Assignment readyAssignment = instance.getServiceManager().createReadyAssignment(recipe, "2 people", 0);
            System.out.println(readyAssignment);
            System.out.println("\nSummary sheet: " + summarySheet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
