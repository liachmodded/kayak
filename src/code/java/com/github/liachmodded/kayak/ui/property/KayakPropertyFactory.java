/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.ui.property;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import net.minecraft.container.Property;

public final class KayakPropertyFactory {

  @SafeVarargs
  public static <T> void addToContainer(Consumer<Property> adder, T target, PropertyApplication<T>... applications) {
    for (PropertyApplication<T> application : applications) {
      adder.accept(of(target, application));
    }
  }

  public static <T> void addToContainer(Consumer<Property> adder, T target, Iterable<PropertyApplication<T>> applications) {
    for (PropertyApplication<T> application : applications) {
      adder.accept(of(target, application));
    }
  }

  public static <T, E extends Enum<E> & PropertyApplication<T>> Map<E, Property> map(T target, Class<E> enumType) {
    EnumMap<E, Property> map = new EnumMap<>(enumType);
    for (E key : enumType.getEnumConstants()) {
      map.put(key, of(target, key));
    }
    return map;
  }

  public static <T> Property of(T target, PropertyApplication<T> application) {
    return new Property() {
      @Override
      public int get() {
        return application.get(target);
      }

      @Override
      public void set(int var1) {
        application.set(target, var1);
      }
    };
  }

  public static Property of(IntSupplier getter) {
    return new Property() {
      @Override
      public int get() {
        return getter.getAsInt();
      }

      @Override
      public void set(int var1) {
        throw new UnsupportedOperationException("View only");
      }
    };
  }
}
