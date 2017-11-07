package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xpath.XPath;
import org.apache.xerces.impl.xpath.XPath.Axis;
import org.apache.xerces.impl.xpath.XPath.LocationPath;
import org.apache.xerces.impl.xpath.XPath.NodeTest;
import org.apache.xerces.impl.xpath.XPath.Step;
import org.apache.xerces.util.IntStack;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSTypeDefinition;

public class XPathMatcher
{
  protected static final boolean DEBUG_ALL = false;
  protected static final boolean DEBUG_METHODS = false;
  protected static final boolean DEBUG_METHODS2 = false;
  protected static final boolean DEBUG_METHODS3 = false;
  protected static final boolean DEBUG_MATCH = false;
  protected static final boolean DEBUG_STACK = false;
  protected static final boolean DEBUG_ANY = false;
  protected static final int MATCHED = 1;
  protected static final int MATCHED_ATTRIBUTE = 3;
  protected static final int MATCHED_DESCENDANT = 5;
  protected static final int MATCHED_DESCENDANT_PREVIOUS = 13;
  private final XPath.LocationPath[] fLocationPaths;
  private final int[] fMatched;
  protected Object fMatchedString;
  private final IntStack[] fStepIndexes;
  private final int[] fCurrentStep;
  private final int[] fNoMatchDepth;
  final QName fQName = new QName();
  
  public XPathMatcher(XPath paramXPath)
  {
    fLocationPaths = paramXPath.getLocationPaths();
    fStepIndexes = new IntStack[fLocationPaths.length];
    for (int i = 0; i < fStepIndexes.length; i++) {
      fStepIndexes[i] = new IntStack();
    }
    fCurrentStep = new int[fLocationPaths.length];
    fNoMatchDepth = new int[fLocationPaths.length];
    fMatched = new int[fLocationPaths.length];
  }
  
  public boolean isMatched()
  {
    for (int i = 0; i < fLocationPaths.length; i++) {
      if (((fMatched[i] & 0x1) == 1) && ((fMatched[i] & 0xD) != 13) && ((fNoMatchDepth[i] == 0) || ((fMatched[i] & 0x5) == 5))) {
        return true;
      }
    }
    return false;
  }
  
  protected void handleContent(XSTypeDefinition paramXSTypeDefinition, boolean paramBoolean, Object paramObject, short paramShort, ShortList paramShortList) {}
  
  protected void matched(Object paramObject, short paramShort, ShortList paramShortList, boolean paramBoolean) {}
  
