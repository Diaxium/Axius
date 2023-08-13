package com.app.util.inventory.item.tool;

/**
 * Contains classes related to controllers and networking functionality.
 */
import com.app.controller.Axius;
import com.app.network.packets.inventory.item.tool.EnchantPacket;

/**
 * Contains classes related to item and enchantment handling.
 */
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import java.util.Map;


/**
 * The Enchant class provides methods to interact with enchantments on ItemStacks.
 */
public class Enchant {

    /**
     * Adds an enchantment to the given ItemStack.
     *
     * @param item The ItemStack to which the enchantment will be added.
     * @param enchant The enchantment to be added.
     * @param level The level of the enchantment.
     */
    public static void Add(ItemStack item, Enchantment enchant, int level) {
        EnchantPacket packet = new EnchantPacket(item, enchant, level, EnchantPacket.Operation.ADD);
        Axius.getPacketHandler().sendToServer(packet);
    }

    /**
     * Removes an enchantment from the given ItemStack.
     *
     * @param item The ItemStack from which the enchantment will be removed.
     * @param enchant The enchantment to be removed.
     */
    public static void Remove(ItemStack item, Enchantment enchant) {
        EnchantPacket packet = new EnchantPacket(item, enchant, 0, EnchantPacket.Operation.REMOVE);
        Axius.getPacketHandler().sendToServer(packet);
    }

    /**
     * Modifies the level of an enchantment on the given ItemStack.
     *
     * @param item The ItemStack on which the enchantment will be modified.
     * @param enchant The enchantment to be modified.
     * @param level The new level of the enchantment.
     */
    public static void Modify(ItemStack item, Enchantment enchant, int level) {
        EnchantPacket packet = new EnchantPacket(item, enchant, level, EnchantPacket.Operation.MODIFY);
        Axius.getPacketHandler().sendToServer(packet);
    }

    /**
     * Retrieves a map of enchantments and their levels from the given ItemStack.
     *
     * @param item The ItemStack from which to retrieve enchantments.
     * @return A map of enchantments and their levels on the ItemStack.
     */
    public Map<Enchantment, Integer> Get(ItemStack item) {
        return EnchantmentHelper.getEnchantments(item);
    }
}