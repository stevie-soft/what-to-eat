package david.buzas.whattoeat.repositories;

import david.buzas.whattoeat.entities.MealCategory;
import david.buzas.whattoeat.itemmanagement.ItemRepository;
import david.buzas.whattoeat.itemmanagement.JsonFileItemManager;


public class MealCategoryRepository extends ItemRepository<MealCategory> {

    public MealCategoryRepository(String filePathRaw) {
        super(MealCategory.class, new JsonFileItemManager<>(MealCategory.class, filePathRaw));
    }
}
