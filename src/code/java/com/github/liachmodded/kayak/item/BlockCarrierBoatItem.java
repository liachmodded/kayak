/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.item;

import com.github.liachmodded.kayak.entity.BlockCarrierBoatEntity;
import com.github.liachmodded.kayak.entity.KayakEntities;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public class BlockCarrierBoatItem extends Item {

  private static final Predicate<Entity> COLLISION_CHECK;
  private final BoatEntity.Type type;

  public BlockCarrierBoatItem(BoatEntity.Type type, Settings settings) {
    super(settings);
    this.type = type;
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getStackInHand(hand);
    HitResult hitResult = rayTrace(world, player, RayTraceContext.FluidHandling.ANY);
    if (hitResult.getType() == HitResult.Type.MISS) {
      return TypedActionResult.pass(stack);
    } else {
      Vec3d facing = player.getRotationVec(1.0F);
      List<Entity> collisions = world.getEntities(player, player.getBoundingBox().stretch(facing.multiply(5.0D)).expand(1.0D),
          COLLISION_CHECK);
      if (!collisions.isEmpty()) {
        Vec3d camera = player.getCameraPosVec(1.0F);
        for (Entity entity : collisions) {
          Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
          if (box.contains(camera)) {
            return TypedActionResult.pass(stack);
          }
        }
      }

      if (hitResult.getType() == HitResult.Type.BLOCK) {
        BlockCarrierBoatEntity boat = new BlockCarrierBoatEntity(KayakEntities.BLOCK_CARRIER_BOAT, world);
        boat.init(hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
        boat.setBoatType(this.type);
        boat.setCarriedState(Blocks.CHEST.getDefaultState());
        boat.yaw = player.yaw;
        if (!world.doesNotCollide(boat, boat.getBoundingBox().expand(-0.1D))) {
          return TypedActionResult.fail(stack);
        } else {
          if (!world.isClient) {
            world.spawnEntity(boat);
            if (!player.abilities.creativeMode) {
              stack.decrement(1);
            }
          }

          player.incrementStat(Stats.USED.getOrCreateStat(this));
          return TypedActionResult.successWithSwing(stack);
        }
      } else {
        return TypedActionResult.pass(stack);
      }
    }
  }

  static {
    COLLISION_CHECK = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::collides);
  }
}
