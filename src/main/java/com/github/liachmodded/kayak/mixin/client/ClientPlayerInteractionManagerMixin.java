/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.mixin.client;

import com.github.liachmodded.kayak.entity.CustomInventoryVehicle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

  @Shadow @Final private MinecraftClient client;

  @Inject(method = "hasRidingInventory", at = @At("HEAD"), cancellable = true)
  public void onHasRidingInventory(CallbackInfoReturnable<Boolean> ci) {
    if (client.player.getVehicle() instanceof CustomInventoryVehicle) {
      ci.setReturnValue(true);
    }
  }
}
