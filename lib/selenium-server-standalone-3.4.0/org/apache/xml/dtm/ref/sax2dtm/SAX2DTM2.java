package org.apache.xml.dtm.ref.sax2dtm;

import java.util.Vector;
import javax.xml.transform.Source;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMException;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators.RootIterator;
import org.apache.xml.dtm.ref.DTMDefaultBaseIterators.SingletonIterator;
import org.apache.xml.dtm.ref.DTMStringPool;
import org.apache.xml.dtm.ref.ExpandedNameTable;
import org.apache.xml.dtm.ref.ExtendedType;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.SuballocatedIntVector;
import org.apache.xml.utils.XMLString;
import org.apache.xml.utils.XMLStringDefault;
import org.apache.xml.utils.XMLStringFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

























public class SAX2DTM2
  extends SAX2DTM
{
  private int[] m_exptype_map0;
  private int[] m_nextsib_map0;
  private int[] m_firstch_map0;
  private int[] m_parent_map0;
  private int[][] m_exptype_map;
  private int[][] m_nextsib_map;
  private int[][] m_firstch_map;
  private int[][] m_parent_map;
  protected ExtendedType[] m_extendedTypes;
  protected Vector m_values;
  
  public final class ChildrenIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
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
        _currentNode = (node == -1 ? -1 : _firstch2(makeNodeIdentity(node)));
        

        return resetPosition();
      }
      
      return this;
    }
    






    public int next()
    {
      if (_currentNode != -1) {
        int node = _currentNode;
        _currentNode = _nextsib2(node);
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
        
        if (node != -1) {
          _currentNode = _parent2(makeNodeIdentity(node));
        } else {
          _currentNode = -1;
        }
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
      if (result == -1) {
        return -1;
      }
      
      if (_nodeType == -1) {
        _currentNode = -1;
        return returnNode(makeNodeHandle(result));
      }
      if (_nodeType >= 14) {
        if (_nodeType == _exptype2(result)) {
          _currentNode = -1;
          return returnNode(makeNodeHandle(result));
        }
        
      }
      else if (_nodeType == _type2(result)) {
        _currentNode = -1;
        return returnNode(makeNodeHandle(result));
      }
      

      return -1;
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
        _currentNode = (node == -1 ? -1 : _firstch2(makeNodeIdentity(_startNode)));
        


        return resetPosition();
      }
      
      return this;
    }
    





    public int next()
    {
      int node = _currentNode;
      if (node == -1) {
        return -1;
      }
      int nodeType = _nodeType;
      
      if (nodeType != 1) {
        while ((node != -1) && (_exptype2(node) != nodeType)) {
          node = _nextsib2(node);
        }
      }
      






      while (node != -1) {
        int eType = _exptype2(node);
        if (eType >= 14) {
          break;
        }
        node = _nextsib2(node);
      }
      

      if (node == -1) {
        _currentNode = -1;
        return -1;
      }
      _currentNode = _nextsib2(node);
      return returnNode(makeNodeHandle(node));
    }
    





    public int getNodeByPosition(int position)
    {
      if (position <= 0) {
        return -1;
      }
      int node = _currentNode;
      int pos = 0;
      
      int nodeType = _nodeType;
      if (nodeType != 1) {
        while (node != -1) {
          if (_exptype2(node) == nodeType) {
            pos++;
            if (pos == position) {
              return makeNodeHandle(node);
            }
          }
          node = _nextsib2(node);
        }
        return -1;
      }
      
      while (node != -1) {
        if (_exptype2(node) >= 14) {
          pos++;
          if (pos == position)
            return makeNodeHandle(node);
        }
        node = _nextsib2(node);
      }
      return -1;
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
      int node = _startNode;
      int expType = _exptype2(makeNodeIdentity(node));
      
      _currentNode = node;
      
      if (_nodeType >= 14) {
        if (_nodeType == expType) {
          return returnNode(node);
        }
        
      }
      else if (expType < 14) {
        if (expType == _nodeType) {
          return returnNode(node);
        }
        
      }
      else if (m_extendedTypes[expType].getNodeType() == _nodeType) {
        return returnNode(node);
      }
      


      return -1;
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
      _currentNode = (_currentNode == -1 ? -1 : _nextsib2(_currentNode));
      
      return returnNode(makeNodeHandle(_currentNode));
    }
  }
  





  public final class TypedFollowingSiblingIterator
    extends SAX2DTM2.FollowingSiblingIterator
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
      
      if (nodeType != 1) {
        while (((node = _nextsib2(node)) != -1) && (_exptype2(node) != nodeType)) {}
      }
      
      while (((node = _nextsib2(node)) != -1) && (_exptype2(node) < 14)) {}
      

      _currentNode = node;
      
      return node == -1 ? -1 : returnNode(makeNodeHandle(node));
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
        
        int type = _type2(node);
        if ((2 == type) || (13 == type))
        {

          _currentNode = node;

        }
        else
        {
          _currentNode = _parent2(node);
          if (-1 != _currentNode) {
            _currentNode = _firstch2(_currentNode);
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
      _currentNode = _nextsib2(node);
      
      return returnNode(makeNodeHandle(node));
    }
  }
  






  public final class TypedPrecedingSiblingIterator
    extends SAX2DTM2.PrecedingSiblingIterator
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
      int startNodeID = _startNodeID;
      
      if (nodeType != 1) {
        while ((node != -1) && (node != startNodeID) && (_exptype2(node) != nodeType)) {
          node = _nextsib2(node);
        }
      }
      
      while ((node != -1) && (node != startNodeID) && (_exptype2(node) < 14)) {
        node = _nextsib2(node);
      }
      

      if ((node == -1) || (node == startNodeID)) {
        _currentNode = -1;
        return -1;
      }
      
      _currentNode = _nextsib2(node);
      return returnNode(makeNodeHandle(node));
    }
    




    public int getLast()
    {
      if (_last != -1) {
        return _last;
      }
      setMark();
      
      int node = _currentNode;
      int nodeType = _nodeType;
      int startNodeID = _startNodeID;
      
      int last = 0;
      if (nodeType != 1) {
        while ((node != -1) && (node != startNodeID)) {
          if (_exptype2(node) == nodeType) {
            last++;
          }
          node = _nextsib2(node);
        }
      }
      
      while ((node != -1) && (node != startNodeID)) {
        if (_exptype2(node) >= 14) {
          last++;
        }
        node = _nextsib2(node);
      }
      

      gotoMark();
      
      return this._last = last;
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
        



        if (_type2(node) == 2) {
          node = _parent2(node);
        }
        _startNode = node; int 
          tmp59_58 = 0;int index = tmp59_58;_stack[tmp59_58] = node;
        
        int parent = node;
        while ((parent = _parent2(parent)) != -1)
        {
          index++; if (index == _stack.length)
          {
            int[] stack = new int[index * 2];
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
      for (_currentNode += 1; _sp >= 0; _currentNode += 1)
      {
        if (_currentNode < _stack[_sp])
        {
          int type = _type2(_currentNode);
          if ((type != 2) && (type != 13)) {
            return returnNode(makeNodeHandle(_currentNode));
          }
        } else {
          _sp -= 1;
        } }
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
    extends SAX2DTM2.PrecedingIterator
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
          do { node++;
            
            if (_sp < 0) {
              node = -1;
              break label167;
            }
            if (node < _stack[_sp]) break;
          } while (--_sp >= 0);
          node = -1;
          break;

        }
        while (_exptype2(node) != nodeType);

      }
      else
      {

        for (;;)
        {

          node++;
          
          if (_sp < 0) {
            node = -1;

          }
          else if (node >= _stack[_sp]) {
            if (--_sp < 0) {
              node = -1;
            }
          }
          else
          {
            int expType = _exptype2(node);
            if (expType < 14) {
              if (expType == nodeType) {
                break;
              }
              
            }
            else if (m_extendedTypes[expType].getNodeType() == nodeType) {
              break;
            }
          }
        }
      }
      
      label167:
      _currentNode = node;
      
      return node == -1 ? -1 : returnNode(makeNodeHandle(node));
    }
  }
  



  public class FollowingIterator
    extends DTMDefaultBaseIterators.InternalAxisIteratorBase
  {
    public FollowingIterator()
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
        


        node = makeNodeIdentity(node);
        

        int type = _type2(node);
        
        if ((2 == type) || (13 == type))
        {
          node = _parent2(node);
          int first = _firstch2(node);
          
          if (-1 != first) {
            _currentNode = makeNodeHandle(first);
            return resetPosition();
          }
        }
        int first;
        do
        {
          first = _nextsib2(node);
          
          if (-1 == first) {
            node = _parent2(node);
          }
        } while ((-1 == first) && (-1 != node));
        
        _currentNode = makeNodeHandle(first);
        

        return resetPosition();
      }
      
      return this;
    }
    






    public int next()
    {
      int node = _currentNode;
      

      int current = makeNodeIdentity(node);
      int type;
      do
      {
        current++;
        
        type = _type2(current);
        if (-1 == type) {
          _currentNode = -1;
          return returnNode(node);
        }
        
      } while ((2 == type) || (13 == type));
      

      _currentNode = makeNodeHandle(current);
      return returnNode(node);
    }
  }
  






  public final class TypedFollowingIterator
    extends SAX2DTM2.FollowingIterator
  {
    private final int _nodeType;
    





    public TypedFollowingIterator(int type)
    {
      super();
      _nodeType = type;
    }
    









    public int next()
    {
      int nodeType = _nodeType;
      int currentNodeID = makeNodeIdentity(_currentNode);
      int node;
      if (nodeType >= 14) {
        int node;
        do { node = currentNodeID;
          int current = node;
          int type;
          do {
            current++;
            type = _type2(current);
          }
          while ((type != -1) && ((2 == type) || (13 == type)));
          
          currentNodeID = type != -1 ? current : -1;
          
          if (node == -1) break; } while (_exptype2(node) != nodeType);
      }
      else {
        do {
          node = currentNodeID;
          int current = node;
          int type;
          do {
            current++;
            type = _type2(current);
          }
          while ((type != -1) && ((2 == type) || (13 == type)));
          
          currentNodeID = type != -1 ? current : -1;

        }
        while ((node != -1) && (_exptype2(node) != nodeType) && (_type2(node) != nodeType));
      }
      
      _currentNode = makeNodeHandle(currentNodeID);
      return node == -1 ? -1 : returnNode(makeNodeHandle(node));
    }
  }
  
  public class AncestorIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase {
    private static final int m_blocksize = 32;
    
    public AncestorIterator() {
      super();
    }
    



    int[] m_ancestors = new int[32];
    

    int m_size = 0;
    


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
        m_size = 0;
        
        if (nodeID == -1) {
          _currentNode = -1;
          m_ancestorsPos = 0;
          return this;
        }
        


        if (!_includeSelf) {
          nodeID = _parent2(nodeID);
          node = makeNodeHandle(nodeID);
        }
        
        _startNode = node;
        
        while (nodeID != -1)
        {
          if (m_size >= m_ancestors.length)
          {
            int[] newAncestors = new int[m_size * 2];
            System.arraycopy(m_ancestors, 0, newAncestors, 0, m_ancestors.length);
            m_ancestors = newAncestors;
          }
          
          m_ancestors[(m_size++)] = node;
          nodeID = _parent2(nodeID);
          node = makeNodeHandle(nodeID);
        }
        
        m_ancestorsPos = (m_size - 1);
        
        _currentNode = (m_ancestorsPos >= 0 ? m_ancestors[m_ancestorsPos] : -1);
        


        return resetPosition();
      }
      
      return this;
    }
    







    public DTMAxisIterator reset()
    {
      m_ancestorsPos = (m_size - 1);
      
      _currentNode = (m_ancestorsPos >= 0 ? m_ancestors[m_ancestorsPos] : -1);
      

      return resetPosition();
    }
    






    public int next()
    {
      int next = _currentNode;
      
      int pos = --m_ancestorsPos;
      
      _currentNode = (pos >= 0 ? m_ancestors[m_ancestorsPos] : -1);
      

      return returnNode(next);
    }
    
    public void setMark() {
      m_markedPos = m_ancestorsPos;
    }
    
    public void gotoMark() {
      m_ancestorsPos = m_markedPos;
      _currentNode = (m_ancestorsPos >= 0 ? m_ancestors[m_ancestorsPos] : -1);
    }
  }
  





  public final class TypedAncestorIterator
    extends SAX2DTM2.AncestorIterator
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
        m_size = 0;
        
        if (nodeID == -1) {
          _currentNode = -1;
          m_ancestorsPos = 0;
          return this;
        }
        
        int nodeType = _nodeType;
        
        if (!_includeSelf) {
          nodeID = _parent2(nodeID);
          node = makeNodeHandle(nodeID);
        }
        
        _startNode = node;
        
        if (nodeType >= 14) {
          while (nodeID != -1) {
            int eType = _exptype2(nodeID);
            
            if (eType == nodeType) {
              if (m_size >= m_ancestors.length)
              {
                int[] newAncestors = new int[m_size * 2];
                System.arraycopy(m_ancestors, 0, newAncestors, 0, m_ancestors.length);
                m_ancestors = newAncestors;
              }
              m_ancestors[(m_size++)] = makeNodeHandle(nodeID);
            }
            nodeID = _parent2(nodeID);
          }
        }
        
        while (nodeID != -1) {
          int eType = _exptype2(nodeID);
          
          if (((eType < 14) && (eType == nodeType)) || ((eType >= 14) && (m_extendedTypes[eType].getNodeType() == nodeType)))
          {

            if (m_size >= m_ancestors.length)
            {
              int[] newAncestors = new int[m_size * 2];
              System.arraycopy(m_ancestors, 0, newAncestors, 0, m_ancestors.length);
              m_ancestors = newAncestors;
            }
            m_ancestors[(m_size++)] = makeNodeHandle(nodeID);
          }
          nodeID = _parent2(nodeID);
        }
        
        m_ancestorsPos = (m_size - 1);
        
        _currentNode = (m_ancestorsPos >= 0 ? m_ancestors[m_ancestorsPos] : -1);
        


        return resetPosition();
      }
      
      return this;
    }
    



    public int getNodeByPosition(int position)
    {
      if ((position > 0) && (position <= m_size)) {
        return m_ancestors[(position - 1)];
      }
      
      return -1;
    }
    



    public int getLast()
    {
      return m_size;
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
    














    protected final boolean isDescendant(int identity)
    {
      return (_parent2(identity) >= _startNode) || (_startNode == identity);
    }
    





    public int next()
    {
      int startNode = _startNode;
      if (startNode == -1) {
        return -1;
      }
      
      if ((_includeSelf) && (_currentNode + 1 == startNode)) {
        return returnNode(makeNodeHandle(++_currentNode));
      }
      int node = _currentNode;
      



      if (startNode == 0) {
        int eType;
        int type;
        do { node++;
          eType = _exptype2(node);
          
          if (-1 == eType) {
            _currentNode = -1;
            return -1;
          }
          
        }
        while ((eType == 3) || ((type = m_extendedTypes[eType].getNodeType()) == 2) || (type == 13));
      } else {
        int type;
        do {
          node++;
          type = _type2(node);
          
          if ((-1 == type) || (!isDescendant(node))) {
            _currentNode = -1;
            return -1;
          }
          
        } while ((2 == type) || (3 == type) || (13 == type));
      }
      
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
    extends SAX2DTM2.DescendantIterator
  {
    private final int _nodeType;
    





    public TypedDescendantIterator(int nodeType)
    {
      super();
      _nodeType = nodeType;
    }
    





    public int next()
    {
      int startNode = _startNode;
      if (_startNode == -1) {
        return -1;
      }
      
      int node = _currentNode;
      

      int nodeType = _nodeType;
      
      if (nodeType != 1)
      {
        int expType;
        do {
          node++;
          expType = _exptype2(node);
          
          if ((-1 == expType) || ((_parent2(node) < startNode) && (startNode != node))) {
            _currentNode = -1;
            return -1;
          }
          
        } while (expType != nodeType);



      }
      else if (startNode == 0)
      {
        int expType;
        do {
          node++;
          expType = _exptype2(node);
          
          if (-1 == expType) {
            _currentNode = -1;
            return -1;
          }
          
        } while ((expType < 14) || (m_extendedTypes[expType].getNodeType() != 1));
      }
      else
      {
        int expType;
        do {
          node++;
          expType = _exptype2(node);
          
          if ((-1 == expType) || ((_parent2(node) < startNode) && (startNode != node))) {
            _currentNode = -1;
            return -1;
          }
          
        }
        while ((expType < 14) || (m_extendedTypes[expType].getNodeType() != 1));
      }
      
      _currentNode = node;
      return returnNode(makeNodeHandle(node));
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
      if (result == -1) {
        return -1;
      }
      _currentNode = -1;
      
      if (_nodeType >= 14) {
        if (_exptype2(makeNodeIdentity(result)) == _nodeType) {
          return returnNode(result);
        }
        
      }
      else if (_type2(makeNodeIdentity(result)) == _nodeType) {
        return returnNode(result);
      }
      

      return -1;
    }
  }
  


































  private int m_valueIndex = 0;
  


  private int m_maxNodeIndex;
  


  protected int m_SHIFT;
  

  protected int m_MASK;
  

  protected int m_blocksize;
  

  protected static final int TEXT_LENGTH_BITS = 10;
  

  protected static final int TEXT_OFFSET_BITS = 21;
  

  protected static final int TEXT_LENGTH_MAX = 1023;
  

  protected static final int TEXT_OFFSET_MAX = 2097151;
  

  protected boolean m_buildIdIndex = true;
  

  private static final String EMPTY_STR = "";
  

  private static final XMLString EMPTY_XML_STR = new XMLStringDefault("");
  







  public SAX2DTM2(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
  {
    this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, true, false);
  }
  












  public SAX2DTM2(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean buildIdIndex, boolean newNameTable)
  {
    super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
    



    for (int shift = 0; blocksize >>>= 1 != 0; shift++) {}
    
    m_blocksize = (1 << shift);
    m_SHIFT = shift;
    m_MASK = (m_blocksize - 1);
    
    m_buildIdIndex = buildIdIndex;
    



    m_values = new Vector(32, 512);
    
    m_maxNodeIndex = 65536;
    

    m_exptype_map0 = m_exptype.getMap0();
    m_nextsib_map0 = m_nextsib.getMap0();
    m_firstch_map0 = m_firstch.getMap0();
    m_parent_map0 = m_parent.getMap0();
  }
  






  public final int _exptype(int identity)
  {
    return m_exptype.elementAt(identity);
  }
  


















  public final int _exptype2(int identity)
  {
    if (identity < m_blocksize) {
      return m_exptype_map0[identity];
    }
    return m_exptype_map[(identity >>> m_SHIFT)][(identity & m_MASK)];
  }
  








  public final int _nextsib2(int identity)
  {
    if (identity < m_blocksize) {
      return m_nextsib_map0[identity];
    }
    return m_nextsib_map[(identity >>> m_SHIFT)][(identity & m_MASK)];
  }
  








  public final int _firstch2(int identity)
  {
    if (identity < m_blocksize) {
      return m_firstch_map0[identity];
    }
    return m_firstch_map[(identity >>> m_SHIFT)][(identity & m_MASK)];
  }
  








  public final int _parent2(int identity)
  {
    if (identity < m_blocksize) {
      return m_parent_map0[identity];
    }
    return m_parent_map[(identity >>> m_SHIFT)][(identity & m_MASK)];
  }
  


  public final int _type2(int identity)
  {
    int eType;
    

    int eType;
    

    if (identity < m_blocksize) {
      eType = m_exptype_map0[identity];
    } else {
      eType = m_exptype_map[(identity >>> m_SHIFT)][(identity & m_MASK)];
    }
    if (-1 != eType) {
      return m_extendedTypes[eType].getNodeType();
    }
    return -1;
  }
  






  public final int getExpandedTypeID2(int nodeHandle)
  {
    int nodeID = makeNodeIdentity(nodeHandle);
    


    if (nodeID != -1) {
      if (nodeID < m_blocksize) {
        return m_exptype_map0[nodeID];
      }
      return m_exptype_map[(nodeID >>> m_SHIFT)][(nodeID & m_MASK)];
    }
    
    return -1;
  }
  








  public final int _exptype2Type(int exptype)
  {
    if (-1 != exptype) {
      return m_extendedTypes[exptype].getNodeType();
    }
    return -1;
  }
  








  public int getIdForNamespace(String uri)
  {
    int index = m_values.indexOf(uri);
    if (index < 0)
    {
      m_values.addElement(uri);
      return m_valueIndex++;
    }
    
    return index;
  }
  
























  public void startElement(String uri, String localName, String qName, Attributes attributes)
    throws SAXException
  {
    charactersFlush();
    
    int exName = m_expandedNameTable.getExpandedTypeID(uri, localName, 1);
    
    int prefixIndex = qName.length() != localName.length() ? m_valuesOrPrefixes.stringToIndex(qName) : 0;
    

    int elemNode = addNode(1, exName, m_parents.peek(), m_previous, prefixIndex, true);
    

    if (m_indexing) {
      indexNode(exName, elemNode);
    }
    m_parents.push(elemNode);
    
    int startDecls = m_contextIndexes.peek();
    int nDecls = m_prefixMappings.size();
    

    if (!m_pastFirstElement)
    {

      String prefix = "xml";
      String declURL = "http://www.w3.org/XML/1998/namespace";
      exName = m_expandedNameTable.getExpandedTypeID(null, prefix, 13);
      m_values.addElement(declURL);
      int val = m_valueIndex++;
      addNode(13, exName, elemNode, -1, val, false);
      
      m_pastFirstElement = true;
    }
    
    for (int i = startDecls; i < nDecls; i += 2)
    {
      String prefix = (String)m_prefixMappings.elementAt(i);
      
      if (prefix != null)
      {

        String declURL = (String)m_prefixMappings.elementAt(i + 1);
        
        exName = m_expandedNameTable.getExpandedTypeID(null, prefix, 13);
        
        m_values.addElement(declURL);
        int val = m_valueIndex++;
        
        addNode(13, exName, elemNode, -1, val, false);
      }
    }
    int n = attributes.getLength();
    
    for (int i = 0; i < n; i++)
    {
      String attrUri = attributes.getURI(i);
      String attrQName = attributes.getQName(i);
      String valString = attributes.getValue(i);
      


      String attrLocalName = attributes.getLocalName(i);
      int nodeType;
      int nodeType; if ((null != attrQName) && ((attrQName.equals("xmlns")) || (attrQName.startsWith("xmlns:"))))
      {


        String prefix = getPrefix(attrQName, attrUri);
        if (declAlreadyDeclared(prefix)) {
          continue;
        }
        nodeType = 13;
      }
      else
      {
        nodeType = 2;
        
        if ((m_buildIdIndex) && (attributes.getType(i).equalsIgnoreCase("ID"))) {
          setIDAttribute(valString, elemNode);
        }
      }
      

      if (null == valString) {
        valString = "";
      }
      m_values.addElement(valString);
      int val = m_valueIndex++;
      
      if (attrLocalName.length() != attrQName.length())
      {

        prefixIndex = m_valuesOrPrefixes.stringToIndex(attrQName);
        
        int dataIndex = m_data.size();
        
        m_data.addElement(prefixIndex);
        m_data.addElement(val);
        
        val = -dataIndex;
      }
      
      exName = m_expandedNameTable.getExpandedTypeID(attrUri, attrLocalName, nodeType);
      addNode(nodeType, exName, elemNode, -1, val, false);
    }
    

    if (null != m_wsfilter)
    {
      short wsv = m_wsfilter.getShouldStripSpace(makeNodeHandle(elemNode), this);
      boolean shouldStrip = 2 == wsv ? true : 3 == wsv ? getShouldStripWhitespace() : false;
      


      pushShouldStripWhitespace(shouldStrip);
    }
    
    m_previous = -1;
    
    m_contextIndexes.push(m_prefixMappings.size());
  }
  




















  public void endElement(String uri, String localName, String qName)
    throws SAXException
  {
    charactersFlush();
    


    m_contextIndexes.quickPop(1);
    

    int topContextIndex = m_contextIndexes.peek();
    if (topContextIndex != m_prefixMappings.size()) {
      m_prefixMappings.setSize(topContextIndex);
    }
    
    m_previous = m_parents.pop();
    
    popShouldStripWhitespace();
  }
  












  public void comment(char[] ch, int start, int length)
    throws SAXException
  {
    if (m_insideDTD) {
      return;
    }
    charactersFlush();
    


    m_values.addElement(new String(ch, start, length));
    int dataIndex = m_valueIndex++;
    
    m_previous = addNode(8, 8, m_parents.peek(), m_previous, dataIndex, false);
  }
  








  public void startDocument()
    throws SAXException
  {
    int doc = addNode(9, 9, -1, -1, 0, true);
    


    m_parents.push(doc);
    m_previous = -1;
    
    m_contextIndexes.push(m_prefixMappings.size());
  }
  






  public void endDocument()
    throws SAXException
  {
    super.endDocument();
    


    m_exptype.addElement(-1);
    m_parent.addElement(-1);
    m_nextsib.addElement(-1);
    m_firstch.addElement(-1);
    

    m_extendedTypes = m_expandedNameTable.getExtendedTypes();
    m_exptype_map = m_exptype.getMap();
    m_nextsib_map = m_nextsib.getMap();
    m_firstch_map = m_firstch.getMap();
    m_parent_map = m_parent.getMap();
  }
  
















  protected final int addNode(int type, int expandedTypeID, int parentIndex, int previousSibling, int dataOrPrefix, boolean canHaveFirstChild)
  {
    int nodeIndex = m_size++;
    


    if (nodeIndex == m_maxNodeIndex)
    {
      addNewDTMID(nodeIndex);
      m_maxNodeIndex += 65536;
    }
    
    m_firstch.addElement(-1);
    m_nextsib.addElement(-1);
    m_parent.addElement(parentIndex);
    m_exptype.addElement(expandedTypeID);
    m_dataOrQName.addElement(dataOrPrefix);
    
    if (m_prevsib != null) {
      m_prevsib.addElement(previousSibling);
    }
    
    if ((m_locator != null) && (m_useSourceLocationProperty)) {
      setSourceLocation();
    }
    




    switch (type)
    {
    case 13: 
      declareNamespaceInContext(parentIndex, nodeIndex);
      break;
    case 2: 
      break;
    default: 
      if (-1 != previousSibling) {
        m_nextsib.setElementAt(nodeIndex, previousSibling);
      }
      else if (-1 != parentIndex) {
        m_firstch.setElementAt(nodeIndex, parentIndex);
      }
      break;
    }
    
    return nodeIndex;
  }
  





  protected final void charactersFlush()
  {
    if (m_textPendingStart >= 0)
    {
      int length = m_chars.size() - m_textPendingStart;
      boolean doStrip = false;
      
      if (getShouldStripWhitespace())
      {
        doStrip = m_chars.isWhitespace(m_textPendingStart, length);
      }
      
      if (doStrip) {
        m_chars.setLength(m_textPendingStart);


      }
      else if (length > 0)
      {


        if ((length <= 1023) && (m_textPendingStart <= 2097151))
        {
          m_previous = addNode(m_coalescedTextType, 3, m_parents.peek(), m_previous, length + (m_textPendingStart << 10), false);


        }
        else
        {


          int dataIndex = m_data.size();
          m_previous = addNode(m_coalescedTextType, 3, m_parents.peek(), m_previous, -dataIndex, false);
          

          m_data.addElement(m_textPendingStart);
          m_data.addElement(length);
        }
      }
      


      m_textPendingStart = -1;
      m_textType = (this.m_coalescedTextType = 3);
    }
  }
  

















  public void processingInstruction(String target, String data)
    throws SAXException
  {
    charactersFlush();
    
    int dataIndex = m_data.size();
    m_previous = addNode(7, 7, m_parents.peek(), m_previous, -dataIndex, false);
    



    m_data.addElement(m_valuesOrPrefixes.stringToIndex(target));
    m_values.addElement(data);
    m_data.addElement(m_valueIndex++);
  }
  









  public final int getFirstAttribute(int nodeHandle)
  {
    int nodeID = makeNodeIdentity(nodeHandle);
    
    if (nodeID == -1) {
      return -1;
    }
    int type = _type2(nodeID);
    
    if (1 == type)
    {
      for (;;)
      {

        nodeID++;
        
        type = _type2(nodeID);
        
        if (type == 2)
        {
          return makeNodeHandle(nodeID);
        }
        if (13 != type) {
          break;
        }
      }
    }
    

    return -1;
  }
  







  protected int getFirstAttributeIdentity(int identity)
  {
    if (identity == -1) {
      return -1;
    }
    int type = _type2(identity);
    
    if (1 == type)
    {
      for (;;)
      {

        identity++;
        

        type = _type2(identity);
        
        if (type == 2)
        {
          return identity;
        }
        if (13 != type) {
          break;
        }
      }
    }
    

    return -1;
  }
  











  protected int getNextAttributeIdentity(int identity)
  {
    for (;;)
    {
      identity++;
      int type = _type2(identity);
      
      if (type == 2)
        return identity;
      if (type != 13) {
        break;
      }
    }
    
    return -1;
  }
  












  protected final int getTypedAttribute(int nodeHandle, int attType)
  {
    int nodeID = makeNodeIdentity(nodeHandle);
    
    if (nodeID == -1) {
      return -1;
    }
    int type = _type2(nodeID);
    
    if (1 == type)
    {
      for (;;)
      {

        nodeID++;
        int expType = _exptype2(nodeID);
        
        if (expType != -1) {
          type = m_extendedTypes[expType].getNodeType();
        } else {
          return -1;
        }
        if (type == 2)
        {
          if (expType == attType) return makeNodeHandle(nodeID);
        }
        else if (13 != type) {
          break;
        }
      }
    }
    

    return -1;
  }
  










  public String getLocalName(int nodeHandle)
  {
    int expType = _exptype(makeNodeIdentity(nodeHandle));
    
    if (expType == 7)
    {
      int dataIndex = _dataOrQName(makeNodeIdentity(nodeHandle));
      dataIndex = m_data.elementAt(-dataIndex);
      return m_valuesOrPrefixes.indexToString(dataIndex);
    }
    
    return m_expandedNameTable.getLocalName(expType);
  }
  










  public final String getNodeNameX(int nodeHandle)
  {
    int nodeID = makeNodeIdentity(nodeHandle);
    int eType = _exptype2(nodeID);
    
    if (eType == 7)
    {
      int dataIndex = _dataOrQName(nodeID);
      dataIndex = m_data.elementAt(-dataIndex);
      return m_valuesOrPrefixes.indexToString(dataIndex);
    }
    
    ExtendedType extType = m_extendedTypes[eType];
    
    if (extType.getNamespace().length() == 0)
    {
      return extType.getLocalName();
    }
    

    int qnameIndex = m_dataOrQName.elementAt(nodeID);
    
    if (qnameIndex == 0) {
      return extType.getLocalName();
    }
    if (qnameIndex < 0)
    {
      qnameIndex = -qnameIndex;
      qnameIndex = m_data.elementAt(qnameIndex);
    }
    
    return m_valuesOrPrefixes.indexToString(qnameIndex);
  }
  













  public String getNodeName(int nodeHandle)
  {
    int nodeID = makeNodeIdentity(nodeHandle);
    int eType = _exptype2(nodeID);
    
    ExtendedType extType = m_extendedTypes[eType];
    if (extType.getNamespace().length() == 0)
    {
      int type = extType.getNodeType();
      
      String localName = extType.getLocalName();
      if (type == 13)
      {
        if (localName.length() == 0) {
          return "xmlns";
        }
        return "xmlns:" + localName;
      }
      if (type == 7)
      {
        int dataIndex = _dataOrQName(nodeID);
        dataIndex = m_data.elementAt(-dataIndex);
        return m_valuesOrPrefixes.indexToString(dataIndex);
      }
      if (localName.length() == 0)
      {
        return getFixedNames(type);
      }
      
      return localName;
    }
    

    int qnameIndex = m_dataOrQName.elementAt(nodeID);
    
    if (qnameIndex == 0) {
      return extType.getLocalName();
    }
    if (qnameIndex < 0)
    {
      qnameIndex = -qnameIndex;
      qnameIndex = m_data.elementAt(qnameIndex);
    }
    
    return m_valuesOrPrefixes.indexToString(qnameIndex);
  }
  


















  public XMLString getStringValue(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    if (identity == -1) {
      return EMPTY_XML_STR;
    }
    int type = _type2(identity);
    
    if ((type == 1) || (type == 9))
    {
      int startNode = identity;
      identity = _firstch2(identity);
      if (-1 != identity)
      {
        int offset = -1;
        int length = 0;
        
        do
        {
          type = _exptype2(identity);
          
          if ((type == 3) || (type == 4))
          {
            int dataIndex = m_dataOrQName.elementAt(identity);
            if (dataIndex >= 0)
            {
              if (-1 == offset)
              {
                offset = dataIndex >>> 10;
              }
              
              length += (dataIndex & 0x3FF);
            }
            else
            {
              if (-1 == offset)
              {
                offset = m_data.elementAt(-dataIndex);
              }
              
              length += m_data.elementAt(-dataIndex + 1);
            }
          }
          
          identity++;
        } while (_parent2(identity) >= startNode);
        
        if (length > 0)
        {
          if (m_xstrf != null) {
            return m_xstrf.newstr(m_chars, offset, length);
          }
          return new XMLStringDefault(m_chars.getString(offset, length));
        }
        
        return EMPTY_XML_STR;
      }
      
      return EMPTY_XML_STR;
    }
    if ((3 == type) || (4 == type))
    {
      int dataIndex = m_dataOrQName.elementAt(identity);
      if (dataIndex >= 0)
      {
        if (m_xstrf != null) {
          return m_xstrf.newstr(m_chars, dataIndex >>> 10, dataIndex & 0x3FF);
        }
        
        return new XMLStringDefault(m_chars.getString(dataIndex >>> 10, dataIndex & 0x3FF));
      }
      


      if (m_xstrf != null) {
        return m_xstrf.newstr(m_chars, m_data.elementAt(-dataIndex), m_data.elementAt(-dataIndex + 1));
      }
      
      return new XMLStringDefault(m_chars.getString(m_data.elementAt(-dataIndex), m_data.elementAt(-dataIndex + 1)));
    }
    



    int dataIndex = m_dataOrQName.elementAt(identity);
    
    if (dataIndex < 0)
    {
      dataIndex = -dataIndex;
      dataIndex = m_data.elementAt(dataIndex + 1);
    }
    
    if (m_xstrf != null) {
      return m_xstrf.newstr((String)m_values.elementAt(dataIndex));
    }
    return new XMLStringDefault((String)m_values.elementAt(dataIndex));
  }
  















  public final String getStringValueX(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    if (identity == -1) {
      return "";
    }
    int type = _type2(identity);
    
    if ((type == 1) || (type == 9))
    {
      int startNode = identity;
      identity = _firstch2(identity);
      if (-1 != identity)
      {
        int offset = -1;
        int length = 0;
        
        do
        {
          type = _exptype2(identity);
          
          if ((type == 3) || (type == 4))
          {
            int dataIndex = m_dataOrQName.elementAt(identity);
            if (dataIndex >= 0)
            {
              if (-1 == offset)
              {
                offset = dataIndex >>> 10;
              }
              
              length += (dataIndex & 0x3FF);
            }
            else
            {
              if (-1 == offset)
              {
                offset = m_data.elementAt(-dataIndex);
              }
              
              length += m_data.elementAt(-dataIndex + 1);
            }
          }
          
          identity++;
        } while (_parent2(identity) >= startNode);
        
        if (length > 0)
        {
          return m_chars.getString(offset, length);
        }
        
        return "";
      }
      
      return "";
    }
    if ((3 == type) || (4 == type))
    {
      int dataIndex = m_dataOrQName.elementAt(identity);
      if (dataIndex >= 0)
      {
        return m_chars.getString(dataIndex >>> 10, dataIndex & 0x3FF);
      }
      


      return m_chars.getString(m_data.elementAt(-dataIndex), m_data.elementAt(-dataIndex + 1));
    }
    



    int dataIndex = m_dataOrQName.elementAt(identity);
    
    if (dataIndex < 0)
    {
      dataIndex = -dataIndex;
      dataIndex = m_data.elementAt(dataIndex + 1);
    }
    
    return (String)m_values.elementAt(dataIndex);
  }
  




  public String getStringValue()
  {
    int child = _firstch2(0);
    if (child == -1) { return "";
    }
    
    if ((_exptype2(child) == 3) && (_nextsib2(child) == -1))
    {
      int dataIndex = m_dataOrQName.elementAt(child);
      if (dataIndex >= 0) {
        return m_chars.getString(dataIndex >>> 10, dataIndex & 0x3FF);
      }
      return m_chars.getString(m_data.elementAt(-dataIndex), m_data.elementAt(-dataIndex + 1));
    }
    

    return getStringValueX(getDocument());
  }
  






















  public final void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize)
    throws SAXException
  {
    int identity = makeNodeIdentity(nodeHandle);
    
    if (identity == -1) {
      return;
    }
    int type = _type2(identity);
    
    if ((type == 1) || (type == 9))
    {
      int startNode = identity;
      identity = _firstch2(identity);
      if (-1 != identity)
      {
        int offset = -1;
        int length = 0;
        
        do
        {
          type = _exptype2(identity);
          
          if ((type == 3) || (type == 4))
          {
            int dataIndex = m_dataOrQName.elementAt(identity);
            
            if (dataIndex >= 0)
            {
              if (-1 == offset)
              {
                offset = dataIndex >>> 10;
              }
              
              length += (dataIndex & 0x3FF);
            }
            else
            {
              if (-1 == offset)
              {
                offset = m_data.elementAt(-dataIndex);
              }
              
              length += m_data.elementAt(-dataIndex + 1);
            }
          }
          
          identity++;
        } while (_parent2(identity) >= startNode);
        
        if (length > 0)
        {
          if (normalize) {
            m_chars.sendNormalizedSAXcharacters(ch, offset, length);
          } else {
            m_chars.sendSAXcharacters(ch, offset, length);
          }
        }
      }
    } else if ((3 == type) || (4 == type))
    {
      int dataIndex = m_dataOrQName.elementAt(identity);
      
      if (dataIndex >= 0)
      {
        if (normalize) {
          m_chars.sendNormalizedSAXcharacters(ch, dataIndex >>> 10, dataIndex & 0x3FF);
        }
        else {
          m_chars.sendSAXcharacters(ch, dataIndex >>> 10, dataIndex & 0x3FF);
        }
        

      }
      else if (normalize) {
        m_chars.sendNormalizedSAXcharacters(ch, m_data.elementAt(-dataIndex), m_data.elementAt(-dataIndex + 1));
      }
      else {
        m_chars.sendSAXcharacters(ch, m_data.elementAt(-dataIndex), m_data.elementAt(-dataIndex + 1));
      }
      
    }
    else
    {
      int dataIndex = m_dataOrQName.elementAt(identity);
      
      if (dataIndex < 0)
      {
        dataIndex = -dataIndex;
        dataIndex = m_data.elementAt(dataIndex + 1);
      }
      
      String str = (String)m_values.elementAt(dataIndex);
      
      if (normalize) {
        FastStringBuffer.sendNormalizedSAXcharacters(str.toCharArray(), 0, str.length(), ch);
      }
      else {
        ch.characters(str.toCharArray(), 0, str.length());
      }
    }
  }
  









  public String getNodeValue(int nodeHandle)
  {
    int identity = makeNodeIdentity(nodeHandle);
    int type = _type2(identity);
    
    if ((type == 3) || (type == 4))
    {
      int dataIndex = _dataOrQName(identity);
      if (dataIndex > 0)
      {
        return m_chars.getString(dataIndex >>> 10, dataIndex & 0x3FF);
      }
      


      return m_chars.getString(m_data.elementAt(-dataIndex), m_data.elementAt(-dataIndex + 1));
    }
    

    if ((1 == type) || (11 == type) || (9 == type))
    {

      return null;
    }
    

    int dataIndex = m_dataOrQName.elementAt(identity);
    
    if (dataIndex < 0)
    {
      dataIndex = -dataIndex;
      dataIndex = m_data.elementAt(dataIndex + 1);
    }
    
    return (String)m_values.elementAt(dataIndex);
  }
  




  protected final void copyTextNode(int nodeID, SerializationHandler handler)
    throws SAXException
  {
    if (nodeID != -1) {
      int dataIndex = m_dataOrQName.elementAt(nodeID);
      if (dataIndex >= 0) {
        m_chars.sendSAXcharacters(handler, dataIndex >>> 10, dataIndex & 0x3FF);
      }
      else
      {
        m_chars.sendSAXcharacters(handler, m_data.elementAt(-dataIndex), m_data.elementAt(-dataIndex + 1));
      }
    }
  }
  










  protected final String copyElement(int nodeID, int exptype, SerializationHandler handler)
    throws SAXException
  {
    ExtendedType extType = m_extendedTypes[exptype];
    String uri = extType.getNamespace();
    String name = extType.getLocalName();
    
    if (uri.length() == 0) {
      handler.startElement(name);
      return name;
    }
    
    int qnameIndex = m_dataOrQName.elementAt(nodeID);
    
    if (qnameIndex == 0) {
      handler.startElement(name);
      handler.namespaceAfterStartElement("", uri);
      return name;
    }
    
    if (qnameIndex < 0) {
      qnameIndex = -qnameIndex;
      qnameIndex = m_data.elementAt(qnameIndex);
    }
    
    String qName = m_valuesOrPrefixes.indexToString(qnameIndex);
    handler.startElement(qName);
    int prefixIndex = qName.indexOf(':');
    String prefix;
    String prefix; if (prefixIndex > 0) {
      prefix = qName.substring(0, prefixIndex);
    }
    else {
      prefix = null;
    }
    handler.namespaceAfterStartElement(prefix, uri);
    return qName;
  }
  
















  protected final void copyNS(int nodeID, SerializationHandler handler, boolean inScope)
    throws SAXException
  {
    if ((m_namespaceDeclSetElements != null) && (m_namespaceDeclSetElements.size() == 1) && (m_namespaceDeclSets != null) && (((SuballocatedIntVector)m_namespaceDeclSets.elementAt(0)).size() == 1))
    {



      return;
    }
    SuballocatedIntVector nsContext = null;
    
    int nextNSNode;
    int nextNSNode;
    if (inScope) {
      nsContext = findNamespaceContext(nodeID);
      if ((nsContext == null) || (nsContext.size() < 1)) {
        return;
      }
      nextNSNode = makeNodeIdentity(nsContext.elementAt(0));
    }
    else {
      nextNSNode = getNextNamespaceNode2(nodeID);
    }
    int nsIndex = 1;
    while (nextNSNode != -1)
    {
      int eType = _exptype2(nextNSNode);
      String nodeName = m_extendedTypes[eType].getLocalName();
      

      int dataIndex = m_dataOrQName.elementAt(nextNSNode);
      
      if (dataIndex < 0) {
        dataIndex = -dataIndex;
        dataIndex = m_data.elementAt(dataIndex + 1);
      }
      
      String nodeValue = (String)m_values.elementAt(dataIndex);
      
      handler.namespaceAfterStartElement(nodeName, nodeValue);
      
      if (inScope) {
        if (nsIndex < nsContext.size()) {
          nextNSNode = makeNodeIdentity(nsContext.elementAt(nsIndex));
          nsIndex++;
        }
        

      }
      else {
        nextNSNode = getNextNamespaceNode2(nextNSNode);
      }
    }
  }
  



  protected final int getNextNamespaceNode2(int baseID)
  {
    int type;
    

    while ((type = _type2(++baseID)) == 2) {}
    
    if (type == 13) {
      return baseID;
    }
    return -1;
  }
  






  protected final void copyAttributes(int nodeID, SerializationHandler handler)
    throws SAXException
  {
    for (int current = getFirstAttributeIdentity(nodeID); current != -1; current = getNextAttributeIdentity(current)) {
      int eType = _exptype2(current);
      copyAttribute(current, eType, handler);
    }
  }
  


















  protected final void copyAttribute(int nodeID, int exptype, SerializationHandler handler)
    throws SAXException
  {
    ExtendedType extType = m_extendedTypes[exptype];
    String uri = extType.getNamespace();
    String localName = extType.getLocalName();
    
    String prefix = null;
    String qname = null;
    int dataIndex = _dataOrQName(nodeID);
    int valueIndex = dataIndex;
    if (dataIndex <= 0) {
      int prefixIndex = m_data.elementAt(-dataIndex);
      valueIndex = m_data.elementAt(-dataIndex + 1);
      qname = m_valuesOrPrefixes.indexToString(prefixIndex);
      int colonIndex = qname.indexOf(':');
      if (colonIndex > 0) {
        prefix = qname.substring(0, colonIndex);
      }
    }
    if (uri.length() != 0) {
      handler.namespaceAfterStartElement(prefix, uri);
    }
    
    String nodeName = prefix != null ? qname : localName;
    String nodeValue = (String)m_values.elementAt(valueIndex);
    
    handler.addAttribute(nodeName, nodeValue);
  }
}
