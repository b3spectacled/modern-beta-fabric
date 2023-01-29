package mod.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.world.gen.GeneratorOptions;

@Environment(EnvType.CLIENT)
@Mixin(MoreOptionsDialog.class)
public interface MixinMoreOptionsDialogInvoker {
    @Invoker("setGeneratorOptions")
    public void invokeSetGeneratorOptions(GeneratorOptions generatorOptions);
}