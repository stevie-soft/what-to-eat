package david.buzas.whattoeat;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonFileItemManager<TItem> implements ItemManager<TItem> {
    private final Path filePath;
    private final File file;
    private final ObjectMapper mapper;
    private final JavaType typeRef;

    JsonFileItemManager(Class<TItem> itemClass, String filePathRaw) {
        this.filePath = Path.of(filePathRaw);
        URI fileUri = this.filePath.toUri();
        this.file = new File(fileUri);
        this.mapper = new ObjectMapper();
        TypeFactory typeFactory = this.mapper.getTypeFactory();
        this.typeRef = typeFactory.constructCollectionType(List.class, itemClass);
    }

    public List<TItem> readItems() throws IOException {
        this.ensureFileExists();
        return this.mapper.readValue(this.file, this.typeRef);
    }

    public void writeItems(List<TItem> items) throws IOException {
        this.ensureFileExists();
        this.mapper.writeValue(this.file, items);
    }

    private void ensureFileExists() throws IOException {
        if (Files.exists(this.filePath)) {
            return;
        }

        Files.createFile(this.filePath);
        this.writeItems(new ArrayList<>());
    }
}
