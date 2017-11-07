package org.apache.xml.dtm.ref;

import javax.xml.transform.Source;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMAxisTraverser;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.XMLStringFactory;







































public abstract class DTMDefaultBaseIterators
  extends DTMDefaultBaseTraversers
{
  public DTMDefaultBaseIterators(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
  {
    super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
  }
  























  public DTMDefaultBaseIterators(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable)
  {
    super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
  }
  













  public DTMAxisIterator getTypedAxisIterator(int axis, int type)
  {
    DTMAxisIterator iterator = null;
    














    switch (axis)
    {
    case 13: 
      iterator = new TypedSingletonIterator(type);
      break;
    case 3: 
      iterator = new TypedChildrenIterator(type);
      break;
    case 10: 
      return new ParentIterator().setNodeType(type);
    case 0: 
      return new TypedAncestorIterator(type);
    case 1: 
      return new TypedAncestorIterator(type).includeSelf();
    case 2: 
      return new TypedAttributeIterator(type);
    case 4: 
      iterator = new TypedDescendantIterator(type);
      break;
    case 5: 
      iterator = new TypedDescendantIterator(type).includeSelf();
      break;
    case 6: 
      iterator = new TypedFollowingIterator(type);
      break;
    case 11: 
      iterator = new TypedPrecedingIterator(type);
      break;
    case 7: 
      iterator = new TypedFollowingSiblingIterator(type);
      break;
    case 12: 
      iterator = new TypedPrecedingSiblingIterator(type);
      break;
    case 9: 
      iterator = new TypedNamespaceIterator(type);
      break;
    case 19: 
      iterator = new TypedRootIterator(type);
      break;
    case 8: case 14: case 15: case 16: case 17: case 18: default: 
      throw new DTMException(XMLMessages.createXMLMessage("ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED", new Object[] { Axis.getNames(axis) }));
    }
    
    




    return iterator;
  }
  











  public DTMAxisIterator getAxisIterator(int axis)
  {
    DTMAxisIterator iterator = null;
    
    switch (axis)
    {
    case 13: 
      iterator = new SingletonIterator();
      break;
    case 3: 
      iterator = new ChildrenIterator();
      break;
    case 10: 
      return new ParentIterator();
    case 0: 
      return new AncestorIterator();
    case 1: 
      return new AncestorIterator().includeSelf();
    case 2: 
      return new AttributeIterator();
    case 4: 
      iterator = new DescendantIterator();
      break;
    case 5: 
      iterator = new DescendantIterator().includeSelf();
      break;
    case 6: 
      iterator = new FollowingIterator();
      break;
    case 11: 
      iterator = new PrecedingIterator();
      break;
    case 7: 
      iterator = new FollowingSiblingIterator();
      break;
    case 12: 
      iterator = new PrecedingSiblingIterator();
      break;
    case 9: 
      iterator = new NamespaceIterator();
      break;
    case 19: 
      iterator = new RootIterator();
      break;
    case 8: case 14: case 15: case 16: case 17: case 18: default: 
      throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_AXIS_NOT_IMPLEMENTED", new Object[] { Axis.getNames(axis) }));
    }
    
    



    return iterator;
  }
  








  public abstract class InternalAxisIteratorBase
    extends DTMAxisIteratorBase
  {
    protected int _currentNode;
    








    public InternalAxisIteratorBase() {}
    








    public void setMark()
    {
      _markedNode = _currentNode;
    }
    





    public void gotoMark()
    {
      _currentNode = _markedNode;
    }
  }
  
  public final class ChildrenIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    public ChildrenIterator()
    {
      super();
    }
    













    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        _startNode = node;
        _currentNode = (node == -1 ? -1 : _firstch(makeNodeIdentity(node)));
        

        return resetPosition();
      }
      
      return this;
    }
    






    public int next()
    {
      if (_currentNode != -1) {
        int node = _currentNode;
        _currentNode = _nextsib(node);
        return returnNode(makeNodeHandle(node));
      }
      
      return -1;
    }
  }
  
  public final class ParentIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    public ParentIterator()
    {
      super();
    }
    

    private int _nodeType = -1;
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        _startNode = node;
        _currentNode = getParent(node);
        
        return resetPosition();
      }
      
      return this;
    }
    











    public DTMAxisIterator setNodeType(int type)
    {
      _nodeType = type;
      
      return this;
    }
    






    public int next()
    {
      int result = _currentNode;
      
      if (_nodeType >= 14) {
        if (_nodeType != getExpandedTypeID(_currentNode)) {
          result = -1;
        }
      } else if ((_nodeType != -1) && 
        (_nodeType != getNodeType(_currentNode))) {
        result = -1;
      }
      

      _currentNode = -1;
      
      return returnNode(result);
    }
  }
  






  public final class TypedChildrenIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    private final int _nodeType;
    






    public TypedChildrenIterator(int nodeType)
    {
      super();
      _nodeType = nodeType;
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        _startNode = node;
        _currentNode = (node == -1 ? -1 : _firstch(makeNodeIdentity(_startNode)));
        


        return resetPosition();
      }
      
      return this;
    }
    






    public int next()
    {
      int node = _currentNode;
      
      int nodeType = _nodeType;
      
      if (nodeType >= 14) {
        while ((node != -1) && (_exptype(node) != nodeType)) {
          node = _nextsib(node);
        }
      }
      while (node != -1) {
        int eType = _exptype(node);
        if (eType < 14 ? 
          eType == nodeType : 
          

          m_expandedNameTable.getType(eType) == nodeType) {
          break;
        }
        node = _nextsib(node);
      }
      

      if (node == -1) {
        _currentNode = -1;
        return -1;
      }
      _currentNode = _nextsib(node);
      return returnNode(makeNodeHandle(node));
    }
  }
  








  public final class NamespaceChildrenIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    private final int _nsType;
    







    public NamespaceChildrenIterator(int type)
    {
      super();
      _nsType = type;
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        _startNode = node;
        _currentNode = (node == -1 ? -1 : -2);
        
        return resetPosition();
      }
      
      return this;
    }
    





    public int next()
    {
      if (_currentNode != -1) {
        for (int node = -2 == _currentNode ? _firstch(makeNodeIdentity(_startNode)) : _nextsib(_currentNode); 
            

            node != -1; 
            node = _nextsib(node)) {
          if (m_expandedNameTable.getNamespaceID(_exptype(node)) == _nsType) {
            _currentNode = node;
            
            return returnNode(node);
          }
        }
      }
      
      return -1;
    }
  }
  









  public class NamespaceIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    public NamespaceIterator()
    {
      super();
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        _startNode = node;
        _currentNode = getFirstNamespaceNode(node, true);
        
        return resetPosition();
      }
      
      return this;
    }
    






    public int next()
    {
      int node = _currentNode;
      
      if (-1 != node) {
        _currentNode = getNextNamespaceNode(_startNode, node, true);
      }
      return returnNode(node);
    }
  }
  






  public class TypedNamespaceIterator
    extends DTMDefaultBaseIterators.NamespaceIterator
  {
    private final int _nodeType;
    





    public TypedNamespaceIterator(int nodeType)
    {
      super();
      _nodeType = nodeType;
    }
    







    public int next()
    {
      for (int node = _currentNode; 
          node != -1; 
          node = getNextNamespaceNode(_startNode, node, true)) {
        if ((getExpandedTypeID(node) == _nodeType) || (getNodeType(node) == _nodeType) || (getNamespaceType(node) == _nodeType))
        {

          _currentNode = node;
          
          return returnNode(node);
        }
      }
      
      return this._currentNode = -1;
    }
  }
  









  public class RootIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    public RootIterator()
    {
      super();
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (_isRestartable)
      {
        _startNode = getDocumentRoot(node);
        _currentNode = -1;
        
        return resetPosition();
      }
      
      return this;
    }
    





    public int next()
    {
      if (_startNode == _currentNode) {
        return -1;
      }
      _currentNode = _startNode;
      
      return returnNode(_startNode);
    }
  }
  





  public class TypedRootIterator
    extends DTMDefaultBaseIterators.RootIterator
  {
    private final int _nodeType;
    





    public TypedRootIterator(int nodeType)
    {
      super();
      _nodeType = nodeType;
    }
    





    public int next()
    {
      if (_startNode == _currentNode) {
        return -1;
      }
      int nodeType = _nodeType;
      int node = _startNode;
      int expType = getExpandedTypeID(node);
      
      _currentNode = node;
      
      if (nodeType >= 14) {
        if (nodeType == expType) {
          return returnNode(node);
        }
      }
      else if (expType < 14) {
        if (expType == nodeType) {
          return returnNode(node);
        }
      }
      else if (m_expandedNameTable.getType(expType) == nodeType) {
        return returnNode(node);
      }
      


      return -1;
    }
  }
  






  public final class NamespaceAttributeIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    private final int _nsType;
    






    public NamespaceAttributeIterator(int nsType)
    {
      super();
      
      _nsType = nsType;
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        _startNode = node;
        _currentNode = getFirstNamespaceNode(node, false);
        
        return resetPosition();
      }
      
      return this;
    }
    






    public int next()
    {
      int node = _currentNode;
      
      if (-1 != node) {
        _currentNode = getNextNamespaceNode(_startNode, node, false);
      }
      return returnNode(node);
    }
  }
  
  public class FollowingSiblingIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    public FollowingSiblingIterator() {
      super();
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        _startNode = node;
        _currentNode = makeNodeIdentity(node);
        
        return resetPosition();
      }
      
      return this;
    }
    





    public int next()
    {
      _currentNode = (_currentNode == -1 ? -1 : _nextsib(_currentNode));
      
      return returnNode(makeNodeHandle(_currentNode));
    }
  }
  





  public final class TypedFollowingSiblingIterator
    extends DTMDefaultBaseIterators.FollowingSiblingIterator
  {
    private final int _nodeType;
    





    public TypedFollowingSiblingIterator(int type)
    {
      super();
      _nodeType = type;
    }
    





    public int next()
    {
      if (_currentNode == -1) {
        return -1;
      }
      
      int node = _currentNode;
      
      int nodeType = _nodeType;
      
      if (nodeType >= 14) {
        do {
          node = _nextsib(node);
          if (node == -1) break; } while (_exptype(node) != nodeType);
      } else {
        while ((node = _nextsib(node)) != -1) {
          int eType = _exptype(node);
          if (eType < 14) {
            if (eType == nodeType) {
              break;
            }
          } else if (m_expandedNameTable.getType(eType) == nodeType) {
            break;
          }
        }
      }
      
      _currentNode = node;
      
      return _currentNode == -1 ? -1 : returnNode(makeNodeHandle(_currentNode));
    }
  }
  
  public final class AttributeIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    public AttributeIterator()
    {
      super();
    }
    











    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        _startNode = node;
        _currentNode = getFirstAttributeIdentity(makeNodeIdentity(node));
        
        return resetPosition();
      }
      
      return this;
    }
    






    public int next()
    {
      int node = _currentNode;
      
      if (node != -1) {
        _currentNode = getNextAttributeIdentity(node);
        return returnNode(makeNodeHandle(node));
      }
      
      return -1;
    }
  }
  





  public final class TypedAttributeIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    private final int _nodeType;
    




    public TypedAttributeIterator(int nodeType)
    {
      super();
      _nodeType = nodeType;
    }
    










    public DTMAxisIterator setStartNode(int node)
    {
      if (_isRestartable)
      {
        _startNode = node;
        
        _currentNode = getTypedAttribute(node, _nodeType);
        
        return resetPosition();
      }
      
      return this;
    }
    






    public int next()
    {
      int node = _currentNode;
      


      _currentNode = -1;
      
      return returnNode(node);
    }
  }
  
  public class PrecedingSiblingIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
    protected int _startNodeID;
    
    public PrecedingSiblingIterator() { super(); }
    











    public boolean isReverse()
    {
      return true;
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        _startNode = node;
        node = this._startNodeID = makeNodeIdentity(node);
        
        if (node == -1)
        {
          _currentNode = node;
          return resetPosition();
        }
        
        int type = m_expandedNameTable.getType(_exptype(node));
        if ((2 == type) || (13 == type))
        {

          _currentNode = node;

        }
        else
        {
          _currentNode = _parent(node);
          if (-1 != _currentNode) {
            _currentNode = _firstch(_currentNode);
          } else {
            _currentNode = node;
          }
        }
        return resetPosition();
      }
      
      return this;
    }
    






    public int next()
    {
      if ((_currentNode == _startNodeID) || (_currentNode == -1))
      {
        return -1;
      }
      

      int node = _currentNode;
      _currentNode = _nextsib(node);
      
      return returnNode(makeNodeHandle(node));
    }
  }
  






  public final class TypedPrecedingSiblingIterator
    extends DTMDefaultBaseIterators.PrecedingSiblingIterator
  {
    private final int _nodeType;
    






    public TypedPrecedingSiblingIterator(int type)
    {
      super();
      _nodeType = type;
    }
    





    public int next()
    {
      int node = _currentNode;
      

      int nodeType = _nodeType;
      int startID = _startNodeID;
      
      if (nodeType >= 14) {
        while ((node != -1) && (node != startID) && (_exptype(node) != nodeType)) {
          node = _nextsib(node);
        }
      }
      while ((node != -1) && (node != startID)) {
        int expType = _exptype(node);
        if (expType < 14 ? 
          expType == nodeType : 
          


          m_expandedNameTable.getType(expType) == nodeType) {
          break;
        }
        
        node = _nextsib(node);
      }
      

      if ((node == -1) || (node == _startNodeID)) {
        _currentNode = -1;
        return -1;
      }
      _currentNode = _nextsib(node);
      return returnNode(makeNodeHandle(node));
    }
  }
  

  public class PrecedingIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    public PrecedingIterator()
    {
      super();
    }
    

    private final int _maxAncestors = 8;
    




    protected int[] _stack = new int[8];
    

    protected int _sp;
    
    protected int _oldsp;
    
    protected int _markedsp;
    
    protected int _markedNode;
    
    protected int _markedDescendant;
    

    public boolean isReverse()
    {
      return true;
    }
    





    public DTMAxisIterator cloneIterator()
    {
      _isRestartable = false;
      
      try
      {
        PrecedingIterator clone = (PrecedingIterator)super.clone();
        int[] stackCopy = new int[_stack.length];
        System.arraycopy(_stack, 0, stackCopy, 0, _stack.length);
        
        _stack = stackCopy;
        

        return clone;
      }
      catch (CloneNotSupportedException e)
      {
        throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", null));
      }
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        node = makeNodeIdentity(node);
        



        if (_type(node) == 2) {
          node = _parent(node);
        }
        _startNode = node; int 
          tmp59_58 = 0;int index = tmp59_58;_stack[tmp59_58] = node;
        


        int parent = node;
        while ((parent = _parent(parent)) != -1)
        {
          index++; if (index == _stack.length)
          {
            int[] stack = new int[index + 4];
            System.arraycopy(_stack, 0, stack, 0, index);
            _stack = stack;
          }
          _stack[index] = parent;
        }
        if (index > 0) {
          index--;
        }
        _currentNode = _stack[index];
        
        _oldsp = (this._sp = index);
        
        return resetPosition();
      }
      
      return this;
    }
    








    public int next()
    {
      for (_currentNode += 1; 
          _sp >= 0; 
          _currentNode += 1)
      {
        if (_currentNode < _stack[_sp])
        {
          if ((_type(_currentNode) != 2) && (_type(_currentNode) != 13))
          {
            return returnNode(makeNodeHandle(_currentNode));
          }
        } else
          _sp -= 1;
      }
      return -1;
    }
    









    public DTMAxisIterator reset()
    {
      _sp = _oldsp;
      
      return resetPosition();
    }
    
    public void setMark() {
      _markedsp = _sp;
      _markedNode = _currentNode;
      _markedDescendant = _stack[0];
    }
    
    public void gotoMark() {
      _sp = _markedsp;
      _currentNode = _markedNode;
    }
  }
  






  public final class TypedPrecedingIterator
    extends DTMDefaultBaseIterators.PrecedingIterator
  {
    private final int _nodeType;
    





    public TypedPrecedingIterator(int type)
    {
      super();
      _nodeType = type;
    }
    





    public int next()
    {
      int node = _currentNode;
      int nodeType = _nodeType;
      
      if (nodeType >= 14) {
        do {
          do { node += 1;
            
            if (_sp < 0) {
              node = -1;
              break label168; }
            if (node < _stack[_sp]) break;
          } while (--_sp >= 0);
          node = -1;
          break;
        }
        while (_exptype(node) != nodeType);

      }
      else
      {
        for (;;)
        {

          node += 1;
          
          if (_sp < 0) {
            node = -1;
          }
          else if (node >= _stack[_sp]) {
            if (--_sp < 0) {
              node = -1;
            }
          }
          else {
            int expType = _exptype(node);
            if (expType < 14) {
              if (expType == nodeType) {
                break;
              }
            }
            else if (m_expandedNameTable.getType(expType) == nodeType) {
              break;
            }
          }
        }
      }
      
      label168:
      _currentNode = node;
      
      return node == -1 ? -1 : returnNode(makeNodeHandle(node));
    }
  }
  

  public class FollowingIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    DTMAxisTraverser m_traverser;
    
    public FollowingIterator()
    {
      super();
      m_traverser = getAxisTraverser(6);
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        _startNode = node;
        





        _currentNode = m_traverser.first(node);
        

        return resetPosition();
      }
      
      return this;
    }
    






    public int next()
    {
      int node = _currentNode;
      
      _currentNode = m_traverser.next(_startNode, _currentNode);
      
      return returnNode(node);
    }
  }
  





  public final class TypedFollowingIterator
    extends DTMDefaultBaseIterators.FollowingIterator
  {
    private final int _nodeType;
    




    public TypedFollowingIterator(int type)
    {
      super();
      _nodeType = type;
    }
    



    public int next()
    {
      int node;
      


      do
      {
        node = _currentNode;
        
        _currentNode = m_traverser.next(_startNode, _currentNode);


      }
      while ((node != -1) && (getExpandedTypeID(node) != _nodeType) && (getNodeType(node) != _nodeType));
      
      return node == -1 ? -1 : returnNode(node);
    }
  }
  

  public class AncestorIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    public AncestorIterator() { super(); }
    
    NodeVector m_ancestors = new NodeVector();
    


    int m_ancestorsPos;
    


    int m_markedPos;
    


    int m_realStartNode;
    


    public int getStartNode()
    {
      return m_realStartNode;
    }
    





    public final boolean isReverse()
    {
      return true;
    }
    





    public DTMAxisIterator cloneIterator()
    {
      _isRestartable = false;
      
      try
      {
        AncestorIterator clone = (AncestorIterator)super.clone();
        
        _startNode = _startNode;
        

        return clone;
      }
      catch (CloneNotSupportedException e)
      {
        throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", null));
      }
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      m_realStartNode = node;
      
      if (_isRestartable)
      {
        int nodeID = makeNodeIdentity(node);
        
        if ((!_includeSelf) && (node != -1)) {
          nodeID = _parent(nodeID);
          node = makeNodeHandle(nodeID);
        }
        
        _startNode = node;
        
        while (nodeID != -1) {
          m_ancestors.addElement(node);
          nodeID = _parent(nodeID);
          node = makeNodeHandle(nodeID);
        }
        m_ancestorsPos = (m_ancestors.size() - 1);
        
        _currentNode = (m_ancestorsPos >= 0 ? m_ancestors.elementAt(m_ancestorsPos) : -1);
        


        return resetPosition();
      }
      
      return this;
    }
    







    public DTMAxisIterator reset()
    {
      m_ancestorsPos = (m_ancestors.size() - 1);
      
      _currentNode = (m_ancestorsPos >= 0 ? m_ancestors.elementAt(m_ancestorsPos) : -1);
      

      return resetPosition();
    }
    






    public int next()
    {
      int next = _currentNode;
      
      int pos = --m_ancestorsPos;
      
      _currentNode = (pos >= 0 ? m_ancestors.elementAt(m_ancestorsPos) : -1);
      

      return returnNode(next);
    }
    
    public void setMark() {
      m_markedPos = m_ancestorsPos;
    }
    
    public void gotoMark() {
      m_ancestorsPos = m_markedPos;
      _currentNode = (m_ancestorsPos >= 0 ? m_ancestors.elementAt(m_ancestorsPos) : -1);
    }
  }
  





  public final class TypedAncestorIterator
    extends DTMDefaultBaseIterators.AncestorIterator
  {
    private final int _nodeType;
    





    public TypedAncestorIterator(int type)
    {
      super();
      _nodeType = type;
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      m_realStartNode = node;
      
      if (_isRestartable)
      {
        int nodeID = makeNodeIdentity(node);
        int nodeType = _nodeType;
        
        if ((!_includeSelf) && (node != -1)) {
          nodeID = _parent(nodeID);
        }
        
        _startNode = node;
        
        if (nodeType >= 14) {
          while (nodeID != -1) {
            int eType = _exptype(nodeID);
            
            if (eType == nodeType) {
              m_ancestors.addElement(makeNodeHandle(nodeID));
            }
            nodeID = _parent(nodeID);
          }
        }
        while (nodeID != -1) {
          int eType = _exptype(nodeID);
          
          if (((eType >= 14) && (m_expandedNameTable.getType(eType) == nodeType)) || ((eType < 14) && (eType == nodeType)))
          {

            m_ancestors.addElement(makeNodeHandle(nodeID));
          }
          nodeID = _parent(nodeID);
        }
        
        m_ancestorsPos = (m_ancestors.size() - 1);
        
        _currentNode = (m_ancestorsPos >= 0 ? m_ancestors.elementAt(m_ancestorsPos) : -1);
        


        return resetPosition();
      }
      
      return this;
    }
  }
  
  public class DescendantIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    public DescendantIterator() {
      super();
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isRestartable)
      {
        node = makeNodeIdentity(node);
        _startNode = node;
        
        if (_includeSelf) {
          node--;
        }
        _currentNode = node;
        
        return resetPosition();
      }
      
      return this;
    }
    














    protected boolean isDescendant(int identity)
    {
      return (_parent(identity) >= _startNode) || (_startNode == identity);
    }
    





    public int next()
    {
      if (_startNode == -1) {
        return -1;
      }
      
      if ((_includeSelf) && (_currentNode + 1 == _startNode)) {
        return returnNode(makeNodeHandle(++_currentNode));
      }
      int node = _currentNode;
      int type;
      do
      {
        node++;
        type = _type(node);
        
        if ((-1 == type) || (!isDescendant(node))) {
          _currentNode = -1;
          return -1;
        }
        
      } while ((2 == type) || (3 == type) || (13 == type));
      
      _currentNode = node;
      return returnNode(makeNodeHandle(node));
    }
    





    public DTMAxisIterator reset()
    {
      boolean temp = _isRestartable;
      
      _isRestartable = true;
      
      setStartNode(makeNodeHandle(_startNode));
      
      _isRestartable = temp;
      
      return this;
    }
  }
  





  public final class TypedDescendantIterator
    extends DTMDefaultBaseIterators.DescendantIterator
  {
    private final int _nodeType;
    





    public TypedDescendantIterator(int nodeType)
    {
      super();
      _nodeType = nodeType;
    }
    








    public int next()
    {
      if (_startNode == -1) {
        return -1;
      }
      
      int node = _currentNode;
      int type;
      do
      {
        node++;
        type = _type(node);
        
        if ((-1 == type) || (!isDescendant(node))) {
          _currentNode = -1;
          return -1;
        }
        
      } while ((type != _nodeType) && (_exptype(node) != _nodeType));
      
      _currentNode = node;
      return returnNode(makeNodeHandle(node));
    }
  }
  





  public class NthDescendantIterator
    extends DTMDefaultBaseIterators.DescendantIterator
  {
    int _pos;
    





    public NthDescendantIterator(int pos)
    {
      super();
      _pos = pos;
    }
    




    public int next()
    {
      int node;
      



      while ((node = super.next()) != -1)
      {
        node = makeNodeIdentity(node);
        
        int parent = _parent(node);
        int child = _firstch(parent);
        int pos = 0;
        
        do
        {
          int type = _type(child);
          
          if (1 == type) {
            pos++;
          }
        } while ((pos < _pos) && ((child = _nextsib(child)) != -1));
        
        if (node == child) {
          return node;
        }
      }
      return -1;
    }
  }
  




  public class SingletonIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    private boolean _isConstant;
    




    public SingletonIterator()
    {
      this(Integer.MIN_VALUE, false);
    }
    






    public SingletonIterator(int node)
    {
      this(node, false);
    }
    






    public SingletonIterator(int node, boolean constant)
    {
      super();
      _currentNode = (this._startNode = node);
      _isConstant = constant;
    }
    









    public DTMAxisIterator setStartNode(int node)
    {
      if (node == 0)
        node = getDocument();
      if (_isConstant)
      {
        _currentNode = _startNode;
        
        return resetPosition();
      }
      if (_isRestartable)
      {
        _currentNode = (this._startNode = node);
        
        return resetPosition();
      }
      
      return this;
    }
    







    public DTMAxisIterator reset()
    {
      if (_isConstant)
      {
        _currentNode = _startNode;
        
        return resetPosition();
      }
      

      boolean temp = _isRestartable;
      
      _isRestartable = true;
      
      setStartNode(_startNode);
      
      _isRestartable = temp;
      

      return this;
    }
    






    public int next()
    {
      int result = _currentNode;
      
      _currentNode = -1;
      
      return returnNode(result);
    }
  }
  





  public final class TypedSingletonIterator
    extends DTMDefaultBaseIterators.SingletonIterator
  {
    private final int _nodeType;
    




    public TypedSingletonIterator(int nodeType)
    {
      super();
      _nodeType = nodeType;
    }
    







    public int next()
    {
      int result = _currentNode;
      int nodeType = _nodeType;
      
      _currentNode = -1;
      
      if (nodeType >= 14) {
        if (getExpandedTypeID(result) == nodeType) {
          return returnNode(result);
        }
      }
      else if (getNodeType(result) == nodeType) {
        return returnNode(result);
      }
      

      return -1;
    }
  }
}
