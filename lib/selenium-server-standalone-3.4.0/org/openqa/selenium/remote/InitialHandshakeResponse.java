package org.openqa.selenium.remote;

import com.google.common.base.Preconditions;
import java.time.Duration;
import java.util.Map;

















class InitialHandshakeResponse
{
  private final Duration requestDuration;
  private final int httpStatusCode;
  private final Map<?, ?> data;
  
  public InitialHandshakeResponse(long millis, int statusCode, Map<?, ?> data)
  {
    requestDuration = Duration.ofMillis(millis);
    httpStatusCode = statusCode;
    this.data = ((Map)Preconditions.checkNotNull(data));
  }
  
  public Duration getRequestDuration() {
    return requestDuration;
  }
  
  public int getStatusCode() {
    return httpStatusCode;
  }
  
  public Map<?, ?> getData() {
    return data;
  }
}
