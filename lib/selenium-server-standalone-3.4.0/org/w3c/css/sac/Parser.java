package org.w3c.css.sac;

import java.io.IOException;
import java.util.Locale;

public abstract interface Parser
{
  public abstract void setLocale(Locale paramLocale)
    throws CSSException;
  
  public abstract void setDocumentHandler(DocumentHandler paramDocumentHandler);
  
  public abstract void setSelectorFactory(SelectorFactory paramSelectorFactory);
  
  public abstract void setConditionFactory(ConditionFactory paramConditionFactory);
  
  public abstract void setErrorHandler(ErrorHandler paramErrorHandler);
  
  public abstract void parseStyleSheet(InputSource paramInputSource)
    throws CSSException, IOException;
  
  public abstract void parseStyleSheet(String paramString)
    throws CSSException, IOException;
  
  public abstract void parseStyleDeclaration(InputSource paramInputSource)
    throws CSSException, IOException;
  
  public abstract void parseRule(InputSource paramInputSource)
    throws CSSException, IOException;
  
  public abstract String getParserVersion();
  
  public abstract SelectorList parseSelectors(InputSource paramInputSource)
    throws CSSException, IOException;
  
  public abstract LexicalUnit parsePropertyValue(InputSource paramInputSource)
    throws CSSException, IOException;
  
  public abstract boolean parsePriority(InputSource paramInputSource)
    throws CSSException, IOException;
}
