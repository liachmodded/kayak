/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.liachmodded.kayak.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.advancement.Advancement;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class KayakAdvancementProvider implements DataProvider {

  private static final Logger LOGGER = LogManager.getLogger();
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private final DataGenerator generator;
  private final List<Consumer<Consumer<Advancement>>> tabGenerators = ImmutableList.of(new KayakAdvancementTab());

  public KayakAdvancementProvider(DataGenerator generator) {
    this.generator = generator;
  }

  private static Path getOutput(Path path_1, Advancement advancement_1) {
    return path_1.resolve("data/" + advancement_1.getId().getNamespace() + "/advancements/" + advancement_1.getId().getPath() + ".json");
  }

  @Override
  public void run(DataCache dataCache_1) {
    Path root = this.generator.getOutput();
    Set<Identifier> written = Sets.newHashSet();
    Consumer<Advancement> writer = advancement -> {
      if (!written.add(advancement.getId())) {
        throw new IllegalStateException("Duplicate advancement " + advancement.getId());
      }
      Path output = getOutput(root, advancement);

      try {
        DataProvider.writeToPath(GSON, dataCache_1, advancement.createTask().toJson(), output);
      } catch (IOException ex) {
        LOGGER.error("Couldn't save advancement {}", output, ex);
      }
    };
    for (Consumer<Consumer<Advancement>> generator : this.tabGenerators) {
      generator.accept(writer);
    }
  }

  @Override
  public String getName() {
    return "Kayak Advancements";
  }
}
