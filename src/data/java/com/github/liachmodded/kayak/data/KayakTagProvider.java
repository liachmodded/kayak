/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.data;

import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.tag.Tag;
import net.minecraft.tag.Tag.Builder;
import net.minecraft.util.Identifier;

public interface KayakTagProvider<T> {

  Map<Tag<T>, Builder<T>> getBuilderMap();

  default void upload(Identifier id, Consumer<Builder<T>> setup) {
    Builder<T> builder = Builder.create();
    getBuilderMap().put(new Tag<>(id), builder);
    setup.accept(builder);
  }

}
