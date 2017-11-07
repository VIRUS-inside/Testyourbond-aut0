package org.apache.xerces.xni.grammars;

import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;

public abstract interface XMLSchemaDescription
  extends XMLGrammarDescription
{
  public static final short CONTEXT_INCLUDE = 0;
  public static final short CONTEXT_REDEFINE = 1;
  public static final short CONTEXT_IMPORT = 2;
  public static final short CONTEXT_PREPARSE = 3;
  public static final short CONTEXT_INSTANCE = 4;
  public static final short CONTEXT_ELEMENT = 5;
  public static final short CONTEXT_ATTRIBUTE = 6;
  public static final short CONTEXT_XSITYPE = 7;
  
  public abstract short getContextType();
  
  public abstract String getTargetNamespace();
  
  public abstract String[] getLocationHints();
  
  public abstract QName getTriggeringComponent();
  
  public abstract QName getEnclosingElementName();
  
  public abstract XMLAttributes getAttributes();
}
