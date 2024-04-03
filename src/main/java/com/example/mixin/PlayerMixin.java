package com.example.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

import static com.example.ExampleMod.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin{
	@Shadow public ScreenHandler currentScreenHandler;

	/**
	 * @author UnixSupremacist
	 * @reason cancel all item drops
	 */
	@Overwrite
	public ItemEntity dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership) {
		ServerPlayerEntity player = ((ServerPlayerEntity)((Object)this));
		setInventory(player);
		if (stack.getItem() == Items.NETHER_STAR && stack.getName().equals(menuName)){
			serverMenu(player);
		}
		return null;
	}

	/**
	 * @author UnixSupremacist
	 * @reason item interact
	 */
	@Overwrite
	public void onPickupSlotClick(ItemStack cursorStack, ItemStack stack, ClickType clickType) {
		ServerPlayerEntity player = ((ServerPlayerEntity)((Object)this));
		setInventory(player);
		if (stack.getItem() == Items.NETHER_STAR && stack.getName().equals(menuName)){
			serverMenu(player);
		}
	}

	@Inject(at = @At("HEAD"), method = "openHandledScreen")
	public void screen (NamedScreenHandlerFactory factory, CallbackInfoReturnable<OptionalInt> cir){
		ServerPlayerEntity player = ((ServerPlayerEntity)((Object)this));
		setInventory(player);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	public void tick (CallbackInfo ci){
		ServerPlayerEntity player = ((ServerPlayerEntity)((Object)this));
		setInventory(player);
	}
}