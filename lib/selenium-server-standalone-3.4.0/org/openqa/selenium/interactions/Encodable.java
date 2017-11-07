package org.openqa.selenium.interactions;

import java.util.Map;

public abstract interface Encodable
{
  public abstract Map<String, Object> encode();
}
