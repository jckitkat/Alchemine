package jckitkat.alchemy.blocks.blockEntities.alchemicalTables.calcinationTable;

import jckitkat.alchemy.blocks.ModBlocks;
import jckitkat.alchemy.blocks.blockEntities.ImplementedInventory;
import jckitkat.alchemy.blocks.blockEntities.ModBlockEntities;
import jckitkat.alchemy.items.ModItems;
import jckitkat.alchemy.screen.custom.CalcinationScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CalcinationTableEntity extends AbstractFurnaceBlockEntity implements ExtendedScreenHandlerFactory<BlockPos>{
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;

	protected final PropertyDelegate propertyDelegate;
	private int progress = 0;
	private int maxProgress = 72;

	public CalcinationTableEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.CALCINATION_TABLE_ENTITY, pos, state);
		this.propertyDelegate = new PropertyDelegate() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> CalcinationTableEntity.this.progress;
					case 1 -> CalcinationTableEntity.this.maxProgress;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0: CalcinationTableEntity.this.progress = value;
					case 1: CalcinationTableEntity.this.maxProgress = value;
				}
			}

			@Override
			public int size() {
				return 2;
			}
		};
	}

	@Override
	public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
		return this.pos;
	}

	@Override
	public Text getDisplayName() {
		return Text.translatable(ModBlocks.CALCINATION_TABLE.getTranslationKey());
	}

	@Override
	protected Text getContainerName() {
		return null;
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new CalcinationScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new CalcinationScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		Inventories.writeNbt(nbt, inventory, registryLookup);
		nbt.putInt("calcination_table.progress", progress);
		nbt.putInt("calcination_table.max_progress", maxProgress);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		Inventories.readNbt(nbt, inventory, registryLookup);
		progress = nbt.getInt("calcination_table.progress", 0);
		maxProgress = nbt.getInt("calcination_table.max_progress", 72);
		super.readNbt(nbt, registryLookup);
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		if(hasRecipe()) {
			increaseCraftingProgress();
			markDirty(world, pos, state);

			if(hasCraftingFinished()) {
				craftItem();
				resetProgress();
			}
		} else {
			resetProgress();
		}
	}

	private void resetProgress() {
		this.progress = 0;
		this.maxProgress = 72;
	}

	private void craftItem() {
		ItemStack output = new ItemStack(ModItems.SULFUR, 6);

		this.removeStack(INPUT_SLOT, 1);
		this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
				this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));
	}

	private boolean hasCraftingFinished() {
		return this.progress >= this.maxProgress;
	}

	private void increaseCraftingProgress() {
		this.progress++;
	}

	private boolean hasRecipe() {
		Item input = Items.GUNPOWDER;
		ItemStack output = new ItemStack(ModItems.SULFUR, 6);

		return this.getStack(INPUT_SLOT).isOf(input) &&
				canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
	}

	private boolean canInsertItemIntoOutputSlot(ItemStack output) {
		return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
	}

	private boolean canInsertAmountIntoOutputSlot(int count) {
		int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
		int currentCount = this.getStack(OUTPUT_SLOT).getCount();

		return maxCount >= currentCount + count;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		return createNbt(registryLookup);
	}
}
