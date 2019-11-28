/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.item.KayakItems;
import java.util.Optional;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CustomBlockBoatEntity extends CarrierBoatEntity {

  private static final BlockState AIR = Blocks.AIR.getDefaultState();
  private static final TrackedData<Optional<BlockState>> CARRIED_STATE = DataTracker
      .registerData(CustomBlockBoatEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_STATE);

  protected CustomBlockBoatEntity(EntityType<? extends BoatEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected ActionResult interactRear(PlayerEntity player, Hand hand) {
    // Nothing happens!
    return ActionResult.PASS;
  }

  @Override
  protected void initDataTracker() {
    super.initDataTracker();
    this.dataTracker.startTracking(CARRIED_STATE, Optional.of(AIR));
  }

  @Override
  public BlockState getCarriedState() {
    return dataTracker.get(CARRIED_STATE).orElse(AIR);
  }

  public void setCarriedState(BlockState state) {
    this.dataTracker.set(CARRIED_STATE, Optional.of(state));
  }

  @Override
  protected void writeCustomDataToTag(CompoundTag tag) {
    super.writeCustomDataToTag(tag);
    tag.put("carriedState", NbtHelper.fromBlockState(getCarriedState()));
  }

  @Override
  protected void readCustomDataFromTag(CompoundTag tag) {
    super.readCustomDataFromTag(tag);
    setCarriedState(NbtHelper.toBlockState(tag.getCompound("carriedState")));
  }

  @Override
  public Item asItem() {
    return KayakItems.getVanillaBoat(getBoatType());
  }
}
