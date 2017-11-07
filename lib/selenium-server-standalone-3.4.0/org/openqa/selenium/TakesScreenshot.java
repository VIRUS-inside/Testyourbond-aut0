package org.openqa.selenium;

public abstract interface TakesScreenshot
{
  public abstract <X> X getScreenshotAs(OutputType<X> paramOutputType)
    throws WebDriverException;
}
