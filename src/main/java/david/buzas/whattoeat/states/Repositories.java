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
        this.mealRepository = new MealRepository("data/user-meals.json");
        this.mealCategoryRepository = new MealCategoryRepository("data/meal-categories.json");
        this.mealTypeRepository = new MealTypeRepository("data/meal-types.json");
        this.mealConsumptionRepository = new MealConsumptionRepository("data/user-consumptions.json");
    }
}
