package org.openqa.selenium.remote.server;

import java.util.Map;

public abstract interface JsonParametersAware
{
  public abstract void setJsonParameters(Map<String, Object> paramMap)
    throws Exception;
}
