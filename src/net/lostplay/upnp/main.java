package net.lostplay.upnp;

import org.bukkit.plugin.java.JavaPlugin;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Logger;
public class main
        extends JavaPlugin
{
    private Logger log = Logger.getLogger("Minecraft");
    private boolean failed = false;
    private UpnpService upnp;

    public void onEnable()
    {
        this.log.info("[AutoUpnp] AutoUpnp v1.0 by firestorm942 starting!");
        this.log.info("[AutoUpnp] Attempting to forward port: " + getServer().getPort());

        forwardPort(getServer().getPort());
    }

    private void forwardPort(int port)
    {
        String address = null;
        try
        {
            address = getInternalIP();
        }
        catch (SocketException e)
        {
            this.log.info("[AutoUpnp] Failed to forward port. :-( Stack trace: ");
            e.printStackTrace();
            this.failed = true;
            disable();
        }
        PortMapping pm = new PortMapping(port, address, PortMapping.Protocol.TCP, "Minecraft");
        this.upnp = new UpnpServiceImpl(new PortMappingListener(pm)
        {
            public void handleFailureMessage(String s)
            {
                main.this.log.severe("[AutoPortForward] Failed to forward port. :-( Error message: " + s);
                main.this.failed = true;
                main.this.disable();
            }
        });
        this.upnp.getControlPoint().search();
    }

    private String getInternalIP()
            throws SocketException
    {
        Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
        while (nics.hasMoreElements())
        {
            NetworkInterface nic = nics.nextElement();
            if (!nic.isLoopback()) {
                return (nic.getInetAddresses().nextElement()).getHostAddress();
            }
        }
        return null;
    }

    private void disable()
    {
        getServer().getPluginManager().disablePlugin(this);
    }

    public void onDisable()
    {
        this.log.info("[AutoUpnp] Disabling..");
        if (!this.failed)
        {
            String address = null;
            try
            {
                address = getInternalIP();
            }
            catch (SocketException e)
            {
                this.log.info("[AutoUpnp] Failed to remove port mapping. :-( Stack trace: ");
                e.printStackTrace();
                this.failed = true;
                disable();
            }
            this.log.info("[AutoUpnp] Removing port mapping");
            this.upnp.shutdown();
        }
    }
}
