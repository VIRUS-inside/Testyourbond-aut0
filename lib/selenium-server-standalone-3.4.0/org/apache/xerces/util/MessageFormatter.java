package org.apache.xerces.util;

import java.util.Locale;
import java.util.MissingResourceException;

public abstract interface MessageFormatter
{
  public abstract String formatMessage(Locale paramLocale, String paramString, Object[] paramArrayOfObject)
    throws MissingResourceException;
}
