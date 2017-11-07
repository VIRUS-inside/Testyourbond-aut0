package org.w3c.css.sac.helpers;

import org.w3c.css.sac.Parser;






















public class ParserFactory
{
  public ParserFactory() {}
  
  public Parser makeParser()
    throws ClassNotFoundException, IllegalAccessException, InstantiationException, NullPointerException, ClassCastException
  {
    String str = System.getProperty("org.w3c.css.sac.parser");
    if (str == null) {
      throw new NullPointerException("No value for sac.parser property");
    }
    return (Parser)Class.forName(str).newInstance();
  }
}
