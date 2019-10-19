/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.mixin;

import com.github.liachmodded.kayak.entity.CustomInventoryVehicle;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HorseBaseEntity.class)
public abstract class BaseHorseEntityMixin implements CustomInventoryVehicle {

  @Shadow public abstract void openInventory(PlayerEntity playerEntity_1);

  @Override
  public void openCustomInventory(ServerPlayerEntity player) {
    this.openInventory(player);
  }
}
