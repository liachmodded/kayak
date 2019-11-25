/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.stat;

import com.github.liachmodded.kayak.Kayak;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class KayakStats {

  public static final Identifier CHEST_BOAT_INTERACTION = register("chest_boat_interaction");
  public static final Identifier HOPPER_BOAT_INTERACTION = register("hopper_boat_interaction");
  public static final Identifier FURNACE_BOAT_INTERACTION = register("furnace_boat_interaction");

  private KayakStats() {}

  private static Identifier register(String name, StatFormatter formatter) {
    Identifier id = Kayak.name(name);
    Registry.register(Registry.CUSTOM_STAT, name, id);
    Stats.CUSTOM.getOrCreateStat(id, formatter);
    return id;
  }

  private static Identifier register(String name) {
    Identifier id = Kayak.name(name);
    Registry.register(Registry.CUSTOM_STAT, name, id);
    return id;
  }

  public static void init() {
    // classloading
  }

}
