package mod.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ReadableContainer;

@Mixin(ChunkSection.class)
public interface MixinChunkSection {
    @Accessor("biomeContainer")
    public void setBiomeContainer(ReadableContainer<RegistryEntry<Biome>> biomeContainer);
}
