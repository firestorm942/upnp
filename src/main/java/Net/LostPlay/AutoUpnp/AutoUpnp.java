package Net.LostPlay.AutoUpnp;

import org.bukkit.plugin.java.JavaPlugin;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.support.model.PortMapping;

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
        getConfig();
        getConfig().addDefault("settings.protocol", String.valueOf("TCP"));
        getConfig().addDefault("settings.name", String.valueOf("minecraft"));
        getConfig().addDefault("settings.ipaddress", String.valueOf("0.0.0.0"));
        getConfig().addDefault("settings.port", Integer.valueOf(25565));
        getConfig().options().copyDefaults(true);
        protocol = getConfig().getString("settings.protocol");
        name = getConfig().getString("settings.name");
        ip = getConfig().getString("settings.ipaddress");
        port = getConfig().getInt("settings.port");
        saveConfig();
        getLogger().info("[AutoUpnp] AutoUpnp v2.0 by firestorm942 starting!");
        openPort(ip, port, name, protocol);
    }

    private void openPort(String ip, int port, String name, String protocol)
    {
        getLogger().info("[AutoUpnp] Attempting to forward port: " + getServer().getPort());
        PortMapping mapping;
        UpnpService upnpService;
        if (protocol.equals("TCP")) {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.TCP, name);
        } else if (protocol.equals("UDP")) {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.UDP, name);
        } else {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.TCP, name);
        }
        upnpService = new org.fourthline.cling.UpnpServiceImpl(new org.fourthline.cling.support.igd.PortMappingListener(mapping));
        upnpService.getControlPoint().search();
    }


    public void onDisable()
    {
        getLogger().info("[AutoUpnp] Removing port mapping.");
        new UpnpService.Shutdown();
    }
}
