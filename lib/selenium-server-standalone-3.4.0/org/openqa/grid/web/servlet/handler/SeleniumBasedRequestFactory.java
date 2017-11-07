package org.openqa.grid.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import org.openqa.grid.internal.Registry;

public abstract interface SeleniumBasedRequestFactory
{
  public abstract SeleniumBasedRequest createFromRequest(HttpServletRequest paramHttpServletRequest, Registry paramRegistry);
}
