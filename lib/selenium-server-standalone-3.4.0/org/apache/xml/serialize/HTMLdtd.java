package org.apache.xml.serialize;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.dom.DOMMessageFormatter;

/**
 * @deprecated
 */
public final class HTMLdtd
{
  public static final String HTMLPublicId = "-//W3C//DTD HTML 4.01//EN";
  public static final String HTMLSystemId = "http://www.w3.org/TR/html4/strict.dtd";
  public static final String XHTMLPublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
  public static final String XHTMLSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
  private static Hashtable _byChar;
  private static Hashtable _byName;
  private static Hashtable _boolAttrs;
  private static Hashtable _elemDefs = new Hashtable();
  private static final String ENTITIES_RESOURCE = "HTMLEntities.res";
  private static final int ONLY_OPENING = 1;
  private static final int ELEM_CONTENT = 2;
  private static final int PRESERVE = 4;
  private static final int OPT_CLOSING = 8;
  private static final int EMPTY = 17;
  private static final int ALLOWED_HEAD = 32;
  private static final int CLOSE_P = 64;
  private static final int CLOSE_DD_DT = 128;
  private static final int CLOSE_SELF = 256;
  private static final int CLOSE_TABLE = 512;
  private static final int CLOSE_TH_TD = 16384;
  
  public HTMLdtd() {}
  
  public static boolean isEmptyTag(String paramString)
  {
    return isElement(paramString, 17);
  }
  
  public static boolean isElementContent(String paramString)
  {
    return isElement(paramString, 2);
  }
  
  public static boolean isPreserveSpace(String paramString)
  {
    return isElement(paramString, 4);
  }
  
  public static boolean isOptionalClosing(String paramString)
  {
    return isElement(paramString, 8);
  }
  
  public static boolean isOnlyOpening(String paramString)
  {
    return isElement(paramString, 1);
  }
  
  public static boolean isClosing(String paramString1, String paramString2)
  {
    if (paramString2.equalsIgnoreCase("HEAD")) {
      return !isElement(paramString1, 32);
    }
    if (paramString2.equalsIgnoreCase("P")) {
      return isElement(paramString1, 64);
    }
    if ((paramString2.equalsIgnoreCase("DT")) || (paramString2.equalsIgnoreCase("DD"))) {
      return isElement(paramString1, 128);
    }
    if ((paramString2.equalsIgnoreCase("LI")) || (paramString2.equalsIgnoreCase("OPTION"))) {
      return isElement(paramString1, 256);
    }
    if ((paramString2.equalsIgnoreCase("THEAD")) || (paramString2.equalsIgnoreCase("TFOOT")) || (paramString2.equalsIgnoreCase("TBODY")) || (paramString2.equalsIgnoreCase("TR")) || (paramString2.equalsIgnoreCase("COLGROUP"))) {
      return isElement(paramString1, 512);
    }
    if ((paramString2.equalsIgnoreCase("TH")) || (paramString2.equalsIgnoreCase("TD"))) {
      return isElement(paramString1, 16384);
    }
    return false;
  }
  
  public static boolean isURI(String paramString1, String paramString2)
  {
    return (paramString2.equalsIgnoreCase("href")) || (paramString2.equalsIgnoreCase("src"));
  }
  
  public static boolean isBoolean(String paramString1, String paramString2)
  {
    String[] arrayOfString = (String[])_boolAttrs.get(paramString1.toUpperCase(Locale.ENGLISH));
    if (arrayOfString == null) {
      return false;
    }
    for (int i = 0; i < arrayOfString.length; i++) {
      if (arrayOfString[i].equalsIgnoreCase(paramString2)) {
        return true;
      }
    }
    return false;
  }
  
  public static int charFromName(String paramString)
  {
    initialize();
    Object localObject = _byName.get(paramString);
    if ((localObject != null) && ((localObject instanceof Integer))) {
      return ((Integer)localObject).intValue();
    }
    return -1;
  }
  
  public static String fromChar(int paramInt)
  {
    if (paramInt > 65535) {
      return null;
    }
    initialize();
    String str = (String)_byChar.get(new Integer(paramInt));
    return str;
  }
  
