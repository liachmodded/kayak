/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.item;

import com.github.liachmodded.kayak.Kayak;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.entity.vehicle.BoatEntity.Type;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

public final class KayakItems {

  public static final Map<Type, Item> BOAT_ITEMS;

  static {
    Map<Type, Item> boats = new EnumMap<>(Type.class);
    Settings settings = new Settings().maxCount(1).group(ItemGroup.TRANSPORTATION);
    for (Type boatType : Type.values()) {
      boats.put(boatType, register(boatType.getName() + "_carrier_boat", new BlockCarrierBoatItem(boatType, settings)));
    }
    BOAT_ITEMS = Collections.unmodifiableMap(boats);
  }

  static <T extends Item> T register(String name, T item) {
    return Registry.register(Registry.ITEM, Kayak.name(name), item);
  }

  public static Item getVanillaBoat(Type boatType) {
    switch (boatType) {
      case OAK:
        return Items.OAK_BOAT;
      case SPRUCE:
        return Items.SPRUCE_BOAT;
      case BIRCH:
        return Items.BIRCH_BOAT;
      case JUNGLE:
        return Items.JUNGLE_BOAT;
      case ACACIA:
        return Items.ACACIA_BOAT;
      case DARK_OAK:
        return Items.DARK_OAK_BOAT;
    }
    Kayak.LOGGER.error("{} is an unknown boat type!", boatType);
    return Items.OAK_BOAT;
  }

  public static void init() {
    // classloading
  }

  private KayakItems() {}

}
