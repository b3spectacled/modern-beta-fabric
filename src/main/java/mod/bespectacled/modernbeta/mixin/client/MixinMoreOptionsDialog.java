package mod.bespectacled.modernbeta.mixin.client;

import java.util.Optional;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mod.bespectacled.modernbeta.client.gui.screen.ModernBetaWorldScreen;
import mod.bespectacled.modernbeta.client.gui.screen.ModernBetaWorldScreenProvider;
import mod.bespectacled.modernbeta.world.preset.ModernBetaWorldPresets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.LevelScreenProvider;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.WorldPreset;

@Environment(EnvType.CLIENT)
@Mixin(MoreOptionsDialog.class)
public class MixinMoreOptionsDialog {
    @Shadow private ButtonWidget customizeTypeButton;
    @Shadow private Optional<RegistryEntry<WorldPreset>> presetEntry;
    @Shadow private GeneratorOptionsHolder generatorOptionsHolder;
    
    @Dynamic("World customize button")
    @Inject(method = "method_28087", at = @At(value = "TAIL"))
    private void injectCustomizeButton(MinecraftClient client, CreateWorldScreen screen, ButtonWidget widget, CallbackInfo info) {
        if (this.presetEntry.flatMap(RegistryEntry::getKey).map(this::isModernBetaPreset).get()) {
            LevelScreenProvider provider = (parent, generatorOptionsHolder) -> {
                return new ModernBetaWorldScreen(
                    parent,
                    generatorOptionsHolder,
                    (settingsChunk, settingsBiome, settingsCaveBiome) -> parent.moreOptionsDialog.apply(
                        ModernBetaWorldScreenProvider.createModifier(
                            settingsChunk,
                            settingsBiome,
                            settingsCaveBiome
                        )
                    )
                );
            };
            
            client.setScreen(provider.createEditScreen(screen, this.generatorOptionsHolder));
        }
    }
    
    @Inject(method = "setVisible", at = @At(value = "TAIL"))
    private void injectSetVisible(boolean visible, CallbackInfo info) {
        this.customizeTypeButton.visible = visible && this.presetEntry
            .flatMap(RegistryEntry::getKey)
            .map(this::isModernBetaPreset)
            .get();
    }
    
    @Unique
    private boolean isModernBetaPreset(RegistryKey<WorldPreset> key) {
        return key.equals(ModernBetaWorldPresets.MODERN_BETA);
    }
}
