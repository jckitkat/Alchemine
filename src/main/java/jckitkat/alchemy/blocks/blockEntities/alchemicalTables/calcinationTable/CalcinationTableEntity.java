package jckitkat.alchemy.blocks.blockEntities.alchemicalTables.calcinationTable;

import com.ibm.icu.impl.TextTrieMap;
import jckitkat.alchemy.blocks.ModBlocks;
import jckitkat.alchemy.blocks.blockEntities.ImplementedInventory;
import jckitkat.alchemy.blocks.blockEntities.ModBlockEntities;
import jckitkat.alchemy.items.ModItems;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
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

public class CalcinationTableEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

	private final static int INPUT_SLOT = 0;
	private final static int OUTPUT_SLOT = 1;

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
	public DefaultedList<ItemStack> getItems() {
		return inventory;
	}

	@Override
	public Object getScreenOpeningData(ServerPlayerEntity player) {
		return this.pos;
	}

	@Override
	public Text getDisplayName() {
		return Text.translatable(ModBlocks.CALCINATION_TABLE.getTranslationKey());
	}

	@Override
	public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return null;
	}

	public void tick(World world1, BlockPos pos, BlockState state1) {

		if (hasRecipe()) {

			increaseCraftingProgress();
			markDirty(world, pos, state1);

			if (hasCraftingFinished()) {
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
		ItemStack output = new ItemStack(ModItems.SULFUR, 2);
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
		ItemStack output = new ItemStack(ModItems.SULFUR, 2);

		return this.getStack(INPUT_SLOT).isOf(input) && canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOuputSlot(output);
	}

	private boolean canInsertItemIntoOuputSlot(ItemStack output) {
		return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
	}

	private boolean canInsertAmountIntoOutputSlot(int count) {
		int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
		int currentCount = this.getStack(OUTPUT_SLOT).getCount();

		return maxCount >= currentCount + count;
	}

	@Override
	public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
		return createNbt(registries);
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
		Inventories.writeNbt(nbt, inventory, registries);
		nbt.putInt("calcination_table.progress", progress);
		nbt.putInt("calcination_table.max_progress", maxProgress);
		super.writeNbt(nbt, registries);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
		Inventories.readNbt(nbt, inventory, registries);
		progress = nbt.getInt("calcination_table.progress", 0);
		maxProgress = nbt.getInt("calcination_table.max_progress", 72);
		super.readNbt(nbt, registries);
	}
}
