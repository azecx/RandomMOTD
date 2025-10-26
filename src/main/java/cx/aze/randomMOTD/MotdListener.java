package cx.aze.randomMOTD;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {

    private final RandomMOTD plugin;

    public MotdListener(RandomMOTD plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        String motdTemplate = plugin.getConfig().getString("motd", "&cNo MOTD in config!");
        String randomMotd = plugin.getRandomMotd();

        String motd = motdTemplate.replace("%random%", randomMotd);
        motd = motd.replace("\\n", "\n");
        motd = ChatColor.translateAlternateColorCodes('&', motd);

        event.setMotd(motd);
    }
}
