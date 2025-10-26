package cx.aze.randomMOTD;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class RandomMOTD extends JavaPlugin implements CommandExecutor {

    private File motdFile;
    private final List<String> motds = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        motdFile = new File(getDataFolder(), "motds.txt");
        if (!motdFile.exists()) {
            saveResource("motds.txt", false);
            getLogger().info("Created default motds.txt");
        }

        loadMotds();

        getServer().getPluginManager().registerEvents(new MotdListener(this), this);
        getCommand("rmotd").setExecutor(this);

        getLogger().info("RandomMOTD has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RandomMOTD has been disabled!");
    }

    public void loadMotds() {
        motds.clear();
        try {
            motds.addAll(Files.readAllLines(motdFile.toPath()));
            motds.removeIf(String::isBlank);
            getLogger().info("Loaded " + motds.size() + " MOTDs from motds.txt");
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to load motds.txt", e);
        }
    }

    public String getRandomMotd() {
        if (motds.isEmpty()) return "&cNo MOTDs found in motds.txt!";
        return motds.get((int) (Math.random() * motds.size()));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("randommotd.reload")) {
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
                return true;
            }
            reloadConfig();
            loadMotds();
            sender.sendMessage("§aRandomMOTD reloaded!");
            return true;
        }
        sender.sendMessage("§eUsage: /rmotd reload");
        return true;
    }
}
