package com.bespectacled.modernbeta.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.biome.vanilla.VanillaSurfaceBiomes;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;

/**
 * 
 * @author PaulEvs
 *
 */
public class CustomizeBiomesScreen extends Screen {
    protected final AbstractCustomizeLevelScreen parent;
    protected final CompoundTag settings;
    protected final Consumer<CompoundTag> consumer;
    
    protected ButtonListWidget buttonList;
    private Map<Biome, Boolean> biomeMap;
    
    protected CustomizeBiomesScreen(AbstractCustomizeLevelScreen parent, CompoundTag settings, Consumer<CompoundTag> consumer) {
        super(new TranslatableText(""));
        
        this.parent = parent;
        this.settings = settings;
        this.consumer = consumer;
        
        this.biomeMap = this.initBiomes();
    }
    
    @Override
    protected void init() {
        this.addButton(new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                this.client.openScreen(this.parent);
            }
        ));

        this.addButton(new ButtonWidget(
            this.width / 2 + 5, this.height - 28, 150, 20, 
            ScreenTexts.CANCEL,
            (buttonWidget) -> {
                this.client.openScreen(this.parent);
            }
        ));
        
        this.addButton(new ButtonWidget(
            this.width / 2 - 155, 8, 150, 20, 
            new TranslatableText("createworld.customize.biomes.enable_all"),
            (buttonWidget) -> {
                this.biomeMap.keySet().forEach(b -> biomeMap.put(b, true));
                
                this.buttonList.children().forEach(b -> {
                    b.children().forEach(e -> {
                        if (e instanceof AbstractButtonWidget) {
                            AbstractButtonWidget button = (AbstractButtonWidget) e;
                            String start = button.getMessage().getString();
                            if (start.isBlank()) return;
                            
                            start = start.substring(0, start.lastIndexOf(":"));
                            
                            MutableText optionText = new TranslatableText(start);
                            optionText.append(new TranslatableText(": "));
                            optionText.append(new TranslatableText("\u00A7aOn"));
                            
                            button.setMessage(optionText);
                            
                        }
                    });
                });
            }
        ));
        
        this.addButton(new ButtonWidget(
            this.width / 2 + 5, 8, 150, 20, 
            new TranslatableText("createworld.customize.biomes.disable_all"),
            (buttonWidget) -> {
                this.biomeMap.keySet().forEach(b -> biomeMap.put(b, false));
                
                this.buttonList.children().forEach(b -> {
                    b.children().forEach(e -> {
                        if (e instanceof AbstractButtonWidget) {
                            AbstractButtonWidget button = (AbstractButtonWidget) e;
                            String start = button.getMessage().getString();
                            if (start.isBlank()) return;
                            
                            start = start.substring(0, start.lastIndexOf(":"));
                            
                            MutableText optionText = new TranslatableText(start);
                            optionText.append(new TranslatableText(": "));
                            optionText.append(new TranslatableText("\u00A7cOff"));
                            
                            button.setMessage(optionText);
                        }
                    });
                });
            }
        ));
        
        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        
        VanillaSurfaceBiomes.BIOMES.forEach(b -> {
            Identifier id = BuiltinRegistries.BIOME.getId(b);
            
            this.buttonList.addSingleOptionEntry(
                CyclingOption.create("biome." + id.getNamespace() + "." + id.getPath(), 
                (gameOptions) -> { return this.biomeMap.get(b); }, 
                (gameOptions, option, value) -> { // Setter
                    this.biomeMap.put(b, value);
                    //this.settings.putBoolean("generateOceans", this.generateOceans);
                    
                    //this.consumer.accept(this.settings);
            }));
        });
        
        this.children.add(this.buttonList);
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(matrixStack);
        
        this.buttonList.render(matrixStack, mouseX, mouseY, tickDelta);
        DrawableHelper.drawCenteredText(matrixStack, this.textRenderer, this.title, this.width / 2, 16, 16777215);
        
        super.render(matrixStack, mouseX, mouseY, tickDelta);
    }
    
    private Map<Biome, Boolean> initBiomes() {
        Map<Biome, Boolean> biomes = new HashMap<Biome, Boolean>();
        VanillaSurfaceBiomes.BIOMES.forEach(b -> biomes.put(b, true));
        
        return biomes;
    }

}
