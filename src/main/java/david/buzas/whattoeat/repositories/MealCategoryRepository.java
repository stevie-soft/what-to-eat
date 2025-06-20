package david.buzas.whattoeat.repositories;

import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.utils.itemmanager.JsonFileItemManager;


public class MealCategoryRepository extends ItemRepository<MealCategory> {

    public MealCategoryRepository(String filePathRaw) {
        super(MealCategory.class, new JsonFileItemManager<>(MealCategory.class, filePathRaw));
    }
}
