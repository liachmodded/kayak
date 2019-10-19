/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.item.inventory;

import java.util.Set;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface ForwardingInventory extends Inventory {

  Inventory getBackingInventory();

  @Override
  default int getInvSize() {
    return getBackingInventory().getInvSize();
  }

  @Override
  default boolean isInvEmpty() {
    return getBackingInventory().isInvEmpty();
  }

  @Override
  default ItemStack getInvStack(int var1) {
    return getBackingInventory().getInvStack(var1);
  }

  @Override
  default ItemStack takeInvStack(int var1, int var2) {
    return getBackingInventory().takeInvStack(var1, var2);
  }

  @Override
  default ItemStack removeInvStack(int var1) {
    return getBackingInventory().removeInvStack(var1);
  }

  @Override
  default void setInvStack(int var1, ItemStack var2) {
    getBackingInventory().setInvStack(var1, var2);
  }

  @Override
  default void markDirty() {
    getBackingInventory().markDirty();
  }

  @Override
  default boolean canPlayerUseInv(PlayerEntity var1) {
    return getBackingInventory().canPlayerUseInv(var1);
  }

  @Override
  default void clear() {
    getBackingInventory().clear();
  }

  @Override
  default int getInvMaxStackAmount() {
    return getBackingInventory().getInvMaxStackAmount();
  }

  @Override
  default void onInvOpen(PlayerEntity playerEntity_1) {
    getBackingInventory().onInvOpen(playerEntity_1);
  }

  @Override
  default void onInvClose(PlayerEntity playerEntity_1) {
    getBackingInventory().onInvClose(playerEntity_1);
  }

  @Override
  default boolean isValidInvStack(int int_1, ItemStack itemStack_1) {
    return getBackingInventory().isValidInvStack(int_1, itemStack_1);
  }

  @Override
  default int countInInv(Item item_1) {
    return getBackingInventory().countInInv(item_1);
  }

  @Override
  default boolean containsAnyInInv(Set<Item> set_1) {
    return getBackingInventory().containsAnyInInv(set_1);
  }
}
