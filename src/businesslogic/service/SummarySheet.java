package businesslogic.service;

import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SummarySheet {

    private int id;
    private User creator;
    private List<Assignment> assignments;

    @Override
    public String toString() {
        return "SummarySheet{" +
                "id=" + id +
                ", creator=" + creator +
                ", assignments=" + assignments +
                '}';
    }

    public SummarySheet() {
    }

    public SummarySheet(User user) {
        this.creator = user;
        assignments = new ArrayList<>();
    }

    public List<Assignment> getAllAssignment() {
        return this.assignments;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Assignment addAssignment(Recipe recipe) {
        Assignment assignment = new Assignment(recipe);
        assignments.add(assignment);
        return assignment;
    }

    public Assignment addReadyAssignment(Recipe recipe, String quantity) {
        Assignment assignment = new Assignment(recipe, quantity);
        assignments.add(assignment);
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
        String summarySheetInsert = "INSERT INTO catering.SummarySheets (creator_id, service_id) VALUES (?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(summarySheetInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, service.getChef().getId());
                ps.setInt(2, service.getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    summarySheet.id = rs.getInt(1);
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

    public static void saveAssignmentOrder(SummarySheet summarySheet) {
        String upd = "UPDATE Assignments SET position = ? WHERE id = ?";
        PersistenceManager.executeBatchUpdate(upd, summarySheet.assignments.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, batchCount);
                ps.setInt(2, summarySheet.assignments.get(batchCount).getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) {
                // no generated ids to handle
            }
        });
    }

    public static SummarySheet loadSummarySheet(Service s) {
        String query = "SELECT * FROM SummarySheets sh " +
                "join Assignments a on (sh.id=a.summary_sheet_id) " +
                "WHERE sh.service_id=" + s.getId();
        List<Assignment> newTasks = new ArrayList<>();
        ArrayList<Integer> cookIds = new ArrayList<>();
        ArrayList<Integer> shiftIds = new ArrayList<>();
        ArrayList<Integer> recipeIds = new ArrayList<>();
        final int[] sheetId = new int[1];

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                sheetId[0] = rs.getInt("sh.id");
                Assignment kt = new Assignment();
                kt.setId(rs.getInt("a.summary_sheet_id"));
                kt.setToPrepare(rs.getBoolean("prepared"));
                kt.setQuantity(rs.getString("quantity"));
                kt.setTime(rs.getInt("time_required"));
                kt.setAssigned(rs.getBoolean("assigned"));
                kt.setPosition(rs.getInt("position"));

                newTasks.add(kt);
                cookIds.add(rs.getInt("cook_id"));
                shiftIds.add(rs.getInt("shift_id"));
                recipeIds.add(rs.getInt("recipe_id"));
            }
        });

        SummarySheet newSheet = new SummarySheet();

        int i = 0;
        for (Assignment task : newTasks) {

            User cook = User.loadUserById(cookIds.get(i));
            task.setCook((cook.getId() > 0) ? cook : null);

            Shift shift = Shift.loadShiftById(shiftIds.get(i));
            task.setShift((shift.getId() > 0) ? shift : null);

            Recipe recipe = Recipe.loadRecipeById(recipeIds.get(i));
            task.setRecipe((recipe.getId() > 0) ? recipe : null);

            newSheet.assignments.add(task);
            i++;
        }

        return newSheet;
    }

    public static void saveKitchenTasksOrder(SummarySheet sheet) {
        String upd = "UPDATE Assignment SET position = ? WHERE id = ?";
        PersistenceManager.executeBatchUpdate(upd, sheet.assignments.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, batchCount);
                ps.setInt(2, sheet.assignments.get(batchCount).getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // no generated ids to handle
            }
        });
    }

}
