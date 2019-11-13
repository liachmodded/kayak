/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.mixin;

import com.github.liachmodded.kayak.entity.SleepableLivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements SleepableLivingEntity {

  @Shadow
  public abstract void resetStat(Stat<?> stat);

  public PlayerEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Override
  public void sleep() {
    this.resetStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
    super.sleep();
  }
}
