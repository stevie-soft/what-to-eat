package david.buzas.whattoeat.states;


import david.buzas.whattoeat.repositories.Repository;

public class AppState {
    public AppConfigState appConfigState;
    public RepositoriesState repositoriesState;
    public MealsState mealsState;
    public MealConsumptionsState mealConsumptionsState;
    public MealCategoriesState mealCategoriesState;
    public MealTypesState mealTypesState;
    public MealFormState mealFormState;
    public MealConsumptionFormState mealConsumptionFormState;

    public AppState() {
        this.appConfigState = new AppConfigState();
        this.appConfigState.setMealsFilePath("user-meals.json");
        this.appConfigState.setMealConsumptionsFilePath("user-consumptions.json");
        this.appConfigState.setMealCategoriesFilePath("meal-categories.json");
        this.appConfigState.setMealTypesFilePath("meal-types.json");

        this.repositoriesState = new RepositoriesState(this);
        this.mealsState = new MealsState(this);
        this.mealConsumptionsState = new MealConsumptionsState(this);
        this.mealCategoriesState = new MealCategoriesState(this);
        this.mealTypesState = new MealTypesState(this);
        this.mealFormState = new MealFormState(this);
        this.mealConsumptionFormState = new MealConsumptionFormState(this);
    }

    public void setup() throws Repository.OperationException {
        this.mealCategoriesState.refreshAll();
        this.mealTypesState.refreshAll();
        this.mealsState.refreshAll();
        this.mealConsumptionsState.refreshAll();
    }
}
