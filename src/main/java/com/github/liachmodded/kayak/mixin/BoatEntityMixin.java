/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.mixin;

import com.github.liachmodded.kayak.entity.CustomDropBoat;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.world.World;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {

  public BoatEntityMixin(EntityType<?> entityType_1, World world_1) {
    super(entityType_1, world_1);
  }

  @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;remove()V"))
  public void beforeRemove(DamageSource source, float damage, CallbackInfoReturnable<Boolean> ci) {
    if (this instanceof CustomDropBoat) {
      ((CustomDropBoat) this).dropCustom(source);
    }
  }
  
  @Redirect(method = "tick", slice = @Slice(
      from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;hasPassenger(Lnet/minecraft/entity/Entity;)Z"),
      to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;hasVehicle()Z")
  ), at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", remap = false))
  public int pickupEntityThreshold(List<?> passengers) {
    // smaller than 2: allow pickup
    return this.canAddPassenger(null) ? 1 : 2;
  }
}
