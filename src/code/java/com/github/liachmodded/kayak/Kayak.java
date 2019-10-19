/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak;

import com.github.liachmodded.kayak.entity.KayakEntities;
import com.github.liachmodded.kayak.item.KayakItems;
import com.github.liachmodded.kayak.stat.KayakStats;
import com.github.liachmodded.kayak.ui.KayakContainerProviders;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Kayak implements ModInitializer {

  /**
   * The mod identifier of kayak.
   */
  public static final String ID = "kayak";
  public static final Logger LOGGER = LogManager.getLogger(ID);

  public static Identifier name(String name) {
    return new Identifier(ID, name);
  }

  @Override
  public void onInitialize() {
    KayakEntities.init();
    KayakItems.init();
    KayakStats.init();
    KayakContainerProviders.init();
  }
}
