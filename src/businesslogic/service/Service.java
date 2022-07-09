package businesslogic.service;

import businesslogic.menu.Menu;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Map;

public class Service {

    private int id;
    private String name;
    private Date date;
    private Time timeStart;
    private Time timeEnd;
    private int participants;
    private SummarySheet summarySheet;
    private Menu menu;
    private ShiftBoard shiftBoard;
    private Event event;
    private User chef;

    private static Map<Integer, Service> loadedServices = FXCollections.observableHashMap();

    public Service(String name) {
        this.name = name;
    }

    public Service(Event event, User user) {
        this.event = event;
        this.chef = user;
    }

    public Service() {

    }

    public SummarySheet createSummarySheet(User user) {
        summarySheet = new SummarySheet(user);
        return summarySheet;
    }

    public int getId() {
        return id;
    }

    public SummarySheet getSummarySheet() {
        return summarySheet;
    }

    public void setSummarySheet(SummarySheet summarySheet) {
        this.summarySheet = summarySheet;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public ShiftBoard getShiftBoard() {
        return shiftBoard;
    }

    public void setShiftBoard(ShiftBoard shiftBoard) {
        this.shiftBoard = shiftBoard;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isInCharge(User user) {
        return chef.getId() == user.getId();
    }

    public User getChef() {
        return chef;
    }

    public void setChef(User chef) {
        this.chef = chef;
    }

    public String toString() {
        return name + ": " + date + " (" + timeStart + "-" + timeEnd + "), " + participants + " pp.";
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<Service> loadServicesForEvent(int event_id) {
        ObservableList<Service> result = FXCollections.observableArrayList();
        String query = "SELECT id, name, service_date, time_start, time_end, expected_participants " +
                "FROM Services WHERE event_id = " + event_id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String s = rs.getString("name");
                Service serv = new Service(s);
                serv.id = rs.getInt("id");
                serv.date = rs.getDate("service_date");
                serv.timeStart = rs.getTime("time_start");
                serv.timeEnd = rs.getTime("time_end");
                serv.participants = rs.getInt("expected_participants");
                result.add(serv);
            }
        });

        return result;
    }

    public static Service loadServiceById(int serviceId) {
        String query = "SELECT * FROM Services WHERE id=" + serviceId;
        Service ser = new Service();
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                ser.name = rs.getString("name");
                ser.id = rs.getInt("id");
                ser.participants = rs.getInt("expected_participants");
                ser.timeEnd = rs.getTime("time_end");
                ser.timeStart = rs.getTime("time_start");
                ser.participants = rs.getInt("expected_participants");
                ser.date = rs.getDate("service_date");
                int eId = rs.getInt("event_id");
                ser.event = Event.loadEventById(eId);
                int chefId = rs.getInt("chef_id");
                ser.chef = User.loadUserById(chefId);
                int menuId = rs.getInt("approved_menu_id");
                ser.menu = Menu.loadMenuById(menuId);
            }
        });

        return ser;
    }
}