  public void startDocumentFragment()
  {
    fMatchedString = null;
    for (int i = 0; i < fLocationPaths.length; i++)
    {
      fStepIndexes[i].clear();
      fCurrentStep[i] = 0;
      fNoMatchDepth[i] = 0;
      fMatched[i] = 0;
    }
  }
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes)
  {
    for (int i = 0; i < fLocationPaths.length; i++)
    {
      int j = fCurrentStep[i];
      fStepIndexes[i].push(j);
      if (((fMatched[i] & 0x5) == 1) || (fNoMatchDepth[i] > 0))
      {
        fNoMatchDepth[i] += 1;
      }
      else
      {
        if ((fMatched[i] & 0x5) == 5) {
          fMatched[i] = 13;
        }
        XPath.Step[] arrayOfStep = fLocationPaths[i].steps;
        while ((fCurrentStep[i] < arrayOfStep.length) && (fCurrentStep[i]].axis.type == 3)) {
          fCurrentStep[i] += 1;
        }
        if (fCurrentStep[i] == arrayOfStep.length)
        {
          fMatched[i] = 1;
        }
        else
        {
          int k = fCurrentStep[i];
          while ((fCurrentStep[i] < arrayOfStep.length) && (fCurrentStep[i]].axis.type == 4)) {
            fCurrentStep[i] += 1;
          }
          int m = fCurrentStep[i] > k ? 1 : 0;
          if (fCurrentStep[i] == arrayOfStep.length)
          {
            fNoMatchDepth[i] += 1;
          }
          else
          {
            XPath.NodeTest localNodeTest;
            if (((fCurrentStep[i] == j) || (fCurrentStep[i] > k)) && (fCurrentStep[i]].axis.type == 1))
            {
              XPath.Step localStep = arrayOfStep[fCurrentStep[i]];
              localNodeTest = nodeTest;
              if (!matches(localNodeTest, paramQName))
              {
                if (fCurrentStep[i] > k) {
                  fCurrentStep[i] = k;
                } else {
                  fNoMatchDepth[i] += 1;
                }
              }
              else {
                fCurrentStep[i] += 1;
              }
            }
            else if (fCurrentStep[i] == arrayOfStep.length)
            {
              if (m != 0)
              {
                fCurrentStep[i] = k;
                fMatched[i] = 5;
              }
              else
              {
                fMatched[i] = 1;
              }
            }
            else if ((fCurrentStep[i] < arrayOfStep.length) && (fCurrentStep[i]].axis.type == 2))
            {
              int n = paramXMLAttributes.getLength();
              if (n > 0)
              {
                localNodeTest = fCurrentStep[i]].nodeTest;
                for (int i1 = 0; i1 < n; i1++)
                {
                  paramXMLAttributes.getName(i1, fQName);
                  if (matches(localNodeTest, fQName))
                  {
                    fCurrentStep[i] += 1;
                    if (fCurrentStep[i] != arrayOfStep.length) {
                      break;
                    }
                    fMatched[i] = 3;
                    for (int i2 = 0; (i2 < i) && ((fMatched[i2] & 0x1) != 1); i2++) {}
                    if (i2 != i) {
                      break;
                    }
                    AttributePSVI localAttributePSVI = (AttributePSVI)paramXMLAttributes.getAugmentations(i1).getItem("ATTRIBUTE_PSVI");
                    fMatchedString = localAttributePSVI.getActualNormalizedValue();
                    matched(fMatchedString, localAttributePSVI.getActualNormalizedValueType(), localAttributePSVI.getItemValueTypes(), false);
                    break;
                  }
                }
              }
              if ((fMatched[i] & 0x1) != 1) {
                if (fCurrentStep[i] > k) {
                  fCurrentStep[i] = k;
                } else {
                  fNoMatchDepth[i] += 1;
                }
              }
            }
          }
        }
      }
    }
  }
  
  public void endElement(QName paramQName, XSTypeDefinition paramXSTypeDefinition, boolean paramBoolean, Object paramObject, short paramShort, ShortList paramShortList)
  {
    for (int i = 0; i < fLocationPaths.length; i++)
    {
      fCurrentStep[i] = fStepIndexes[i].pop();
      if (fNoMatchDepth[i] > 0)
      {
        fNoMatchDepth[i] -= 1;
      }
      else
      {
        for (int j = 0; (j < i) && ((fMatched[j] & 0x1) != 1); j++) {}
        if ((j >= i) && (fMatched[j] != 0)) {
          if ((fMatched[j] & 0x3) == 3)
          {
            fMatched[i] = 0;
          }
          else
          {
            handleContent(paramXSTypeDefinition, paramBoolean, paramObject, paramShort, paramShortList);
            fMatched[i] = 0;
          }
        }
      }
    }
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = super.toString();
    int i = str.lastIndexOf('.');
    if (i != -1) {
      str = str.substring(i + 1);
    }
    localStringBuffer.append(str);
    for (int j = 0; j < fLocationPaths.length; j++)
    {
      localStringBuffer.append('[');
      XPath.Step[] arrayOfStep = fLocationPaths[j].steps;
      for (int k = 0; k < arrayOfStep.length; k++)
      {
        if (k == fCurrentStep[j]) {
          localStringBuffer.append('^');
        }
        localStringBuffer.append(arrayOfStep[k].toString());
        if (k < arrayOfStep.length - 1) {
          localStringBuffer.append('/');
        }
      }
      if (fCurrentStep[j] == arrayOfStep.length) {
        localStringBuffer.append('^');
      }
      localStringBuffer.append(']');
      localStringBuffer.append(',');
    }
    return localStringBuffer.toString();
  }
  
  private String normalize(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      switch (c)
      {
      case '\n': 
        localStringBuffer.append("\\n");
        break;
      default: 
        localStringBuffer.append(c);
      }
    }
    return localStringBuffer.toString();
  }
  
  private static boolean matches(XPath.NodeTest paramNodeTest, QName paramQName)
  {
    if (type == 1) {
      return name.equals(paramQName);
    }
    if (type == 4) {
      return name.uri == uri;
    }
    return true;
  }
}
