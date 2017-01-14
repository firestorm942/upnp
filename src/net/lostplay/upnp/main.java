package net.lostplay.upnp;

import org.bukkit.plugin.java.JavaPlugin;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.support.model.PortMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;
public class main
        extends JavaPlugin
{
    private static UpnpService upnpService = null;
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
        try {
            getConfig().addDefault("settings.ipaddress", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        getConfig().addDefault("settings.port", Integer.valueOf(25565));
        getConfig().options().copyDefaults(true);
        protocol = getConfig().getString("settings.protocol");
        name = getConfig().getString("settings.name");
        ip = getConfig().getString("settings.ipaddress");
        port = getConfig().getInt("settings.port");
        saveConfig();
        getLogger().info("[AutoUpnp] AutoUpnp v1.0 by firestorm942 starting!");
        getLogger().info("[AutoUpnp] Attempting to forward port: " + getServer().getPort());
        openPort(ip, port, name, protocol);
    }

    private static void openPort(String ip, int port, String name, String protocol)
    {
        PortMapping mapping;
        UpnpService upnpService;
        if (protocol == "TCP") {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.TCP, name);
        } else if (protocol == "UDP") {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.UDP, name);
        } else {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.TCP, name);
        }
        upnpService = new org.fourthline.cling.UpnpServiceImpl(new org.fourthline.cling.registry.RegistryListener[] { new org.fourthline.cling.support.igd.PortMappingListener(mapping) });
        upnpService.getControlPoint().search();
    }


    public void onDisable()
    {
        getLogger().info("[AutoUpnp] Removing port mapping.");
        upnpService.shutdown();
    }
}
