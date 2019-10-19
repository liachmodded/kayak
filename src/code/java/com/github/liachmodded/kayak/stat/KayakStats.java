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

  public static final Identifier CHEST_CARRIER_BOAT_INTERACTION = register("chest_carrier_boat_interaction", StatFormatter.DEFAULT);
  public static final Identifier FURNACE_CARRIER_BOAT_INTERACTION = register("furnace_carrier_boat_interaction", StatFormatter.DEFAULT);

  private KayakStats() {}

  static Identifier register(String name, StatFormatter formatter) {
    Identifier identifier_1 = Kayak.name(name);
    Registry.register(Registry.CUSTOM_STAT, name, identifier_1);
    Stats.CUSTOM.getOrCreateStat(identifier_1, formatter);
    return identifier_1;
  }

  public static void init() {
    // classloading
  }

}
