package businesslogic.service;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import businesslogic.user.User;

import java.util.ArrayList;
import java.util.List;

public class ServiceManager {
    private Service currentService;
    private ArrayList<ServiceEventReceiver> serviceReceivers;

    public ServiceManager() {
        serviceReceivers = new ArrayList<>();
    }

    public SummarySheet createEventSheet(Event event, Service service) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }

        setCurrentService(service);

        if (currentService.getEvent() != event) {//TODO si fa con == o tipo compare?
            throw new UseCaseLogicException();
        }
        if (!service.isInCharge(user)) {
            throw new UseCaseLogicException();
        }

        SummarySheet summarySheet = service.createSummarySheet(user);
        Menu menu = service.getMenu();
        List<Recipe> recipes = menu.getRecipes(); //TODO nel dcd c'era già dalla prof

        for (Recipe recipe : recipes) {
            summarySheet.addAssignment(recipe);
        }

        notifySummarySheetCreated(service, summarySheet);
        return summarySheet;
    }

    public SummarySheet openExistingSummarySheet(Event event, Service service) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if (service.getEvent() != event) { // TODO usare != o compare?
            throw new UseCaseLogicException();
        }
        if (!service.isInCharge(user)) { // TODO si potrebbero unificare tutte le eccezioni uguali nel dsd (adesso sto solo mettendo UseCaseLogicException, poi si vede di creare quelle giuste
            throw new UseCaseLogicException();
        }

        setCurrentService(service);
        SummarySheet summarySheet = currentService.getSummarySheet();

        if (summarySheet == null) {
            throw new UseCaseLogicException();
        }
        if (summarySheet.getCreator() != user) { // TODO usare .getCreator nel dsd invece che .creator ?
            throw new UseCaseLogicException();
        }

        return summarySheet;
    }

    public Assignment addAssigment(Recipe recipe) throws UseCaseLogicException {
        SummarySheet summarySheet = currentService.getSummarySheet();
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if (summarySheet == null) {
            throw new UseCaseLogicException();
        }
        if (currentService.isInCharge(user)) { // TODO nel dsd manca la chiamata che in riga 77 ho aggiunto
            throw new UseCaseLogicException();
        }

        Assignment assignment = summarySheet.addAssignment(recipe);

        notifyAddedAssignment(summarySheet, assignment); // TODO nel dsd 2 c'è un parametro di troppo (currentService)! controllare anche i dcd

        return assignment;
    }

    public SummarySheet sortSummarySheet(Assignment assignment, Integer position) throws UseCaseLogicException {
        SummarySheet summarySheet = currentService.getSummarySheet();

        if (summarySheet == null || !summarySheet.containAssignment(assignment)) {
            throw new UseCaseLogicException();
        }
        if (position < 0 || position >= summarySheet.getAssignments().size()) { //TODO secondo me il controllo sulla posizione è un po troppo di basso livello per l'uc, non so se lo terrei
            throw new UseCaseLogicException(); //TODO non sarebbe sicuramente una UseCaseLogicException se dovesse rimanere
        }

        summarySheet.moveAssignment(assignment, position);

        notifyMovedAssignment(summarySheet); //TODO l'ho rinominato rispetto al dsd e poi manca nel dcd

        return summarySheet;
    }

    public ShiftBoard getShiftBoard() {
        // TODO MOLTO IMPORTANTE: penso che sia un bel pattern singleton, il tabellone dei turni è unico (non ricordo se è unico per CatERing o solo per il servizio / evento)
        // potrebbe essere un modulo a parte? ha un suo controller grasp? nel dubbio non faccio il metodo
        return null;
    }

    public Assignment setAssignment(Assignment assignment, String quantity, Shift shift, Cook cook, Integer time) throws UseCaseLogicException {
        SummarySheet summarySheet = currentService.getSummarySheet();
        //TODO nel dsd ci sono due condizioni nell'alt che possono essere unificate in uno solo
        // qui metto la versione ridotta unificata
        if (summarySheet == null || !summarySheet.containAssignment(assignment)) {
            throw new UseCaseLogicException();
        }

        summarySheet.setAssignment(assignment, quantity, shift, cook, time); //TODO dubbio, entra in setAssignment per leggere

        notifyModifiedAssignment(summarySheet, assignment); // TODO nel dsd si chiama in altro modo (e non c'è quel modo) scegli tu quale

        return assignment;
    }

    public Assignment modifyAssignment(Assignment assignment, String quantity, Shift shift, Cook cook, Integer time) throws UseCaseLogicException {
        //TODO è identica a setAssignment, si potrebbe semplificare uc dettagliato, ssd, contratti e implementazione
        //TODO nel dsd ci sono due condizioni nell'alt che possono essere unificate in uno solo
        // qui metto la versione ridotta unificata
        SummarySheet summarySheet = currentService.getSummarySheet();
        if (summarySheet == null || !summarySheet.containAssignment(assignment)) {
            throw new UseCaseLogicException();
        }

        summarySheet.setAssignment(assignment, quantity, shift, cook, time); //TODO nel dsd ci mancano tutti i parametri in questa chiamata e poi c'è un dubbio, entra in setAssignment per leggere

        notifyModifiedAssignment(summarySheet, assignment); // TODO nel dsd si chiama in altro modo (e non c'è quel modo) scegli tu quale

        return assignment;
    }

    public void deleteAssignment(Assignment assignment) throws UseCaseLogicException {
        //TODO nel dsd ci sono due condizioni nell'alt che possono essere unificate in uno solo
        // qui metto la versione ridotta unificata
        SummarySheet summarySheet = currentService.getSummarySheet();
        if (summarySheet == null || !summarySheet.containAssignment(assignment)) {
            throw new UseCaseLogicException();
        }

        summarySheet.deleteAssignment(assignment);

        notifyDeleteAssignement(summarySheet, assignment);
    }

    public Assignment createReadyAssignment(Recipe recipe, String quantity) throws UseCaseLogicException {
        SummarySheet summarySheet = currentService.getSummarySheet();
        if (summarySheet == null) {
            throw new UseCaseLogicException();
        }

        Assignment assignment = summarySheet.addAssignment(recipe); //TODO nel dsd non passiamo la qty. addassignment di per se non prende qty
        //TODO si deve usare un altro metodo addReadyAssignment? c'è un modo per avere attributi opzionali?
        //si aggiunge qty alla funzione e chi non lo passava passa null? boh
        //TODO EDIT: in setAssignment ogni volta, in base al dsd, si settano cose diverse, forse in questo caso conviene usare un altro metodo
        //infatti abbiamo tipo daPreparare false che solitamente in ogni creazione è true

        notifyAddedAssignment(summarySheet, assignment); //TODO nel dsd c'è la notify del delete! (e anche update)

        return assignment;
    }

    private void notifySummarySheetCreated(Service service, SummarySheet summarySheet) {
        for (ServiceEventReceiver ser : serviceReceivers) {
            ser.updateSummarySheetCreated(service, summarySheet);
        }
    }

    private void notifyAddedAssignment(SummarySheet summarySheet, Assignment assignment) {
        for (ServiceEventReceiver ser : serviceReceivers) {
            ser.updateAddedAssignment(summarySheet, assignment);
        }
    }

    private void notifyMovedAssignment(SummarySheet summarySheet) {
        for (ServiceEventReceiver ser : serviceReceivers) {
            ser.updateMovedAssignment(summarySheet);
        }
    }

    private void notifyModifiedAssignment(SummarySheet summarySheet, Assignment assignment) {
        for (ServiceEventReceiver ser : serviceReceivers) {
            ser.updateModifiedAssignment(summarySheet, assignment);

        }
    }

    private void notifyDeleteAssignement(SummarySheet summarySheet, Assignment assignment) {
        for (ServiceEventReceiver ser : serviceReceivers) {
            ser.updateDeleteAssignment(summarySheet, assignment);
        }
    }

    public Service getCurrentService() {
        return currentService;
    }

    public void setCurrentService(Service currentService) {
        this.currentService = currentService;
    }

    public void addEventReceiver(ServiceEventReceiver rec) {
        this.serviceReceivers.add(rec);
    }

    public void removeEventReceiver(ServiceEventReceiver rec) {
        this.serviceReceivers.remove(rec);
    }
}


