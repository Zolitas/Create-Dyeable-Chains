package de.zolitas.createdyeablechains.mixin;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorInteractionHandler;
import de.zolitas.createdyeablechains.ChainDyeInteractionPacket;
import de.zolitas.createdyeablechains.ClientChainDyeUtils;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChainConveyorInteractionHandler.class)
public class ChainConveyorInteractionHandlerMixin {
  @Shadow
  public static BlockPos selectedLift;

  @Shadow
  public static BlockPos selectedConnection;

  @Inject(method = "isActive", at = @At("HEAD"), cancellable = true)
  private static void isActive(CallbackInfoReturnable<Boolean> cir) {
    if (ClientChainDyeUtils.isHoldingDyeItem()) {
      cir.setReturnValue(true);
    }
  }

  @ModifyVariable(method = "clientTick", name = "isWrench", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getEyePosition()Lnet/minecraft/world/phys/Vec3;",
      shift = At.Shift.AFTER))
  private static boolean isWrench(boolean isWrench) {
    return ClientChainDyeUtils.isHoldingDyeItem() || isWrench;
  }

  @Inject(method = "onUse", at = @At(value = "TAIL"), cancellable = true)
  private static void onUse(CallbackInfoReturnable<Boolean> cir) {
    if (ClientChainDyeUtils.isHoldingDyeItem()) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) return;
      ItemStack dyeItem = player.getMainHandItem();
      CatnipServices.NETWORK.sendToServer(new ChainDyeInteractionPacket(selectedLift, selectedLift.offset(selectedConnection), DyeColor.getColor(dyeItem)));

      cir.setReturnValue(true);
    }
  }
}
