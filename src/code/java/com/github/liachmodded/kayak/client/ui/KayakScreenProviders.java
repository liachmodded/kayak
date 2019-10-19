/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.client.ui;

import com.github.liachmodded.kayak.ui.FurnaceBoatContainer;
import com.github.liachmodded.kayak.ui.KayakContainerProviders;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.container.Container;
import net.minecraft.util.Identifier;

public final class KayakScreenProviders {

  public static final Identifier FURNACE_BOAT = register(KayakContainerProviders.FURNACE_BOAT,
      (FurnaceBoatContainer container) -> new FurnaceBoatScreen(container, container.getPlayerInventory(), container.getBoat().getName()));

  private KayakScreenProviders() {}

  static <C extends Container> Identifier register(Identifier id, ContainerScreenFactory<C> factory) {
    ScreenProviderRegistry.INSTANCE.registerFactory(id, factory);
    return id;
  }

  public static void init() {
    // classloading
  }

}
