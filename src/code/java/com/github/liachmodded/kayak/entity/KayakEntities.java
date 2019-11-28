/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.Kayak;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public final class KayakEntities {

  public static final EntityType<ChestBoatEntity> CHEST_BOAT = register("chest_boat",
      FabricEntityTypeBuilder.create(EntityCategory.MISC, ChestBoatEntity::new)
          .size(EntityDimensions.fixed(1.375F, 0.5625F))
          .trackable(80, 3)
          .build()
  );
  public static final EntityType<HopperBoatEntity> HOPPER_BOAT = register("hopper_boat",
      FabricEntityTypeBuilder.create(EntityCategory.MISC, HopperBoatEntity::new)
          .size(EntityDimensions.fixed(1.375F, 0.5625F))
          .trackable(80, 3)
          .build()
  );
  public static final EntityType<CustomBlockBoatEntity> BLOCK_BOAT = register("block_boat",
      FabricEntityTypeBuilder.create(EntityCategory.MISC, CustomBlockBoatEntity::new)
          .size(EntityDimensions.fixed(1.375F, 0.5625F))
          .trackable(80, 3)
          .build()
  );
  public static final EntityType<FurnaceBoatEntity> FURNACE_BOAT = register("furnace_boat",
      FabricEntityTypeBuilder.create(EntityCategory.MISC, FurnaceBoatEntity::new)
          .size(EntityDimensions.fixed(1.375F, 0.5625F))
          .trackable(80, 3)
          .build()
  );
  public static final EntityType<BedBoatEntity> BED_BOAT = register("bed_boat",
      FabricEntityTypeBuilder.create(EntityCategory.MISC, BedBoatEntity::new)
          .size(EntityDimensions.fixed(1.375F, 0.5625F))
          .trackable(80, 3)
          .build()
  );
  public static final EntityType<JukeboxBoatEntity> JUKEBOX_BOAT = register("jukebox_boat",
      FabricEntityTypeBuilder.create(EntityCategory.MISC, JukeboxBoatEntity::new)
          .size(EntityDimensions.fixed(1.375F, 0.5625F))
          .trackable(80, 3)
          .build()
  );

  public static final Collection<EntityType<? extends CarrierBoatEntity>> CARRIER_BOATS = ImmutableList.of(
      CHEST_BOAT, HOPPER_BOAT, BLOCK_BOAT, FURNACE_BOAT, JUKEBOX_BOAT
  ); // bed boat is special case

  private KayakEntities() {}

  static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
    return Registry.register(Registry.ENTITY_TYPE, Kayak.name(name), type);
  }

  public static void init() {
    // classloading
  }

}
