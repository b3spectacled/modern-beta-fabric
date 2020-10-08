package com.bespectacled.modernbeta.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import net.minecraft.predicate.entity.EntityFlagsPredicate.Builder;

import com.bespectacled.modernbeta.ModernBeta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.Level;

public final class ModernBetaConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Paths.get("config", "modernbeta.json");
    
    public static final int CURRENT_VERSION = 4;
    public final int VERSION;
    
    public final Long fixed_seed;
    public final boolean render_beta_sky_color;
    public final boolean render_beta_grass_color;
    public final boolean generate_oceans;
    public final boolean generate_ice_desert;
    public final boolean alpha_winter_mode;
    public final boolean alpha_plus;
    public final boolean generate_sky;
    
    public ModernBetaConfig() {
        this.VERSION = CURRENT_VERSION;
        
        this.fixed_seed = 0L;
        this.render_beta_sky_color = true;
        this.render_beta_grass_color = true;
        this.generate_oceans = false;
        this.generate_ice_desert = false;
        this.alpha_winter_mode = false;
        this.alpha_plus = false;
        this.generate_sky = false;
    }
    
    public static ModernBetaConfig loadConfig() {
        return readConfig();
    }
    
    private static ModernBetaConfig createConfig() {
        ModernBetaConfig config = new ModernBetaConfig();
        
        try {
            if (!Files.exists(PATH))
                Files.createDirectories(PATH.getParent());
            
            FileWriter writer = new FileWriter(PATH.toFile());
            writer.write(GSON.toJson(config));
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return config;
    }
    
    private static ModernBetaConfig readConfig() {
        ModernBetaConfig config = null;
        
        try {
            BufferedReader bufferedReader = new BufferedReader(
                new FileReader(PATH.toFile())
            );
            
            config = GSON.fromJson(bufferedReader, ModernBetaConfig.class);
        }
        catch (IOException e) {
            ModernBeta.LOGGER.log(Level.WARN, "Config file not found, creating...");
            createConfig();
        }
        
        if (config == null || config.VERSION != CURRENT_VERSION) {
            ModernBeta.LOGGER.log(Level.WARN, "Missing or outdated config, recreating...");
            config = createConfig();
        }
        
        return config;
    }

}