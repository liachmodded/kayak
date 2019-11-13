/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.client.render.entity;

import com.github.liachmodded.kayak.entity.BedBoatEntity;
import com.github.liachmodded.kayak.item.inventory.KayakInventoryTools;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;

public final class BedBoatEntityRenderer<T extends BedBoatEntity> extends AbstractBoatEntityRenderer<T> {

  public BedBoatEntityRenderer(EntityRenderDispatcher dispatcher) {
    super(dispatcher);
  }

  @Override
  protected void renderContent(T entity, float yaw, float tickDelta,
      MatrixStack matrices, VertexConsumerProvider vertexConsumers, int ambientLightmapIndex) {
    BlockState state = KayakInventoryTools.BEDS.get(entity.getBedColor()).getDefaultState();
    if (state.getRenderType() != BlockRenderType.INVISIBLE) {
      int blockOffset = 6;//entity.getBlockOffset();
      int blockLight = state.hasEmissiveLighting() ? 15 : Math.max((ambientLightmapIndex & ((1 << 20) - 1)) >> 4, state.getLuminance());
      int lightmapIndex = ((ambientLightmapIndex >> 20) << 20) | blockLight << 4;
      matrices.push();
      matrices.scale(-1, -1, 1); // Boat is flipped by default
      float scale = 0.75F;
      matrices.scale(scale, scale, scale);
      matrices.translate(-0.5D, (blockOffset - 8) / 16.0F, 0.5D);
      matrices.translate(0.7D, 0D, 0D);
      BlockRenderManager renderManager = MinecraftClient.getInstance().getBlockRenderManager();
      renderManager.renderBlockAsEntity(state.with(BedBlock.PART, BedPart.FOOT), matrices, vertexConsumers, lightmapIndex, OverlayTexture.DEFAULT_UV);
      matrices.translate(-1D, 0D, 0D);
      renderManager.renderBlockAsEntity(state.with(BedBlock.PART, BedPart.HEAD), matrices, vertexConsumers, lightmapIndex, OverlayTexture.DEFAULT_UV);

      matrices.pop();
    }
  }
}
