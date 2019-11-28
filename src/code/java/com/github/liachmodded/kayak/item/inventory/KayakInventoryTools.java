/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.item.inventory;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.PacketByteBuf;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class KayakInventoryTools {

  public static final Map<DyeColor, Block> BEDS = ImmutableMap.<DyeColor, Block>builder()
      .put(DyeColor.WHITE, Blocks.WHITE_BED)
      .put(DyeColor.ORANGE, Blocks.ORANGE_BED)
      .put(DyeColor.MAGENTA, Blocks.MAGENTA_BED)
      .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_BED)
      .put(DyeColor.YELLOW, Blocks.YELLOW_BED)
      .put(DyeColor.LIME, Blocks.LIME_BED)
      .put(DyeColor.PINK, Blocks.PINK_BED)
      .put(DyeColor.GRAY, Blocks.GRAY_BED)
      .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_BED)
      .put(DyeColor.CYAN, Blocks.CYAN_BED)
      .put(DyeColor.PURPLE, Blocks.PURPLE_BED)
      .put(DyeColor.BLUE, Blocks.BLUE_BED)
      .put(DyeColor.BROWN, Blocks.BROWN_BED)
      .put(DyeColor.GREEN, Blocks.GREEN_BED)
      .put(DyeColor.RED, Blocks.RED_BED)
      .put(DyeColor.BLACK, Blocks.BLACK_BED)
      .build();

  public static ItemStack copyOne(ItemStack old) {
    return copy(old, 1);
  }

  public static ItemStack copy(ItemStack old, int size) {
    if (old.isEmpty()) {
      return ItemStack.EMPTY;
    }
    ItemStack ret = old.copy();
    ret.setCount(size);
    return ret;
  }

  public static ListTag toTag(Inventory inventory) {
    ListTag ret = new ListTag();
    toTags(ret, inventory);
    return ret;
  }

  public static void fromTag(Inventory inventory, @Nullable ListTag listTag) {
    inventory.clear();
    if (listTag == null || listTag.getElementType() != NbtType.COMPOUND) {
      return;
    }

    for (Tag element : listTag) {
      readSlot((CompoundTag) element, inventory);
    }
  }

  private static void toTags(Collection<? super CompoundTag> collection, Inventory inventory) {
    for (int i = 0; i < inventory.getInvSize(); i++) {
      ItemStack stack = inventory.getInvStack(i);
      if (!stack.isEmpty()) {
        CompoundTag tag = stack.toTag(new CompoundTag());
        tag.putByte("Slot", (byte) i);
        collection.add(tag);
      }
    }
  }

  private static void readSlot(@Nullable CompoundTag tag, Inventory inventory) {
    if (tag == null || !tag.contains("Slot", NbtType.NUMBER)) {
      return;
    }
    ItemStack stack = ItemStack.fromTag(tag);
    if (stack.isEmpty()) {
      return;
    }
    int slot = tag.getByte("Slot");
    inventory.setInvStack(slot, stack);
  }

  public static Iterable<ItemStack> iterate(Inventory inventory) {
    return () -> new AbstractIterator<ItemStack>() {
      int nextAvailable = 0;

      @Override
      protected ItemStack computeNext() {
        while (true) {
          if (nextAvailable >= inventory.getInvSize()) {
            return endOfData();
          }

          ItemStack stack = inventory.getInvStack(nextAvailable);
          nextAvailable++;
          if (!stack.isEmpty()) {
            return stack;
          }
        }
      }
    };
  }

  public static void toPacket(Inventory inventory, PacketByteBuf buf) {
    List<CompoundTag> tags = new ArrayList<>();
    toTags(tags, inventory);
    buf.writeVarInt(tags.size());
    for (CompoundTag each : tags) {
      buf.writeCompoundTag(each);
    }
  }

  public static void fromPacket(Inventory inventory, PacketByteBuf buf) {
    inventory.clear();
    int n = buf.readVarInt();
    for (int i = 0; i < n; i++) {
      readSlot(buf.readCompoundTag(), inventory);
    }
  }

}
