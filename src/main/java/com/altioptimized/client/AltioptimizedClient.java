package com.altioptimized.client;

import com.altioptimized.Altioptimized;
import com.altioptimized.config.AltioptimizedConfig;
import com.altioptimized.optimization.GameSettingsOptimizer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AltioptimizedClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        Altioptimized.LOGGER.info("Initializing altiOptimized client...");
        
        // Initialize config
        AltioptimizedConfig.init();
        
        // Apply optimized settings on startup
        GameSettingsOptimizer.applyOptimizedSettings();
        
        Altioptimized.LOGGER.info("altiOptimized client initialized successfully!");
    }
}