package org.eclipse.jetty.websocket.client.io;

import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;

public abstract interface UpgradeListener
{
  public abstract void onHandshakeRequest(UpgradeRequest paramUpgradeRequest);
  
  public abstract void onHandshakeResponse(UpgradeResponse paramUpgradeResponse);
}
