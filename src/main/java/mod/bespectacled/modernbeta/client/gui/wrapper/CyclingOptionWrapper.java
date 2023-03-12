package mod.bespectacled.modernbeta.client.gui.wrapper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import mod.bespectacled.modernbeta.api.client.gui.wrapper.OptionWrapper;
import mod.bespectacled.modernbeta.client.gui.option.ExtendedCyclingOption;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class CyclingOptionWrapper<T> implements OptionWrapper {
    private final String key;
    private final T[] collection;
    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private final Function<T, Formatting> formatting;
    private final Function<T, List<OrderedText>> tooltips;
    
    public CyclingOptionWrapper(
        String key, 
        T[] collection,
        Supplier<T> getter, 
        Consumer<T> setter, 
        Function<T, Formatting> formatting,
        Function<T, List<OrderedText>> tooltips
    ) {
        this.key = key;
        this.collection = collection;
        this.getter = getter;
        this.setter = setter;
        this.formatting = formatting;
        this.tooltips = tooltips;
    }
    
    public CyclingOptionWrapper(String key, T[] collection, Supplier<T> getter, Consumer<T> setter) {
        this(key, collection, getter, setter, value -> Formatting.RESET, value -> ImmutableList.of());
    }
    
    public CyclingOptionWrapper(String key, T[] collection, Supplier<T> getter, Consumer<T> setter, Function<T, Formatting> formatting) {
        this(key, collection, getter, setter, formatting, value -> ImmutableList.of());
    }
    
    @Override
    public CyclingOption<T> create() {
        CyclingOption<T> cyclingOption = CyclingOption.create(
            this.key,
            this.collection,
            value -> new TranslatableText(this.key + "." + value.toString().toLowerCase()).formatted(this.formatting.apply(value)), 
            gameOptions -> this.getter.get(),
            (gameOptions, option, value) -> this.setter.accept(value)
        ).tooltip(client -> value -> this.tooltips.apply(value));
        
        return cyclingOption;
    }
    
    @Override
    public CyclingOption<T> create(boolean active) {
        CyclingOption<T> cyclingOption = ExtendedCyclingOption.create(
            this.key,
            this.collection,
            value -> new TranslatableText(this.key + "." + value.toString().toLowerCase()).formatted(this.formatting.apply(value)), 
            gameOptions -> this.getter.get(),
            (gameOptions, option, value) -> this.setter.accept(value),
            active
        ).tooltip(client -> value -> this.tooltips.apply(value));
        
        return cyclingOption;
    }
}
