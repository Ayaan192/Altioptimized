package com.altioptimized.command;

import com.altioptimized.Altioptimized;
import com.altioptimized.optimization.GameSettingsOptimizer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class OptimizationCommands {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registerOptimizeCommand(dispatcher);
            registerRestoreCommand(dispatcher);
        });
        
        Altioptimized.LOGGER.info("Registered optimization commands");
    }
    
    private static void registerOptimizeCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            ClientCommandManager.literal("optimize")
                .executes(context -> executeOptimize(context, "default"))
                .then(ClientCommandManager.literal("fps")
                    .executes(context -> executeOptimize(context, "fps")))
                .then(ClientCommandManager.literal("performance")
                    .executes(context -> executeOptimize(context, "performance")))
                .then(ClientCommandManager.literal("max")
                    .executes(context -> executeOptimize(context, "max")))
        );
    }
    
    private static void registerRestoreCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            ClientCommandManager.literal("restore")
                .executes(OptimizationCommands::executeRestore)
        );
    }
    
    private static int executeOptimize(CommandContext<FabricClientCommandSource> context, String mode) {
        // All optimization modes currently apply the same settings
        // This could be expanded in the future to have different optimization profiles
        GameSettingsOptimizer.applyOptimizedSettings();
        
        String message = switch (mode) {
            case "fps" -> "[AltiOptimized]=Applied (FPS) optimization settings";
            case "performance" -> "[AltiOptimized]=Applied (performance) optimization settings";
            case "max" -> "[AltiOptimized]=Applied (maximum) optimization settings";
            default -> "[AltiOptimized]=Applied (optimization) settings";
        };
        
        context.getSource().sendFeedback(Text.literal(message));
        return 1;
    }
    
    private static int executeRestore(CommandContext<FabricClientCommandSource> context) {
        GameSettingsOptimizer.restoreOriginalSettings();
        context.getSource().sendFeedback(Text.literal("[AltiOptimized]=Restored original settings"));
        return 1;
    }
}