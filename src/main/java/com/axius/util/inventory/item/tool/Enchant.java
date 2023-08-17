package com.axius.util.inventory.item.tool;

/**
 * Contains classes related to controllers and networking functionality.
 */
import com.axius.controller.Axius;
import com.axius.server.packets.inventory.item.tool.EnchantPacket;

/**
 * Contains classes related to item and enchantment handling.
 */
import com.axius.controller.Axius;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

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
    public static void add(ItemStack item, Enchantment enchant, int level) {
        EnchantPacket packet = new EnchantPacket(item, enchant, level, EnchantPacket.Operation.ADD);
        Axius.getPacketHandler().sendToServer(packet);
    }

    /**
     * Removes an enchantment from the given ItemStack.
     *
     * @param item The ItemStack from which the enchantment will be removed.
     * @param enchant The enchantment to be removed.
     */
    public static void remove(ItemStack item, Enchantment enchant) {
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
    public static void modify(ItemStack item, Enchantment enchant, int level) {
        EnchantPacket packet = new EnchantPacket(item, enchant, level, EnchantPacket.Operation.MODIFY);
        Axius.getPacketHandler().sendToServer(packet);
    }

    /**
     * Retrieves a map of enchantments and their levels from the given ItemStack.
     *
     * @param item The ItemStack from which to retrieve enchantments.
     * @return A map of enchantments and their levels on the ItemStack.
     */
    public static Map<Enchantment, Integer> get(ItemStack item) {
        return EnchantmentHelper.getEnchantments(item);
    }

    /**
     * Checks if the given ItemStack contains a specific enchantment.
     *
     * @param item The ItemStack to check for the enchantment.
     * @param enchant The enchantment to check for.
     * @return True if the enchantment is present on the ItemStack, false otherwise.
     */
    public static boolean contains(ItemStack item, Enchantment enchant) {
        Map<Enchantment, Integer> enchantments = get(item);

        return enchantments.containsKey(enchant);
    }

    /**
     * Retrieves the level of a specific enchantment on an ItemStack.
     *
     * @param stack The ItemStack on which to check for the enchantment.
     * @param enchant The enchantment for which to retrieve the level.
     * @return The level of the enchantment on the ItemStack, or 0 if not found.
     */
    public static int getEnchantmentLevel(ItemStack stack, Enchantment enchant) {
        if (stack.isEmpty() || !stack.isEnchanted()) {
            return 0;
        }

        String id = ForgeRegistries.ENCHANTMENTS.getKey(enchant).toString();
        ListTag list = stack.getEnchantmentTags();

        for (int i = 0, m = list.size(); i < m; i++) {
            CompoundTag tag = list.getCompound(i);

            if (id.equalsIgnoreCase(tag.getString("id"))) {
                return tag.getInt("lvl");
            }
        }

        return 0;
    }
}