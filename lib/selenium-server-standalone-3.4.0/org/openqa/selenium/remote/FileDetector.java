package org.openqa.selenium.remote;

import java.io.File;

public abstract interface FileDetector
{
  public abstract File getLocalFile(CharSequence... paramVarArgs);
}
