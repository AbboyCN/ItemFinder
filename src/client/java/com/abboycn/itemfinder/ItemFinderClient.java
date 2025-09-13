package com.abboycn.itemfinder;

import com.abboycn.itemfinder.commands.*;
import com.abboycn.itemfinder.render.*;
import com.abboycn.itemfinder.searcher.ItemLoaderUnstackable;
import com.abboycn.itemfinder.searcher.ItemNameMapLoader;
import com.abboycn.itemfinder.searcher.ZoneLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ItemFinderClient implements ClientModInitializer {
	public static final String MOD_ID = "itemfinder";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		//载入资源
		ClientPlayConnectionEvents.JOIN.register((handler,sender,client)->initName(client.player));
		ZoneLoader.load();
		LOGGER.info("Loaded search zones!");
		ItemLoaderUnstackable.load();
		LOGGER.info("Loaded unstackable items!");
		//注册命令
		ClientCommandRegistrationCallback.EVENT.register(cmdFindItem::register);
		ClientCommandRegistrationCallback.EVENT.register(cmdFI::register);
		ClientCommandRegistrationCallback.EVENT.register((d,r)->cmdFindItemName.register(d));
		ClientCommandRegistrationCallback.EVENT.register((d,r)->cmdFN.register(d));
		ClientCommandRegistrationCallback.EVENT.register((d,r)->cmdItemFinder.register(d));
		// 注册光柱渲染器(每刻渲染光柱)
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.world != null) {
				beamRender.update(client.world);
				shulkerRender.update();
			}
		});
	}

	public static void initName(ClientPlayerEntity clientPlayer){
		if(clientPlayer != null) {
			ItemNameMapLoader.load();
			LOGGER.info("Loaded item name translater!");
		}
	}
}