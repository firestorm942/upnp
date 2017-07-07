package Net.LostPlay.AutoUpnp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;
import static java.lang.String.*;

public class AutoUpnp
        extends JavaPlugin
{
    public void onEnable()
    {
        getLogger();
        String protocol;
        String name;
        String ip;
        int port;
        FileConfiguration config = getConfig();
        config.addDefault("settings.protocol", valueOf("TCP"));
        config.addDefault("settings.name", valueOf("minecraft"));
        config.addDefault("settings.ipaddress", valueOf("0.0.0.0"));
        config.addDefault("settings.port", 25565);
        config.options().copyDefaults(true);
        protocol = config.getString("settings.protocol");
        name = config.getString("settings.name");
        ip = config.getString("settings.ipaddress");
        port = config.getInt("settings.port");
        saveConfig();
        openPort(ip, port, name, protocol);
    }

    private void openPort(String ip, int port, String name, String protocol)
    {
        getLogger().info(format("[AutoUpnp] Attempting to forward port: %d", getServer().getPort()));
        PortMapping mapping;
        UpnpService upnpService;
        if (protocol.equals("TCP")) {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.TCP, name);

        } else if (protocol.equals("UDP")) {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.UDP, name);

        } else {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.TCP, name);

        }
        upnpService = new UpnpServiceImpl(new PortMappingListener(mapping));
        upnpService.getControlPoint().search();
    }


    public void onDisable()
    {
        getLogger().info("[AutoUpnp] Removing port mapping.");
        new UpnpService.Shutdown();
    }
}
