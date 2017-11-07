package org.apache.xerces.impl.xs.traversers;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaGrammar.BuiltinSchemaGrammar;
import org.apache.xerces.impl.xs.SchemaNamespaceSupport;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAttributeDecl;
import org.apache.xerces.impl.xs.XSGrammarBucket;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XIntPool;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class XSAttributeChecker
{
  private static final String ELEMENT_N = "element_n";
  private static final String ELEMENT_R = "element_r";
  private static final String ATTRIBUTE_N = "attribute_n";
  private static final String ATTRIBUTE_R = "attribute_r";
  private static int ATTIDX_COUNT = 0;
  public static final int ATTIDX_ABSTRACT = ATTIDX_COUNT++;
  public static final int ATTIDX_AFORMDEFAULT = ATTIDX_COUNT++;
  public static final int ATTIDX_BASE = ATTIDX_COUNT++;
  public static final int ATTIDX_BLOCK = ATTIDX_COUNT++;
  public static final int ATTIDX_BLOCKDEFAULT = ATTIDX_COUNT++;
  public static final int ATTIDX_DEFAULT = ATTIDX_COUNT++;
  public static final int ATTIDX_EFORMDEFAULT = ATTIDX_COUNT++;
  public static final int ATTIDX_FINAL = ATTIDX_COUNT++;
  public static final int ATTIDX_FINALDEFAULT = ATTIDX_COUNT++;
  public static final int ATTIDX_FIXED = ATTIDX_COUNT++;
  public static final int ATTIDX_FORM = ATTIDX_COUNT++;
  public static final int ATTIDX_ID = ATTIDX_COUNT++;
  public static final int ATTIDX_ITEMTYPE = ATTIDX_COUNT++;
  public static final int ATTIDX_MAXOCCURS = ATTIDX_COUNT++;
  public static final int ATTIDX_MEMBERTYPES = ATTIDX_COUNT++;
  public static final int ATTIDX_MINOCCURS = ATTIDX_COUNT++;
  public static final int ATTIDX_MIXED = ATTIDX_COUNT++;
  public static final int ATTIDX_NAME = ATTIDX_COUNT++;
  public static final int ATTIDX_NAMESPACE = ATTIDX_COUNT++;
  public static final int ATTIDX_NAMESPACE_LIST = ATTIDX_COUNT++;
  public static final int ATTIDX_NILLABLE = ATTIDX_COUNT++;
  public static final int ATTIDX_NONSCHEMA = ATTIDX_COUNT++;
  public static final int ATTIDX_PROCESSCONTENTS = ATTIDX_COUNT++;
  public static final int ATTIDX_PUBLIC = ATTIDX_COUNT++;
  public static final int ATTIDX_REF = ATTIDX_COUNT++;
  public static final int ATTIDX_REFER = ATTIDX_COUNT++;
  public static final int ATTIDX_SCHEMALOCATION = ATTIDX_COUNT++;
  public static final int ATTIDX_SOURCE = ATTIDX_COUNT++;
  public static final int ATTIDX_SUBSGROUP = ATTIDX_COUNT++;
  public static final int ATTIDX_SYSTEM = ATTIDX_COUNT++;
  public static final int ATTIDX_TARGETNAMESPACE = ATTIDX_COUNT++;
  public static final int ATTIDX_TYPE = ATTIDX_COUNT++;
  public static final int ATTIDX_USE = ATTIDX_COUNT++;
  public static final int ATTIDX_VALUE = ATTIDX_COUNT++;
  public static final int ATTIDX_ENUMNSDECLS = ATTIDX_COUNT++;
  public static final int ATTIDX_VERSION = ATTIDX_COUNT++;
  public static final int ATTIDX_XML_LANG = ATTIDX_COUNT++;
  public static final int ATTIDX_XPATH = ATTIDX_COUNT++;
  public static final int ATTIDX_FROMDEFAULT = ATTIDX_COUNT++;
  public static final int ATTIDX_ISRETURNED = ATTIDX_COUNT++;
  private static final XIntPool fXIntPool = new XIntPool();
  private static final XInt INT_QUALIFIED = fXIntPool.getXInt(1);
  private static final XInt INT_UNQUALIFIED = fXIntPool.getXInt(0);
  private static final XInt INT_EMPTY_SET = fXIntPool.getXInt(0);
  private static final XInt INT_ANY_STRICT = fXIntPool.getXInt(1);
  private static final XInt INT_ANY_LAX = fXIntPool.getXInt(3);
  private static final XInt INT_ANY_SKIP = fXIntPool.getXInt(2);
  private static final XInt INT_ANY_ANY = fXIntPool.getXInt(1);
  private static final XInt INT_ANY_LIST = fXIntPool.getXInt(3);
  private static final XInt INT_ANY_NOT = fXIntPool.getXInt(2);
  private static final XInt INT_USE_OPTIONAL = fXIntPool.getXInt(0);
  private static final XInt INT_USE_REQUIRED = fXIntPool.getXInt(1);
  private static final XInt INT_USE_PROHIBITED = fXIntPool.getXInt(2);
  private static final XInt INT_WS_PRESERVE = fXIntPool.getXInt(0);
  private static final XInt INT_WS_REPLACE = fXIntPool.getXInt(1);
  private static final XInt INT_WS_COLLAPSE = fXIntPool.getXInt(2);
  private static final XInt INT_UNBOUNDED = fXIntPool.getXInt(-1);
  private static final Hashtable fEleAttrsMapG = new Hashtable(29);
  private static final Hashtable fEleAttrsMapL = new Hashtable(79);
  protected static final int DT_ANYURI = 0;
  protected static final int DT_ID = 1;
  protected static final int DT_QNAME = 2;
  protected static final int DT_STRING = 3;
  protected static final int DT_TOKEN = 4;
  protected static final int DT_NCNAME = 5;
  protected static final int DT_XPATH = 6;
  protected static final int DT_XPATH1 = 7;
  protected static final int DT_LANGUAGE = 8;
  protected static final int DT_COUNT = 9;
  private static final XSSimpleType[] fExtraDVs = new XSSimpleType[9];
  protected static final int DT_BLOCK = -1;
  protected static final int DT_BLOCK1 = -2;
  protected static final int DT_FINAL = -3;
  protected static final int DT_FINAL1 = -4;
  protected static final int DT_FINAL2 = -5;
  protected static final int DT_FORM = -6;
  protected static final int DT_MAXOCCURS = -7;
  protected static final int DT_MAXOCCURS1 = -8;
  protected static final int DT_MEMBERTYPES = -9;
  protected static final int DT_MINOCCURS1 = -10;
  protected static final int DT_NAMESPACE = -11;
  protected static final int DT_PROCESSCONTENTS = -12;
  protected static final int DT_USE = -13;
  protected static final int DT_WHITESPACE = -14;
  protected static final int DT_BOOLEAN = -15;
  protected static final int DT_NONNEGINT = -16;
  protected static final int DT_POSINT = -17;
  protected XSDHandler fSchemaHandler = null;
  protected SymbolTable fSymbolTable = null;
  protected Hashtable fNonSchemaAttrs = new Hashtable();
  protected Vector fNamespaceList = new Vector();
  protected boolean[] fSeen = new boolean[ATTIDX_COUNT];
  private static boolean[] fSeenTemp = new boolean[ATTIDX_COUNT];
  static final int INIT_POOL_SIZE = 10;
  static final int INC_POOL_SIZE = 10;
  Object[][] fArrayPool = new Object[10][ATTIDX_COUNT];
  private static Object[] fTempArray = new Object[ATTIDX_COUNT];
  int fPoolPos = 0;
  
  public XSAttributeChecker(XSDHandler paramXSDHandler)
  {
    fSchemaHandler = paramXSDHandler;
  }
  
  public void reset(SymbolTable paramSymbolTable)
  {
    fSymbolTable = paramSymbolTable;
    fNonSchemaAttrs.clear();
  }
  
  public Object[] checkAttributes(Element paramElement, boolean paramBoolean, XSDocumentInfo paramXSDocumentInfo)
  {
    return checkAttributes(paramElement, paramBoolean, paramXSDocumentInfo, false);
  }
  
  public Object[] checkAttributes(Element paramElement, boolean paramBoolean1, XSDocumentInfo paramXSDocumentInfo, boolean paramBoolean2)
  {
    if (paramElement == null) {
      return null;
    }
    Attr[] arrayOfAttr = DOMUtil.getAttrs(paramElement);
    resolveNamespace(paramElement, arrayOfAttr, fNamespaceSupport);
    String str1 = DOMUtil.getNamespaceURI(paramElement);
    String str2 = DOMUtil.getLocalName(paramElement);
    if (!SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(str1)) {
      reportSchemaError("s4s-elt-schema-ns", new Object[] { str2 }, paramElement);
    }
    Hashtable localHashtable = fEleAttrsMapG;
    String str3 = str2;
    if (!paramBoolean1)
    {
      localHashtable = fEleAttrsMapL;
      if (str2.equals(SchemaSymbols.ELT_ELEMENT))
      {
        if (DOMUtil.getAttr(paramElement, SchemaSymbols.ATT_REF) != null) {
          str3 = "element_r";
        } else {
          str3 = "element_n";
        }
      }
      else if (str2.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
        if (DOMUtil.getAttr(paramElement, SchemaSymbols.ATT_REF) != null) {
          str3 = "attribute_r";
        } else {
          str3 = "attribute_n";
        }
      }
    }
    Container localContainer = (Container)localHashtable.get(str3);
    if (localContainer == null)
    {
      reportSchemaError("s4s-elt-invalid", new Object[] { str2 }, paramElement);
      return null;
    }
    Object[] arrayOfObject = getAvailableArray();
    long l = 0L;
    System.arraycopy(fSeenTemp, 0, fSeen, 0, ATTIDX_COUNT);
    int i = arrayOfAttr.length;
    Attr localAttr = null;
    String str5;
    for (int j = 0; j < i; j++)
    {
      localAttr = arrayOfAttr[j];
      localObject1 = localAttr.getName();
      String str4 = DOMUtil.getNamespaceURI(localAttr);
      str5 = DOMUtil.getValue(localAttr);
      Object localObject2;
      if (((String)localObject1).startsWith("xml"))
      {
        localObject2 = DOMUtil.getPrefix(localAttr);
        if (("xmlns".equals(localObject2)) || ("xmlns".equals(localObject1))) {
          continue;
        }
        if ((SchemaSymbols.ATT_XML_LANG.equals(localObject1)) && ((SchemaSymbols.ELT_SCHEMA.equals(str2)) || (SchemaSymbols.ELT_DOCUMENTATION.equals(str2)))) {
          str4 = null;
        }
      }
      if ((str4 != null) && (str4.length() != 0))
      {
        if (str4.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA))
        {
          reportSchemaError("s4s-att-not-allowed", new Object[] { str2, localObject1 }, paramElement);
        }
        else
        {
          if (arrayOfObject[ATTIDX_NONSCHEMA] == null) {
            arrayOfObject[ATTIDX_NONSCHEMA] = new Vector(4, 2);
          }
          ((Vector)arrayOfObject[ATTIDX_NONSCHEMA]).addElement(localObject1);
          ((Vector)arrayOfObject[ATTIDX_NONSCHEMA]).addElement(str5);
        }
      }
      else
      {
        localObject2 = localContainer.get((String)localObject1);
        if (localObject2 == null)
        {
          reportSchemaError("s4s-att-not-allowed", new Object[] { str2, localObject1 }, paramElement);
        }
        else
        {
          fSeen[valueIndex] = true;
          try
          {
            if (dvIndex >= 0)
            {
              if ((dvIndex != 3) && (dvIndex != 6) && (dvIndex != 7))
              {
                XSSimpleType localXSSimpleType = fExtraDVs[dvIndex];
                Object localObject3 = localXSSimpleType.validate(str5, fValidationContext, null);
                if (dvIndex == 2)
                {
                  QName localQName = (QName)localObject3;
                  if ((prefix == XMLSymbols.EMPTY_STRING) && (uri == null) && (fIsChameleonSchema)) {
                    uri = fTargetNamespace;
                  }
                }
                arrayOfObject[valueIndex] = localObject3;
              }
              else
              {
                arrayOfObject[valueIndex] = str5;
              }
            }
            else {
              arrayOfObject[valueIndex] = validate(arrayOfObject, (String)localObject1, str5, dvIndex, paramXSDocumentInfo);
            }
          }
          catch (InvalidDatatypeValueException localInvalidDatatypeValueException)
          {
            reportSchemaError("s4s-att-invalid-value", new Object[] { str2, localObject1, localInvalidDatatypeValueException.getMessage() }, paramElement);
            if (dfltValue != null) {
              arrayOfObject[valueIndex] = dfltValue;
            }
          }
          if ((str2.equals(SchemaSymbols.ELT_ENUMERATION)) && (paramBoolean2)) {
            arrayOfObject[ATTIDX_ENUMNSDECLS] = new SchemaNamespaceSupport(fNamespaceSupport);
          }
        }
      }
    }
    Object localObject1 = values;
    for (int k = 0; k < localObject1.length; k++)
    {
      str5 = localObject1[k];
      if ((dfltValue != null) && (fSeen[valueIndex] == 0))
      {
        arrayOfObject[valueIndex] = dfltValue;
        l |= 1 << valueIndex;
      }
    }
    arrayOfObject[ATTIDX_FROMDEFAULT] = new Long(l);
    if (arrayOfObject[ATTIDX_MAXOCCURS] != null)
    {
      int m = ((XInt)arrayOfObject[ATTIDX_MINOCCURS]).intValue();
      int n = ((XInt)arrayOfObject[ATTIDX_MAXOCCURS]).intValue();
      if ((n != -1) && (m > n))
      {
        reportSchemaError("p-props-correct.2.1", new Object[] { str2, arrayOfObject[ATTIDX_MINOCCURS], arrayOfObject[ATTIDX_MAXOCCURS] }, paramElement);
        arrayOfObject[ATTIDX_MINOCCURS] = arrayOfObject[ATTIDX_MAXOCCURS];
      }
    }
    return arrayOfObject;
  }
  
  private Object validate(Object[] paramArrayOfObject, String paramString1, String paramString2, int paramInt, XSDocumentInfo paramXSDocumentInfo)
    throws InvalidDatatypeValueException
  {
    if (paramString2 == null) {
      return null;
    }
    String str1 = XMLChar.trim(paramString2);
    Object localObject1 = null;
    int i;
    StringTokenizer localStringTokenizer1;
    String str2;
    Object localObject3;
    switch (paramInt)
    {
    case -15: 
      if ((str1.equals("false")) || (str1.equals("0"))) {
        localObject1 = Boolean.FALSE;
      } else if ((str1.equals("true")) || (str1.equals("1"))) {
        localObject1 = Boolean.TRUE;
      } else {
        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { str1, "boolean" });
      }
      break;
    case -16: 
      try
      {
        if ((str1.length() > 0) && (str1.charAt(0) == '+')) {
          str1 = str1.substring(1);
        }
        localObject1 = fXIntPool.getXInt(Integer.parseInt(str1));
      }
      catch (NumberFormatException localNumberFormatException1)
      {
        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { str1, "nonNegativeInteger" });
      }
      if (((XInt)localObject1).intValue() < 0) {
        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { str1, "nonNegativeInteger" });
      }
      break;
    case -17: 
      try
      {
        if ((str1.length() > 0) && (str1.charAt(0) == '+')) {
          str1 = str1.substring(1);
        }
        localObject1 = fXIntPool.getXInt(Integer.parseInt(str1));
      }
      catch (NumberFormatException localNumberFormatException2)
      {
        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { str1, "positiveInteger" });
      }
      if (((XInt)localObject1).intValue() <= 0) {
        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { str1, "positiveInteger" });
      }
      break;
    case -1: 
      i = 0;
      if (str1.equals("#all"))
      {
        i = 31;
      }
      else
      {
        localStringTokenizer1 = new StringTokenizer(str1, " \n\t\r");
        while (localStringTokenizer1.hasMoreTokens())
        {
          str2 = localStringTokenizer1.nextToken();
          if (str2.equals("extension")) {
            i |= 0x1;
          } else if (str2.equals("restriction")) {
            i |= 0x2;
          } else if (str2.equals("substitution")) {
            i |= 0x4;
          } else {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { str1, "(#all | List of (extension | restriction | substitution))" });
          }
        }
      }
      localObject1 = fXIntPool.getXInt(i);
      break;
    case -3: 
    case -2: 
      i = 0;
      if (str1.equals("#all"))
      {
        i = 31;
      }
      else
      {
        localStringTokenizer1 = new StringTokenizer(str1, " \n\t\r");
        while (localStringTokenizer1.hasMoreTokens())
        {
          str2 = localStringTokenizer1.nextToken();
          if (str2.equals("extension")) {
            i |= 0x1;
          } else if (str2.equals("restriction")) {
            i |= 0x2;
          } else {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { str1, "(#all | List of (extension | restriction))" });
          }
        }
      }
      localObject1 = fXIntPool.getXInt(i);
      break;
    case -4: 
      i = 0;
      if (str1.equals("#all"))
      {
        i = 31;
      }
      else
      {
        localStringTokenizer1 = new StringTokenizer(str1, " \n\t\r");
        while (localStringTokenizer1.hasMoreTokens())
        {
          str2 = localStringTokenizer1.nextToken();
          if (str2.equals("list")) {
            i |= 0x10;
          } else if (str2.equals("union")) {
            i |= 0x8;
          } else if (str2.equals("restriction")) {
            i |= 0x2;
          } else {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { str1, "(#all | List of (list | union | restriction))" });
          }
        }
      }
      localObject1 = fXIntPool.getXInt(i);
      break;
    case -5: 
      i = 0;
      if (str1.equals("#all"))
      {
        i = 31;
      }
      else
      {
        localStringTokenizer1 = new StringTokenizer(str1, " \n\t\r");
        while (localStringTokenizer1.hasMoreTokens())
        {
          str2 = localStringTokenizer1.nextToken();
          if (str2.equals("extension")) {
            i |= 0x1;
          } else if (str2.equals("restriction")) {
            i |= 0x2;
          } else if (str2.equals("list")) {
            i |= 0x10;
          } else if (str2.equals("union")) {
            i |= 0x8;
          } else {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { str1, "(#all | List of (extension | restriction | list | union))" });
          }
        }
      }
      localObject1 = fXIntPool.getXInt(i);
      break;
    case -6: 
      if (str1.equals("qualified")) {
        localObject1 = INT_QUALIFIED;
      } else if (str1.equals("unqualified")) {
        localObject1 = INT_UNQUALIFIED;
      } else {
        throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { str1, "(qualified | unqualified)" });
      }
      break;
    case -7: 
      if (str1.equals("unbounded")) {
        localObject1 = INT_UNBOUNDED;
      } else {
        try
        {
          localObject1 = validate(paramArrayOfObject, paramString1, str1, -16, paramXSDocumentInfo);
        }
        catch (NumberFormatException localNumberFormatException3)
        {
          throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { str1, "(nonNegativeInteger | unbounded)" });
        }
      }
      break;
    case -8: 
      if (str1.equals("1")) {
        localObject1 = fXIntPool.getXInt(1);
      } else {
        throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { str1, "(1)" });
      }
      break;
    case -9: 
      Vector localVector = new Vector();
      try
      {
        StringTokenizer localStringTokenizer2 = new StringTokenizer(str1, " \n\t\r");
        while (localStringTokenizer2.hasMoreTokens())
        {
          str2 = localStringTokenizer2.nextToken();
          localObject3 = (QName)fExtraDVs[2].validate(str2, fValidationContext, null);
          if ((prefix == XMLSymbols.EMPTY_STRING) && (uri == null) && (fIsChameleonSchema)) {
            uri = fTargetNamespace;
          }
          localVector.addElement(localObject3);
        }
        localObject1 = localVector;
      }
      catch (InvalidDatatypeValueException localInvalidDatatypeValueException1)
      {
        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.2", new Object[] { str1, "(List of QName)" });
      }
    case -10: 
      if (str1.equals("0")) {
        localObject1 = fXIntPool.getXInt(0);
      } else if (str1.equals("1")) {
        localObject1 = fXIntPool.getXInt(1);
      } else {
        throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { str1, "(0 | 1)" });
      }
      break;
    case -11: 
      if (str1.equals("##any"))
      {
        localObject1 = INT_ANY_ANY;
      }
      else
      {
        Object localObject2;
        if (str1.equals("##other"))
        {
          localObject1 = INT_ANY_NOT;
          localObject2 = new String[2];
          localObject2[0] = fTargetNamespace;
          localObject2[1] = null;
          paramArrayOfObject[ATTIDX_NAMESPACE_LIST] = localObject2;
        }
        else
        {
          localObject1 = INT_ANY_LIST;
          fNamespaceList.removeAllElements();
          localObject2 = new StringTokenizer(str1, " \n\t\r");
          try
          {
            while (((StringTokenizer)localObject2).hasMoreTokens())
            {
              str2 = ((StringTokenizer)localObject2).nextToken();
              if (str2.equals("##local"))
              {
                localObject3 = null;
              }
              else if (str2.equals("##targetNamespace"))
              {
                localObject3 = fTargetNamespace;
              }
              else
              {
                fExtraDVs[0].validate(str2, fValidationContext, null);
                localObject3 = fSymbolTable.addSymbol(str2);
              }
              if (!fNamespaceList.contains(localObject3)) {
                fNamespaceList.addElement(localObject3);
              }
            }
          }
          catch (InvalidDatatypeValueException localInvalidDatatypeValueException2)
          {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { str1, "((##any | ##other) | List of (anyURI | (##targetNamespace | ##local)) )" });
          }
          int j = fNamespaceList.size();
          String[] arrayOfString = new String[j];
          fNamespaceList.copyInto(arrayOfString);
          paramArrayOfObject[ATTIDX_NAMESPACE_LIST] = arrayOfString;
        }
      }
      break;
    case -12: 
      if (str1.equals("strict")) {
        localObject1 = INT_ANY_STRICT;
      } else if (str1.equals("lax")) {
        localObject1 = INT_ANY_LAX;
      } else if (str1.equals("skip")) {
        localObject1 = INT_ANY_SKIP;
      } else {
        throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { str1, "(lax | skip | strict)" });
      }
      break;
    case -13: 
      if (str1.equals("optional")) {
        localObject1 = INT_USE_OPTIONAL;
      } else if (str1.equals("required")) {
        localObject1 = INT_USE_REQUIRED;
      } else if (str1.equals("prohibited")) {
        localObject1 = INT_USE_PROHIBITED;
      } else {
        throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { str1, "(optional | prohibited | required)" });
      }
      break;
    case -14: 
      if (str1.equals("preserve")) {
        localObject1 = INT_WS_PRESERVE;
      } else if (str1.equals("replace")) {
        localObject1 = INT_WS_REPLACE;
      } else if (str1.equals("collapse")) {
        localObject1 = INT_WS_COLLAPSE;
      } else {
        throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { str1, "(preserve | replace | collapse)" });
      }
      break;
    }
    return localObject1;
  }
  
  void reportSchemaError(String paramString, Object[] paramArrayOfObject, Element paramElement)
  {
    fSchemaHandler.reportSchemaError(paramString, paramArrayOfObject, paramElement);
  }
  
  public void checkNonSchemaAttributes(XSGrammarBucket paramXSGrammarBucket)
  {
    Iterator localIterator = fNonSchemaAttrs.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str1 = (String)localEntry.getKey();
      String str2 = str1.substring(0, str1.indexOf(','));
      String str3 = str1.substring(str1.indexOf(',') + 1);
      SchemaGrammar localSchemaGrammar = paramXSGrammarBucket.getGrammar(str2);
      if (localSchemaGrammar != null)
      {
        XSAttributeDecl localXSAttributeDecl = localSchemaGrammar.getGlobalAttributeDecl(str3);
        if (localXSAttributeDecl != null)
        {
          XSSimpleType localXSSimpleType = (XSSimpleType)localXSAttributeDecl.getTypeDefinition();
          if (localXSSimpleType != null)
          {
            Vector localVector = (Vector)localEntry.getValue();
            String str5 = (String)localVector.elementAt(0);
            int i = localVector.size();
            for (int j = 1; j < i; j += 2)
            {
              String str4 = (String)localVector.elementAt(j);
              try
              {
                localXSSimpleType.validate((String)localVector.elementAt(j + 1), null, null);
              }
              catch (InvalidDatatypeValueException localInvalidDatatypeValueException)
              {
                reportSchemaError("s4s-att-invalid-value", new Object[] { str4, str5, localInvalidDatatypeValueException.getMessage() }, null);
              }
            }
          }
        }
      }
    }
  }
  
  public static String normalize(String paramString, short paramShort)
  {
    int i = paramString == null ? 0 : paramString.length();
    if ((i == 0) || (paramShort == 0)) {
      return paramString;
    }
    StringBuffer localStringBuffer = new StringBuffer();
    int j;
    char c;
    if (paramShort == 1)
    {
      for (j = 0; j < i; j++)
      {
        c = paramString.charAt(j);
        if ((c != '\t') && (c != '\n') && (c != '\r')) {
          localStringBuffer.append(c);
        } else {
          localStringBuffer.append(' ');
        }
      }
    }
    else
    {
      int k = 1;
      for (j = 0; j < i; j++)
      {
        c = paramString.charAt(j);
        if ((c != '\t') && (c != '\n') && (c != '\r') && (c != ' '))
        {
          localStringBuffer.append(c);
          k = 0;
        }
        else
        {
          while (j < i - 1)
          {
            c = paramString.charAt(j + 1);
            if ((c != '\t') && (c != '\n') && (c != '\r') && (c != ' ')) {
              break;
            }
            j++;
          }
          if ((j < i - 1) && (k == 0)) {
            localStringBuffer.append(' ');
          }
        }
      }
    }
    return localStringBuffer.toString();
  }
  
  protected Object[] getAvailableArray()
  {
    if (fArrayPool.length == fPoolPos)
    {
      fArrayPool = new Object[fPoolPos + 10][];
      for (int i = fPoolPos; i < fArrayPool.length; i++) {
        fArrayPool[i] = new Object[ATTIDX_COUNT];
      }
    }
    Object[] arrayOfObject = fArrayPool[fPoolPos];
    fArrayPool[(fPoolPos++)] = null;
    System.arraycopy(fTempArray, 0, arrayOfObject, 0, ATTIDX_COUNT - 1);
    arrayOfObject[ATTIDX_ISRETURNED] = Boolean.FALSE;
    return arrayOfObject;
  }
  
  public void returnAttrArray(Object[] paramArrayOfObject, XSDocumentInfo paramXSDocumentInfo)
  {
    if (paramXSDocumentInfo != null) {
      fNamespaceSupport.popContext();
    }
    if ((fPoolPos == 0) || (paramArrayOfObject == null) || (paramArrayOfObject.length != ATTIDX_COUNT) || (((Boolean)paramArrayOfObject[ATTIDX_ISRETURNED]).booleanValue())) {
      return;
    }
    paramArrayOfObject[ATTIDX_ISRETURNED] = Boolean.TRUE;
    if (paramArrayOfObject[ATTIDX_NONSCHEMA] != null) {
      ((Vector)paramArrayOfObject[ATTIDX_NONSCHEMA]).clear();
    }
    fArrayPool[(--fPoolPos)] = paramArrayOfObject;
  }
  
  public void resolveNamespace(Element paramElement, Attr[] paramArrayOfAttr, SchemaNamespaceSupport paramSchemaNamespaceSupport)
  {
    paramSchemaNamespaceSupport.pushContext();
    int i = paramArrayOfAttr.length;
    Attr localAttr = null;
    for (int j = 0; j < i; j++)
    {
      localAttr = paramArrayOfAttr[j];
      String str1 = DOMUtil.getName(localAttr);
      String str2 = null;
      if (str1.equals(XMLSymbols.PREFIX_XMLNS)) {
        str2 = XMLSymbols.EMPTY_STRING;
      } else if (str1.startsWith("xmlns:")) {
        str2 = fSymbolTable.addSymbol(DOMUtil.getLocalName(localAttr));
      }
      if (str2 != null)
      {
        String str3 = fSymbolTable.addSymbol(DOMUtil.getValue(localAttr));
        paramSchemaNamespaceSupport.declarePrefix(str2, str3.length() != 0 ? str3 : null);
      }
    }
  }
  
  static
  {
    SchemaGrammar.BuiltinSchemaGrammar localBuiltinSchemaGrammar = SchemaGrammar.SG_SchemaNS;
    fExtraDVs[0] = ((XSSimpleType)localBuiltinSchemaGrammar.getGlobalTypeDecl("anyURI"));
    fExtraDVs[1] = ((XSSimpleType)localBuiltinSchemaGrammar.getGlobalTypeDecl("ID"));
    fExtraDVs[2] = ((XSSimpleType)localBuiltinSchemaGrammar.getGlobalTypeDecl("QName"));
    fExtraDVs[3] = ((XSSimpleType)localBuiltinSchemaGrammar.getGlobalTypeDecl("string"));
    fExtraDVs[4] = ((XSSimpleType)localBuiltinSchemaGrammar.getGlobalTypeDecl("token"));
    fExtraDVs[5] = ((XSSimpleType)localBuiltinSchemaGrammar.getGlobalTypeDecl("NCName"));
    fExtraDVs[6] = fExtraDVs[3];
    fExtraDVs[6] = fExtraDVs[3];
    fExtraDVs[8] = ((XSSimpleType)localBuiltinSchemaGrammar.getGlobalTypeDecl("language"));
    int i = 0;
    int j = i++;
    int k = i++;
    int m = i++;
    int n = i++;
    int i1 = i++;
    int i2 = i++;
    int i3 = i++;
    int i4 = i++;
    int i5 = i++;
    int i6 = i++;
    int i7 = i++;
    int i8 = i++;
    int i9 = i++;
    int i10 = i++;
    int i11 = i++;
    int i12 = i++;
    int i13 = i++;
    int i14 = i++;
    int i15 = i++;
    int i16 = i++;
    int i17 = i++;
    int i18 = i++;
    int i19 = i++;
    int i20 = i++;
    int i21 = i++;
    int i22 = i++;
    int i23 = i++;
    int i24 = i++;
    int i25 = i++;
    int i26 = i++;
    int i27 = i++;
    int i28 = i++;
    int i29 = i++;
    int i30 = i++;
    int i31 = i++;
    int i32 = i++;
    int i33 = i++;
    int i34 = i++;
    int i35 = i++;
    int i36 = i++;
    int i37 = i++;
    int i38 = i++;
    int i39 = i++;
    int i40 = i++;
    int i41 = i++;
    int i42 = i++;
    int i43 = i++;
    int i44 = i++;
    OneAttr[] arrayOfOneAttr = new OneAttr[i];
    arrayOfOneAttr[j] = new OneAttr(SchemaSymbols.ATT_ABSTRACT, -15, ATTIDX_ABSTRACT, Boolean.FALSE);
    arrayOfOneAttr[k] = new OneAttr(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT, -6, ATTIDX_AFORMDEFAULT, INT_UNQUALIFIED);
    arrayOfOneAttr[m] = new OneAttr(SchemaSymbols.ATT_BASE, 2, ATTIDX_BASE, null);
    arrayOfOneAttr[n] = new OneAttr(SchemaSymbols.ATT_BASE, 2, ATTIDX_BASE, null);
    arrayOfOneAttr[i1] = new OneAttr(SchemaSymbols.ATT_BLOCK, -1, ATTIDX_BLOCK, null);
    arrayOfOneAttr[i2] = new OneAttr(SchemaSymbols.ATT_BLOCK, -2, ATTIDX_BLOCK, null);
    arrayOfOneAttr[i3] = new OneAttr(SchemaSymbols.ATT_BLOCKDEFAULT, -1, ATTIDX_BLOCKDEFAULT, INT_EMPTY_SET);
    arrayOfOneAttr[i4] = new OneAttr(SchemaSymbols.ATT_DEFAULT, 3, ATTIDX_DEFAULT, null);
    arrayOfOneAttr[i5] = new OneAttr(SchemaSymbols.ATT_ELEMENTFORMDEFAULT, -6, ATTIDX_EFORMDEFAULT, INT_UNQUALIFIED);
    arrayOfOneAttr[i6] = new OneAttr(SchemaSymbols.ATT_FINAL, -3, ATTIDX_FINAL, null);
    arrayOfOneAttr[i7] = new OneAttr(SchemaSymbols.ATT_FINAL, -4, ATTIDX_FINAL, null);
    arrayOfOneAttr[i8] = new OneAttr(SchemaSymbols.ATT_FINALDEFAULT, -5, ATTIDX_FINALDEFAULT, INT_EMPTY_SET);
    arrayOfOneAttr[i9] = new OneAttr(SchemaSymbols.ATT_FIXED, 3, ATTIDX_FIXED, null);
    arrayOfOneAttr[i10] = new OneAttr(SchemaSymbols.ATT_FIXED, -15, ATTIDX_FIXED, Boolean.FALSE);
    arrayOfOneAttr[i11] = new OneAttr(SchemaSymbols.ATT_FORM, -6, ATTIDX_FORM, null);
    arrayOfOneAttr[i12] = new OneAttr(SchemaSymbols.ATT_ID, 1, ATTIDX_ID, null);
    arrayOfOneAttr[i13] = new OneAttr(SchemaSymbols.ATT_ITEMTYPE, 2, ATTIDX_ITEMTYPE, null);
    arrayOfOneAttr[i14] = new OneAttr(SchemaSymbols.ATT_MAXOCCURS, -7, ATTIDX_MAXOCCURS, fXIntPool.getXInt(1));
    arrayOfOneAttr[i15] = new OneAttr(SchemaSymbols.ATT_MAXOCCURS, -8, ATTIDX_MAXOCCURS, fXIntPool.getXInt(1));
    arrayOfOneAttr[i16] = new OneAttr(SchemaSymbols.ATT_MEMBERTYPES, -9, ATTIDX_MEMBERTYPES, null);
    arrayOfOneAttr[i17] = new OneAttr(SchemaSymbols.ATT_MINOCCURS, -16, ATTIDX_MINOCCURS, fXIntPool.getXInt(1));
    arrayOfOneAttr[i18] = new OneAttr(SchemaSymbols.ATT_MINOCCURS, -10, ATTIDX_MINOCCURS, fXIntPool.getXInt(1));
    arrayOfOneAttr[i19] = new OneAttr(SchemaSymbols.ATT_MIXED, -15, ATTIDX_MIXED, Boolean.FALSE);
    arrayOfOneAttr[i20] = new OneAttr(SchemaSymbols.ATT_MIXED, -15, ATTIDX_MIXED, null);
    arrayOfOneAttr[i21] = new OneAttr(SchemaSymbols.ATT_NAME, 5, ATTIDX_NAME, null);
    arrayOfOneAttr[i22] = new OneAttr(SchemaSymbols.ATT_NAMESPACE, -11, ATTIDX_NAMESPACE, INT_ANY_ANY);
    arrayOfOneAttr[i23] = new OneAttr(SchemaSymbols.ATT_NAMESPACE, 0, ATTIDX_NAMESPACE, null);
    arrayOfOneAttr[i24] = new OneAttr(SchemaSymbols.ATT_NILLABLE, -15, ATTIDX_NILLABLE, Boolean.FALSE);
    arrayOfOneAttr[i25] = new OneAttr(SchemaSymbols.ATT_PROCESSCONTENTS, -12, ATTIDX_PROCESSCONTENTS, INT_ANY_STRICT);
    arrayOfOneAttr[i26] = new OneAttr(SchemaSymbols.ATT_PUBLIC, 4, ATTIDX_PUBLIC, null);
    arrayOfOneAttr[i27] = new OneAttr(SchemaSymbols.ATT_REF, 2, ATTIDX_REF, null);
    arrayOfOneAttr[i28] = new OneAttr(SchemaSymbols.ATT_REFER, 2, ATTIDX_REFER, null);
    arrayOfOneAttr[i29] = new OneAttr(SchemaSymbols.ATT_SCHEMALOCATION, 0, ATTIDX_SCHEMALOCATION, null);
    arrayOfOneAttr[i30] = new OneAttr(SchemaSymbols.ATT_SCHEMALOCATION, 0, ATTIDX_SCHEMALOCATION, null);
    arrayOfOneAttr[i31] = new OneAttr(SchemaSymbols.ATT_SOURCE, 0, ATTIDX_SOURCE, null);
    arrayOfOneAttr[i32] = new OneAttr(SchemaSymbols.ATT_SUBSTITUTIONGROUP, 2, ATTIDX_SUBSGROUP, null);
    arrayOfOneAttr[i33] = new OneAttr(SchemaSymbols.ATT_SYSTEM, 0, ATTIDX_SYSTEM, null);
    arrayOfOneAttr[i34] = new OneAttr(SchemaSymbols.ATT_TARGETNAMESPACE, 0, ATTIDX_TARGETNAMESPACE, null);
    arrayOfOneAttr[i35] = new OneAttr(SchemaSymbols.ATT_TYPE, 2, ATTIDX_TYPE, null);
    arrayOfOneAttr[i36] = new OneAttr(SchemaSymbols.ATT_USE, -13, ATTIDX_USE, INT_USE_OPTIONAL);
    arrayOfOneAttr[i37] = new OneAttr(SchemaSymbols.ATT_VALUE, -16, ATTIDX_VALUE, null);
    arrayOfOneAttr[i38] = new OneAttr(SchemaSymbols.ATT_VALUE, -17, ATTIDX_VALUE, null);
    arrayOfOneAttr[i39] = new OneAttr(SchemaSymbols.ATT_VALUE, 3, ATTIDX_VALUE, null);
    arrayOfOneAttr[i40] = new OneAttr(SchemaSymbols.ATT_VALUE, -14, ATTIDX_VALUE, null);
    arrayOfOneAttr[i41] = new OneAttr(SchemaSymbols.ATT_VERSION, 4, ATTIDX_VERSION, null);
    arrayOfOneAttr[i42] = new OneAttr(SchemaSymbols.ATT_XML_LANG, 8, ATTIDX_XML_LANG, null);
    arrayOfOneAttr[i43] = new OneAttr(SchemaSymbols.ATT_XPATH, 6, ATTIDX_XPATH, null);
    arrayOfOneAttr[i44] = new OneAttr(SchemaSymbols.ATT_XPATH, 7, ATTIDX_XPATH, null);
    Container localContainer = Container.getContainer(5);
    localContainer.put(SchemaSymbols.ATT_DEFAULT, arrayOfOneAttr[i4]);
    localContainer.put(SchemaSymbols.ATT_FIXED, arrayOfOneAttr[i9]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    localContainer.put(SchemaSymbols.ATT_TYPE, arrayOfOneAttr[i35]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_ATTRIBUTE, localContainer);
    localContainer = Container.getContainer(7);
    localContainer.put(SchemaSymbols.ATT_DEFAULT, arrayOfOneAttr[i4]);
    localContainer.put(SchemaSymbols.ATT_FIXED, arrayOfOneAttr[i9]);
    localContainer.put(SchemaSymbols.ATT_FORM, arrayOfOneAttr[i11]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    localContainer.put(SchemaSymbols.ATT_TYPE, arrayOfOneAttr[i35]);
    localContainer.put(SchemaSymbols.ATT_USE, arrayOfOneAttr[i36]);
    fEleAttrsMapL.put("attribute_n", localContainer);
    localContainer = Container.getContainer(5);
    localContainer.put(SchemaSymbols.ATT_DEFAULT, arrayOfOneAttr[i4]);
    localContainer.put(SchemaSymbols.ATT_FIXED, arrayOfOneAttr[i9]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_REF, arrayOfOneAttr[i27]);
    localContainer.put(SchemaSymbols.ATT_USE, arrayOfOneAttr[i36]);
    fEleAttrsMapL.put("attribute_r", localContainer);
    localContainer = Container.getContainer(10);
    localContainer.put(SchemaSymbols.ATT_ABSTRACT, arrayOfOneAttr[j]);
    localContainer.put(SchemaSymbols.ATT_BLOCK, arrayOfOneAttr[i1]);
    localContainer.put(SchemaSymbols.ATT_DEFAULT, arrayOfOneAttr[i4]);
    localContainer.put(SchemaSymbols.ATT_FINAL, arrayOfOneAttr[i6]);
    localContainer.put(SchemaSymbols.ATT_FIXED, arrayOfOneAttr[i9]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    localContainer.put(SchemaSymbols.ATT_NILLABLE, arrayOfOneAttr[i24]);
    localContainer.put(SchemaSymbols.ATT_SUBSTITUTIONGROUP, arrayOfOneAttr[i32]);
    localContainer.put(SchemaSymbols.ATT_TYPE, arrayOfOneAttr[i35]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_ELEMENT, localContainer);
    localContainer = Container.getContainer(10);
    localContainer.put(SchemaSymbols.ATT_BLOCK, arrayOfOneAttr[i1]);
    localContainer.put(SchemaSymbols.ATT_DEFAULT, arrayOfOneAttr[i4]);
    localContainer.put(SchemaSymbols.ATT_FIXED, arrayOfOneAttr[i9]);
    localContainer.put(SchemaSymbols.ATT_FORM, arrayOfOneAttr[i11]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_MAXOCCURS, arrayOfOneAttr[i14]);
    localContainer.put(SchemaSymbols.ATT_MINOCCURS, arrayOfOneAttr[i17]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    localContainer.put(SchemaSymbols.ATT_NILLABLE, arrayOfOneAttr[i24]);
    localContainer.put(SchemaSymbols.ATT_TYPE, arrayOfOneAttr[i35]);
    fEleAttrsMapL.put("element_n", localContainer);
    localContainer = Container.getContainer(4);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_MAXOCCURS, arrayOfOneAttr[i14]);
    localContainer.put(SchemaSymbols.ATT_MINOCCURS, arrayOfOneAttr[i17]);
    localContainer.put(SchemaSymbols.ATT_REF, arrayOfOneAttr[i27]);
    fEleAttrsMapL.put("element_r", localContainer);
    localContainer = Container.getContainer(6);
    localContainer.put(SchemaSymbols.ATT_ABSTRACT, arrayOfOneAttr[j]);
    localContainer.put(SchemaSymbols.ATT_BLOCK, arrayOfOneAttr[i2]);
    localContainer.put(SchemaSymbols.ATT_FINAL, arrayOfOneAttr[i6]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_MIXED, arrayOfOneAttr[i19]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_COMPLEXTYPE, localContainer);
    localContainer = Container.getContainer(4);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    localContainer.put(SchemaSymbols.ATT_PUBLIC, arrayOfOneAttr[i26]);
    localContainer.put(SchemaSymbols.ATT_SYSTEM, arrayOfOneAttr[i33]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_NOTATION, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_MIXED, arrayOfOneAttr[i19]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_COMPLEXTYPE, localContainer);
    localContainer = Container.getContainer(1);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_SIMPLECONTENT, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_BASE, arrayOfOneAttr[n]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_RESTRICTION, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_BASE, arrayOfOneAttr[m]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_EXTENSION, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_REF, arrayOfOneAttr[i27]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_ATTRIBUTEGROUP, localContainer);
    localContainer = Container.getContainer(3);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAMESPACE, arrayOfOneAttr[i22]);
    localContainer.put(SchemaSymbols.ATT_PROCESSCONTENTS, arrayOfOneAttr[i25]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_ANYATTRIBUTE, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_MIXED, arrayOfOneAttr[i20]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_COMPLEXCONTENT, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_ATTRIBUTEGROUP, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_GROUP, localContainer);
    localContainer = Container.getContainer(4);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_MAXOCCURS, arrayOfOneAttr[i14]);
    localContainer.put(SchemaSymbols.ATT_MINOCCURS, arrayOfOneAttr[i17]);
    localContainer.put(SchemaSymbols.ATT_REF, arrayOfOneAttr[i27]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_GROUP, localContainer);
    localContainer = Container.getContainer(3);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_MAXOCCURS, arrayOfOneAttr[i15]);
    localContainer.put(SchemaSymbols.ATT_MINOCCURS, arrayOfOneAttr[i18]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_ALL, localContainer);
    localContainer = Container.getContainer(3);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_MAXOCCURS, arrayOfOneAttr[i14]);
    localContainer.put(SchemaSymbols.ATT_MINOCCURS, arrayOfOneAttr[i17]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_CHOICE, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_SEQUENCE, localContainer);
    localContainer = Container.getContainer(5);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_MAXOCCURS, arrayOfOneAttr[i14]);
    localContainer.put(SchemaSymbols.ATT_MINOCCURS, arrayOfOneAttr[i17]);
    localContainer.put(SchemaSymbols.ATT_NAMESPACE, arrayOfOneAttr[i22]);
    localContainer.put(SchemaSymbols.ATT_PROCESSCONTENTS, arrayOfOneAttr[i25]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_ANY, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_UNIQUE, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_KEY, localContainer);
    localContainer = Container.getContainer(3);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    localContainer.put(SchemaSymbols.ATT_REFER, arrayOfOneAttr[i28]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_KEYREF, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_XPATH, arrayOfOneAttr[i43]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_SELECTOR, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_XPATH, arrayOfOneAttr[i44]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_FIELD, localContainer);
    localContainer = Container.getContainer(1);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_ANNOTATION, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_ANNOTATION, localContainer);
    localContainer = Container.getContainer(1);
    localContainer.put(SchemaSymbols.ATT_SOURCE, arrayOfOneAttr[i31]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_APPINFO, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_APPINFO, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_SOURCE, arrayOfOneAttr[i31]);
    localContainer.put(SchemaSymbols.ATT_XML_LANG, arrayOfOneAttr[i42]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_DOCUMENTATION, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_DOCUMENTATION, localContainer);
    localContainer = Container.getContainer(3);
    localContainer.put(SchemaSymbols.ATT_FINAL, arrayOfOneAttr[i7]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAME, arrayOfOneAttr[i21]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_SIMPLETYPE, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_FINAL, arrayOfOneAttr[i7]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_SIMPLETYPE, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_ITEMTYPE, arrayOfOneAttr[i13]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_LIST, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_MEMBERTYPES, arrayOfOneAttr[i16]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_UNION, localContainer);
    localContainer = Container.getContainer(8);
    localContainer.put(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT, arrayOfOneAttr[k]);
    localContainer.put(SchemaSymbols.ATT_BLOCKDEFAULT, arrayOfOneAttr[i3]);
    localContainer.put(SchemaSymbols.ATT_ELEMENTFORMDEFAULT, arrayOfOneAttr[i5]);
    localContainer.put(SchemaSymbols.ATT_FINALDEFAULT, arrayOfOneAttr[i8]);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_TARGETNAMESPACE, arrayOfOneAttr[i34]);
    localContainer.put(SchemaSymbols.ATT_VERSION, arrayOfOneAttr[i41]);
    localContainer.put(SchemaSymbols.ATT_XML_LANG, arrayOfOneAttr[i42]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_SCHEMA, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_SCHEMALOCATION, arrayOfOneAttr[i29]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_INCLUDE, localContainer);
    fEleAttrsMapG.put(SchemaSymbols.ELT_REDEFINE, localContainer);
    localContainer = Container.getContainer(3);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_NAMESPACE, arrayOfOneAttr[i23]);
    localContainer.put(SchemaSymbols.ATT_SCHEMALOCATION, arrayOfOneAttr[i30]);
    fEleAttrsMapG.put(SchemaSymbols.ELT_IMPORT, localContainer);
    localContainer = Container.getContainer(3);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_VALUE, arrayOfOneAttr[i37]);
    localContainer.put(SchemaSymbols.ATT_FIXED, arrayOfOneAttr[i10]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_LENGTH, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_MINLENGTH, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_MAXLENGTH, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_FRACTIONDIGITS, localContainer);
    localContainer = Container.getContainer(3);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_VALUE, arrayOfOneAttr[i38]);
    localContainer.put(SchemaSymbols.ATT_FIXED, arrayOfOneAttr[i10]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_TOTALDIGITS, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_VALUE, arrayOfOneAttr[i39]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_PATTERN, localContainer);
    localContainer = Container.getContainer(2);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_VALUE, arrayOfOneAttr[i39]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_ENUMERATION, localContainer);
    localContainer = Container.getContainer(3);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_VALUE, arrayOfOneAttr[i40]);
    localContainer.put(SchemaSymbols.ATT_FIXED, arrayOfOneAttr[i10]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_WHITESPACE, localContainer);
    localContainer = Container.getContainer(3);
    localContainer.put(SchemaSymbols.ATT_ID, arrayOfOneAttr[i12]);
    localContainer.put(SchemaSymbols.ATT_VALUE, arrayOfOneAttr[i39]);
    localContainer.put(SchemaSymbols.ATT_FIXED, arrayOfOneAttr[i10]);
    fEleAttrsMapL.put(SchemaSymbols.ELT_MAXINCLUSIVE, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_MAXEXCLUSIVE, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_MININCLUSIVE, localContainer);
    fEleAttrsMapL.put(SchemaSymbols.ELT_MINEXCLUSIVE, localContainer);
  }
}
