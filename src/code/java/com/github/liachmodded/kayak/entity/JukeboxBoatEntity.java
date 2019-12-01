/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.client.KayakClient;
import com.github.liachmodded.kayak.client.sound.EntitySpecificSoundManager;
import com.github.liachmodded.kayak.client.sound.StoppableEntityTrackingSoundInstance;
import com.github.liachmodded.kayak.item.KayakItems;
import com.github.liachmodded.kayak.item.inventory.KayakInventoryTools;
import com.github.liachmodded.kayak.stat.KayakStats;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Clearable;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class JukeboxBoatEntity extends CarrierBoatEntity implements Clearable {

  protected static final TrackedData<ItemStack> MUSIC_DISC = DataTracker.registerData(JukeboxBoatEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

  protected JukeboxBoatEntity(EntityType<? extends BoatEntity> type, World world) {
    super(type, world);
  }

  @Override
  protected void initDataTracker() {
    super.initDataTracker();
    dataTracker.startTracking(MUSIC_DISC, ItemStack.EMPTY);
  }

  @Override
  public void onTrackedDataSet(TrackedData<?> data) {
    super.onTrackedDataSet(data);
    if (world.isClient && data.equals(MUSIC_DISC)) {
      updateMusic();
    }
  }

  private void updateMusic() {
    EntitySpecificSoundManager sounds = KayakClient.getInstance().getJukeboxBoatSounds();

    ItemStack disc = getMusicDisc();
    if (disc.isEmpty()) {
      sounds.remove(this);
      return;
    }

    MusicDiscItem musicDisc = (MusicDiscItem) disc.getItem();
    StoppableEntityTrackingSoundInstance newSound = new StoppableEntityTrackingSoundInstance(musicDisc.getSound(),
        SoundCategory.RECORDS, this);
    MinecraftClient.getInstance().inGameHud.setRecordPlayingOverlay(musicDisc.getDescription().asFormattedString());
    sounds.put(this, newSound);
  }

  @Override
  protected ActionResult interactRear(PlayerEntity player, Hand hand) {
    ItemStack held = player.getStackInHand(hand);
    boolean hadRecord = !getMusicDisc().isEmpty();
    boolean heldMusicDisc = held.getItem() instanceof MusicDiscItem;
    if (world.isClient) {
      return hadRecord || heldMusicDisc ? ActionResult.SUCCESS : ActionResult.PASS;
    }

    if (hadRecord) {
      if (player.abilities.creativeMode && heldMusicDisc) {
        clear();
      } else {
        popRecord();
      }
    }

    if (heldMusicDisc) {
      setMusicDisc(KayakInventoryTools.copyOne(held));
      if (!player.abilities.creativeMode) {
        player.getStackInHand(hand).decrement(1);
      }
    }

    if (hadRecord || heldMusicDisc) {
      player.increaseStat(KayakStats.JUKEBOX_BOAT_INTERACTION, 1);
      return ActionResult.SUCCESS;
    }
    return ActionResult.PASS;
  }

  private void popRecord() {
    ItemStack record = getMusicDisc();
    clear();
    this.dropStack(record, 0.7f); // nvm too lazy to make 
  }

  @Override
  public BlockState getCarriedState() {
    return Blocks.JUKEBOX.getDefaultState();
  }

  @Override
  public void clear() {
    setMusicDisc(ItemStack.EMPTY);
  }

  public void setMusicDisc(ItemStack record) {
    dataTracker.set(MUSIC_DISC, record);
  }

  public ItemStack getMusicDisc() {
    return dataTracker.get(MUSIC_DISC);
  }

  @Override
  protected void writeCustomDataToTag(CompoundTag tag) {
    super.writeCustomDataToTag(tag);
    if (!getMusicDisc().isEmpty()) {
      tag.put("RecordItem", getMusicDisc().toTag(new CompoundTag()));
    }
  }

  @Override
  protected void readCustomDataFromTag(CompoundTag tag) {
    super.readCustomDataFromTag(tag);
    if (tag.contains("RecordItem", NbtType.COMPOUND)) {
      setMusicDisc(ItemStack.fromTag(tag.getCompound("RecordItem")));
    }
  }

  @Override
  public Item asItem() {
    return KayakItems.JUKEBOX_BOAT_ITEMS.get(getBoatType());
  }
}
