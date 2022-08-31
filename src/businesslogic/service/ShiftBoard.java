package businesslogic.service;

import businesslogic.user.User;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ShiftBoard {
    private static final Map<Integer, ShiftBoard> loadedShiftboards = new HashMap<>();
    private int id;
    private String name;

    public ShiftBoard() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ShiftBoard loadShiftBoardById(int id) {
        if (loadedShiftboards.containsKey(id)) return loadedShiftboards.get(id);

        final ShiftBoard[] load = new ShiftBoard[1];
        String userQuery = "SELECT * FROM ShiftBoards WHERE id = " + id;
        PersistenceManager.executeQuery(userQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                load[0] = new ShiftBoard();
                load[0].id = rs.getInt("id");
                load[0].name = rs.getString("name");
                loadedShiftboards.put(id, load[0]);
            }
        });

        return load[0];
    }
}
