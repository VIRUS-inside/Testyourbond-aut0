package org.openqa.selenium.phantomjs;

import org.openqa.selenium.remote.HttpCommandExecutor;






































public class MultiSessionCommandExecutor
  extends HttpCommandExecutor
{
  public MultiSessionCommandExecutor(PhantomJSDriverService service)
  {
    super(PhantomJSDriver.getCustomCommands(), service.getUrl());
  }
}
