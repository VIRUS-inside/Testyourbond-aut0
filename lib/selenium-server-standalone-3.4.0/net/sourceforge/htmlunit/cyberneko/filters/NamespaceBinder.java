package net.sourceforge.htmlunit.cyberneko.filters;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;
import net.sourceforge.htmlunit.cyberneko.HTMLConfiguration;
import net.sourceforge.htmlunit.cyberneko.HTMLElements;
import net.sourceforge.htmlunit.cyberneko.xercesbridge.XercesBridge;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;





















































public class NamespaceBinder
  extends DefaultFilter
{
  public static final String XHTML_1_0_URI = "http://www.w3.org/1999/xhtml";
  public static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
  public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String OVERRIDE_NAMESPACES = "http://cyberneko.org/html/features/override-namespaces";
  protected static final String INSERT_NAMESPACES = "http://cyberneko.org/html/features/insert-namespaces";
  private static final String[] RECOGNIZED_FEATURES = {
    "http://xml.org/sax/features/namespaces", 
    "http://cyberneko.org/html/features/override-namespaces", 
    "http://cyberneko.org/html/features/insert-namespaces" };
  


  private static final Boolean[] FEATURE_DEFAULTS = {
  
    0, Boolean.FALSE, 
    Boolean.FALSE };
  


  protected static final String NAMES_ELEMS = "http://cyberneko.org/html/properties/names/elems";
  


  protected static final String NAMES_ATTRS = "http://cyberneko.org/html/properties/names/attrs";
  


  protected static final String NAMESPACES_URI = "http://cyberneko.org/html/properties/namespaces-uri";
  

  private static final String[] RECOGNIZED_PROPERTIES = {
    "http://cyberneko.org/html/properties/names/elems", 
    "http://cyberneko.org/html/properties/names/attrs", 
    "http://cyberneko.org/html/properties/namespaces-uri" };
  


  private static final Object[] PROPERTY_DEFAULTS = {
  

    00"http://www.w3.org/1999/xhtml" };
  



  protected static final short NAMES_NO_CHANGE = 0;
  



  protected static final short NAMES_UPPERCASE = 1;
  


  protected static final short NAMES_LOWERCASE = 2;
  


  protected boolean fNamespaces;
  


  protected boolean fNamespacePrefixes;
  


  protected boolean fOverrideNamespaces;
  


  protected boolean fInsertNamespaces;
  


  protected short fNamesElems;
  


  protected short fNamesAttrs;
  


  protected String fNamespacesURI;
  


  protected final NamespaceSupport fNamespaceContext = new NamespaceSupport();
  



  private final QName fQName = new QName();
  private final HTMLConfiguration htmlConfiguration_;
  
  public NamespaceBinder(HTMLConfiguration htmlConfiguration)
  {
    htmlConfiguration_ = htmlConfiguration;
  }
  








  public String[] getRecognizedFeatures()
  {
    return merge(super.getRecognizedFeatures(), RECOGNIZED_FEATURES);
  }
  





  public Boolean getFeatureDefault(String featureId)
  {
    for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
      if (RECOGNIZED_FEATURES[i].equals(featureId)) {
        return FEATURE_DEFAULTS[i];
      }
    }
    return super.getFeatureDefault(featureId);
  }
  





  public String[] getRecognizedProperties()
  {
    return merge(super.getRecognizedProperties(), RECOGNIZED_PROPERTIES);
  }
  





  public Object getPropertyDefault(String propertyId)
  {
    for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
      if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
        return PROPERTY_DEFAULTS[i];
      }
    }
    return super.getPropertyDefault(propertyId);
  }
  









  public void reset(XMLComponentManager manager)
    throws XMLConfigurationException
  {
    super.reset(manager);
    

    fNamespaces = manager.getFeature("http://xml.org/sax/features/namespaces");
    fOverrideNamespaces = manager.getFeature("http://cyberneko.org/html/features/override-namespaces");
    fInsertNamespaces = manager.getFeature("http://cyberneko.org/html/features/insert-namespaces");
    

    fNamesElems = getNamesValue(String.valueOf(manager.getProperty("http://cyberneko.org/html/properties/names/elems")));
    fNamesAttrs = getNamesValue(String.valueOf(manager.getProperty("http://cyberneko.org/html/properties/names/attrs")));
    fNamespacesURI = String.valueOf(manager.getProperty("http://cyberneko.org/html/properties/namespaces-uri"));
    

    fNamespaceContext.reset();
  }
  










  public void startDocument(XMLLocator locator, String encoding, NamespaceContext nscontext, Augmentations augs)
    throws XNIException
  {
    super.startDocument(locator, encoding, fNamespaceContext, augs);
  }
  




  public void startElement(QName element, XMLAttributes attrs, Augmentations augs)
    throws XNIException
  {
    if (fNamespaces) {
      fNamespaceContext.pushContext();
      bindNamespaces(element, attrs);
      
      int dcount = fNamespaceContext.getDeclaredPrefixCount();
      if ((fDocumentHandler != null) && (dcount > 0)) {
        for (int i = 0; i < dcount; i++) {
          String prefix = fNamespaceContext.getDeclaredPrefixAt(i);
          String uri = fNamespaceContext.getURI(prefix);
          XercesBridge.getInstance().XMLDocumentHandler_startPrefixMapping(fDocumentHandler, prefix, uri, augs);
        }
      }
    }
    

    super.startElement(element, attrs, augs);
  }
  




  public void emptyElement(QName element, XMLAttributes attrs, Augmentations augs)
    throws XNIException
  {
    if (fNamespaces) {
      fNamespaceContext.pushContext();
      bindNamespaces(element, attrs);
      
      int dcount = fNamespaceContext.getDeclaredPrefixCount();
      if ((fDocumentHandler != null) && (dcount > 0)) {
        for (int i = 0; i < dcount; i++) {
          String prefix = fNamespaceContext.getDeclaredPrefixAt(i);
          String uri = fNamespaceContext.getURI(prefix);
          XercesBridge.getInstance().XMLDocumentHandler_startPrefixMapping(fDocumentHandler, prefix, uri, augs);
        }
      }
    }
    

    super.emptyElement(element, attrs, augs);
    

    if (fNamespaces) {
      int dcount = fNamespaceContext.getDeclaredPrefixCount();
      if ((fDocumentHandler != null) && (dcount > 0)) {
        for (int i = dcount - 1; i >= 0; i--) {
          String prefix = fNamespaceContext.getDeclaredPrefixAt(i);
          XercesBridge.getInstance().XMLDocumentHandler_endPrefixMapping(fDocumentHandler, prefix, augs);
        }
      }
      
      fNamespaceContext.popContext();
    }
  }
  




  public void endElement(QName element, Augmentations augs)
    throws XNIException
  {
    if (fNamespaces) {
      bindNamespaces(element, null);
    }
    

    super.endElement(element, augs);
    

    if (fNamespaces) {
      int dcount = fNamespaceContext.getDeclaredPrefixCount();
      if ((fDocumentHandler != null) && (dcount > 0)) {
        for (int i = dcount - 1; i >= 0; i--) {
          String prefix = fNamespaceContext.getDeclaredPrefixAt(i);
          XercesBridge.getInstance().XMLDocumentHandler_endPrefixMapping(fDocumentHandler, prefix, augs);
        }
      }
      
      fNamespaceContext.popContext();
    }
  }
  





  protected static void splitQName(QName qname)
  {
    int index = rawname.indexOf(':');
    if (index != -1) {
      prefix = rawname.substring(0, index);
      localpart = rawname.substring(index + 1);
    }
  }
  






  protected static final short getNamesValue(String value)
  {
    if (value.equals("lower")) return 2;
    if (value.equals("upper")) return 1;
    return 0;
  }
  
  protected static final String modifyName(String name, short mode)
  {
    switch (mode) {
    case 1:  return name.toUpperCase(Locale.ENGLISH);
    case 2:  return name.toLowerCase(Locale.ENGLISH);
    }
    return name;
  }
  






  protected void bindNamespaces(QName element, XMLAttributes attrs)
  {
    splitQName(element);
    

    int attrCount = attrs != null ? attrs.getLength() : 0;
    for (int i = attrCount - 1; i >= 0; i--) {
      attrs.getName(i, fQName);
      String aname = fQName.rawname;
      String ANAME = aname.toUpperCase(Locale.ENGLISH);
      if ((ANAME.startsWith("XMLNS:")) || (ANAME.equals("XMLNS"))) {
        int anamelen = aname.length();
        

        String aprefix = anamelen > 5 ? aname.substring(0, 5) : null;
        String alocal = anamelen > 5 ? aname.substring(6) : aname;
        String avalue = attrs.getValue(i);
        

        if (anamelen > 5) {
          aprefix = modifyName(aprefix, (short)2);
          alocal = modifyName(alocal, fNamesElems);
          aname = aprefix + ':' + alocal;
        }
        else {
          alocal = modifyName(alocal, (short)2);
          aname = alocal;
        }
        fQName.setValues(aprefix, alocal, aname, null);
        attrs.setName(i, fQName);
        

        String prefix = alocal != aname ? alocal : "";
        String uri = avalue.length() > 0 ? avalue : null;
        if ((fOverrideNamespaces) && 
          (prefix.equals(prefix)) && 
          (htmlConfiguration_.htmlElements_.getElement(localpart, null) != null)) {
          uri = fNamespacesURI;
        }
        fNamespaceContext.declarePrefix(prefix, uri);
      }
    }
    

    String prefix = prefix != null ? prefix : "";
    uri = fNamespaceContext.getURI(prefix);
    




    if ((uri != null) && (prefix == null)) {
      prefix = "";
    }
    

    if ((fInsertNamespaces) && (attrs != null) && 
      (htmlConfiguration_.htmlElements_.getElement(localpart, null) != null) && (
      (prefix == null) || 
      (fNamespaceContext.getURI(prefix) == null))) {
      String xmlns = "xmlns" + (prefix != null ? 
        ":" + prefix : "");
      fQName.setValues(null, xmlns, xmlns, null);
      attrs.addAttribute(fQName, "CDATA", fNamespacesURI);
      bindNamespaces(element, attrs);
      return;
    }
    


    attrCount = attrs != null ? attrs.getLength() : 0;
    for (int i = 0; i < attrCount; i++) {
      attrs.getName(i, fQName);
      splitQName(fQName);
      prefix = !fQName.rawname.equals("xmlns") ? 
        "" : fQName.prefix != null ? fQName.prefix : "xmlns";
      
      if (!prefix.equals("")) {
        fQName.uri = (prefix.equals("xml") ? "http://www.w3.org/XML/1998/namespace" : fNamespaceContext.getURI(prefix));
      }
      

      if ((prefix.equals("xmlns")) && (fQName.uri == null)) {
        fQName.uri = "http://www.w3.org/2000/xmlns/";
      }
      attrs.setName(i, fQName);
    }
  }
  















  public static class NamespaceSupport
    implements NamespaceContext
  {
    protected int fTop = 0;
    

    protected int[] fLevels = new int[10];
    

    protected Entry[] fEntries = new Entry[10];
    




    public NamespaceSupport()
    {
      pushContext();
      declarePrefix("xml", NamespaceContext.XML_URI);
      declarePrefix("xmlns", NamespaceContext.XMLNS_URI);
    }
    







    public String getURI(String prefix)
    {
      for (int i = fLevels[fTop] - 1; i >= 0; i--) {
        Entry entry = fEntries[i];
        if (prefix.equals(prefix)) {
          return uri;
        }
      }
      return null;
    }
    

    public int getDeclaredPrefixCount()
    {
      return fLevels[fTop] - fLevels[(fTop - 1)];
    }
    

    public String getDeclaredPrefixAt(int index)
    {
      return fEntries[(fLevels[(fTop - 1)] + index)].prefix;
    }
    
    public NamespaceContext getParentContext()
    {
      return this;
    }
    



    public void reset()
    {
      int tmp6_5 = 1;fTop = tmp6_5;fLevels[tmp6_5] = fLevels[(fTop - 1)];
    }
    

    public void pushContext()
    {
      if (++fTop == fLevels.length) {
        int[] iarray = new int[fLevels.length + 10];
        System.arraycopy(fLevels, 0, iarray, 0, fLevels.length);
        fLevels = iarray;
      }
      fLevels[fTop] = fLevels[(fTop - 1)];
    }
    

    public void popContext()
    {
      if (fTop > 1) {
        fTop -= 1;
      }
    }
    

    public boolean declarePrefix(String prefix, String uri)
    {
      int count = getDeclaredPrefixCount();
      for (int i = 0; i < count; i++) {
        String dprefix = getDeclaredPrefixAt(i);
        if (dprefix.equals(prefix)) {
          return false;
        }
      }
      Entry entry = new Entry(prefix, uri);
      if (fLevels[fTop] == fEntries.length) {
        Entry[] earray = new Entry[fEntries.length + 10];
        System.arraycopy(fEntries, 0, earray, 0, fEntries.length);
        fEntries = earray;
      }
      int tmp114_111 = fTop; int[] tmp114_107 = fLevels; int tmp116_115 = tmp114_107[tmp114_111];tmp114_107[tmp114_111] = (tmp116_115 + 1);fEntries[tmp116_115] = entry;
      return true;
    }
    

    public String getPrefix(String uri)
    {
      for (int i = fLevels[fTop] - 1; i >= 0; i--) {
        Entry entry = fEntries[i];
        if (uri.equals(uri)) {
          return prefix;
        }
      }
      return null;
    }
    

    public Enumeration<String> getAllPrefixes()
    {
      Vector<String> prefixes = new Vector();
      for (int i = fLevels[1]; i < fLevels[fTop]; i++) {
        String prefix = fEntries[i].prefix;
        if (!prefixes.contains(prefix)) {
          prefixes.addElement(prefix);
        }
      }
      return prefixes.elements();
    }
    






    static class Entry
    {
      public String prefix;
      




      public String uri;
      





      public Entry(String prefix, String uri)
      {
        this.prefix = prefix;
        this.uri = uri;
      }
    }
  }
}
