package com.axius.controller;

/**
 * Contains classes related to configuration and setup of the mod.
 */
import com.axius.api.Config;
import com.axius.api.Input;
import com.axius.packages.keybindings.toggles.ToggleEnchant;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Contains classes related to APIs and input handling.
 */
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

/**
 * Contains classes related to networking and event handling.
 */
import com.axius.server.PacketHandler;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.MinecraftForge;
import org.slf4j.Logger;

import static com.axius.util.input.Update.bindingState;
import static com.axius.util.input.Update.keyState;


/**
 * The main mod class for Axius.
 */
@Mod(Axius.MODID)
@Mod.EventBusSubscriber(modid = Axius.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Axius {
    /**
     * The mod ID.
     */
    public static final String MODID = "axius";

    /**
     * The logger instance.
     */
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * The packet handler instance.
     */
    private static PacketHandler packetHandler;

    /**
     * The Input manager instance.
     */
    private static Input inputManager;

    /**
     * The configuration settings.
     */
    public static Config Settings;

    /**
     * Constructor for the Axius mod.
     */
    public Axius() {
        packetHandler = new PacketHandler();
        inputManager = new Input();
        Settings = new Config();

        InitializeSettings();
        InitializeToggles();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    // Global:

    /**
     * Initializes the settings for the mod.
     */
    private void InitializeSettings() {
        // Create a ForgeConfigSpec builder for the 'general' settings category.
    }

    /**
     * Initializes the toggles for specific enchantments.
     */
    private void InitializeToggles() {
        // Enchants:
        ToggleEnchant Silk_touch = new ToggleEnchant(Enchantments.SILK_TOUCH, 20, Input.Key.KEY_LEFT_SHIFT);
    }

    /**
     * Get the packet handler instance.
     *
     * @return The packet handler instance.
     */
    public static PacketHandler getPacketHandler() {
        return packetHandler;
    }

    /**
     * Get the input manager instance.
     *
     * @return The input manager instance.
     */
    public static Input getInputManager() {
        return inputManager;
    }

    // Events:

    /**
     * Performs common setup tasks during the FMLCommonSetupEvent.
     *
     * @param event The FMLCommonSetupEvent that triggers this method.
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    /**
     * Event handler for server starting.
     *
     * @param event The ServerStartingEvent.
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    /**
     * Event handler for client setup.
     *
     * @param event The FMLClientSetupEvent.
     */
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("HELLO FROM CLIENT SETUP");
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    /**
     * This method is a subscriber to the ServerChatEvent. It gets triggered when a player sends a chat message on the server.
     *
     * @param event The ServerChatEvent containing information about the chat message event.
     */
    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        // Get the player who sent the chat message
        Player player = event.getPlayer();

        // Get the chat message content
        Component message = event.getMessage();
    }

    /**
     * This method handles key input events.
     *
     * @param event The input event to process.
     */
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        int keyCode = event.getKey();
        Input.Type state;

        switch (event.getAction()) {
            case InputConstants.PRESS:
                state = Input.Type.Press;

                break;
            case InputConstants.RELEASE:
                state = Input.Type.Release;

                break;
            case InputConstants.REPEAT:
                state = Input.Type.Hold;

                break;
            default:

                return;
        }

        keyState(Input.getKeyInputs(), keyCode, state);
        bindingState(Input.getbindedInputs(), keyCode, state);
    }
}
