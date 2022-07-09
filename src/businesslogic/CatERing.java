package businesslogic;

import businesslogic.service.ServiceManager;
import businesslogic.menu.MenuManager;
import businesslogic.recipe.RecipeManager;
import businesslogic.user.UserManager;
import persistence.MenuPersistence;
import persistence.ServicePersistence;

public class CatERing {
    private static CatERing singleInstance;

    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    private MenuManager menuMgr;
    private RecipeManager recipeMgr;
    private UserManager userMgr;
    private ServiceManager serviceMgr;
    private MenuPersistence menuPersistence;
    private ServicePersistence servicePersistence;

    private CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new RecipeManager();
        userMgr = new UserManager();
        serviceMgr = new ServiceManager();
        menuPersistence = new MenuPersistence();
        menuMgr.addEventReceiver(menuPersistence);
        servicePersistence = new ServicePersistence();
        serviceMgr.addEventReceiver(servicePersistence);
    }


    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public RecipeManager getRecipeManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public ServiceManager getServiceManager() {
        return serviceMgr;
    }

}
