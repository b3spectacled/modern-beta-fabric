package mod.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.bespectacled.modernbeta.client.gui.screen.ModernBetaWorldScreen;
import mod.bespectacled.modernbeta.client.gui.screen.ModernBetaWorldScreenProvider;
import mod.bespectacled.modernbeta.world.preset.ModernBetaWorldPresets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.LevelScreenProvider;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.WorldPreset;

@Environment(EnvType.CLIENT)
@Mixin(WorldCreator.class)
public abstract class MixinWorldCreator {
    @Inject(method = "getLevelScreenProvider", at = @At("RETURN"), cancellable = true)
    public void injectGetLevelScreenProvider(CallbackInfoReturnable<LevelScreenProvider> info) {
        RegistryEntry<WorldPreset> preset = this.getWorldType().preset();
        RegistryKey<WorldPreset> modernBeta = ModernBetaWorldPresets.MODERN_BETA;
        
        if (preset != null && preset.getKey().isPresent() && preset.getKey().get().equals(modernBeta)) {
            info.setReturnValue(
                (parent, generatorOptionsHolder) -> {
                    return new ModernBetaWorldScreen(
                        parent,
                        generatorOptionsHolder,
                        (settingsChunk, settingsBiome, settingsCaveBiome) -> parent.getWorldCreator().applyModifier(
                            ModernBetaWorldScreenProvider.createModifier(
                                settingsChunk,
                                settingsBiome,
                                settingsCaveBiome
                            )
                        )
                    );
                }
            );
        }
    }
    
    @Shadow
    public abstract WorldCreator.WorldType getWorldType();
}
