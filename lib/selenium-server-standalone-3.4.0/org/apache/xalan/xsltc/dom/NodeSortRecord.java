package org.apache.xalan.xsltc.dom;

import java.text.Collator;
import java.util.Locale;
import org.apache.xalan.xsltc.CollatorFactory;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.utils.StringComparable;































public abstract class NodeSortRecord
{
  public static final int COMPARE_STRING = 0;
  public static final int COMPARE_NUMERIC = 1;
  public static final int COMPARE_ASCENDING = 0;
  public static final int COMPARE_DESCENDING = 1;
  /**
   * @deprecated
   */
  private static final Collator DEFAULT_COLLATOR = ;
  


  /**
   * @deprecated
   */
  protected Collator _collator = DEFAULT_COLLATOR;
  

  protected Collator[] _collators;
  
  /**
   * @deprecated
   */
  protected Locale _locale;
  
  protected CollatorFactory _collatorFactory;
  
  protected SortSettings _settings;
  
  private DOM _dom = null;
  private int _node;
  private int _last = 0;
  private int _scanned = 0;
  


  private Object[] _values;
  



  public NodeSortRecord(int node)
  {
    _node = node;
  }
  
  public NodeSortRecord() {
    this(0);
  }
  





  public final void initialize(int node, int last, DOM dom, SortSettings settings)
    throws TransletException
  {
    _dom = dom;
    _node = node;
    _last = last;
    _settings = settings;
    
    int levels = settings.getSortOrders().length;
    _values = new Object[levels];
    

    String colFactClassname = System.getProperty("org.apache.xalan.xsltc.COLLATOR_FACTORY");
    

    if (colFactClassname != null) {
      try {
        Object candObj = ObjectFactory.findProviderClass(colFactClassname, ObjectFactory.findClassLoader(), true);
        
        _collatorFactory = ((CollatorFactory)candObj);
      } catch (ClassNotFoundException e) {
        throw new TransletException(e);
      }
      Locale[] locales = settings.getLocales();
      _collators = new Collator[levels];
      for (int i = 0; i < levels; i++) {
        _collators[i] = _collatorFactory.getCollator(locales[i]);
      }
      _collator = _collators[0];
    } else {
      _collators = settings.getCollators();
      _collator = _collators[0];
    }
  }
  


  public final int getNode()
  {
    return _node;
  }
  


  public final int compareDocOrder(NodeSortRecord other)
  {
    return _node - _node;
  }
  





  private final Comparable stringValue(int level)
  {
    if (_scanned <= level) {
      AbstractTranslet translet = _settings.getTranslet();
      Locale[] locales = _settings.getLocales();
      String[] caseOrder = _settings.getCaseOrders();
      

      String str = extractValueFromDOM(_dom, _node, level, translet, _last);
      
      Comparable key = StringComparable.getComparator(str, locales[level], _collators[level], caseOrder[level]);
      


      _values[(_scanned++)] = key;
      return key;
    }
    return (Comparable)_values[level];
  }
  
  private final Double numericValue(int level)
  {
    if (_scanned <= level) {
      AbstractTranslet translet = _settings.getTranslet();
      

      String str = extractValueFromDOM(_dom, _node, level, translet, _last);
      Double num;
      try
      {
        num = new Double(str);
      }
      catch (NumberFormatException e)
      {
        num = new Double(Double.NEGATIVE_INFINITY);
      }
      _values[(_scanned++)] = num;
      return num;
    }
    return (Double)_values[level];
  }
  







  public int compareTo(NodeSortRecord other)
  {
    int[] sortOrder = _settings.getSortOrders();
    int levels = _settings.getSortOrders().length;
    int[] compareTypes = _settings.getTypes();
    
    for (int level = 0; level < levels; level++) { int cmp;
      int cmp;
      if (compareTypes[level] == 1) {
        Double our = numericValue(level);
        Double their = other.numericValue(level);
        cmp = our.compareTo(their);
      }
      else {
        Comparable our = stringValue(level);
        Comparable their = other.stringValue(level);
        cmp = our.compareTo(their);
      }
      

      if (cmp != 0) {
        return sortOrder[level] == 1 ? 0 - cmp : cmp;
      }
    }
    
    return _node - _node;
  }
  



  public Collator[] getCollator()
  {
    return _collators;
  }
  
  public abstract String extractValueFromDOM(DOM paramDOM, int paramInt1, int paramInt2, AbstractTranslet paramAbstractTranslet, int paramInt3);
}
