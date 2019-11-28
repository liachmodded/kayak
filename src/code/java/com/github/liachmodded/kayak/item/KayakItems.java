/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.item;

import com.github.liachmodded.kayak.Kayak;
import com.github.liachmodded.kayak.entity.KayakEntities;
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

  public static final Map<Type, Item> CHEST_BOAT_ITEMS;
  public static final Map<Type, Item> HOPPER_BOAT_ITEMS;
  public static final Map<Type, Item> JUKEBOX_BOAT_ITEMS;

  static {
    Map<Type, Item> chestBoats = new EnumMap<>(Type.class);
    Map<Type, Item> hopperBoats = new EnumMap<>(Type.class);
    Map<Type, Item> jukeboxBoats = new EnumMap<>(Type.class);
    Settings settings = new Settings().maxCount(1).group(ItemGroup.TRANSPORTATION);
    for (Type boatType : Type.values()) {
      chestBoats.put(boatType, register(boatType.getName() + "_chest_boat",
          new CarrierBoatItem(KayakEntities.CHEST_BOAT, boatType, settings)));
      hopperBoats.put(boatType, register(boatType.getName() + "_hopper_boat",
          new CarrierBoatItem(KayakEntities.HOPPER_BOAT, boatType, settings)));
      jukeboxBoats.put(boatType, register(boatType.getName() + "_jukebox_boat",
          new CarrierBoatItem(KayakEntities.JUKEBOX_BOAT, boatType, settings)));
    }
    CHEST_BOAT_ITEMS = Collections.unmodifiableMap(chestBoats);
    HOPPER_BOAT_ITEMS = Collections.unmodifiableMap(hopperBoats);
    JUKEBOX_BOAT_ITEMS = Collections.unmodifiableMap(jukeboxBoats);
  }

  private KayakItems() {}

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

}
