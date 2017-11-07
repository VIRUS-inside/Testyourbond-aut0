package org.apache.xerces.dom;

import java.util.ArrayList;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.ranges.RangeException;

public class RangeImpl
  implements Range
{
  private DocumentImpl fDocument;
  private Node fStartContainer;
  private Node fEndContainer;
  private int fStartOffset;
  private int fEndOffset;
  private boolean fDetach = false;
  private Node fInsertNode = null;
  private Node fDeleteNode = null;
  private Node fSplitNode = null;
  private boolean fInsertedFromRange = false;
  private Node fRemoveChild = null;
  static final int EXTRACT_CONTENTS = 1;
  static final int CLONE_CONTENTS = 2;
  static final int DELETE_CONTENTS = 3;
  
  public RangeImpl(DocumentImpl paramDocumentImpl)
  {
    fDocument = paramDocumentImpl;
    fStartContainer = paramDocumentImpl;
    fEndContainer = paramDocumentImpl;
    fStartOffset = 0;
    fEndOffset = 0;
    fDetach = false;
  }
  
  public Node getStartContainer()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    return fStartContainer;
  }
  
  public int getStartOffset()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    return fStartOffset;
  }
  
  public Node getEndContainer()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    return fEndContainer;
  }
  
  public int getEndOffset()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    return fEndOffset;
  }
  
  public boolean getCollapsed()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    return (fStartContainer == fEndContainer) && (fStartOffset == fEndOffset);
  }
  
  public Node getCommonAncestorContainer()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    ArrayList localArrayList1 = new ArrayList();
    for (Node localNode = fStartContainer; localNode != null; localNode = localNode.getParentNode()) {
      localArrayList1.add(localNode);
    }
    ArrayList localArrayList2 = new ArrayList();
    for (localNode = fEndContainer; localNode != null; localNode = localNode.getParentNode()) {
      localArrayList2.add(localNode);
    }
    int i = localArrayList1.size() - 1;
    int j = localArrayList2.size() - 1;
    Object localObject = null;
    while ((i >= 0) && (j >= 0))
    {
      if (localArrayList1.get(i) != localArrayList2.get(j)) {
        break;
      }
      localObject = localArrayList1.get(i);
      i--;
      j--;
    }
    return (Node)localObject;
  }
  
  public void setStart(Node paramNode, int paramInt)
    throws RangeException, DOMException
  {
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if (!isLegalContainer(paramNode)) {
        throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
      }
      if ((fDocument != paramNode.getOwnerDocument()) && (fDocument != paramNode)) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
    }
    checkIndex(paramNode, paramInt);
    fStartContainer = paramNode;
    fStartOffset = paramInt;
    if ((getCommonAncestorContainer() == null) || ((fStartContainer == fEndContainer) && (fEndOffset < fStartOffset))) {
      collapse(true);
    }
  }
  
  public void setEnd(Node paramNode, int paramInt)
    throws RangeException, DOMException
  {
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if (!isLegalContainer(paramNode)) {
        throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
      }
      if ((fDocument != paramNode.getOwnerDocument()) && (fDocument != paramNode)) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
    }
    checkIndex(paramNode, paramInt);
    fEndContainer = paramNode;
    fEndOffset = paramInt;
    if ((getCommonAncestorContainer() == null) || ((fStartContainer == fEndContainer) && (fEndOffset < fStartOffset))) {
      collapse(false);
    }
  }
  
  public void setStartBefore(Node paramNode)
    throws RangeException
  {
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if ((!hasLegalRootContainer(paramNode)) || (!isLegalContainedNode(paramNode))) {
        throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
      }
      if ((fDocument != paramNode.getOwnerDocument()) && (fDocument != paramNode)) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
    }
    fStartContainer = paramNode.getParentNode();
    int i = 0;
    for (Node localNode = paramNode; localNode != null; localNode = localNode.getPreviousSibling()) {
      i++;
    }
    fStartOffset = (i - 1);
    if ((getCommonAncestorContainer() == null) || ((fStartContainer == fEndContainer) && (fEndOffset < fStartOffset))) {
      collapse(true);
    }
  }
  
  public void setStartAfter(Node paramNode)
    throws RangeException
  {
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if ((!hasLegalRootContainer(paramNode)) || (!isLegalContainedNode(paramNode))) {
        throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
      }
      if ((fDocument != paramNode.getOwnerDocument()) && (fDocument != paramNode)) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
    }
    fStartContainer = paramNode.getParentNode();
    int i = 0;
    for (Node localNode = paramNode; localNode != null; localNode = localNode.getPreviousSibling()) {
      i++;
    }
    fStartOffset = i;
    if ((getCommonAncestorContainer() == null) || ((fStartContainer == fEndContainer) && (fEndOffset < fStartOffset))) {
      collapse(true);
    }
  }
  
  public void setEndBefore(Node paramNode)
    throws RangeException
  {
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if ((!hasLegalRootContainer(paramNode)) || (!isLegalContainedNode(paramNode))) {
        throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
      }
      if ((fDocument != paramNode.getOwnerDocument()) && (fDocument != paramNode)) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
    }
    fEndContainer = paramNode.getParentNode();
    int i = 0;
    for (Node localNode = paramNode; localNode != null; localNode = localNode.getPreviousSibling()) {
      i++;
    }
    fEndOffset = (i - 1);
    if ((getCommonAncestorContainer() == null) || ((fStartContainer == fEndContainer) && (fEndOffset < fStartOffset))) {
      collapse(false);
    }
  }
  
  public void setEndAfter(Node paramNode)
    throws RangeException
  {
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if ((!hasLegalRootContainer(paramNode)) || (!isLegalContainedNode(paramNode))) {
        throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
      }
      if ((fDocument != paramNode.getOwnerDocument()) && (fDocument != paramNode)) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
    }
    fEndContainer = paramNode.getParentNode();
    int i = 0;
    for (Node localNode = paramNode; localNode != null; localNode = localNode.getPreviousSibling()) {
      i++;
    }
    fEndOffset = i;
    if ((getCommonAncestorContainer() == null) || ((fStartContainer == fEndContainer) && (fEndOffset < fStartOffset))) {
      collapse(false);
    }
  }
  
  public void collapse(boolean paramBoolean)
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    if (paramBoolean)
    {
      fEndContainer = fStartContainer;
      fEndOffset = fStartOffset;
    }
    else
    {
      fStartContainer = fEndContainer;
      fStartOffset = fEndOffset;
    }
  }
  
  public void selectNode(Node paramNode)
    throws RangeException
  {
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if ((!isLegalContainer(paramNode.getParentNode())) || (!isLegalContainedNode(paramNode))) {
        throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
      }
      if ((fDocument != paramNode.getOwnerDocument()) && (fDocument != paramNode)) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
    }
    Node localNode1 = paramNode.getParentNode();
    if (localNode1 != null)
    {
      fStartContainer = localNode1;
      fEndContainer = localNode1;
      int i = 0;
      for (Node localNode2 = paramNode; localNode2 != null; localNode2 = localNode2.getPreviousSibling()) {
        i++;
      }
      fStartOffset = (i - 1);
      fEndOffset = (fStartOffset + 1);
    }
  }
  
  public void selectNodeContents(Node paramNode)
    throws RangeException
  {
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if (!isLegalContainer(paramNode)) {
        throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
      }
      if ((fDocument != paramNode.getOwnerDocument()) && (fDocument != paramNode)) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
    }
    fStartContainer = paramNode;
    fEndContainer = paramNode;
    Node localNode1 = paramNode.getFirstChild();
    fStartOffset = 0;
    if (localNode1 == null)
    {
      fEndOffset = 0;
    }
    else
    {
      int i = 0;
      for (Node localNode2 = localNode1; localNode2 != null; localNode2 = localNode2.getNextSibling()) {
        i++;
      }
      fEndOffset = i;
    }
  }
  
  public short compareBoundaryPoints(short paramShort, Range paramRange)
    throws DOMException
  {
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if (((fDocument != paramRange.getStartContainer().getOwnerDocument()) && (fDocument != paramRange.getStartContainer()) && (paramRange.getStartContainer() != null)) || ((fDocument != paramRange.getEndContainer().getOwnerDocument()) && (fDocument != paramRange.getEndContainer()) && (paramRange.getStartContainer() != null))) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
    }
    Object localObject1;
    Object localObject2;
    int i;
    int j;
    if (paramShort == 0)
    {
      localObject1 = paramRange.getStartContainer();
      localObject2 = fStartContainer;
      i = paramRange.getStartOffset();
      j = fStartOffset;
    }
    else if (paramShort == 1)
    {
      localObject1 = paramRange.getStartContainer();
      localObject2 = fEndContainer;
      i = paramRange.getStartOffset();
      j = fEndOffset;
    }
    else if (paramShort == 3)
    {
      localObject1 = paramRange.getEndContainer();
      localObject2 = fStartContainer;
      i = paramRange.getEndOffset();
      j = fStartOffset;
    }
    else
    {
      localObject1 = paramRange.getEndContainer();
      localObject2 = fEndContainer;
      i = paramRange.getEndOffset();
      j = fEndOffset;
    }
    if (localObject1 == localObject2)
    {
      if (i < j) {
        return 1;
      }
      if (i == j) {
        return 0;
      }
      return -1;
    }
    Object localObject3 = localObject2;
    for (Node localNode1 = localObject3.getParentNode(); localNode1 != null; localNode1 = localNode1.getParentNode())
    {
      if (localNode1 == localObject1)
      {
        int k = indexOf(localObject3, (Node)localObject1);
        if (i <= k) {
          return 1;
        }
        return -1;
      }
      localObject3 = localNode1;
    }
    Object localObject4 = localObject1;
    for (Node localNode2 = localObject4.getParentNode(); localNode2 != null; localNode2 = localNode2.getParentNode())
    {
      if (localNode2 == localObject2)
      {
        m = indexOf(localObject4, (Node)localObject2);
        if (m < j) {
          return 1;
        }
        return -1;
      }
      localObject4 = localNode2;
    }
    int m = 0;
    for (Object localObject5 = localObject1; localObject5 != null; localObject5 = ((Node)localObject5).getParentNode()) {
      m++;
    }
    for (Object localObject6 = localObject2; localObject6 != null; localObject6 = ((Node)localObject6).getParentNode()) {
      m--;
    }
    while (m > 0)
    {
      localObject1 = ((Node)localObject1).getParentNode();
      m--;
    }
    while (m < 0)
    {
      localObject2 = ((Node)localObject2).getParentNode();
      m++;
    }
    Node localNode3 = ((Node)localObject1).getParentNode();
    for (Node localNode4 = ((Node)localObject2).getParentNode(); localNode3 != localNode4; localNode4 = localNode4.getParentNode())
    {
      localObject1 = localNode3;
      localObject2 = localNode4;
      localNode3 = localNode3.getParentNode();
    }
    for (Node localNode5 = ((Node)localObject1).getNextSibling(); localNode5 != null; localNode5 = localNode5.getNextSibling()) {
      if (localNode5 == localObject2) {
        return 1;
      }
    }
    return -1;
  }
  
  public void deleteContents()
    throws DOMException
  {
    traverseContents(3);
  }
  
  public DocumentFragment extractContents()
    throws DOMException
  {
    return traverseContents(1);
  }
  
  public DocumentFragment cloneContents()
    throws DOMException
  {
    return traverseContents(2);
  }
  
  public void insertNode(Node paramNode)
    throws DOMException, RangeException
  {
    if (paramNode == null) {
      return;
    }
    int i = paramNode.getNodeType();
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if (fDocument != paramNode.getOwnerDocument()) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
      if ((i == 2) || (i == 6) || (i == 12) || (i == 9)) {
        throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
      }
    }
    int j = 0;
    fInsertedFromRange = true;
    if (fStartContainer.getNodeType() == 3)
    {
      Node localNode3 = fStartContainer.getParentNode();
      j = localNode3.getChildNodes().getLength();
      Node localNode1 = fStartContainer.cloneNode(false);
      ((TextImpl)localNode1).setNodeValueInternal(localNode1.getNodeValue().substring(fStartOffset));
      ((TextImpl)fStartContainer).setNodeValueInternal(fStartContainer.getNodeValue().substring(0, fStartOffset));
      Node localNode4 = fStartContainer.getNextSibling();
      if (localNode4 != null)
      {
        if (localNode3 != null)
        {
          localNode3.insertBefore(paramNode, localNode4);
          localNode3.insertBefore(localNode1, localNode4);
        }
      }
      else if (localNode3 != null)
      {
        localNode3.appendChild(paramNode);
        localNode3.appendChild(localNode1);
      }
      if (fEndContainer == fStartContainer)
      {
        fEndContainer = localNode1;
        fEndOffset -= fStartOffset;
      }
      else if (fEndContainer == localNode3)
      {
        fEndOffset += localNode3.getChildNodes().getLength() - j;
      }
      signalSplitData(fStartContainer, localNode1, fStartOffset);
    }
    else
    {
      if (fEndContainer == fStartContainer) {
        j = fEndContainer.getChildNodes().getLength();
      }
      Node localNode2 = fStartContainer.getFirstChild();
      int k = 0;
      for (k = 0; (k < fStartOffset) && (localNode2 != null); k++) {
        localNode2 = localNode2.getNextSibling();
      }
      if (localNode2 != null) {
        fStartContainer.insertBefore(paramNode, localNode2);
      } else {
        fStartContainer.appendChild(paramNode);
      }
      if ((fEndContainer == fStartContainer) && (fEndOffset != 0)) {
        fEndOffset += fEndContainer.getChildNodes().getLength() - j;
      }
    }
    fInsertedFromRange = false;
  }
  
  public void surroundContents(Node paramNode)
    throws DOMException, RangeException
  {
    if (paramNode == null) {
      return;
    }
    int i = paramNode.getNodeType();
    if (fDocument.errorChecking)
    {
      if (fDetach) {
        throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
      }
      if ((i == 2) || (i == 6) || (i == 12) || (i == 10) || (i == 9) || (i == 11)) {
        throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
      }
    }
    Node localNode1 = fStartContainer;
    Node localNode2 = fEndContainer;
    if (fStartContainer.getNodeType() == 3) {
      localNode1 = fStartContainer.getParentNode();
    }
    if (fEndContainer.getNodeType() == 3) {
      localNode2 = fEndContainer.getParentNode();
    }
    if (localNode1 != localNode2) {
      throw new RangeExceptionImpl((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "BAD_BOUNDARYPOINTS_ERR", null));
    }
    DocumentFragment localDocumentFragment = extractContents();
    insertNode(paramNode);
    paramNode.appendChild(localDocumentFragment);
    selectNode(paramNode);
  }
  
  public Range cloneRange()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    Range localRange = fDocument.createRange();
    localRange.setStart(fStartContainer, fStartOffset);
    localRange.setEnd(fEndContainer, fEndOffset);
    return localRange;
  }
  
  public String toString()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    Node localNode1 = fStartContainer;
    Node localNode2 = fEndContainer;
    StringBuffer localStringBuffer = new StringBuffer();
    int i;
    if ((fStartContainer.getNodeType() == 3) || (fStartContainer.getNodeType() == 4))
    {
      if (fStartContainer == fEndContainer)
      {
        localStringBuffer.append(fStartContainer.getNodeValue().substring(fStartOffset, fEndOffset));
        return localStringBuffer.toString();
      }
      localStringBuffer.append(fStartContainer.getNodeValue().substring(fStartOffset));
      localNode1 = nextNode(localNode1, true);
    }
    else
    {
      localNode1 = localNode1.getFirstChild();
      if (fStartOffset > 0) {
        for (i = 0; (i < fStartOffset) && (localNode1 != null); i++) {
          localNode1 = localNode1.getNextSibling();
        }
      }
      if (localNode1 == null) {
        localNode1 = nextNode(fStartContainer, false);
      }
    }
    if ((fEndContainer.getNodeType() != 3) && (fEndContainer.getNodeType() != 4))
    {
      i = fEndOffset;
      for (localNode2 = fEndContainer.getFirstChild(); (i > 0) && (localNode2 != null); localNode2 = localNode2.getNextSibling()) {
        i--;
      }
      if (localNode2 == null) {
        localNode2 = nextNode(fEndContainer, false);
      }
    }
    while (localNode1 != localNode2)
    {
      if (localNode1 == null) {
        break;
      }
      if ((localNode1.getNodeType() == 3) || (localNode1.getNodeType() == 4)) {
        localStringBuffer.append(localNode1.getNodeValue());
      }
      localNode1 = nextNode(localNode1, true);
    }
    if ((fEndContainer.getNodeType() == 3) || (fEndContainer.getNodeType() == 4)) {
      localStringBuffer.append(fEndContainer.getNodeValue().substring(0, fEndOffset));
    }
    return localStringBuffer.toString();
  }
  
  public void detach()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    fDetach = true;
    fDocument.removeRange(this);
  }
  
  void signalSplitData(Node paramNode1, Node paramNode2, int paramInt)
  {
    fSplitNode = paramNode1;
    fDocument.splitData(paramNode1, paramNode2, paramInt);
    fSplitNode = null;
  }
  
  void receiveSplitData(Node paramNode1, Node paramNode2, int paramInt)
  {
    if ((paramNode1 == null) || (paramNode2 == null)) {
      return;
    }
    if (fSplitNode == paramNode1) {
      return;
    }
    if ((paramNode1 == fStartContainer) && (fStartContainer.getNodeType() == 3) && (fStartOffset > paramInt))
    {
      fStartOffset -= paramInt;
      fStartContainer = paramNode2;
    }
    if ((paramNode1 == fEndContainer) && (fEndContainer.getNodeType() == 3) && (fEndOffset > paramInt))
    {
      fEndOffset -= paramInt;
      fEndContainer = paramNode2;
    }
  }
  
  void deleteData(CharacterData paramCharacterData, int paramInt1, int paramInt2)
  {
    fDeleteNode = paramCharacterData;
    paramCharacterData.deleteData(paramInt1, paramInt2);
    fDeleteNode = null;
  }
  
  void receiveDeletedText(CharacterDataImpl paramCharacterDataImpl, int paramInt1, int paramInt2)
  {
    if (paramCharacterDataImpl == null) {
      return;
    }
    if (fDeleteNode == paramCharacterDataImpl) {
      return;
    }
    if (paramCharacterDataImpl == fStartContainer) {
      if (fStartOffset > paramInt1 + paramInt2) {
        fStartOffset = (paramInt1 + (fStartOffset - (paramInt1 + paramInt2)));
      } else if (fStartOffset > paramInt1) {
        fStartOffset = paramInt1;
      }
    }
    if (paramCharacterDataImpl == fEndContainer) {
      if (fEndOffset > paramInt1 + paramInt2) {
        fEndOffset = (paramInt1 + (fEndOffset - (paramInt1 + paramInt2)));
      } else if (fEndOffset > paramInt1) {
        fEndOffset = paramInt1;
      }
    }
  }
  
  void insertData(CharacterData paramCharacterData, int paramInt, String paramString)
  {
    fInsertNode = paramCharacterData;
    paramCharacterData.insertData(paramInt, paramString);
    fInsertNode = null;
  }
  
  void receiveInsertedText(CharacterDataImpl paramCharacterDataImpl, int paramInt1, int paramInt2)
  {
    if (paramCharacterDataImpl == null) {
      return;
    }
    if (fInsertNode == paramCharacterDataImpl) {
      return;
    }
    if ((paramCharacterDataImpl == fStartContainer) && (paramInt1 < fStartOffset)) {
      fStartOffset += paramInt2;
    }
    if ((paramCharacterDataImpl == fEndContainer) && (paramInt1 < fEndOffset)) {
      fEndOffset += paramInt2;
    }
  }
  
  void receiveReplacedText(CharacterDataImpl paramCharacterDataImpl)
  {
    if (paramCharacterDataImpl == null) {
      return;
    }
    if (paramCharacterDataImpl == fStartContainer) {
      fStartOffset = 0;
    }
    if (paramCharacterDataImpl == fEndContainer) {
      fEndOffset = 0;
    }
  }
  
  public void insertedNodeFromDOM(Node paramNode)
  {
    if (paramNode == null) {
      return;
    }
    if (fInsertNode == paramNode) {
      return;
    }
    if (fInsertedFromRange) {
      return;
    }
    Node localNode = paramNode.getParentNode();
    int i;
    if (localNode == fStartContainer)
    {
      i = indexOf(paramNode, fStartContainer);
      if (i < fStartOffset) {
        fStartOffset += 1;
      }
    }
    if (localNode == fEndContainer)
    {
      i = indexOf(paramNode, fEndContainer);
      if (i < fEndOffset) {
        fEndOffset += 1;
      }
    }
  }
  
  Node removeChild(Node paramNode1, Node paramNode2)
  {
    fRemoveChild = paramNode2;
    Node localNode = paramNode1.removeChild(paramNode2);
    fRemoveChild = null;
    return localNode;
  }
  
  void removeNode(Node paramNode)
  {
    if (paramNode == null) {
      return;
    }
    if (fRemoveChild == paramNode) {
      return;
    }
    Node localNode = paramNode.getParentNode();
    int i;
    if (localNode == fStartContainer)
    {
      i = indexOf(paramNode, fStartContainer);
      if (i < fStartOffset) {
        fStartOffset -= 1;
      }
    }
    if (localNode == fEndContainer)
    {
      i = indexOf(paramNode, fEndContainer);
      if (i < fEndOffset) {
        fEndOffset -= 1;
      }
    }
    if ((localNode != fStartContainer) || (localNode != fEndContainer))
    {
      if (isAncestorOf(paramNode, fStartContainer))
      {
        fStartContainer = localNode;
        fStartOffset = indexOf(paramNode, localNode);
      }
      if (isAncestorOf(paramNode, fEndContainer))
      {
        fEndContainer = localNode;
        fEndOffset = indexOf(paramNode, localNode);
      }
    }
  }
  
  private DocumentFragment traverseContents(int paramInt)
    throws DOMException
  {
    if ((fStartContainer == null) || (fEndContainer == null)) {
      return null;
    }
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    if (fStartContainer == fEndContainer) {
      return traverseSameContainer(paramInt);
    }
    int i = 0;
    Object localObject1 = fEndContainer;
    for (Node localNode1 = ((Node)localObject1).getParentNode(); localNode1 != null; localNode1 = localNode1.getParentNode())
    {
      if (localNode1 == fStartContainer) {
        return traverseCommonStartContainer((Node)localObject1, paramInt);
      }
      i++;
      localObject1 = localNode1;
    }
    int j = 0;
    Object localObject2 = fStartContainer;
    for (Node localNode2 = ((Node)localObject2).getParentNode(); localNode2 != null; localNode2 = localNode2.getParentNode())
    {
      if (localNode2 == fEndContainer) {
        return traverseCommonEndContainer((Node)localObject2, paramInt);
      }
      j++;
      localObject2 = localNode2;
    }
    int k = j - i;
    Object localObject3 = fStartContainer;
    while (k > 0)
    {
      localObject3 = ((Node)localObject3).getParentNode();
      k--;
    }
    Object localObject4 = fEndContainer;
    while (k < 0)
    {
      localObject4 = ((Node)localObject4).getParentNode();
      k++;
    }
    Node localNode3 = ((Node)localObject3).getParentNode();
    for (Node localNode4 = ((Node)localObject4).getParentNode(); localNode3 != localNode4; localNode4 = localNode4.getParentNode())
    {
      localObject3 = localNode3;
      localObject4 = localNode4;
      localNode3 = localNode3.getParentNode();
    }
    return traverseCommonAncestors((Node)localObject3, (Node)localObject4, paramInt);
  }
  
  private DocumentFragment traverseSameContainer(int paramInt)
  {
    DocumentFragment localDocumentFragment = null;
    if (paramInt != 3) {
      localDocumentFragment = fDocument.createDocumentFragment();
    }
    if (fStartOffset == fEndOffset) {
      return localDocumentFragment;
    }
    int i = fStartContainer.getNodeType();
    if ((i == 3) || (i == 4) || (i == 8) || (i == 7))
    {
      localObject = fStartContainer.getNodeValue();
      String str = ((String)localObject).substring(fStartOffset, fEndOffset);
      if (paramInt != 2)
      {
        ((CharacterDataImpl)fStartContainer).deleteData(fStartOffset, fEndOffset - fStartOffset);
        collapse(true);
      }
      if (paramInt == 3) {
        return null;
      }
      if (i == 3) {
        localDocumentFragment.appendChild(fDocument.createTextNode(str));
      } else if (i == 4) {
        localDocumentFragment.appendChild(fDocument.createCDATASection(str));
      } else if (i == 8) {
        localDocumentFragment.appendChild(fDocument.createComment(str));
      } else {
        localDocumentFragment.appendChild(fDocument.createProcessingInstruction(fStartContainer.getNodeName(), str));
      }
      return localDocumentFragment;
    }
    Object localObject = getSelectedNode(fStartContainer, fStartOffset);
    int j = fEndOffset - fStartOffset;
    while (j > 0)
    {
      Node localNode1 = ((Node)localObject).getNextSibling();
      Node localNode2 = traverseFullySelected((Node)localObject, paramInt);
      if (localDocumentFragment != null) {
        localDocumentFragment.appendChild(localNode2);
      }
      j--;
      localObject = localNode1;
    }
    if (paramInt != 2) {
      collapse(true);
    }
    return localDocumentFragment;
  }
  
  private DocumentFragment traverseCommonStartContainer(Node paramNode, int paramInt)
  {
    DocumentFragment localDocumentFragment = null;
    if (paramInt != 3) {
      localDocumentFragment = fDocument.createDocumentFragment();
    }
    Object localObject = traverseRightBoundary(paramNode, paramInt);
    if (localDocumentFragment != null) {
      localDocumentFragment.appendChild((Node)localObject);
    }
    int i = indexOf(paramNode, fStartContainer);
    int j = i - fStartOffset;
    if (j <= 0)
    {
      if (paramInt != 2)
      {
        setEndBefore(paramNode);
        collapse(false);
      }
      return localDocumentFragment;
    }
    Node localNode1;
    for (localObject = paramNode.getPreviousSibling(); j > 0; localObject = localNode1)
    {
      localNode1 = ((Node)localObject).getPreviousSibling();
      Node localNode2 = traverseFullySelected((Node)localObject, paramInt);
      if (localDocumentFragment != null) {
        localDocumentFragment.insertBefore(localNode2, localDocumentFragment.getFirstChild());
      }
      j--;
    }
    if (paramInt != 2)
    {
      setEndBefore(paramNode);
      collapse(false);
    }
    return localDocumentFragment;
  }
  
  private DocumentFragment traverseCommonEndContainer(Node paramNode, int paramInt)
  {
    DocumentFragment localDocumentFragment = null;
    if (paramInt != 3) {
      localDocumentFragment = fDocument.createDocumentFragment();
    }
    Object localObject = traverseLeftBoundary(paramNode, paramInt);
    if (localDocumentFragment != null) {
      localDocumentFragment.appendChild((Node)localObject);
    }
    int i = indexOf(paramNode, fEndContainer);
    i++;
    int j = fEndOffset - i;
    Node localNode1;
    for (localObject = paramNode.getNextSibling(); j > 0; localObject = localNode1)
    {
      localNode1 = ((Node)localObject).getNextSibling();
      Node localNode2 = traverseFullySelected((Node)localObject, paramInt);
      if (localDocumentFragment != null) {
        localDocumentFragment.appendChild(localNode2);
      }
      j--;
    }
    if (paramInt != 2)
    {
      setStartAfter(paramNode);
      collapse(true);
    }
    return localDocumentFragment;
  }
  
  private DocumentFragment traverseCommonAncestors(Node paramNode1, Node paramNode2, int paramInt)
  {
    DocumentFragment localDocumentFragment = null;
    if (paramInt != 3) {
      localDocumentFragment = fDocument.createDocumentFragment();
    }
    Node localNode1 = traverseLeftBoundary(paramNode1, paramInt);
    if (localDocumentFragment != null) {
      localDocumentFragment.appendChild(localNode1);
    }
    Node localNode2 = paramNode1.getParentNode();
    int i = indexOf(paramNode1, localNode2);
    int j = indexOf(paramNode2, localNode2);
    i++;
    int k = j - i;
    Object localObject = paramNode1.getNextSibling();
    while (k > 0)
    {
      Node localNode3 = ((Node)localObject).getNextSibling();
      localNode1 = traverseFullySelected((Node)localObject, paramInt);
      if (localDocumentFragment != null) {
        localDocumentFragment.appendChild(localNode1);
      }
      localObject = localNode3;
      k--;
    }
    localNode1 = traverseRightBoundary(paramNode2, paramInt);
    if (localDocumentFragment != null) {
      localDocumentFragment.appendChild(localNode1);
    }
    if (paramInt != 2)
    {
      setStartAfter(paramNode1);
      collapse(true);
    }
    return localDocumentFragment;
  }
  
  private Node traverseRightBoundary(Node paramNode, int paramInt)
  {
    Object localObject1 = getSelectedNode(fEndContainer, fEndOffset - 1);
    boolean bool = localObject1 != fEndContainer;
    if (localObject1 == paramNode) {
      return traverseNode((Node)localObject1, bool, false, paramInt);
    }
    Node localNode1 = ((Node)localObject1).getParentNode();
    Object localObject2 = traverseNode(localNode1, false, false, paramInt);
    while (localNode1 != null)
    {
      while (localObject1 != null)
      {
        localNode2 = ((Node)localObject1).getPreviousSibling();
        Node localNode3 = traverseNode((Node)localObject1, bool, false, paramInt);
        if (paramInt != 3) {
          ((Node)localObject2).insertBefore(localNode3, ((Node)localObject2).getFirstChild());
        }
        bool = true;
        localObject1 = localNode2;
      }
      if (localNode1 == paramNode) {
        return localObject2;
      }
      localObject1 = localNode1.getPreviousSibling();
      localNode1 = localNode1.getParentNode();
      Node localNode2 = traverseNode(localNode1, false, false, paramInt);
      if (paramInt != 3) {
        localNode2.appendChild((Node)localObject2);
      }
      localObject2 = localNode2;
    }
    return null;
  }
  
  private Node traverseLeftBoundary(Node paramNode, int paramInt)
  {
    Object localObject1 = getSelectedNode(getStartContainer(), getStartOffset());
    boolean bool = localObject1 != getStartContainer();
    if (localObject1 == paramNode) {
      return traverseNode((Node)localObject1, bool, true, paramInt);
    }
    Node localNode1 = ((Node)localObject1).getParentNode();
    Object localObject2 = traverseNode(localNode1, false, true, paramInt);
    while (localNode1 != null)
    {
      while (localObject1 != null)
      {
        localNode2 = ((Node)localObject1).getNextSibling();
        Node localNode3 = traverseNode((Node)localObject1, bool, true, paramInt);
        if (paramInt != 3) {
          ((Node)localObject2).appendChild(localNode3);
        }
        bool = true;
        localObject1 = localNode2;
      }
      if (localNode1 == paramNode) {
        return localObject2;
      }
      localObject1 = localNode1.getNextSibling();
      localNode1 = localNode1.getParentNode();
      Node localNode2 = traverseNode(localNode1, false, true, paramInt);
      if (paramInt != 3) {
        localNode2.appendChild((Node)localObject2);
      }
      localObject2 = localNode2;
    }
    return null;
  }
  
  private Node traverseNode(Node paramNode, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
  {
    if (paramBoolean1) {
      return traverseFullySelected(paramNode, paramInt);
    }
    int i = paramNode.getNodeType();
    if ((i == 3) || (i == 4) || (i == 8) || (i == 7)) {
      return traverseCharacterDataNode(paramNode, paramBoolean2, paramInt);
    }
    return traversePartiallySelected(paramNode, paramInt);
  }
  
  private Node traverseFullySelected(Node paramNode, int paramInt)
  {
    switch (paramInt)
    {
    case 2: 
      return paramNode.cloneNode(true);
    case 1: 
      if (paramNode.getNodeType() == 10) {
        throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
      }
      return paramNode;
    case 3: 
      paramNode.getParentNode().removeChild(paramNode);
      return null;
    }
    return null;
  }
  
  private Node traversePartiallySelected(Node paramNode, int paramInt)
  {
    switch (paramInt)
    {
    case 3: 
      return null;
    case 1: 
    case 2: 
      return paramNode.cloneNode(false);
    }
    return null;
  }
  
  private Node traverseCharacterDataNode(Node paramNode, boolean paramBoolean, int paramInt)
  {
    String str1 = paramNode.getNodeValue();
    int i;
    String str2;
    String str3;
    if (paramBoolean)
    {
      i = getStartOffset();
      str2 = str1.substring(i);
      str3 = str1.substring(0, i);
    }
    else
    {
      i = getEndOffset();
      str2 = str1.substring(0, i);
      str3 = str1.substring(i);
    }
    if (paramInt != 2) {
      paramNode.setNodeValue(str3);
    }
    if (paramInt == 3) {
      return null;
    }
    Node localNode = paramNode.cloneNode(false);
    localNode.setNodeValue(str2);
    return localNode;
  }
  
  void checkIndex(Node paramNode, int paramInt)
    throws DOMException
  {
    if (paramInt < 0) {
      throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null));
    }
    int i = paramNode.getNodeType();
    if ((i == 3) || (i == 4) || (i == 8) || (i == 7))
    {
      if (paramInt > paramNode.getNodeValue().length()) {
        throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null));
      }
    }
    else if (paramInt > paramNode.getChildNodes().getLength()) {
      throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null));
    }
  }
  
  private Node getRootContainer(Node paramNode)
  {
    if (paramNode == null) {
      return null;
    }
    while (paramNode.getParentNode() != null) {
      paramNode = paramNode.getParentNode();
    }
    return paramNode;
  }
  
  private boolean isLegalContainer(Node paramNode)
  {
    if (paramNode == null) {
      return false;
    }
    while (paramNode != null)
    {
      switch (paramNode.getNodeType())
      {
      case 6: 
      case 10: 
      case 12: 
        return false;
      }
      paramNode = paramNode.getParentNode();
    }
    return true;
  }
  
  private boolean hasLegalRootContainer(Node paramNode)
  {
    if (paramNode == null) {
      return false;
    }
    Node localNode = getRootContainer(paramNode);
    switch (localNode.getNodeType())
    {
    case 2: 
    case 9: 
    case 11: 
      return true;
    }
    return false;
  }
  
  private boolean isLegalContainedNode(Node paramNode)
  {
    if (paramNode == null) {
      return false;
    }
    switch (paramNode.getNodeType())
    {
    case 2: 
    case 6: 
    case 9: 
    case 11: 
    case 12: 
      return false;
    }
    return true;
  }
  
  Node nextNode(Node paramNode, boolean paramBoolean)
  {
    if (paramNode == null) {
      return null;
    }
    if (paramBoolean)
    {
      localNode1 = paramNode.getFirstChild();
      if (localNode1 != null) {
        return localNode1;
      }
    }
    Node localNode1 = paramNode.getNextSibling();
    if (localNode1 != null) {
      return localNode1;
    }
    for (Node localNode2 = paramNode.getParentNode(); (localNode2 != null) && (localNode2 != fDocument); localNode2 = localNode2.getParentNode())
    {
      localNode1 = localNode2.getNextSibling();
      if (localNode1 != null) {
        return localNode1;
      }
    }
    return null;
  }
  
  boolean isAncestorOf(Node paramNode1, Node paramNode2)
  {
    for (Node localNode = paramNode2; localNode != null; localNode = localNode.getParentNode()) {
      if (localNode == paramNode1) {
        return true;
      }
    }
    return false;
  }
  
  int indexOf(Node paramNode1, Node paramNode2)
  {
    if (paramNode1.getParentNode() != paramNode2) {
      return -1;
    }
    int i = 0;
    for (Node localNode = paramNode2.getFirstChild(); localNode != paramNode1; localNode = localNode.getNextSibling()) {
      i++;
    }
    return i;
  }
  
  private Node getSelectedNode(Node paramNode, int paramInt)
  {
    if (paramNode.getNodeType() == 3) {
      return paramNode;
    }
    if (paramInt < 0) {
      return paramNode;
    }
    for (Node localNode = paramNode.getFirstChild(); (localNode != null) && (paramInt > 0); localNode = localNode.getNextSibling()) {
      paramInt--;
    }
    if (localNode != null) {
      return localNode;
    }
    return paramNode;
  }
}
