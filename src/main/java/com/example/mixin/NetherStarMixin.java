package com.example.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NetherStarItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import static com.example.ExampleMod.menuName;
import static com.example.ExampleMod.serverMenu;

@Mixin(NetherStarItem.class)
public abstract class NetherStarMixin extends Item {
	public NetherStarMixin(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
			serverMenu(((ServerPlayerEntity) user));
			return TypedActionResult.pass(user.getStackInHand(hand));
	}
}