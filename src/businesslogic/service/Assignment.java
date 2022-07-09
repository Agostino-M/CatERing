package businesslogic.service;

import businesslogic.menu.Menu;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Assignment {

    private int id;
    private String quantity;
    private Integer time;
    private Integer position;
    private boolean toPrepare;
    private boolean assigned;
    private User cook;
    private Shift shift;
    private Recipe recipe;

    public Assignment() {

    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", quantity='" + quantity + '\'' +
                ", time=" + time +
                ", position=" + position +
                ", toPrepare=" + toPrepare +
                ", assigned=" + assigned +
                ", cook=" + cook +
                ", shift=" + shift +
                ", recipe=" + recipe +
                '}';
    }

    public Assignment(Recipe recipe) {
        this.recipe = recipe;
        this.assigned = false;
        this.toPrepare = true;
    }

    public Assignment(Recipe recipe, String quantity) {
        this.recipe = recipe;
        this.assigned = false;
        this.toPrepare = false;
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public boolean isToPrepare() {
        return toPrepare;
    }

    public void setToPrepare(boolean toPrepare) {
        this.toPrepare = toPrepare;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public User getCook() {
        return cook;
    }

    public void setCook(User cook) {
        this.cook = cook;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // STATIC METHODS FOR PERSISTENCE
    public static void saveNewAssignment(int summarySheetId, Assignment assignment) {
        String secInsert = "INSERT INTO catering.Assignments (time, quantity, to_prepare, assigned, cook_id, shift_id, recipe_id, summary_sheet_id)" +
                "VALUES (" +
                assignment.time + ", " +
                assignment.quantity + ", " +
                assignment.toPrepare + ", " +
                assignment.assigned + ", " +
                assignment.cook + ", " +
                assignment.shift + ", " +
                assignment.recipe + ", " +
                summarySheetId +
                ");";
        PersistenceManager.executeUpdate(secInsert);
        assignment.id = PersistenceManager.getLastId();
    }

    public static void saveAllNewAssignments(int summarySheetId, List<Assignment> assignments) {
        String secInsert = "INSERT INTO Assignments (time, quantity, to_prepare, assigned, cook_id, shift_id, recipe_id, summary_sheet_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        PersistenceManager.executeBatchUpdate(secInsert, assignments.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, assignments.get(batchCount).time);
                ps.setString(2, PersistenceManager.escapeString(assignments.get(batchCount).quantity));
                ps.setBoolean(3, assignments.get(batchCount).toPrepare);
                ps.setBoolean(4, assignments.get(batchCount).assigned);
                ps.setInt(5, assignments.get(batchCount).cook.getId());
                ps.setInt(6, assignments.get(batchCount).shift.getId());
                ps.setInt(6, assignments.get(batchCount).recipe.getId());
                ps.setInt(7, summarySheetId);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                assignments.get(count).id = rs.getInt(1);
            }
        });
    }

    public static void updateAssignment(int summarySheetId, Assignment assignment) {
        String secInsert = "UPDATE catering.Assignments SET (" +
                "time = " + assignment.time + ", " +
                "quantity = " + assignment.quantity + ", " +
                "to_prepare = " + assignment.toPrepare + ", " +
                "assigned = " + assignment.assigned + ", " +
                "cook_id = " + assignment.cook + ", " +
                "shift_id = " + assignment.shift + ", " +
                "recipe_id = " + assignment.recipe + ", " +
                "summary_sheet_id = " + summarySheetId +
                ");";
        PersistenceManager.executeUpdate(secInsert);
    }

    public static void deleteAssignment(Assignment assignment) {
        String del = "DELETE FROM Assignments WHERE id = " + assignment.id;
        PersistenceManager.executeUpdate(del);
    }
}
