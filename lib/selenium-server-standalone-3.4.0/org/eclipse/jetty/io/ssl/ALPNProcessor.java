package org.eclipse.jetty.io.ssl;

import java.util.List;
import javax.net.ssl.SSLEngine;




















public abstract interface ALPNProcessor
{
  public static abstract interface Server
  {
    public static final Server NOOP = new Server() {};
    


    public void configure(SSLEngine sslEngine) {}
  }
  


  public static abstract interface Client
  {
    public static final Client NOOP = new Client() {};
    
    public void configure(SSLEngine sslEngine, List<String> protocols) {}
    
    public void process(SSLEngine sslEngine) {}
  }
}
