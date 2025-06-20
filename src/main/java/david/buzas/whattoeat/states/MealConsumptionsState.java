package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.repositories.Repository;
import david.buzas.whattoeat.states.utils.ArrayListProperty;
import javafx.beans.property.SimpleListProperty;

import java.util.List;

public class MealConsumptionsState extends PartialAppState {
    public final SimpleListProperty<MealConsumption> property;

    private final Repository<MealConsumption> repository;

    public MealConsumptionsState(AppState appState) {
        super(appState);
        this.property = new ArrayListProperty<>();
        this.repository = this.appState.repositories.getMealConsumptionRepository();
    }

    public void refreshAll() throws Repository.OperationException {
        List<MealConsumption> mealConsumptions = this.repository.getAll();
        this.property.setAll(mealConsumptions);
    }

    public void add(MealConsumption mealConsumption) throws Repository.OperationException {
        this.repository.add(mealConsumption);
        this.refreshAll();
    }

    public void update(MealConsumption mealConsumption) throws Repository.OperationException {
        this.repository.update(mealConsumption);
        this.refreshAll();
    }

    public void remove(MealConsumption mealConsumption) throws Repository.OperationException {
        this.repository.remove(mealConsumption);
        this.refreshAll();
    }

    public void removeManyByMeal(Meal meal) throws Repository.OperationException {
        this.repository.removeManyBy(MealConsumption::getMealUuid, meal.getUuid());
        this.refreshAll();
    }

}
