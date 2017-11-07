package org.seleniumhq.jetty9.jmx;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import org.seleniumhq.jetty9.util.HostPort;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.component.LifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.ShutdownThread;
























public class ConnectorServer
  extends AbstractLifeCycle
{
  private static final Logger LOG = Log.getLogger(ConnectorServer.class);
  



  JMXConnectorServer _connectorServer;
  



  Registry _registry;
  



  public ConnectorServer(JMXServiceURL serviceURL, String name)
    throws Exception
  {
    this(serviceURL, null, name);
  }
  














  public ConnectorServer(JMXServiceURL svcUrl, Map<String, ?> environment, String name)
    throws Exception
  {
    String urlPath = svcUrl.getURLPath();
    int idx = urlPath.indexOf("rmi://");
    if (idx > 0)
    {
      String hostPort = urlPath.substring(idx + 6, urlPath.indexOf('/', idx + 6));
      String regHostPort = startRegistry(hostPort);
      if (regHostPort != null) {
        urlPath = urlPath.replace(hostPort, regHostPort);
        svcUrl = new JMXServiceURL(svcUrl.getProtocol(), svcUrl.getHost(), svcUrl.getPort(), urlPath);
      }
    }
    MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
    _connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(svcUrl, environment, mbeanServer);
    mbeanServer.registerMBean(_connectorServer, new ObjectName(name));
  }
  





  public void doStart()
    throws Exception
  {
    _connectorServer.start();
    ShutdownThread.register(0, new LifeCycle[] { this });
    
    LOG.info("JMX Remote URL: {}", new Object[] { _connectorServer.getAddress().toString() });
  }
  





  public void doStop()
    throws Exception
  {
    ShutdownThread.deregister(this);
    _connectorServer.stop();
    stopRegistry();
  }
  






  private String startRegistry(String hostPath)
    throws Exception
  {
    HostPort hostPort = new HostPort(hostPath);
    
    String rmiHost = hostPort.getHost();
    int rmiPort = hostPort.getPort(1099);
    

    InetAddress hostAddress = InetAddress.getByName(rmiHost);
    if (hostAddress.isLoopbackAddress())
    {
      if (rmiPort == 0)
      {
        ServerSocket socket = new ServerSocket(0);
        rmiPort = socket.getLocalPort();
        socket.close();

      }
      else
      {
        try
        {
          LocateRegistry.getRegistry(rmiPort).list();
          return null;
        }
        catch (Exception ex)
        {
          LOG.ignore(ex);
        }
      }
      
      _registry = LocateRegistry.createRegistry(rmiPort);
      Thread.sleep(1000L);
      
      rmiHost = HostPort.normalizeHost(InetAddress.getLocalHost().getCanonicalHostName());
      return rmiHost + ':' + Integer.toString(rmiPort);
    }
    
    return null;
  }
  
  private void stopRegistry()
  {
    if (_registry != null)
    {
      try
      {
        UnicastRemoteObject.unexportObject(_registry, true);
      }
      catch (Exception ex)
      {
        LOG.ignore(ex);
      }
    }
  }
}
