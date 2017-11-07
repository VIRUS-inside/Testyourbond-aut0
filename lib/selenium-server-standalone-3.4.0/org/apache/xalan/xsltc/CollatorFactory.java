package org.apache.xalan.xsltc;

import java.text.Collator;
import java.util.Locale;

public abstract interface CollatorFactory
{
  public abstract Collator getCollator(String paramString1, String paramString2);
  
  public abstract Collator getCollator(Locale paramLocale);
}
