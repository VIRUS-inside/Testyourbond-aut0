package org.apache.xalan.xsltc.dom;

import java.text.Collator;
import java.util.Locale;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.utils.LocaleUtility;






















public class NodeSortRecordFactory
{
  private static int DESCENDING = "descending".length();
  private static int NUMBER = "number".length();
  


  private final DOM _dom;
  

  private final String _className;
  

  private Class _class;
  

  private SortSettings _sortSettings;
  

  protected Collator _collator;
  


  /**
   * @deprecated
   */
  public NodeSortRecordFactory(DOM dom, String className, Translet translet, String[] order, String[] type)
    throws TransletException
  {
    this(dom, className, translet, order, type, null, null);
  }
  








  public NodeSortRecordFactory(DOM dom, String className, Translet translet, String[] order, String[] type, String[] lang, String[] caseOrder)
    throws TransletException
  {
    try
    {
      _dom = dom;
      _className = className;
      
      _class = translet.getAuxiliaryClass(className);
      
      if (_class == null) {
        _class = ObjectFactory.findProviderClass(className, ObjectFactory.findClassLoader(), true);
      }
      

      int levels = order.length;
      int[] iOrder = new int[levels];
      int[] iType = new int[levels];
      for (int i = 0; i < levels; i++) {
        if (order[i].length() == DESCENDING) {
          iOrder[i] = 1;
        }
        if (type[i].length() == NUMBER) {
          iType[i] = 1;
        }
      }
      



      String[] emptyStringArray = null;
      if ((lang == null) || (caseOrder == null)) {
        int numSortKeys = order.length;
        emptyStringArray = new String[numSortKeys];
        


        for (int i = 0; i < numSortKeys; i++) {
          emptyStringArray[i] = "";
        }
      }
      
      if (lang == null) {
        lang = emptyStringArray;
      }
      if (caseOrder == null) {
        caseOrder = emptyStringArray;
      }
      
      int length = lang.length;
      Locale[] locales = new Locale[length];
      Collator[] collators = new Collator[length];
      for (int i = 0; i < length; i++) {
        locales[i] = LocaleUtility.langToLocale(lang[i]);
        collators[i] = Collator.getInstance(locales[i]);
      }
      
      _sortSettings = new SortSettings((AbstractTranslet)translet, iOrder, iType, locales, collators, caseOrder);
    }
    catch (ClassNotFoundException e)
    {
      throw new TransletException(e);
    }
  }
  











  public NodeSortRecord makeNodeSortRecord(int node, int last)
    throws ExceptionInInitializerError, LinkageError, IllegalAccessException, InstantiationException, SecurityException, TransletException
  {
    NodeSortRecord sortRecord = (NodeSortRecord)_class.newInstance();
    
    sortRecord.initialize(node, last, _dom, _sortSettings);
    return sortRecord;
  }
  
  public String getClassName() {
    return _className;
  }
  
  private final void setLang(String[] lang) {}
}
