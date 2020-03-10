/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.data;

import com.github.liachmodded.kayak.entity.KayakEntities;
import com.github.liachmodded.kayak.entity.KayakEntityTags;
import java.util.Map;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag;
import net.minecraft.tag.Tag.Builder;
import net.minecraft.tag.TagContainer;

public class KayakEntityTagProvider extends EntityTypeTagsProvider implements KayakTagProvider<EntityType<?>> {
  
  public KayakEntityTagProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  protected void configure() {
    upload(KayakEntityTags.CARRIER_BOAT, this::addCarrierBoats);
  }
  
  private void addCarrierBoats(Builder<EntityType<?>> builder) {
    for (EntityType<?> type : KayakEntities.CARRIER_BOATS) {
      builder.add(type);
    }
  }

  @Override
  public Map<Tag<EntityType<?>>, Builder<EntityType<?>>> getBuilderMap() {
    return this.tagBuilders;
  }

  @Override
  protected void setContainer(TagContainer<EntityType<?>> container) {
  }

  @Override
  public String getName() {
    return "Kayak Entity Tag";
  }
}
