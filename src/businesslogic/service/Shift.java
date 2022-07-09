package businesslogic.service;

import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class Shift {
    private int id;
    private Time startTime;
    private Time endTime;

    public String toString() {
        return "start time: " + this.startTime +
                ", end time: " + this.endTime + ".";
    }

    public Shift() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static Shift loadShiftById(int sId) {
        Shift shift = new Shift();
        String query = "SELECT s.* FROM Shifts WHERE id =" + sId;
        ArrayList<Integer> availableCookIds = new ArrayList<>();
        ArrayList<Integer> assignedTaskIds = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                shift.id = rs.getInt("id");
                shift.startTime = rs.getTime("start_time");
                shift.endTime = rs.getTime("end_time");
            }
        });

        return shift;
    }
}
