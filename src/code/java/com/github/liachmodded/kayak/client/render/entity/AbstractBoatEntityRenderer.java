/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.client.render.entity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.render.LayeredVertexConsumerStorage;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.BoatEntity.Type;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MatrixStack;
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
  public Identifier getTexture(T boatEntity_1) {
    return TEXTURES.get(boatEntity_1.getBoatType());
  }

  protected abstract void renderContent(T entity, double double_1, double double_2, double double_3, float float_1, float float_2,
      MatrixStack matrixStack_1, LayeredVertexConsumerStorage layeredVertexConsumerStorage_1);

  @Override
  public void render(T boatEntity_1, double double_1, double double_2, double double_3, float float_1, float float_2, MatrixStack matrixStack_1,
      LayeredVertexConsumerStorage layeredVertexConsumerStorage_1) {
    matrixStack_1.push();
    matrixStack_1.translate(0.0D, 0.375D, 0.0D);
    matrixStack_1.multiply(Vector3f.POSITIVE_Y.getRotationQuaternion(180.0F - float_1));
    float float_3 = (float) boatEntity_1.getDamageWobbleTicks() - float_2;
    float float_4 = boatEntity_1.getDamageWobbleStrength() - float_2;
    if (float_4 < 0.0F) {
      float_4 = 0.0F;
    }

    if (float_3 > 0.0F) {
      matrixStack_1.multiply(Vector3f.POSITIVE_X
          .getRotationQuaternion(MathHelper.sin(float_3) * float_3 * float_4 / 10.0F * (float) boatEntity_1.getDamageWobbleSide()));
    }

    float float_5 = boatEntity_1.interpolateBubbleWobble(float_2);
    if (!MathHelper.approximatelyEquals(float_5, 0.0F)) {
      matrixStack_1.multiply(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), boatEntity_1.interpolateBubbleWobble(float_2), true));
    }

    matrixStack_1.scale(-1.0F, -1.0F, 1.0F);
    int int_1 = boatEntity_1.getLightmapCoordinates();
    matrixStack_1.multiply(Vector3f.POSITIVE_Y.getRotationQuaternion(90.0F));

    renderContent(boatEntity_1, double_1, double_2, double_3, float_1, float_2, matrixStack_1, layeredVertexConsumerStorage_1);

    this.model.method_22952(boatEntity_1, float_2, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
    VertexConsumer vertexConsumer_1 = layeredVertexConsumerStorage_1.getBuffer(this.model.method_23500(this.getTexture(boatEntity_1)));
    this.model.renderItem(matrixStack_1, vertexConsumer_1, int_1, OverlayTexture.field_21444, 1.0F, 1.0F, 1.0F);
    VertexConsumer vertexConsumer_2 = layeredVertexConsumerStorage_1.getBuffer(RenderLayer.getWaterMask());
    this.model.method_22954().render(matrixStack_1, vertexConsumer_2, 0.0625F, int_1, OverlayTexture.field_21444, (Sprite) null);
    matrixStack_1.pop();
    super.render(boatEntity_1, double_1, double_2, double_3, float_1, float_2, matrixStack_1, layeredVertexConsumerStorage_1);
  }
}
