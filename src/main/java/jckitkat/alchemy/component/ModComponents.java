package jckitkat.alchemy.component;

import com.mojang.serialization.Codec;
import jckitkat.alchemy.Alchemy;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModComponents {

	public static final ComponentType<Boolean> ALCHEMICAL_BASE = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			Identifier.of(Alchemy.MOD_ID, "alchemical_base"),
			ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
	);

	protected static void initialize() {
		Alchemy.LOGGER.info("Registering {} components", Alchemy.MOD_ID);
	}

}
