package david.buzas.whattoeat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MealTypeRepository {
    private List<MealType> items;

    public static class NotFoundByKeyException extends Exception {
        public NotFoundByKeyException(String key) {
            super(String.format("MealType with key '%s' not found. ", key));
        }
    }

    public MealTypeRepository() {
        this.items = new ArrayList<>();
    }

    public MealTypeRepository(List<MealType> items) {
        this.items = items;
    }

    public MealTypeRepository(URL jsonFileUrl) throws URISyntaxException, IOException {
        this(Paths.get(jsonFileUrl.toURI()));
    }

    public MealTypeRepository(Path jsonFilePath) throws IOException {
        this(Files.readString(jsonFilePath));
    }

    public MealTypeRepository(String jsonString) throws JsonProcessingException {
        this(new ObjectMapper().readValue(jsonString, new TypeReference<List<MealType>>(){}));
    }

    public List<MealType> getAll() {
        return this.items;
    }

    public MealType getByKey(String key) throws NotFoundByKeyException {
        Optional<MealType> mealType = this.findByKey(key);

        if (mealType.isEmpty()) {
            throw new NotFoundByKeyException(key);
        }

        return mealType.get();
    }

    public Optional<MealType> findByKey(String key) {
        return this.getAll().stream()
                .filter((mealType -> mealType.getKey().equals(key)))
                .findFirst();
    }
}
