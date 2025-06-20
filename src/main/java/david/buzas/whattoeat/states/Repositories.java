package david.buzas.whattoeat.states;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.entities.MealConsumption;
import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.*;
import lombok.Getter;
import lombok.Setter;

public class Repositories {
    @Getter @Setter
    private Repository<Meal> mealRepository;

    @Getter @Setter
    private Repository<MealCategory> mealCategoryRepository;

    @Getter @Setter
    private Repository<MealType> mealTypeRepository;

    @Getter @Setter
    private Repository<MealConsumption> mealConsumptionRepository;

    public Repositories() {
        this.mealRepository = new MealRepository("user-meals.json");
        this.mealCategoryRepository = new MealCategoryRepository("user-consumptions.json");
        this.mealTypeRepository = new MealTypeRepository("meal-categories.json");
        this.mealConsumptionRepository = new MealConsumptionRepository("meal-types.json");
    }
}
