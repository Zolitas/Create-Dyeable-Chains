package de.zolitas.createdyeablechains.mixin;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import de.zolitas.createdyeablechains.MixedChainConveyor;
import net.createmod.catnip.codecs.CatnipCodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(ChainConveyorBlockEntity.class)
public class ChainConveyerBlockEntityMixin implements MixedChainConveyor {
  @Shadow
  public Set<BlockPos> connections;
  @Unique
  private final Map<BlockPos, DyeColor> create_dyeable_chains$connectionColors = new HashMap<>();

  @Override
  public Map<BlockPos, DyeColor> create_dyeable_chains$getConnectionColorMap() {
    return create_dyeable_chains$connectionColors;
  }

  @Inject(method = "read", at = @At("TAIL"))
  private void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
    create_dyeable_chains$connectionColors.clear();
    ListTag connectionColors = compound.getList("ConnectionColors", Tag.TAG_COMPOUND);
    connectionColors.forEach(tagItem -> {
      if (!(tagItem instanceof CompoundTag compoundItem)) return;
      int colorId = compoundItem.getInt("Color");
      DyeColor dyeColor = DyeColor.byId(colorId);
      BlockPos connection = CatnipCodecUtils.decode(BlockPos.CODEC, registries, compoundItem.get("Connection")).orElseThrow();
      create_dyeable_chains$connectionColors.put(connection, dyeColor);
    });
  }

  @Inject(method = "write", at = @At("TAIL"))
  private void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
    ListTag list = new ListTag();
    create_dyeable_chains$connectionColors.forEach((blockPos, dyeColor) -> {
      CompoundTag compoundItem = new CompoundTag();
      compoundItem.put("Connection", CatnipCodecUtils.encode(BlockPos.CODEC, blockPos).orElseThrow());
      compoundItem.putInt("Color", dyeColor.getId());
      list.add(compoundItem);
    });
    compound.put("ConnectionColors", list);
  }

  @Inject(method = "notifyUpdate", at = @At("TAIL"))
  private void notifyUpdate(CallbackInfo ci) {
    Set<BlockPos> posToBeRemoved = new HashSet<>();
    create_dyeable_chains$connectionColors.forEach((blockPos, dyeColor) -> {
      if (!connections.contains(blockPos)) {
        posToBeRemoved.add(blockPos);
      }
    });
    posToBeRemoved.forEach(create_dyeable_chains$connectionColors::remove);
  }
}
