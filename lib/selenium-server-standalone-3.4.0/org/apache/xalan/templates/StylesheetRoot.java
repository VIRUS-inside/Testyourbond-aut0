package org.apache.xalan.templates;

import java.io.Serializable;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExtensionNamespacesManager;
import org.apache.xalan.processor.XSLTSchema;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;































public class StylesheetRoot
  extends StylesheetComposed
  implements Serializable, Templates
{
  static final long serialVersionUID = 3875353123529147855L;
  private boolean m_optimizer = true;
  



  private boolean m_incremental = false;
  



  private boolean m_source_location = false;
  



  private boolean m_isSecureProcessing = false;
  

  private HashMap m_availElems;
  

  public StylesheetRoot(ErrorListener errorListener)
    throws TransformerConfigurationException
  {
    super(null);
    
    setStylesheetRoot(this);
    
    try
    {
      m_selectDefault = new XPath("node()", this, this, 0, errorListener);
      
      initDefaultRule(errorListener);
    }
    catch (TransformerException se)
    {
      throw new TransformerConfigurationException(XSLMessages.createMessage("ER_CANNOT_INIT_DEFAULT_TEMPLATES", null), se);
    }
  }
  













  public StylesheetRoot(XSLTSchema schema, ErrorListener listener)
    throws TransformerConfigurationException
  {
    this(listener);
    m_availElems = schema.getElemsAvailable();
  }
  





  public boolean isRoot()
  {
    return true;
  }
  



  public void setSecureProcessing(boolean flag)
  {
    m_isSecureProcessing = flag;
  }
  



  public boolean isSecureProcessing()
  {
    return m_isSecureProcessing;
  }
  






  public HashMap getAvailableElements()
  {
    return m_availElems;
  }
  
  private transient ExtensionNamespacesManager m_extNsMgr = null;
  
  private StylesheetComposed[] m_globalImportList;
  
  private OutputProperties m_outputProperties;
  
  public ExtensionNamespacesManager getExtensionNamespacesManager()
  {
    if (m_extNsMgr == null)
      m_extNsMgr = new ExtensionNamespacesManager();
    return m_extNsMgr;
  }
  





  public Vector getExtensions()
  {
    return m_extNsMgr != null ? m_extNsMgr.getExtensions() : null;
  }
  

















  public Transformer newTransformer()
  {
    return new TransformerImpl(this);
  }
  

  public Properties getDefaultOutputProps()
  {
    return m_outputProperties.getProperties();
  }
  











  public Properties getOutputProperties()
  {
    return (Properties)getDefaultOutputProps().clone();
  }
  














  public void recompose()
    throws TransformerException
  {
    Vector recomposableElements = new Vector();
    


    if (null == m_globalImportList)
    {

      Vector importList = new Vector();
      
      addImports(this, true, importList);
      






      m_globalImportList = new StylesheetComposed[importList.size()];
      
      int i = 0; for (int j = importList.size() - 1; i < importList.size(); i++)
      {
        m_globalImportList[j] = ((StylesheetComposed)importList.elementAt(i));
        


        m_globalImportList[j].recomposeIncludes(m_globalImportList[j]);
        
        m_globalImportList[(j--)].recomposeImports();
      }
    }
    
    int n = getGlobalImportCount();
    
    for (int i = 0; i < n; i++)
    {
      StylesheetComposed imported = getGlobalImport(i);
      imported.recompose(recomposableElements);
    }
    


    QuickSort2(recomposableElements, 0, recomposableElements.size() - 1);
    



    m_outputProperties = new OutputProperties("");
    

    m_attrSets = new HashMap();
    m_decimalFormatSymbols = new Hashtable();
    m_keyDecls = new Vector();
    m_namespaceAliasComposed = new Hashtable();
    m_templateList = new TemplateList();
    m_variables = new Vector();
    




    for (int i = recomposableElements.size() - 1; i >= 0; i--) {
      ((ElemTemplateElement)recomposableElements.elementAt(i)).recompose(this);
    }
    














    initComposeState();
    

    m_templateList.compose(this);
    

    m_outputProperties.compose(this);
    m_outputProperties.endCompose(this);
    



    n = getGlobalImportCount();
    
    for (int i = 0; i < n; i++)
    {
      StylesheetComposed imported = getGlobalImport(i);
      int includedCount = imported.getIncludeCountComposed();
      for (int j = -1; j < includedCount; j++)
      {
        Stylesheet included = imported.getIncludeComposed(j);
        composeTemplates(included);
      }
    }
    
    if (m_extNsMgr != null) {
      m_extNsMgr.registerUnregisteredNamespaces();
    }
    clearComposeState();
  }
  







  void composeTemplates(ElemTemplateElement templ)
    throws TransformerException
  {
    templ.compose(this);
    
    for (ElemTemplateElement child = templ.getFirstChildElem(); 
        child != null; child = child.getNextSiblingElem())
    {
      composeTemplates(child);
    }
    
    templ.endCompose(this);
  }
  






























  protected void addImports(Stylesheet stylesheet, boolean addToList, Vector importList)
  {
    int n = stylesheet.getImportCount();
    
    if (n > 0)
    {
      for (int i = 0; i < n; i++)
      {
        Stylesheet imported = stylesheet.getImport(i);
        
        addImports(imported, true, importList);
      }
    }
    
    n = stylesheet.getIncludeCount();
    
    if (n > 0)
    {
      for (int i = 0; i < n; i++)
      {
        Stylesheet included = stylesheet.getInclude(i);
        
        addImports(included, false, importList);
      }
    }
    
    if (addToList) {
      importList.addElement(stylesheet);
    }
  }
  








  public StylesheetComposed getGlobalImport(int i)
  {
    return m_globalImportList[i];
  }
  








  public int getGlobalImportCount()
  {
    return m_globalImportList != null ? m_globalImportList.length : 1;
  }
  











  public int getImportNumber(StylesheetComposed sheet)
  {
    if (this == sheet) {
      return 0;
    }
    int n = getGlobalImportCount();
    
    for (int i = 0; i < n; i++)
    {
      if (sheet == getGlobalImport(i)) {
        return i;
      }
    }
    return -1;
  }
  













  void recomposeOutput(OutputProperties oprops)
    throws TransformerException
  {
    m_outputProperties.copyFrom(oprops);
  }
  














  public OutputProperties getOutputComposed()
  {
    return m_outputProperties;
  }
  


  private boolean m_outputMethodSet = false;
  private HashMap m_attrSets;
  private Hashtable m_decimalFormatSymbols;
  private Vector m_keyDecls;
  private Hashtable m_namespaceAliasComposed;
  private TemplateList m_templateList;
  private Vector m_variables;
  
  public boolean isOutputMethodSet()
  {
    return m_outputMethodSet;
  }
  












  void recomposeAttributeSets(ElemAttributeSet attrSet)
  {
    ArrayList attrSetList = (ArrayList)m_attrSets.get(attrSet.getName());
    
    if (null == attrSetList)
    {
      attrSetList = new ArrayList();
      
      m_attrSets.put(attrSet.getName(), attrSetList);
    }
    
    attrSetList.add(attrSet);
  }
  










  public ArrayList getAttributeSetComposed(QName name)
    throws ArrayIndexOutOfBoundsException
  {
    return (ArrayList)m_attrSets.get(name);
  }
  











  void recomposeDecimalFormats(DecimalFormatProperties dfp)
  {
    DecimalFormatSymbols oldDfs = (DecimalFormatSymbols)m_decimalFormatSymbols.get(dfp.getName());
    
    if (null == oldDfs)
    {
      m_decimalFormatSymbols.put(dfp.getName(), dfp.getDecimalFormatSymbols());
    }
    else if (!dfp.getDecimalFormatSymbols().equals(oldDfs)) {
      String themsg;
      String themsg;
      if (dfp.getName().equals(new QName("")))
      {

        themsg = XSLMessages.createWarning("WG_ONE_DEFAULT_XSLDECIMALFORMAT_ALLOWED", new Object[0]);


      }
      else
      {

        themsg = XSLMessages.createWarning("WG_XSLDECIMALFORMAT_NAMES_MUST_BE_UNIQUE", new Object[] { dfp.getName() });
      }
      


      error(themsg);
    }
  }
  
















  public DecimalFormatSymbols getDecimalFormatComposed(QName name)
  {
    return (DecimalFormatSymbols)m_decimalFormatSymbols.get(name);
  }
  












  void recomposeKeys(KeyDeclaration keyDecl)
  {
    m_keyDecls.addElement(keyDecl);
  }
  






  public Vector getKeysComposed()
  {
    return m_keyDecls;
  }
  











  void recomposeNamespaceAliases(NamespaceAlias nsAlias)
  {
    m_namespaceAliasComposed.put(nsAlias.getStylesheetNamespace(), nsAlias);
  }
  










  public NamespaceAlias getNamespaceAliasComposed(String uri)
  {
    return (NamespaceAlias)(null == m_namespaceAliasComposed ? null : m_namespaceAliasComposed.get(uri));
  }
  












  void recomposeTemplates(ElemTemplate template)
  {
    m_templateList.setTemplate(template);
  }
  






  public final TemplateList getTemplateListComposed()
  {
    return m_templateList;
  }
  







  public final void setTemplateListComposed(TemplateList templateList)
  {
    m_templateList = templateList;
  }
  



















  public ElemTemplate getTemplateComposed(XPathContext xctxt, int targetNode, QName mode, boolean quietConflictWarnings, DTM dtm)
    throws TransformerException
  {
    return m_templateList.getTemplate(xctxt, targetNode, mode, quietConflictWarnings, dtm);
  }
  



  private TemplateList m_whiteSpaceInfoList;
  


  private ElemTemplate m_defaultTextRule;
  


  private ElemTemplate m_defaultRule;
  

  private ElemTemplate m_defaultRootRule;
  

  private ElemTemplate m_startRule;
  

  XPath m_selectDefault;
  

  private transient ComposeState m_composeState;
  

  public ElemTemplate getTemplateComposed(XPathContext xctxt, int targetNode, QName mode, int maxImportLevel, int endImportLevel, boolean quietConflictWarnings, DTM dtm)
    throws TransformerException
  {
    return m_templateList.getTemplate(xctxt, targetNode, mode, maxImportLevel, endImportLevel, quietConflictWarnings, dtm);
  }
  












  public ElemTemplate getTemplateComposed(QName qname)
  {
    return m_templateList.getTemplate(qname);
  }
  












  void recomposeVariables(ElemVariable elemVar)
  {
    if (getVariableOrParamComposed(elemVar.getName()) == null)
    {
      elemVar.setIsTopLevel(true);
      elemVar.setIndex(m_variables.size());
      m_variables.addElement(elemVar);
    }
  }
  








  public ElemVariable getVariableOrParamComposed(QName qname)
  {
    if (null != m_variables)
    {
      int n = m_variables.size();
      
      for (int i = 0; i < n; i++)
      {
        ElemVariable var = (ElemVariable)m_variables.elementAt(i);
        if (var.getName().equals(qname)) {
          return var;
        }
      }
    }
    return null;
  }
  






  public Vector getVariablesAndParamsComposed()
  {
    return m_variables;
  }
  












  void recomposeWhiteSpaceInfo(WhiteSpaceInfo wsi)
  {
    if (null == m_whiteSpaceInfoList) {
      m_whiteSpaceInfoList = new TemplateList();
    }
    m_whiteSpaceInfoList.setTemplate(wsi);
  }
  







  public boolean shouldCheckWhitespace()
  {
    return null != m_whiteSpaceInfoList;
  }
  












  public WhiteSpaceInfo getWhiteSpaceInfo(XPathContext support, int targetElement, DTM dtm)
    throws TransformerException
  {
    if (null != m_whiteSpaceInfoList) {
      return (WhiteSpaceInfo)m_whiteSpaceInfoList.getTemplate(support, targetElement, null, false, dtm);
    }
    
    return null;
  }
  











  public boolean shouldStripWhiteSpace(XPathContext support, int targetElement)
    throws TransformerException
  {
    if (null != m_whiteSpaceInfoList)
    {
      while (-1 != targetElement)
      {
        DTM dtm = support.getDTM(targetElement);
        WhiteSpaceInfo info = (WhiteSpaceInfo)m_whiteSpaceInfoList.getTemplate(support, targetElement, null, false, dtm);
        
        if (null != info) {
          return info.getShouldStripSpace();
        }
        int parent = dtm.getParent(targetElement);
        if ((-1 != parent) && (1 == dtm.getNodeType(parent))) {
          targetElement = parent;
        } else
          targetElement = -1;
      }
    }
    return false;
  }
  






  public boolean canStripWhiteSpace()
  {
    return null != m_whiteSpaceInfoList;
  }
  
















  public final ElemTemplate getDefaultTextRule()
  {
    return m_defaultTextRule;
  }
  














  public final ElemTemplate getDefaultRule()
  {
    return m_defaultRule;
  }
  



















  public final ElemTemplate getDefaultRootRule()
  {
    return m_defaultRootRule;
  }
  













  public final ElemTemplate getStartRule()
  {
    return m_startRule;
  }
  













  private void initDefaultRule(ErrorListener errorListener)
    throws TransformerException
  {
    m_defaultRule = new ElemTemplate();
    
    m_defaultRule.setStylesheet(this);
    
    XPath defMatch = new XPath("*", this, this, 1, errorListener);
    
    m_defaultRule.setMatch(defMatch);
    
    ElemApplyTemplates childrenElement = new ElemApplyTemplates();
    
    childrenElement.setIsDefaultTemplate(true);
    childrenElement.setSelect(m_selectDefault);
    m_defaultRule.appendChild(childrenElement);
    
    m_startRule = m_defaultRule;
    

    m_defaultTextRule = new ElemTemplate();
    
    m_defaultTextRule.setStylesheet(this);
    
    defMatch = new XPath("text() | @*", this, this, 1, errorListener);
    
    m_defaultTextRule.setMatch(defMatch);
    
    ElemValueOf elemValueOf = new ElemValueOf();
    
    m_defaultTextRule.appendChild(elemValueOf);
    
    XPath selectPattern = new XPath(".", this, this, 0, errorListener);
    
    elemValueOf.setSelect(selectPattern);
    

    m_defaultRootRule = new ElemTemplate();
    
    m_defaultRootRule.setStylesheet(this);
    
    defMatch = new XPath("/", this, this, 1, errorListener);
    
    m_defaultRootRule.setMatch(defMatch);
    
    childrenElement = new ElemApplyTemplates();
    
    childrenElement.setIsDefaultTemplate(true);
    m_defaultRootRule.appendChild(childrenElement);
    childrenElement.setSelect(m_selectDefault);
  }
  



















  private void QuickSort2(Vector v, int lo0, int hi0)
  {
    int lo = lo0;
    int hi = hi0;
    
    if (hi0 > lo0)
    {


      ElemTemplateElement midNode = (ElemTemplateElement)v.elementAt((lo0 + hi0) / 2);
      

      while (lo <= hi)
      {


        while ((lo < hi0) && (((ElemTemplateElement)v.elementAt(lo)).compareTo(midNode) < 0))
        {
          lo++;
        }
        


        while ((hi > lo0) && (((ElemTemplateElement)v.elementAt(hi)).compareTo(midNode) > 0)) {
          hi--;
        }
        

        if (lo <= hi)
        {
          ElemTemplateElement node = (ElemTemplateElement)v.elementAt(lo);
          v.setElementAt(v.elementAt(hi), lo);
          v.setElementAt(node, hi);
          
          lo++;
          hi--;
        }
      }
      


      if (lo0 < hi)
      {
        QuickSort2(v, lo0, hi);
      }
      


      if (lo < hi0)
      {
        QuickSort2(v, lo, hi0);
      }
    }
  }
  





  void initComposeState()
  {
    m_composeState = new ComposeState();
  }
  




  ComposeState getComposeState()
  {
    return m_composeState;
  }
  



  private void clearComposeState()
  {
    m_composeState = null;
  }
  
  private String m_extensionHandlerClass = "org.apache.xalan.extensions.ExtensionHandlerExsltFunction";
  






  public String setExtensionHandlerClass(String handlerClassName)
  {
    String oldvalue = m_extensionHandlerClass;
    m_extensionHandlerClass = handlerClassName;
    return oldvalue;
  }
  


  public String getExtensionHandlerClass()
  {
    return m_extensionHandlerClass;
  }
  



  class ComposeState
  {
    ComposeState()
    {
      int size = m_variables.size();
      for (int i = 0; i < size; i++)
      {
        ElemVariable ev = (ElemVariable)m_variables.elementAt(i);
        m_variableNames.addElement(ev.getName());
      }
    }
    

    private ExpandedNameTable m_ent = new ExpandedNameTable();
    









    public int getQNameID(QName qname)
    {
      return m_ent.getExpandedTypeID(qname.getNamespace(), qname.getLocalName(), 1);
    }
    








    private Vector m_variableNames = new Vector();
    






    int addVariableName(QName qname)
    {
      int pos = m_variableNames.size();
      m_variableNames.addElement(qname);
      int frameSize = m_variableNames.size() - getGlobalsSize();
      if (frameSize > m_maxStackFrameSize)
        m_maxStackFrameSize += 1;
      return pos;
    }
    
    void resetStackFrameSize()
    {
      m_maxStackFrameSize = 0;
    }
    
    int getFrameSize()
    {
      return m_maxStackFrameSize;
    }
    





    int getCurrentStackFrameSize()
    {
      return m_variableNames.size();
    }
    



    void setCurrentStackFrameSize(int sz)
    {
      m_variableNames.setSize(sz);
    }
    
    int getGlobalsSize()
    {
      return m_variables.size();
    }
    
    IntStack m_marks = new IntStack();
    private int m_maxStackFrameSize;
    
    void pushStackMark() {
      m_marks.push(getCurrentStackFrameSize());
    }
    
    void popStackMark()
    {
      int mark = m_marks.pop();
      setCurrentStackFrameSize(mark);
    }
    







    Vector getVariableNames()
    {
      return m_variableNames;
    }
  }
  





  public boolean getOptimizer()
  {
    return m_optimizer;
  }
  


  public void setOptimizer(boolean b)
  {
    m_optimizer = b;
  }
  


  public boolean getIncremental()
  {
    return m_incremental;
  }
  


  public boolean getSource_location()
  {
    return m_source_location;
  }
  


  public void setIncremental(boolean b)
  {
    m_incremental = b;
  }
  


  public void setSource_location(boolean b)
  {
    m_source_location = b;
  }
}
