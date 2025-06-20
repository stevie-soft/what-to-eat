package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.*;
import lombok.Getter;
import lombok.Setter;

public class RepositoriesState extends PartialAppState {
    @Getter @Setter
    private Repository<Meal> mealRepository;

    @Getter @Setter
    private Repository<MealCategory> mealCategoryRepository;

    @Getter @Setter
    private Repository<MealType> mealTypeRepository;

    @Getter @Setter
    private Repository<MealConsumption> mealConsumptionRepository;

    public RepositoriesState(AppState appState) {
        super(appState);

        String mealsFilePath = appState.appConfigState.getMealsFilePath();
        String mealCategoriesFilePath = appState.appConfigState.getMealCategoriesFilePath();
        String mealTypeFilePath = appState.appConfigState.getMealTypesFilePath();
        String mealConsumptionsFilePath = appState.appConfigState.getMealConsumptionsFilePath();

        this.mealRepository = new MealRepository(mealsFilePath);
        this.mealCategoryRepository = new MealCategoryRepository(mealCategoriesFilePath);
        this.mealTypeRepository = new MealTypeRepository(mealTypeFilePath);
        this.mealConsumptionRepository = new MealConsumptionRepository(mealConsumptionsFilePath);
    }
}
