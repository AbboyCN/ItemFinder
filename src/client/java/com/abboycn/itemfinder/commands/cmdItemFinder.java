package com.abboycn.itemfinder.commands;

import com.abboycn.itemfinder.commands.tabExecuter.TabexeItemFinder;
import com.abboycn.itemfinder.searcher.ItemLoaderUnstackable;
import com.abboycn.itemfinder.searcher.ItemNameMapLoader;
import com.abboycn.itemfinder.searcher.ZoneLoader;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class cmdItemFinder {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("itemfinder")
                .then(ClientCommandManager.literal("reload")
                        .then(argument("target",StringArgumentType.string())
                                .suggests((context,builder)-> TabexeItemFinder.suggestItemFinderReload(builder))
                                .executes(c->cmdItemFinderReloadExecuter(c.getSource().getPlayer(),StringArgumentType.getString(c,"target")))))
                .then(ClientCommandManager.literal("help")
                        .executes((c->cmdItemFinderHelpExecuter(c.getSource().getPlayer())))));
    }

    public static int cmdItemFinderReloadExecuter(ClientPlayerEntity player, String target){
        if(player==null)
            return 0;
        if(target==null||target.isEmpty()){
            player.sendMessage(Text.literal("§c对象\""+target+"\"不合法."),false);
            return 0;
        }
        switch (target){
            case "pinyin":
                ItemNameMapLoader.load();
                player.sendMessage(Text.literal("§a已重载."),false);
                break;
            case "unstackable_items":
                ItemLoaderUnstackable.load();
                player.sendMessage(Text.literal("§a已重载."),false);
                break;
            case "zones":
                ZoneLoader.load();
                player.sendMessage(Text.literal("§a已重载."),false);
                break;
            default:
                player.sendMessage(Text.literal("§c对象\""+target+"\"不合法."),false);
                return 0;
        }
        return 1;
    }

    public static int cmdItemFinderHelpExecuter(ClientPlayerEntity player){
        if(player==null)
            System.out.println("See help on https://modrinth.com/mod/itemfinderinall-items.");
        else
            player.sendMessage(Text.literal("§e查看帮助请转到§f https://modrinth.com/mod/itemfinderinall-items."),false);
        return 1;
    }
}
