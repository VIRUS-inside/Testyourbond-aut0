package org.xml.sax.helpers;

import org.xml.sax.Parser;

/**
 * @deprecated
 */
public class ParserFactory
{
  private ParserFactory() {}
  
  public static Parser makeParser()
    throws ClassNotFoundException, IllegalAccessException, InstantiationException, NullPointerException, ClassCastException
  {
    String str = SecuritySupport.getSystemProperty("org.xml.sax.parser");
    if (str == null) {
      throw new NullPointerException("No value for sax.parser property");
    }
    return makeParser(str);
  }
  
  public static Parser makeParser(String paramString)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException, ClassCastException
  {
    return (Parser)NewInstance.newInstance(NewInstance.getClassLoader(), paramString);
  }
}
