package com.altioptimized.optimization;

import com.altioptimized.Altioptimized;
import com.altioptimized.config.AltioptimizedConfig;
import com.altioptimized.mixin.GameOptionsAccessor;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.SimpleOption;
import java.util.HashMap;
import java.util.Map;

public class GameSettingsOptimizer {

    private static final String GRAPHICS_MODE_KEY = "graphicsMode";

    // Method to safely retrieve the GameOptionsAccessor
    private static GameOptionsAccessor getAccessor(GameOptions options) {
        if (options == null) return null;
        try {
            return (GameOptionsAccessor) options;
        } catch (ClassCastException e) {
            Altioptimized.LOGGER.error("Failed to cast GameOptions to GameOptionsAccessor. Mixin is likely failing.", e);
            return null;
        }
    }

    // Maps a field key to its getter method on the accessor
    private static final Map<String, SimpleOption<?>> getOptionsMap(GameOptionsAccessor accessor) {
        // We use ImmutableMap for efficiency and safety since this map is constant per accessor logic
        return ImmutableMap.<String, SimpleOption<?>>builder()
                .put(GRAPHICS_MODE_KEY, accessor.getGraphicsMode())
                .put("viewDistance", accessor.getViewDistance())
                .put("simulationDistance", accessor.getSimulationDistance())
                .put("ao", accessor.getAo())
                .put("enableVsync", accessor.getEnableVsync())
                .put("particles", accessor.getParticles())
                .put("cloudRenderMode", accessor.getCloudRenderMode())
                .put("entityShadows", accessor.getEntityShadows())
                .put("maxFps", accessor.getMaxFps())
                .put("mipmapLevels", accessor.getMipmapLevels())
                .put("biomeBlendRadius", accessor.getBiomeBlendRadius())
                .put("distortionEffectScale", accessor.getDistortionEffectScale())
                .put("fovEffectScale", accessor.getFovEffectScale())
                .put("entityDistanceScaling", accessor.getEntityDistanceScaling())
                .put("gamma", accessor.getGamma())
                .put("guiScale", accessor.getGuiScale())
                // .put("attackIndicator", accessor.getAttackIndicator()) // Attack Indicator is not typically performance related
                .put("bobView", accessor.getBobView())
                .put("fullscreen", accessor.getFullscreen())
                .build();
    }
    
    // Helper to extract a value from a SimpleOption, handling different types
    private static Object getValue(SimpleOption<?> option) {
        Object value = option.getValue();
        // Since we are using String/Int/Boolean in the config, handle the enum conversion
        if (value instanceof GraphicsMode) {
            return ((GraphicsMode)value).getId(); // Get ID for GraphicsMode
        }
        if (value instanceof ParticlesMode) {
             return ((ParticlesMode)value).getId(); // Get ID for ParticlesMode
        }
        if (value instanceof CloudRenderMode) {
            return ((CloudRenderMode)value).getId(); // Get ID for CloudRenderMode
        }
        return value;
    }
    
    @SuppressWarnings("unchecked")
    private static void applyValue(SimpleOption<?> option, String key, Object value) {
        try {
            // Check the generic type of the option to determine how to set it
            if (option instanceof SimpleOption) {
                if (option.getValue() instanceof GraphicsMode) {
                    // Handle GraphicsMode (integer in config, enum in game)
                    if (value instanceof Number) {
                        ((SimpleOption<GraphicsMode>) option).setValue(GraphicsMode.byId(((Number) value).intValue()));
                    }
                } else if (option.getValue() instanceof ParticlesMode) {
                    // Handle ParticlesMode
                    if (value instanceof Number) {
                        ((SimpleOption<ParticlesMode>) option).setValue(ParticlesMode.byId(((Number) value).intValue()));
                    }
                } else if (option.getValue() instanceof CloudRenderMode) {
                    // Handle CloudRenderMode
                    if (value instanceof Number) {
                        int cloudMode = ((Number) value).intValue();
                        ((SimpleOption<CloudRenderMode>) option).setValue(cloudMode == 0 ? CloudRenderMode.OFF : CloudRenderMode.FANCY);
                    }
                } else if (value instanceof Integer || value instanceof Number && !(value instanceof Double || value instanceof Float)) {
                    ((SimpleOption<Integer>) option).setValue(((Number) value).intValue());
                } else if (value instanceof Boolean) {
                    ((SimpleOption<Boolean>) option).setValue((Boolean) value);
                } else if (value instanceof Double || value instanceof Float) {
                    ((SimpleOption<Double>) option).setValue(((Number) value).doubleValue());
                } else {
                    Altioptimized.LOGGER.warn("Attempted to apply unknown option type for key: {}", key);
                }
            }
        } catch (Exception e) {
            Altioptimized.LOGGER.error("Failed to set option {} with value {}", key, value, e);
        }
    }


