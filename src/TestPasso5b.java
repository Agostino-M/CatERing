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
    /*68,0,',1,0,2,2,7,39,0
            71,0,',1,0,2,1,1,39,1
            69,0,',1,0,,,8,39,2
            70,0,',1,0,,,1,39,3
            75,10,10 piatti,1,0,,,7,39,4
            74,0,',1,0,,,7,39,5
            94,0,',1,0,,,8,39,6
            72,0,',1,0,,,7,39,7
            67,180,2kg,1,0,,,8,39,8
*/
}
