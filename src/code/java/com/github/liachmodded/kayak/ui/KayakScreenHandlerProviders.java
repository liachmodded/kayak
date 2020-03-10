/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.ui;

import com.github.liachmodded.kayak.Kayak;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class KayakScreenHandlerProviders {

  public static final Identifier FURNACE_BOAT = register("furnace_boat", FurnaceBoatScreenHandler::fromPacket);

  private KayakScreenHandlerProviders() {}

  static Identifier register(String name, ContainerFactory<@Nullable ScreenHandler> factory) {
    Identifier id = Kayak.name(name);
    ContainerProviderRegistry.INSTANCE.registerFactory(id, factory);
    return id;
  }

  public static void init() {
    // classloading
  }

}
