package org.openqa.selenium.chrome;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.service.DriverCommandExecutor;
import org.openqa.selenium.remote.service.DriverService;






















class ChromeDriverCommandExecutor
  extends DriverCommandExecutor
{
  private static final Map<String, CommandInfo> CHROME_COMMAND_NAME_TO_URL = ImmutableMap.of("launchApp", new CommandInfo("/session/:sessionId/chromium/launch_app", HttpMethod.POST));
  

  public ChromeDriverCommandExecutor(DriverService service)
  {
    super(service, CHROME_COMMAND_NAME_TO_URL);
  }
}
