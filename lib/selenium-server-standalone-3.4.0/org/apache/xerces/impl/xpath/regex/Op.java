package org.apache.xerces.impl.xpath.regex;

import java.util.Vector;

class Op
{
  static final int DOT = 0;
  static final int CHAR = 1;
  static final int RANGE = 3;
  static final int NRANGE = 4;
  static final int ANCHOR = 5;
  static final int STRING = 6;
  static final int CLOSURE = 7;
  static final int NONGREEDYCLOSURE = 8;
  static final int QUESTION = 9;
  static final int NONGREEDYQUESTION = 10;
  static final int UNION = 11;
  static final int CAPTURE = 15;
  static final int BACKREFERENCE = 16;
  static final int LOOKAHEAD = 20;
  static final int NEGATIVELOOKAHEAD = 21;
  static final int LOOKBEHIND = 22;
  static final int NEGATIVELOOKBEHIND = 23;
  static final int INDEPENDENT = 24;
  static final int MODIFIER = 25;
  static final int CONDITION = 26;
  static int nofinstances = 0;
  static final boolean COUNT = false;
  final int type;
  Op next = null;
  
  static Op createDot()
  {
    return new Op(0);
  }
  
  static CharOp createChar(int paramInt)
  {
    return new CharOp(1, paramInt);
  }
  
  static CharOp createAnchor(int paramInt)
  {
    return new CharOp(5, paramInt);
  }
  
  static CharOp createCapture(int paramInt, Op paramOp)
  {
    CharOp localCharOp = new CharOp(15, paramInt);
    next = paramOp;
    return localCharOp;
  }
  
  static UnionOp createUnion(int paramInt)
  {
    return new UnionOp(11, paramInt);
  }
  
  static ChildOp createClosure(int paramInt)
  {
    return new ModifierOp(7, paramInt, -1);
  }
  
  static ChildOp createNonGreedyClosure()
  {
    return new ChildOp(8);
  }
  
  static ChildOp createQuestion(boolean paramBoolean)
  {
    return new ChildOp(paramBoolean ? 10 : 9);
  }
  
  static RangeOp createRange(Token paramToken)
  {
    return new RangeOp(3, paramToken);
  }
  
  static ChildOp createLook(int paramInt, Op paramOp1, Op paramOp2)
  {
    ChildOp localChildOp = new ChildOp(paramInt);
    localChildOp.setChild(paramOp2);
    next = paramOp1;
    return localChildOp;
  }
  
  static CharOp createBackReference(int paramInt)
  {
    return new CharOp(16, paramInt);
  }
  
  static StringOp createString(String paramString)
  {
    return new StringOp(6, paramString);
  }
  
  static ChildOp createIndependent(Op paramOp1, Op paramOp2)
  {
    ChildOp localChildOp = new ChildOp(24);
    localChildOp.setChild(paramOp2);
    next = paramOp1;
    return localChildOp;
  }
  
  static ModifierOp createModifier(Op paramOp1, Op paramOp2, int paramInt1, int paramInt2)
  {
    ModifierOp localModifierOp = new ModifierOp(25, paramInt1, paramInt2);
    localModifierOp.setChild(paramOp2);
    next = paramOp1;
    return localModifierOp;
  }
  
  static ConditionOp createCondition(Op paramOp1, int paramInt, Op paramOp2, Op paramOp3, Op paramOp4)
  {
    ConditionOp localConditionOp = new ConditionOp(26, paramInt, paramOp2, paramOp3, paramOp4);
    next = paramOp1;
    return localConditionOp;
  }
  
  protected Op(int paramInt)
  {
    type = paramInt;
  }
  
  int size()
  {
    return 0;
  }
  
  Op elementAt(int paramInt)
  {
    throw new RuntimeException("Internal Error: type=" + type);
  }
  
  Op getChild()
  {
    throw new RuntimeException("Internal Error: type=" + type);
  }
  
  int getData()
  {
    throw new RuntimeException("Internal Error: type=" + type);
  }
  
  int getData2()
  {
    throw new RuntimeException("Internal Error: type=" + type);
  }
  
  RangeToken getToken()
  {
    throw new RuntimeException("Internal Error: type=" + type);
  }
  
  String getString()
  {
    throw new RuntimeException("Internal Error: type=" + type);
  }
  
  static class ConditionOp
    extends Op
  {
    final int refNumber;
    final Op condition;
    final Op yes;
    final Op no;
    
    ConditionOp(int paramInt1, int paramInt2, Op paramOp1, Op paramOp2, Op paramOp3)
    {
      super();
      refNumber = paramInt2;
      condition = paramOp1;
      yes = paramOp2;
      no = paramOp3;
    }
  }
  
  static class StringOp
    extends Op
  {
    final String string;
    
    StringOp(int paramInt, String paramString)
    {
      super();
      string = paramString;
    }
    
    String getString()
    {
      return string;
    }
  }
  
  static class RangeOp
    extends Op
  {
    final Token tok;
    
    RangeOp(int paramInt, Token paramToken)
    {
      super();
      tok = paramToken;
    }
    
    RangeToken getToken()
    {
      return (RangeToken)tok;
    }
  }
  
  static class ModifierOp
    extends Op.ChildOp
  {
    final int v1;
    final int v2;
    
    ModifierOp(int paramInt1, int paramInt2, int paramInt3)
    {
      super();
      v1 = paramInt2;
      v2 = paramInt3;
    }
    
    int getData()
    {
      return v1;
    }
    
    int getData2()
    {
      return v2;
    }
  }
  
  static class ChildOp
    extends Op
  {
    Op child;
    
    ChildOp(int paramInt)
    {
      super();
    }
    
    void setChild(Op paramOp)
    {
      child = paramOp;
    }
    
    Op getChild()
    {
      return child;
    }
  }
  
  static class UnionOp
    extends Op
  {
    final Vector branches;
    
    UnionOp(int paramInt1, int paramInt2)
    {
      super();
      branches = new Vector(paramInt2);
    }
    
    void addElement(Op paramOp)
    {
      branches.addElement(paramOp);
    }
    
    int size()
    {
      return branches.size();
    }
    
    Op elementAt(int paramInt)
    {
      return (Op)branches.elementAt(paramInt);
    }
  }
  
  static class CharOp
    extends Op
  {
    final int charData;
    
    CharOp(int paramInt1, int paramInt2)
    {
      super();
      charData = paramInt2;
    }
    
    int getData()
    {
      return charData;
    }
  }
}
