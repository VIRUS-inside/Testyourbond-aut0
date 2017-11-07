package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.server.Session;
















public class GetAvailableLogTypesHandler
  extends WebDriverHandler<Set<String>>
{
  public GetAvailableLogTypesHandler(Session session)
  {
    super(session);
  }
  
  public Set<String> call() throws Exception
  {
    return Sets.union(
      getDriver().manage().logs().getAvailableLogTypes(), ImmutableSet.of("server"));
  }
  
  public String toString()
  {
    return String.format("[fetching available log types]", new Object[0]);
  }
}
