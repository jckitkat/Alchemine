package jckitkat.alchemy.items;

import jckitkat.alchemy.Alchemy;
import jckitkat.alchemy.component.ModComponents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {

	public static final Item SULFUR = register("sulfur", Item::new, new Item.Settings().component(ModComponents.ALCHEMICAL_BASE, true));

	private static Object FabricDocsReference;
	public static final RegistryKey<ItemGroup> ALCHEMY_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(Alchemy.MOD_ID, "item_group"));
	public static final ItemGroup ALCHEMY_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(ModItems.SULFUR))
			.displayName(Text.translatable("itemGroup.alchemy"))
			.build();

	public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
		// Create the item key.
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Alchemy.MOD_ID, name));

		// Create the item instance.
		Item item = itemFactory.apply(settings.registryKey(itemKey));

		// Register the item.
		Registry.register(Registries.ITEM, itemKey, item);

		return item;
	}

	public static void initialize() {
		Registry.register(Registries.ITEM_GROUP, ALCHEMY_GROUP_KEY, ALCHEMY_GROUP);

		ItemGroupEvents.modifyEntriesEvent(ModItems.ALCHEMY_GROUP_KEY).register((itemGroup) -> itemGroup.add(ModItems.SULFUR));
	}

}
