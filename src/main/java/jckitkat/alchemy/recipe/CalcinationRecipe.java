package jckitkat.alchemy.recipy;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CalcinationRecipe implements Recipe<CalcinationRecipeInput> {
	private final Ingredient ingredient;
	private final ItemStack result;
	@Nullable
	private IngredientPlacement ingredientPlacement;

	public CalcinationRecipe(Ingredient ingredient, ItemStack output) {
		this.ingredient = ingredient;
		this.result = output;
	}

	@Override
	public boolean matches(CalcinationRecipeInput input, World world) {
		return this.ingredient.test(input.getStackInSlot(0));
	}

	@Override
	public ItemStack craft(CalcinationRecipeInput input, RegistryWrapper.WrapperLookup registries) {
		return null;
	}

	@Override
	public RecipeSerializer<? extends Recipe<CalcinationRecipeInput>> getSerializer() {
		return null;
	}

	@Override
	public RecipeType<? extends Recipe<CalcinationRecipeInput>> getType() {
		return null;
	}

	@Override
	public IngredientPlacement getIngredientPlacement() {
		return null;
	}

	@Override
	public RecipeBookCategory getRecipeBookCategory() {
		return null;
	}
}
