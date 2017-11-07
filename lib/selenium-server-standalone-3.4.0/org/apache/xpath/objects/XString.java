package org.apache.xpath.objects;

import java.util.Locale;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLCharacterRecognizer;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringFactory;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;






















public class XString
  extends XObject
  implements XMLString
{
  static final long serialVersionUID = 2020470518395094525L;
  public static final XString EMPTYSTRING = new XString("");
  





  protected XString(Object val)
  {
    super(val);
  }
  





  public XString(String val)
  {
    super(val);
  }
  





  public int getType()
  {
    return 3;
  }
  






  public String getTypeString()
  {
    return "#STRING";
  }
  





  public boolean hasString()
  {
    return true;
  }
  






  public double num()
  {
    return toDouble();
  }
  













  public double toDouble()
  {
    XMLString s = trim();
    double result = NaN.0D;
    for (int i = 0; i < s.length(); i++)
    {
      char c = s.charAt(i);
      if ((c != '-') && (c != '.') && ((c < '0') || (c > '9')))
      {

        return result;
      }
    }
    try
    {
      result = Double.parseDouble(s.toString());
    }
    catch (NumberFormatException e) {}
    return result;
  }
  






  public boolean bool()
  {
    return str().length() > 0;
  }
  





  public XMLString xstr()
  {
    return this;
  }
  





  public String str()
  {
    return null != m_obj ? (String)m_obj : "";
  }
  








  public int rtf(XPathContext support)
  {
    DTM frag = support.createDocumentFragment();
    
    frag.appendTextChild(str());
    
    return frag.getDocument();
  }
  












  public void dispatchCharactersEvents(ContentHandler ch)
    throws SAXException
  {
    String str = str();
    
    ch.characters(str.toCharArray(), 0, str.length());
  }
  










  public void dispatchAsComment(LexicalHandler lh)
    throws SAXException
  {
    String str = str();
    
    lh.comment(str.toCharArray(), 0, str.length());
  }
  






  public int length()
  {
    return str().length();
  }
  













  public char charAt(int index)
  {
    return str().charAt(index);
  }
  





















  public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
  {
    str().getChars(srcBegin, srcEnd, dst, dstBegin);
  }
  













  public boolean equals(XObject obj2)
  {
    int t = obj2.getType();
    try
    {
      if (4 == t) {
        return obj2.equals(this);
      }
      

      if (1 == t) {
        return obj2.bool() == bool();
      }
      
      if (2 == t) {
        return obj2.num() == num();
      }
    }
    catch (TransformerException te) {
      throw new WrappedRuntimeException(te);
    }
    


    return xstr().equals(obj2.xstr());
  }
  











  public boolean equals(String obj2)
  {
    return str().equals(obj2);
  }
  













  public boolean equals(XMLString obj2)
  {
    if (obj2 != null) {
      if (!obj2.hasString()) {
        return obj2.equals(str());
      }
      return str().equals(obj2.toString());
    }
    
    return false;
  }
  














  public boolean equals(Object obj2)
  {
    if (null == obj2) {
      return false;
    }
    


    if ((obj2 instanceof XNodeSet))
      return obj2.equals(this);
    if ((obj2 instanceof XNumber)) {
      return obj2.equals(this);
    }
    return str().equals(obj2.toString());
  }
  















  public boolean equalsIgnoreCase(String anotherString)
  {
    return str().equalsIgnoreCase(anotherString);
  }
  














  public int compareTo(XMLString xstr)
  {
    int len1 = length();
    int len2 = xstr.length();
    int n = Math.min(len1, len2);
    int i = 0;
    int j = 0;
    
    while (n-- != 0)
    {
      char c1 = charAt(i);
      char c2 = xstr.charAt(j);
      
      if (c1 != c2)
      {
        return c1 - c2;
      }
      
      i++;
      j++;
    }
    
    return len1 - len2;
  }
  


























  public int compareToIgnoreCase(XMLString str)
  {
    throw new WrappedRuntimeException(new NoSuchMethodException("Java 1.2 method, not yet implemented"));
  }
  





















  public boolean startsWith(String prefix, int toffset)
  {
    return str().startsWith(prefix, toffset);
  }
  














  public boolean startsWith(String prefix)
  {
    return startsWith(prefix, 0);
  }
  




















  public boolean startsWith(XMLString prefix, int toffset)
  {
    int to = toffset;
    int tlim = length();
    int po = 0;
    int pc = prefix.length();
    

    if ((toffset < 0) || (toffset > tlim - pc))
    {
      return false;
    }
    for (;;) {
      pc--; if (pc < 0)
        break;
      if (charAt(to) != prefix.charAt(po))
      {
        return false;
      }
      
      to++;
      po++;
    }
    
    return true;
  }
  














  public boolean startsWith(XMLString prefix)
  {
    return startsWith(prefix, 0);
  }
  













  public boolean endsWith(String suffix)
  {
    return str().endsWith(suffix);
  }
  













  public int hashCode()
  {
    return str().hashCode();
  }
  

















  public int indexOf(int ch)
  {
    return str().indexOf(ch);
  }
  




























  public int indexOf(int ch, int fromIndex)
  {
    return str().indexOf(ch, fromIndex);
  }
  















  public int lastIndexOf(int ch)
  {
    return str().lastIndexOf(ch);
  }
  























  public int lastIndexOf(int ch, int fromIndex)
  {
    return str().lastIndexOf(ch, fromIndex);
  }
  

















  public int indexOf(String str)
  {
    return str().indexOf(str);
  }
  

















  public int indexOf(XMLString str)
  {
    return str().indexOf(str.toString());
  }
  


























  public int indexOf(String str, int fromIndex)
  {
    return str().indexOf(str, fromIndex);
  }
  


















  public int lastIndexOf(String str)
  {
    return str().lastIndexOf(str);
  }
  




















  public int lastIndexOf(String str, int fromIndex)
  {
    return str().lastIndexOf(str, fromIndex);
  }
  

















  public XMLString substring(int beginIndex)
  {
    return new XString(str().substring(beginIndex));
  }
  
















  public XMLString substring(int beginIndex, int endIndex)
  {
    return new XString(str().substring(beginIndex, endIndex));
  }
  












  public XMLString concat(String str)
  {
    return new XString(str().concat(str));
  }
  









  public XMLString toLowerCase(Locale locale)
  {
    return new XString(str().toLowerCase(locale));
  }
  










  public XMLString toLowerCase()
  {
    return new XString(str().toLowerCase());
  }
  








  public XMLString toUpperCase(Locale locale)
  {
    return new XString(str().toUpperCase(locale));
  }
  


























  public XMLString toUpperCase()
  {
    return new XString(str().toUpperCase());
  }
  





  public XMLString trim()
  {
    return new XString(str().trim());
  }
  







  private static boolean isSpace(char ch)
  {
    return XMLCharacterRecognizer.isWhiteSpace(ch);
  }
  

















  public XMLString fixWhiteSpace(boolean trimHead, boolean trimTail, boolean doublePunctuationSpaces)
  {
    int len = length();
    char[] buf = new char[len];
    
    getChars(0, len, buf, 0);
    
    boolean edit = false;
    

    for (int s = 0; s < len; s++)
    {
      if (isSpace(buf[s])) {
        break;
      }
    }
    


    int d = s;
    boolean pres = false;
    for (; 
        s < len; s++)
    {
      char c = buf[s];
      
      if (isSpace(c))
      {
        if (!pres)
        {
          if (' ' != c)
          {
            edit = true;
          }
          
          buf[(d++)] = ' ';
          
          if ((doublePunctuationSpaces) && (s != 0))
          {
            char prevChar = buf[(s - 1)];
            
            if ((prevChar != '.') && (prevChar != '!') && (prevChar != '?'))
            {

              pres = true;
            }
          }
          else
          {
            pres = true;
          }
        }
        else
        {
          edit = true;
          pres = true;
        }
      }
      else
      {
        buf[(d++)] = c;
        pres = false;
      }
    }
    
    if ((trimTail) && (1 <= d) && (' ' == buf[(d - 1)]))
    {
      edit = true;
      
      d--;
    }
    
    int start = 0;
    
    if ((trimHead) && (0 < d) && (' ' == buf[0]))
    {
      edit = true;
      
      start++;
    }
    
    XMLStringFactory xsf = XMLStringFactoryImpl.getFactory();
    
    return edit ? xsf.newstr(new String(buf, start, d - start)) : this;
  }
  



  public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
  {
    visitor.visitStringLiteral(owner, this);
  }
}
