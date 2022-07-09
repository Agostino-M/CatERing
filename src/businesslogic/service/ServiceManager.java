package businesslogic.service;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuException;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;

import java.util.ArrayList;
import java.util.List;

public class ServiceManager {
    private Service currentService;
    private ArrayList<ServiceEventReceiver> serviceReceivers;

    public ServiceManager() {
        serviceReceivers = new ArrayList<>();
    }

    public SummarySheet createSummarySheet(Event event, Service service) throws UseCaseLogicException, MenuException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }

        if (SummarySheet.loadSummarySheet(service) != null) {
            throw new UseCaseLogicException("Summary sheet already exists for service with id: " + service.getId());
        }

        setCurrentService(service);

        if (currentService.getEvent().getId() != event.getId()) {
            throw new UseCaseLogicException();
        }
        if (!service.isInCharge(user)) {
            throw new UseCaseLogicException();
        }

        SummarySheet summarySheet = service.createSummarySheet(user);
        Menu menu = service.getMenu();
        List<Recipe> recipes = menu.getRecipes();

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
        if (service.getEvent().getId() != event.getId()) {
            throw new UseCaseLogicException();
        }
        if (!service.isInCharge(user)) {
            throw new UseCaseLogicException();
        }

        setCurrentService(service);
        SummarySheet summarySheet = currentService.getSummarySheet();

        if (summarySheet == null) {
            throw new UseCaseLogicException();
        }
        if (summarySheet.getCreator() != user) {
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
        if (currentService.isInCharge(user)) {
            throw new UseCaseLogicException();
        }

        Assignment assignment = summarySheet.addAssignment(recipe);

        notifyAddedAssignment(summarySheet, assignment);

        return assignment;
    }

    public SummarySheet sortSummarySheet(Assignment assignment, Integer position) throws UseCaseLogicException, ServiceException {
        SummarySheet summarySheet = currentService.getSummarySheet();

        if (summarySheet == null || !summarySheet.containAssignment(assignment)) {
            throw new UseCaseLogicException();
        }
        if (position < 0 || position >= summarySheet.getAssignments().size()) {
            throw new ServiceException();
        }

        summarySheet.moveAssignment(assignment, position);

        notifyMovedAssignment(summarySheet);

        return summarySheet;
    }

    public ShiftBoard getShiftBoard() {
        return currentService.getShiftBoard();
    }

    public Assignment setAssignment(Assignment assignment, String quantity, Shift shift, User cook, Integer time) throws UseCaseLogicException {
        SummarySheet summarySheet = currentService.getSummarySheet();

        if (summarySheet == null || !summarySheet.containAssignment(assignment)) {
            throw new UseCaseLogicException();
        }

        summarySheet.setAssignment(assignment, quantity, shift, cook, time);

        notifyUpdatedAssignment(summarySheet, assignment);

        return assignment;
    }

    public void deleteAssignment(Assignment assignment) throws UseCaseLogicException {
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

        Assignment assignment = summarySheet.addReadyAssignment(recipe, quantity);

        notifyAddedAssignment(summarySheet, assignment);

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

    private void notifyUpdatedAssignment(SummarySheet summarySheet, Assignment assignment) {
        for (ServiceEventReceiver ser : serviceReceivers) {
            ser.updateUpdatedAssignment(summarySheet, assignment);

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

    public Service loadServiceById(int serviceId) {
        return this.currentService = Service.loadServiceById(serviceId);
    }
}


