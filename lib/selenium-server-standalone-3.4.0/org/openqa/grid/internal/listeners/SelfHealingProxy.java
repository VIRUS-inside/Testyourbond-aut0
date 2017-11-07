package org.openqa.grid.internal.listeners;

import java.util.List;
import org.openqa.grid.common.exception.RemoteException;

public abstract interface SelfHealingProxy
{
  public abstract void startPolling();
  
  public abstract void stopPolling();
  
  public abstract void addNewEvent(RemoteException paramRemoteException);
  
  public abstract void onEvent(List<RemoteException> paramList, RemoteException paramRemoteException);
}
