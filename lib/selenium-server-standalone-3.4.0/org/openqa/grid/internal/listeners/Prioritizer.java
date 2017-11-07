package org.openqa.grid.internal.listeners;

import java.util.Map;

public abstract interface Prioritizer
{
  public abstract int compareTo(Map<String, Object> paramMap1, Map<String, Object> paramMap2);
}
