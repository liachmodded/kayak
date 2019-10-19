/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.ui;

import com.github.liachmodded.kayak.Kayak;
import com.github.liachmodded.kayak.entity.FurnaceCarrierBoatEntity;
import com.github.liachmodded.kayak.item.inventory.KayakInventoryTools;
import com.github.liachmodded.kayak.ui.property.KayakPropertyFactory;
import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.container.Slot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.checkerframework.checker.nullness.qual.Nullable;

public class FurnaceBoatContainer extends InventoryContainer {

  private final FurnaceCarrierBoatEntity boat;

  protected FurnaceBoatContainer(int syncId, PlayerInventory playerInventory, Inventory inventory, FurnaceCarrierBoatEntity boat) {
    super(syncId, playerInventory, inventory);
    this.boat = Preconditions.checkNotNull(boat);

    addSlots(inventory, playerInventory);
  }

  public static void open(PlayerEntity player, FurnaceCarrierBoatEntity boat, Inventory inventory) {
    ContainerProviderRegistry.INSTANCE.openContainer(KayakContainerProviders.FURNACE_BOAT, player, buf -> {
      buf.writeVarInt(boat.getEntityId());
      KayakInventoryTools.toPacket(inventory, buf);
    });
  }

  public static @Nullable FurnaceBoatContainer fromPacket(int syncId, Identifier guiId, PlayerEntity player, PacketByteBuf buf) {
    int entityId = buf.readVarInt();
    Entity entity = player.world.getEntityById(entityId);
    if (!(entity instanceof FurnaceCarrierBoatEntity)) {
      Kayak.LOGGER.warn("Invalid entity network id {} received!", entityId);
      return null;
    }
    Inventory inventory = new BasicInventory(1);
    KayakInventoryTools.fromPacket(inventory, buf);
    return new FurnaceBoatContainer(syncId, player.inventory, inventory, (FurnaceCarrierBoatEntity) entity);
  }

  public FurnaceCarrierBoatEntity getBoat() {
    return boat;
  }

  public int getFuelProgress() {
    return boat.getFuel() * 13 / FurnaceCarrierBoatEntity.FUEL_CONSUMPTION_THRESHOLD;
  }

  @Override
  protected int getSize() {
    return 1;
  }

  @Override
  protected void addSlots(Inventory inventory_1, PlayerInventory playerInventory_1) {
    this.addSlot(new Slot(inventory_1, 0, 56, 53) {
      @Override
      public boolean canInsert(ItemStack itemStack_1) {
        return AbstractFurnaceBlockEntity.canUseAsFuel(itemStack_1);
      }
    });

    int int_4;
    for (int_4 = 0; int_4 < 3; ++int_4) {
      for (int int_3 = 0; int_3 < 9; ++int_3) {
        this.addSlot(new Slot(playerInventory_1, int_3 + int_4 * 9 + 9, 8 + int_3 * 18, 84 + int_4 * 18));
      }
    }

    for (int_4 = 0; int_4 < 9; ++int_4) {
      this.addSlot(new Slot(playerInventory_1, int_4, 8 + int_4 * 18, 142));
    }

    addProperty(KayakPropertyFactory.of(boat::getFuel));
  }
}
