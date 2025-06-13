package david.buzas.whattoeat.repositories;

import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.itemmanagement.ItemRepository;
import david.buzas.whattoeat.itemmanagement.JsonFileItemManager;


public class MealCategoryRepository extends ItemRepository<MealCategory> {
    public MealCategoryRepository() {
        super(MealCategory.class, new JsonFileItemManager<>(MealCategory.class, "meal-categories.json"));
    }
}
