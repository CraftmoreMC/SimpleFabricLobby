package com.example;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.gui.SimpleGui;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.literal;

public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static Text menuName;

	@Override
	public void onInitialize() {
		menuName = Text.literal("Server Select");
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("test")
				.executes(context -> {
					ServerPlayerEntity player = context.getSource().getPlayer();
					setInventory(player);
					return 0;
				})));
	}

	public static void setInventory(ServerPlayerEntity player){
		ItemStack itemStack = Items.NETHER_STAR.getDefaultStack().setCustomName((menuName));
		if(player.getInventory().getStack(4).getItem() != Items.NETHER_STAR){
			player.getInventory().clear();
			player.playerScreenHandler.setCursorStack(ItemStack.EMPTY);
			player.currentScreenHandler.setCursorStack(ItemStack.EMPTY);
			player.getInventory().setStack(4, itemStack);
			player.getInventory().updateItems();
		}
	}

	public static void buildServerItem (ServerPlayerEntity player, SimpleGui gui, int slot, Item item, String server){
		ItemStack itemStack = item.getDefaultStack().setCustomName(Text.literal(WordUtils.capitalize(server)));
		itemStack.addEnchantment(null, 0);
		gui.setSlot(slot, new GuiElement(itemStack, (index, clickType, actionType) -> {
			ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
			dataOutput.writeUTF("Connect");
			dataOutput.writeUTF(server);
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeIdentifier(new Identifier("bungeecord", "main"));
			buf.writeBytes(dataOutput.toByteArray());
			CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(buf);
			player.networkHandler.sendPacket(packet);
		}));
	}

	public static int serverMenu(ServerPlayerEntity player) {
		try {
			SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X3, player, false);

			gui.setTitle(Text.literal("Server Select"));

			buildServerItem(player, gui, 11, Items.GRASS_BLOCK, "survival");
			buildServerItem(player, gui, 15, Items.DIAMOND_BLOCK, "creative");

			gui.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}