package david.buzas.whattoeat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MealRepository {
    private List<Meal> items;

    public static class NotFoundByUuidException extends Exception {
        public NotFoundByUuidException(UUID uuid) {
            super(String.format("Meal with UUID '%s' not found. ", uuid.toString()));
        }
    }

    public MealRepository() {
        this.items = new ArrayList<>();
    }

    public void loadFromFile(String jsonFilePathRaw) throws IOException {
        this.items = this.loadItemsFrom(jsonFilePathRaw);
    }

    private List<Meal> loadItemsFrom(String jsonFilePathRaw) throws IOException {
        Path jsonFilePath = Path.of(jsonFilePathRaw);
        URI jsonFileUri = jsonFilePath.toUri();
        File jsonFile = new File(jsonFileUri);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Meal>> typeReference = new TypeReference<>() {};

        try {
            return mapper.readValue(jsonFile, typeReference);
        } catch (IOException e) {
            if (Files.exists(jsonFilePath)) {
                throw e;
            }

            return new ArrayList<>();
        }
    }

    public List<Meal> getAll() throws IOException {
        return this.items;
    }

    public Meal getByUuid(UUID uuid) throws NotFoundByUuidException, IOException {
        Optional<Meal> meal = this.findByUuid(uuid);

        if (meal.isEmpty()) {
            throw new NotFoundByUuidException(uuid);
        }

        return meal.get();
    }

    public Optional<Meal> findByUuid(UUID uuid) throws IOException {
        return this.getAll().stream()
                .filter(meal -> meal.getUuid().equals(uuid))
                .findFirst();
    }

    public void add(Meal meal) throws IOException {
        meal.setUuid(UUID.randomUUID());
        this.items.add(meal);
        this.persist();
    }

    public void update(Meal meal) throws IOException, NotFoundByUuidException {
        Meal mealToUpdate = this.getByUuid(meal.getUuid());
        int targetIndex = this.items.indexOf(mealToUpdate);
        this.items.set(targetIndex, meal);
        this.persist();
    }

    public void remove(Meal meal) throws IOException, NotFoundByUuidException {
        Meal mealToDelete = this.getByUuid(meal.getUuid());
        this.items.remove(mealToDelete);
        this.persist();
    }

    private void persist() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Path jsonFilePath = Path.of("meals.json");

        if (!Files.exists(jsonFilePath)) {
            Files.createFile(jsonFilePath);
        }

        URI jsonFileUri = jsonFilePath.toUri();
        File jsonFile = new File(jsonFileUri);
        mapper.writeValue(jsonFile, this.items);
    }
}
