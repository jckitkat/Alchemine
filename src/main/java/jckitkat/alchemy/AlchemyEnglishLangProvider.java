package jckitkat.alchemy;

import jckitkat.alchemy.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class AlchemyEnglishLangProvider extends FabricLanguageProvider {

	protected AlchemyEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, "en_us", registryLookup);
	}

	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
		translationBuilder.add("item.alchemy.sulfur", "Sulfur");
		translationBuilder.add("tooltip.alchemy.alchemical_base", "Alchemical Base");
		translationBuilder.add("itemGroup.alchemy", "Alchemy");
		translationBuilder.add(ModBlocks.CALCINATION_TABLE.asItem().getTranslationKey(), "Calcination Table");
	}
}
