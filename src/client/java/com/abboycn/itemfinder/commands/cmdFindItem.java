package com.abboycn.itemfinder.commands;

import com.abboycn.itemfinder.render.beamRender;
import com.abboycn.itemfinder.searcher.ItemSearcher;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class cmdFindItem {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher,CommandRegistryAccess registryAccess){
        dispatcher.register(ClientCommandManager.literal("finditem")
                        .then(argument("itemID", ItemStackArgumentType.itemStack(registryAccess))
                        .executes(c -> cmdFindItemExecuter(c.getSource().getPlayer(),ItemStackArgumentType.getItemStackArgument(c,"itemID").getItem()))));
    }

    public static int cmdFindItemExecuter(ClientPlayerEntity player, Item target){
        if(player==null)
            return 0;
        if(!(player.getWorld().getRegistryKey() == World.OVERWORLD)){
            player.sendMessage(Text.literal("§c维度\"minecraft:overworld\"未在客户端加载"),false);
            return 0;
        }
        if(target== Items.AIR){
            player.sendMessage(Text.literal("§c你可以按下Alt+F4退出游戏,谁闲的没事通过崩端退游戏啊?!"),false);
            return 0;
        }
        List<BlockPos> found = ItemSearcher.search(player.clientWorld, target);
        if(found.isEmpty()) {
            player.sendMessage(Text.literal("§c无法找到物品\"" + target.getName().getString() + "\"的位置信息"), false);
            return 0;
        }
        // 发送结果 & 修订位置
        List<BlockPos> foundModified = new java.util.ArrayList<>(List.of());
        player.sendMessage(Text.literal("§a找到 §e" + target.getName().getString() + "§a 位于：§b("+ found.size() + ")"), false);
        for (BlockPos pos : found) {
            posSender(player, directionJudge(pos), pos);
            foundModified.add(posModifier(pos));
        }
        // 更新光柱
        beamRender.addBeams(foundModified);
        return 1;
    }

    private static String directionJudge(BlockPos pos){
        if(pos.getZ()>=37)
            return "南区(Z+) ";
        else if(pos.getZ()<=7)
            return "北区(Z-) ";
        else if(pos.getX()>=71)
            return "大宗(X+) ";
        else if(pos.getZ()==29)
            return "常用物品处(四大金刚) ";
        else if(pos.getZ()==15)
            return "不可堆叠分类区";
        else return "";
    }

    private static BlockPos posModifier(BlockPos pos){
        if(pos.getX()==59)
            pos = new BlockPos(58,pos.getY(),pos.getZ());
        else if(pos.getZ()==47)
            pos = new BlockPos(48,pos.getY(),pos.getZ());
        else if(pos.getZ()==26)
            pos = new BlockPos(pos.getX(),pos.getY(),25);
        else if(pos.getZ()==18)
            pos = new BlockPos(pos.getX(),pos.getY(),19);
        return pos;
    }

    private static void posSender(ClientPlayerEntity player,String direction,BlockPos pos){
        player.sendMessage(Text.literal(String.format("§7- §d%s§f[%d, %d, %d]",
                direction,
                pos.getX(),
                pos.getY(),
                pos.getZ()
        )), false);
    }
}
