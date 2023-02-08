package mod.bespectacled.modernbeta.data;

import java.util.concurrent.CompletableFuture;

import mod.bespectacled.modernbeta.ModernBeta;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.WorldPresetTags;
import net.minecraft.world.gen.WorldPreset;

public class ModernBetaWorldPresetTagProvider extends FabricTagProvider<WorldPreset> {
    public static final RegistryKey<WorldPreset> MODERN_BETA = RegistryKey.of(RegistryKeys.WORLD_PRESET, ModernBeta.createId(ModernBeta.MOD_ID));
    
    public ModernBetaWorldPresetTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.WORLD_PRESET, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup arg) {
        getOrCreateTagBuilder(WorldPresetTags.NORMAL)
            .add(MODERN_BETA);
    }
}
