package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.repositories.Repository;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

import java.time.LocalDate;

public class MealConsumptionFormState extends PartialAppState {
    public SimpleObjectProperty<Meal> mealProperty;
    public SimpleObjectProperty<LocalDate> dateProperty;
    public SimpleObjectProperty<MealConsumption> selectedMealConsumptionProperty;
    public SimpleBooleanProperty editDisabledProperty;


    public MealConsumptionFormState(AppState appState) {
        super(appState);
        this.mealProperty = new SimpleObjectProperty<>();
        this.dateProperty = new SimpleObjectProperty<>(LocalDate.now());
        this.selectedMealConsumptionProperty = new SimpleObjectProperty<>();
        this.selectedMealConsumptionProperty.addListener(this.onSelectedMealConsumptionChange());
        this.editDisabledProperty = new SimpleBooleanProperty(true);
    }

    public void addNewMealConsumption() throws Repository.OperationException {
        MealConsumption mealConsumption = this.buildMealConsumptionFromFormValues();
        this.appState.mealConsumptionsState.add(mealConsumption);
        this.resetFormValues();
    }

    public void updateSelectedMealConsumption() throws Repository.OperationException {
        MealConsumption mealConsumption = this.buildMealConsumptionFromFormValues();
        mealConsumption.setUuid(this.selectedMealConsumptionProperty.get().getUuid());
        this.appState.mealConsumptionsState.update(mealConsumption);
        this.selectedMealConsumptionProperty.set(mealConsumption);
    }

    public void removeSelectedMealConsumption() throws Repository.OperationException {
        this.appState.mealConsumptionsState.remove(this.selectedMealConsumptionProperty.get());
        this.resetFormValues();
    }

    private void loadFormValuesFromMealConsumption(MealConsumption mealConsumption) throws Repository.OperationException {
        Meal meal = this.appState.repositoriesState.getMealRepository().getByUuid(mealConsumption.getMealUuid());
        this.mealProperty.set(meal);
        this.dateProperty.set(mealConsumption.getDate());
    }

    private void resetFormValues() {
        this.selectedMealConsumptionProperty.set(null);
        this.mealProperty.set(null);
        this.dateProperty.set(LocalDate.now());
    }

    private MealConsumption buildMealConsumptionFromFormValues() {
        MealConsumption mealConsumption = new MealConsumption();

        mealConsumption.setMealUuid(mealProperty.get().getUuid());
        mealConsumption.setDate(dateProperty.get());

        return mealConsumption;
    }

    private ChangeListener<MealConsumption> onSelectedMealConsumptionChange() {
        return (_, _, mealConsumption) -> {
            if (mealConsumption == null) {
                this.editDisabledProperty.set(true);
                return;
            }

            try {
                this.loadFormValuesFromMealConsumption(mealConsumption);
                this.editDisabledProperty.set(false);
            } catch (Repository.OperationException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
