package david.buzas.whattoeat.states;


import david.buzas.whattoeat.repositories.Repository;

public class AppState {
    public Repositories repositories;
    public MealsState mealsState;
    public MealConsumptionsState mealConsumptionsState;
    public MealCategoriesState mealCategoriesState;
    public MealTypesState mealTypesState;
    public MealFormState mealFormState;
    public MealConsumptionFormState mealConsumptionFormState;
    public GenerateMealState generateMealState;

    public AppState() {
        this.repositories = new Repositories();
        this.mealsState = new MealsState(this);
        this.mealConsumptionsState = new MealConsumptionsState(this);
        this.mealCategoriesState = new MealCategoriesState(this);
        this.mealTypesState = new MealTypesState(this);
        this.mealFormState = new MealFormState(this);
        this.mealConsumptionFormState = new MealConsumptionFormState(this);
        this.generateMealState = new GenerateMealState(this);
    }

    public void setup() throws Repository.OperationException {
        this.mealCategoriesState.refreshAll();
        this.mealTypesState.refreshAll();
        this.mealsState.refreshAll();
        this.mealConsumptionsState.refreshAll();
    }
}
