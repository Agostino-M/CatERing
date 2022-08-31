package businesslogic.service;

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

    public Assignment(Recipe recipe, int position) {
        this.recipe = recipe;
        this.position = position;
        this.assigned = false;
        this.toPrepare = true;
    }

    public Assignment(Recipe recipe, String quantity, int position) {
        this.recipe = recipe;
        this.assigned = false;
        this.toPrepare = false;
        this.position = position;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "\n\t\t" + position + "°: " + recipe + (quantity != null && !quantity.equals("") ? ", " + quantity : "") +
                ", " + time + " minutes" +
                ", " + (toPrepare ? "to prepare" : "not to prepare") +
                ", " + (assigned ? "assigned to " + cook : "not assigned") +
                ", " + (shift != null ? "in shift with: " + shift : "without shift");
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
    public static void saveNewAssignment(int summarySheetId, Assignment assignment, int position) {
        String secInsert = "INSERT INTO catering.Assignments (time, quantity, to_prepare, assigned, cook_id, shift_id, recipe_id, summary_sheet_id, position)" +
                "VALUES (" +
                ((assignment.time != null) ? assignment.time : 0) + ", " +
                "'" + (assignment.quantity != null ? PersistenceManager.escapeString(assignment.quantity) : "") + "', " +
                assignment.toPrepare + ", " +
                assignment.assigned + ", " +
                (assignment.cook != null ? String.valueOf(assignment.cook.getId()) : null) + ", " +
                (assignment.shift != null ? assignment.shift.getId() : null) + ", " +
                assignment.recipe.getId() + ", " +
                summarySheetId + ", " +
                position +
                ");";
        PersistenceManager.executeUpdate(secInsert);
        assignment.id = PersistenceManager.getLastId();
    }

    public static void saveAllNewAssignments(int summarySheetId, List<Assignment> assignments) {
        String secInsert = "INSERT INTO Assignments (time, quantity, to_prepare, assigned, cook_id, shift_id, recipe_id, summary_sheet_id, position) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PersistenceManager.executeBatchUpdate(secInsert, assignments.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, (assignments.get(batchCount).time != null) ? assignments.get(batchCount).time : 0);
                ps.setObject(2, (assignments.get(batchCount).quantity != null) ? PersistenceManager.escapeString(assignments.get(batchCount).quantity) : null);
                ps.setBoolean(3, assignments.get(batchCount).toPrepare);
                ps.setBoolean(4, assignments.get(batchCount).assigned);
                ps.setObject(5, (assignments.get(batchCount).cook != null) ? assignments.get(batchCount).cook.getId() : null);
                ps.setObject(6, (assignments.get(batchCount).shift != null) ? assignments.get(batchCount).shift.getId() : null);
                ps.setInt(7, assignments.get(batchCount).recipe.getId());
                ps.setInt(8, summarySheetId);
                ps.setInt(9, batchCount);

            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                assignments.get(count).id = rs.getInt(1);
                assignments.get(count).toPrepare = assignments.get(count).toPrepare;
                assignments.get(count).assigned = assignments.get(count).assigned;
            }
        });
    }

    public static void updateAssignment(int summarySheetId, Assignment assignment) {
        String update = "UPDATE catering.Assignments SET " +
                "time = " + assignment.time + ", " +
                "quantity = '" + (assignment.quantity != null ? PersistenceManager.escapeString(assignment.quantity) : "") + "', " +
                "to_prepare = " + assignment.toPrepare + ", " +
                "assigned = " + assignment.assigned + ", " +
                "cook_id = " + ((assignment.cook != null) ? assignment.cook.getId() : null) + ", " +
                "shift_id = " + ((assignment.shift != null) ? assignment.shift.getId() : null) + ", " +
                "recipe_id = " + assignment.recipe.getId() + ", " +
                "summary_sheet_id = " + summarySheetId + ", " +
                "position = " + (assignment.getPosition()) +
                " WHERE id = " + assignment.getId();
        PersistenceManager.executeUpdate(update);
    }

    public static void deleteAssignment(int summarySheetId, Assignment assignment) {
        String del = "DELETE FROM Assignments WHERE id = " + assignment.id;
        PersistenceManager.executeUpdate(del);

        String update = "UPDATE Assignments SET position = (position -1) " +
                " WHERE position > " + assignment.getPosition() +
                " AND summary_sheet_id = " + summarySheetId +
                " ORDER BY position asc";
        PersistenceManager.executeUpdate(update);
    }
}
