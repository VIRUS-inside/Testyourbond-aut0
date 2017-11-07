package org.openqa.grid.internal;

public enum SessionTerminationReason
{
  TIMEOUT,  BROWSER_TIMEOUT,  ORPHAN,  CLIENT_STOPPED_SESSION,  CLIENT_GONE,  FORWARDING_TO_NODE_FAILED,  CREATIONFAILED,  PROXY_REREGISTRATION,  SO_TIMEOUT;
  
  private SessionTerminationReason() {}
}
