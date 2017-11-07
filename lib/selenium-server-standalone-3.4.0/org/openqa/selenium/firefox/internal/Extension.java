package org.openqa.selenium.firefox.internal;

import java.io.File;
import java.io.IOException;

public abstract interface Extension
{
  public abstract void writeTo(File paramFile)
    throws IOException;
}
