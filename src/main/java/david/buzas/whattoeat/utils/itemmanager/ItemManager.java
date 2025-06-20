package david.buzas.whattoeat.utils.itemmanager;

import java.io.IOException;
import java.util.List;

public interface ItemManager<TItem> {
    List<TItem> readItems() throws IOException;
    void writeItems(List<TItem> items) throws IOException;
}
