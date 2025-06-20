package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.repositories.Repository;
import david.buzas.whattoeat.states.utils.ArrayListProperty;
import javafx.beans.property.SimpleListProperty;

import java.util.List;

public class MealsState extends PartialAppState {
    public final SimpleListProperty<Meal> property;

    private final Repository<Meal> repository;

    public MealsState(AppState appState) {
        super(appState);
        this.property = new ArrayListProperty<>();
        this.repository = this.appState.repositoriesState.getMealRepository();
    }

    public void refreshAll() throws Repository.OperationException {
        List<Meal> meals = this.repository.getAll();
        this.property.setAll(meals);
    }

    public void add(Meal meal) throws Repository.OperationException {
        this.repository.add(meal);
        this.refreshAll();
    }

    public void update(Meal meal) throws Repository.OperationException {
        this.repository.update(meal);
        this.refreshAll();
    }

    public void remove(Meal meal) throws Repository.OperationException {
        this.repository.remove(meal);
        this.refreshAll();
        this.appState.mealConsumptionsState.removeManyByMeal(meal);
    }
}
