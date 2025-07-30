package com.example.creativecommands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;
import java.util.List;

public class CreativeCommands extends JavaPlugin implements CommandExecutor, TabCompleter {

    private final Random random = new Random();
    private final Map<String, String> emojiMap = new HashMap<>();

    @Override
    public void onEnable() {
        // 注册命令执行器
        getCommand("hello").setExecutor(this);
        getCommand("randomtp").setExecutor(this);
        getCommand("weatherfun").setExecutor(this);
        getCommand("emoji").setExecutor(this);
getCommand("zz").setExecutor(this);
getCommand("qxhelp").setExecutor(this);

        // 初始化表情符号映射
        initEmojiMap();

        Bukkit.getLogger().info("§ahello world1.16.5byqiu_xi_903插件已成功启用！");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("§chello world1.16.5byqiu_xi_903插件已禁用！");
    }

    private void initEmojiMap() {
        emojiMap.put("heart", "❤️❤️❤️❤️❤️\n❤️§cLOVE§f❤️\n❤️❤️❤️❤️❤️");
        emojiMap.put("star", "⭐⭐⭐\n⭐§eSTAR§f⭐\n⭐⭐⭐");
        emojiMap.put("smile", "😀😀😀😀\n😀§6HAPPY§f😀\n😀😀😀😀");
        emojiMap.put("cool", "😎😎😎\n😎§bCOOL§f😎\n😎😎😎");
        emojiMap.put("fire", "🔥🔥🔥🔥\n🔥§cFIRE§f🔥\n🔥🔥🔥🔥");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用此命令!");
            return true;
        }

        Player player = (Player) sender;

