package businesslogic.service;

import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Event {

    private int id;
    private String name;
    private User manager;
    private Date dateStart;
    private Date dateEnd;
    private int participants;

    private ObservableList<Service> services;

    public Event() {
    }

    public Event(String name) {
        this.name = name;
        id = 0;
    }

    public ObservableList<Service> getServices() {
        return FXCollections.unmodifiableObservableList(this.services);
    }

    public String toString() {
        return name + ": " + dateStart + "-" + dateEnd + ", " + participants + " pp. (" + manager.getUserName() + ")";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<Event> loadAllEvent() {
        ObservableList<Event> all = FXCollections.observableArrayList();
        String query = "SELECT * FROM Events WHERE true";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String n = rs.getString("name");
                Event e = new Event(n);
                e.id = rs.getInt("id");
                e.dateStart = rs.getDate("date_start");
                e.dateEnd = rs.getDate("date_end");
                e.participants = rs.getInt("expected_participants");
                int org = rs.getInt("organizer_id");
                e.manager = User.loadUserById(org);
                all.add(e);
            }
        });

        for (Event e : all) {
            e.services = Service.loadServicesForEvent(e.id);
        }
        return all;
    }

    public static Event loadEventById(int eventId) {
        ObservableList<Event> all = FXCollections.observableArrayList();
        String query = "SELECT * FROM Events WHERE id=" + eventId;
        Event ev = new Event();
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                ev.name = rs.getString("name");
                ev.id = rs.getInt("id");
                ev.dateStart = rs.getDate("date_start");
                ev.dateEnd = rs.getDate("date_end");
                ev.participants = rs.getInt("expected_participants");
                int org = rs.getInt("organizer_id");
                ev.manager = User.loadUserById(org);
                all.add(ev);
            }
        });

        for (Event e : all) {
            e.services = Service.loadServicesForEvent(e.id);
        }
        return ev;
    }
}
