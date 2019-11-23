/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.mixin;

import com.github.liachmodded.kayak.entity.SleepableLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SleepableLivingEntity {

  @Shadow
  protected abstract void setPositionInBed(BlockPos pos);

  @Shadow
  public abstract void setSleepingPosition(BlockPos pos);

  private boolean movingSleeping;

  public LivingEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Override
  public void sleep() {
    this.setPose(EntityPose.SLEEPING);
    //this.setPositionInBed(pos);
//    this.setSleepingPosition(pos);
    this.setVelocity(Vec3d.ZERO);
    this.velocityDirty = true;
    this.movingSleeping = true;
  }

//  @Inject(method = "baseTick()V", at = @At("TAIL"))
//  public void kayak$onBaseTick(CallbackInfo ci) {
//    if (this.movingSleeping) {
//      setSleepingPosition(getBlockPos());
//    }
//  }

  @Inject(method = "isSleeping", at = @At("HEAD"), cancellable = true)
  public void kayak$onSleeping(CallbackInfoReturnable<Boolean> ci) {
    if (this.movingSleeping)
      ci.setReturnValue(true);
  }

  @Inject(method = "isSleepingInBed", at = @At("HEAD"), cancellable = true)
  public void kayak$onSleepingInBed(CallbackInfoReturnable<Boolean> ci) {
    if (this.movingSleeping)
      ci.setReturnValue(true);
  }

  @Inject(method = "wakeUp()V", at = @At("TAIL"))
  public void kayak$onWakeUp(CallbackInfo ci) {
    this.movingSleeping = false;
  }

  @Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
  public void onReadTag(CompoundTag tag, CallbackInfo ci) {
    movingSleeping = tag.getBoolean("kayak.movingSleeping");
  }

  @Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
  public void onWriteTag(CompoundTag tag, CallbackInfo ci) {
    tag.putBoolean("kayak.movingSleeping", movingSleeping);
  }
}
