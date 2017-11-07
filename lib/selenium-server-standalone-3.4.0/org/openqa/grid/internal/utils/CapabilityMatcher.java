package org.openqa.grid.internal.utils;

import java.util.Map;

public abstract interface CapabilityMatcher
{
  public abstract boolean matches(Map<String, Object> paramMap1, Map<String, Object> paramMap2);
}
