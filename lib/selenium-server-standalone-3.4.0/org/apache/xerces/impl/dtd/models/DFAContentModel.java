package org.apache.xerces.impl.dtd.models;

import java.io.PrintStream;
import java.util.HashMap;
import org.apache.xerces.xni.QName;

public class DFAContentModel
  implements ContentModelValidator
{
  private static String fEpsilonString = fEpsilonString.intern();
  private static String fEOCString = fEOCString.intern();
  private static final boolean DEBUG_VALIDATE_CONTENT = false;
  private QName[] fElemMap = null;
  private int[] fElemMapType = null;
  private int fElemMapSize = 0;
  private boolean fMixed;
  private int fEOCPos = 0;
  private boolean[] fFinalStateFlags = null;
  private CMStateSet[] fFollowList = null;
  private CMNode fHeadNode = null;
  private int fLeafCount = 0;
  private CMLeaf[] fLeafList = null;
  private int[] fLeafListType = null;
  private int[][] fTransTable = null;
  private int fTransTableSize = 0;
  private boolean fEmptyContentIsValid = false;
  private final QName fQName = new QName();
  
  public DFAContentModel(CMNode paramCMNode, int paramInt, boolean paramBoolean)
  {
    fLeafCount = paramInt;
    fMixed = paramBoolean;
    buildDFA(paramCMNode);
  }
  
  public int validate(QName[] paramArrayOfQName, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0) {
      return fEmptyContentIsValid ? -1 : 0;
    }
    int i = 0;
    for (int j = 0; j < paramInt2; j++)
    {
      QName localQName = paramArrayOfQName[(paramInt1 + j)];
      if ((!fMixed) || (localpart != null))
      {
        for (int k = 0; k < fElemMapSize; k++)
        {
          int m = fElemMapType[k] & 0xF;
          if (m == 0)
          {
            if (fElemMap[k].rawname == rawname) {
              break;
            }
          }
          else if (m == 6)
          {
            String str = fElemMap[k].uri;
            if (str == null) {
              break;
            }
            if (str == uri) {
              break;
            }
          }
          else
          {
            if (m == 8 ? uri != null : (m == 7) && (fElemMap[k].uri != uri)) {
              break;
            }
          }
        }
        if (k == fElemMapSize) {
          return j;
        }
        i = fTransTable[i][k];
        if (i == -1) {
          return j;
        }
      }
    }
    if (fFinalStateFlags[i] == 0) {
      return paramInt2;
    }
    return -1;
  }
  
  private void buildDFA(CMNode paramCMNode)
  {
    fQName.setValues(null, fEOCString, fEOCString, null);
    CMLeaf localCMLeaf = new CMLeaf(fQName);
    fHeadNode = new CMBinOp(5, paramCMNode, localCMLeaf);
    fEOCPos = fLeafCount;
    localCMLeaf.setPosition(fLeafCount++);
    fLeafList = new CMLeaf[fLeafCount];
    fLeafListType = new int[fLeafCount];
    postTreeBuildInit(fHeadNode, 0);
    fFollowList = new CMStateSet[fLeafCount];
    for (int i = 0; i < fLeafCount; i++) {
      fFollowList[i] = new CMStateSet(fLeafCount);
    }
    calcFollowList(fHeadNode);
    fElemMap = new QName[fLeafCount];
    fElemMapType = new int[fLeafCount];
    fElemMapSize = 0;
    for (int j = 0; j < fLeafCount; j++)
    {
      fElemMap[j] = new QName();
      localObject1 = fLeafList[j].getElement();
      for (k = 0; k < fElemMapSize; k++) {
        if (fElemMap[k].rawname == rawname) {
          break;
        }
      }
      if (k == fElemMapSize)
      {
        fElemMap[fElemMapSize].setValues((QName)localObject1);
        fElemMapType[fElemMapSize] = fLeafListType[j];
        fElemMapSize += 1;
      }
    }
    Object localObject1 = new int[fLeafCount + fElemMapSize];
    int k = 0;
    for (int m = 0; m < fElemMapSize; m++)
    {
      for (n = 0; n < fLeafCount; n++)
      {
        localObject2 = fLeafList[n].getElement();
        localObject3 = fElemMap[m];
        if (rawname == rawname) {
          localObject1[(k++)] = n;
        }
      }
      localObject1[(k++)] = -1;
    }
    int n = fLeafCount * 4;
    Object localObject2 = new CMStateSet[n];
    fFinalStateFlags = new boolean[n];
    fTransTable = new int[n][];
    Object localObject3 = fHeadNode.firstPos();
    int i1 = 0;
    int i2 = 0;
    fTransTable[i2] = makeDefStateList();
    localObject2[i2] = localObject3;
    i2++;
    HashMap localHashMap = new HashMap();
    while (i1 < i2)
    {
      localObject3 = localObject2[i1];
      int[] arrayOfInt = fTransTable[i1];
      fFinalStateFlags[i1] = ((CMStateSet)localObject3).getBit(fEOCPos);
      i1++;
      CMStateSet localCMStateSet = null;
      int i3 = 0;
      for (int i4 = 0; i4 < fElemMapSize; i4++)
      {
        if (localCMStateSet == null) {
          localCMStateSet = new CMStateSet(fLeafCount);
        } else {
          localCMStateSet.zeroBits();
        }
        for (int i5 = localObject1[(i3++)]; i5 != -1; i5 = localObject1[(i3++)]) {
          if (((CMStateSet)localObject3).getBit(i5)) {
            localCMStateSet.union(fFollowList[i5]);
          }
        }
        if (!localCMStateSet.isEmpty())
        {
          Integer localInteger = (Integer)localHashMap.get(localCMStateSet);
          int i6 = localInteger == null ? i2 : localInteger.intValue();
          if (i6 == i2)
          {
            localObject2[i2] = localCMStateSet;
            fTransTable[i2] = makeDefStateList();
            localHashMap.put(localCMStateSet, new Integer(i2));
            i2++;
            localCMStateSet = null;
          }
          arrayOfInt[i4] = i6;
          if (i2 == n)
          {
            int i7 = (int)(n * 1.5D);
            CMStateSet[] arrayOfCMStateSet = new CMStateSet[i7];
            boolean[] arrayOfBoolean = new boolean[i7];
            int[][] arrayOfInt1 = new int[i7][];
            System.arraycopy(localObject2, 0, arrayOfCMStateSet, 0, n);
            System.arraycopy(fFinalStateFlags, 0, arrayOfBoolean, 0, n);
            System.arraycopy(fTransTable, 0, arrayOfInt1, 0, n);
            n = i7;
            localObject2 = arrayOfCMStateSet;
            fFinalStateFlags = arrayOfBoolean;
            fTransTable = arrayOfInt1;
          }
        }
      }
    }
    fEmptyContentIsValid = ((CMBinOp)fHeadNode).getLeft().isNullable();
    fHeadNode = null;
    fLeafList = null;
    fFollowList = null;
  }
  
  private void calcFollowList(CMNode paramCMNode)
  {
    if (paramCMNode.type() == 4)
    {
      calcFollowList(((CMBinOp)paramCMNode).getLeft());
      calcFollowList(((CMBinOp)paramCMNode).getRight());
    }
    else
    {
      CMStateSet localCMStateSet1;
      CMStateSet localCMStateSet2;
      int i;
      if (paramCMNode.type() == 5)
      {
        calcFollowList(((CMBinOp)paramCMNode).getLeft());
        calcFollowList(((CMBinOp)paramCMNode).getRight());
        localCMStateSet1 = ((CMBinOp)paramCMNode).getLeft().lastPos();
        localCMStateSet2 = ((CMBinOp)paramCMNode).getRight().firstPos();
        for (i = 0; i < fLeafCount; i++) {
          if (localCMStateSet1.getBit(i)) {
            fFollowList[i].union(localCMStateSet2);
          }
        }
      }
      else if ((paramCMNode.type() == 2) || (paramCMNode.type() == 3))
      {
        calcFollowList(((CMUniOp)paramCMNode).getChild());
        localCMStateSet1 = paramCMNode.firstPos();
        localCMStateSet2 = paramCMNode.lastPos();
        for (i = 0; i < fLeafCount; i++) {
          if (localCMStateSet2.getBit(i)) {
            fFollowList[i].union(localCMStateSet1);
          }
        }
      }
      else if (paramCMNode.type() == 1)
      {
        calcFollowList(((CMUniOp)paramCMNode).getChild());
      }
    }
  }
  
  private void dumpTree(CMNode paramCMNode, int paramInt)
  {
    for (int i = 0; i < paramInt; i++) {
      System.out.print("   ");
    }
    int j = paramCMNode.type();
    if ((j == 4) || (j == 5))
    {
      if (j == 4) {
        System.out.print("Choice Node ");
      } else {
        System.out.print("Seq Node ");
      }
      if (paramCMNode.isNullable()) {
        System.out.print("Nullable ");
      }
      System.out.print("firstPos=");
      System.out.print(paramCMNode.firstPos().toString());
      System.out.print(" lastPos=");
      System.out.println(paramCMNode.lastPos().toString());
      dumpTree(((CMBinOp)paramCMNode).getLeft(), paramInt + 1);
      dumpTree(((CMBinOp)paramCMNode).getRight(), paramInt + 1);
    }
    else if (paramCMNode.type() == 2)
    {
      System.out.print("Rep Node ");
      if (paramCMNode.isNullable()) {
        System.out.print("Nullable ");
      }
      System.out.print("firstPos=");
      System.out.print(paramCMNode.firstPos().toString());
      System.out.print(" lastPos=");
      System.out.println(paramCMNode.lastPos().toString());
      dumpTree(((CMUniOp)paramCMNode).getChild(), paramInt + 1);
    }
    else if (paramCMNode.type() == 0)
    {
      System.out.print("Leaf: (pos=" + ((CMLeaf)paramCMNode).getPosition() + "), " + ((CMLeaf)paramCMNode).getElement() + "(elemIndex=" + ((CMLeaf)paramCMNode).getElement() + ") ");
      if (paramCMNode.isNullable()) {
        System.out.print(" Nullable ");
      }
      System.out.print("firstPos=");
      System.out.print(paramCMNode.firstPos().toString());
      System.out.print(" lastPos=");
      System.out.println(paramCMNode.lastPos().toString());
    }
    else
    {
      throw new RuntimeException("ImplementationMessages.VAL_NIICM");
    }
  }
  
  private int[] makeDefStateList()
  {
    int[] arrayOfInt = new int[fElemMapSize];
    for (int i = 0; i < fElemMapSize; i++) {
      arrayOfInt[i] = -1;
    }
    return arrayOfInt;
  }
  
  private int postTreeBuildInit(CMNode paramCMNode, int paramInt)
  {
    paramCMNode.setMaxStates(fLeafCount);
    QName localQName;
    if (((paramCMNode.type() & 0xF) == 6) || ((paramCMNode.type() & 0xF) == 8) || ((paramCMNode.type() & 0xF) == 7))
    {
      localQName = new QName(null, null, null, ((CMAny)paramCMNode).getURI());
      fLeafList[paramInt] = new CMLeaf(localQName, ((CMAny)paramCMNode).getPosition());
      fLeafListType[paramInt] = paramCMNode.type();
      paramInt++;
    }
    else if ((paramCMNode.type() == 4) || (paramCMNode.type() == 5))
    {
      paramInt = postTreeBuildInit(((CMBinOp)paramCMNode).getLeft(), paramInt);
      paramInt = postTreeBuildInit(((CMBinOp)paramCMNode).getRight(), paramInt);
    }
    else if ((paramCMNode.type() == 2) || (paramCMNode.type() == 3) || (paramCMNode.type() == 1))
    {
      paramInt = postTreeBuildInit(((CMUniOp)paramCMNode).getChild(), paramInt);
    }
    else if (paramCMNode.type() == 0)
    {
      localQName = ((CMLeaf)paramCMNode).getElement();
      if (localpart != fEpsilonString)
      {
        fLeafList[paramInt] = ((CMLeaf)paramCMNode);
        fLeafListType[paramInt] = 0;
        paramInt++;
      }
    }
    else
    {
      throw new RuntimeException("ImplementationMessages.VAL_NIICM: type=" + paramCMNode.type());
    }
    return paramInt;
  }
}
