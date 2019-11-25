/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.stat.KayakStats;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.client.network.ClientDummyContainerProvider;
import net.minecraft.container.HopperContainer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.world.World;

public class HopperBoatEntity extends InventoryCarrierBoatEntity implements Hopper {

  public HopperBoatEntity(EntityType<? extends BoatEntity> entityType_1, World world_1) {
    super(entityType_1, world_1, new BasicInventory(5));
  }

  @Override
  protected void openInventory(PlayerEntity player) {
    player.openContainer(new ClientDummyContainerProvider((syncId, inv, owner) ->
        new HopperContainer(syncId, inv, inventory), getName()));
    player.increaseStat(KayakStats.HOPPER_BOAT_INTERACTION, 1);
  }

  @Override
  public BlockState getCarriedState() {
    return Blocks.HOPPER.getDefaultState();
  }

  @Override
  public int getBlockOffset() {
    return 0;
  }

  @Override
  public World getWorld() {
    return world;
  }

  @Override
  public double getHopperX() {
    return getX();
  }

  @Override
  public double getHopperY() {
    return getY();
  }

  @Override
  public double getHopperZ() {
    return getZ();
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.world.isClient && this.isAlive()) {
      this.suck();
    }
  }

  protected void suck() {
    if (HopperBlockEntity.extract(this)) {
      return;
    }
    List<ItemEntity> drops = this.world.getEntities(ItemEntity.class, this.getBoundingBox().expand(0.25D, 0.1D, 0.25D),
        EntityPredicates.VALID_ENTITY);
    if (!drops.isEmpty()) {
      HopperBlockEntity.extract(this, drops.get(0));
    }
  }
}
