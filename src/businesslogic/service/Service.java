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
import java.util.HashMap;
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

    private static final Map<Integer, Service> loadedServices = new HashMap<>();

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

    @Override
    public String toString() {
        return name + " " + date + " from " + timeStart + " to " + timeEnd + " " + participants + " pp." + "\n" +
                " \tsummary Sheet: " + summarySheet + "\n" +
                " \tmenu: " + menu + "\n" +
                " \tshift board: " + shiftBoard + "\n" +
                " \tevent: " + event + "\n" +
                " \tchef: " + chef;
    }

// STATIC METHODS FOR PERSISTENCE

    public static ObservableList<Service> loadServicesByEvent(int event_id) {
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
        if (loadedServices.containsKey(serviceId)) {
            return loadedServices.get(serviceId);
        }

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
                ser.date = rs.getDate("service_date");
                int eId = rs.getInt("event_id");
                ser.event = Event.loadEventById(eId);
                int chefId = rs.getInt("chef_id");
                ser.chef = User.loadUserById(chefId);
                int menuId = rs.getInt("approved_menu_id");
                ser.menu = Menu.loadMenuById(menuId);
                int sumId = rs.getInt("summary_sheet_id");
                ser.summarySheet = SummarySheet.loadSummarySheetById(sumId);
                int shiftId = rs.getInt("shift_board_id");
                ser.shiftBoard = ShiftBoard.loadShiftBoardById(shiftId);
            }
        });

        if (ser.id > 0) {
            loadedServices.put(ser.id, ser);
        }

        return ser;
    }

    public static void updateService(Service service) {
        String upd = "UPDATE Services SET summary_sheet_id = " + service.summarySheet.getId() +
                " WHERE id = " + service.getId();
        PersistenceManager.executeUpdate(upd);
    }

}
