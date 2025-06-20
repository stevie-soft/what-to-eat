package david.buzas.whattoeat.repositories;

import david.buzas.whattoeat.repositories.utils.ItemManager;
import david.buzas.whattoeat.entities.Entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class ItemRepository<TItem extends Entity> implements Repository<TItem> {
    private final Class<TItem> itemClass;
    private final ItemManager<TItem> itemManager;
    private List<TItem> items;

    public ItemRepository(Class<TItem> itemClass, ItemManager<TItem> itemManager) {
        this.items = new ArrayList<>();
        this.itemClass = itemClass;
        this.itemManager = itemManager;
    }

    public List<TItem> getAll() throws Repository.OperationException {
        try {
            this.items = this.itemManager.readItems();
        } catch (IOException e) {
            throw new Repository.ReadOperationException(this.itemClass, e.getMessage());
        }

        return this.items;
    }

    public TItem getByUuid(UUID uuid) throws Repository.OperationException {
        return this.getBy(TItem::getUuid, uuid);
    }

    public Optional<TItem> findByUuid(UUID uuid) throws Repository.OperationException {
        return this.findBy(TItem::getUuid, uuid);
    }

    public <TReturnType> TItem getBy(Function<TItem, TReturnType> getter, TReturnType value) throws OperationException {
        Optional<TItem> item = this.findBy(getter, value);

        if (item.isEmpty()) {
            throw new Repository.OperationException(this.itemClass, "get", String.format("not found by '%s'", value));
        }

        return item.get();
    }

    public <TReturnType> Optional<TItem> findBy(Function<TItem, TReturnType> getter, TReturnType value) throws OperationException {
        return this.getAll().stream()
                .filter(item -> getter.apply(item).equals(value))
                .findFirst();
    }

    public <TReturnType> List<TItem> findManyBy(Function<TItem, TReturnType> getter, TReturnType value) throws OperationException {
        return this.getAll().stream()
                .filter(item -> getter.apply(item).equals(value))
                .toList();
    }

    public void add(TItem item) throws Repository.OperationException {
        item.setUuid(UUID.randomUUID());
        this.items.add(item);

        try {
            this.persist();
        } catch (IOException e) {
            throw new Repository.AddOperationException(this.itemClass, e.getMessage());
        }
    }

    public void update(TItem item) throws Repository.OperationException {
        TItem itemToUpdate = this.getByUuid(item.getUuid());
        int targetIndex = this.items.indexOf(itemToUpdate);
        this.items.set(targetIndex, item);

        try {
            this.persist();
        } catch (IOException e) {
            throw new Repository.UpdateOperationException(this.itemClass, e.getMessage());
        }
    }

    public void remove(TItem item) throws Repository.OperationException {
        TItem itemToRemove = this.getByUuid(item.getUuid());
        this.items.remove(itemToRemove);

        try {
            this.persist();
        } catch (IOException e) {
            throw new Repository.RemoveOperationException(this.itemClass, e.getMessage());
        }
    }

    public <TReturnType> void removeManyBy(Function<TItem, TReturnType> getter, TReturnType value) throws OperationException {
        this.items = this.getAll().stream()
                .filter((item) -> !(getter.apply(item).equals(value)))
                .toList();

        try {
            this.persist();
        } catch (IOException e) {
            throw new Repository.RemoveOperationException(this.itemClass, e.getMessage());
        }
    }

    private void persist() throws IOException {
        this.itemManager.writeItems(this.items);
    }
}
