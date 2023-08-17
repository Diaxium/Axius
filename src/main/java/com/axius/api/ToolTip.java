package com.app.api;

/**
 * Contains classes related to handling Forge events.
 */
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;


/**
 * Contains classes related to handling collections and data structures.
 */
import java.util.HashMap;
import java.util.Map;


/**
 * A utility class for managing and interacting with custom tooltip events in a Minecraft mod.
 */
public class ToolTip {

    // Map to store tooltip event listeners by name
    private static final Map<String, IEvent> toolTipEvents = new HashMap<>();

    /**
     * Interface for defining custom tooltip event listeners.
     */
    public interface IEvent {
        /**
         * Called when the custom tooltip event is triggered.
         *
         * @param event The tooltip event object containing relevant information.
         */
        void listen(ItemTooltipEvent event);
    }

    /**
     * Constructor to create and register a new tooltip event listener.
     *
     * @param name     The unique name for the tooltip event.
     * @param listener The custom event listener implementing the IEvent interface.
     */
    public ToolTip(String name, IEvent listener) {
        assert toolTipEvents.get(name) == null : "Error tooltip event already exists.";

        toolTipEvents.putIfAbsent(name, listener);
        MinecraftForge.EVENT_BUS.addListener(listener::listen);
    }

    /**
     * Retrieve the tooltip event listener associated with the provided name.
     *
     * @param name The name of the tooltip event listener.
     * @return The tooltip event listener (IEvent) or null if not found.
     */
    public IEvent get(String name) {
        return toolTipEvents.get(name);
    }

    /**
     * Remove and unregister a tooltip event listener by its name.
     *
     * @param name The name of the tooltip event listener to be removed.
     */
    public void remove(String name) {
        IEvent removedEvent = toolTipEvents.remove(name);

        if (removedEvent != null) {
            MinecraftForge.EVENT_BUS.unregister(removedEvent);
        }
    }

    /**
     * Check if a tooltip event listener with the given name exists.
     *
     * @param name The name of the tooltip event listener to check.
     * @return True if a tooltip event listener with the provided name exists, false otherwise.
     */
    public boolean contains(String name) {
        return toolTipEvents.containsKey(name);
    }

    /**
     * Get the number of registered tooltip event listeners.
     *
     * @return The count of registered tooltip event listeners.
     */
    public int getSize() {
        return toolTipEvents.size();
    }

    /**
     * Remove all registered tooltip event listeners and unregister the ToolTip class itself.
     * This should be called when cleaning up or shutting down the mod.
     */
    public void clearAll() {
        toolTipEvents.clear();
        MinecraftForge.EVENT_BUS.unregister(ToolTip.class); // Unregister the whole class
    }
}
