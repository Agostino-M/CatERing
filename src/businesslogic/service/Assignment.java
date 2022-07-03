package businesslogic.service;

public class Assignment {

    private String quantity;
    private String time; //TODO check il tipo del parametro
    private boolean toPrepare;
    private boolean assigned;
    private Cook cook;
    private Shift shift;
    private Recipe recipe;


    public Assignment(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isToPrepare() {
        return toPrepare;
    }

    public void setToPrepare(boolean toPrepare) {
        this.toPrepare = toPrepare;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public Cook getCook() {
        return cook;
    }

    public void setCook(Cook cook) {
        this.cook = cook;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
