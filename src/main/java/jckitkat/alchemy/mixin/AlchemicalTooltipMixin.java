package jckitkat.alchemy.mixin;

import jckitkat.alchemy.component.ModComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(Item.class)
public class AlchemicalTooltipMixin {
	@Inject(at = @At("RETURN"), method = "appendTooltip")
	private void appendCustomTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
		if (stack.getComponents().contains(ModComponents.ALCHEMICAL_BASE)) {
			tooltip.add(Text.translatable("tooltip.alchemy.alchemical_base").formatted(Formatting.GOLD));
		}
	}
}