  private static void initialize()
  {
    InputStream localInputStream = null;
    BufferedReader localBufferedReader = null;
    if (_byName != null) {
      return;
    }
    try
    {
      _byName = new Hashtable();
      _byChar = new Hashtable();
      localInputStream = HTMLdtd.class.getResourceAsStream("HTMLEntities.res");
      if (localInputStream == null) {
        throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResourceNotFound", new Object[] { "HTMLEntities.res" }));
      }
      localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream, "ASCII"));
      String str3 = localBufferedReader.readLine();
      while (str3 != null) {
        if ((str3.length() == 0) || (str3.charAt(0) == '#'))
        {
          str3 = localBufferedReader.readLine();
        }
        else
        {
          int i = str3.indexOf(' ');
          if (i > 1)
          {
            String str1 = str3.substring(0, i);
            i++;
            if (i < str3.length())
            {
              String str2 = str3.substring(i);
              i = str2.indexOf(' ');
              if (i > 0) {
                str2 = str2.substring(0, i);
              }
              int j = Integer.parseInt(str2);
              defineEntity(str1, (char)j);
            }
          }
          str3 = localBufferedReader.readLine();
        }
      }
      localInputStream.close();
    }
    catch (Exception localException1)
    {
      throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResourceNotLoaded", new Object[] { "HTMLEntities.res", localException1.toString() }));
    }
    finally
    {
      if (localInputStream != null) {
        try
        {
          localInputStream.close();
        }
        catch (Exception localException2) {}
      }
    }
  }
  
  private static void defineEntity(String paramString, char paramChar)
  {
    if (_byName.get(paramString) == null)
    {
      _byName.put(paramString, new Integer(paramChar));
      _byChar.put(new Integer(paramChar), paramString);
    }
  }
  
  private static void defineElement(String paramString, int paramInt)
  {
    _elemDefs.put(paramString, new Integer(paramInt));
  }
  
  private static void defineBoolean(String paramString1, String paramString2)
  {
    defineBoolean(paramString1, new String[] { paramString2 });
  }
  
  private static void defineBoolean(String paramString, String[] paramArrayOfString)
  {
    _boolAttrs.put(paramString, paramArrayOfString);
  }
  
  private static boolean isElement(String paramString, int paramInt)
  {
    Integer localInteger = (Integer)_elemDefs.get(paramString.toUpperCase(Locale.ENGLISH));
    if (localInteger == null) {
      return false;
    }
    return (localInteger.intValue() & paramInt) == paramInt;
  }
  
  static
  {
    defineElement("ADDRESS", 64);
    defineElement("AREA", 17);
    defineElement("BASE", 49);
    defineElement("BASEFONT", 17);
    defineElement("BLOCKQUOTE", 64);
    defineElement("BODY", 8);
    defineElement("BR", 17);
    defineElement("COL", 17);
    defineElement("COLGROUP", 522);
    defineElement("DD", 137);
    defineElement("DIV", 64);
    defineElement("DL", 66);
    defineElement("DT", 137);
    defineElement("FIELDSET", 64);
    defineElement("FORM", 64);
    defineElement("FRAME", 25);
    defineElement("H1", 64);
    defineElement("H2", 64);
    defineElement("H3", 64);
    defineElement("H4", 64);
    defineElement("H5", 64);
    defineElement("H6", 64);
    defineElement("HEAD", 10);
    defineElement("HR", 81);
    defineElement("HTML", 10);
    defineElement("IMG", 17);
    defineElement("INPUT", 17);
    defineElement("ISINDEX", 49);
    defineElement("LI", 265);
    defineElement("LINK", 49);
    defineElement("MAP", 32);
    defineElement("META", 49);
    defineElement("OL", 66);
    defineElement("OPTGROUP", 2);
    defineElement("OPTION", 265);
    defineElement("P", 328);
    defineElement("PARAM", 17);
    defineElement("PRE", 68);
    defineElement("SCRIPT", 36);
    defineElement("NOSCRIPT", 36);
    defineElement("SELECT", 2);
    defineElement("STYLE", 36);
    defineElement("TABLE", 66);
    defineElement("TBODY", 522);
    defineElement("TD", 16392);
    defineElement("TEXTAREA", 4);
    defineElement("TFOOT", 522);
    defineElement("TH", 16392);
    defineElement("THEAD", 522);
    defineElement("TITLE", 32);
    defineElement("TR", 522);
    defineElement("UL", 66);
    _boolAttrs = new Hashtable();
    defineBoolean("AREA", "href");
    defineBoolean("BUTTON", "disabled");
    defineBoolean("DIR", "compact");
    defineBoolean("DL", "compact");
    defineBoolean("FRAME", "noresize");
    defineBoolean("HR", "noshade");
    defineBoolean("IMAGE", "ismap");
    defineBoolean("INPUT", new String[] { "defaultchecked", "checked", "readonly", "disabled" });
    defineBoolean("LINK", "link");
    defineBoolean("MENU", "compact");
    defineBoolean("OBJECT", "declare");
    defineBoolean("OL", "compact");
    defineBoolean("OPTGROUP", "disabled");
    defineBoolean("OPTION", new String[] { "default-selected", "selected", "disabled" });
    defineBoolean("SCRIPT", "defer");
    defineBoolean("SELECT", new String[] { "multiple", "disabled" });
    defineBoolean("STYLE", "disabled");
    defineBoolean("TD", "nowrap");
    defineBoolean("TH", "nowrap");
    defineBoolean("TEXTAREA", new String[] { "disabled", "readonly" });
    defineBoolean("UL", "compact");
    initialize();
  }
}
