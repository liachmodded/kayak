/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.data;

import com.github.liachmodded.kayak.item.KayakItems;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.client.BlockStateDefinitionProvider;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.Model;
import net.minecraft.data.client.model.ModelIds;
import net.minecraft.data.client.model.Models;
import net.minecraft.data.client.model.Texture;
import net.minecraft.data.client.model.TextureKey;
import net.minecraft.entity.vehicle.BoatEntity;
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

    Map<Identifier, Supplier<JsonElement>> models = Maps.newHashMap();
    BiConsumer<Identifier, Supplier<JsonElement>> modelCollector = (identifier, supplier) -> {
      Supplier<JsonElement> supplier2 = models.put(identifier, supplier);
      if (supplier2 != null) {
        throw new IllegalStateException("Duplicate model definition for " + identifier);
      }
    };

    Collection<Item> simpleItems = new ArrayList<>();
    Model model = Models.GENERATED;

    for (Item item : simpleItems) {
      model.upload(ModelIds.getItemModelId(item), Texture.layer0(item), modelCollector);
    }
    
    // TODO cannot write typed boat json as vanilla texture gen does not support layer1 key
//    Collection<Map<BoatEntity.Type, Item>> boatItemGroups = new ArrayList<>();
//    for (Map<BoatEntity.Type, Item> map : boatItemGroups) {
//      for (Map.Entry<BoatEntity.Type, Item> entry : map.entrySet()) {
//        Item item = entry.getValue();
//        model.upload(ModelIds.getItemModelId(item), Texture.layer0(KayakItems.getVanillaBoat(entry.getKey())).put(TextureKey.LAYER1))
//        writeTypedBoatJsonTo(cache, entry.getKey(), "chest_boat", itemId, path);
//      }
//    }

    for (Map.Entry<BoatEntity.Type, Item> entry : KayakItems.CHEST_BOAT_ITEMS.entrySet()) {
      Identifier itemId = Registry.ITEM.getId(entry.getValue());
      writeTypedBoatJsonTo(cache, entry.getKey(), "chest_boat", itemId, path);
    }

    for (Map.Entry<BoatEntity.Type, Item> entry : KayakItems.HOPPER_BOAT_ITEMS.entrySet()) {
      Identifier itemId = Registry.ITEM.getId(entry.getValue());
      writeTypedBoatJsonTo(cache, entry.getKey(), "hopper_boat", itemId, path);
    }
    
    for (Map.Entry<BoatEntity.Type, Item> entry : KayakItems.JUKEBOX_BOAT_ITEMS.entrySet()) {
      Identifier itemId = Registry.ITEM.getId(entry.getValue());
      writeTypedBoatJsonTo(cache, entry.getKey(), "jukebox_boat", itemId, path);
    }

    this.writeJsons(cache, path, models, KayakItemModelProvider::getModelJsonPath);
  }

  private void writeSimpleJsonTo(DataCache cache, Identifier itemId, Path root) {
    writeJson(cache, makeSimpleGeneratedModel(itemId),
        root.resolve("assets/" + itemId.getNamespace() + "/models/item/" + itemId.getPath() + ".json"));
  }

  private void writeTypedBoatJsonTo(DataCache cache, BoatEntity.Type boatType, String secondLayer, Identifier itemId, Path root) {
    writeJson(cache, makeBoatItemModel(boatType, secondLayer, itemId),
        root.resolve("assets/" + itemId.getNamespace() + "/models/item/" + itemId.getPath() + ".json"));
  }

  private JsonObject makeBoatItemModel(BoatEntity.Type boatType, String secondLayer, Identifier itemId) {
    JsonObject ret = new JsonObject();
    ret.addProperty("parent", MINECRAFT_GENERATED.toString());
    JsonObject textures = new JsonObject();
    textures.addProperty("layer0", new Identifier("item/" + boatType.getName() + "_boat").toString());
    textures.addProperty("layer1", new Identifier(itemId.getNamespace(), "item/" + secondLayer).toString());
    ret.add("textures", textures);
    return ret;
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

  private <T> void writeJsons(DataCache cache, Path root, Map<T, ? extends Supplier<JsonElement>> jsons, BiFunction<Path, T, Path> locator) {
    jsons.forEach((item, supplier) -> {
      Path location = locator.apply(root, item);

      try {
        DataProvider.writeToPath(GSON, cache, supplier.get(), location);
      } catch (Exception var7) {
        LOGGER.error("Couldn't save {}", location, var7);
      }

    });
  }

  private static Path getModelJsonPath(Path root, Identifier id) {
    return root.resolve("assets/" + id.getNamespace() + "/models/" + id.getPath() + ".json");
  }

  @Override
  public String getName() {
    return "Kayak Item Model provider";
  }
}
