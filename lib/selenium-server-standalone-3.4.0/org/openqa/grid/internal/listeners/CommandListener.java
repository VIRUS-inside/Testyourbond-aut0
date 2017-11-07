package org.openqa.grid.internal.listeners;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.internal.TestSession;

public abstract interface CommandListener
{
  public abstract void beforeCommand(TestSession paramTestSession, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
  
  public abstract void afterCommand(TestSession paramTestSession, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
}
