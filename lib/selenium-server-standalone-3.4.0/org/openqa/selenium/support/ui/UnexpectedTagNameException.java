package org.openqa.selenium.support.ui;

import org.openqa.selenium.WebDriverException;















public class UnexpectedTagNameException
  extends WebDriverException
{
  public UnexpectedTagNameException(String expectedTagName, String actualTagName)
  {
    super(String.format("Element should have been \"%s\" but was \"%s\"", new Object[] { expectedTagName, actualTagName }));
  }
}
