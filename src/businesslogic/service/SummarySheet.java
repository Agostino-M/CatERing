package businesslogic.service;

import businesslogic.menu.MenuItem;
import businesslogic.user.User;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class SummarySheet {

    private User creator;
    private List<Assignment> assignments;

    public SummarySheet() {
    }

    public SummarySheet(User user) {
        this.creator = user;
        assignments = new ArrayList<>();
    }

    public Assignment addAssignment(Recipe recipe) {
        // TODO nel dsd 1 non setta nulla dell'assignment, nel dsd 2 invece fa assegnato = false e daPreparare = true
        // scegliere cosa fare (forse nel dsd 1 ci vuole l'assegmanento delle variabili? nel dubbio lo metto ma commentato (R 29-30)
        // TODO i nomi assegnato e daPreparare non sono in inglese... e manco ci sono nel dcd....
        // li aggiungo solo nel codice...
        //TODO EDIT: sono al dsd 5c1 e setta assegnato=false, dapreparare=false e qty (i primi due sono in italiano tra l'altro)
        Assignment assignment = new Assignment(recipe);
        //assignment.setAssigned(false);
        //assignment.setToPrepare(true);
        assignments.add(assignment);
        return assignment; // TODO credo che nel dcd il metodo è void (il valore di ritorno serve per far fare la notify al chiamante)
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

    }

    public void setAssignment(Assignment assignment, String quantity, Shift shift, Cook cook, Integer time) {
        //TODO il dubbio è: c'è bisogno di venire fino al SummarySheet per modificare l'istanza assignment?
        //la avevamo già nel serviceManager e potevamo invocare i set direttamente da là
        //penso si possa semplificare ma per adesso lascio i set qui
        //oppure lasciamo separati per dividere le responsabilità?
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

    }

    public void createReadyAssignment(Recipe recipe, String quantity) {

    }
}
