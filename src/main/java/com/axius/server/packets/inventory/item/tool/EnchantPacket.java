package com.axius.server.packets.inventory.item.tool;

/**
 * Contains classes related to networking and packet handling.
 */
import com.axius.controller.Axius;
import com.axius.server.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Contains classes related to handling resources and registries.
 */
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Contains classes related to player and item handling.
 */
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import java.util.Map;

/**
 * Represents a packet for sending enchantment-related information between server and client.
 */
public class EnchantPacket implements IPacket {

    private ItemStack item;
    private ResourceLocation id;
    private int level;
    private Operation operation;

    /**
     * Enum defining the possible operations for the enchantment packet.
     */
    public enum Operation {
        ADD,
        REMOVE,
        MODIFY
    }

    public EnchantPacket() {}

    /**
     * Constructs an EnchantPacket with the given parameters.
     *
     * @param item      The ItemStack on which the enchantment operation is performed.
     * @param enchant   The enchantment to be operated on the ItemStack.
     * @param level     The level of the enchantment.
     * @param operation The operation to be performed (ADD, REMOVE, MODIFY).
     */
    public EnchantPacket(ItemStack item, Enchantment enchant, int level, Operation operation) {
        this.item = item;
        this.level = level;
        this.id = ForgeRegistries.ENCHANTMENTS.getKey(enchant);
        this.operation = operation;
    }

    /**
     * Encodes the packet data into the provided buffer.
     *
     * @param buffer The buffer to encode the data into.
     */
    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItemStack(item, false);
        buffer.writeInt(level);
        buffer.writeResourceLocation(id);
        buffer.writeEnum(operation);
    }

    /**
     * Decodes the packet data from the provided buffer.
     *
     * @param buffer The buffer to decode the data from.
     */
    @Override
    public void decode(FriendlyByteBuf buffer) {
        item = buffer.readItem();
        level = buffer.readInt();
        id = buffer.readResourceLocation();
        operation = buffer.readEnum(Operation.class);
    }

    /**
     * Handles the received packet on the specified network context.
     *
     * @param context The network context for handling the packet.
     */
    public void handle(NetworkEvent.Context context) {
        Player player = context.getSender();

        assert (player != null && player.level.isClientSide) : "The player object is null or the player's level is not on the client side.";

        int slot = player.getInventory().findSlotMatchingItem(this.item);
        Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(this.id);
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(this.item);

        if (slot != -1) {
            switch (this.operation) {
                case ADD -> enchantments.putIfAbsent(enchantment, this.level);
                case REMOVE -> enchantments.remove(enchantment);
                case MODIFY -> enchantments.computeIfPresent(enchantment, (existingEnchantment, existingLevel) -> {
                    return this.level;
                });
            }

            EnchantmentHelper.setEnchantments(enchantments, player.getInventory().getItem(slot));
            context.setPacketHandled(true);
        }
    }
}