    public static void applyOptimizedSettings() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options == null) {
            Altioptimized.LOGGER.warn("GameOptions not available yet. Settings will be applied later.");
            return;
        }

        GameOptions options = client.options;
        GameOptionsAccessor accessor = getAccessor(options);
        if (accessor == null) return;
        
        AltioptimizedConfig config = AltioptimizedConfig.getInstance();
        
        // 1. Backup current settings only if a backup doesn't exist (i.e., first run)
        if (config.getOriginalSettings().isEmpty()) {
            Map<String, Object> currentSettings = new HashMap<>();
            Map<String, SimpleOption<?>> optionsMap = getOptionsMap(accessor);

            for (Map.Entry<String, SimpleOption<?>> entry : optionsMap.entrySet()) {
                currentSettings.put(entry.getKey(), getValue(entry.getValue()));
            }
            config.backupOriginalSettings(currentSettings);
            Altioptimized.LOGGER.info("Backed up original game settings.");
        }
        
        // 2. Apply optimized settings from config
        try {
            accessor.getGraphicsMode().setValue(config.isFastGraphics() ? GraphicsMode.FAST : GraphicsMode.FABULOUS);
            accessor.getViewDistance().setValue(config.getRenderDistance());
            accessor.getSimulationDistance().setValue(config.getSimulationDistance());
            accessor.getAo().setValue(config.isSmoothLighting()); // Smooth lighting is Ao
            accessor.getEnableVsync().setValue(config.isVSync());
            accessor.getParticles().setValue(ParticlesMode.byId(config.getParticles()));
            accessor.getCloudRenderMode().setValue(config.isClouds() ? CloudRenderMode.FANCY : CloudRenderMode.OFF); // Set clouds based on config
            accessor.getEntityShadows().setValue(config.isEntityShadows());
            accessor.getMaxFps().setValue(config.getMaxFramerate());
            accessor.getMipmapLevels().setValue(config.getMipmapLevels());
            accessor.getBiomeBlendRadius().setValue(config.getBiomeBlend());
            
            // Scaling options use Double
            accessor.getDistortionEffectScale().setValue((double)config.getDistortionEffects() / 100.0);
            accessor.getFovEffectScale().setValue((double)config.getFovEffects() / 100.0);
            
            // Gamma is already a double in GameOptions, but not in your config. Leaving out for now.
            // accessor.getGamma().setValue(config.getGamma());
            
            // Save options file after applying
            options.write();
            
            // Force a reload of chunks by setting the view distance
            if (client.worldRenderer != null) {
                // This will trigger chunk reloading without directly calling private methods
                client.options.getViewDistance().setValue(client.options.getViewDistance().getValue());
            }
            
            Altioptimized.LOGGER.info("Applied optimized settings.");
        } catch (Exception e) {
            Altioptimized.LOGGER.error("Critical failure during settings application.", e);
        }
    }

    public static void restoreOriginalSettings() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options == null) return;

        GameOptions options = client.options;
        GameOptionsAccessor accessor = getAccessor(options);
        if (accessor == null) return;
        
        AltioptimizedConfig config = AltioptimizedConfig.getInstance();
        Map<String, Object> originalSettings = config.getOriginalSettings();

        if (originalSettings.isEmpty()) {
            Altioptimized.LOGGER.warn("No original settings backup found to restore.");
            return;
        }

        Map<String, SimpleOption<?>> optionsMap = getOptionsMap(accessor);
        
        for (Map.Entry<String, Object> entry : originalSettings.entrySet()) {
            SimpleOption<?> option = optionsMap.get(entry.getKey());
            if (option != null) {
                applyValue(option, entry.getKey(), entry.getValue());
            }
        }
        
        // Clear the backup so it can be created again next time
        config.backupOriginalSettings(new HashMap<>());
        
        options.write();

        // Force a reload of chunks by setting the view distance
        if (client.worldRenderer != null) {
            // This will trigger chunk reloading without directly calling private methods
            client.options.getViewDistance().setValue(client.options.getViewDistance().getValue());
        }

        Altioptimized.LOGGER.info("Restored original settings.");
    }
}
