package david.buzas.whattoeat.repositories;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.utils.itemmanager.JsonFileItemManager;

public class MealRepository extends ItemRepository<Meal> {

    public MealRepository(String filePathRaw) {
        super(Meal.class, new JsonFileItemManager<>(Meal.class, filePathRaw));
    }
}
