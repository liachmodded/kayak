/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.client.sound;

import com.google.common.collect.MapMaker;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class EntitySpecificSoundManager {

  private final Map<Entity, StoppableEntityTrackingSoundInstance> specificSounds;

  public EntitySpecificSoundManager() {
    specificSounds = new MapMaker().weakKeys().weakValues().makeMap();
  }

  public void put(Entity entity, StoppableEntityTrackingSoundInstance sound) {
    StoppableEntityTrackingSoundInstance previous = specificSounds.put(entity, sound);
    if (previous != null) {
      previous.stop();
    }
    MinecraftClient.getInstance().getSoundManager().play(sound);
  }

  public void remove(Entity entity) {
    StoppableEntityTrackingSoundInstance previous = specificSounds.remove(entity);
    if (previous != null) {
      previous.stop();
    }
  }
  
  public @Nullable StoppableEntityTrackingSoundInstance get(Entity entity) {
    return specificSounds.get(entity);
  }
}
