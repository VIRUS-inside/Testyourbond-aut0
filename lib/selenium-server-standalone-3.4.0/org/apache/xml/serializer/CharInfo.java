package org.apache.xml.serializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.xml.transform.TransformerException;
import org.apache.xml.serializer.utils.Messages;
import org.apache.xml.serializer.utils.SystemIDResolver;
import org.apache.xml.serializer.utils.Utils;
import org.apache.xml.serializer.utils.WrappedRuntimeException;



























final class CharInfo
{
  private HashMap m_charToString;
  
  CharInfo(String x0, String x1, boolean x2, 1 x3)
  {
    this(x0, x1, x2);
  }
  






  public static final String HTML_ENTITIES_RESOURCE = SerializerBase.PKG_NAME + ".HTMLEntities";
  





  public static final String XML_ENTITIES_RESOURCE = SerializerBase.PKG_NAME + ".XMLEntities";
  



  static final char S_HORIZONAL_TAB = '\t';
  



  static final char S_LINEFEED = '\n';
  



  static final char S_CARRIAGERETURN = '\r';
  



  static final char S_SPACE = ' ';
  



  static final char S_QUOTE = '"';
  



  static final char S_LT = '<';
  



  static final char S_GT = '>';
  



  static final char S_NEL = '';
  



  static final char S_LINE_SEPARATOR = ' ';
  



  boolean onlyQuotAmpLtGt;
  


  static final int ASCII_MAX = 128;
  


  private final boolean[] shouldMapAttrChar_ASCII;
  


  private final boolean[] shouldMapTextChar_ASCII;
  


  private final int[] array_of_bits;
  


  private static final int SHIFT_PER_WORD = 5;
  


  private static final int LOW_ORDER_BITMASK = 31;
  


  private int firstWordNotUsed;
  


  private final CharKey m_charKey;
  



  private CharInfo()
  {
    array_of_bits = createEmptySetOfIntegers(65535);
    firstWordNotUsed = 0;
    shouldMapAttrChar_ASCII = new boolean[''];
    shouldMapTextChar_ASCII = new boolean[''];
    m_charKey = new CharKey();
    



    onlyQuotAmpLtGt = true;
  }
  




  private CharInfo(String entitiesResource, String method, boolean internal)
  {
    this();
    m_charToString = new HashMap();
    
    ResourceBundle entities = null;
    boolean noExtraEntities = true;
    








    if (internal)
    {
      try
      {
        entities = PropertyResourceBundle.getBundle(entitiesResource);
      }
      catch (Exception e) {}
    }
    if (entities != null) {
      Enumeration keys = entities.getKeys();
      while (keys.hasMoreElements()) {
        String name = (String)keys.nextElement();
        String value = entities.getString(name);
        int code = Integer.parseInt(value);
        boolean extra = defineEntity(name, (char)code);
        if (extra)
          noExtraEntities = false;
      }
    } else {
      InputStream is = null;
      

      try
      {
        if (internal) {
          is = CharInfo.class.getResourceAsStream(entitiesResource);
        } else {
          ClassLoader cl = ObjectFactory.findClassLoader();
          if (cl == null) {
            is = ClassLoader.getSystemResourceAsStream(entitiesResource);
          } else {
            is = cl.getResourceAsStream(entitiesResource);
          }
          
          if (is == null) {
            try {
              URL url = new URL(entitiesResource);
              is = url.openStream();
            }
            catch (Exception e) {}
          }
        }
        if (is == null) {
          throw new RuntimeException(Utils.messages.createMessage("ER_RESOURCE_COULD_NOT_FIND", new Object[] { entitiesResource, entitiesResource }));
        }
        










        BufferedReader reader;
        










        try
        {
          reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
          reader = new BufferedReader(new InputStreamReader(is));
        }
        
        String line = reader.readLine();
        
        while (line != null) {
          if ((line.length() == 0) || (line.charAt(0) == '#')) {
            line = reader.readLine();

          }
          else
          {
            int index = line.indexOf(' ');
            
            if (index > 1) {
              String name = line.substring(0, index);
              
              index++;
              
              if (index < line.length()) {
                String value = line.substring(index);
                index = value.indexOf(' ');
                
                if (index > 0) {
                  value = value.substring(0, index);
                }
                
                int code = Integer.parseInt(value);
                
                boolean extra = defineEntity(name, (char)code);
                if (extra) {
                  noExtraEntities = false;
                }
              }
            }
            line = reader.readLine();
          }
        }
        is.close();
        








        if (is != null) {
          try {
            is.close();
          }
          catch (Exception except) {}
        }
        

        onlyQuotAmpLtGt = noExtraEntities;
      }
      catch (Exception e)
      {
        throw new RuntimeException(Utils.messages.createMessage("ER_RESOURCE_COULD_NOT_LOAD", new Object[] { entitiesResource, e.toString(), entitiesResource, e.toString() }));


      }
      finally
      {


        if (is != null) {
          try {
            is.close();
          }
          catch (Exception except) {}
        }
      }
    }
    










    if ("xml".equals(method))
    {

      shouldMapTextChar_ASCII[34] = false;
    }
    
    if ("html".equals(method))
    {


      shouldMapAttrChar_ASCII[60] = false;
      

      shouldMapTextChar_ASCII[34] = false;
    }
  }
  


















  private boolean defineEntity(String name, char value)
  {
    StringBuffer sb = new StringBuffer("&");
    sb.append(name);
    sb.append(';');
    String entityString = sb.toString();
    
    boolean extra = defineChar2StringMapping(entityString, value);
    return extra;
  }
  