        switch (cmd.getName().toLowerCase()) {
            case "hello":
                handleHelloCommand(player, args);
                break;
            case "randomtp":
                handleRandomTpCommand(player, args);
                break;
            case "weatherfun":
                handleWeatherFunCommand(player, args);
                break;
            case "emoji":
                handleEmojiCommand(player, args);
                break;
            case "zz":
                handleZzCommand(player);
                break;
            case "qxhelp":
                handleQxHelpCommand(player);
                break;
            default:
                return false;
        }
        return true;
    }

    private void handleHelloCommand(Player player, String[] args) {
        if (args.length == 0) {
            // 默认行为：发送标题和欢迎消息
            player.sendTitle("§6§l你好世界", "§e欢迎使用创意命令插件", 10, 70, 20);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage("§a欢迎使用创意命令插件！尝试使用其他命令: /randomtp, /weatherfun, /emoji（此插件由邱息编写，是他的第一个插件）");
            return;
        }

        // 处理参数化命令
        String param = args.length > 0 ? args[0] : "";
        String text = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "";

        // 命令引导说明
        if (param.isEmpty()) {
            player.sendMessage("§6===== §eHello命令帮助 §6=====");
            player.sendMessage("§b/hello upmaintitle <文本> §f- 向所有玩家显示大型标题");
            player.sendMessage("§b/hello uptitle <文本> §f- 向所有玩家发送聊天消息");
            player.sendMessage("§b/hello maintitle <文本> §f- 仅向自己显示大型标题");
            player.sendMessage("§b/hello title <文本> §f- 仅向自己发送聊天消息");
            player.sendMessage("§6=========================");
            return;
        }

        switch (param.toLowerCase()) {
            case "upmaintitle":
                // 向所有玩家显示大型标题
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle("§6" + text, "", 10, 70, 20);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                }
                player.sendMessage("§a已向所有玩家发送大型标题!");
                break;
            case "uptitle":
                // 向所有玩家发送聊天消息
                String broadcastMessage = "§e[全局通知] §f" + text;
                Bukkit.broadcastMessage(broadcastMessage);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                break;
            case "maintitle":
                // 仅向自己显示大型标题
                player.sendTitle("§6" + text, "", 10, 70, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                break;
            case "title":
                // 仅向自己发送聊天消息
                player.sendMessage("§e" + text);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                break;
            default:
                player.sendMessage("§c未知命令参数! 使用/hello查看帮助");
                break;
        }
    }

    private void handleRandomTpCommand(Player player, String[] args) {
        int radius = 1000;
        if (args.length > 0) {
            try {
                radius = Integer.parseInt(args[0]);
                if (radius < 100 || radius > 5000) {
                    player.sendMessage("§c范围必须在100-5000之间！（此插件由邱息编写，是他的第一个插件）");
                    return;
                }
            } catch (NumberFormatException e) {
                player.sendMessage("§c请输入有效的数字！（此插件由邱息编写，是他的第一个插件）");
                return;
            }
        }

        Location loc = getRandomSafeLocation(player.getWorld(), player.getLocation(), radius);
        if (loc == null) {
            player.sendMessage("§c无法找到安全的位置！（此插件由邱息编写，是他的第一个插件）");
            return;
        }

        player.teleport(loc);
        player.sendMessage("§a已随机传送到X: §6" + loc.getBlockX() + " §aY: §6" + loc.getBlockY() + " §aZ: §6" + loc.getBlockZ() + "（此插件由邱息编写，是他的第一个插件）");
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    private Location getRandomSafeLocation(World world, Location center, int radius) {
        for (int i = 0; i < 10; i++) {
            double x = center.getX() + (random.nextDouble() * radius * 2 - radius);
            double z = center.getZ() + (random.nextDouble() * radius * 2 - radius);
            Location loc = new Location(world, x, world.getHighestBlockYAt((int)x, (int)z) + 1, z);

            if (isLocationSafe(loc)) {
                return loc;
            }
        }
        return null;
    }

    private boolean isLocationSafe(Location loc) {
        if (loc.getBlock().getType().isSolid()) return false;
        if (loc.clone().subtract(0, 1, 0).getBlock().getType().isSolid()) return true;
        return false;
    }

    private void handleWeatherFunCommand(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage("§c你没有权限使用此命令！需要OP权限。");
            return;
        }
        World world = player.getWorld();

        if (args.length == 0) {
            player.sendMessage("§c请指定天气类型: sun, rain, thunder（此插件由邱息编写，是他的第一个插件）");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "sun":
                world.setStorm(false);
                world.setThundering(false);
                world.setWeatherDuration(12000);
                spawnWeatherParticles(player, Particle.VILLAGER_HAPPY);
                player.sendMessage("§a天气已设置为晴天！（此插件由邱息编写，是他的第一个插件）");
                break;
            case "rain":
                world.setStorm(true);
                world.setThundering(false);
                world.setWeatherDuration(12000);
                spawnWeatherParticles(player, Particle.WATER_DROP);
                player.sendMessage("§a天气已设置为下雨！（此插件由邱息编写，是他的第一个插件）");
                break;
            case "thunder":
                world.setStorm(true);
                world.setThundering(true);
                world.setWeatherDuration(12000);
                spawnWeatherParticles(player, Particle.CLOUD);
                player.sendMessage("§a天气已设置为雷暴！（此插件由邱息编写，是他的第一个插件）");
                break;
            default:
                player.sendMessage("§c未知的天气类型！请使用: sun, rain, thunder（此插件由邱息编写，是他的第一个插件）");
        }
    }

    private void spawnWeatherParticles(Player player, Particle particle) {
        Location loc = player.getLocation();
        for (int i = 0; i < 50; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 10;
            double offsetY = (random.nextDouble() - 0.5) * 10;
            double offsetZ = (random.nextDouble() - 0.5) * 10;
            player.getWorld().spawnParticle(particle, loc.getX() + offsetX, loc.getY() + 5 + offsetY, loc.getZ() + offsetZ,
                    1, 0, 0, 0, null);
        }
    }

    private void handleZzCommand(Player player) {
        player.sendMessage("邱息是此插件的作者");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
    }

    private void handleQxHelpCommand(Player player) {
        player.sendMessage("§6===== 插件功能列表 =====");
        player.sendMessage("§a/hello §f- 显示大型标题，支持参数: upmaintitle+, uptitle+, maintitle+, title+");
        player.sendMessage("§a/randomtp [范围] §f- 随机传送（范围100-5000）");
        player.sendMessage("§a/weatherfun [sun/rain/thunder] §f- 切换天气效果");
        player.sendMessage("§a/emoji [表情名称] §f- 发送大型表情（可用: heart, star, smile, cool, fire）");
        player.sendMessage("§a/zz §f- 显示作者信息");
        player.sendMessage("§a/qxhelp §f- 查看所有功能");
        player.sendMessage("§6=======================");
        player.sendMessage("（此插件由邱息编写，是他的第一个插件）");
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
    }

    private void handleEmojiCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§c请指定表情名称！可用: §6" + String.join("§f, §6", emojiMap.keySet()) + "（此插件由邱息编写，是他的第一个插件）");
            return;
        }

        String emojiName = args[0].toLowerCase();
        if (!emojiMap.containsKey(emojiName)) {
            player.sendMessage("§c未知的表情！可用: §6" + String.join("§f, §6", emojiMap.keySet()) + "（此插件由邱息编写，是他的第一个插件）");
            return;
        }

        // 向所有玩家发送大型表情符号
        Bukkit.broadcastMessage("\n§6" + player.getName() + " §f发送了一个表情:§r\n" + emojiMap.get(emojiName) + "\n（此插件由邱息编写，是他的第一个插件）");
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("hello")) {
            if (args.length == 1) {
                return Arrays.asList("upmaintitle", "uptitle", "maintitle", "title");
            }
        }
        return null;
    }
}