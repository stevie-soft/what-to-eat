package david.buzas.whattoeat.repositories;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.itemmanagement.ItemRepository;
import david.buzas.whattoeat.itemmanagement.JsonFileItemManager;

public class MealRepository extends ItemRepository<Meal> {

    public MealRepository() {
        super(Meal.class, new JsonFileItemManager<>(Meal.class, "user-meals.json"));
    }
}