  String getOutputStringForChar(char value)
  {
    m_charKey.setChar(value);
    return (String)m_charToString.get(m_charKey);
  }
  












  final boolean shouldMapAttrChar(int value)
  {
    if (value < 128) {
      return shouldMapAttrChar_ASCII[value];
    }
    

    return get(value);
  }
  













  final boolean shouldMapTextChar(int value)
  {
    if (value < 128) {
      return shouldMapTextChar_ASCII[value];
    }
    

    return get(value);
  }
  



  private static CharInfo getCharInfoBasedOnPrivilege(String entitiesFileName, String method, boolean internal)
  {
    (CharInfo)AccessController.doPrivileged(new PrivilegedAction() {
      private final String val$entitiesFileName;
      
      public Object run() { return new CharInfo(val$entitiesFileName, val$method, val$internal, null); }
    });
  }
  






  private final String val$method;
  





  private final boolean val$internal;
  





  static CharInfo getCharInfo(String entitiesFileName, String method)
  {
    CharInfo charInfo = (CharInfo)m_getCharInfoCache.get(entitiesFileName);
    if (charInfo != null) {
      return mutableCopyOf(charInfo);
    }
    
    try
    {
      charInfo = getCharInfoBasedOnPrivilege(entitiesFileName, method, true);
      


      m_getCharInfoCache.put(entitiesFileName, charInfo);
      return mutableCopyOf(charInfo);
    }
    catch (Exception e)
    {
      try {
        return getCharInfoBasedOnPrivilege(entitiesFileName, method, false);
      }
      catch (Exception e)
      {
        String absoluteEntitiesFileName;
        
        if (entitiesFileName.indexOf(':') < 0) {
          absoluteEntitiesFileName = SystemIDResolver.getAbsoluteURIFromRelative(entitiesFileName);
        } else
          try
          {
            absoluteEntitiesFileName = SystemIDResolver.getAbsoluteURI(entitiesFileName, null);
          } catch (TransformerException te) {
            String absoluteEntitiesFileName;
            throw new WrappedRuntimeException(te);
          }
      }
    }
    return getCharInfoBasedOnPrivilege(entitiesFileName, method, false);
  }
  





  private static CharInfo mutableCopyOf(CharInfo charInfo)
  {
    CharInfo copy = new CharInfo();
    
    int max = array_of_bits.length;
    System.arraycopy(array_of_bits, 0, array_of_bits, 0, max);
    
    firstWordNotUsed = firstWordNotUsed;
    
    max = shouldMapAttrChar_ASCII.length;
    System.arraycopy(shouldMapAttrChar_ASCII, 0, shouldMapAttrChar_ASCII, 0, max);
    
    max = shouldMapTextChar_ASCII.length;
    System.arraycopy(shouldMapTextChar_ASCII, 0, shouldMapTextChar_ASCII, 0, max);
    


    m_charToString = ((HashMap)m_charToString.clone());
    
    onlyQuotAmpLtGt = onlyQuotAmpLtGt;
    
    return copy;
  }
  







  private static Hashtable m_getCharInfoCache = new Hashtable();
  





  private static int arrayIndex(int i)
  {
    return i >> 5;
  }
  




  private static int bit(int i)
  {
    int ret = 1 << (i & 0x1F);
    return ret;
  }
  



  private int[] createEmptySetOfIntegers(int max)
  {
    firstWordNotUsed = 0;
    
    int[] arr = new int[arrayIndex(max - 1) + 1];
    return arr;
  }
  






  private final void set(int i)
  {
    setASCIItextDirty(i);
    setASCIIattrDirty(i);
    
    int j = i >> 5;
    int k = j + 1;
    
    if (firstWordNotUsed < k) {
      firstWordNotUsed = k;
    }
    array_of_bits[j] |= 1 << (i & 0x1F);
  }
  











  private final boolean get(int i)
  {
    boolean in_the_set = false;
    int j = i >> 5;
    

    if (j < firstWordNotUsed) {
      in_the_set = (array_of_bits[j] & 1 << (i & 0x1F)) != 0;
    }
    
    return in_the_set;
  }
  















  private boolean extraEntity(String outputString, int charToMap)
  {
    boolean extra = false;
    if (charToMap < 128)
    {
      switch (charToMap)
      {
      case 34: 
        if (!outputString.equals("&quot;"))
          extra = true;
        break;
      case 38: 
        if (!outputString.equals("&amp;"))
          extra = true;
        break;
      case 60: 
        if (!outputString.equals("&lt;"))
          extra = true;
        break;
      case 62: 
        if (!outputString.equals("&gt;"))
          extra = true;
        break;
      default: 
        extra = true;
      }
    }
    return extra;
  }
  






  private void setASCIItextDirty(int j)
  {
    if ((0 <= j) && (j < 128))
    {
      shouldMapTextChar_ASCII[j] = true;
    }
  }
  






  private void setASCIIattrDirty(int j)
  {
    if ((0 <= j) && (j < 128))
    {
      shouldMapAttrChar_ASCII[j] = true;
    }
  }
  














  boolean defineChar2StringMapping(String outputString, char inputChar)
  {
    CharKey character = new CharKey(inputChar);
    m_charToString.put(character, outputString);
    set(inputChar);
    
    boolean extraMapping = extraEntity(outputString, inputChar);
    return extraMapping;
  }
  







  private static class CharKey
  {
    private char m_char;
    







    public CharKey(char key)
    {
      m_char = key;
    }
    






    public CharKey() {}
    






    public final void setChar(char c)
    {
      m_char = c;
    }
    







    public final int hashCode()
    {
      return m_char;
    }
    







    public final boolean equals(Object obj)
    {
      return m_char == m_char;
    }
  }
}
