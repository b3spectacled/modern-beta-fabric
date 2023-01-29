package mod.bespectacled.modernbeta.util.settings;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;

public class ImmutableSettings implements Settings {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final Codec<ImmutableSettings> CODEC = Codec.PASSTHROUGH.comapFlatMap(
        dynamic -> {
            NbtElement element = dynamic.convert(NbtOps.INSTANCE).getValue();
            if (element instanceof NbtCompound compound) {
                return DataResult.success(new ImmutableSettings(compound));
            }
            
            return DataResult.error("Not a compound tag: " + element);
        },
        settings -> new Dynamic(NbtOps.INSTANCE, settings.getNbt())
    );
    
    private final Map<String, NbtElement> entries = new LinkedHashMap<>();
    
    public ImmutableSettings() {}
    
    public ImmutableSettings(NbtCompound initial) {
        for (String key : initial.getKeys()) {
            this.entries.put(key, initial.get(key));
        }
    }
    
    @Override
    public void put(String key, NbtElement element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putCompound(NbtCompound compound) {
        throw new UnsupportedOperationException();
        
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(String key) {
        return this.entries.containsKey(key);
    }

    @Override
    public NbtElement get(String key) {
        return this.entries.get(key);
    }
    
    @Override
    public Set<String> keySet() {
        return this.entries.keySet();
    }
    
    private NbtCompound getNbt() {
        NbtCompound compound = new NbtCompound();
        
        for (Entry<String, NbtElement> change : this.entries.entrySet()) {
            compound.put(change.getKey(), change.getValue().copy());
        }
        
        return compound;
    }
    
    public static ImmutableSettings copyOf(Settings settings) {
        NbtCompound compound = new NbtCompound();
        
        for (String key : settings.keySet()) {
            compound.put(key, settings.get(key));
        }
        
        return new ImmutableSettings(compound);
    }
}
