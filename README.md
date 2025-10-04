# altiOptimized

**Mod Type**: `client`

## Overview

altiOptimized is a Minecraft Fabric optimization mod for version 1.21.1 that automatically configures video settings to their lowest values for maximum FPS. It includes AI-powered commands for optimization.

## Functionality

- Automatically sets graphics to `Fast` mode
- Sets render distance to `2 chunks` (minimum possible)
- Sets simulation distance to `2 chunks` (minimum possible)
- Turns smooth lighting `Off`
- Disables V-Sync for uncapped framerate
- Sets particles to `Minimal`
- Disables clouds completely
- Disables entity shadows
- Sets maximum FPS to unlimited (260)
- Disables mipmaps for texture optimization
- Disables biome blend for performance
- Disables distortion and FOV effects
- Minimizes entity render distance
- Optimizes GUI scale and other visual settings

## Commands

- `/optimize` - Apply all optimization settings
- `/optimize fps` - Apply FPS-focused optimization settings
- `/optimize performance` - Apply performance-focused optimization settings
- `/optimize max` - Apply maximum optimization settings
- `/restore` - Restore original settings

## Technical Implementation

- Uses mixins to apply settings as early as possible during game initialization
- Backs up original settings for easy restoration
- Implements client-side commands for on-demand optimization
- Automatically applies optimizations on game startup

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.1
2. Download the altiOptimized mod JAR file
3. Place the JAR file in your Minecraft mods folder
4. Launch Minecraft with Fabric loader

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16.14 or higher
- Java 21 or higher

## License

This project is licensed under the CC0-1.0 License - see the LICENSE file for details.