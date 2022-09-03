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
    private final ArrayList<ServiceEventReceiver> serviceReceivers;

    public ServiceManager() {
        serviceReceivers = new ArrayList<>();
    }

    public SummarySheet createSummarySheet(Event event, Service service) throws UseCaseLogicException, MenuException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }

        setCurrentService(service);

        if (currentService.getEvent().getId() != event.getId()) {
            throw new UseCaseLogicException();
        }
        if (!service.isInCharge(user)) {
            throw new UseCaseLogicException();
        }
        if (currentService.getSummarySheet() != null) {
            throw new UseCaseLogicException();
        }

        SummarySheet summarySheet = currentService.createSummarySheet(user);
        Menu menu = currentService.getMenu();

        List<Recipe> recipes = menu.getRecipes();

        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            summarySheet.addAssignment(recipe, i);
        }

        notifySummarySheetCreated(currentService, summarySheet);
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
        SummarySheet summarySheet = service.getSummarySheet();

        if (summarySheet == null) {
            throw new UseCaseLogicException();
        }
        if (summarySheet.getCreator() != user) {
            throw new UseCaseLogicException();
        }

        return summarySheet;
    }

    public Assignment addAssignment(Recipe recipe, int position) throws UseCaseLogicException, SummarySheetException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        SummarySheet summarySheet = currentService.getSummarySheet();

        if (summarySheet == null) {
            throw new UseCaseLogicException();
        }
        if (!currentService.isInCharge(user)) {
            throw new UseCaseLogicException();
        }
        if (position < 0 || position >= summarySheet.getAssignments().size()) {
            throw new SummarySheetException();
        }

        Assignment assignment = summarySheet.addAssignment(recipe, position);

        notifyAddedAssignment(summarySheet, assignment, position);

        return assignment;
    }

    public void sortSummarySheet(Assignment assignment, Integer position) throws UseCaseLogicException, SummarySheetException {
        SummarySheet summarySheet = currentService.getSummarySheet();

        if (summarySheet == null || !summarySheet.containAssignment(assignment)) {
            throw new UseCaseLogicException();
        }
        if (position <= 0 || position >= summarySheet.getAssignments().size()) {
            throw new SummarySheetException();
        }

        summarySheet.moveAssignment(assignment, position);

        notifyMovedAssignment(summarySheet, assignment, position);
    }

    public ShiftBoard getShiftBoard() throws ShiftBoardException {
        ShiftBoard shiftBoard = currentService.getShiftBoard();

        if (shiftBoard == null) {
            throw new ShiftBoardException();
        }

        return shiftBoard;
    }

    public void setAssignment(Assignment assignment, String quantity, Shift shift, User cook, Integer time) throws UseCaseLogicException {
        SummarySheet summarySheet = currentService.getSummarySheet();

        if (summarySheet == null || !summarySheet.containAssignment(assignment)) {
            throw new UseCaseLogicException();
        }

        summarySheet.setAssignment(assignment, quantity, shift, cook, time);

        notifyUpdatedAssignment(summarySheet, assignment);
    }

    public void deleteAssignment(Assignment assignment) throws UseCaseLogicException {
        SummarySheet summarySheet = currentService.getSummarySheet();
        if (summarySheet == null || !summarySheet.containAssignment(assignment)) {
            throw new UseCaseLogicException();
        }

        summarySheet.deleteAssignment(assignment);

        notifyDeleteAssignment(summarySheet, assignment);
    }

    public Assignment createReadyAssignment(Recipe recipe, String quantity, int position) throws UseCaseLogicException {
        SummarySheet summarySheet = currentService.getSummarySheet();
        if (summarySheet == null) {
            throw new UseCaseLogicException();
        }

        Assignment assignment = summarySheet.addReadyAssignment(recipe, quantity, position);

        notifyAddedAssignment(summarySheet, assignment, position);

        return assignment;
    }

    private void notifySummarySheetCreated(Service service, SummarySheet summarySheet) {
        for (ServiceEventReceiver ser : serviceReceivers) {
            ser.updateSummarySheetCreated(service, summarySheet);
        }
    }

    private void notifyAddedAssignment(SummarySheet summarySheet, Assignment assignment, int position) {
        for (ServiceEventReceiver ser : serviceReceivers) {
            ser.updateAddedAssignment(summarySheet, assignment, position);
        }
    }

    private void notifyMovedAssignment(SummarySheet summarySheet, Assignment assignment, int position) {
        for (ServiceEventReceiver ser : serviceReceivers) {
            ser.updateMovedAssignment(summarySheet, assignment, position);
        }
    }

    private void notifyUpdatedAssignment(SummarySheet summarySheet, Assignment assignment) {
        for (ServiceEventReceiver ser : serviceReceivers) {
            ser.updateUpdatedAssignment(summarySheet, assignment);

        }
    }

    private void notifyDeleteAssignment(SummarySheet summarySheet, Assignment assignment) {
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


