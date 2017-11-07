package org.apache.xerces.dom.events;

import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

public class MutationEventImpl
  extends EventImpl
  implements MutationEvent
{
  Node relatedNode = null;
  String prevValue = null;
  String newValue = null;
  String attrName = null;
  public short attrChange;
  public static final String DOM_SUBTREE_MODIFIED = "DOMSubtreeModified";
  public static final String DOM_NODE_INSERTED = "DOMNodeInserted";
  public static final String DOM_NODE_REMOVED = "DOMNodeRemoved";
  public static final String DOM_NODE_REMOVED_FROM_DOCUMENT = "DOMNodeRemovedFromDocument";
  public static final String DOM_NODE_INSERTED_INTO_DOCUMENT = "DOMNodeInsertedIntoDocument";
  public static final String DOM_ATTR_MODIFIED = "DOMAttrModified";
  public static final String DOM_CHARACTER_DATA_MODIFIED = "DOMCharacterDataModified";
  
  public MutationEventImpl() {}
  
  public String getAttrName()
  {
    return attrName;
  }
  
  public short getAttrChange()
  {
    return attrChange;
  }
  
  public String getNewValue()
  {
    return newValue;
  }
  
  public String getPrevValue()
  {
    return prevValue;
  }
  
  public Node getRelatedNode()
  {
    return relatedNode;
  }
  
  public void initMutationEvent(String paramString1, boolean paramBoolean1, boolean paramBoolean2, Node paramNode, String paramString2, String paramString3, String paramString4, short paramShort)
  {
    relatedNode = paramNode;
    prevValue = paramString2;
    newValue = paramString3;
    attrName = paramString4;
    attrChange = paramShort;
    super.initEvent(paramString1, paramBoolean1, paramBoolean2);
  }
}
