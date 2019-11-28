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
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;

public final class BlockCarrierBoatEntityRenderer<T extends CarrierBoatEntity> extends AbstractBoatEntityRenderer<T> {

  public BlockCarrierBoatEntityRenderer(EntityRenderDispatcher entityRenderDispatcher_1) {
    super(entityRenderDispatcher_1);
  }

  @Override
  protected void renderContent(T entity, float yaw, float tickDelta,
      MatrixStack matrices, VertexConsumerProvider vertexConsumers, int ambientLightmapIndex) {
    BlockState state = entity.getCarriedState();
    if (state.getRenderType() != BlockRenderType.INVISIBLE) {
      int blockOffset = entity.getBlockOffset();
      int blockLight = state.hasEmissiveLighting() ? 15 : Math.max((ambientLightmapIndex & ((1 << 20) - 1)) >> 4, state.getLuminance());
      int lightmapIndex = ((ambientLightmapIndex >> 20) << 20) | blockLight << 4;
      matrices.push();
      matrices.scale(-1, -1, 1); // Boat is flipped by default
      float scale = 0.75F;
      matrices.scale(scale, scale, scale);
      matrices.translate(-0.5D, (blockOffset - 8) / 16.0F, 0.5D);
      matrices.translate(0.6D, 0D, 0D);
      matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
      MinecraftClient.getInstance().getBlockRenderManager()
          .renderBlockAsEntity(state, matrices, vertexConsumers, lightmapIndex, OverlayTexture.DEFAULT_UV);
      matrices.pop();
    }
  }
}
