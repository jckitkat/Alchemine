package jckitkat.alchemy.blocks.blockEntities.alchemicalTables.calcinationTable;

import com.mojang.serialization.MapCodec;
import jckitkat.alchemy.blocks.blockEntities.ModBlockEntities;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CalcinationTable extends BlockWithEntity {

	public static final MapCodec<CalcinationTable> CODEC = CalcinationTable.createCodec(CalcinationTable::new);

	public CalcinationTable(Settings settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return createCodec(CalcinationTable::new);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CalcinationTableEntity(pos, state);
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
		if (state.getBlock() != world.getBlockState(pos).getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CalcinationTableEntity) {
				ItemScatterer.spawn((World) world, pos, ((CalcinationTableEntity) blockEntity));
				world.updateComparators(pos, this);
			}
		}
		super.onStateReplaced(state, world, pos, moved);
	}

	@Override
	protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			NamedScreenHandlerFactory screenHandlerFactory = ((CalcinationTableEntity) world.getBlockEntity(pos));
			if (screenHandlerFactory != null) {
				player.openHandledScreen(screenHandlerFactory);
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient()) {
			return null;
		}

		return validateTicker(type, ModBlockEntities.CALCINATION_TABLE_ENTITY,
				((world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1)));
	}
}
