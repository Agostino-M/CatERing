package businesslogic.service;

import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SummarySheet {

    private int id;
    private User creator;
    private List<Assignment> assignments;
    private static final Map<Integer, SummarySheet> loadedSummarySheets = new HashMap<>();

    @Override
    public String toString() {
        return "created by " + creator + " \n\twith " + assignments.size() + " assignments: " + assignments;
    }

    public SummarySheet() {
        assignments = new ArrayList<>();
    }

    public SummarySheet(User user) {
        this.creator = user;
        assignments = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Assignment addAssignment(Recipe recipe, int position) {
        Assignment assignment = new Assignment(recipe, position);
        assignments.add(position, assignment);
        return assignment;
    }

    public Assignment addReadyAssignment(Recipe recipe, String quantity, int position) {
        Assignment assignment = new Assignment(recipe, quantity, position);
        assignments.add(position, assignment);
        return assignment;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public boolean containAssignment(Assignment assignment) {
        return assignments.contains(assignment);
    }

    public void moveAssignment(Assignment assignment, Integer position) {
        this.assignments.remove(assignment);
        this.assignments.add(position, assignment);
    }

    public void setAssignment(Assignment assignment, String quantity, Shift shift, User cook, Integer time) {
        if (quantity != null) {
            assignment.setQuantity(quantity);
        }
        if (shift != null) {
            assignment.setShift(shift);
        }
        if (cook != null) {
            assignment.setAssigned(true);
            assignment.setCook(cook);
        }
        if (time != null) {
            assignment.setTime(time);
        }
    }

    public void deleteAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
    }

    // STATIC METHODS FOR PERSISTENCE

    public static void saveNewSummarySheet(Service service, SummarySheet summarySheet) {
        String summarySheetInsert = "INSERT INTO catering.SummarySheets (creator_id) VALUES (?);";
        int[] result = PersistenceManager.executeBatchUpdate(summarySheetInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, service.getChef().getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    summarySheet.id = rs.getInt(1);
                    summarySheet.creator = User.loadUserById(service.getChef().getId());
                }
            }
        });

        if (result[0] > 0) { // summarySheet effettivamente inserito

            // salva gli assegnamenti
            if (summarySheet.assignments.size() > 0) {
                Assignment.saveAllNewAssignments(summarySheet.id, summarySheet.assignments);
            }
        }
    }

    public static SummarySheet loadSummarySheetById(int id) {
        if (loadedSummarySheets.containsKey(id)) {
            System.out.println("hehe");
            return loadedSummarySheets.get(id);
        }

        String query = "SELECT * FROM SummarySheets sh " +
                "join Assignments a on (sh.id=a.summary_sheet_id) " +
                "WHERE sh.id=" + id;
        List<Assignment> newTasks = new ArrayList<>();
        ArrayList<Integer> cookIds = new ArrayList<>();
        ArrayList<Integer> shiftIds = new ArrayList<>();
        ArrayList<Integer> recipeIds = new ArrayList<>();
        final int[] sheetId = new int[1];
        int[] creatorId = new int[1];
        SummarySheet newSheet = new SummarySheet();
        newSheet.assignments = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                sheetId[0] = rs.getInt("sh.id");
                creatorId[0] = rs.getInt("creator_id");

                Assignment kt = new Assignment();
                kt.setId(rs.getInt("a.id"));
                kt.setToPrepare(rs.getBoolean("to_prepare"));
                kt.setQuantity(rs.getString("quantity"));
                kt.setTime(rs.getInt("time"));
                kt.setAssigned(rs.getBoolean("assigned"));
                kt.setPosition(rs.getInt("position"));

                newTasks.add(kt);
                cookIds.add(rs.getInt("cook_id"));
                shiftIds.add(rs.getInt("shift_id"));
                recipeIds.add(rs.getInt("recipe_id"));
            }
        });

        if (newTasks.isEmpty()) {
            return null;
        }

        newSheet.setId(sheetId[0]);
        newSheet.setCreator(User.loadUserById(creatorId[0]));

        int i = 0;
        newTasks.sort(Comparator.comparing(Assignment::getPosition));
        for (Assignment task : newTasks) {

            User cook = User.loadUserById(cookIds.get(i));
            task.setCook((cook.getId() > 0) ? cook : null);

            Shift shift = Shift.loadShiftById(shiftIds.get(i));
            task.setShift((shift.getId() > 0) ? shift : null);

            Recipe recipe = Recipe.loadRecipeById(recipeIds.get(i));
            task.setRecipe((recipe.getId() > 0) ? recipe : null);

            newSheet.assignments.add(task.getPosition(), task);
            i++;
        }

        loadedSummarySheets.put(id, newSheet);
        return newSheet;
    }
}
