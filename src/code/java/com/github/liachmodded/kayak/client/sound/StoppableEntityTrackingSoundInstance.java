/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.client.sound;

import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public final class StoppableEntityTrackingSoundInstance extends EntityTrackingSoundInstance {

  public StoppableEntityTrackingSoundInstance(SoundEvent sound, SoundCategory soundCategory, Entity entity) {
    super(sound, soundCategory, entity);
  }

  public StoppableEntityTrackingSoundInstance(SoundEvent sound, SoundCategory soundCategory, float volume, float pitch, Entity entity) {
    super(sound, soundCategory, volume, pitch, entity);
  }
  
  public void stop() {
    this.done = true;
  }
}
