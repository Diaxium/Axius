package com.app.util.inventory.item;

/**
 * Contains classes related to controllers and mod functionality.
 */
import com.app.controller.Axius;

/**
 * Contains classes related to networking and packet handling.
 */
import com.app.network.packets.inventory.item.DurabilityPacket;

/**
 * Contains classes related to item handling.
 */
import net.minecraft.world.item.ItemStack;


/**
 * The Durability class is responsible for managing the durability of tools/items.
 */
public class Durability {

    /**
     * Adds the specified amount of durability to the given item.
     *
     * @param item       The ItemStack representing the item.
     * @param durability The amount of durability to add.
     */
    public static void Add(ItemStack item, int durability) {
        DurabilityPacket packet = new DurabilityPacket(item, durability, DurabilityPacket.Operation.ADD);
        Axius.getPacketHandler().sendToServer(packet);
    }

    /**
     * Subtracts the specified amount of durability from the given item.
     *
     * @param item       The ItemStack representing the item.
     * @param durability The amount of durability to subtract.
     */
    public static void Subtract(ItemStack item, int durability) {
        DurabilityPacket packet = new DurabilityPacket(item, durability, DurabilityPacket.Operation.SUBTRACT);
        Axius.getPacketHandler().sendToServer(packet);
    }

    /**
     * Multiple's the specified amount of durability from the given item.
     *
     * @param item       The ItemStack representing the item.
     * @param durability The amount of durability to subtract.
     */
    public static void Multiply(ItemStack item, int durability) {
        DurabilityPacket packet = new DurabilityPacket(item, durability, DurabilityPacket.Operation.MULTIPLE);
        Axius.getPacketHandler().sendToServer(packet);
    }

    /**
     * Divide's the specified amount of durability from the given item.
     *
     * @param item       The ItemStack representing the item.
     * @param durability The amount of durability to subtract.
     */
    public static void Divide(ItemStack item, int durability) {
        DurabilityPacket packet = new DurabilityPacket(item, durability, DurabilityPacket.Operation.DIVIDE);
        Axius.getPacketHandler().sendToServer(packet);
    }

    /**
     * Modifies the durability of the given item to the specified value.
     *
     * @param item       The ItemStack representing the item.
     * @param durability The new durability value for the item.
     */
    public static void Modify(ItemStack item, int durability) {
        DurabilityPacket packet = new DurabilityPacket(item, durability, DurabilityPacket.Operation.MODIFY);
        Axius.getPacketHandler().sendToServer(packet);
    }
}
