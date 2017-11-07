package org.eclipse.jetty.websocket.api;












public class WebSocketPolicy
{
  private static final int KB = 1024;
  










  public static WebSocketPolicy newClientPolicy()
  {
    return new WebSocketPolicy(WebSocketBehavior.CLIENT);
  }
  
  public static WebSocketPolicy newServerPolicy()
  {
    return new WebSocketPolicy(WebSocketBehavior.SERVER);
  }
  







  private int maxTextMessageSize = 65536;
  







  private int maxTextMessageBufferSize = 32768;
  







  private int maxBinaryMessageSize = 65536;
  







  private int maxBinaryMessageBufferSize = 32768;
  





  private long asyncWriteTimeout = 60000L;
  





  private long idleTimeout = 300000L;
  





  private int inputBufferSize = 4096;
  

  private final WebSocketBehavior behavior;
  


  public WebSocketPolicy(WebSocketBehavior behavior)
  {
    this.behavior = behavior;
  }
  
  private void assertLessThan(String name, long size, String otherName, long otherSize)
  {
    if (size > otherSize)
    {
      throw new IllegalArgumentException(String.format("%s [%d] must be less than %s [%d]", new Object[] { name, Long.valueOf(size), otherName, Long.valueOf(otherSize) }));
    }
  }
  
  private void assertGreaterThan(String name, long size, long minSize)
  {
    if (size < minSize)
    {
      throw new IllegalArgumentException(String.format("%s [%d] must be a greater than or equal to " + minSize, new Object[] { name, Long.valueOf(size) }));
    }
  }
  
  public void assertValidBinaryMessageSize(int requestedSize)
  {
    if (maxBinaryMessageSize > 0)
    {

      if (requestedSize > maxBinaryMessageSize)
      {
        throw new MessageTooLargeException("Binary message size [" + requestedSize + "] exceeds maximum size [" + maxBinaryMessageSize + "]");
      }
    }
  }
  
  public void assertValidTextMessageSize(int requestedSize)
  {
    if (maxTextMessageSize > 0)
    {

      if (requestedSize > maxTextMessageSize)
      {
        throw new MessageTooLargeException("Text message size [" + requestedSize + "] exceeds maximum size [" + maxTextMessageSize + "]");
      }
    }
  }
  
  public WebSocketPolicy clonePolicy()
  {
    WebSocketPolicy clone = new WebSocketPolicy(behavior);
    idleTimeout = idleTimeout;
    maxTextMessageSize = maxTextMessageSize;
    maxTextMessageBufferSize = maxTextMessageBufferSize;
    maxBinaryMessageSize = maxBinaryMessageSize;
    maxBinaryMessageBufferSize = maxBinaryMessageBufferSize;
    inputBufferSize = inputBufferSize;
    asyncWriteTimeout = asyncWriteTimeout;
    return clone;
  }
  







  public long getAsyncWriteTimeout()
  {
    return asyncWriteTimeout;
  }
  
  public WebSocketBehavior getBehavior()
  {
    return behavior;
  }
  





  public long getIdleTimeout()
  {
    return idleTimeout;
  }
  







  public int getInputBufferSize()
  {
    return inputBufferSize;
  }
  





  public int getMaxBinaryMessageBufferSize()
  {
    return maxBinaryMessageBufferSize;
  }
  







  public int getMaxBinaryMessageSize()
  {
    return maxBinaryMessageSize;
  }
  





  public int getMaxTextMessageBufferSize()
  {
    return maxTextMessageBufferSize;
  }
  







  public int getMaxTextMessageSize()
  {
    return maxTextMessageSize;
  }
  








  public void setAsyncWriteTimeout(long ms)
  {
    assertLessThan("AsyncWriteTimeout", ms, "IdleTimeout", idleTimeout);
    asyncWriteTimeout = ms;
  }
  






  public void setIdleTimeout(long ms)
  {
    assertGreaterThan("IdleTimeout", ms, 0L);
    idleTimeout = ms;
  }
  






  public void setInputBufferSize(int size)
  {
    assertGreaterThan("InputBufferSize", size, 1L);
    assertLessThan("InputBufferSize", size, "MaxTextMessageBufferSize", maxTextMessageBufferSize);
    assertLessThan("InputBufferSize", size, "MaxBinaryMessageBufferSize", maxBinaryMessageBufferSize);
    
    inputBufferSize = size;
  }
  








  public void setMaxBinaryMessageBufferSize(int size)
  {
    assertGreaterThan("MaxBinaryMessageBufferSize", size, 1L);
    
    maxBinaryMessageBufferSize = size;
  }
  








  public void setMaxBinaryMessageSize(int size)
  {
    assertGreaterThan("MaxBinaryMessageSize", size, 1L);
    
    maxBinaryMessageSize = size;
  }
  








  public void setMaxTextMessageBufferSize(int size)
  {
    assertGreaterThan("MaxTextMessageBufferSize", size, 1L);
    
    maxTextMessageBufferSize = size;
  }
  








  public void setMaxTextMessageSize(int size)
  {
    assertGreaterThan("MaxTextMessageSize", size, 1L);
    
    maxTextMessageSize = size;
  }
  

  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("WebSocketPolicy@").append(Integer.toHexString(hashCode()));
    builder.append("[behavior=").append(behavior);
    builder.append(",maxTextMessageSize=").append(maxTextMessageSize);
    builder.append(",maxTextMessageBufferSize=").append(maxTextMessageBufferSize);
    builder.append(",maxBinaryMessageSize=").append(maxBinaryMessageSize);
    builder.append(",maxBinaryMessageBufferSize=").append(maxBinaryMessageBufferSize);
    builder.append(",asyncWriteTimeout=").append(asyncWriteTimeout);
    builder.append(",idleTimeout=").append(idleTimeout);
    builder.append(",inputBufferSize=").append(inputBufferSize);
    builder.append("]");
    return builder.toString();
  }
}
