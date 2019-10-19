/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.item.KayakItems;
import com.github.liachmodded.kayak.mixin.BoatEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class BlockCarrierBoatEntity extends BoatEntity implements PositionInitializerEntity {

  protected BlockCarrierBoatEntity(EntityType<? extends BoatEntity> entityType_1, World world_1) {
    super(entityType_1, world_1);
  }

  @Override
  public void init(double x, double y, double z) {
    this.setPosition(x, y, z);
    this.setVelocity(Vec3d.ZERO);
    this.prevX = x;
    this.prevY = y;
    this.prevZ = z;
  }

  public abstract BlockState getCarriedState();

  @Override
  public boolean interact(PlayerEntity playerEntity_1, Hand hand_1) {
    return false; // Moved to hitPos aware version
  }

  @Override
  public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
    // test negative dot product
    Vec3d facing = getRotationVector(0f, this.yaw);
    boolean back = hitPos.dotProduct(facing) < 0;
    if (!back) {
      if (super.interact(player, hand)) {
        return ActionResult.SUCCESS;
      }
      return super.interactAt(player, hitPos, hand);
    }

    if (!world.isClient) {
      interactRear(player, hand);
      return ActionResult.SUCCESS;
    }
    return super.interactAt(player, hitPos, hand);
  }

  protected abstract void interactRear(PlayerEntity player, Hand hand);

  @Override
  public void updatePassengerPosition(Entity entity_1) {
    // Make it only apply to one passenger
    if (this.hasPassenger(entity_1)) {
      float float_2 = (float) ((this.removed ? 0.009999999776482582D : this.getMountedHeightOffset()) + entity_1.getHeightOffset());

      Vec3d vec3d_1 = (new Vec3d(0.2F, 0.0D, 0.0D)).rotateY(-this.yaw * 0.017453292F - 1.5707964F);
      entity_1.setPosition(this.getX() + vec3d_1.x, this.getY() + (double) float_2, this.getZ() + vec3d_1.z);
      entity_1.yaw += ((BoatEntityAccess) this).getYawVelocity();
      entity_1.setHeadYaw(entity_1.getHeadYaw() + ((BoatEntityAccess) this).getYawVelocity());
      this.copyEntityData(entity_1);
    }
  }

  @Override
  public Item asItem() {
    return KayakItems.BOAT_ITEMS.get(getBoatType());
  }

  @Override
  protected boolean canAddPassenger(Entity entity_1) {
    return this.getPassengerList().size() < 1;
  }


}
