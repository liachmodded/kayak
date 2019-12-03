/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.github.liachmodded.kayak.client.ui;

import com.github.liachmodded.kayak.ui.FurnaceBoatContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FurnaceBoatScreen extends AbstractContainerScreen<FurnaceBoatContainer> {

  private static final Identifier BACKGROUND = new Identifier("minecraft", "textures/gui/container/furnace.png");

  public FurnaceBoatScreen(FurnaceBoatContainer abstractFurnaceContainer_1, PlayerInventory playerInventory_1, Text text_1) {
    super(abstractFurnaceContainer_1, playerInventory_1, text_1);
  }

  @Override
  public void render(int int_1, int int_2, float float_1) {
    this.renderBackground();
    super.render(int_1, int_2, float_1);
    this.drawMouseoverTooltip(int_1, int_2);
  }

  @Override
  protected void drawForeground(int int_1, int int_2) {
    String string_1 = this.title.asFormattedString();
    this.font.draw(string_1, (float) (this.containerWidth / 2 - this.font.getStringWidth(string_1) / 2), 6.0F, 4210752);
    this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float) (this.containerHeight - 96 + 2), 4210752);
  }

  @Override
  protected void drawBackground(float float_1, int int_1, int int_2) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.minecraft.getTextureManager().bindTexture(BACKGROUND);
    int int_3 = this.x;
    int int_4 = this.y;
    this.blit(int_3, int_4, 0, 0, this.containerWidth, this.containerHeight);
    int int_6;
    if (this.container.getBoat().getFuel() > 0) {
      int_6 = this.container.getFuelProgress();
      this.blit(int_3 + 56, int_4 + 36 + 12 - int_6, 176, 12 - int_6, 14, int_6 + 1);
    }

//    int_6 = ((AbstractFurnaceContainer)this.container).getCookProgress();
//    this.blit(int_3 + 79, int_4 + 34, 176, 14, int_6 + 1, 16);
  }
}
