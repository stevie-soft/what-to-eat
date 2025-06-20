package david.buzas.whattoeat.ui.utils;

import javafx.util.StringConverter;

import java.util.function.Function;

public class ObjectStringConverter<TObject> extends StringConverter<TObject> {
    private final Function<TObject, String> getter;
    private final String placeholderText;

    public ObjectStringConverter(Function<TObject, String> getter, String placeholderText) {
        super();
        this.getter = getter;
        this.placeholderText = placeholderText;
    }

    public ObjectStringConverter(Function<TObject, String> getter) {
        this(getter, "Válassz...");
    }

    @Override
    public String toString(TObject object) {
        return object != null ? this.getter.apply(object) : this.placeholderText;
    }

    @Override
    public TObject fromString(String s) {
        return null;
    }
}
