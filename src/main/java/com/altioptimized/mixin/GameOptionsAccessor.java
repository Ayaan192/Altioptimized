package com.altioptimized.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.option.SimpleOption;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameOptions.class)
public interface GameOptionsAccessor {
    @Accessor
    SimpleOption<GraphicsMode> getGraphicsMode();
    
    @Accessor
    SimpleOption<Integer> getViewDistance();
    
    @Accessor
    SimpleOption<Integer> getSimulationDistance();
    
    @Accessor
    SimpleOption<Boolean> getAo();
    
    @Accessor
    SimpleOption<Boolean> getEnableVsync();
    
    @Accessor
    SimpleOption<ParticlesMode> getParticles();
    
    @Accessor
    SimpleOption<CloudRenderMode> getCloudRenderMode();
    
    @Accessor
    SimpleOption<Boolean> getEntityShadows();
    
    @Accessor
    SimpleOption<Integer> getMaxFps();
    
    @Accessor
    SimpleOption<Integer> getMipmapLevels();
    
    @Accessor
    SimpleOption<Integer> getBiomeBlendRadius();
    
    @Accessor
    SimpleOption<Double> getDistortionEffectScale();
    
    @Accessor
    SimpleOption<Double> getFovEffectScale();
    
    @Accessor
    SimpleOption<Double> getEntityDistanceScaling();
    
    @Accessor
    SimpleOption<Double> getGamma();
    
    @Accessor
    SimpleOption<Integer> getGuiScale();
    
    @Accessor
    SimpleOption<AttackIndicator> getAttackIndicator();
    
    // The field is actually called maxFps in GameOptions
    // No need for a separate framerateLimit accessor
    
    @Accessor
    SimpleOption<Boolean> getBobView();
    
    @Accessor
    SimpleOption<Boolean> getFullscreen();
}