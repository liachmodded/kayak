/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.stat.KayakStats;
import com.github.liachmodded.kayak.ui.FurnaceBoatContainer;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class FurnaceCarrierBoatEntity extends InventoryCarrierBoatEntity implements PitchableBoat {

  public static final int FUEL_CONSUMPTION_THRESHOLD = 300;
  private static final BlockState FURNACE = Blocks.FURNACE.getDefaultState().with(AbstractFurnaceBlock.FACING, Direction.SOUTH);
  private static final BlockState LIT_FURNACE = FURNACE.with(AbstractFurnaceBlock.LIT, true);
  private static final TrackedData<Integer> FUEL = DataTracker.registerData(FurnaceCarrierBoatEntity.class, TrackedDataHandlerRegistry.INTEGER);
  private int lastFuel;

  public FurnaceCarrierBoatEntity(EntityType<? extends BoatEntity> entityType_1, World world_1) {
    super(entityType_1, world_1, new BasicInventory(1));
  }

  @Override
  protected void initDataTracker() {
    super.initDataTracker();
    dataTracker.startTracking(FUEL, 0);
  }

  @Override
  public void tick() {
    if (getFuel() > 0) {
      setFuel(getFuel() - 1);
    } else {
      setFuel(0);
    }
    while (getFuel() <= FUEL_CONSUMPTION_THRESHOLD) {
      ItemStack fuel = inventory.getInvStack(0);
      if (fuel.isEmpty()) {
        break;
      }
      Integer value = AbstractFurnaceBlockEntity.createFuelTimeMap().get(fuel.getItem());
      if (value == null || value <= 0) {
        break;
      }
      fuel.decrement(1);
      setFuel(getFuel() + value);
    }

    super.tick();

    if (getFuel() > 0 && random.nextInt(7) == 0) {
      KayakEntityTools.fuzzParticle(world, ParticleTypes.SMOKE, getX(), getY(), getZ(), 0.3, 0.4, random);
    }

    lastFuel = getFuel();
  }

  public int getFuel() {
    return dataTracker.get(FUEL);
  }

  public void setFuel(int value) {
    dataTracker.set(FUEL, value);
  }

  @Override
  public float getRenderPitch(float partialTicks) {
    return 3f * MathHelper.lerp(partialTicks, lastFuel, getFuel()) / FUEL_CONSUMPTION_THRESHOLD;
  }

  @Override
  protected void openInventory(PlayerEntity player) {
    FurnaceBoatContainer.open(player, this, inventory);
    player.increaseStat(KayakStats.FURNACE_CARRIER_BOAT_INTERACTION, 1);
  }

  @Override
  public BlockState getCarriedState() {
    return getFuel() > 0 ? LIT_FURNACE : FURNACE;
  }
}
