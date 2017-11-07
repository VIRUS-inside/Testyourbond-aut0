package org.apache.http.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.io.HttpTransportMetrics;


































public class HttpConnectionMetricsImpl
  implements HttpConnectionMetrics
{
  public static final String REQUEST_COUNT = "http.request-count";
  public static final String RESPONSE_COUNT = "http.response-count";
  public static final String SENT_BYTES_COUNT = "http.sent-bytes-count";
  public static final String RECEIVED_BYTES_COUNT = "http.received-bytes-count";
  private final HttpTransportMetrics inTransportMetric;
  private final HttpTransportMetrics outTransportMetric;
  private long requestCount = 0L;
  private long responseCount = 0L;
  


  private Map<String, Object> metricsCache;
  



  public HttpConnectionMetricsImpl(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric)
  {
    this.inTransportMetric = inTransportMetric;
    this.outTransportMetric = outTransportMetric;
  }
  


  public long getReceivedBytesCount()
  {
    if (inTransportMetric != null) {
      return inTransportMetric.getBytesTransferred();
    }
    return -1L;
  }
  

  public long getSentBytesCount()
  {
    if (outTransportMetric != null) {
      return outTransportMetric.getBytesTransferred();
    }
    return -1L;
  }
  

  public long getRequestCount()
  {
    return requestCount;
  }
  
  public void incrementRequestCount() {
    requestCount += 1L;
  }
  
  public long getResponseCount()
  {
    return responseCount;
  }
  
  public void incrementResponseCount() {
    responseCount += 1L;
  }
  
  public Object getMetric(String metricName)
  {
    Object value = null;
    if (metricsCache != null) {
      value = metricsCache.get(metricName);
    }
    if (value == null) {
      if ("http.request-count".equals(metricName)) {
        value = Long.valueOf(requestCount);
      } else if ("http.response-count".equals(metricName)) {
        value = Long.valueOf(responseCount);
      } else { if ("http.received-bytes-count".equals(metricName)) {
          if (inTransportMetric != null) {
            return Long.valueOf(inTransportMetric.getBytesTransferred());
          }
          return null;
        }
        if ("http.sent-bytes-count".equals(metricName)) {
          if (outTransportMetric != null) {
            return Long.valueOf(outTransportMetric.getBytesTransferred());
          }
          return null;
        }
      }
    }
    return value;
  }
  
  public void setMetric(String metricName, Object obj) {
    if (metricsCache == null) {
      metricsCache = new HashMap();
    }
    metricsCache.put(metricName, obj);
  }
  
  public void reset()
  {
    if (outTransportMetric != null) {
      outTransportMetric.reset();
    }
    if (inTransportMetric != null) {
      inTransportMetric.reset();
    }
    requestCount = 0L;
    responseCount = 0L;
    metricsCache = null;
  }
}
