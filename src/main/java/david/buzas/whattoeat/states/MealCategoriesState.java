package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.repositories.Repository;
import david.buzas.whattoeat.states.utils.ArrayListProperty;
import javafx.beans.property.SimpleListProperty;

import java.util.List;

public class MealCategoriesState extends PartialAppState {
    public final SimpleListProperty<MealCategory> property;

    private final Repository<MealCategory> repository;

    public MealCategoriesState(AppState appState) {
        super(appState);
        this.property = new ArrayListProperty<>();
        this.repository = this.appState.repositoriesState.getMealCategoryRepository();
    }

    public void refreshAll() throws Repository.OperationException {
        List<MealCategory> mealCategories = this.repository.getAll();
        this.property.setAll(mealCategories);
    }
}
