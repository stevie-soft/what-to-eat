package david.buzas.whattoeat.itemmanagement;

import david.buzas.whattoeat.entities.Meal;
import david.buzas.whattoeat.utils.itemmanager.JsonFileItemManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


class JsonFileItemManagerTest {
    @Test
    void testAutomaticCreationOfJsonFile() {
        Random rand = new Random();
        String fileName = String.format("test-file-%d.json", rand.nextLong());
        JsonFileItemManager<Object> itemManager = new JsonFileItemManager<>(Object.class, fileName);

        try {
            itemManager.writeItems(new ArrayList<>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.delete(Path.of(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCorrectnessOfSerialization() {
        String fileName = "test-file.json";
        JsonFileItemManager<Meal> itemManager = new JsonFileItemManager<>(Meal.class, fileName);

        Meal meal = new Meal();
        meal.setUuid(UUID.fromString("ef29c4a6-63a3-40fb-a0d9-691ec00b6bfb"));
        meal.setTitle("Some meal");
        meal.setCategoryKey("soup");
        meal.setTypeKey("chicken");
        meal.setConsumptionFrequencyDays(3);
        meal.setAverageCostForint(950);

        List<Meal> meals = new ArrayList<>();
        meals.add(meal);

        try {
            itemManager.writeItems(meals);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Meal> mealsFromFile;

        try {
            mealsFromFile = itemManager.readItems();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assert meals.size() == mealsFromFile.size();

        Meal mealA = meals.getFirst();
        Meal mealB = mealsFromFile.getFirst();

        assert mealA.getUuid().equals(mealB.getUuid());
        assert mealA.getTitle().equals(mealB.getTitle());
        assert mealA.getConsumptionFrequencyDays() == mealB.getConsumptionFrequencyDays();

        try {
            Files.delete(Path.of(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}