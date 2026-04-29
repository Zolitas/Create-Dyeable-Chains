package de.zolitas.createdyeablechains;

import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public enum ChainDyePacket implements BasePacketPayload.PacketTypeProvider {
  CHAIN_DYE(ChainDyeInteractionPacket.class, ChainDyeInteractionPacket.STREAM_CODEC);

  private final CatnipPacketRegistry.PacketType<?> type;

  <T extends BasePacketPayload> ChainDyePacket(Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
    String name = this.name().toLowerCase(Locale.ROOT);
    this.type = new CatnipPacketRegistry.PacketType<>(
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateDyeableChains.MOD_ID, name)),
        clazz, codec
    );
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
    return (CustomPacketPayload.Type<T>) this.type.type();
  }

  public static void register() {
    CatnipPacketRegistry packetRegistry = new CatnipPacketRegistry(CreateDyeableChains.MOD_ID, CreateDyeableChains.PACKET_VERSION);
    for (ChainDyePacket packet : ChainDyePacket.values()) {
      packetRegistry.registerPacket(packet.type);
    }
    packetRegistry.registerAllPackets();
  }
}
