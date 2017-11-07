package org.apache.xml.dtm.ref;

import javax.xml.transform.Source;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.XMLStringFactory;













































public abstract class DTMDefaultBaseTraversers
  extends DTMDefaultBase
{
  public DTMDefaultBaseTraversers(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
  {
    super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
  }
  























  public DTMDefaultBaseTraversers(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable)
  {
    super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
  }
  




  public DTMAxisTraverser getAxisTraverser(int axis)
  {
    DTMAxisTraverser traverser;
    


    DTMAxisTraverser traverser;
    


    if (null == m_traversers)
    {
      m_traversers = new DTMAxisTraverser[Axis.getNamesLength()];
      traverser = null;
    }
    else
    {
      traverser = m_traversers[axis];
      
      if (traverser != null) {
        return traverser;
      }
    }
    switch (axis)
    {
    case 0: 
      traverser = new AncestorTraverser(null);
      break;
    case 1: 
      traverser = new AncestorOrSelfTraverser(null);
      break;
    case 2: 
      traverser = new AttributeTraverser(null);
      break;
    case 3: 
      traverser = new ChildTraverser(null);
      break;
    case 4: 
      traverser = new DescendantTraverser(null);
      break;
    case 5: 
      traverser = new DescendantOrSelfTraverser(null);
      break;
    case 6: 
      traverser = new FollowingTraverser(null);
      break;
    case 7: 
      traverser = new FollowingSiblingTraverser(null);
      break;
    case 9: 
      traverser = new NamespaceTraverser(null);
      break;
    case 8: 
      traverser = new NamespaceDeclsTraverser(null);
      break;
    case 10: 
      traverser = new ParentTraverser(null);
      break;
    case 11: 
      traverser = new PrecedingTraverser(null);
      break;
    case 12: 
      traverser = new PrecedingSiblingTraverser(null);
      break;
    case 13: 
      traverser = new SelfTraverser(null);
      break;
    case 16: 
      traverser = new AllFromRootTraverser(null);
      break;
    case 14: 
      traverser = new AllFromNodeTraverser(null);
      break;
    case 15: 
      traverser = new PrecedingAndAncestorTraverser(null);
      break;
    case 17: 
      traverser = new DescendantFromRootTraverser(null);
      break;
    case 18: 
      traverser = new DescendantOrSelfFromRootTraverser(null);
      break;
    case 19: 
      traverser = new RootTraverser(null);
      break;
    case 20: 
      return null;
    default: 
      throw new DTMException(XMLMessages.createXMLMessage("ER_UNKNOWN_AXIS_TYPE", new Object[] { Integer.toString(axis) }));
    }
    
    if (null == traverser) {
      throw new DTMException(XMLMessages.createXMLMessage("ER_AXIS_TRAVERSER_NOT_SUPPORTED", new Object[] { Axis.getNames(axis) }));
    }
    

    m_traversers[axis] = traverser;
    
    return traverser;
  }
  
  private class AncestorTraverser extends DTMAxisTraverser
  {
    AncestorTraverser(DTMDefaultBaseTraversers.1 x1) {
      this();
    }
    








    public int next(int context, int current)
    {
      return getParent(current);
    }
    











    public int next(int context, int current, int expandedTypeID)
    {
      current = makeNodeIdentity(current);
      
      while (-1 != (current = m_parent.elementAt(current)))
      {
        if (m_exptype.elementAt(current) == expandedTypeID) {
          return makeNodeHandle(current);
        }
      }
      return -1;
    }
    
    private AncestorTraverser() {}
  }
  
  private class AncestorOrSelfTraverser extends DTMDefaultBaseTraversers.AncestorTraverser {
    private AncestorOrSelfTraverser() { super(null); } AncestorOrSelfTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    










    public int first(int context)
    {
      return context;
    }
    












    public int first(int context, int expandedTypeID)
    {
      return getExpandedTypeID(context) == expandedTypeID ? context : next(context, context, expandedTypeID);
    }
  }
  
  private class AttributeTraverser extends DTMAxisTraverser
  {
    AttributeTraverser(DTMDefaultBaseTraversers.1 x1)
    {
      this();
    }
    








    public int next(int context, int current)
    {
      return context == current ? getFirstAttribute(context) : getNextAttribute(current);
    }
    












    public int next(int context, int current, int expandedTypeID)
    {
      current = context == current ? getFirstAttribute(context) : getNextAttribute(current);
      

      do
      {
        if (getExpandedTypeID(current) == expandedTypeID) {
          return current;
        }
      } while (-1 != (current = getNextAttribute(current)));
      
      return -1;
    }
    
    private AttributeTraverser() {}
  }
  
  private class ChildTraverser extends DTMAxisTraverser {
    ChildTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    















    protected int getNextIndexed(int axisRoot, int nextPotential, int expandedTypeID)
    {
      int nsIndex = m_expandedNameTable.getNamespaceID(expandedTypeID);
      int lnIndex = m_expandedNameTable.getLocalNameID(expandedTypeID);
      
      for (;;)
      {
        int nextID = findElementFromIndex(nsIndex, lnIndex, nextPotential);
        
        if (-2 != nextID)
        {
          int parentID = m_parent.elementAt(nextID);
          

          if (parentID == axisRoot) {
            return nextID;
          }
          

          if (parentID < axisRoot) {
            return -1;
          }
          




          do
          {
            parentID = m_parent.elementAt(parentID);
            if (parentID < axisRoot) {
              return -1;
            }
          } while (parentID > axisRoot);
          

          nextPotential = nextID + 1;
        }
        else
        {
          nextNode();
          
          if (m_nextsib.elementAt(axisRoot) != -2)
            break;
        }
      }
      return -1;
    }
    












    public int first(int context)
    {
      return getFirstChild(context);
    }
    
















    public int first(int context, int expandedTypeID)
    {
      int identity = makeNodeIdentity(context);
      
      int firstMatch = getNextIndexed(identity, _firstch(identity), expandedTypeID);
      

      return makeNodeHandle(firstMatch);
    }
    





















    public int next(int context, int current)
    {
      return getNextSibling(current);
    }
    











    public int next(int context, int current, int expandedTypeID)
    {
      for (current = _nextsib(makeNodeIdentity(current)); 
          -1 != current; 
          current = _nextsib(current))
      {
        if (m_exptype.elementAt(current) == expandedTypeID) {
          return makeNodeHandle(current);
        }
      }
      return -1;
    }
    
    private ChildTraverser() {}
  }
  
  private abstract class IndexedDTMAxisTraverser extends DTMAxisTraverser {
    IndexedDTMAxisTraverser(DTMDefaultBaseTraversers.1 x1) {
      this();
    }
    










    protected final boolean isIndexed(int expandedTypeID)
    {
      return (m_indexing) && (1 == m_expandedNameTable.getType(expandedTypeID));
    }
    







































    protected int getNextIndexed(int axisRoot, int nextPotential, int expandedTypeID)
    {
      int nsIndex = m_expandedNameTable.getNamespaceID(expandedTypeID);
      int lnIndex = m_expandedNameTable.getLocalNameID(expandedTypeID);
      
      for (;;)
      {
        int next = findElementFromIndex(nsIndex, lnIndex, nextPotential);
        
        if (-2 != next)
        {
          if (isAfterAxis(axisRoot, next)) {
            return -1;
          }
          
          return next;
        }
        if (axisHasBeenProcessed(axisRoot)) {
          break;
        }
        nextNode();
      }
      
      return -1; }
    
    private IndexedDTMAxisTraverser() {}
    
    protected abstract boolean isAfterAxis(int paramInt1, int paramInt2);
    
    protected abstract boolean axisHasBeenProcessed(int paramInt); }
  private class DescendantTraverser extends DTMDefaultBaseTraversers.IndexedDTMAxisTraverser { private DescendantTraverser() { super(null); } DescendantTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    








    protected int getFirstPotential(int identity)
    {
      return identity + 1;
    }
    








    protected boolean axisHasBeenProcessed(int axisRoot)
    {
      return m_nextsib.elementAt(axisRoot) != -2;
    }
    








    protected int getSubtreeRoot(int handle)
    {
      return makeNodeIdentity(handle);
    }
    












    protected boolean isDescendant(int subtreeRootIdentity, int identity)
    {
      return _parent(identity) >= subtreeRootIdentity;
    }
    













    protected boolean isAfterAxis(int axisRoot, int identity)
    {
      do
      {
        if (identity == axisRoot)
          return false;
        identity = m_parent.elementAt(identity);
      }
      while (identity >= axisRoot);
      
      return true;
    }
    















    public int first(int context, int expandedTypeID)
    {
      if (isIndexed(expandedTypeID))
      {
        int identity = getSubtreeRoot(context);
        int firstPotential = getFirstPotential(identity);
        
        return makeNodeHandle(getNextIndexed(identity, firstPotential, expandedTypeID));
      }
      
      return next(context, context, expandedTypeID);
    }
    









    public int next(int context, int current)
    {
      int subtreeRootIdent = getSubtreeRoot(context);
      
      for (current = makeNodeIdentity(current) + 1;; current++)
      {
        int type = _type(current);
        
        if (!isDescendant(subtreeRootIdent, current)) {
          return -1;
        }
        if ((2 != type) && (13 != type))
        {

          return makeNodeHandle(current);
        }
      }
    }
    










    public int next(int context, int current, int expandedTypeID)
    {
      int subtreeRootIdent = getSubtreeRoot(context);
      
      current = makeNodeIdentity(current) + 1;
      
      if (isIndexed(expandedTypeID))
      {
        return makeNodeHandle(getNextIndexed(subtreeRootIdent, current, expandedTypeID));
      }
      for (;; 
          current++)
      {
        int exptype = _exptype(current);
        
        if (!isDescendant(subtreeRootIdent, current)) {
          return -1;
        }
        if (exptype == expandedTypeID)
        {

          return makeNodeHandle(current);
        }
      }
    }
  }
  
  private class DescendantOrSelfTraverser
    extends DTMDefaultBaseTraversers.DescendantTraverser {
    private DescendantOrSelfTraverser() { super(null); } DescendantOrSelfTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    









    protected int getFirstPotential(int identity)
    {
      return identity;
    }
    









    public int first(int context)
    {
      return context;
    }
  }
  
  private class AllFromNodeTraverser
    extends DTMDefaultBaseTraversers.DescendantOrSelfTraverser
  {
    private AllFromNodeTraverser() { super(null); } AllFromNodeTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    










    public int next(int context, int current)
    {
      int subtreeRootIdent = makeNodeIdentity(context);
      
      current = makeNodeIdentity(current) + 1;
      





      _exptype(current);
      
      if (!isDescendant(subtreeRootIdent, current)) {
        return -1;
      }
      return makeNodeHandle(current);
    }
  }
  

  private class FollowingTraverser
    extends DTMDefaultBaseTraversers.DescendantTraverser
  {
    private FollowingTraverser() { super(null); } FollowingTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    









    public int first(int context)
    {
      context = makeNodeIdentity(context);
      

      int type = _type(context);
      
      if ((2 == type) || (13 == type))
      {
        context = _parent(context);
        int first = _firstch(context);
        
        if (-1 != first) {
          return makeNodeHandle(first);
        }
      }
      int first;
      do {
        first = _nextsib(context);
        
        if (-1 == first) {
          context = _parent(context);
        }
      } while ((-1 == first) && (-1 != context));
      
      return makeNodeHandle(first);
    }
    












    public int first(int context, int expandedTypeID)
    {
      int type = getNodeType(context);
      
      if ((2 == type) || (13 == type))
      {
        context = getParent(context);
        int first = getFirstChild(context);
        
        if (-1 != first)
        {
          if (getExpandedTypeID(first) == expandedTypeID) {
            return first;
          }
          return next(context, first, expandedTypeID);
        }
      }
      int first;
      do
      {
        first = getNextSibling(context);
        
        if (-1 == first) {
          context = getParent(context);
        }
        else {
          if (getExpandedTypeID(first) == expandedTypeID) {
            return first;
          }
          return next(context, first, expandedTypeID);
        }
        
      } while ((-1 == first) && (-1 != context));
      
      return first;
    }
    









    public int next(int context, int current)
    {
      current = makeNodeIdentity(current);
      int type;
      do
      {
        current++;
        

        type = _type(current);
        
        if (-1 == type) {
          return -1;
        }
      } while ((2 == type) || (13 == type));
      

      return makeNodeHandle(current);
    }
    












    public int next(int context, int current, int expandedTypeID)
    {
      current = makeNodeIdentity(current);
      int etype;
      do
      {
        current++;
        
        etype = _exptype(current);
        
        if (-1 == etype) {
          return -1;
        }
      } while (etype != expandedTypeID);
      

      return makeNodeHandle(current);
    }
  }
  
  private class FollowingSiblingTraverser extends DTMAxisTraverser
  {
    FollowingSiblingTraverser(DTMDefaultBaseTraversers.1 x1)
    {
      this();
    }
    








    public int next(int context, int current)
    {
      return getNextSibling(current);
    }
    











    public int next(int context, int current, int expandedTypeID)
    {
      while (-1 != (current = getNextSibling(current)))
      {
        if (getExpandedTypeID(current) == expandedTypeID) {
          return current;
        }
      }
      return -1;
    }
    
    private FollowingSiblingTraverser() {}
  }
  
  private class NamespaceDeclsTraverser extends DTMAxisTraverser {
    NamespaceDeclsTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    










    public int next(int context, int current)
    {
      return context == current ? getFirstNamespaceNode(context, false) : getNextNamespaceNode(context, current, false);
    }
    













    public int next(int context, int current, int expandedTypeID)
    {
      current = context == current ? getFirstNamespaceNode(context, false) : getNextNamespaceNode(context, current, false);
      


      do
      {
        if (getExpandedTypeID(current) == expandedTypeID) {
          return current;
        }
        
      } while (-1 != (current = getNextNamespaceNode(context, current, false)));
      
      return -1;
    }
    
    private NamespaceDeclsTraverser() {}
  }
  
  private class NamespaceTraverser extends DTMAxisTraverser {
    NamespaceTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    










    public int next(int context, int current)
    {
      return context == current ? getFirstNamespaceNode(context, true) : getNextNamespaceNode(context, current, true);
    }
    













    public int next(int context, int current, int expandedTypeID)
    {
      current = context == current ? getFirstNamespaceNode(context, true) : getNextNamespaceNode(context, current, true);
      


      do
      {
        if (getExpandedTypeID(current) == expandedTypeID) {
          return current;
        }
        
      } while (-1 != (current = getNextNamespaceNode(context, current, true)));
      
      return -1;
    }
    
    private NamespaceTraverser() {}
  }
  
  private class ParentTraverser extends DTMAxisTraverser {
    ParentTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    












    public int first(int context)
    {
      return getParent(context);
    }
    















    public int first(int current, int expandedTypeID)
    {
      current = makeNodeIdentity(current);
      
      while (-1 != (current = m_parent.elementAt(current)))
      {
        if (m_exptype.elementAt(current) == expandedTypeID) {
          return makeNodeHandle(current);
        }
      }
      return -1;
    }
    










    public int next(int context, int current)
    {
      return -1;
    }
    













    public int next(int context, int current, int expandedTypeID)
    {
      return -1;
    }
    
    private ParentTraverser() {}
  }
  
  private class PrecedingTraverser extends DTMAxisTraverser {
    PrecedingTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    












    protected boolean isAncestor(int contextIdent, int currentIdent)
    {
      for (contextIdent = m_parent.elementAt(contextIdent); -1 != contextIdent; 
          contextIdent = m_parent.elementAt(contextIdent))
      {
        if (contextIdent == currentIdent) {
          return true;
        }
      }
      return false;
    }
    









    public int next(int context, int current)
    {
      int subtreeRootIdent = makeNodeIdentity(context);
      
      for (current = makeNodeIdentity(current) - 1; current >= 0; current--)
      {
        short type = _type(current);
        
        if ((2 != type) && (13 != type) && (!isAncestor(subtreeRootIdent, current)))
        {


          return makeNodeHandle(current);
        }
      }
      return -1;
    }
    











    public int next(int context, int current, int expandedTypeID)
    {
      int subtreeRootIdent = makeNodeIdentity(context);
      
      for (current = makeNodeIdentity(current) - 1; current >= 0; current--)
      {
        int exptype = m_exptype.elementAt(current);
        
        if ((exptype == expandedTypeID) && (!isAncestor(subtreeRootIdent, current)))
        {


          return makeNodeHandle(current);
        }
      }
      return -1;
    }
    
    private PrecedingTraverser() {}
  }
  
  private class PrecedingAndAncestorTraverser extends DTMAxisTraverser {
    PrecedingAndAncestorTraverser(DTMDefaultBaseTraversers.1 x1) {
      this();
    }
    









    public int next(int context, int current)
    {
      int subtreeRootIdent = makeNodeIdentity(context);
      
      for (current = makeNodeIdentity(current) - 1; current >= 0; current--)
      {
        short type = _type(current);
        
        if ((2 != type) && (13 != type))
        {

          return makeNodeHandle(current);
        }
      }
      return -1;
    }
    











    public int next(int context, int current, int expandedTypeID)
    {
      int subtreeRootIdent = makeNodeIdentity(context);
      
      for (current = makeNodeIdentity(current) - 1; current >= 0; current--)
      {
        int exptype = m_exptype.elementAt(current);
        
        if (exptype == expandedTypeID)
        {

          return makeNodeHandle(current);
        }
      }
      return -1;
    }
    
    private PrecedingAndAncestorTraverser() {}
  }
  
  private class PrecedingSiblingTraverser extends DTMAxisTraverser {
    PrecedingSiblingTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    









    public int next(int context, int current)
    {
      return getPreviousSibling(current);
    }
    











    public int next(int context, int current, int expandedTypeID)
    {
      while (-1 != (current = getPreviousSibling(current)))
      {
        if (getExpandedTypeID(current) == expandedTypeID) {
          return current;
        }
      }
      return -1;
    }
    
    private PrecedingSiblingTraverser() {}
  }
  
  private class SelfTraverser extends DTMAxisTraverser {
    SelfTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    










    public int first(int context)
    {
      return context;
    }
    












    public int first(int context, int expandedTypeID)
    {
      return getExpandedTypeID(context) == expandedTypeID ? context : -1;
    }
    








    public int next(int context, int current)
    {
      return -1;
    }
    










    public int next(int context, int current, int expandedTypeID)
    {
      return -1;
    }
    
    private SelfTraverser() {}
  }
  
  private class AllFromRootTraverser extends DTMDefaultBaseTraversers.AllFromNodeTraverser {
    private AllFromRootTraverser() { super(null); } AllFromRootTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    








    public int first(int context)
    {
      return getDocumentRoot(context);
    }
    








    public int first(int context, int expandedTypeID)
    {
      return getExpandedTypeID(getDocumentRoot(context)) == expandedTypeID ? context : next(context, context, expandedTypeID);
    }
    










    public int next(int context, int current)
    {
      int subtreeRootIdent = makeNodeIdentity(context);
      
      current = makeNodeIdentity(current) + 1;
      

      int type = _type(current);
      if (type == -1) {
        return -1;
      }
      return makeNodeHandle(current);
    }
    












    public int next(int context, int current, int expandedTypeID)
    {
      int subtreeRootIdent = makeNodeIdentity(context);
      
      for (current = makeNodeIdentity(current) + 1;; current++)
      {
        int exptype = _exptype(current);
        
        if (exptype == -1) {
          return -1;
        }
        if (exptype == expandedTypeID)
        {

          return makeNodeHandle(current);
        }
      }
    }
  }
  
  private class RootTraverser
    extends DTMDefaultBaseTraversers.AllFromRootTraverser {
    private RootTraverser() { super(null); } RootTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    









    public int first(int context, int expandedTypeID)
    {
      int root = getDocumentRoot(context);
      return getExpandedTypeID(root) == expandedTypeID ? root : -1;
    }
    









    public int next(int context, int current)
    {
      return -1;
    }
    










    public int next(int context, int current, int expandedTypeID)
    {
      return -1;
    }
  }
  

  private class DescendantOrSelfFromRootTraverser
    extends DTMDefaultBaseTraversers.DescendantTraverser
  {
    private DescendantOrSelfFromRootTraverser() { super(null); } DescendantOrSelfFromRootTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    









    protected int getFirstPotential(int identity)
    {
      return identity;
    }
    






    protected int getSubtreeRoot(int handle)
    {
      return makeNodeIdentity(getDocument());
    }
    







    public int first(int context)
    {
      return getDocumentRoot(context);
    }
    














    public int first(int context, int expandedTypeID)
    {
      if (isIndexed(expandedTypeID))
      {
        int identity = 0;
        int firstPotential = getFirstPotential(identity);
        
        return makeNodeHandle(getNextIndexed(identity, firstPotential, expandedTypeID));
      }
      
      int root = first(context);
      return next(root, root, expandedTypeID);
    }
  }
  

  private class DescendantFromRootTraverser
    extends DTMDefaultBaseTraversers.DescendantTraverser
  {
    private DescendantFromRootTraverser() { super(null); } DescendantFromRootTraverser(DTMDefaultBaseTraversers.1 x1) { this(); }
    









    protected int getFirstPotential(int identity)
    {
      return _firstch(0);
    }
    





    protected int getSubtreeRoot(int handle)
    {
      return 0;
    }
    







    public int first(int context)
    {
      return makeNodeHandle(_firstch(0));
    }
    














    public int first(int context, int expandedTypeID)
    {
      if (isIndexed(expandedTypeID))
      {
        int identity = 0;
        int firstPotential = getFirstPotential(identity);
        
        return makeNodeHandle(getNextIndexed(identity, firstPotential, expandedTypeID));
      }
      
      int root = getDocumentRoot(context);
      return next(root, root, expandedTypeID);
    }
  }
}
