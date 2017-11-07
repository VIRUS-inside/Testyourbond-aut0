package org.apache.http.config;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class SocketConfig
  implements Cloneable
{
  public static final SocketConfig DEFAULT = new Builder().build();
  
  private final int soTimeout;
  
  private final boolean soReuseAddress;
  
  private final int soLinger;
  
  private final boolean soKeepAlive;
  
  private final boolean tcpNoDelay;
  
  private final int sndBufSize;
  
  private final int rcvBufSize;
  
  private final int backlogSize;
  

  SocketConfig(int soTimeout, boolean soReuseAddress, int soLinger, boolean soKeepAlive, boolean tcpNoDelay, int sndBufSize, int rcvBufSize, int backlogSize)
  {
    this.soTimeout = soTimeout;
    this.soReuseAddress = soReuseAddress;
    this.soLinger = soLinger;
    this.soKeepAlive = soKeepAlive;
    this.tcpNoDelay = tcpNoDelay;
    this.sndBufSize = sndBufSize;
    this.rcvBufSize = rcvBufSize;
    this.backlogSize = backlogSize;
  }
  








  public int getSoTimeout()
  {
    return soTimeout;
  }
  









  public boolean isSoReuseAddress()
  {
    return soReuseAddress;
  }
  









  public int getSoLinger()
  {
    return soLinger;
  }
  









  public boolean isSoKeepAlive()
  {
    return soKeepAlive;
  }
  









  public boolean isTcpNoDelay()
  {
    return tcpNoDelay;
  }
  










  public int getSndBufSize()
  {
    return sndBufSize;
  }
  










  public int getRcvBufSize()
  {
    return rcvBufSize;
  }
  








  public int getBacklogSize()
  {
    return backlogSize;
  }
  
  protected SocketConfig clone() throws CloneNotSupportedException
  {
    return (SocketConfig)super.clone();
  }
  
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("[soTimeout=").append(soTimeout).append(", soReuseAddress=").append(soReuseAddress).append(", soLinger=").append(soLinger).append(", soKeepAlive=").append(soKeepAlive).append(", tcpNoDelay=").append(tcpNoDelay).append(", sndBufSize=").append(sndBufSize).append(", rcvBufSize=").append(rcvBufSize).append(", backlogSize=").append(backlogSize).append("]");
    







    return builder.toString();
  }
  
  public static Builder custom() {
    return new Builder();
  }
  
  public static Builder copy(SocketConfig config) {
    Args.notNull(config, "Socket config");
    return new Builder().setSoTimeout(config.getSoTimeout()).setSoReuseAddress(config.isSoReuseAddress()).setSoLinger(config.getSoLinger()).setSoKeepAlive(config.isSoKeepAlive()).setTcpNoDelay(config.isTcpNoDelay()).setSndBufSize(config.getSndBufSize()).setRcvBufSize(config.getRcvBufSize()).setBacklogSize(config.getBacklogSize());
  }
  

  public static class Builder
  {
    private int soTimeout;
    
    private boolean soReuseAddress;
    
    private int soLinger;
    
    private boolean soKeepAlive;
    
    private boolean tcpNoDelay;
    
    private int sndBufSize;
    
    private int rcvBufSize;
    private int backlogSize;
    
    Builder()
    {
      soLinger = -1;
      tcpNoDelay = true;
    }
    
    public Builder setSoTimeout(int soTimeout) {
      this.soTimeout = soTimeout;
      return this;
    }
    
    public Builder setSoReuseAddress(boolean soReuseAddress) {
      this.soReuseAddress = soReuseAddress;
      return this;
    }
    
    public Builder setSoLinger(int soLinger) {
      this.soLinger = soLinger;
      return this;
    }
    
    public Builder setSoKeepAlive(boolean soKeepAlive) {
      this.soKeepAlive = soKeepAlive;
      return this;
    }
    
    public Builder setTcpNoDelay(boolean tcpNoDelay) {
      this.tcpNoDelay = tcpNoDelay;
      return this;
    }
    


    public Builder setSndBufSize(int sndBufSize)
    {
      this.sndBufSize = sndBufSize;
      return this;
    }
    


    public Builder setRcvBufSize(int rcvBufSize)
    {
      this.rcvBufSize = rcvBufSize;
      return this;
    }
    


    public Builder setBacklogSize(int backlogSize)
    {
      this.backlogSize = backlogSize;
      return this;
    }
    
    public SocketConfig build() {
      return new SocketConfig(soTimeout, soReuseAddress, soLinger, soKeepAlive, tcpNoDelay, sndBufSize, rcvBufSize, backlogSize);
    }
  }
}
