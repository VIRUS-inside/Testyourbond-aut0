package org.apache.http.impl.io;

import org.apache.http.io.HttpTransportMetrics;
































public class HttpTransportMetricsImpl
  implements HttpTransportMetrics
{
  private long bytesTransferred = 0L;
  

  public HttpTransportMetricsImpl() {}
  

  public long getBytesTransferred()
  {
    return bytesTransferred;
  }
  
  public void setBytesTransferred(long count) {
    bytesTransferred = count;
  }
  
  public void incrementBytesTransferred(long count) {
    bytesTransferred += count;
  }
  
  public void reset()
  {
    bytesTransferred = 0L;
  }
}
