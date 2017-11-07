package org.apache.http.impl.bootstrap;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocket;

public abstract interface SSLServerSetupHandler
{
  public abstract void initialize(SSLServerSocket paramSSLServerSocket)
    throws SSLException;
}
