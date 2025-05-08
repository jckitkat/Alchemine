package jckitkat.alchemy;

import jckitkat.alchemy.blocks.blockEntities.ModBlockEntities;
import jckitkat.alchemy.screen.ModScreenHandlers;
import jckitkat.alchemy.screen.custom.CalcinationScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class AlchemyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HandledScreens.register(ModScreenHandlers.CALCINATION_SCREEN_HANDLER, CalcinationScreen::new);
	}
}