package org.openqa.selenium.remote;

import com.google.gson.JsonSyntaxException;
import org.openqa.selenium.WebDriverException;
















public class JsonException
  extends WebDriverException
{
  public JsonException(JsonSyntaxException e)
  {
    super(e);
  }
  
  public JsonException(JsonSyntaxException e, Object text) { super("JSON command: " + text, e); }
}
