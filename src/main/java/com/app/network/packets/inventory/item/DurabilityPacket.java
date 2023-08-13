package com.app.network.packets.inventory.item;

/**
 * Contains classes related to networking and packet handling.
 */
import com.app.network.IPacket;
import com.app.util.MathExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Contains classes related to player and item handling.
 */
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


/**
 * A packet class for handling durability information related to items.
 */
public class DurabilityPacket implements IPacket {

    private ItemStack item;
    int durability;
    private Operation operation;

    /**
     * Enum defining the possible operations for the durability packet.
     */
    public enum Operation {
        ADD,
        SUBTRACT,
        MULTIPLE,
        DIVIDE,
        MODIFY
    }

    /**
     * Default constructor for DurabilityPacket.
     */
    public DurabilityPacket() {}

    /**
     * Constructor for creating a DurabilityPacket with specific parameters.
     *
     * @param item The ItemStack representing the item for which durability is being conveyed.
     * @param durability The durability value associated with the item.
     * @param operation The operation indicating how the durability information should be used.
     */
    public DurabilityPacket(ItemStack item, int durability, Operation operation) {
        this.item = item;
        this.durability = durability * -1;
        this.operation = operation;
    }

    /**
     * Encodes the packet's data into the provided buffer.
     *
     * @param buffer The buffer to write the encoded data to.
     */
    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItemStack(item, false);
        buffer.writeInt(durability);
        buffer.writeEnum(operation);
    }

    /**
     * Decodes the packet's data from the provided buffer.
     *
     * @param buffer The buffer to read the encoded data from.
     */
    @Override
    public void decode(FriendlyByteBuf buffer) {
        this.item = buffer.readItem();
        this.durability = buffer.readInt();
        this.operation = buffer.readEnum(Operation.class);
    }

    /**
     * Handles the received packet on the specified network context.
     *
     * @param context The network context for handling the packet.
     */
    @Override
    public void handle(NetworkEvent.Context context) {
        Player player = context.getSender();

        assert (player != null && player.level.isClientSide) : "The player object is null or the player's level is not on the client side.";

        int slot = player.getInventory().findSlotMatchingItem(this.item);

        if (slot != -1 && this.item.isDamageableItem()) {
            item = player.getInventory().getItem(slot);

            int maxDurability = item.getMaxDamage();

            switch (this.operation) {
                case ADD -> item.setDamageValue(MathExtension.clamp(item.getDamageValue() + this.durability, 0, maxDurability));
                case SUBTRACT -> item.setDamageValue(MathExtension.clamp(item.getDamageValue() - this.durability, 0, maxDurability));
                case MULTIPLE -> item.setDamageValue(MathExtension.clamp(item.getDamageValue() * this.durability, 0, maxDurability));
                case DIVIDE -> item.setDamageValue(MathExtension.clamp(item.getDamageValue() / this.durability, 0, maxDurability));
                case MODIFY -> item.setDamageValue(MathExtension.clamp(this.durability, 0, maxDurability));
            }

            if (this.durability == maxDurability) {
                player.getInventory().removeItem(item);
            }

            context.setPacketHandled(true);
        }
    }
}
