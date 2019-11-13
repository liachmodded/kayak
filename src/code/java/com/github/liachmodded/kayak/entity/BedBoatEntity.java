/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.mixin.BoatEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

public class BedBoatEntity extends BoatEntity {

  private static final TrackedData<Byte> COLOR = DataTracker
      .registerData(BedBoatEntity.class, TrackedDataHandlerRegistry.BYTE);

  protected BedBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
    super(entityType, world);
  }

  @Override
  protected void initDataTracker() {
    super.initDataTracker();
    this.dataTracker.startTracking(COLOR, (byte) 0);
  }

  public void toggleSleepState() {
    Entity passenger = getPrimaryPassenger();
    if (!(passenger instanceof LivingEntity)) {
      return;
    }
    LivingEntity livingEntity = (LivingEntity) passenger;
    if (livingEntity.isSleeping()) {
      livingEntity.wakeUp();
      livingEntity.startRiding(this);
    } else {
      ((SleepableLivingEntity) passenger).sleep();
    }
  }

  public DyeColor getBedColor() {
    return DyeColor.byId(dataTracker.get(COLOR));
  }

  public void setBedColor(DyeColor color) {
    dataTracker.set(COLOR, (byte) color.getId());
  }

  @Override
  protected boolean canAddPassenger(Entity passenger) {
    return this.getPassengerList().size() < 1;
  }

  @Override
  public void updatePassengerPosition(Entity passenger) {
    // Make it only apply to one passenger
    if (this.hasPassenger(passenger)) {
      float float_2 = (float) ((this.removed ? 0.009999999776482582D : this.getMountedHeightOffset()) + passenger.getHeightOffset());

      passenger.setPosition(this.getX(), this.getY() + (double) float_2, this.getZ());
      passenger.yaw += ((BoatEntityAccess) this).getYawVelocity();
      passenger.setHeadYaw(passenger.getHeadYaw() + ((BoatEntityAccess) this).getYawVelocity());
      this.copyEntityData(passenger);
    }
  }

  @Override
  protected void writeCustomDataToTag(CompoundTag tag) {
    super.writeCustomDataToTag(tag);
    tag.putByte("color", dataTracker.get(COLOR));
  }

  @Override
  protected void readCustomDataFromTag(CompoundTag tag) {
    super.readCustomDataFromTag(tag);
    dataTracker.set(COLOR, tag.getByte("color"));
  }
}
