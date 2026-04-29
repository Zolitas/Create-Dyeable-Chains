package de.zolitas.createdyeablechains;

import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

public class ClientChainDyeUtils {
  public static ResourceLocation getDyedChainTexture(DyeColor dyeColor) {
    return ResourceLocation.fromNamespaceAndPath(
        CreateDyeableChains.MOD_ID,
        "textures/block/chain_dyed_" + dyeColor.getName() + ".png"
    );
  }

  public static boolean isHoldingDyeItem() {
    Minecraft mc = Minecraft.getInstance();
    LocalPlayer player = mc.player;
    if (player == null) return false;
    ItemStack handItem = player.getMainHandItem();

    boolean itemHasWater = GenericItemEmptying.emptyItem(mc.level, handItem, true)
        .getFirst()
        .getFluid()
        .isSame(Fluids.WATER);

    return itemHasWater || handItem.is(Tags.Items.DYES);
  }
}
