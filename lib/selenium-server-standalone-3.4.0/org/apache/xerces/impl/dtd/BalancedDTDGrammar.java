package org.apache.xerces.impl.dtd;

import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XNIException;

final class BalancedDTDGrammar
  extends DTDGrammar
{
  private boolean fMixed;
  private int fDepth = 0;
  private short[] fOpStack = null;
  private int[][] fGroupIndexStack;
  private int[] fGroupIndexStackSizes;
  
  public BalancedDTDGrammar(SymbolTable paramSymbolTable, XMLDTDDescription paramXMLDTDDescription)
  {
    super(paramSymbolTable, paramXMLDTDDescription);
  }
  
  public final void startContentModel(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    fDepth = 0;
    initializeContentModelStacks();
    super.startContentModel(paramString, paramAugmentations);
  }
  
  public final void startGroup(Augmentations paramAugmentations)
    throws XNIException
  {
    fDepth += 1;
    initializeContentModelStacks();
    fMixed = false;
  }
  
  public final void pcdata(Augmentations paramAugmentations)
    throws XNIException
  {
    fMixed = true;
  }
  
  public final void element(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    addToCurrentGroup(addUniqueLeafNode(paramString));
  }
  
  public final void separator(short paramShort, Augmentations paramAugmentations)
    throws XNIException
  {
    if (paramShort == 0) {
      fOpStack[fDepth] = 4;
    } else if (paramShort == 1) {
      fOpStack[fDepth] = 5;
    }
  }
  
  public final void occurrence(short paramShort, Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fMixed)
    {
      int i = fGroupIndexStackSizes[fDepth] - 1;
      if (paramShort == 2) {
        fGroupIndexStack[fDepth][i] = addContentSpecNode(1, fGroupIndexStack[fDepth][i], -1);
      } else if (paramShort == 3) {
        fGroupIndexStack[fDepth][i] = addContentSpecNode(2, fGroupIndexStack[fDepth][i], -1);
      } else if (paramShort == 4) {
        fGroupIndexStack[fDepth][i] = addContentSpecNode(3, fGroupIndexStack[fDepth][i], -1);
      }
    }
  }
  
  public final void endGroup(Augmentations paramAugmentations)
    throws XNIException
  {
    int i = fGroupIndexStackSizes[fDepth];
    int j = i > 0 ? addContentSpecNodes(0, i - 1) : addUniqueLeafNode(null);
    fDepth -= 1;
    addToCurrentGroup(j);
  }
  
  public final void endDTD(Augmentations paramAugmentations)
    throws XNIException
  {
    super.endDTD(paramAugmentations);
    fOpStack = null;
    fGroupIndexStack = null;
    fGroupIndexStackSizes = null;
  }
  
  protected final void addContentSpecToElement(XMLElementDecl paramXMLElementDecl)
  {
    int i = fGroupIndexStackSizes[0] > 0 ? fGroupIndexStack[0][0] : -1;
    setContentSpecIndex(fCurrentElementIndex, i);
  }
  
  private int addContentSpecNodes(int paramInt1, int paramInt2)
  {
    if (paramInt1 == paramInt2) {
      return fGroupIndexStack[fDepth][paramInt1];
    }
    int i = paramInt1 + paramInt2 >>> 1;
    return addContentSpecNode(fOpStack[fDepth], addContentSpecNodes(paramInt1, i), addContentSpecNodes(i + 1, paramInt2));
  }
  
  private void initializeContentModelStacks()
  {
    if (fOpStack == null)
    {
      fOpStack = new short[8];
      fGroupIndexStack = new int[8][];
      fGroupIndexStackSizes = new int[8];
    }
    else if (fDepth == fOpStack.length)
    {
      short[] arrayOfShort = new short[fDepth * 2];
      System.arraycopy(fOpStack, 0, arrayOfShort, 0, fDepth);
      fOpStack = arrayOfShort;
      int[][] arrayOfInt = new int[fDepth * 2][];
      System.arraycopy(fGroupIndexStack, 0, arrayOfInt, 0, fDepth);
      fGroupIndexStack = arrayOfInt;
      int[] arrayOfInt1 = new int[fDepth * 2];
      System.arraycopy(fGroupIndexStackSizes, 0, arrayOfInt1, 0, fDepth);
      fGroupIndexStackSizes = arrayOfInt1;
    }
    fOpStack[fDepth] = -1;
    fGroupIndexStackSizes[fDepth] = 0;
  }
  
  private void addToCurrentGroup(int paramInt)
  {
    Object localObject = fGroupIndexStack[fDepth];
    int tmp18_15 = fDepth;
    int[] tmp18_11 = fGroupIndexStackSizes;
    int tmp20_19 = tmp18_11[tmp18_15];
    tmp18_11[tmp18_15] = (tmp20_19 + 1);
    int i = tmp20_19;
    if (localObject == null)
    {
      localObject = new int[8];
      fGroupIndexStack[fDepth] = localObject;
    }
    else if (i == localObject.length)
    {
      int[] arrayOfInt = new int[localObject.length * 2];
      System.arraycopy(localObject, 0, arrayOfInt, 0, localObject.length);
      localObject = arrayOfInt;
      fGroupIndexStack[fDepth] = localObject;
    }
    localObject[i] = paramInt;
  }
}
