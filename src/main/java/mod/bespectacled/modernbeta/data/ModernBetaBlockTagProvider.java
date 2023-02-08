package mod.bespectacled.modernbeta.data;

import java.util.concurrent.CompletableFuture;

import mod.bespectacled.modernbeta.ModernBeta;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;

public class ModernBetaBlockTagProvider extends FabricTagProvider<Block> {
    public static final TagKey<Block> OVERWORLD_CARVER_REPLACEABLES = keyOf("overworld_carver_replaceables");
    
    public ModernBetaBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup arg) {
        getOrCreateTagBuilder(OVERWORLD_CARVER_REPLACEABLES).add(
            Blocks.STONE,
            Blocks.COBBLESTONE,
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            Blocks.DEEPSLATE, 
            Blocks.TUFF, 
            Blocks.GRANITE, 
            Blocks.IRON_ORE, 
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.RAW_IRON_BLOCK,
            Blocks.COPPER_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.RAW_COPPER_BLOCK,
            Blocks.COAL_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.COAL_BLOCK
        );
    }
    
    private static TagKey<Block> keyOf(String id) {
        return TagKey.of(RegistryKeys.BLOCK, ModernBeta.createId(id));
    }
}
