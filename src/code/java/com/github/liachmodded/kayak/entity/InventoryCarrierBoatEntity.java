/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.item.inventory.ForwardingInventory;
import com.github.liachmodded.kayak.item.inventory.KayakInventoryTools;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public abstract class InventoryCarrierBoatEntity extends CarrierBoatEntity implements CustomDropBoat, CustomInventoryVehicle,
    ForwardingInventory {

  protected final Inventory inventory;

  protected InventoryCarrierBoatEntity(EntityType<? extends BoatEntity> type, World world, Inventory inventory) {
    super(type, world);
    this.inventory = inventory;
  }

  @Override
  public void dropCustom(DamageSource source) {
    if (!world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
      return;
    }
    for (ItemStack stack : KayakInventoryTools.iterate(inventory)) {
      dropStack(stack);
    }
  }

  @Override
  protected ActionResult interactRear(PlayerEntity player, Hand hand) {
    if (!world.isClient) {
      openInventory(player);
    }
    return ActionResult.SUCCESS;
  }

  @Override
  public void openCustomInventory(ServerPlayerEntity player) {
    openInventory(player);
  }

  protected abstract void openInventory(PlayerEntity player);

  @Override
  public abstract BlockState getCarriedState();

  @Override
  public Inventory getBackingInventory() {
    return inventory;
  }

  @Override
  public boolean canPlayerUseInv(PlayerEntity var1) {
    return true;
  }

  @Override
  protected void writeCustomDataToTag(CompoundTag tag) {
    super.writeCustomDataToTag(tag);
    tag.put("Inventory", KayakInventoryTools.toTag(inventory));
  }

  @Override
  protected void readCustomDataFromTag(CompoundTag tag) {
    super.readCustomDataFromTag(tag);
    KayakInventoryTools.fromTag(inventory, tag.getList("Inventory", NbtType.COMPOUND));
  }
}
