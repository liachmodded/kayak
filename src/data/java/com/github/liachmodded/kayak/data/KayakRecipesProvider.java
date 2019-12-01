/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.data;

import com.github.liachmodded.kayak.item.KayakItems;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.advancement.criterion.EnterBlockCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class KayakRecipesProvider implements DataProvider {

  private static final Logger LOGGER = LogManager.getLogger();
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private final DataGenerator root;

  public KayakRecipesProvider(DataGenerator generator) {
    this.root = generator;
  }

  private void generate(Consumer<RecipeJsonProvider> consumer) {
    for (Entry<BoatEntity.Type, Item> entry : KayakItems.CHEST_BOAT_ITEMS.entrySet()) {
      Item vanillaBoat = KayakItems.getVanillaBoat(entry.getKey());
      ShapelessRecipeJsonFactory.create(entry.getValue())
          .input(vanillaBoat)
          .input(Items.CHEST)
          .criterion("has_boat", has(vanillaBoat))
          .offerTo(consumer);
    }

    for (Entry<BoatEntity.Type, Item> entry : KayakItems.HOPPER_BOAT_ITEMS.entrySet()) {
      Item vanillaBoat = KayakItems.getVanillaBoat(entry.getKey());
      ShapelessRecipeJsonFactory.create(entry.getValue())
          .input(vanillaBoat)
          .input(Items.HOPPER)
          .criterion("has_boat", has(vanillaBoat))
          .offerTo(consumer);
    }

    for (Entry<BoatEntity.Type, Item> entry : KayakItems.JUKEBOX_BOAT_ITEMS.entrySet()) {
      Item vanillaBoat = KayakItems.getVanillaBoat(entry.getKey());
      ShapelessRecipeJsonFactory.create(entry.getValue())
          .input(vanillaBoat)
          .input(Blocks.JUKEBOX)
          .criterion("has_boat", has(vanillaBoat))
          .offerTo(consumer);
    }
  }

  @Override
  public void run(DataCache cache) {
    Path output = this.root.getOutput();
    Set<Identifier> set = Sets.newHashSet();
    this.generate((recipeJsonProvider) -> {
      if (!set.add(recipeJsonProvider.getRecipeId())) {
        throw new IllegalStateException("Duplicate recipe " + recipeJsonProvider.getRecipeId());
      }
      this.writeJson(cache, recipeJsonProvider.toJson(), output.resolve(
          "data/" + recipeJsonProvider.getRecipeId().getNamespace() + "/recipes/" + recipeJsonProvider.getRecipeId().getPath() + ".json"));
      JsonObject jsonObject_1 = recipeJsonProvider.toAdvancementJson();
      if (jsonObject_1 != null) {
        this.writeJson(cache, jsonObject_1, output.resolve(
            "data/" + recipeJsonProvider.getAdvancementId().getNamespace() + "/advancements/" + recipeJsonProvider.getAdvancementId().getPath()
                + ".json"));
      }
    });
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

  private EnterBlockCriterion.Conditions entered(Block block_1) {
    return new EnterBlockCriterion.Conditions(block_1, StatePredicate.ANY);
  }

  private InventoryChangedCriterion.Conditions has(ItemConvertible itemConvertible_1) {
    return this.has(ItemPredicate.Builder.create().item(itemConvertible_1).build());
  }

  private InventoryChangedCriterion.Conditions has(Tag<Item> tag_1) {
    return this.has(ItemPredicate.Builder.create().tag(tag_1).build());
  }

  private InventoryChangedCriterion.Conditions has(ItemPredicate... itemPredicates_1) {
    return new InventoryChangedCriterion.Conditions(NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, itemPredicates_1);
  }

  @Override
  public String getName() {
    return "Kayak Recipes";
  }
}
