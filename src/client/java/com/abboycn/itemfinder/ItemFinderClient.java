package com.abboycn.itemfinder;

import com.abboycn.itemfinder.commands.cmdFindItem;
import com.abboycn.itemfinder.commands.cmdFI;
import com.abboycn.itemfinder.render.beamRender;
import com.abboycn.itemfinder.searcher.ItemLoaderUnstackable;
import com.abboycn.itemfinder.searcher.ZoneLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ItemFinderClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		//载入资源
		ZoneLoader.load();
		ItemLoaderUnstackable.load();
		//注册命令
		ClientCommandRegistrationCallback.EVENT.register((dispatcher,registryAccess)->cmdFindItem.register(dispatcher,registryAccess));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher,registryAccess)->cmdFI.register(dispatcher,registryAccess));
		// 注册光柱渲染器(每刻渲染光柱)
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.world != null) {
				beamRender.render(client.world);
			}
		});
	}
}