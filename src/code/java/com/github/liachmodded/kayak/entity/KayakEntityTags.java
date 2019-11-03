/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.Kayak;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public final class KayakEntityTags {

  public static final Identifier CARRIER_BOAT = Kayak.name("carrier_boat");

  public static Tag<EntityType<?>> get(World world, Identifier id) {
    return world.getTagManager().entityTypes().getOrCreate(id);
  }

  public static Tag<EntityType<?>> get(MinecraftServer server, Identifier id) {
    return server.getTagManager().entityTypes().getOrCreate(id);
  }

  private KayakEntityTags() {}
}
