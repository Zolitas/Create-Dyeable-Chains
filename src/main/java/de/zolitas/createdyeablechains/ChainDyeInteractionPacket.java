package de.zolitas.createdyeablechains;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;

import javax.annotation.Nullable;
import java.util.Map;

public class ChainDyeInteractionPacket extends BlockEntityConfigurationPacket<ChainConveyorBlockEntity> {
  public static final StreamCodec<FriendlyByteBuf, ChainDyeInteractionPacket> STREAM_CODEC = StreamCodec.composite(
      BlockPos.STREAM_CODEC, packet -> packet.pos,
      BlockPos.STREAM_CODEC, packet -> packet.selectedConnection,
      CatnipStreamCodecBuilders.nullable(DyeColor.STREAM_CODEC), packet -> packet.dyeColor,
      ChainDyeInteractionPacket::new
  );

  private final BlockPos selectedConnection;
  @Nullable private final DyeColor dyeColor;

  public ChainDyeInteractionPacket(BlockPos pos, BlockPos selectedConnection, @Nullable DyeColor dyeColor) {
    super(pos);
    this.selectedConnection = selectedConnection;
    this.dyeColor = dyeColor;
  }

  @Override
  protected void applySettings(ServerPlayer serverPlayer, ChainConveyorBlockEntity chainConveyor) {
    if (selectedConnection == null) return;
    BlockPos localConnection = selectedConnection.subtract(chainConveyor.getBlockPos());
    if (!chainConveyor.connections.contains(localConnection)) return;

    MixedChainConveyor mixedChainConveyor = (MixedChainConveyor) chainConveyor;
    Map<BlockPos, DyeColor> connectionColors = mixedChainConveyor.create_dyeable_chains$getConnectionColorMap();

    if (dyeColor != null) {
      connectionColors.put(localConnection, dyeColor);
    }
    else {
      connectionColors.remove(localConnection);
    }
  }

  @Override
  public PacketTypeProvider getTypeProvider() {
    return ChainDyePacket.CHAIN_DYE;
  }
}
