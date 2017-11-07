package org.openqa.grid.internal;

import com.google.gson.JsonObject;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.common.SeleniumProtocol;
import org.openqa.grid.internal.utils.CapabilityMatcher;
import org.openqa.grid.internal.utils.HtmlRenderer;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.selenium.remote.internal.HttpClientFactory;

































public abstract interface RemoteProxy
  extends Comparable<RemoteProxy>
{
  public TestSlot createTestSlot(SeleniumProtocol protocol, Map<String, Object> capabilities)
  {
    return new TestSlot(this, protocol, capabilities);
  }
  
  public abstract List<TestSlot> getTestSlots();
  
  public abstract Registry getRegistry();
  
  public abstract CapabilityMatcher getCapabilityHelper();
  
  public abstract void setupTimeoutListener();
  
  public abstract String getId();
  
  public abstract void teardown();
  
  public abstract GridNodeConfiguration getConfig();
  
  public abstract RegistrationRequest getOriginalRegistrationRequest();
  
  public abstract int getMaxNumberOfConcurrentTestSessions();
  
  public abstract URL getRemoteHost();
  
  public abstract TestSession getNewSession(Map<String, Object> paramMap);
  
  public abstract int getTotalUsed();
  
  public abstract HtmlRenderer getHtmlRender();
  
  public abstract int getTimeOut();
  
  public abstract HttpClientFactory getHttpClientFactory();
  
  public abstract JsonObject getStatus();
  
  public abstract boolean hasCapability(Map<String, Object> paramMap);
  
  public abstract boolean isBusy();
  
  public abstract float getResourceUsageInPercent();
  
  public abstract long getLastSessionStart();
}
