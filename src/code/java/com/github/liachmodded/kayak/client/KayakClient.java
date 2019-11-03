/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.client;

import com.github.liachmodded.kayak.client.render.entity.BlockCarrierBoatEntityRenderer;
import com.github.liachmodded.kayak.client.ui.KayakScreenProviders;
import com.github.liachmodded.kayak.entity.KayakEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public final class KayakClient implements ClientModInitializer {

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

    KayakScreenProviders.init();
  }
}
