package org.openqa.grid.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import org.openqa.grid.internal.Registry;














public class LegacySeleniumRequestFactory
  implements SeleniumBasedRequestFactory
{
  public LegacySeleniumRequestFactory() {}
  
  public SeleniumBasedRequest createFromRequest(HttpServletRequest request, Registry registry)
  {
    if (!"/selenium-server/driver".equals(request.getServletPath())) {
      return null;
    }
    return new LegacySeleniumRequest(request, registry);
  }
}
