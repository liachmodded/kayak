/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.ui.property;

import com.github.liachmodded.kayak.entity.FurnaceBoatEntity;
import com.github.liachmodded.kayak.ui.KayakContainerProviders;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;

public final class KayakContainerTools {

  public static void open(PlayerEntity player, FurnaceBoatEntity boat) {
    ContainerProviderRegistry.INSTANCE.openContainer(KayakContainerProviders.FURNACE_BOAT, player, buf -> {
      buf.writeVarInt(boat.getEntityId());
    });
  }

  private KayakContainerTools() {}

}
