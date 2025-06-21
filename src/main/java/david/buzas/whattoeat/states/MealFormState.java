package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.Repository;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;

public class MealFormState extends PartialAppState {
    public SimpleStringProperty mealTitleProperty;
    public SimpleObjectProperty<MealCategory> mealCategoryProperty;
    public SimpleObjectProperty<MealType> mealTypeProperty;
    public SimpleStringProperty consumptionFrequencyDaysProperty;
    public SimpleStringProperty averageCostForintProperty;

    public SimpleObjectProperty<Meal> selectedMealProperty;
    public SimpleBooleanProperty editDisabledProperty;

    public MealFormState(AppState appState) {
        super(appState);
        this.mealTitleProperty = new SimpleStringProperty();
        this.mealCategoryProperty = new SimpleObjectProperty<>();
        this.mealTypeProperty = new SimpleObjectProperty<>();
        this.consumptionFrequencyDaysProperty = new SimpleStringProperty();
        this.averageCostForintProperty = new SimpleStringProperty();
        this.selectedMealProperty = new SimpleObjectProperty<>();
        this.selectedMealProperty.addListener(this.onSelectedMealChange());
        this.editDisabledProperty = new SimpleBooleanProperty(true);
    }

    public void addNewMeal() throws Repository.OperationException {
        Meal meal = this.buildMealFromFormValues();
        this.appState.mealsState.add(meal);
        this.resetFormValues();
    }

    public void updateSelectedMeal() throws Repository.OperationException {
        Meal meal = this.buildMealFromFormValues();
        meal.setUuid(this.selectedMealProperty.get().getUuid());
        this.appState.mealsState.update(meal);
        this.selectedMealProperty.set(meal);
    }

    public void removeSelectedMeal() throws Repository.OperationException {
        this.appState.mealsState.remove(selectedMealProperty.get());
        this.selectedMealProperty.set(null);
        this.resetFormValues();
    }

    public boolean isValid() {
        String title = this.mealTitleProperty.get();
        if (title == null || title.isEmpty()) {
            return false;
        }

        if (this.mealCategoryProperty.get() == null) {
            return false;
        }

        if (this.mealTypeProperty.get() == null) {
            return false;
        }


        String consumptionFrequencyDaysProperty = this.consumptionFrequencyDaysProperty.get();
        if (consumptionFrequencyDaysProperty == null || consumptionFrequencyDaysProperty.isEmpty()) {
            return false;
        }

        String averageCostForintProperty = this.averageCostForintProperty.get();
        if (averageCostForintProperty == null || averageCostForintProperty.isEmpty()) {
            return false;
        }

        return true;
    }

    public boolean isInvalid() {
        return !this.isValid();
    }

    private void loadFormValuesFromMeal(Meal meal) throws Repository.OperationException {
        this.mealTitleProperty.set(meal.getTitle());

        Repository<MealCategory> mealCategoryRepository = this.appState.repositories.getMealCategoryRepository();
        MealCategory mealCategory = mealCategoryRepository.getBy(MealCategory::getKey, meal.getCategoryKey());
        this.mealCategoryProperty.set(mealCategory);

        Repository<MealType> mealTypeRepository = this.appState.repositories.getMealTypeRepository();
        MealType mealType = mealTypeRepository.getBy(MealType::getKey, meal.getTypeKey());
        this.mealTypeProperty.set(mealType);

        String consumptionFrequencyDays = String.valueOf(meal.getConsumptionFrequencyDays());
        this.consumptionFrequencyDaysProperty.set(consumptionFrequencyDays);

        String averageCostForint = String.valueOf(meal.getAverageCostForint());
        this.averageCostForintProperty.set(averageCostForint);
    }

    private void resetFormValues() {
        this.selectedMealProperty.set(null);
        this.mealTitleProperty.set(null);
        this.mealCategoryProperty.set(null);
        this.mealTypeProperty.set(null);
        this.consumptionFrequencyDaysProperty.set(null);
        this.averageCostForintProperty.set(null);
    }

    private Meal buildMealFromFormValues() {
        Meal meal = new Meal();

        meal.setTitle(this.mealTitleProperty.get());
        meal.setCategoryKey(this.mealCategoryProperty.get().getKey());
        meal.setTypeKey(this.mealTypeProperty.get().getKey());
        meal.setConsumptionFrequencyDays(Integer.parseInt(this.consumptionFrequencyDaysProperty.get()));
        meal.setAverageCostForint(Integer.parseInt(this.averageCostForintProperty.get()));

        return meal;
    }

    private ChangeListener<Meal> onSelectedMealChange() {
        return (_, _, meal) -> {
            if (meal == null) {
                this.editDisabledProperty.set(true);
                return;
            }

            try {
                this.loadFormValuesFromMeal(meal);
                this.editDisabledProperty.set(false);
            } catch (Repository.OperationException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
