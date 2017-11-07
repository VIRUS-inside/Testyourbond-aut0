package org.openqa.selenium.remote;

import java.io.IOException;

public abstract interface CommandExecutor
{
  public abstract Response execute(Command paramCommand)
    throws IOException;
}
