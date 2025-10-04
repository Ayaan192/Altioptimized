package com.altioptimized.config;

import com.altioptimized.Altioptimized;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AltioptimizedConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("altioptimized.json").toFile();
    
    private static AltioptimizedConfig INSTANCE;
    
    // Original settings backup to restore if needed
    private Map<String, Object> originalSettings = new HashMap<>();
    
    // Current optimized settings
    private int renderDistance = 2;
    private int simulationDistance = 2;
    private boolean smoothLighting = false;
    private boolean vSync = false;
    private int particles = 0; // Minimal
    private boolean clouds = false;
    private boolean entityShadows = false;
    private int maxFramerate = 260; // Unlimited
    private int mipmapLevels = 0;
    private int biomeBlend = 0; // Off
    private int distortionEffects = 0; // 0%
    private int fovEffects = 0; // 0%
    private boolean fastGraphics = true;
    
    public static void init() {
        if (INSTANCE == null) {
            if (CONFIG_FILE.exists()) {
                try (FileReader reader = new FileReader(CONFIG_FILE)) {
                    INSTANCE = GSON.fromJson(reader, AltioptimizedConfig.class);
                    Altioptimized.LOGGER.info("Loaded altiOptimized config");
                } catch (IOException e) {
                    Altioptimized.LOGGER.error("Failed to load config", e);
                    INSTANCE = new AltioptimizedConfig();
                }
            } else {
                INSTANCE = new AltioptimizedConfig();
                saveConfig();
            }
        }
    }
    
    public static AltioptimizedConfig getInstance() {
        if (INSTANCE == null) {
            init();
        }
        return INSTANCE;
    }
    
    public static void saveConfig() {
        try {
            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.getParentFile().mkdirs();
                CONFIG_FILE.createNewFile();
            }
            
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(INSTANCE, writer);
            }
            
            Altioptimized.LOGGER.info("Saved altiOptimized config");
        } catch (IOException e) {
            Altioptimized.LOGGER.error("Failed to save config", e);
        }
    }
    
    // Backup the original settings before optimization
    public void backupOriginalSettings(Map<String, Object> settings) {
        this.originalSettings = new HashMap<>(settings);
        saveConfig();
    }
    
    // Getters and setters
    public Map<String, Object> getOriginalSettings() {
        return originalSettings;
    }
    
    public int getRenderDistance() {
        return renderDistance;
    }
    
    public int getSimulationDistance() {
        return simulationDistance;
    }
    
    public boolean isSmoothLighting() {
        return smoothLighting;
    }
    
    public boolean isVSync() {
        return vSync;
    }
    
    public int getParticles() {
        return particles;
    }
    
    public boolean isClouds() {
        return clouds;
    }
    
    public boolean isEntityShadows() {
        return entityShadows;
    }
    
    public int getMaxFramerate() {
        return maxFramerate;
    }
    
    public int getMipmapLevels() {
        return mipmapLevels;
    }
    
    public int getBiomeBlend() {
        return biomeBlend;
    }
    
    public int getDistortionEffects() {
        return distortionEffects;
    }
    
    public int getFovEffects() {
        return fovEffects;
    }
    
    public boolean isFastGraphics() {
        return fastGraphics;
    }
}