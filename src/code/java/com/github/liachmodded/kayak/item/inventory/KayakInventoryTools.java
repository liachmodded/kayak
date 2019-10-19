/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.item.inventory;

import com.google.common.collect.AbstractIterator;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class KayakInventoryTools {

  public static ListTag writeBasicInventory(Inventory inventory) {
    ListTag ret = new ListTag();
    for (int i = 0; i < inventory.getInvSize(); i++) {
      ItemStack stack = inventory.getInvStack(i);
      if (!stack.isEmpty()) {
        CompoundTag tag = stack.toTag(new CompoundTag());
        tag.putByte("Slot", (byte) i);
        ret.add(tag);
      }
    }
    return ret;
  }

  public static void loadBasicInventory(Inventory inventory, @Nullable ListTag listTag) {
    inventory.clear();
    if (listTag == null || listTag.getElementType() != NbtType.COMPOUND) {
      return;
    }

    for (Tag element : listTag) {
      CompoundTag tag = (CompoundTag) element;
      if (!tag.contains("Slot", NbtType.NUMBER)) {
        continue;
      }
      ItemStack stack = ItemStack.fromTag(tag);
      if (stack.isEmpty()) {
        continue;
      }
      int slot = tag.getByte("Slot");
      inventory.setInvStack(slot, stack);
    }
  }

  public static Iterable<ItemStack> iterate(Inventory inventory) {
    return () -> new AbstractIterator<ItemStack>() {
      int nextAvailable = 0;
      
      @Override
      protected ItemStack computeNext() {
        while (true) {
          if (nextAvailable >= inventory.getInvSize())
            return endOfData();
          
          ItemStack stack = inventory.getInvStack(nextAvailable);
          nextAvailable++;
          if (!stack.isEmpty()){
            return stack;
          }
        }
      }
    };
  }

}
