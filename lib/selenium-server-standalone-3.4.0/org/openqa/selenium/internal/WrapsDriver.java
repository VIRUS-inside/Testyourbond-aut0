package org.openqa.selenium.internal;

import org.openqa.selenium.WebDriver;

public abstract interface WrapsDriver
{
  public abstract WebDriver getWrappedDriver();
}
