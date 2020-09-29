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
    
    public boolean render_beta_sky_color;
    //public boolean render_beta_grass_color;
    //public boolean render_old_stars;
    public boolean generate_oceans;
    public boolean generate_ice_desert;
    
    public ModernBetaConfig() {
        // Default values
        render_beta_sky_color = true;
        //render_beta_grass_color = false;
        //render_old_stars = false;
        generate_oceans = false;
        generate_ice_desert = false;
    }
    
    public static ModernBetaConfig loadConfig() {
        return readConfig();
    }
    
    private static ModernBetaConfig createConfig(Path path) {
        ModernBetaConfig config = new ModernBetaConfig();
        
        try {
            FileWriter writer = new FileWriter(path.toFile());
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
        Path path = Paths.get("config", "modernbeta.json");
        
        try {
            BufferedReader bufferedReader = new BufferedReader(
                new FileReader(path.toFile())
            );
            
            config = GSON.fromJson(bufferedReader, ModernBetaConfig.class);
        }
        catch (IOException e) {
            ModernBeta.LOGGER.log(Level.INFO, "Config file not found, creating...");
            createConfig(path);
        }
        
        if (config == null) config = createConfig(path);
        
        return config;
    }

}