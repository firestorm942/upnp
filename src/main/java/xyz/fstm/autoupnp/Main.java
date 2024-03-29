package xyz.fstm.autoupnp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jupnp.UpnpService;
import org.jupnp.UpnpServiceConfiguration;
import org.jupnp.UpnpServiceImpl;
import org.jupnp.support.igd.PortMappingListener;
import org.jupnp.support.model.PortMapping;
import static java.lang.String.*;

public class Main
        extends JavaPlugin {

    public void onEnable() {
        getLogger();
        String protocol;
        String name;
        String ip;
        int port;
        FileConfiguration config = getConfig();
        config.addDefault("settings.protocol", "TCP");
        config.addDefault("settings.name", "minecraft");
        config.addDefault("settings.ipaddress", "0.0.0.0");
        config.addDefault("settings.port", 25565);
        config.options().copyDefaults(true);
        protocol = config.getString("settings.protocol");
        name = config.getString("settings.name");
        ip = config.getString("settings.ipaddress");
        port = config.getInt("settings.port");
        saveConfig();
        assert protocol != null;
        openPort(ip, port, name, protocol);
    }
    private void openPort(String ip, int port, String name, String protocol) {
        getLogger().info(format("Attempting to forward port: %d", getServer().getPort()));
        PortMapping mapping;
        UpnpService upnpport;
        if (protocol.equals("TCP")) {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.TCP, name);

        } else if (protocol.equals("UDP")) {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.UDP, name);

        } else {
            mapping = new PortMapping(port, ip, PortMapping.Protocol.TCP, name);

        }
        //upnpService = new UpnpServiceImpl((UpnpServiceConfiguration) new PortMappingListener(mapping));
        // This Line is causing a casting error. I do not know how to fix it. Help me!
        upnpport = new UpnpServiceImpl((UpnpServiceConfiguration) new PortMappingListener(mapping));

        upnpport.getControlPoint().search();
    }


    public void onDisable() {
        getLogger().info("Removing port mapping.");
        new UpnpService.Shutdown();
    }
}
