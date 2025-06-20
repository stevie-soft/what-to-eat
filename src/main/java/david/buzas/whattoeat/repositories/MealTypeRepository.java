package david.buzas.whattoeat.repositories;

import david.buzas.whattoeat.entities.MealType;
import david.buzas.whattoeat.repositories.utils.JsonFileItemManager;

public class MealTypeRepository extends ItemRepository<MealType> {

    public MealTypeRepository(String filePathRaw) {
        super(MealType.class, new JsonFileItemManager<>(MealType.class, filePathRaw));
    }
}
