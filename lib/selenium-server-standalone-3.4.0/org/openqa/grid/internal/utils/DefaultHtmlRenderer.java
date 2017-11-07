package org.openqa.grid.internal.utils;

import java.util.Map;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.internal.TestSlot;






















public class DefaultHtmlRenderer
  implements HtmlRenderer
{
  private RemoteProxy proxy;
  
  private DefaultHtmlRenderer() {}
  
  public DefaultHtmlRenderer(RemoteProxy proxy)
  {
    this.proxy = proxy;
  }
  
  public String renderSummary() {
    StringBuilder builder = new StringBuilder();
    builder.append("<fieldset>");
    builder.append("<legend>").append(proxy.getClass().getSimpleName()).append("</legend>");
    builder.append("listening on ").append(proxy.getRemoteHost());
    int inSec; if (proxy.getTimeOut() > 0) {
      inSec = proxy.getTimeOut() / 1000;
      builder.append("test session time out after ").append(inSec).append(" sec.");
    }
    

    builder.append("<br>Supports up to <b>").append(proxy.getMaxNumberOfConcurrentTestSessions()).append("</b> concurrent tests from : </u><br>");
    
    for (TestSlot slot : proxy.getTestSlots()) {
      builder.append(slot.getCapabilities().containsKey("browserName") ? slot.getCapabilities().get("browserName") : slot
        .getCapabilities().get("applicationName"));
      builder.append("protocol:" + slot.getProtocol() + "<br>");
      TestSession session = slot.getSession();
      builder.append("(busy, session " + session + ")");
      builder.append("<br>");
    }
    builder.append("</fieldset>");
    return builder.toString();
  }
}
