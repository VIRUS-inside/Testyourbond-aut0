package org.apache.xerces.impl.xpath;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;

public class XPath
{
  private static final boolean DEBUG_ALL = false;
  private static final boolean DEBUG_XPATH_PARSE = false;
  private static final boolean DEBUG_ANY = false;
  protected final String fExpression;
  protected final SymbolTable fSymbolTable;
  protected final LocationPath[] fLocationPaths;
  
  public XPath(String paramString, SymbolTable paramSymbolTable, NamespaceContext paramNamespaceContext)
    throws XPathException
  {
    fExpression = paramString;
    fSymbolTable = paramSymbolTable;
    fLocationPaths = parseExpression(paramNamespaceContext);
  }
  
  public LocationPath[] getLocationPaths()
  {
    LocationPath[] arrayOfLocationPath = new LocationPath[fLocationPaths.length];
    for (int i = 0; i < fLocationPaths.length; i++) {
      arrayOfLocationPath[i] = ((LocationPath)fLocationPaths[i].clone());
    }
    return arrayOfLocationPath;
  }
  
  public LocationPath getLocationPath()
  {
    return (LocationPath)fLocationPaths[0].clone();
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < fLocationPaths.length; i++)
    {
      if (i > 0) {
        localStringBuffer.append('|');
      }
      localStringBuffer.append(fLocationPaths[i].toString());
    }
    return localStringBuffer.toString();
  }
  
  private static void check(boolean paramBoolean)
    throws XPathException
  {
    if (!paramBoolean) {
      throw new XPathException("c-general-xpath");
    }
  }
  
  private LocationPath buildLocationPath(Vector paramVector)
    throws XPathException
  {
    int i = paramVector.size();
    check(i != 0);
    Step[] arrayOfStep = new Step[i];
    paramVector.copyInto(arrayOfStep);
    paramVector.removeAllElements();
    return new LocationPath(arrayOfStep);
  }
  
  private LocationPath[] parseExpression(NamespaceContext paramNamespaceContext)
    throws XPathException
  {
    Tokens localTokens = new Tokens(fSymbolTable);
    Scanner local1 = new Scanner(fSymbolTable)
    {
      protected void addToken(XPath.Tokens paramAnonymousTokens, int paramAnonymousInt)
        throws XPathException
      {
        if ((paramAnonymousInt == 6) || (paramAnonymousInt == 11) || (paramAnonymousInt == 21) || (paramAnonymousInt == 4) || (paramAnonymousInt == 9) || (paramAnonymousInt == 10) || (paramAnonymousInt == 22) || (paramAnonymousInt == 23) || (paramAnonymousInt == 36) || (paramAnonymousInt == 35) || (paramAnonymousInt == 8))
        {
          super.addToken(paramAnonymousTokens, paramAnonymousInt);
          return;
        }
        throw new XPathException("c-general-xpath");
      }
    };
    int i = fExpression.length();
    boolean bool1 = local1.scanExpr(fSymbolTable, localTokens, fExpression, 0, i);
    if (!bool1) {
      throw new XPathException("c-general-xpath");
    }
    Vector localVector = new Vector();
    ArrayList localArrayList = new ArrayList();
    boolean bool2 = true;
    while (localTokens.hasMore())
    {
      int j = localTokens.nextToken();
      Object localObject;
      switch (j)
      {
      case 23: 
        check(!bool2);
        localArrayList.add(buildLocationPath(localVector));
        bool2 = true;
        break;
      case 6: 
        check(bool2);
        localObject = new Step(new Axis((short)2), parseNodeTest(localTokens.nextToken(), localTokens, paramNamespaceContext));
        localVector.addElement(localObject);
        bool2 = false;
        break;
      case 35: 
        check(bool2);
        if (localTokens.nextToken() != 8) {
          throw new XPathException("c-general-xpath");
        }
        localObject = new Step(new Axis((short)2), parseNodeTest(localTokens.nextToken(), localTokens, paramNamespaceContext));
        localVector.addElement(localObject);
        bool2 = false;
        break;
      case 9: 
      case 10: 
      case 11: 
        check(bool2);
        localObject = new Step(new Axis((short)1), parseNodeTest(j, localTokens, paramNamespaceContext));
        localVector.addElement(localObject);
        bool2 = false;
        break;
      case 36: 
        check(bool2);
        if (localTokens.nextToken() != 8) {
          throw new XPathException("c-general-xpath");
        }
        localObject = new Step(new Axis((short)1), parseNodeTest(localTokens.nextToken(), localTokens, paramNamespaceContext));
        localVector.addElement(localObject);
        bool2 = false;
        break;
      case 4: 
        check(bool2);
        bool2 = false;
        if (localVector.size() == 0)
        {
          localObject = new Axis((short)3);
          NodeTest localNodeTest = new NodeTest((short)3);
          Step localStep = new Step((Axis)localObject, localNodeTest);
          localVector.addElement(localStep);
          if ((localTokens.hasMore()) && (localTokens.peekToken() == 22))
          {
            localTokens.nextToken();
            localObject = new Axis((short)4);
            localNodeTest = new NodeTest((short)3);
            localStep = new Step((Axis)localObject, localNodeTest);
            localVector.addElement(localStep);
            bool2 = true;
          }
        }
        break;
      case 22: 
        throw new XPathException("c-general-xpath");
      case 8: 
        throw new XPathException("c-general-xpath");
      case 21: 
        check(!bool2);
        bool2 = true;
        break;
      case 5: 
      case 7: 
      case 12: 
      case 13: 
      case 14: 
      case 15: 
      case 16: 
      case 17: 
      case 18: 
      case 19: 
      case 20: 
      case 24: 
      case 25: 
      case 26: 
      case 27: 
      case 28: 
      case 29: 
      case 30: 
      case 31: 
      case 32: 
      case 33: 
      case 34: 
      default: 
        throw new InternalError();
      }
    }
    check(!bool2);
    localArrayList.add(buildLocationPath(localVector));
    return (LocationPath[])localArrayList.toArray(new LocationPath[localArrayList.size()]);
  }
  
  private NodeTest parseNodeTest(int paramInt, Tokens paramTokens, NamespaceContext paramNamespaceContext)
    throws XPathException
  {
    switch (paramInt)
    {
    case 9: 
      return new NodeTest((short)2);
    case 10: 
    case 11: 
      String str1 = paramTokens.nextTokenAsString();
      String str2 = null;
      if ((paramNamespaceContext != null) && (str1 != XMLSymbols.EMPTY_STRING)) {
        str2 = paramNamespaceContext.getURI(str1);
      }
      if ((str1 != XMLSymbols.EMPTY_STRING) && (paramNamespaceContext != null) && (str2 == null)) {
        throw new XPathException("c-general-xpath-ns");
      }
      if (paramInt == 10) {
        return new NodeTest(str1, str2);
      }
      String str3 = paramTokens.nextTokenAsString();
      String str4 = str1 != XMLSymbols.EMPTY_STRING ? fSymbolTable.addSymbol(str1 + ':' + str3) : str3;
      return new NodeTest(new QName(str1, str3, str4, str2));
    }
    throw new XPathException("c-general-xpath");
  }
  
  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      String str = paramArrayOfString[i];
      System.out.println("# XPath expression: \"" + str + '"');
      try
      {
        SymbolTable localSymbolTable = new SymbolTable();
        XPath localXPath = new XPath(str, localSymbolTable, null);
        System.out.println("expanded xpath: \"" + localXPath.toString() + '"');
      }
      catch (XPathException localXPathException)
      {
        System.out.println("error: " + localXPathException.getMessage());
      }
    }
  }
  
  private static class Scanner
  {
    private static final byte CHARTYPE_INVALID = 0;
    private static final byte CHARTYPE_OTHER = 1;
    private static final byte CHARTYPE_WHITESPACE = 2;
    private static final byte CHARTYPE_EXCLAMATION = 3;
    private static final byte CHARTYPE_QUOTE = 4;
    private static final byte CHARTYPE_DOLLAR = 5;
    private static final byte CHARTYPE_OPEN_PAREN = 6;
    private static final byte CHARTYPE_CLOSE_PAREN = 7;
    private static final byte CHARTYPE_STAR = 8;
    private static final byte CHARTYPE_PLUS = 9;
    private static final byte CHARTYPE_COMMA = 10;
    private static final byte CHARTYPE_MINUS = 11;
    private static final byte CHARTYPE_PERIOD = 12;
    private static final byte CHARTYPE_SLASH = 13;
    private static final byte CHARTYPE_DIGIT = 14;
    private static final byte CHARTYPE_COLON = 15;
    private static final byte CHARTYPE_LESS = 16;
    private static final byte CHARTYPE_EQUAL = 17;
    private static final byte CHARTYPE_GREATER = 18;
    private static final byte CHARTYPE_ATSIGN = 19;
    private static final byte CHARTYPE_LETTER = 20;
    private static final byte CHARTYPE_OPEN_BRACKET = 21;
    private static final byte CHARTYPE_CLOSE_BRACKET = 22;
    private static final byte CHARTYPE_UNDERSCORE = 23;
    private static final byte CHARTYPE_UNION = 24;
    private static final byte CHARTYPE_NONASCII = 25;
    private static final byte[] fASCIICharMap = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 1, 5, 1, 1, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 1, 16, 17, 18, 1, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 1, 22, 1, 23, 1, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 1, 24, 1, 1, 1 };
    private SymbolTable fSymbolTable;
    private static final String fAndSymbol = "and".intern();
    private static final String fOrSymbol = "or".intern();
    private static final String fModSymbol = "mod".intern();
    private static final String fDivSymbol = "div".intern();
    private static final String fCommentSymbol = "comment".intern();
    private static final String fTextSymbol = "text".intern();
    private static final String fPISymbol = "processing-instruction".intern();
    private static final String fNodeSymbol = "node".intern();
    private static final String fAncestorSymbol = "ancestor".intern();
    private static final String fAncestorOrSelfSymbol = "ancestor-or-self".intern();
    private static final String fAttributeSymbol = "attribute".intern();
    private static final String fChildSymbol = "child".intern();
    private static final String fDescendantSymbol = "descendant".intern();
    private static final String fDescendantOrSelfSymbol = "descendant-or-self".intern();
    private static final String fFollowingSymbol = "following".intern();
    private static final String fFollowingSiblingSymbol = "following-sibling".intern();
    private static final String fNamespaceSymbol = "namespace".intern();
    private static final String fParentSymbol = "parent".intern();
    private static final String fPrecedingSymbol = "preceding".intern();
    private static final String fPrecedingSiblingSymbol = "preceding-sibling".intern();
    private static final String fSelfSymbol = "self".intern();
    
    public Scanner(SymbolTable paramSymbolTable)
    {
      fSymbolTable = paramSymbolTable;
    }
    
    public boolean scanExpr(SymbolTable paramSymbolTable, XPath.Tokens paramTokens, String paramString, int paramInt1, int paramInt2)
      throws XPathException
    {
      int j = 0;
      while (paramInt1 != paramInt2)
      {
        for (int k = paramString.charAt(paramInt1); (k == 32) || (k == 10) || (k == 9) || (k == 13); k = paramString.charAt(paramInt1))
        {
          paramInt1++;
          if (paramInt1 == paramInt2) {
            break;
          }
        }
        if (paramInt1 != paramInt2)
        {
          int m = k >= 128 ? 25 : fASCIICharMap[k];
          int i;
          String str1;
          String str2;
          switch (m)
          {
          case 6: 
            addToken(paramTokens, 0);
            j = 0;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 7: 
            addToken(paramTokens, 1);
            j = 1;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 21: 
            addToken(paramTokens, 2);
            j = 0;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 22: 
            addToken(paramTokens, 3);
            j = 1;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 12: 
            if (paramInt1 + 1 == paramInt2)
            {
              addToken(paramTokens, 4);
              j = 1;
              paramInt1++;
            }
            else
            {
              k = paramString.charAt(paramInt1 + 1);
              if (k == 46)
              {
                addToken(paramTokens, 5);
                j = 1;
                paramInt1 += 2;
              }
              else if ((k >= 48) && (k <= 57))
              {
                addToken(paramTokens, 47);
                j = 1;
                paramInt1 = scanNumber(paramTokens, paramString, paramInt2, paramInt1);
              }
              else if (k == 47)
              {
                addToken(paramTokens, 4);
                j = 1;
                paramInt1++;
              }
              else
              {
                if (k == 124)
                {
                  addToken(paramTokens, 4);
                  j = 1;
                  paramInt1++;
                  continue;
                }
                if ((k == 32) || (k == 10) || (k == 9) || (k == 13))
                {
                  do
                  {
                    paramInt1++;
                    if (paramInt1 == paramInt2) {
                      break;
                    }
                    k = paramString.charAt(paramInt1);
                  } while ((k == 32) || (k == 10) || (k == 9) || (k == 13));
                  if ((paramInt1 == paramInt2) || (k == 124))
                  {
                    addToken(paramTokens, 4);
                    j = 1;
                    continue;
                  }
                  throw new XPathException("c-general-xpath");
                }
                throw new XPathException("c-general-xpath");
              }
              if (paramInt1 != paramInt2) {}
            }
            break;
          case 19: 
            addToken(paramTokens, 6);
            j = 0;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 10: 
            addToken(paramTokens, 7);
            j = 0;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 15: 
            paramInt1++;
            if (paramInt1 == paramInt2) {
              return false;
            }
            k = paramString.charAt(paramInt1);
            if (k != 58) {
              return false;
            }
            addToken(paramTokens, 8);
            j = 0;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 13: 
            paramInt1++;
            if (paramInt1 == paramInt2)
            {
              addToken(paramTokens, 21);
              j = 0;
            }
            else
            {
              k = paramString.charAt(paramInt1);
              if (k == 47)
              {
                addToken(paramTokens, 22);
                j = 0;
                paramInt1++;
                if (paramInt1 != paramInt2) {}
              }
              else
              {
                addToken(paramTokens, 21);
                j = 0;
              }
            }
            break;
          case 24: 
            addToken(paramTokens, 23);
            j = 0;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 9: 
            addToken(paramTokens, 24);
            j = 0;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 11: 
            addToken(paramTokens, 25);
            j = 0;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 17: 
            addToken(paramTokens, 26);
            j = 0;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 3: 
            paramInt1++;
            if (paramInt1 == paramInt2) {
              return false;
            }
            k = paramString.charAt(paramInt1);
            if (k != 61) {
              return false;
            }
            addToken(paramTokens, 27);
            j = 0;
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 16: 
            paramInt1++;
            if (paramInt1 == paramInt2)
            {
              addToken(paramTokens, 28);
              j = 0;
            }
            else
            {
              k = paramString.charAt(paramInt1);
              if (k == 61)
              {
                addToken(paramTokens, 29);
                j = 0;
                paramInt1++;
                if (paramInt1 != paramInt2) {}
              }
              else
              {
                addToken(paramTokens, 28);
                j = 0;
              }
            }
            break;
          case 18: 
            paramInt1++;
            if (paramInt1 == paramInt2)
            {
              addToken(paramTokens, 30);
              j = 0;
            }
            else
            {
              k = paramString.charAt(paramInt1);
              if (k == 61)
              {
                addToken(paramTokens, 31);
                j = 0;
                paramInt1++;
                if (paramInt1 != paramInt2) {}
              }
              else
              {
                addToken(paramTokens, 30);
                j = 0;
              }
            }
            break;
          case 4: 
            int n = k;
            paramInt1++;
            if (paramInt1 == paramInt2) {
              return false;
            }
            k = paramString.charAt(paramInt1);
            int i1 = paramInt1;
            while (k != n)
            {
              paramInt1++;
              if (paramInt1 == paramInt2) {
                return false;
              }
              k = paramString.charAt(paramInt1);
            }
            int i2 = paramInt1 - i1;
            addToken(paramTokens, 46);
            j = 1;
            paramTokens.addToken(paramSymbolTable.addSymbol(paramString.substring(i1, i1 + i2)));
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 14: 
            addToken(paramTokens, 47);
            j = 1;
            paramInt1 = scanNumber(paramTokens, paramString, paramInt2, paramInt1);
            break;
          case 5: 
            paramInt1++;
            if (paramInt1 == paramInt2) {
              return false;
            }
            i = paramInt1;
            paramInt1 = scanNCName(paramString, paramInt2, paramInt1);
            if (paramInt1 == i) {
              return false;
            }
            if (paramInt1 < paramInt2) {
              k = paramString.charAt(paramInt1);
            } else {
              k = -1;
            }
            str1 = paramSymbolTable.addSymbol(paramString.substring(i, paramInt1));
            if (k != 58)
            {
              str2 = XMLSymbols.EMPTY_STRING;
            }
            else
            {
              str2 = str1;
              paramInt1++;
              if (paramInt1 == paramInt2) {
                return false;
              }
              i = paramInt1;
              paramInt1 = scanNCName(paramString, paramInt2, paramInt1);
              if (paramInt1 == i) {
                return false;
              }
              if (paramInt1 < paramInt2) {
                k = paramString.charAt(paramInt1);
              } else {
                k = -1;
              }
              str1 = paramSymbolTable.addSymbol(paramString.substring(i, paramInt1));
            }
            addToken(paramTokens, 48);
            j = 1;
            paramTokens.addToken(str2);
            paramTokens.addToken(str1);
            break;
          case 8: 
            if (j != 0)
            {
              addToken(paramTokens, 20);
              j = 0;
            }
            else
            {
              addToken(paramTokens, 9);
              j = 1;
            }
            paramInt1++;
            if (paramInt1 != paramInt2) {}
            break;
          case 20: 
          case 23: 
          case 25: 
            i = paramInt1;
            paramInt1 = scanNCName(paramString, paramInt2, paramInt1);
            if (paramInt1 == i) {
              return false;
            }
            if (paramInt1 < paramInt2) {
              k = paramString.charAt(paramInt1);
            } else {
              k = -1;
            }
            str1 = paramSymbolTable.addSymbol(paramString.substring(i, paramInt1));
            int i3 = 0;
            int i4 = 0;
            str2 = XMLSymbols.EMPTY_STRING;
            if (k == 58)
            {
              paramInt1++;
              if (paramInt1 == paramInt2) {
                return false;
              }
              k = paramString.charAt(paramInt1);
              if (k == 42)
              {
                paramInt1++;
                if (paramInt1 < paramInt2) {
                  k = paramString.charAt(paramInt1);
                }
                i3 = 1;
              }
              else if (k == 58)
              {
                paramInt1++;
                if (paramInt1 < paramInt2) {
                  k = paramString.charAt(paramInt1);
                }
                i4 = 1;
              }
              else
              {
                str2 = str1;
                i = paramInt1;
                paramInt1 = scanNCName(paramString, paramInt2, paramInt1);
                if (paramInt1 == i) {
                  return false;
                }
                if (paramInt1 < paramInt2) {
                  k = paramString.charAt(paramInt1);
                } else {
                  k = -1;
                }
                str1 = paramSymbolTable.addSymbol(paramString.substring(i, paramInt1));
              }
            }
            while ((k == 32) || (k == 10) || (k == 9) || (k == 13))
            {
              paramInt1++;
              if (paramInt1 == paramInt2) {
                break;
              }
              k = paramString.charAt(paramInt1);
            }
            if (j != 0)
            {
              if (str1 == fAndSymbol)
              {
                addToken(paramTokens, 16);
                j = 0;
              }
              else if (str1 == fOrSymbol)
              {
                addToken(paramTokens, 17);
                j = 0;
              }
              else if (str1 == fModSymbol)
              {
                addToken(paramTokens, 18);
                j = 0;
              }
              else if (str1 == fDivSymbol)
              {
                addToken(paramTokens, 19);
                j = 0;
              }
              else
              {
                return false;
              }
              if (i3 != 0) {
                return false;
              }
              if (i4 != 0) {
                return false;
              }
            }
            else if ((k == 40) && (i3 == 0) && (i4 == 0))
            {
              if (str1 == fCommentSymbol)
              {
                addToken(paramTokens, 12);
              }
              else if (str1 == fTextSymbol)
              {
                addToken(paramTokens, 13);
              }
              else if (str1 == fPISymbol)
              {
                addToken(paramTokens, 14);
              }
              else if (str1 == fNodeSymbol)
              {
                addToken(paramTokens, 15);
              }
              else
              {
                addToken(paramTokens, 32);
                paramTokens.addToken(str2);
                paramTokens.addToken(str1);
              }
              addToken(paramTokens, 0);
              j = 0;
              paramInt1++;
              if (paramInt1 != paramInt2) {}
            }
            else if ((i4 != 0) || ((k == 58) && (paramInt1 + 1 < paramInt2) && (paramString.charAt(paramInt1 + 1) == ':')))
            {
              if (str1 == fAncestorSymbol) {
                addToken(paramTokens, 33);
              } else if (str1 == fAncestorOrSelfSymbol) {
                addToken(paramTokens, 34);
              } else if (str1 == fAttributeSymbol) {
                addToken(paramTokens, 35);
              } else if (str1 == fChildSymbol) {
                addToken(paramTokens, 36);
              } else if (str1 == fDescendantSymbol) {
                addToken(paramTokens, 37);
              } else if (str1 == fDescendantOrSelfSymbol) {
                addToken(paramTokens, 38);
              } else if (str1 == fFollowingSymbol) {
                addToken(paramTokens, 39);
              } else if (str1 == fFollowingSiblingSymbol) {
                addToken(paramTokens, 40);
              } else if (str1 == fNamespaceSymbol) {
                addToken(paramTokens, 41);
              } else if (str1 == fParentSymbol) {
                addToken(paramTokens, 42);
              } else if (str1 == fPrecedingSymbol) {
                addToken(paramTokens, 43);
              } else if (str1 == fPrecedingSiblingSymbol) {
                addToken(paramTokens, 44);
              } else if (str1 == fSelfSymbol) {
                addToken(paramTokens, 45);
              } else {
                return false;
              }
              if (i3 != 0) {
                return false;
              }
              addToken(paramTokens, 8);
              j = 0;
              if (i4 == 0)
              {
                paramInt1++;
                paramInt1++;
                if (paramInt1 != paramInt2) {}
              }
            }
            else if (i3 != 0)
            {
              addToken(paramTokens, 10);
              j = 1;
              paramTokens.addToken(str1);
            }
            else
            {
              addToken(paramTokens, 11);
              j = 1;
              paramTokens.addToken(str2);
              paramTokens.addToken(str1);
            }
            break;
          default: 
            return false;
          }
        }
      }
      return true;
    }
    
    int scanNCName(String paramString, int paramInt1, int paramInt2)
    {
      int i = paramString.charAt(paramInt2);
      int j;
      if (i >= 128)
      {
        if (!XMLChar.isNameStart(i)) {
          return paramInt2;
        }
      }
      else
      {
        j = fASCIICharMap[i];
        if ((j != 20) && (j != 23)) {
          return paramInt2;
        }
      }
      do
      {
        i = paramString.charAt(paramInt2);
        if (i >= 128)
        {
          if (!XMLChar.isName(i)) {
            break;
          }
        }
        else
        {
          j = fASCIICharMap[i];
          if ((j != 20) && (j != 14) && (j != 12) && (j != 11) && (j != 23)) {
            break;
          }
        }
        paramInt2++;
      } while (paramInt2 < paramInt1);
      return paramInt2;
    }
    
    private int scanNumber(XPath.Tokens paramTokens, String paramString, int paramInt1, int paramInt2)
    {
      int i = paramString.charAt(paramInt2);
      int j = 0;
      int k = 0;
      while ((i >= 48) && (i <= 57))
      {
        j = j * 10 + (i - 48);
        paramInt2++;
        if (paramInt2 == paramInt1) {
          break;
        }
        i = paramString.charAt(paramInt2);
      }
      if (i == 46)
      {
        paramInt2++;
        if (paramInt2 < paramInt1)
        {
          for (i = paramString.charAt(paramInt2); (i >= 48) && (i <= 57); i = paramString.charAt(paramInt2))
          {
            k = k * 10 + (i - 48);
            paramInt2++;
            if (paramInt2 == paramInt1) {
              break;
            }
          }
          if (k != 0) {
            throw new RuntimeException("find a solution!");
          }
        }
      }
      paramTokens.addToken(j);
      paramTokens.addToken(k);
      return paramInt2;
    }
    
    protected void addToken(XPath.Tokens paramTokens, int paramInt)
      throws XPathException
    {
      paramTokens.addToken(paramInt);
    }
  }
  
  private static final class Tokens
  {
    static final boolean DUMP_TOKENS = false;
    public static final int EXPRTOKEN_OPEN_PAREN = 0;
    public static final int EXPRTOKEN_CLOSE_PAREN = 1;
    public static final int EXPRTOKEN_OPEN_BRACKET = 2;
    public static final int EXPRTOKEN_CLOSE_BRACKET = 3;
    public static final int EXPRTOKEN_PERIOD = 4;
    public static final int EXPRTOKEN_DOUBLE_PERIOD = 5;
    public static final int EXPRTOKEN_ATSIGN = 6;
    public static final int EXPRTOKEN_COMMA = 7;
    public static final int EXPRTOKEN_DOUBLE_COLON = 8;
    public static final int EXPRTOKEN_NAMETEST_ANY = 9;
    public static final int EXPRTOKEN_NAMETEST_NAMESPACE = 10;
    public static final int EXPRTOKEN_NAMETEST_QNAME = 11;
    public static final int EXPRTOKEN_NODETYPE_COMMENT = 12;
    public static final int EXPRTOKEN_NODETYPE_TEXT = 13;
    public static final int EXPRTOKEN_NODETYPE_PI = 14;
    public static final int EXPRTOKEN_NODETYPE_NODE = 15;
    public static final int EXPRTOKEN_OPERATOR_AND = 16;
    public static final int EXPRTOKEN_OPERATOR_OR = 17;
    public static final int EXPRTOKEN_OPERATOR_MOD = 18;
    public static final int EXPRTOKEN_OPERATOR_DIV = 19;
    public static final int EXPRTOKEN_OPERATOR_MULT = 20;
    public static final int EXPRTOKEN_OPERATOR_SLASH = 21;
    public static final int EXPRTOKEN_OPERATOR_DOUBLE_SLASH = 22;
    public static final int EXPRTOKEN_OPERATOR_UNION = 23;
    public static final int EXPRTOKEN_OPERATOR_PLUS = 24;
    public static final int EXPRTOKEN_OPERATOR_MINUS = 25;
    public static final int EXPRTOKEN_OPERATOR_EQUAL = 26;
    public static final int EXPRTOKEN_OPERATOR_NOT_EQUAL = 27;
    public static final int EXPRTOKEN_OPERATOR_LESS = 28;
    public static final int EXPRTOKEN_OPERATOR_LESS_EQUAL = 29;
    public static final int EXPRTOKEN_OPERATOR_GREATER = 30;
    public static final int EXPRTOKEN_OPERATOR_GREATER_EQUAL = 31;
    public static final int EXPRTOKEN_FUNCTION_NAME = 32;
    public static final int EXPRTOKEN_AXISNAME_ANCESTOR = 33;
    public static final int EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF = 34;
    public static final int EXPRTOKEN_AXISNAME_ATTRIBUTE = 35;
    public static final int EXPRTOKEN_AXISNAME_CHILD = 36;
    public static final int EXPRTOKEN_AXISNAME_DESCENDANT = 37;
    public static final int EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF = 38;
    public static final int EXPRTOKEN_AXISNAME_FOLLOWING = 39;
    public static final int EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING = 40;
    public static final int EXPRTOKEN_AXISNAME_NAMESPACE = 41;
    public static final int EXPRTOKEN_AXISNAME_PARENT = 42;
    public static final int EXPRTOKEN_AXISNAME_PRECEDING = 43;
    public static final int EXPRTOKEN_AXISNAME_PRECEDING_SIBLING = 44;
    public static final int EXPRTOKEN_AXISNAME_SELF = 45;
    public static final int EXPRTOKEN_LITERAL = 46;
    public static final int EXPRTOKEN_NUMBER = 47;
    public static final int EXPRTOKEN_VARIABLE_REFERENCE = 48;
    private static final String[] fgTokenNames = { "EXPRTOKEN_OPEN_PAREN", "EXPRTOKEN_CLOSE_PAREN", "EXPRTOKEN_OPEN_BRACKET", "EXPRTOKEN_CLOSE_BRACKET", "EXPRTOKEN_PERIOD", "EXPRTOKEN_DOUBLE_PERIOD", "EXPRTOKEN_ATSIGN", "EXPRTOKEN_COMMA", "EXPRTOKEN_DOUBLE_COLON", "EXPRTOKEN_NAMETEST_ANY", "EXPRTOKEN_NAMETEST_NAMESPACE", "EXPRTOKEN_NAMETEST_QNAME", "EXPRTOKEN_NODETYPE_COMMENT", "EXPRTOKEN_NODETYPE_TEXT", "EXPRTOKEN_NODETYPE_PI", "EXPRTOKEN_NODETYPE_NODE", "EXPRTOKEN_OPERATOR_AND", "EXPRTOKEN_OPERATOR_OR", "EXPRTOKEN_OPERATOR_MOD", "EXPRTOKEN_OPERATOR_DIV", "EXPRTOKEN_OPERATOR_MULT", "EXPRTOKEN_OPERATOR_SLASH", "EXPRTOKEN_OPERATOR_DOUBLE_SLASH", "EXPRTOKEN_OPERATOR_UNION", "EXPRTOKEN_OPERATOR_PLUS", "EXPRTOKEN_OPERATOR_MINUS", "EXPRTOKEN_OPERATOR_EQUAL", "EXPRTOKEN_OPERATOR_NOT_EQUAL", "EXPRTOKEN_OPERATOR_LESS", "EXPRTOKEN_OPERATOR_LESS_EQUAL", "EXPRTOKEN_OPERATOR_GREATER", "EXPRTOKEN_OPERATOR_GREATER_EQUAL", "EXPRTOKEN_FUNCTION_NAME", "EXPRTOKEN_AXISNAME_ANCESTOR", "EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF", "EXPRTOKEN_AXISNAME_ATTRIBUTE", "EXPRTOKEN_AXISNAME_CHILD", "EXPRTOKEN_AXISNAME_DESCENDANT", "EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF", "EXPRTOKEN_AXISNAME_FOLLOWING", "EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING", "EXPRTOKEN_AXISNAME_NAMESPACE", "EXPRTOKEN_AXISNAME_PARENT", "EXPRTOKEN_AXISNAME_PRECEDING", "EXPRTOKEN_AXISNAME_PRECEDING_SIBLING", "EXPRTOKEN_AXISNAME_SELF", "EXPRTOKEN_LITERAL", "EXPRTOKEN_NUMBER", "EXPRTOKEN_VARIABLE_REFERENCE" };
    private static final int INITIAL_TOKEN_COUNT = 256;
    private int[] fTokens = new int['Ā'];
    private int fTokenCount = 0;
    private SymbolTable fSymbolTable;
    private Hashtable fSymbolMapping = new Hashtable();
    private Hashtable fTokenNames = new Hashtable();
    private int fCurrentTokenIndex;
    
    public Tokens(SymbolTable paramSymbolTable)
    {
      fSymbolTable = paramSymbolTable;
      String[] arrayOfString = { "ancestor", "ancestor-or-self", "attribute", "child", "descendant", "descendant-or-self", "following", "following-sibling", "namespace", "parent", "preceding", "preceding-sibling", "self" };
      for (int i = 0; i < arrayOfString.length; i++) {
        fSymbolMapping.put(fSymbolTable.addSymbol(arrayOfString[i]), new Integer(i));
      }
      fTokenNames.put(new Integer(0), "EXPRTOKEN_OPEN_PAREN");
      fTokenNames.put(new Integer(1), "EXPRTOKEN_CLOSE_PAREN");
      fTokenNames.put(new Integer(2), "EXPRTOKEN_OPEN_BRACKET");
      fTokenNames.put(new Integer(3), "EXPRTOKEN_CLOSE_BRACKET");
      fTokenNames.put(new Integer(4), "EXPRTOKEN_PERIOD");
      fTokenNames.put(new Integer(5), "EXPRTOKEN_DOUBLE_PERIOD");
      fTokenNames.put(new Integer(6), "EXPRTOKEN_ATSIGN");
      fTokenNames.put(new Integer(7), "EXPRTOKEN_COMMA");
      fTokenNames.put(new Integer(8), "EXPRTOKEN_DOUBLE_COLON");
      fTokenNames.put(new Integer(9), "EXPRTOKEN_NAMETEST_ANY");
      fTokenNames.put(new Integer(10), "EXPRTOKEN_NAMETEST_NAMESPACE");
      fTokenNames.put(new Integer(11), "EXPRTOKEN_NAMETEST_QNAME");
      fTokenNames.put(new Integer(12), "EXPRTOKEN_NODETYPE_COMMENT");
      fTokenNames.put(new Integer(13), "EXPRTOKEN_NODETYPE_TEXT");
      fTokenNames.put(new Integer(14), "EXPRTOKEN_NODETYPE_PI");
      fTokenNames.put(new Integer(15), "EXPRTOKEN_NODETYPE_NODE");
      fTokenNames.put(new Integer(16), "EXPRTOKEN_OPERATOR_AND");
      fTokenNames.put(new Integer(17), "EXPRTOKEN_OPERATOR_OR");
      fTokenNames.put(new Integer(18), "EXPRTOKEN_OPERATOR_MOD");
      fTokenNames.put(new Integer(19), "EXPRTOKEN_OPERATOR_DIV");
      fTokenNames.put(new Integer(20), "EXPRTOKEN_OPERATOR_MULT");
      fTokenNames.put(new Integer(21), "EXPRTOKEN_OPERATOR_SLASH");
      fTokenNames.put(new Integer(22), "EXPRTOKEN_OPERATOR_DOUBLE_SLASH");
      fTokenNames.put(new Integer(23), "EXPRTOKEN_OPERATOR_UNION");
      fTokenNames.put(new Integer(24), "EXPRTOKEN_OPERATOR_PLUS");
      fTokenNames.put(new Integer(25), "EXPRTOKEN_OPERATOR_MINUS");
      fTokenNames.put(new Integer(26), "EXPRTOKEN_OPERATOR_EQUAL");
      fTokenNames.put(new Integer(27), "EXPRTOKEN_OPERATOR_NOT_EQUAL");
      fTokenNames.put(new Integer(28), "EXPRTOKEN_OPERATOR_LESS");
      fTokenNames.put(new Integer(29), "EXPRTOKEN_OPERATOR_LESS_EQUAL");
      fTokenNames.put(new Integer(30), "EXPRTOKEN_OPERATOR_GREATER");
      fTokenNames.put(new Integer(31), "EXPRTOKEN_OPERATOR_GREATER_EQUAL");
      fTokenNames.put(new Integer(32), "EXPRTOKEN_FUNCTION_NAME");
      fTokenNames.put(new Integer(33), "EXPRTOKEN_AXISNAME_ANCESTOR");
      fTokenNames.put(new Integer(34), "EXPRTOKEN_AXISNAME_ANCESTOR_OR_SELF");
      fTokenNames.put(new Integer(35), "EXPRTOKEN_AXISNAME_ATTRIBUTE");
      fTokenNames.put(new Integer(36), "EXPRTOKEN_AXISNAME_CHILD");
      fTokenNames.put(new Integer(37), "EXPRTOKEN_AXISNAME_DESCENDANT");
      fTokenNames.put(new Integer(38), "EXPRTOKEN_AXISNAME_DESCENDANT_OR_SELF");
      fTokenNames.put(new Integer(39), "EXPRTOKEN_AXISNAME_FOLLOWING");
      fTokenNames.put(new Integer(40), "EXPRTOKEN_AXISNAME_FOLLOWING_SIBLING");
      fTokenNames.put(new Integer(41), "EXPRTOKEN_AXISNAME_NAMESPACE");
      fTokenNames.put(new Integer(42), "EXPRTOKEN_AXISNAME_PARENT");
      fTokenNames.put(new Integer(43), "EXPRTOKEN_AXISNAME_PRECEDING");
      fTokenNames.put(new Integer(44), "EXPRTOKEN_AXISNAME_PRECEDING_SIBLING");
      fTokenNames.put(new Integer(45), "EXPRTOKEN_AXISNAME_SELF");
      fTokenNames.put(new Integer(46), "EXPRTOKEN_LITERAL");
      fTokenNames.put(new Integer(47), "EXPRTOKEN_NUMBER");
      fTokenNames.put(new Integer(48), "EXPRTOKEN_VARIABLE_REFERENCE");
    }
    
    public String getTokenString(int paramInt)
    {
      return (String)fTokenNames.get(new Integer(paramInt));
    }
    
    public void addToken(String paramString)
    {
      Integer localInteger = (Integer)fTokenNames.get(paramString);
      if (localInteger == null)
      {
        localInteger = new Integer(fTokenNames.size());
        fTokenNames.put(localInteger, paramString);
      }
      addToken(localInteger.intValue());
    }
    
    public void addToken(int paramInt)
    {
      try
      {
        fTokens[fTokenCount] = paramInt;
      }
      catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
      {
        int[] arrayOfInt = fTokens;
        fTokens = new int[fTokenCount << 1];
        System.arraycopy(arrayOfInt, 0, fTokens, 0, fTokenCount);
        fTokens[fTokenCount] = paramInt;
      }
      fTokenCount += 1;
    }
    
    public void rewind()
    {
      fCurrentTokenIndex = 0;
    }
    
    public boolean hasMore()
    {
      return fCurrentTokenIndex < fTokenCount;
    }
    
    public int nextToken()
      throws XPathException
    {
      if (fCurrentTokenIndex == fTokenCount) {
        throw new XPathException("c-general-xpath");
      }
      return fTokens[(fCurrentTokenIndex++)];
    }
    
    public int peekToken()
      throws XPathException
    {
      if (fCurrentTokenIndex == fTokenCount) {
        throw new XPathException("c-general-xpath");
      }
      return fTokens[fCurrentTokenIndex];
    }
    
    public String nextTokenAsString()
      throws XPathException
    {
      String str = getTokenString(nextToken());
      if (str == null) {
        throw new XPathException("c-general-xpath");
      }
      return str;
    }
    
    public void dumpTokens()
    {
      for (int i = 0; i < fTokenCount; i++) {
        switch (fTokens[i])
        {
        case 0: 
          System.out.print("<OPEN_PAREN/>");
          break;
        case 1: 
          System.out.print("<CLOSE_PAREN/>");
          break;
        case 2: 
          System.out.print("<OPEN_BRACKET/>");
          break;
        case 3: 
          System.out.print("<CLOSE_BRACKET/>");
          break;
        case 4: 
          System.out.print("<PERIOD/>");
          break;
        case 5: 
          System.out.print("<DOUBLE_PERIOD/>");
          break;
        case 6: 
          System.out.print("<ATSIGN/>");
          break;
        case 7: 
          System.out.print("<COMMA/>");
          break;
        case 8: 
          System.out.print("<DOUBLE_COLON/>");
          break;
        case 9: 
          System.out.print("<NAMETEST_ANY/>");
          break;
        case 10: 
          System.out.print("<NAMETEST_NAMESPACE");
          System.out.print(" prefix=\"" + getTokenString(fTokens[(++i)]) + "\"");
          System.out.print("/>");
          break;
        case 11: 
          System.out.print("<NAMETEST_QNAME");
          if (fTokens[(++i)] != -1) {
            System.out.print(" prefix=\"" + getTokenString(fTokens[i]) + "\"");
          }
          System.out.print(" localpart=\"" + getTokenString(fTokens[(++i)]) + "\"");
          System.out.print("/>");
          break;
        case 12: 
          System.out.print("<NODETYPE_COMMENT/>");
          break;
        case 13: 
          System.out.print("<NODETYPE_TEXT/>");
          break;
        case 14: 
          System.out.print("<NODETYPE_PI/>");
          break;
        case 15: 
          System.out.print("<NODETYPE_NODE/>");
          break;
        case 16: 
          System.out.print("<OPERATOR_AND/>");
          break;
        case 17: 
          System.out.print("<OPERATOR_OR/>");
          break;
        case 18: 
          System.out.print("<OPERATOR_MOD/>");
          break;
        case 19: 
          System.out.print("<OPERATOR_DIV/>");
          break;
        case 20: 
          System.out.print("<OPERATOR_MULT/>");
          break;
        case 21: 
          System.out.print("<OPERATOR_SLASH/>");
          if (i + 1 < fTokenCount)
          {
            System.out.println();
            System.out.print("  ");
          }
          break;
        case 22: 
          System.out.print("<OPERATOR_DOUBLE_SLASH/>");
          break;
        case 23: 
          System.out.print("<OPERATOR_UNION/>");
          break;
        case 24: 
          System.out.print("<OPERATOR_PLUS/>");
          break;
        case 25: 
          System.out.print("<OPERATOR_MINUS/>");
          break;
        case 26: 
          System.out.print("<OPERATOR_EQUAL/>");
          break;
        case 27: 
          System.out.print("<OPERATOR_NOT_EQUAL/>");
          break;
        case 28: 
          System.out.print("<OPERATOR_LESS/>");
          break;
        case 29: 
          System.out.print("<OPERATOR_LESS_EQUAL/>");
          break;
        case 30: 
          System.out.print("<OPERATOR_GREATER/>");
          break;
        case 31: 
          System.out.print("<OPERATOR_GREATER_EQUAL/>");
          break;
        case 32: 
          System.out.print("<FUNCTION_NAME");
          if (fTokens[(++i)] != -1) {
            System.out.print(" prefix=\"" + getTokenString(fTokens[i]) + "\"");
          }
          System.out.print(" localpart=\"" + getTokenString(fTokens[(++i)]) + "\"");
          System.out.print("/>");
          break;
        case 33: 
          System.out.print("<AXISNAME_ANCESTOR/>");
          break;
        case 34: 
          System.out.print("<AXISNAME_ANCESTOR_OR_SELF/>");
          break;
        case 35: 
          System.out.print("<AXISNAME_ATTRIBUTE/>");
          break;
        case 36: 
          System.out.print("<AXISNAME_CHILD/>");
          break;
        case 37: 
          System.out.print("<AXISNAME_DESCENDANT/>");
          break;
        case 38: 
          System.out.print("<AXISNAME_DESCENDANT_OR_SELF/>");
          break;
        case 39: 
          System.out.print("<AXISNAME_FOLLOWING/>");
          break;
        case 40: 
          System.out.print("<AXISNAME_FOLLOWING_SIBLING/>");
          break;
        case 41: 
          System.out.print("<AXISNAME_NAMESPACE/>");
          break;
        case 42: 
          System.out.print("<AXISNAME_PARENT/>");
          break;
        case 43: 
          System.out.print("<AXISNAME_PRECEDING/>");
          break;
        case 44: 
          System.out.print("<AXISNAME_PRECEDING_SIBLING/>");
          break;
        case 45: 
          System.out.print("<AXISNAME_SELF/>");
          break;
        case 46: 
          System.out.print("<LITERAL");
          System.out.print(" value=\"" + getTokenString(fTokens[(++i)]) + "\"");
          System.out.print("/>");
          break;
        case 47: 
          System.out.print("<NUMBER");
          System.out.print(" whole=\"" + getTokenString(fTokens[(++i)]) + "\"");
          System.out.print(" part=\"" + getTokenString(fTokens[(++i)]) + "\"");
          System.out.print("/>");
          break;
        case 48: 
          System.out.print("<VARIABLE_REFERENCE");
          if (fTokens[(++i)] != -1) {
            System.out.print(" prefix=\"" + getTokenString(fTokens[i]) + "\"");
          }
          System.out.print(" localpart=\"" + getTokenString(fTokens[(++i)]) + "\"");
          System.out.print("/>");
          break;
        default: 
          System.out.println("<???/>");
        }
      }
      System.out.println();
    }
  }
  
  public static class NodeTest
    implements Cloneable
  {
    public static final short QNAME = 1;
    public static final short WILDCARD = 2;
    public static final short NODE = 3;
    public static final short NAMESPACE = 4;
    public final short type;
    public final QName name = new QName();
    
    public NodeTest(short paramShort)
    {
      type = paramShort;
    }
    
    public NodeTest(QName paramQName)
    {
      type = 1;
      name.setValues(paramQName);
    }
    
    public NodeTest(String paramString1, String paramString2)
    {
      type = 4;
      name.setValues(paramString1, null, null, paramString2);
    }
    
    public NodeTest(NodeTest paramNodeTest)
    {
      type = type;
      name.setValues(name);
    }
    
    public String toString()
    {
      switch (type)
      {
      case 1: 
        if (name.prefix.length() != 0)
        {
          if (name.uri != null) {
            return name.prefix + ':' + name.localpart;
          }
          return "{" + name.uri + '}' + name.prefix + ':' + name.localpart;
        }
        return name.localpart;
      case 4: 
        if (name.prefix.length() != 0)
        {
          if (name.uri != null) {
            return name.prefix + ":*";
          }
          return "{" + name.uri + '}' + name.prefix + ":*";
        }
        return "???:*";
      case 2: 
        return "*";
      case 3: 
        return "node()";
      }
      return "???";
    }
    
    public Object clone()
    {
      return new NodeTest(this);
    }
  }
  
  public static class Axis
    implements Cloneable
  {
    public static final short CHILD = 1;
    public static final short ATTRIBUTE = 2;
    public static final short SELF = 3;
    public static final short DESCENDANT = 4;
    public final short type;
    
    public Axis(short paramShort)
    {
      type = paramShort;
    }
    
    protected Axis(Axis paramAxis)
    {
      type = type;
    }
    
    public String toString()
    {
      switch (type)
      {
      case 1: 
        return "child";
      case 2: 
        return "attribute";
      case 3: 
        return "self";
      case 4: 
        return "descendant";
      }
      return "???";
    }
    
    public Object clone()
    {
      return new Axis(this);
    }
  }
  
  public static class Step
    implements Cloneable
  {
    public final XPath.Axis axis;
    public final XPath.NodeTest nodeTest;
    
    public Step(XPath.Axis paramAxis, XPath.NodeTest paramNodeTest)
    {
      axis = paramAxis;
      nodeTest = paramNodeTest;
    }
    
    protected Step(Step paramStep)
    {
      axis = ((XPath.Axis)axis.clone());
      nodeTest = ((XPath.NodeTest)nodeTest.clone());
    }
    
    public String toString()
    {
      if (axis.type == 3) {
        return ".";
      }
      if (axis.type == 2) {
        return "@" + nodeTest.toString();
      }
      if (axis.type == 1) {
        return nodeTest.toString();
      }
      if (axis.type == 4) {
        return "//";
      }
      return "??? (" + axis.type + ')';
    }
    
    public Object clone()
    {
      return new Step(this);
    }
  }
  
  public static class LocationPath
    implements Cloneable
  {
    public final XPath.Step[] steps;
    
    public LocationPath(XPath.Step[] paramArrayOfStep)
    {
      steps = paramArrayOfStep;
    }
    
    protected LocationPath(LocationPath paramLocationPath)
    {
      steps = new XPath.Step[steps.length];
      for (int i = 0; i < steps.length; i++) {
        steps[i] = ((XPath.Step)steps[i].clone());
      }
    }
    
    public String toString()
    {
      StringBuffer localStringBuffer = new StringBuffer();
      for (int i = 0; i < steps.length; i++)
      {
        if ((i > 0) && (steps[(i - 1)].axis.type != 4) && (steps[i].axis.type != 4)) {
          localStringBuffer.append('/');
        }
        localStringBuffer.append(steps[i].toString());
      }
      return localStringBuffer.toString();
    }
    
    public Object clone()
    {
      return new LocationPath(this);
    }
  }
}
