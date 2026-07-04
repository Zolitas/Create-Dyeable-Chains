package de.zolitas.createdyeablechains.mixin.compat;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.reggarf.mods.create_colored_chain_conveyor.blockEntity.renderers.CCCCChainConveyorRenderer;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.simibubi.create.foundation.render.RenderTypes;
import de.zolitas.createdyeablechains.ClientChainDyeUtils;
import de.zolitas.createdyeablechains.MixedChainConveyor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorRenderer.CHAIN_LOCATION;

/**
 * This is a duplicate of {@link de.zolitas.createdyeablechains.mixin.ChainConveyorRendererMixin}
 */
@Mixin(CCCCChainConveyorRenderer.class)
public class CCCCChainConveyorRendererMixin {
  @Redirect(method = "renderChains", at = @At(value = "INVOKE", target = "Lcom/reggarf/mods/create_colored_chain_conveyor/blockEntity/renderers/CCCCChainConveyorRenderer;renderChain(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;FFIIZ)V"))
  private void renderChain(PoseStack ms, MultiBufferSource buffer, float animation, float length,
                           int light1, int light2, boolean far,
                           @Local(argsOnly = true) ChainConveyorBlockEntity chainConveyor,
                           @Local(name = "blockPos") BlockPos blockPos) {
    //noinspection DuplicatedCode
    float radius = far ? 1f / 16f : 1.5f / 16f;
    float minV = far ? 0 : animation;
    float maxV = far ? 1 / 16f : length + minV;
    float minU = far ? 3 / 16f : 0;
    float maxU = far ? 4 / 16f : 3 / 16f;

    ms.pushPose();
    ms.translate(0.5D, 0.0D, 0.5D);

    DyeColor dyeColor = ((MixedChainConveyor) chainConveyor).create_dyeable_chains$getConnectionColorMap().get(blockPos);
    VertexConsumer vc = buffer.getBuffer(RenderTypes.chain(dyeColor != null ? ClientChainDyeUtils.getDyedChainTexture(dyeColor) : CHAIN_LOCATION));

    renderPart(ms, vc, length, 0.0F, radius, radius, 0.0F, -radius, 0.0F, 0.0F, -radius, minU, maxU, minV, maxV,
        light1, light2, far);

    ms.popPose();
  }

  @Shadow
  private static void renderPart(PoseStack pPoseStack, VertexConsumer pConsumer, float pMaxY, float pX0, float pZ0,
                                 float pX1, float pZ1, float pX2, float pZ2, float pX3, float pZ3, float pMinU, float pMaxU, float pMinV,
                                 float pMaxV, int light1, int light2, boolean far) {}
}
