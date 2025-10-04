package com.altioptimized.mixin;

import com.altioptimized.Altioptimized;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.option.CloudRenderMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class VideoSettingsMixin {

    @Shadow @Final public GameOptions options;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        Altioptimized.LOGGER.info("Applying early video optimizations via mixin");

        if (options != null) {
            try {
                // Direct access to options
                
                // Apply settings one by one with error handling
                try { options.getGraphicsMode().setValue(GraphicsMode.FAST); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set graphics mode", e); }
                
                try { options.getViewDistance().setValue(2); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set view distance", e); }
                
                try { options.getSimulationDistance().setValue(5); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set simulation distance", e); }
                
                try { options.getAo().setValue(false); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set ambient occlusion", e); }
                
                try { options.getEnableVsync().setValue(false); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set vsync", e); }
                
                try { options.getParticles().setValue(ParticlesMode.MINIMAL); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set particles", e); }
                
                try { options.getCloudRenderMode().setValue(CloudRenderMode.OFF); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set clouds", e); }
                
                try { options.getEntityShadows().setValue(false); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set entity shadows", e); }
                
                try { options.getMaxFps().setValue(260); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set max FPS", e); }
                
                try { options.getMipmapLevels().setValue(0); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set mipmap levels", e); }
                
                try { options.getBiomeBlendRadius().setValue(0); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set biome blend", e); }
                
                try { options.getDistortionEffectScale().setValue(0.0); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set distortion effect", e); }
                
                try { options.getFovEffectScale().setValue(0.0); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set FOV effect", e); }
                
                try { options.getEntityDistanceScaling().setValue(0.5); } 
                catch (Exception e) { Altioptimized.LOGGER.warn("Failed to set entity distance scaling", e); }

                try { options.write(); } 
                catch (Exception e) { Altioptimized.LOGGER.error("Failed to write options", e); }
                
                Altioptimized.LOGGER.info("Successfully applied early video optimizations");
            } catch (Exception e) {
                Altioptimized.LOGGER.error("Failed to apply early video optimizations", e);
            }
        }
    }
}
