/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.client.render.entity;

import com.github.liachmodded.kayak.entity.CarrierBoatEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LayeredVertexConsumerStorage;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;

public final class BlockCarrierBoatEntityRenderer<T extends CarrierBoatEntity> extends AbstractBoatEntityRenderer<T> {

  public BlockCarrierBoatEntityRenderer(EntityRenderDispatcher entityRenderDispatcher_1) {
    super(entityRenderDispatcher_1);
  }

  @Override
  protected void renderContent(T entity, double double_1, double double_2, double double_3, float float_1, float float_2,
      MatrixStack matrixStack_1, LayeredVertexConsumerStorage layeredVertexConsumerStorage_1) {
    BlockState state = entity.getCarriedState();
    if (state.getRenderType() != BlockRenderType.INVISIBLE) {
      int blockOffset = 6;//entity.getBlockOffset();
      int ambientLightmapIndex = entity.getLightmapCoordinates();
      int blockLight = state.hasEmissiveLighting() ? 15 : Math.max((ambientLightmapIndex & ((1 << 20) - 1)) >> 4, state.getLuminance());
      int lightmapIndex = ((ambientLightmapIndex >> 20) << 20) | blockLight << 4;
      matrixStack_1.push();
      matrixStack_1.scale(-1, -1, 1); // Boat is flipped by default
      float scale = 0.75F;
      matrixStack_1.scale(scale, scale, scale);
      matrixStack_1.translate(-0.5D, (blockOffset - 8) / 16.0F, 0.5D);
      matrixStack_1.translate(0.7D, 0D, 0D);
      MinecraftClient.getInstance().getBlockRenderManager()
          .renderOnEntity(state, matrixStack_1, layeredVertexConsumerStorage_1, lightmapIndex, OverlayTexture.DEFAULT_UV);
      matrixStack_1.pop();
    }
  }
}
