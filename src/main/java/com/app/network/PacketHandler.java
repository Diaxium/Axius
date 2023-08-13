package com.app.network;

/**
 * Contains classes related to controllers and entity management.
 */
import com.app.controller.Axius;
import com.app.network.packets.inventory.item.DurabilityPacket;
import com.app.network.packets.inventory.item.tool.EnchantPacket;
import com.mojang.math.Vector3d;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.server.level.ServerPlayer;

/**
 * Contains classes related to networking and packet handling.
 */
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Contains classes related to handling resources and registries.
 */
import net.minecraft.resources.ResourceLocation;

/**
 * This class handles packet communication for the Axius mod.
 */
public class PacketHandler {
    /**
     * The version of the packet handler.
     */
    public final String version = "1.0.0";

    /**
     * The channel used for packet communication.
     */
    private final SimpleChannel channel;

    /**
     * The ID to be assigned to the next registered packet.
     */
    private int nextPacketId = 0;

    /**
     * Constructs a new PacketHandler instance.
     * Initializes the packet communication channel.
     */
    public PacketHandler() {
        ResourceLocation channelName = new ResourceLocation(Axius.MODID, "networking");
        channel = NetworkRegistry.ChannelBuilder.named(channelName)
                .networkProtocolVersion(() -> version)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .simpleChannel();

        // Register the EnchantPacket class with the channel
        registerPacket(EnchantPacket.class, EnchantPacket::new);

        // Register the DurabilityPacket class with the channel
        registerPacket(DurabilityPacket.class, DurabilityPacket::new);
    }

    /**
     * Registers a packet class with the channel.
     *
     * @param packetClass The class of the packet to register.
     * @param creator     A supplier function to create a new packet instance.
     * @param <T>         The type of the packet.
     */
    private <T extends IPacket> void registerPacket(Class<T> packetClass, Supplier<T> creator) {
        channel.registerMessage(nextPacketId++,
                packetClass,
                this::writePacket,
                buffer -> readPacket(buffer, creator),
                this::handlePacket
        );
    }

    /**
     * Reads a packet from the buffer using the provided creator function.
     *
     * @param buffer  The buffer to read from.
     * @param creator A supplier function to create a new packet instance.
     * @param <T>     The type of the packet.
     * @return The read packet.
     */
    private <T extends IPacket> T readPacket(FriendlyByteBuf buffer, Supplier<T> creator) {
        T packet = creator.get();
        packet.decode(buffer);
        return packet;
    }

    /**
     * Writes a packet to the buffer.
     *
     * @param packet The packet to write.
     * @param buffer The buffer to write to.
     */
    protected void writePacket(IPacket packet, FriendlyByteBuf buffer) {
        packet.encode(buffer);
    }

    /**
     * Handles a packet using the provided packet and context supplier.
     *
     * @param packet   The packet to handle.
     * @param provider A supplier for the network context.
     */
    protected void handlePacket(IPacket packet, Supplier<NetworkEvent.Context> provider) {
        packet.handle(provider.get());
    }

    /**
     * Get the communication channel.
     *
     * @return The communication channel.
     */
    public SimpleChannel getChannel() {
        return channel;
    }

    /**
     * Sends a packet to a specific player.
     *
     * @param packet The packet to be sent.
     * @param player The player to receive the packet.
     * @throws RuntimeException if the provided player is a client player (not a server player).
     */
    public SimpleChannel sendToPlayer(IPacket packet, ServerPlayer player) {
        if (!(player instanceof ServerPlayer))
            throw new RuntimeException("Sending a Packet to a Player from the client is not allowed");
        // The instance of PacketHandler.
        PacketHandler instance = Axius.getPacketHandler();

        // The Channel of instance.
        SimpleChannel Channel = instance.getChannel();

        // Send the packet to the server
        Channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);

        return Channel;
    }

    /**
     * Sends a packet to the server.
     *
     * @param packet The packet to be sent.
     */
    public void sendToServer(IPacket packet) {
        // The instance of PacketHandler.
        PacketHandler instance = Axius.getPacketHandler();

        // The Channel of instance.
        SimpleChannel Channel = instance.getChannel();

        // Send the packet to the server
        Channel.send(PacketDistributor.SERVER.noArg(), packet);
    }

    /**
     * Sends a packet to all players in the network.
     *
     * @param packet The packet to be sent to all players.
     */
    public SimpleChannel sendToAllPlayers(IPacket packet) {
        // Get the instance of PacketHandler.
        PacketHandler instance = Axius.getPacketHandler();

        // The Channel of instance.
        SimpleChannel Channel = instance.getChannel();

        // Send the packet to all players in the network.
        Channel.send(PacketDistributor.ALL.noArg(), packet);

        return Channel;
    }

    /**
     * Sends a packet to all players tracking a specific chunk.
     *
     * @param packet The packet to be sent.
     * @param chunk The chunk for which players are being tracked.
     */
    public SimpleChannel sendToAllChunkWatchers(IPacket packet, LevelChunk chunk) {
        // Get the instance of PacketHandler.
        PacketHandler instance = Axius.getPacketHandler();

        // The Channel of instance.
        SimpleChannel Channel = instance.getChannel();

        // Send the packet to all players tracking the specified chunk.
        Channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), packet);

        return Channel;
    }

    /**
     * Sends a packet to all players tracking an entity, including the entity itself.
     *
     * @param packet The packet to send.
     * @param entity The entity that is being tracked.
     */
    public SimpleChannel sendToAllEntityWatchers(IPacket packet, Entity entity) {
        // Get the instance of PacketHandler
        PacketHandler instance = Axius.getPacketHandler();

        // The Channel of instance.
        SimpleChannel Channel = instance.getChannel();

        // Send the packet to all players tracking the entity, including the entity itself.
        Channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), packet);

        return Channel;
    }

    /**
     * Sends a packet to players near a specified point within a given range.
     *
     * @param packet The packet to be sent.
     * @param position The position around which players are located.
     * @param range The range within which players should receive the packet.
     */
    public SimpleChannel sendToNearby(IPacket packet, Vector3d position, double range) {
        // The instance of PacketHandler.
        PacketHandler instance = Axius.getPacketHandler();

        // The Channel of instance.
        SimpleChannel Channel = instance.getChannel();

        // Create a TargetPoint with the provided position and range.
        PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(position.x, position.y, position.z, range, null);

        // Send the packet to players near the specified point.
        Channel.send(PacketDistributor.NEAR.with(() -> targetPoint), packet);

        return Channel;
    }
}
