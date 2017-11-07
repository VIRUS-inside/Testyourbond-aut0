package org.openqa.grid.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import org.openqa.grid.internal.Registry;














public class WebDriverRequestFactory
  implements SeleniumBasedRequestFactory
{
  public WebDriverRequestFactory() {}
  
  public SeleniumBasedRequest createFromRequest(HttpServletRequest request, Registry registry)
  {
    String path = request.getServletPath();
    if (!"/wd/hub".equals(path)) {
      return null;
    }
    return new WebDriverRequest(request, registry);
  }
}
