package jckitkat.alchemy.screen;

import jckitkat.alchemy.Alchemy;
import jckitkat.alchemy.screen.custom.CalcinationScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {

	public static final ScreenHandlerType<CalcinationScreenHandler> CALCINATION_SCREEN_HANDLER =
			Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Alchemy.MOD_ID, "calcination_screen_handler"),
					new ExtendedScreenHandlerType<>(CalcinationScreenHandler::new, BlockPos.PACKET_CODEC));

	public static void registerScreenHandlers() {
		Alchemy.LOGGER.info("registering Screen Handlers for " + Alchemy.MOD_ID);
	}
}
