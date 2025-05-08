package jckitkat.alchemy.blocks.blockEntities;

import jckitkat.alchemy.Alchemy;
import jckitkat.alchemy.blocks.ModBlocks;
import jckitkat.alchemy.blocks.blockEntities.alchemicalTables.calcinationTable.CalcinationTableEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

	public static final BlockEntityType<CalcinationTableEntity> CALCINATION_TABLE_ENTITY =
			register("counter", CalcinationTableEntity::new, ModBlocks.CALCINATION_TABLE);

	private static <T extends BlockEntity> BlockEntityType<T> register(
			String name,
			FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
			Block... blocks
	) {
		Identifier id = Identifier.of(Alchemy.MOD_ID, name);
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
	}

	public static void initialize() {}
}
