/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.client.render.entity;

import com.github.liachmodded.kayak.entity.PitchableBoat;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.BoatEntity.Type;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;

public abstract class AbstractBoatEntityRenderer<T extends BoatEntity> extends EntityRenderer<T> {

  protected static final Map<Type, Identifier> TEXTURES;

  static {
    Map<BoatEntity.Type, Identifier> skins = new EnumMap<>(BoatEntity.Type.class);
    for (BoatEntity.Type type : BoatEntity.Type.values()) {
      skins.put(type, new Identifier("minecraft", "textures/entity/boat/" + type.getName() + ".png"));
    }
    TEXTURES = Collections.unmodifiableMap(skins);
  }

  protected final BoatEntityModel model = createModel();

  protected AbstractBoatEntityRenderer(EntityRenderDispatcher entityRenderDispatcher_1) {
    super(entityRenderDispatcher_1);
    this.field_4673 = 0.8F;
  }

  protected BoatEntityModel createModel() {
    return new BoatEntityModel();
  }

  @Override
  public Identifier getTexture(T entity) {
    return TEXTURES.get(entity.getBoatType());
  }

  protected abstract void renderContent(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
      int light);

  @Override
  public void render(T boatEntity_1, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
    matrices.push();
    matrices.translate(0.0D, 0.375D, 0.0D);
    matrices.multiply(Vector3f.POSITIVE_Y.getRotationQuaternion(180.0F - yaw));
    float float_3 = (float) boatEntity_1.getDamageWobbleTicks() - tickDelta;
    float float_4 = boatEntity_1.getDamageWobbleStrength() - tickDelta;
    if (float_4 < 0.0F) {
      float_4 = 0.0F;
    }

    if (float_3 > 0.0F) {
      matrices.multiply(Vector3f.POSITIVE_X
          .getRotationQuaternion(MathHelper.sin(float_3) * float_3 * float_4 / 10.0F * (float) boatEntity_1.getDamageWobbleSide()));
    }

    float float_5 = boatEntity_1.interpolateBubbleWobble(tickDelta);
    if (!MathHelper.approximatelyEquals(float_5, 0.0F)) {
      matrices.multiply(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), boatEntity_1.interpolateBubbleWobble(tickDelta), true));
    }

    if (boatEntity_1 instanceof PitchableBoat) {
      float pitch = ((PitchableBoat) boatEntity_1).getRenderPitch(tickDelta);
      matrices.multiply(Vector3f.POSITIVE_X.getRotationQuaternion(pitch));
    }

    matrices.scale(-1.0F, -1.0F, 1.0F);
    matrices.multiply(Vector3f.POSITIVE_Y.getRotationQuaternion(90.0F));

    renderContent(boatEntity_1, yaw, tickDelta, matrices, vertexConsumers, light);

    this.model.method_22952(boatEntity_1, tickDelta, 0.0F, -0.1F, 0.0F, 0.0F);
    VertexConsumer vertexConsumer_1 = vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(boatEntity_1)));
    this.model.render(matrices, vertexConsumer_1, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F);
    VertexConsumer vertexConsumer_2 = vertexConsumers.getBuffer(RenderLayer.getWaterMask());
    this.model.getBottom().render(matrices, vertexConsumer_2, light, OverlayTexture.DEFAULT_UV, null);
    matrices.pop();
    super.render(boatEntity_1, yaw, tickDelta, matrices, vertexConsumers, light);
  }
}
