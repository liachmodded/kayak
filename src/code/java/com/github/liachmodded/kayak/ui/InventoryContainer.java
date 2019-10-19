/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.ui;

import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public abstract class InventoryContainer extends Container {

  private final Inventory inventory;
  private final PlayerInventory playerInventory;

  protected InventoryContainer(int syncId, PlayerInventory playerInventory, Inventory inventory) {
    super(ContainerType.ANVIL, syncId);
    this.inventory = inventory;
    this.playerInventory = playerInventory;
    inventory.onInvOpen(playerInventory.player);
  }

  public PlayerInventory getPlayerInventory() {
    return playerInventory;
  }

  protected void addSlots(Inventory inventory, PlayerInventory playerInventory) {
    int int_6;
    int int_5;
    for (int_6 = 0; int_6 < 3; ++int_6) {
      for (int_5 = 0; int_5 < 3; ++int_5) {
        this.addSlot(new Slot(inventory, int_5 + int_6 * 3, 62 + int_5 * 18, 17 + int_6 * 18));
      }
    }

    for (int_6 = 0; int_6 < 3; ++int_6) {
      for (int_5 = 0; int_5 < 9; ++int_5) {
        this.addSlot(new Slot(playerInventory, int_5 + int_6 * 9 + 9, 8 + int_5 * 18, 84 + int_6 * 18));
      }
    }

    for (int_6 = 0; int_6 < 9; ++int_6) {
      this.addSlot(new Slot(playerInventory, int_6, 8 + int_6 * 18, 142));
    }
  }

  protected abstract int getSize();

  @Override
  public boolean canUse(PlayerEntity playerEntity_1) {
    return this.inventory.canPlayerUseInv(playerEntity_1);
  }

  @Override
  public ItemStack transferSlot(PlayerEntity playerEntity_1, int int_1) {
    ItemStack itemStack_1 = ItemStack.EMPTY;
    Slot slot_1 = this.slotList.get(int_1);
    if (slot_1 != null && slot_1.hasStack()) {
      ItemStack itemStack_2 = slot_1.getStack();
      itemStack_1 = itemStack_2.copy();
      if (int_1 < 9) {
        if (!this.insertItem(itemStack_2, getSize(), getSize() + 36, true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.insertItem(itemStack_2, 0, getSize(), false)) {
        return ItemStack.EMPTY;
      }

      if (itemStack_2.isEmpty()) {
        slot_1.setStack(ItemStack.EMPTY);
      } else {
        slot_1.markDirty();
      }

      if (itemStack_2.getCount() == itemStack_1.getCount()) {
        return ItemStack.EMPTY;
      }

      slot_1.onTakeItem(playerEntity_1, itemStack_2);
    }

    return itemStack_1;
  }

  @Override
  public void close(PlayerEntity playerEntity_1) {
    super.close(playerEntity_1);
    this.inventory.onInvClose(playerEntity_1);
  }
}
