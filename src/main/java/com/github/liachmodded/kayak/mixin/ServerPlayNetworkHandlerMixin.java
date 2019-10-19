/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.mixin;

import com.github.liachmodded.kayak.entity.CustomInventoryVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

  @Redirect(method = "onClientCommand", slice = @Slice(
      from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/JumpingMount;stopJumping()V"),
      to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/HorseBaseEntity;openInventory(Lnet/minecraft/entity/player/PlayerEntity;)V")
  ), at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getVehicle()Lnet/minecraft/entity/Entity;", ordinal = 0))
  public Entity handleOpenInventoryCommand(ServerPlayerEntity player) {
    Entity vehicle = player.getVehicle();
    if (vehicle instanceof CustomInventoryVehicle) {
      ((CustomInventoryVehicle) vehicle).openCustomInventory(player);
    }
    return null; // will fail instanceof check and nothing happens then
  }

}
