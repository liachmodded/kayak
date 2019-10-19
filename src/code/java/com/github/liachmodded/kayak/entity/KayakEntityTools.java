/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.entity;

import java.util.Random;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.IWorld;

public final class KayakEntityTools {

  private KayakEntityTools() {}

  public static double fuzz(double original, double margin, Random random) {
    return random.nextGaussian() * margin + original;
  }

  public static void fuzzParticle(IWorld world, ParticleEffect particleEffect, double x, double y, double z, double margin, double speedMargin,
      Random random) {
    double x1 = fuzz(x, margin, random);
    double y1 = fuzz(y, margin, random);
    double z1 = fuzz(z, margin, random);
    double dx1 = fuzz(0, speedMargin, random);
    double dy1 = fuzz(0, speedMargin, random);
    double dz1 = fuzz(0, speedMargin, random);
    world.addParticle(particleEffect, x1, y1, z1, dx1, dy1, dz1);
  }

}
