/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.item.KayakItems;
import com.github.liachmodded.kayak.stat.KayakStats;
import com.github.liachmodded.kayak.ui.FurnaceBoatScreenHandler;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class FurnaceBoatEntity extends InventoryCarrierBoatEntity implements PitchableBoat {

  public static final int FUEL_CONSUMPTION_THRESHOLD = 3000;
  private static final BlockState FURNACE = Blocks.FURNACE.getDefaultState().with(AbstractFurnaceBlock.FACING, Direction.SOUTH);
  private static final BlockState LIT_FURNACE = FURNACE.with(AbstractFurnaceBlock.LIT, true);
  private static final TrackedData<Integer> FUEL = DataTracker.registerData(FurnaceBoatEntity.class, TrackedDataHandlerRegistry.INTEGER);
  private static final TrackedData<Integer> INCLINATION = DataTracker
      .registerData(FurnaceBoatEntity.class, TrackedDataHandlerRegistry.INTEGER);
  private int lastInclination;

  public FurnaceBoatEntity(EntityType<? extends BoatEntity> type, World world) {
    super(type, world, new BasicInventory(1));
  }

  @Override
  protected void initDataTracker() {
    super.initDataTracker();
    dataTracker.startTracking(FUEL, 0);
    dataTracker.startTracking(INCLINATION, 0);
  }

  @Override
  public void tick() {
    if (getFuel() > 0) {
      setFuel(getFuel() - 1);
    } else {
      setFuel(0);
    }

    int diff = getFuel() - getInclination();
    if (diff > 0) {
      setInclination(getInclination() + Math.max(1, (int) (diff * 0.02f)));
    } else if (diff < 0) {
      setInclination(getInclination() + Math.min(-1, (int) (diff * 0.02f)));
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

    // TODO more cool stuff with fuel

    if (getFuel() > 0 && random.nextInt(5) == 0) {
      KayakEntityTools.fuzzParticle(world, ParticleTypes.SMOKE, getX(), getY(), getZ(), 0.3, 0.08, random);
      world.playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
    }

    lastInclination = getInclination();
  }

  public int getFuel() {
    return dataTracker.get(FUEL);
  }

  public void setFuel(int value) {
    dataTracker.set(FUEL, value);
  }

  public int getInclination() {
    return dataTracker.get(INCLINATION);
  }

  public void setInclination(int value) {
    dataTracker.set(INCLINATION, value);
  }

  @Override
  public float getRenderPitch(float tickDelta) {
    return 5f * MathHelper.clamp(MathHelper.lerp(tickDelta, lastInclination, getInclination()), 0, FUEL_CONSUMPTION_THRESHOLD)
        / FUEL_CONSUMPTION_THRESHOLD;
  }

  @Override
  protected void openInventory(PlayerEntity player) {
    FurnaceBoatScreenHandler.open(player, this);
    player.increaseStat(KayakStats.FURNACE_BOAT_INTERACTION, 1);
  }

  @Override
  public BlockState getCarriedState() {
    return getFuel() > 0 ? LIT_FURNACE : FURNACE;
  }

  @Override
  protected void writeCustomDataToTag(CompoundTag tag) {
    super.writeCustomDataToTag(tag);
    tag.putInt("Fuel", getFuel());
    tag.putInt("Inclination", getInclination());
  }

  @Override
  protected void readCustomDataFromTag(CompoundTag tag) {
    super.readCustomDataFromTag(tag);
    setFuel(tag.getInt("Fuel"));
    setInclination(tag.getInt("Inclination"));
  }

  @Override
  public Item asItem() {
    return KayakItems.getVanillaBoat(getBoatType());
  }
}
