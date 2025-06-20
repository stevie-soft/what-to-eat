package david.buzas.whattoeat.states;


public class AppState {
    AppConfigState appConfigState;
    RepositoriesState repositoriesState;
    MealsState mealsState;
    MealConsumptionsState mealConsumptionsState;
    MealCategoriesState mealCategoriesState;
    MealTypesState mealTypesState;
    MealFormState mealFormState;
    MealConsumptionFormState mealConsumptionFormState;

    public AppState() {
        this.appConfigState = new AppConfigState();
        this.repositoriesState = new RepositoriesState(this);
        this.mealsState = new MealsState(this);
        this.mealConsumptionsState = new MealConsumptionsState(this);
        this.mealCategoriesState = new MealCategoriesState(this);
        this.mealTypesState = new MealTypesState(this);
        this.mealFormState = new MealFormState(this);
        this.mealConsumptionFormState = new MealConsumptionFormState(this);
    }
}
