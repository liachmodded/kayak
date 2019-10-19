/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.Kayak;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public final class KayakEntities {

  public static final EntityType<ChestCarrierBoatEntity> CHEST_CARRIER_BOAT = register("chest_carrier_boat",
      FabricEntityTypeBuilder.create(EntityCategory.MISC, ChestCarrierBoatEntity::new)
          .size(EntityDimensions.fixed(1.375F, 0.5625F))
          .trackable(80, 3)
          .build()
  );
  public static final EntityType<ArbitraryBlockCarrierBoatEntity> BLOCK_CARRIER_BOAT = register("block_carrier_boat",
      FabricEntityTypeBuilder.create(EntityCategory.MISC, ArbitraryBlockCarrierBoatEntity::new)
          .size(EntityDimensions.fixed(1.375F, 0.5625F))
          .trackable(80, 3)
          .build()
  );
  public static final EntityType<FurnaceCarrierBoatEntity> FURNACE_CARRIER_BOAT = register("furnace_carrier_boat",
      FabricEntityTypeBuilder.create(EntityCategory.MISC, FurnaceCarrierBoatEntity::new)
          .size(EntityDimensions.fixed(1.375F, 0.5625F))
          .trackable(80, 3)
          .build()
  );

  private KayakEntities() {}

  static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
    return Registry.register(Registry.ENTITY_TYPE, Kayak.name(name), type);
  }

  public static void init() {
    // classloading
  }

}
