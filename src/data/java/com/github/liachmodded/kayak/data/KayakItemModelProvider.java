/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.data;

import com.github.liachmodded.kayak.item.KayakItems;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class KayakItemModelProvider implements DataProvider {

  private static final Logger LOGGER = LogManager.getLogger();
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private final DataGenerator root;
  private static final Identifier MINECRAFT_GENERATED = new Identifier("minecraft", "item/generated");

  public KayakItemModelProvider(DataGenerator generator) {
    this.root = generator;
  }

  @Override
  public void run(DataCache cache) {
    Path path = this.root.getOutput();

    Collection<Item> targets = new ArrayList<>();
    targets.addAll(KayakItems.CHEST_BOAT_ITEMS.values());
    targets.addAll(KayakItems.HOPPER_BOAT_ITEMS.values());

    // add here

    for (Item item : targets) {
      Identifier itemId = Registry.ITEM.getId(item);
      writeJsonTo(cache, itemId, path);
    }
  }

  private void writeJsonTo(DataCache cache, Identifier itemId, Path root) {
    writeJson(cache, makeSimpleGeneratedModel(itemId),
        root.resolve("assets/" + itemId.getNamespace() + "/models/item/" + itemId.getPath() + ".json"));
  }

  private JsonObject makeSimpleGeneratedModel(Identifier itemId) {
    JsonObject ret = new JsonObject();
    ret.addProperty("parent", MINECRAFT_GENERATED.toString());
    JsonObject textures = new JsonObject();
    textures.addProperty("layer0", new Identifier(itemId.getNamespace(), "item/" + itemId.getPath()).toString());
    ret.add("textures", textures);
    return ret;
  }

  private void writeJson(DataCache cache, JsonObject json, Path path) {
    try {
      String string_1 = GSON.toJson(json);
      String string_2 = SHA1.hashUnencodedChars(string_1).toString();
      if (!Objects.equals(cache.getOldSha1(path), string_2) || !Files.exists(path)) {
        Files.createDirectories(path.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
          writer.write(string_1);
        }
      }

      cache.updateSha1(path, string_2);
    } catch (IOException var19) {
      LOGGER.error("Couldn't save json file {}", path, var19);
    }
  }

  @Override
  public String getName() {
    return "Kayak Item Model provider";
  }
}
