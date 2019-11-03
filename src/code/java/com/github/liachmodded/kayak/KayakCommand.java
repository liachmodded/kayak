/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak;

import com.github.liachmodded.kayak.entity.KayakEntityTags;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.tag.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.registry.Registry;

final class KayakCommand {

  private final Tag<EntityType<?>> actual = TagRegistry.entityType(KayakEntityTags.CARRIER_BOAT);

  KayakCommand() {}

  void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(
        CommandManager.literal("kayak-test")
            .executes(this::executeKayakTest)
    );
  }

  private int executeKayakTest(CommandContext<ServerCommandSource> context) {
    ServerCommandSource source = context.getSource();
    Tag<EntityType<?>> expected = source.getMinecraftServer().getTagManager().entityTypes().getOrCreate(KayakEntityTags.CARRIER_BOAT);
    source.sendFeedback(Texts.join(expected.values(), this::idToText), false);
    source.sendFeedback(Texts.join(this.actual.values(), this::idToText), false);

    return Command.SINGLE_SUCCESS;
  }

  private Text idToText(EntityType<?> type) {
    return new LiteralText(Registry.ENTITY_TYPE.getId(type).toString());
  }

}
