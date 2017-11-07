package org.seleniumhq.jetty9.io;

import java.io.Closeable;
import java.nio.ByteBuffer;

public abstract interface Connection
  extends Closeable
{
  public abstract void addListener(Listener paramListener);
  
  public abstract void removeListener(Listener paramListener);
  
  public abstract void onOpen();
  
  public abstract void onClose();
  
  public abstract EndPoint getEndPoint();
  
  public abstract void close();
  
  public abstract boolean onIdleExpired();
  
  public abstract long getMessagesIn();
  
  public abstract long getMessagesOut();
  
  public abstract long getBytesIn();
  
  public abstract long getBytesOut();
  
  public abstract long getCreatedTimeStamp();
  
  public static abstract interface Listener
  {
    public abstract void onOpened(Connection paramConnection);
    
    public abstract void onClosed(Connection paramConnection);
    
    public static class Adapter
      implements Connection.Listener
    {
      public Adapter() {}
      
      public void onOpened(Connection connection) {}
      
      public void onClosed(Connection connection) {}
    }
  }
  
  public static abstract interface UpgradeTo
  {
    public abstract void onUpgradeTo(ByteBuffer paramByteBuffer);
  }
  
  public static abstract interface UpgradeFrom
  {
    public abstract ByteBuffer onUpgradeFrom();
  }
}
