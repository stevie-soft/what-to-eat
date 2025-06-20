package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.Repository;
import david.buzas.whattoeat.states.utils.ArrayListProperty;
import javafx.beans.property.SimpleListProperty;

import java.util.List;

public class MealTypesState extends PartialAppState {
    public final SimpleListProperty<MealType> property;

    private final Repository<MealType> repository;

    public MealTypesState(AppState appState) {
        super(appState);
        this.property = new ArrayListProperty<>();
        this.repository = this.appState.repositories.getMealTypeRepository();
    }

    public void refreshAll() throws Repository.OperationException {
        List<MealType> mealTypes = this.repository.getAll();
        this.property.setAll(mealTypes);
    }
}
