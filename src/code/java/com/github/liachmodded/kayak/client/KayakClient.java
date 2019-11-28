/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.client;

import com.github.liachmodded.kayak.Kayak;
import com.github.liachmodded.kayak.client.render.entity.BedBoatEntityRenderer;
import com.github.liachmodded.kayak.client.render.entity.BlockCarrierBoatEntityRenderer;
import com.github.liachmodded.kayak.client.ui.KayakScreenProviders;
import com.github.liachmodded.kayak.entity.KayakEntities;
import com.github.liachmodded.kayak.network.KayakNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil.Type;
import org.lwjgl.glfw.GLFW;

public final class KayakClient implements ClientModInitializer {

  private KeyBinding sleepInBoatKey;

  @Override
  public void onInitializeClient() {
    EntityRendererRegistry.INSTANCE.register(KayakEntities.BLOCK_BOAT,
        (dispatcher, context) -> new BlockCarrierBoatEntityRenderer<>(dispatcher));
    EntityRendererRegistry.INSTANCE.register(KayakEntities.CHEST_BOAT,
        (dispatcher, context) -> new BlockCarrierBoatEntityRenderer<>(dispatcher));
    EntityRendererRegistry.INSTANCE.register(KayakEntities.FURNACE_BOAT,
        (dispatcher, context) -> new BlockCarrierBoatEntityRenderer<>(dispatcher));
    EntityRendererRegistry.INSTANCE.register(KayakEntities.HOPPER_BOAT,
        (dispatcher, context) -> new BlockCarrierBoatEntityRenderer<>(dispatcher));
    EntityRendererRegistry.INSTANCE.register(KayakEntities.BED_BOAT,
        (dispatcher, context) -> new BedBoatEntityRenderer<>(dispatcher));
    EntityRendererRegistry.INSTANCE.register(KayakEntities.JUKEBOX_BOAT,
        (dispatcher, context) -> new BlockCarrierBoatEntityRenderer<>(dispatcher));

    KayakScreenProviders.init();

    sleepInBoatKey = FabricKeyBinding.Builder.create(Kayak.name("request_sleep_in_boat"), Type.KEYSYM, GLFW.GLFW_KEY_N, "general").build();
    ClientTickCallback.EVENT.register(this::clientTick);
  }

  public void clientTick(MinecraftClient client) {
    if (sleepInBoatKey.isPressed()) {
      KayakNetworking.requestSleepInBoat();
    }
  }
}
