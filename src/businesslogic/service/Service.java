package businesslogic.service;

import businesslogic.menu.Menu;
import businesslogic.user.User;

public class Service {
    private SummarySheet summarySheet;
    private Menu menu;
    private ShiftBoard shiftBoard;
    private Event event;
    //TODO nel progetto della prof c'era ServiceInfo, quale si deve usare?

    public Service() {
    }

    public SummarySheet createSummarySheet(User user) {
        summarySheet = new SummarySheet(user);
        return summarySheet;
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
        //TODO c'è mel dcd della prof ma non l'hanno aggiunto:
        //lei lo aveva in event che ha il riferimento "manager" a user, noi l'abbiamo spostato a service (che non ha direttamente il collegamento con user)
    }
}
