package com.app.api;

/**
 * Contains classes related to controllers and mod configuration.
 */
import com.app.controller.Axius;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * Contains classes related to Forge mod loading and event handling.
 */
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Contains classes related to file handling.
 */
import net.minecraftforge.fml.loading.FMLPaths;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * This class handles the configuration settings for the mod.
 */
public class Config {
    private final ForgeConfigSpec.Builder builder;
    private final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

    /**
     * Constructor for the configuration handler.
     */
    public Config() {
        this.builder = builder();

        // Validate the configuration folder
        validateConfigFolder();

        // Register this configuration with ModLoadingContext
        registerConfig(ModConfig.Type.COMMON, builder.build(), Axius.MODID);

        // Subscribe to the configuration loading event
        bus.addListener(this::onLoad);

        // Subscribe to the configuration changes event
        bus.addListener(this::onConfigChanged);
    }

    /**
     * Called when the configuration is loaded.
     *
     * @param configEvent The loaded configuration instance.
     */
    private void onLoad(ModConfigEvent.Loading configEvent) {
        ModConfig.Type configType = configEvent.getConfig().getType();

        switch (configType) {
            case COMMON:
                // For common configuration
                break;

            case CLIENT:
                // For client-only configuration
                break;

            case SERVER:
                // For server-only configuration
                break;
        }
    }

    /**
     * Called when the mod configuration changes.
     *
     * @param configEvent The configuration event.
     */
    private void onConfigChanged(ModConfigEvent.Reloading configEvent) {
        if (configEvent.getConfig().getSpec() == builder) {
            // Handle configuration changes
        }
    }

    /**
     * Validates and creates the configuration folder if it doesn't exist.
     */
    private void validateConfigFolder() {
        Path path = FMLPaths.CONFIGDIR.get().resolve(Axius.MODID);

        // Create the config folder if it doesn't exist
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerConfig(ModConfig.Type type, IConfigSpec<?> spec) {
        ModLoadingContext.get().registerConfig(type, spec);
    }

    public void registerConfig(ModConfig.Type type, IConfigSpec<?> spec, String fileName) {
        ModLoadingContext.get().registerConfig(type, spec, fileName);
    }

    /**
     * Pushes a new category onto the configuration builder's stack.
     *
     * @param category The name of the category to push.
     * @return The updated ForgeConfigSpec.Builder with the new category pushed.
     */
    public ForgeConfigSpec.Builder push(String category) {
        return builder.push(category);
    }

    public ForgeConfigSpec.Builder builder() {
        return new ForgeConfigSpec.Builder();
    }

    /**
     * Adds a comment to the configuration builder.
     *
     * @param comment The comment to add.
     * @return The updated ForgeConfigSpec.Builder with the comment added.
     */
    public ForgeConfigSpec.Builder comment(String comment) {
        return builder.comment(comment);
    }

    /**
     * Defines an integer value within a specified range in the configuration builder.
     *
     * @param name        The name of the configuration option.
     * @param defaultValue The default value of the configuration option.
     * @param minValue    The minimum value allowed for the configuration option.
     * @param maxValue    The maximum value allowed for the configuration option.
     * @return The ForgeConfigSpec.IntValue instance representing the defined configuration option.
     */
    public ForgeConfigSpec.IntValue defineInRange(String name, int defaultValue, int minValue, int maxValue) {
        return builder.defineInRange(name, defaultValue, minValue, maxValue);
    }
}
