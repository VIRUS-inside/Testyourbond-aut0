package org.openqa.selenium.support.ui;

import com.google.common.base.Function;
import org.openqa.selenium.WebDriver;

public abstract interface ExpectedCondition<T>
  extends Function<WebDriver, T>
{}
