package mod.bespectacled.modernbeta.data;

import java.util.concurrent.CompletableFuture;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ModernBetaBiomeTagProvider extends FabricTagProvider<Biome> {
    public ModernBetaBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup arg) {
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.BIOME, new Identifier("is_desert")))
            .add(keyOf(ModernBetaBiomes.BETA_DESERT_ID))
            .add(keyOf(ModernBetaBiomes.BETA_ICE_DESERT_ID))
            .add(keyOf(ModernBetaBiomes.PE_DESERT_ID))
            .add(keyOf(ModernBetaBiomes.PE_ICE_DESERT_ID))
            ;
    }
    
    private static RegistryKey<Biome> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.BIOME, id);
    }
}