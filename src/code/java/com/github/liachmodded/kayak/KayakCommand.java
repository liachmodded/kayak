/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak;

import com.github.liachmodded.kayak.entity.KayakEntityTags;
import com.github.liachmodded.kayak.entity.SleepableLivingEntity;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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

    dispatcher.register(
        CommandManager.literal("kayak-sleep")
            .executes(this::executeKayakSleep)
            .then(
                CommandManager.argument("entity", EntityArgumentType.entities())
                    .executes(this::executeKayakSleepEntities)
            )

    );
  }

  private int executeKayakTest(CommandContext<ServerCommandSource> context) {
    ServerCommandSource source = context.getSource();
    Tag<EntityType<?>> expected = source.getMinecraftServer().getTagManager().entityTypes().getOrCreate(KayakEntityTags.CARRIER_BOAT);
    source.sendFeedback(Texts.join(expected.values(), this::idToText), false);
    source.sendFeedback(Texts.join(this.actual.values(), this::idToText), false);

    return Command.SINGLE_SUCCESS;
  }

  private int executeKayakSleep(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    Entity entity = context.getSource().getEntity();
    
    if (entity instanceof LivingEntity) {
      makeSleep(Collections.singleton((LivingEntity) entity));
      
      context.getSource().sendFeedback(new LiteralText("Made yourself sleep"), false);
      return Command.SINGLE_SUCCESS;
    }
    throw ServerCommandSource.REQUIRES_ENTITY_EXCEPTION.create();
  }

  private int executeKayakSleepEntities(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
    Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "entity");
    
    List<LivingEntity> livings = new ArrayList<>();
    for (Entity e : entities) {
      if (e instanceof LivingEntity) {
        livings.add((LivingEntity) e);
      }
    }
    
    if (livings.size() == 0) {
      throw ServerCommandSource.REQUIRES_ENTITY_EXCEPTION.create();
    }

    makeSleep(livings);
    
    return livings.size();
  }
  
  private void makeSleep(Collection<LivingEntity> entities) {
    for (LivingEntity e : entities) {
      if (e.isSleeping()) {
        e.wakeUp();
      } else {
        ((SleepableLivingEntity) e).sleep();
      }
    }
  }

  private Text idToText(EntityType<?> type) {
    return new LiteralText(Registry.ENTITY_TYPE.getId(type).toString());
  }

}
