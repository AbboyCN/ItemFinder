package com.abboycn.itemfinder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import com.abboycn.itemfinder.commands.cmdFindItem;

public class ItemFinderClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		//载入位置信息
		cmdFindItem.loadItemPositions();
		//注册命令
		ClientCommandRegistrationCallback.EVENT.register((dispatcher,registryAccess)->cmdFindItem.register(dispatcher,registryAccess));
	}
}