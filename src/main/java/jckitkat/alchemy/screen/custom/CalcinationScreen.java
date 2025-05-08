package jckitkat.alchemy.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import jckitkat.alchemy.Alchemy;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CalcinationScreen extends HandledScreen<CalcinationScreenHandler>{

	private static final Identifier GUI_TEXTURE =
			Identifier.of(Alchemy.MOD_ID, "textures/gui/calcination/calcination_table_gui.png");
	private static final Identifier ARROW_TEXTURE =
			Identifier.of(Alchemy.MOD_ID, "textures/gui/arrow_progress.png");

	public CalcinationScreen(CalcinationScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;

		context.drawGuiTexture(RenderLayer::getGuiTextured, GUI_TEXTURE, x, y, backgroundWidth, backgroundHeight);

		renderProgressArrow(context, x, y);
	}

	private void renderProgressArrow(DrawContext context, int x, int y) {
		if (handler.isCrafting()) {
			context.drawTexture(RenderLayer::getGuiTextured, ARROW_TEXTURE, x + 73, y + 35,0, 0,
					handler.getScaledArrowProgress(), 16, 24, 16);
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);
		drawMouseoverTooltip(context, mouseX, mouseY);
	}
}
