/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.ui;

import com.github.liachmodded.kayak.Kayak;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.container.Container;
import net.minecraft.util.Identifier;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class KayakContainerProviders {

  public static final Identifier FURNACE_BOAT = register("furnace_boat", FurnaceBoatContainer::fromPacket);

  private KayakContainerProviders() {}

  static Identifier register(String name, ContainerFactory<@Nullable Container> factory) {
    Identifier id = Kayak.name(name);
    ContainerProviderRegistry.INSTANCE.registerFactory(id, factory);
    return id;
  }

  public static void init() {
    // classloading
  }

}
