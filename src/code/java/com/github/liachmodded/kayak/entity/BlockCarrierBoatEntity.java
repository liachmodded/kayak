/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import com.github.liachmodded.kayak.item.KayakItems;
import com.github.liachmodded.kayak.item.inventory.KayakInventoryTools;
import com.github.liachmodded.kayak.mixin.BoatEntityAccess;
import com.github.liachmodded.kayak.stat.KayakStats;
import java.util.Optional;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientDummyContainerProvider;
import net.minecraft.container.ContainerType;
import net.minecraft.container.GenericContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class BlockCarrierBoatEntity extends BoatEntity implements PositionInitializerEntity, CustomDropBoat, CustomInventoryVehicle {

  private static final BlockState AIR = Blocks.AIR.getDefaultState();
  private static final TrackedData<Optional<BlockState>> CARRIED_STATE = DataTracker
      .registerData(BlockCarrierBoatEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_STATE);

  private BasicInventory inventory = new BasicInventory(27);

  public BlockCarrierBoatEntity(EntityType<? extends BoatEntity> entityType_1, World world_1) {
    super(entityType_1, world_1);
  }

  @Override
  public void init(double x, double y, double z) {
    this.setPosition(x, y, z);
    this.setVelocity(Vec3d.ZERO);
    this.prevX = x;
    this.prevY = y;
    this.prevZ = z;
  }

  @Override
  protected void initDataTracker() {
    super.initDataTracker();
    this.dataTracker.startTracking(CARRIED_STATE, Optional.of(AIR));
  }

  public BlockState getCarriedState() {
    return dataTracker.get(CARRIED_STATE).orElse(AIR);
  }

  public void setCarriedState(BlockState state) {
    this.dataTracker.set(CARRIED_STATE, Optional.of(state));
  }

  @Override
  public boolean interact(PlayerEntity playerEntity_1, Hand hand_1) {
    return false; // Moved to hitPos aware version
  }

  @Override
  public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
    // test negative dot product
    Vec3d facing = getRotationVector(0f, this.yaw);
    boolean back = hitPos.dotProduct(facing) < 0;
    //player.sendMessage(new LiteralText(
    //String.format("Hit boat at %f %f %f, current yaw %f %f %f, is back: %s!", hitPos.x, hitPos.y, hitPos.z, facing.x, facing.y, facing.z, back)));
    if (!back) {
      if (super.interact(player, hand)) {
        return ActionResult.SUCCESS;
      }
      return super.interactAt(player, hitPos, hand);
    }

    if (!world.isClient) {
      openInventory(player);
      return ActionResult.SUCCESS;
    }
    return super.interactAt(player, hitPos, hand);
  }

  @Override
  public void openCustomInventory(ServerPlayerEntity player) {
    openInventory(player);
  }

  private void openInventory(PlayerEntity player) {
    player.openContainer(new ClientDummyContainerProvider((syncId, inv, owner) ->
        new GenericContainer(ContainerType.GENERIC_9X3, syncId, inv, inventory, 3), getName()));
    player.increaseStat(KayakStats.CARRIER_BOAT_INTERACTION, 1);
  }

  @Override
  public void updatePassengerPosition(Entity entity_1) {
    if (this.hasPassenger(entity_1)) {
      float float_2 = (float) ((this.removed ? 0.009999999776482582D : this.getMountedHeightOffset()) + entity_1.getHeightOffset());

      Vec3d vec3d_1 = (new Vec3d(0.2F, 0.0D, 0.0D)).rotateY(-this.yaw * 0.017453292F - 1.5707964F);
      entity_1.setPosition(this.getX() + vec3d_1.x, this.getY() + (double) float_2, this.getZ() + vec3d_1.z);
      entity_1.yaw += ((BoatEntityAccess) this).getYawVelocity();
      entity_1.setHeadYaw(entity_1.getHeadYaw() + ((BoatEntityAccess) this).getYawVelocity());
      this.copyEntityData(entity_1);
    }
  }

  @Override
  public Item asItem() {
    return KayakItems.BOAT_ITEMS.get(getBoatType());
  }

  @Override
  protected boolean canAddPassenger(Entity entity_1) {
    return this.getPassengerList().size() < 1;
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
  protected void writeCustomDataToTag(CompoundTag tag) {
    super.writeCustomDataToTag(tag);
    tag.put("carriedState", NbtHelper.fromBlockState(getCarriedState()));
    tag.put("Inventory", KayakInventoryTools.writeBasicInventory(inventory));
  }

  @Override
  protected void readCustomDataFromTag(CompoundTag tag) {
    super.readCustomDataFromTag(tag);
    setCarriedState(NbtHelper.toBlockState(tag.getCompound("carriedState")));
    KayakInventoryTools.loadBasicInventory(inventory, tag.getList("Inventory", NbtType.COMPOUND));
  }
}
