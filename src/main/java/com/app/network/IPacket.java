package com.app.network;

/**
 * Contains classes related to networking and packet handling.
 */
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * This interface represents a packet that can be sent and received in the network.
 * Implementing classes should provide methods to encode, decode, and handle the packet.
 */
public interface IPacket {

    /**
     * Encodes the packet's data into the provided buffer.
     *
     * @param buffer The buffer to write the encoded data to.
     */
    void encode(FriendlyByteBuf buffer);

    /**
     * Decodes the packet's data from the provided buffer.
     *
     * @param buffer The buffer to read the encoded data from.
     */
    void decode(FriendlyByteBuf buffer);

    /**
     * Handles the received packet on the specified network context.
     *
     * @param context The network context for handling the packet.
     */
    void handle(NetworkEvent.Context context);
}
