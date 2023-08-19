package com.axius.api;

/**
 * Contains classes related to controllers and mod configuration.
 */
import com.axius.controller.Axius;
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
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Contains classes related to mod configuration handling and event management.
 */
public class Config {

    private ForgeConfigSpec.Builder builder;
    private ForgeConfigSpec build;

    /**
     * Constructor for the configuration handler.
     * Initializes the configuration builder and sets up event listeners.
     */
    public Config() {
        // Validate the configuration folder
        validateConfigFolder();

        // Subscribe to the configuration loading event
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onLoad);

        // Subscribe to the configuration changes event
        bus.addListener(this::onConfigChanged);
    }

    /**
     * Handles the loading of configuration data.
     *
     * @param configEvent The ModConfigEvent.Loading event.
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
     * Handles configuration changes.
     *
     * @param configEvent The ModConfigEvent.Reloading event.
     */
    private void onConfigChanged(ModConfigEvent.Reloading configEvent) {
        if (configEvent.getConfig().getSpec().equals(builder)) {
            // Handle configuration changes
        }
    }

    /**
     * Validates the configuration folder and creates it if it doesn't exist.
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

    /**
     * Registers a configuration specification with ModLoadingContext.
     *
     * @param type The type of configuration (COMMON, CLIENT, SERVER).
     * @throws IllegalStateException If the configuration builder is not initialized.
     */
    public Config registerConfig(ModConfig.Type type) {
        assert (this.build != null) : "Configuration builder must be initialized before registering config.";

        ModLoadingContext.get().registerConfig(type, this.build);

        return this;
    }
    /**
     * Registers a configuration specification with a specified file name.
     *
     * @param type     The type of configuration (COMMON, CLIENT, SERVER).
     * @param fileName The custom file name for the configuration.
     * @throws IllegalStateException If the configuration builder is not initialized.
     */
    public Config registerConfig(ModConfig.Type type, String fileName) {
        assert (this.build != null) : "Configuration builder must be initialized before registering config.";

        ModLoadingContext.get().registerConfig(type, this.build, String.format("%s/%s.toml", Axius.MODID, fileName));

        return this;
    }

    /**
     * Retrieves the built ForgeConfigSpec instance.
     *
     * @return The built ForgeConfigSpec instance representing the mod's configuration.
     */
    public ForgeConfigSpec getBuild() {
        return this.build;
    }

    /**
     * Pushes a category onto the configuration builder stack.
     *
     * @param path The path to the category.
     * @return The current Config instance.
     */
    public Config push(String path) {
        builder.push(path);
        return this;
    }

    /**
     * Pushes a list of categories onto the configuration builder stack.
     *
     * @param path The list of category paths.
     * @return The current Config instance.
     */
    public Config push(List<String> path) {
        builder.push(path);
        return this;
    }

    /**
     * Pops a specified number of categories from the configuration builder stack.
     *
     * @param count The number of categories to pop.
     * @return The current Config instance.
     */
    public Config pop(int count) {
        builder.pop(count);
        return this;
    }

    /**
     * Pops a category from the configuration builder stack.
     *
     * @return The current Config instance.
     */
    public Config pop() {
        builder.pop();
        return this;
    }

    /**
     * Resets the configuration builder instance.
     *
     * @return A new Config instance with a fresh configuration builder.
     */
    public Config builder() {
        this.builder = new ForgeConfigSpec.Builder();

        return this;
    }

    /**
     * Builds the configuration specification using the provided builder.
     *
     * @return The current Config instance after building the specification.
     */
    public Config build() {
        this.build = this.builder.build();

        return this;
    }

    /**
     * Adds a comment to the configuration builder.
     *
     * @param comment The comment to be added.
     * @return The current Config instance.
     */
    public Config comment(String comment) {
        builder.comment(comment);
        return this;
    }

    /**
     * Adds multiple comments to the configuration builder.
     *
     * @param comment The comments to be added.
     * @return The current Config instance.
     */
    public Config comment(String... comment) {
        builder.comment(comment);
        return this;
    }

    /**
     * Defines an integer value within a specified range in the configuration builder.
     *
     * @param path        The path to the value.
     * @param defaultValue The default value of the integer.
     * @param minValue    The minimum allowed value.
     * @param maxValue    The maximum allowed value.
     * @return The current Config instance.
     */
    public Config defineInRange(String path, int defaultValue, int minValue, int maxValue) {
        builder.defineInRange(path, defaultValue, minValue, maxValue);
        return this;
    }

    /**
     * Defines an integer configuration value within a specified range and applies a consumer
     * to further configure the value. This method is used to define an integer configuration
     * option within the provided path, with a default value, minimum value, and maximum value.
     *
     * @param path       The path to the configuration option, typically in a dot-separated format.
     * @param defaultValue The default value of the configuration option.
     * @param minValue   The minimum value allowed for the configuration option.
     * @param maxValue   The maximum value allowed for the configuration option.
     * @param assign     A consumer function that takes a {@link ForgeConfigSpec.IntValue}
     *                   parameter, allowing additional configuration of the value, such as
     *                   attaching comments or specifying requirements.
     * @return This {@code Config} instance to allow method chaining.
     */
    public Config defineInRange(String path, int defaultValue, int minValue, int maxValue, Consumer<ForgeConfigSpec.IntValue> assign) {
        ForgeConfigSpec.IntValue configValue = builder.defineInRange(path, defaultValue, minValue, maxValue);
        assign.accept(configValue);

        return this;
    }

    /**
     * Defines a boolean value in the configuration builder.
     *
     * @param path         The path to the value.
     * @param defaultValue The default value of the boolean.
     * @return The current Config instance.
     */
    public Config defineBoolean(String path, boolean defaultValue) {
        builder.define(path, defaultValue);
        return this;
    }

    /**
     * Defines a boolean configuration value and applies a consumer to further configure the value.
     * This method is used to define a boolean configuration option within the provided path, with
     * a default value.
     *
     * @param path         The path to the configuration option, typically in a dot-separated format.
     * @param defaultValue The default value of the configuration option.
     * @param assign       A consumer function that takes a {@link ForgeConfigSpec.BooleanValue}
     *                     parameter, allowing additional configuration of the value, such as
     *                     attaching comments or specifying requirements.
     * @return This {@code Config} instance to allow method chaining.
     */
    public Config defineBoolean(String path, boolean defaultValue, Consumer<ForgeConfigSpec.BooleanValue> assign) {
        ForgeConfigSpec.BooleanValue configValue = builder.define(path, defaultValue);
        assign.accept(configValue);

        return this;
    }
}
