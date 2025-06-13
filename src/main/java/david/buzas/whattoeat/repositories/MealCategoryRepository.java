package david.buzas.whattoeat.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import david.buzas.whattoeat.entities.MealCategory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MealCategoryRepository {

    private List<MealCategory> items;

    public static class NotFoundByKeyException extends Exception {
        public NotFoundByKeyException(String key) {
            super(String.format("MealCategory with key '%s' not found. ", key));
        }
    }

    public MealCategoryRepository() {
        this.items = new ArrayList<>();
    }

    public MealCategoryRepository(List<MealCategory> items) {
        this.items = items;
    }

    public MealCategoryRepository(URL jsonFileUrl) throws URISyntaxException, IOException {
        this(Paths.get(jsonFileUrl.toURI()));
    }

    public MealCategoryRepository(Path jsonFilePath) throws IOException {
        this(Files.readString(jsonFilePath));
    }

    public MealCategoryRepository(String jsonString) throws JsonProcessingException {
        this(new ObjectMapper().readValue(jsonString, new TypeReference<List<MealCategory>>(){}));
    }

    public List<MealCategory> getAll() {
        return this.items;
    }

    public MealCategory getByKey(String key) throws NotFoundByKeyException {
        Optional<MealCategory> mealCategory = this.findByKey(key);

        if (mealCategory.isEmpty()) {
            throw new NotFoundByKeyException(key);
        }

        return mealCategory.get();
    }

    public Optional<MealCategory> findByKey(String key) {
        return this.getAll().stream()
                .filter((mealCategory -> mealCategory.getKey().equals(key)))
                .findFirst();
    }
}
