/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.network;

import com.github.liachmodded.kayak.Kayak;
import com.github.liachmodded.kayak.entity.BedBoatEntity;
import io.netty.buffer.Unpooled;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public final class KayakNetworking {

  public static final Identifier REQUEST_SLEEP_IN_BOAT = Kayak.name("request_sleep_in_boat");

  static {
    ServerSidePacketRegistry.INSTANCE.register(REQUEST_SLEEP_IN_BOAT, KayakNetworking::onRequestSleepInBoat);
  }

  public static void requestSleepInBoat() {
    if (ClientSidePacketRegistry.INSTANCE.canServerReceive(REQUEST_SLEEP_IN_BOAT)) {
      sendToServer(REQUEST_SLEEP_IN_BOAT);
    }
  }

  public static void onRequestSleepInBoat(PacketContext context, PacketByteBuf buf) {
    context.getTaskQueue().execute(() -> {
      Entity ridden = context.getPlayer().getVehicle();
      if (!(ridden instanceof BedBoatEntity)) {
        return;
      }
      ((BedBoatEntity) ridden).toggleSleepState();
    });
  }

  public static void sendToServer(Identifier channel, Consumer<PacketByteBuf> writer) {
    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    writer.accept(buf);
    ClientSidePacketRegistry.INSTANCE.sendToServer(channel, buf);
  }

  public static void sendToServer(Identifier channel) {
    sendToServer(channel, buf -> {
    });
  }

  public static void init() {}

  private KayakNetworking() {}

}
