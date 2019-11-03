/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.stat.KayakStats;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientDummyContainerProvider;
import net.minecraft.container.ContainerType;
import net.minecraft.container.GenericContainer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.world.World;

public class ChestBoatEntity extends InventoryCarrierBoatEntity {

  public ChestBoatEntity(EntityType<? extends BoatEntity> entityType_1, World world_1) {
    super(entityType_1, world_1, new BasicInventory(27));
  }

  @Override
  protected void openInventory(PlayerEntity player) {
    player.openContainer(new ClientDummyContainerProvider((syncId, inv, owner) ->
        new GenericContainer(ContainerType.GENERIC_9X3, syncId, inv, this, 3), getName()));
    player.increaseStat(KayakStats.CHEST_CARRIER_BOAT_INTERACTION, 1);
  }

  @Override
  public BlockState getCarriedState() {
    return Blocks.CHEST.getDefaultState();
  }
}
