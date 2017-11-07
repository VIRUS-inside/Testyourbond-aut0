package org.openqa.selenium.remote;

import java.util.Map;

public abstract interface ExecuteMethod
{
  public abstract Object execute(String paramString, Map<String, ?> paramMap);
}
