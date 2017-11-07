package org.apache.xalan.xsltc.dom;

import java.util.Vector;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xml.dtm.DTMAxisIterator;






























public abstract class NodeCounter
{
  public static final int END = -1;
  protected int _node = -1;
  protected int _nodeType = -1;
  protected double _value = -2.147483648E9D;
  
  public final DOM _document;
  
  public final DTMAxisIterator _iterator;
  
  public final Translet _translet;
  protected String _format;
  protected String _lang;
  protected String _letterValue;
  protected String _groupSep;
  protected int _groupSize;
  private boolean _separFirst = true;
  private boolean _separLast = false;
  private Vector _separToks = new Vector();
  private Vector _formatToks = new Vector();
  private int _nSepars = 0;
  private int _nFormats = 0;
  
  private static final String[] Thousands = { "", "m", "mm", "mmm" };
  
  private static final String[] Hundreds = { "", "c", "cc", "ccc", "cd", "d", "dc", "dcc", "dccc", "cm" };
  
  private static final String[] Tens = { "", "x", "xx", "xxx", "xl", "l", "lx", "lxx", "lxxx", "xc" };
  
  private static final String[] Ones = { "", "i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix" };
  

  private StringBuffer _tempBuffer = new StringBuffer();
  
  protected NodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
  {
    _translet = translet;
    _document = document;
    _iterator = iterator;
  }
  




  public abstract NodeCounter setStartNode(int paramInt);
  



  public NodeCounter setValue(double value)
  {
    _value = value;
    return this;
  }
  



  protected void setFormatting(String format, String lang, String letterValue, String groupSep, String groupSize)
  {
    _lang = lang;
    _groupSep = groupSep;
    _letterValue = letterValue;
    try
    {
      _groupSize = Integer.parseInt(groupSize);
    }
    catch (NumberFormatException e) {
      _groupSize = 0;
    }
    setTokens(format);
  }
  

  private final void setTokens(String format)
  {
    if ((_format != null) && (format.equals(_format))) {
      return;
    }
    _format = format;
    
    int length = _format.length();
    boolean isFirst = true;
    _separFirst = true;
    _separLast = false;
    _nSepars = 0;
    _nFormats = 0;
    _separToks.clear();
    _formatToks.clear();
    




    int j = 0; for (int i = 0; i < length;) {
      char c = format.charAt(i);
      for (j = i; Character.isLetterOrDigit(c);) {
        i++; if (i == length) break;
        c = format.charAt(i);
      }
      if (i > j) {
        if (isFirst) {
          _separToks.addElement(".");
          isFirst = this._separFirst = 0;
        }
        _formatToks.addElement(format.substring(j, i));
      }
      
      if (i == length)
        break;
      c = format.charAt(i);
      for (j = i; !Character.isLetterOrDigit(c);) {
        i++; if (i == length) break;
        c = format.charAt(i);
        isFirst = false;
      }
      if (i > j) {
        _separToks.addElement(format.substring(j, i));
      }
    }
    
    _nSepars = _separToks.size();
    _nFormats = _formatToks.size();
    if (_nSepars > _nFormats) { _separLast = true;
    }
    if (_separFirst) _nSepars -= 1;
    if (_separLast) _nSepars -= 1;
    if (_nSepars == 0) {
      _separToks.insertElementAt(".", 1);
      _nSepars += 1;
    }
    if (_separFirst) { _nSepars += 1;
    }
  }
  

  public NodeCounter setDefaultFormatting()
  {
    setFormatting("1", "en", "alphabetic", null, null);
    return this;
  }
  





  public abstract String getCounter();
  




  public String getCounter(String format, String lang, String letterValue, String groupSep, String groupSize)
  {
    setFormatting(format, lang, letterValue, groupSep, groupSize);
    return getCounter();
  }
  




  public boolean matchesCount(int node)
  {
    return _nodeType == _document.getExpandedTypeID(node);
  }
  



  public boolean matchesFrom(int node)
  {
    return false;
  }
  


  protected String formatNumbers(int value)
  {
    return formatNumbers(new int[] { value });
  }
  



  protected String formatNumbers(int[] values)
  {
    int nValues = values.length;
    int length = _format.length();
    
    boolean isEmpty = true;
    for (int i = 0; i < nValues; i++)
      if (values[i] != Integer.MIN_VALUE)
        isEmpty = false;
    if (isEmpty) { return "";
    }
    
    boolean isFirst = true;
    int t = 0;int n = 0;int s = 1;
    _tempBuffer.setLength(0);
    StringBuffer buffer = _tempBuffer;
    

    if (_separFirst) { buffer.append((String)_separToks.elementAt(0));
    }
    
    while (n < nValues) {
      int value = values[n];
      if (value != Integer.MIN_VALUE) {
        if (!isFirst) buffer.append((String)_separToks.elementAt(s++));
        formatValue(value, (String)_formatToks.elementAt(t++), buffer);
        if (t == _nFormats) t--;
        if (s >= _nSepars) s--;
        isFirst = false;
      }
      n++;
    }
    

    if (_separLast) buffer.append((String)_separToks.lastElement());
    return buffer.toString();
  }
  




  private void formatValue(int value, String format, StringBuffer buffer)
  {
    char c = format.charAt(0);
    
    if (Character.isDigit(c)) {
      char zero = (char)(c - Character.getNumericValue(c));
      
      StringBuffer temp = buffer;
      if (_groupSize > 0) {
        temp = new StringBuffer();
      }
      String s = "";
      int n = value;
      while (n > 0) {
        s = (char)(zero + n % 10) + s;
        n /= 10;
      }
      
      for (int i = 0; i < format.length() - s.length(); i++) {
        temp.append(zero);
      }
      temp.append(s);
      
      if (_groupSize > 0) {
        for (int i = 0; i < temp.length(); i++) {
          if ((i != 0) && ((temp.length() - i) % _groupSize == 0)) {
            buffer.append(_groupSep);
          }
          buffer.append(temp.charAt(i));
        }
      }
    }
    else if ((c == 'i') && (!_letterValue.equals("alphabetic"))) {
      buffer.append(romanValue(value));
    }
    else if ((c == 'I') && (!_letterValue.equals("alphabetic"))) {
      buffer.append(romanValue(value).toUpperCase());
    }
    else {
      int min = c;
      int max = c;
      

      if ((c >= 'α') && (c <= 'ω')) {
        max = 969;
      }
      else
      {
        while (Character.isLetterOrDigit((char)(max + 1))) {
          max++;
        }
      }
      buffer.append(alphaValue(value, min, max));
    }
  }
  
  private String alphaValue(int value, int min, int max) {
    if (value <= 0) {
      return "" + value;
    }
    
    int range = max - min + 1;
    char last = (char)((value - 1) % range + min);
    if (value > range) {
      return alphaValue((value - 1) / range, min, max) + last;
    }
    
    return "" + last;
  }
  
  private String romanValue(int n)
  {
    if ((n <= 0) || (n > 4000)) {
      return "" + n;
    }
    return Thousands[(n / 1000)] + Hundreds[(n / 100 % 10)] + Tens[(n / 10 % 10)] + Ones[(n % 10)];
  }
}
