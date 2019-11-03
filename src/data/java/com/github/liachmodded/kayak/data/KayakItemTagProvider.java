/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.data;

import com.github.liachmodded.kayak.item.KayakItemTags;
import com.github.liachmodded.kayak.item.KayakItems;
import java.util.Map;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.tag.Tag.Builder;
import net.minecraft.tag.TagContainer;

public final class KayakItemTagProvider extends ItemTagsProvider implements KayakTagProvider<Item> {

  private TagContainer<Item> tagContainer;

  public KayakItemTagProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  protected void configure() {
    upload(KayakItemTags.CHEST_BOAT, this::addChestBoats);
    upload(KayakItemTags.HOPPER_BOAT, this::addHopperBoats);
  }

  private void addChestBoats(Builder<Item> builder) {
    for (Item boat : KayakItems.CHEST_BOAT_ITEMS.values()) {
      builder.add(boat);
    }
  }

  private void addHopperBoats(Builder<Item> builder) {
    for (Item boat : KayakItems.HOPPER_BOAT_ITEMS.values()) {
      builder.add(boat);
    }
  }

  @Override
  public Map<Tag<Item>, Builder<Item>> getBuilderMap() {
    return field_11481;
  }

  @Override
  protected void method_10511(TagContainer<Item> tagContainer) {
  }

  @Override
  public String getName() {
    return "Kayak Item Tag";
  }
}
