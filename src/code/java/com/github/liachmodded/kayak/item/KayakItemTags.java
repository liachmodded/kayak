/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.item;

import com.github.liachmodded.kayak.Kayak;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public final class KayakItemTags {

  public static final Identifier CHEST_BOAT = Kayak.name("chest_boat");
  public static final Identifier HOPPER_BOAT = Kayak.name("hopper_boat");

  public static Tag<Item> get(World world, Identifier id) {
    return world.getTagManager().items().getOrCreate(id);
  }

  public static Tag<Item> get(MinecraftServer server, Identifier id) {
    return server.getTagManager().items().getOrCreate(id);
  }

  private KayakItemTags() {}
}
