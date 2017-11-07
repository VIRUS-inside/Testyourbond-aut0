package net.sourceforge.htmlunit.corejs.javascript;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sourceforge.htmlunit.corejs.javascript.ast.Comment;
import net.sourceforge.htmlunit.corejs.javascript.ast.Jump;
import net.sourceforge.htmlunit.corejs.javascript.ast.Name;
import net.sourceforge.htmlunit.corejs.javascript.ast.NumberLiteral;
import net.sourceforge.htmlunit.corejs.javascript.ast.Scope;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;

































public class Node
  implements Iterable<Node>
{
  public static final int FUNCTION_PROP = 1;
  public static final int LOCAL_PROP = 2;
  public static final int LOCAL_BLOCK_PROP = 3;
  public static final int REGEXP_PROP = 4;
  public static final int CASEARRAY_PROP = 5;
  public static final int TARGETBLOCK_PROP = 6;
  public static final int VARIABLE_PROP = 7;
  public static final int ISNUMBER_PROP = 8;
  public static final int DIRECTCALL_PROP = 9;
  public static final int SPECIALCALL_PROP = 10;
  public static final int SKIP_INDEXES_PROP = 11;
  public static final int OBJECT_IDS_PROP = 12;
  public static final int INCRDECR_PROP = 13;
  public static final int CATCH_SCOPE_PROP = 14;
  public static final int LABEL_ID_PROP = 15;
  public static final int MEMBER_TYPE_PROP = 16;
  public static final int NAME_PROP = 17;
  public static final int CONTROL_BLOCK_PROP = 18;
  public static final int PARENTHESIZED_PROP = 19;
  public static final int GENERATOR_END_PROP = 20;
  public static final int DESTRUCTURING_ARRAY_LENGTH = 21;
  public static final int DESTRUCTURING_NAMES = 22;
  public static final int DESTRUCTURING_PARAMS = 23;
  public static final int JSDOC_PROP = 24;
  public static final int EXPRESSION_CLOSURE_PROP = 25;
  public static final int DESTRUCTURING_SHORTHAND = 26;
  public static final int LAST_PROP = 26;
  public static final int BOTH = 0;
  public static final int LEFT = 1;
  public static final int RIGHT = 2;
  public static final int NON_SPECIALCALL = 0;
  public static final int SPECIALCALL_EVAL = 1;
  public static final int SPECIALCALL_WITH = 2;
  public static final int DECR_FLAG = 1;
  public static final int POST_FLAG = 2;
  public static final int PROPERTY_FLAG = 1;
  public static final int ATTRIBUTE_FLAG = 2;
  public static final int DESCENDANTS_FLAG = 4;
  
  public Node(int nodeType)
  {
    type = nodeType;
  }
  
  public Node(int nodeType, Node child) {
    type = nodeType;
    first = (this.last = child);
    next = null;
  }
  
  public Node(int nodeType, Node left, Node right) {
    type = nodeType;
    first = left;
    last = right;
    next = right;
    next = null;
  }
  
  public Node(int nodeType, Node left, Node mid, Node right) {
    type = nodeType;
    first = left;
    last = right;
    next = mid;
    next = right;
    next = null;
  }
  
  public Node(int nodeType, int line) {
    type = nodeType;
    lineno = line;
  }
  
  public Node(int nodeType, Node child, int line) {
    this(nodeType, child);
    lineno = line;
  }
  
  public Node(int nodeType, Node left, Node right, int line) {
    this(nodeType, left, right);
    lineno = line;
  }
  
  public Node(int nodeType, Node left, Node mid, Node right, int line) {
    this(nodeType, left, mid, right);
    lineno = line;
  }
  
  public static Node newNumber(double number) {
    NumberLiteral n = new NumberLiteral();
    n.setNumber(number);
    return n;
  }
  
  public static Node newString(String str) {
    return newString(41, str);
  }
  
  public static Node newString(int type, String str) {
    Name name = new Name();
    name.setIdentifier(str);
    name.setType(type);
    return name;
  }
  
  public int getType() {
    return type;
  }
  


  public Node setType(int type)
  {
    this.type = type;
    return this;
  }
  





  public String getJsDoc()
  {
    Comment comment = getJsDocNode();
    if (comment != null) {
      return comment.getValue();
    }
    return null;
  }
  




  public Comment getJsDocNode()
  {
    return (Comment)getProp(24);
  }
  


  public void setJsDocNode(Comment jsdocNode)
  {
    putProp(24, jsdocNode);
  }
  
  public boolean hasChildren() {
    return first != null;
  }
  
  public Node getFirstChild() {
    return first;
  }
  
  public Node getLastChild() {
    return last;
  }
  
  public Node getNext() {
    return next;
  }
  
  public Node getChildBefore(Node child) {
    if (child == first)
      return null;
    Node n = first;
    while (next != child) {
      n = next;
      if (n == null)
        throw new RuntimeException("node is not a child");
    }
    return n;
  }
  
  public Node getLastSibling() {
    Node n = this;
    while (next != null) {
      n = next;
    }
    return n;
  }
  
  public void addChildToFront(Node child) {
    next = first;
    first = child;
    if (last == null) {
      last = child;
    }
  }
  
  public void addChildToBack(Node child) {
    next = null;
    if (last == null) {
      first = (this.last = child);
      return;
    }
    last.next = child;
    last = child;
  }
  
  public void addChildrenToFront(Node children) {
    Node lastSib = children.getLastSibling();
    next = first;
    first = children;
    if (last == null) {
      last = lastSib;
    }
  }
  
  public void addChildrenToBack(Node children) {
    if (last != null) {
      last.next = children;
    }
    last = children.getLastSibling();
    if (first == null) {
      first = children;
    }
  }
  


  public void addChildBefore(Node newChild, Node node)
  {
    if (next != null) {
      throw new RuntimeException("newChild had siblings in addChildBefore");
    }
    if (first == node) {
      next = first;
      first = newChild;
      return;
    }
    Node prev = getChildBefore(node);
    addChildAfter(newChild, prev);
  }
  


  public void addChildAfter(Node newChild, Node node)
  {
    if (next != null) {
      throw new RuntimeException("newChild had siblings in addChildAfter");
    }
    next = next;
    next = newChild;
    if (last == node)
      last = newChild;
  }
  
  public void removeChild(Node child) {
    Node prev = getChildBefore(child);
    if (prev == null) {
      first = first.next;
    } else
      next = next;
    if (child == last)
      last = prev;
    next = null;
  }
  
  public void replaceChild(Node child, Node newChild) {
    next = next;
    if (child == first) {
      first = newChild;
    } else {
      Node prev = getChildBefore(child);
      next = newChild;
    }
    if (child == last)
      last = newChild;
    next = null;
  }
  
  public void replaceChildAfter(Node prevChild, Node newChild) {
    Node child = next;
    next = next;
    next = newChild;
    if (child == last)
      last = newChild;
    next = null;
  }
  
  public void removeChildren() {
    first = (this.last = null);
  }
  
  private static final Node NOT_SET = new Node(-1);
  public static final int END_UNREACHED = 0;
  public static final int END_DROPS_OFF = 1;
  public static final int END_RETURNS = 2;
  public static final int END_RETURNS_VALUE = 4;
  public static final int END_YIELDS = 8;
  
  public class NodeIterator implements Iterator<Node> {
    private Node cursor;
    private Node prev = Node.NOT_SET;
    private Node prev2;
    private boolean removed = false;
    
    public NodeIterator() {
      cursor = first;
    }
    
    public boolean hasNext() {
      return cursor != null;
    }
    
    public Node next() {
      if (cursor == null) {
        throw new NoSuchElementException();
      }
      removed = false;
      prev2 = prev;
      prev = cursor;
      cursor = cursor.next;
      return prev;
    }
    
    public void remove() {
      if (prev == Node.NOT_SET) {
        throw new IllegalStateException("next() has not been called");
      }
      if (removed) {
        throw new IllegalStateException("remove() already called for current element");
      }
      
      if (prev == first) {
        first = prev.next;
      } else if (prev == last) {
        prev2.next = null;
        last = prev2;
      } else {
        prev2.next = cursor;
      }
    }
  }
  


  public Iterator<Node> iterator()
  {
    return new NodeIterator();
  }
  
























































  private static final String propToString(int propType)
  {
    return null;
  }
  
  private PropListItem lookupProperty(int propType) {
    PropListItem x = propListHead;
    while ((x != null) && (propType != type)) {
      x = next;
    }
    return x;
  }
  
  private PropListItem ensureProperty(int propType) {
    PropListItem item = lookupProperty(propType);
    if (item == null) {
      item = new PropListItem(null);
      type = propType;
      next = propListHead;
      propListHead = item;
    }
    return item;
  }
  
  public void removeProp(int propType) {
    PropListItem x = propListHead;
    if (x != null) {
      PropListItem prev = null;
      while (type != propType) {
        prev = x;
        x = next;
        if (x == null) {
          return;
        }
      }
      if (prev == null) {
        propListHead = next;
      } else {
        next = next;
      }
    }
  }
  
  public Object getProp(int propType) {
    PropListItem item = lookupProperty(propType);
    if (item == null) {
      return null;
    }
    return objectValue;
  }
  
  public int getIntProp(int propType, int defaultValue) {
    PropListItem item = lookupProperty(propType);
    if (item == null) {
      return defaultValue;
    }
    return intValue;
  }
  
  public int getExistingIntProp(int propType) {
    PropListItem item = lookupProperty(propType);
    if (item == null) {
      Kit.codeBug();
    }
    return intValue;
  }
  
  public void putProp(int propType, Object prop) {
    if (prop == null) {
      removeProp(propType);
    } else {
      PropListItem item = ensureProperty(propType);
      objectValue = prop;
    }
  }
  
  public void putIntProp(int propType, int prop) {
    PropListItem item = ensureProperty(propType);
    intValue = prop;
  }
  




  public int getLineno()
  {
    return lineno;
  }
  
  public void setLineno(int lineno) {
    this.lineno = lineno;
  }
  
  public final double getDouble()
  {
    return ((NumberLiteral)this).getNumber();
  }
  
  public final void setDouble(double number) {
    ((NumberLiteral)this).setNumber(number);
  }
  
  public final String getString()
  {
    return ((Name)this).getIdentifier();
  }
  
  public final void setString(String s)
  {
    if (s == null)
      Kit.codeBug();
    ((Name)this).setIdentifier(s);
  }
  
  public Scope getScope()
  {
    return ((Name)this).getScope();
  }
  
  public void setScope(Scope s)
  {
    if (s == null)
      Kit.codeBug();
    if (!(this instanceof Name)) {
      throw Kit.codeBug();
    }
    ((Name)this).setScope(s);
  }
  
  public static Node newTarget() {
    return new Node(131);
  }
  
  public final int labelId() {
    if ((type != 131) && (type != 72))
      Kit.codeBug();
    return getIntProp(15, -1);
  }
  
  public void labelId(int labelId) {
    if ((type != 131) && (type != 72))
      Kit.codeBug();
    putIntProp(15, labelId);
  }
  





























































  public boolean hasConsistentReturnUsage()
  {
    int n = endCheck();
    return ((n & 0x4) == 0) || ((n & 0xB) == 0);
  }
  







  private int endCheckIf()
  {
    int rv = 0;
    
    Node th = next;
    Node el = target;
    
    rv = th.endCheck();
    
    if (el != null) {
      rv |= el.endCheck();
    } else {
      rv |= 0x1;
    }
    return rv;
  }
  







  private int endCheckSwitch()
  {
    int rv = 0;
    






















    return rv;
  }
  








  private int endCheckTry()
  {
    int rv = 0;
    































    return rv;
  }
  
















  private int endCheckLoop()
  {
    int rv = 0;
    





    for (Node n = first; next != last; n = next) {}
    

    if (type != 6) {
      return 1;
    }
    
    rv = target.next.endCheck();
    

    if (first.type == 45) {
      rv &= 0xFFFFFFFE;
    }
    
    rv |= getIntProp(18, 0);
    
    return rv;
  }
  







  private int endCheckBlock()
  {
    int rv = 1;
    


    for (Node n = first; ((rv & 0x1) != 0) && (n != null); n = next) {
      rv &= 0xFFFFFFFE;
      rv |= n.endCheck();
    }
    return rv;
  }
  







  private int endCheckLabel()
  {
    int rv = 0;
    
    rv = next.endCheck();
    rv |= getIntProp(18, 0);
    
    return rv;
  }
  





  private int endCheckBreak()
  {
    Node n = ((Jump)this).getJumpStatement();
    n.putIntProp(18, 1);
    return 0;
  }
  









  private int endCheck()
  {
    switch (type) {
    case 120: 
      return endCheckBreak();
    
    case 133: 
      if (first != null)
        return first.endCheck();
      return 1;
    
    case 72: 
      return 8;
    
    case 50: 
    case 121: 
      return 0;
    
    case 4: 
      if (first != null) {
        return 4;
      }
      return 2;
    
    case 131: 
      if (next != null) {
        return next.endCheck();
      }
      return 1;
    
    case 132: 
      return endCheckLoop();
    

    case 129: 
    case 141: 
      if (first == null) {
        return 1;
      }
      switch (first.type) {
      case 130: 
        return first.endCheckLabel();
      
      case 7: 
        return first.endCheckIf();
      
      case 114: 
        return first.endCheckSwitch();
      
      case 81: 
        return first.endCheckTry();
      }
      
      return endCheckBlock();
    }
    
    
    return 1;
  }
  
  public boolean hasSideEffects()
  {
    switch (type) {
    case 89: 
    case 133: 
      if (last != null) {
        return last.hasSideEffects();
      }
      return true;
    
    case 102: 
      if ((first == null) || (first.next == null) || (first.next.next == null))
        Kit.codeBug();
      return (first.next.hasSideEffects()) && 
        (first.next.next.hasSideEffects());
    
    case 104: 
    case 105: 
      if ((first == null) || (last == null))
        Kit.codeBug();
      return (first.hasSideEffects()) || (last.hasSideEffects());
    
    case -1: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 30: 
    case 31: 
    case 35: 
    case 37: 
    case 38: 
    case 50: 
    case 51: 
    case 56: 
    case 57: 
    case 64: 
    case 68: 
    case 69: 
    case 70: 
    case 72: 
    case 81: 
    case 82: 
    case 90: 
    case 91: 
    case 92: 
    case 93: 
    case 94: 
    case 95: 
    case 96: 
    case 97: 
    case 98: 
    case 99: 
    case 100: 
    case 101: 
    case 106: 
    case 107: 
    case 112: 
    case 113: 
    case 114: 
    case 117: 
    case 118: 
    case 119: 
    case 120: 
    case 121: 
    case 122: 
    case 123: 
    case 124: 
    case 125: 
    case 129: 
    case 130: 
    case 131: 
    case 132: 
    case 134: 
    case 135: 
    case 139: 
    case 140: 
    case 141: 
    case 142: 
    case 153: 
    case 154: 
    case 158: 
    case 159: 
      return true;
    }
    
    return false;
  }
  











  public void resetTargets()
  {
    if (type == 125) {
      resetTargets_r();
    } else {
      Kit.codeBug();
    }
  }
  
  private void resetTargets_r() {
    if ((type == 131) || (type == 72)) {
      labelId(-1);
    }
    Node child = first;
    while (child != null) {
      child.resetTargets_r();
      child = next;
    }
  }
  





  public String toString()
  {
    return String.valueOf(type);
  }
  















































































  private void toString(ObjToIntMap printIds, StringBuilder sb) {}
  















































































  public String toStringTree(ScriptNode treeTop)
  {
    return null;
  }
  





















































  protected int type = -1;
  protected Node next;
  protected Node first;
  protected Node last;
  protected int lineno = -1;
  protected PropListItem propListHead;
  
  private static void toStringTreeHelper(ScriptNode treeTop, Node n, ObjToIntMap printIds, int level, StringBuilder sb) {}
  
  private static void generatePrintIds(Node n, ObjToIntMap map) {}
  
  private static void appendPrintId(Node n, ObjToIntMap printIds, StringBuilder sb) {}
  
  private static class PropListItem
  {
    PropListItem next;
    int type;
    int intValue;
    Object objectValue;
    
    private PropListItem() {}
  }
}
