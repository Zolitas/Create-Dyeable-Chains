package de.zolitas.createdyeablechains;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;

import java.util.Map;

public interface MixedChainConveyor {
  Map<BlockPos, DyeColor> create_dyeable_chains$getConnectionColorMap();
}
