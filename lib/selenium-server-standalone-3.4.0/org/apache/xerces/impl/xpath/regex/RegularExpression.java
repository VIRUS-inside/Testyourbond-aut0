package org.apache.xerces.impl.xpath.regex;

import java.io.Serializable;
import java.text.CharacterIterator;
import java.util.Locale;
import java.util.Stack;
import org.apache.xerces.util.IntStack;

public class RegularExpression
  implements Serializable
{
  private static final long serialVersionUID = 6242499334195006401L;
  static final boolean DEBUG = false;
  String regex;
  int options;
  int nofparen;
  Token tokentree;
  boolean hasBackReferences = false;
  transient int minlength;
  transient Op operations = null;
  transient int numberOfClosures;
  transient Context context = null;
  transient RangeToken firstChar = null;
  transient String fixedString = null;
  transient int fixedStringOptions;
  transient BMPattern fixedStringTable = null;
  transient boolean fixedStringOnly = false;
  static final int IGNORE_CASE = 2;
  static final int SINGLE_LINE = 4;
  static final int MULTIPLE_LINES = 8;
  static final int EXTENDED_COMMENT = 16;
  static final int USE_UNICODE_CATEGORY = 32;
  static final int UNICODE_WORD_BOUNDARY = 64;
  static final int PROHIBIT_HEAD_CHARACTER_OPTIMIZATION = 128;
  static final int PROHIBIT_FIXED_STRING_OPTIMIZATION = 256;
  static final int XMLSCHEMA_MODE = 512;
  static final int SPECIAL_COMMA = 1024;
  private static final int WT_IGNORE = 0;
  private static final int WT_LETTER = 1;
  private static final int WT_OTHER = 2;
  static final int LINE_FEED = 10;
  static final int CARRIAGE_RETURN = 13;
  static final int LINE_SEPARATOR = 8232;
  static final int PARAGRAPH_SEPARATOR = 8233;
  
  private synchronized void compile(Token paramToken)
  {
    if (operations != null) {
      return;
    }
    numberOfClosures = 0;
    operations = compile(paramToken, null, false);
  }
  
  private Op compile(Token paramToken, Op paramOp, boolean paramBoolean)
  {
    Object localObject;
    switch (type)
    {
    case 11: 
      localObject = Op.createDot();
      next = paramOp;
      break;
    case 0: 
      localObject = Op.createChar(paramToken.getChar());
      next = paramOp;
      break;
    case 8: 
      localObject = Op.createAnchor(paramToken.getChar());
      next = paramOp;
      break;
    case 4: 
    case 5: 
      localObject = Op.createRange(paramToken);
      next = paramOp;
      break;
    case 1: 
      localObject = paramOp;
      int i;
      if (!paramBoolean) {
        for (i = paramToken.size() - 1; i >= 0; i--) {
          localObject = compile(paramToken.getChild(i), (Op)localObject, false);
        }
      } else {
        for (i = 0; i < paramToken.size(); i++) {
          localObject = compile(paramToken.getChild(i), (Op)localObject, true);
        }
      }
      break;
    case 2: 
      Op.UnionOp localUnionOp = Op.createUnion(paramToken.size());
      for (int j = 0; j < paramToken.size(); j++) {
        localUnionOp.addElement(compile(paramToken.getChild(j), paramOp, paramBoolean));
      }
      localObject = localUnionOp;
      break;
    case 3: 
    case 9: 
      Token localToken = paramToken.getChild(0);
      int k = paramToken.getMin();
      int m = paramToken.getMax();
      int n;
      if ((k >= 0) && (k == m))
      {
        localObject = paramOp;
        for (n = 0; n < k; n++) {
          localObject = compile(localToken, (Op)localObject, paramBoolean);
        }
      }
      else
      {
        if ((k > 0) && (m > 0)) {
          m -= k;
        }
        if (m > 0)
        {
          localObject = paramOp;
          for (n = 0; n < m; n++)
          {
            Op.ChildOp localChildOp2 = Op.createQuestion(type == 9);
            next = paramOp;
            localChildOp2.setChild(compile(localToken, (Op)localObject, paramBoolean));
            localObject = localChildOp2;
          }
        }
        else
        {
          Op.ChildOp localChildOp1;
          if (type == 9) {
            localChildOp1 = Op.createNonGreedyClosure();
          } else {
            localChildOp1 = Op.createClosure(numberOfClosures++);
          }
          next = paramOp;
          localChildOp1.setChild(compile(localToken, localChildOp1, paramBoolean));
          localObject = localChildOp1;
        }
        if (k > 0) {
          for (int i1 = 0; i1 < k; i1++) {
            localObject = compile(localToken, (Op)localObject, paramBoolean);
          }
        }
      }
      break;
    case 7: 
      localObject = paramOp;
      break;
    case 10: 
      localObject = Op.createString(paramToken.getString());
      next = paramOp;
      break;
    case 12: 
      localObject = Op.createBackReference(paramToken.getReferenceNumber());
      next = paramOp;
      break;
    case 6: 
      if (paramToken.getParenNumber() == 0)
      {
        localObject = compile(paramToken.getChild(0), paramOp, paramBoolean);
      }
      else if (paramBoolean)
      {
        paramOp = Op.createCapture(paramToken.getParenNumber(), paramOp);
        paramOp = compile(paramToken.getChild(0), paramOp, paramBoolean);
        localObject = Op.createCapture(-paramToken.getParenNumber(), paramOp);
      }
      else
      {
        paramOp = Op.createCapture(-paramToken.getParenNumber(), paramOp);
        paramOp = compile(paramToken.getChild(0), paramOp, paramBoolean);
        localObject = Op.createCapture(paramToken.getParenNumber(), paramOp);
      }
      break;
    case 20: 
      localObject = Op.createLook(20, paramOp, compile(paramToken.getChild(0), null, false));
      break;
    case 21: 
      localObject = Op.createLook(21, paramOp, compile(paramToken.getChild(0), null, false));
      break;
    case 22: 
      localObject = Op.createLook(22, paramOp, compile(paramToken.getChild(0), null, true));
      break;
    case 23: 
      localObject = Op.createLook(23, paramOp, compile(paramToken.getChild(0), null, true));
      break;
    case 24: 
      localObject = Op.createIndependent(paramOp, compile(paramToken.getChild(0), null, paramBoolean));
      break;
    case 25: 
      localObject = Op.createModifier(paramOp, compile(paramToken.getChild(0), null, paramBoolean), ((Token.ModifierToken)paramToken).getOptions(), ((Token.ModifierToken)paramToken).getOptionsMask());
      break;
    case 26: 
      Token.ConditionToken localConditionToken = (Token.ConditionToken)paramToken;
      int i2 = refNumber;
      Op localOp1 = condition == null ? null : compile(condition, null, paramBoolean);
      Op localOp2 = compile(yes, paramOp, paramBoolean);
      Op localOp3 = no == null ? null : compile(no, paramOp, paramBoolean);
      localObject = Op.createCondition(paramOp, i2, localOp1, localOp2, localOp3);
      break;
    case 13: 
    case 14: 
    case 15: 
    case 16: 
    case 17: 
    case 18: 
    case 19: 
    default: 
      throw new RuntimeException("Unknown token type: " + type);
    }
    return localObject;
  }
  
  public boolean matches(char[] paramArrayOfChar)
  {
    return matches(paramArrayOfChar, 0, paramArrayOfChar.length, (Match)null);
  }
  
  public boolean matches(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    return matches(paramArrayOfChar, paramInt1, paramInt2, (Match)null);
  }
  
  public boolean matches(char[] paramArrayOfChar, Match paramMatch)
  {
    return matches(paramArrayOfChar, 0, paramArrayOfChar.length, paramMatch);
  }
  
  public boolean matches(char[] paramArrayOfChar, int paramInt1, int paramInt2, Match paramMatch)
  {
    synchronized (this)
    {
      if (operations == null) {
        prepare();
      }
      if (context == null) {
        context = new Context();
      }
    }
    Context localContext1 = null;
    synchronized (context)
    {
      localContext1 = context.inuse ? new Context() : context;
      localContext1.reset(paramArrayOfChar, paramInt1, paramInt2, numberOfClosures);
    }
    if (paramMatch != null)
    {
      paramMatch.setNumberOfGroups(nofparen);
      paramMatch.setSource(paramArrayOfChar);
    }
    else if (hasBackReferences)
    {
      paramMatch = new Match();
      paramMatch.setNumberOfGroups(nofparen);
    }
    match = paramMatch;
    if (isSet(options, 512))
    {
      i = match(localContext1, operations, start, 1, options);
      if (i == limit)
      {
        if (match != null)
        {
          match.setBeginning(0, start);
          match.setEnd(0, i);
        }
        localContext1.setInUse(false);
        return true;
      }
      return false;
    }
    if (fixedStringOnly)
    {
      i = fixedStringTable.matches(paramArrayOfChar, start, limit);
      if (i >= 0)
      {
        if (match != null)
        {
          match.setBeginning(0, i);
          match.setEnd(0, i + fixedString.length());
        }
        localContext1.setInUse(false);
        return true;
      }
      localContext1.setInUse(false);
      return false;
    }
    if (fixedString != null)
    {
      i = fixedStringTable.matches(paramArrayOfChar, start, limit);
      if (i < 0)
      {
        localContext1.setInUse(false);
        return false;
      }
    }
    int i = limit - minlength;
    int k = -1;
    int j;
    int n;
    if ((operations != null) && (operations.type == 7) && (operations.getChild().type == 0))
    {
      if (isSet(options, 4))
      {
        j = start;
        k = match(localContext1, operations, start, 1, options);
      }
      else
      {
        int m = 1;
        for (j = start; j <= i; j++)
        {
          n = paramArrayOfChar[j];
          if (isEOLChar(n))
          {
            m = 1;
          }
          else
          {
            if ((m != 0) && (0 <= (k = match(localContext1, operations, j, 1, options)))) {
              break;
            }
            m = 0;
          }
        }
      }
    }
    else if (firstChar != null)
    {
      RangeToken localRangeToken = firstChar;
      for (j = start; j <= i; j++)
      {
        n = paramArrayOfChar[j];
        if ((REUtil.isHighSurrogate(n)) && (j + 1 < limit)) {
          n = REUtil.composeFromSurrogates(n, paramArrayOfChar[(j + 1)]);
        }
        if ((localRangeToken.match(n)) && (0 <= (k = match(localContext1, operations, j, 1, options)))) {
          break;
        }
      }
    }
    else
    {
      for (j = start; j <= i; j++) {
        if (0 <= (k = match(localContext1, operations, j, 1, options))) {
          break;
        }
      }
    }
    if (k >= 0)
    {
      if (match != null)
      {
        match.setBeginning(0, j);
        match.setEnd(0, k);
      }
      localContext1.setInUse(false);
      return true;
    }
    localContext1.setInUse(false);
    return false;
  }
  
  public boolean matches(String paramString)
  {
    return matches(paramString, 0, paramString.length(), (Match)null);
  }
  
  public boolean matches(String paramString, int paramInt1, int paramInt2)
  {
    return matches(paramString, paramInt1, paramInt2, (Match)null);
  }
  
  public boolean matches(String paramString, Match paramMatch)
  {
    return matches(paramString, 0, paramString.length(), paramMatch);
  }
  
  public boolean matches(String paramString, int paramInt1, int paramInt2, Match paramMatch)
  {
    synchronized (this)
    {
      if (operations == null) {
        prepare();
      }
      if (context == null) {
        context = new Context();
      }
    }
    Context localContext1 = null;
    synchronized (context)
    {
      localContext1 = context.inuse ? new Context() : context;
      localContext1.reset(paramString, paramInt1, paramInt2, numberOfClosures);
    }
    if (paramMatch != null)
    {
      paramMatch.setNumberOfGroups(nofparen);
      paramMatch.setSource(paramString);
    }
    else if (hasBackReferences)
    {
      paramMatch = new Match();
      paramMatch.setNumberOfGroups(nofparen);
    }
    match = paramMatch;
    if (isSet(options, 512))
    {
      i = match(localContext1, operations, start, 1, options);
      if (i == limit)
      {
        if (match != null)
        {
          match.setBeginning(0, start);
          match.setEnd(0, i);
        }
        localContext1.setInUse(false);
        return true;
      }
      return false;
    }
    if (fixedStringOnly)
    {
      i = fixedStringTable.matches(paramString, start, limit);
      if (i >= 0)
      {
        if (match != null)
        {
          match.setBeginning(0, i);
          match.setEnd(0, i + fixedString.length());
        }
        localContext1.setInUse(false);
        return true;
      }
      localContext1.setInUse(false);
      return false;
    }
    if (fixedString != null)
    {
      i = fixedStringTable.matches(paramString, start, limit);
      if (i < 0)
      {
        localContext1.setInUse(false);
        return false;
      }
    }
    int i = limit - minlength;
    int k = -1;
    int j;
    int n;
    if ((operations != null) && (operations.type == 7) && (operations.getChild().type == 0))
    {
      if (isSet(options, 4))
      {
        j = start;
        k = match(localContext1, operations, start, 1, options);
      }
      else
      {
        int m = 1;
        for (j = start; j <= i; j++)
        {
          n = paramString.charAt(j);
          if (isEOLChar(n))
          {
            m = 1;
          }
          else
          {
            if ((m != 0) && (0 <= (k = match(localContext1, operations, j, 1, options)))) {
              break;
            }
            m = 0;
          }
        }
      }
    }
    else if (firstChar != null)
    {
      RangeToken localRangeToken = firstChar;
      for (j = start; j <= i; j++)
      {
        n = paramString.charAt(j);
        if ((REUtil.isHighSurrogate(n)) && (j + 1 < limit)) {
          n = REUtil.composeFromSurrogates(n, paramString.charAt(j + 1));
        }
        if ((localRangeToken.match(n)) && (0 <= (k = match(localContext1, operations, j, 1, options)))) {
          break;
        }
      }
    }
    else
    {
      for (j = start; j <= i; j++) {
        if (0 <= (k = match(localContext1, operations, j, 1, options))) {
          break;
        }
      }
    }
    if (k >= 0)
    {
      if (match != null)
      {
        match.setBeginning(0, j);
        match.setEnd(0, k);
      }
      localContext1.setInUse(false);
      return true;
    }
    localContext1.setInUse(false);
    return false;
  }
  
  private int match(Context paramContext, Op paramOp, int paramInt1, int paramInt2, int paramInt3)
  {
    ExpressionTarget localExpressionTarget = target;
    Stack localStack = new Stack();
    IntStack localIntStack = new IntStack();
    boolean bool = isSet(paramInt3, 2);
    int i = -1;
    int j = 0;
    for (;;)
    {
      int i1;
      if ((paramOp == null) || (paramInt1 > limit) || (paramInt1 < start))
      {
        if (paramOp == null) {
          i = (isSet(paramInt3, 512)) && (paramInt1 != limit) ? -1 : paramInt1;
        } else {
          i = -1;
        }
        j = 1;
      }
      else
      {
        i = -1;
        int k;
        int m;
        switch (type)
        {
        case 1: 
          k = paramInt2 > 0 ? paramInt1 : paramInt1 - 1;
          if ((k >= limit) || (k < 0) || (!matchChar(paramOp.getData(), localExpressionTarget.charAt(k), bool)))
          {
            j = 1;
          }
          else
          {
            paramInt1 += paramInt2;
            paramOp = next;
          }
          break;
        case 0: 
          k = paramInt2 > 0 ? paramInt1 : paramInt1 - 1;
          if ((k >= limit) || (k < 0))
          {
            j = 1;
          }
          else
          {
            if (isSet(paramInt3, 4))
            {
              if ((REUtil.isHighSurrogate(localExpressionTarget.charAt(k))) && (k + paramInt2 >= 0) && (k + paramInt2 < limit)) {
                k += paramInt2;
              }
            }
            else
            {
              i1 = localExpressionTarget.charAt(k);
              if ((REUtil.isHighSurrogate(i1)) && (k + paramInt2 >= 0) && (k + paramInt2 < limit))
              {
                k += paramInt2;
                i1 = REUtil.composeFromSurrogates(i1, localExpressionTarget.charAt(k));
              }
              if (isEOLChar(i1))
              {
                j = 1;
                break;
              }
            }
            paramInt1 = paramInt2 > 0 ? k + 1 : k;
            paramOp = next;
          }
          break;
        case 3: 
        case 4: 
          k = paramInt2 > 0 ? paramInt1 : paramInt1 - 1;
          if ((k >= limit) || (k < 0))
          {
            j = 1;
          }
          else
          {
            i1 = localExpressionTarget.charAt(paramInt1);
            if ((REUtil.isHighSurrogate(i1)) && (k + paramInt2 < limit) && (k + paramInt2 >= 0))
            {
              k += paramInt2;
              i1 = REUtil.composeFromSurrogates(i1, localExpressionTarget.charAt(k));
            }
            RangeToken localRangeToken = paramOp.getToken();
            if (!localRangeToken.match(i1))
            {
              j = 1;
            }
            else
            {
              paramInt1 = paramInt2 > 0 ? k + 1 : k;
              paramOp = next;
            }
          }
          break;
        case 5: 
          if (!matchAnchor(localExpressionTarget, paramOp, paramContext, paramInt1, paramInt3)) {
            j = 1;
          } else {
            paramOp = next;
          }
          break;
        case 16: 
          k = paramOp.getData();
          if ((k <= 0) || (k >= nofparen)) {
            throw new RuntimeException("Internal Error: Reference number must be more than zero: " + k);
          }
          if ((match.getBeginning(k) < 0) || (match.getEnd(k) < 0))
          {
            j = 1;
          }
          else
          {
            i1 = match.getBeginning(k);
            int i2 = match.getEnd(k) - i1;
            if (paramInt2 > 0)
            {
              if (!localExpressionTarget.regionMatches(bool, paramInt1, limit, i1, i2))
              {
                j = 1;
                break;
              }
              paramInt1 += i2;
            }
            else
            {
              if (!localExpressionTarget.regionMatches(bool, paramInt1 - i2, limit, i1, i2))
              {
                j = 1;
                break;
              }
              paramInt1 -= i2;
            }
            paramOp = next;
          }
          break;
        case 6: 
          String str = paramOp.getString();
          i1 = str.length();
          if (paramInt2 > 0)
          {
            if (!localExpressionTarget.regionMatches(bool, paramInt1, limit, str, i1))
            {
              j = 1;
              break;
            }
            paramInt1 += i1;
          }
          else
          {
            if (!localExpressionTarget.regionMatches(bool, paramInt1 - i1, limit, str, i1))
            {
              j = 1;
              break;
            }
            paramInt1 -= i1;
          }
          paramOp = next;
          break;
        case 7: 
          m = paramOp.getData();
          if (closureContexts[m].contains(paramInt1)) {
            j = 1;
          } else {
            closureContexts[m].addOffset(paramInt1);
          }
          break;
        case 9: 
          localStack.push(paramOp);
          localIntStack.push(paramInt1);
          paramOp = paramOp.getChild();
          break;
        case 8: 
        case 10: 
          localStack.push(paramOp);
          localIntStack.push(paramInt1);
          paramOp = next;
          break;
        case 11: 
          if (paramOp.size() == 0)
          {
            j = 1;
          }
          else
          {
            localStack.push(paramOp);
            localIntStack.push(0);
            localIntStack.push(paramInt1);
            paramOp = paramOp.elementAt(0);
          }
          break;
        case 15: 
          m = paramOp.getData();
          if (match != null)
          {
            if (m > 0)
            {
              localIntStack.push(match.getBeginning(m));
              match.setBeginning(m, paramInt1);
            }
            else
            {
              i1 = -m;
              localIntStack.push(match.getEnd(i1));
              match.setEnd(i1, paramInt1);
            }
            localStack.push(paramOp);
            localIntStack.push(paramInt1);
          }
          paramOp = next;
          break;
        case 20: 
        case 21: 
        case 22: 
        case 23: 
          localStack.push(paramOp);
          localIntStack.push(paramInt2);
          localIntStack.push(paramInt1);
          paramInt2 = (type == 20) || (type == 21) ? 1 : -1;
          paramOp = paramOp.getChild();
          break;
        case 24: 
          localStack.push(paramOp);
          localIntStack.push(paramInt1);
          paramOp = paramOp.getChild();
          break;
        case 25: 
          m = paramInt3;
          m |= paramOp.getData();
          m &= (paramOp.getData2() ^ 0xFFFFFFFF);
          localStack.push(paramOp);
          localIntStack.push(paramInt3);
          localIntStack.push(paramInt1);
          paramInt3 = m;
          paramOp = paramOp.getChild();
          break;
        case 26: 
          Op.ConditionOp localConditionOp1 = (Op.ConditionOp)paramOp;
          if (refNumber > 0)
          {
            if (refNumber >= nofparen) {
              throw new RuntimeException("Internal Error: Reference number must be more than zero: " + refNumber);
            }
            if ((match.getBeginning(refNumber) >= 0) && (match.getEnd(refNumber) >= 0)) {
              paramOp = yes;
            } else if (no != null) {
              paramOp = no;
            } else {
              paramOp = next;
            }
          }
          else
          {
            localStack.push(paramOp);
            localIntStack.push(paramInt1);
            paramOp = condition;
          }
          break;
        }
      }
      while (j != 0)
      {
        if (localStack.isEmpty()) {
          return i;
        }
        paramOp = (Op)localStack.pop();
        paramInt1 = localIntStack.pop();
        int n;
        switch (type)
        {
        case 7: 
        case 9: 
          if (i < 0)
          {
            paramOp = next;
            j = 0;
          }
          break;
        case 8: 
        case 10: 
          if (i < 0)
          {
            paramOp = paramOp.getChild();
            j = 0;
          }
          break;
        case 11: 
          n = localIntStack.pop();
          if (i < 0)
          {
            n++;
            if (n < paramOp.size())
            {
              localStack.push(paramOp);
              localIntStack.push(n);
              localIntStack.push(paramInt1);
              paramOp = paramOp.elementAt(n);
              j = 0;
            }
            else
            {
              i = -1;
            }
          }
          break;
        case 15: 
          n = paramOp.getData();
          i1 = localIntStack.pop();
          if (i < 0) {
            if (n > 0) {
              match.setBeginning(n, i1);
            } else {
              match.setEnd(-n, i1);
            }
          }
          break;
        case 20: 
        case 22: 
          paramInt2 = localIntStack.pop();
          if (0 <= i)
          {
            paramOp = next;
            j = 0;
          }
          i = -1;
          break;
        case 21: 
        case 23: 
          paramInt2 = localIntStack.pop();
          if (0 > i)
          {
            paramOp = next;
            j = 0;
          }
          i = -1;
          break;
        case 25: 
          paramInt3 = localIntStack.pop();
        case 24: 
          if (i >= 0)
          {
            paramInt1 = i;
            paramOp = next;
            j = 0;
          }
          break;
        case 26: 
          Op.ConditionOp localConditionOp2 = (Op.ConditionOp)paramOp;
          if (0 <= i) {
            paramOp = yes;
          } else if (no != null) {
            paramOp = no;
          } else {
            paramOp = next;
          }
          j = 0;
        }
      }
    }
  }
  
  private boolean matchChar(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    return paramInt1 == paramInt2 ? true : paramBoolean ? matchIgnoreCase(paramInt1, paramInt2) : false;
  }
  
  boolean matchAnchor(ExpressionTarget paramExpressionTarget, Op paramOp, Context paramContext, int paramInt1, int paramInt2)
  {
    int i = 0;
    int j;
    switch (paramOp.getData())
    {
    case 94: 
      if (isSet(paramInt2, 8))
      {
        if ((paramInt1 != start) && ((paramInt1 <= start) || (paramInt1 >= limit) || (!isEOLChar(paramExpressionTarget.charAt(paramInt1 - 1))))) {
          return false;
        }
      }
      else if (paramInt1 != start) {
        return false;
      }
      break;
    case 64: 
      if ((paramInt1 != start) && ((paramInt1 <= start) || (!isEOLChar(paramExpressionTarget.charAt(paramInt1 - 1))))) {
        return false;
      }
      break;
    case 36: 
      if (isSet(paramInt2, 8))
      {
        if ((paramInt1 != limit) && ((paramInt1 >= limit) || (!isEOLChar(paramExpressionTarget.charAt(paramInt1))))) {
          return false;
        }
      }
      else if ((paramInt1 != limit) && ((paramInt1 + 1 != limit) || (!isEOLChar(paramExpressionTarget.charAt(paramInt1)))) && ((paramInt1 + 2 != limit) || (paramExpressionTarget.charAt(paramInt1) != '\r') || (paramExpressionTarget.charAt(paramInt1 + 1) != '\n'))) {
        return false;
      }
      break;
    case 65: 
      if (paramInt1 != start) {
        return false;
      }
      break;
    case 90: 
      if ((paramInt1 != limit) && ((paramInt1 + 1 != limit) || (!isEOLChar(paramExpressionTarget.charAt(paramInt1)))) && ((paramInt1 + 2 != limit) || (paramExpressionTarget.charAt(paramInt1) != '\r') || (paramExpressionTarget.charAt(paramInt1 + 1) != '\n'))) {
        return false;
      }
      break;
    case 122: 
      if (paramInt1 != limit) {
        return false;
      }
      break;
    case 98: 
      if (length == 0) {
        return false;
      }
      j = getWordType(paramExpressionTarget, start, limit, paramInt1, paramInt2);
      if (j == 0) {
        return false;
      }
      int k = getPreviousWordType(paramExpressionTarget, start, limit, paramInt1, paramInt2);
      if (j == k) {
        return false;
      }
      break;
    case 66: 
      if (length == 0)
      {
        i = 1;
      }
      else
      {
        j = getWordType(paramExpressionTarget, start, limit, paramInt1, paramInt2);
        i = (j == 0) || (j == getPreviousWordType(paramExpressionTarget, start, limit, paramInt1, paramInt2)) ? 1 : 0;
      }
      if (i == 0) {
        return false;
      }
      break;
    case 60: 
      if ((length == 0) || (paramInt1 == limit)) {
        return false;
      }
      if ((getWordType(paramExpressionTarget, start, limit, paramInt1, paramInt2) != 1) || (getPreviousWordType(paramExpressionTarget, start, limit, paramInt1, paramInt2) != 2)) {
        return false;
      }
      break;
    case 62: 
      if ((length == 0) || (paramInt1 == start)) {
        return false;
      }
      if ((getWordType(paramExpressionTarget, start, limit, paramInt1, paramInt2) != 2) || (getPreviousWordType(paramExpressionTarget, start, limit, paramInt1, paramInt2) != 1)) {
        return false;
      }
      break;
    }
    return true;
  }
  
  private static final int getPreviousWordType(ExpressionTarget paramExpressionTarget, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    for (int i = getWordType(paramExpressionTarget, paramInt1, paramInt2, --paramInt3, paramInt4); i == 0; i = getWordType(paramExpressionTarget, paramInt1, paramInt2, --paramInt3, paramInt4)) {}
    return i;
  }
  
  private static final int getWordType(ExpressionTarget paramExpressionTarget, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt3 < paramInt1) || (paramInt3 >= paramInt2)) {
      return 2;
    }
    return getWordType0(paramExpressionTarget.charAt(paramInt3), paramInt4);
  }
  
  public boolean matches(CharacterIterator paramCharacterIterator)
  {
    return matches(paramCharacterIterator, (Match)null);
  }
  
  public boolean matches(CharacterIterator paramCharacterIterator, Match paramMatch)
  {
    int i = paramCharacterIterator.getBeginIndex();
    int j = paramCharacterIterator.getEndIndex();
    synchronized (this)
    {
      if (operations == null) {
        prepare();
      }
      if (context == null) {
        context = new Context();
      }
    }
    Context localContext1 = null;
    synchronized (context)
    {
      localContext1 = context.inuse ? new Context() : context;
      localContext1.reset(paramCharacterIterator, i, j, numberOfClosures);
    }
    if (paramMatch != null)
    {
      paramMatch.setNumberOfGroups(nofparen);
      paramMatch.setSource(paramCharacterIterator);
    }
    else if (hasBackReferences)
    {
      paramMatch = new Match();
      paramMatch.setNumberOfGroups(nofparen);
    }
    match = paramMatch;
    if (isSet(options, 512))
    {
      k = match(localContext1, operations, start, 1, options);
      if (k == limit)
      {
        if (match != null)
        {
          match.setBeginning(0, start);
          match.setEnd(0, k);
        }
        localContext1.setInUse(false);
        return true;
      }
      return false;
    }
    if (fixedStringOnly)
    {
      k = fixedStringTable.matches(paramCharacterIterator, start, limit);
      if (k >= 0)
      {
        if (match != null)
        {
          match.setBeginning(0, k);
          match.setEnd(0, k + fixedString.length());
        }
        localContext1.setInUse(false);
        return true;
      }
      localContext1.setInUse(false);
      return false;
    }
    if (fixedString != null)
    {
      k = fixedStringTable.matches(paramCharacterIterator, start, limit);
      if (k < 0)
      {
        localContext1.setInUse(false);
        return false;
      }
    }
    int k = limit - minlength;
    int n = -1;
    int m;
    int i2;
    if ((operations != null) && (operations.type == 7) && (operations.getChild().type == 0))
    {
      if (isSet(options, 4))
      {
        m = start;
        n = match(localContext1, operations, start, 1, options);
      }
      else
      {
        int i1 = 1;
        for (m = start; m <= k; m++)
        {
          i2 = paramCharacterIterator.setIndex(m);
          if (isEOLChar(i2))
          {
            i1 = 1;
          }
          else
          {
            if ((i1 != 0) && (0 <= (n = match(localContext1, operations, m, 1, options)))) {
              break;
            }
            i1 = 0;
          }
        }
      }
    }
    else if (firstChar != null)
    {
      RangeToken localRangeToken = firstChar;
      for (m = start; m <= k; m++)
      {
        i2 = paramCharacterIterator.setIndex(m);
        if ((REUtil.isHighSurrogate(i2)) && (m + 1 < limit)) {
          i2 = REUtil.composeFromSurrogates(i2, paramCharacterIterator.setIndex(m + 1));
        }
        if ((localRangeToken.match(i2)) && (0 <= (n = match(localContext1, operations, m, 1, options)))) {
          break;
        }
      }
    }
    else
    {
      for (m = start; m <= k; m++) {
        if (0 <= (n = match(localContext1, operations, m, 1, options))) {
          break;
        }
      }
    }
    if (n >= 0)
    {
      if (match != null)
      {
        match.setBeginning(0, m);
        match.setEnd(0, n);
      }
      localContext1.setInUse(false);
      return true;
    }
    localContext1.setInUse(false);
    return false;
  }
  
  void prepare()
  {
    compile(tokentree);
    minlength = tokentree.getMinLength();
    firstChar = null;
    Object localObject;
    if ((!isSet(options, 128)) && (!isSet(options, 512)))
    {
      localObject = Token.createRange();
      int i = tokentree.analyzeFirstCharacter((RangeToken)localObject, options);
      if (i == 1)
      {
        ((RangeToken)localObject).compactRanges();
        firstChar = ((RangeToken)localObject);
      }
    }
    if ((operations != null) && ((operations.type == 6) || (operations.type == 1)) && (operations.next == null))
    {
      fixedStringOnly = true;
      if (operations.type == 6)
      {
        fixedString = operations.getString();
      }
      else if (operations.getData() >= 65536)
      {
        fixedString = REUtil.decomposeToSurrogates(operations.getData());
      }
      else
      {
        localObject = new char[1];
        localObject[0] = ((char)operations.getData());
        fixedString = new String((char[])localObject);
      }
      fixedStringOptions = options;
      fixedStringTable = new BMPattern(fixedString, 256, isSet(fixedStringOptions, 2));
    }
    else if ((!isSet(options, 256)) && (!isSet(options, 512)))
    {
      localObject = new Token.FixedStringContainer();
      tokentree.findFixedString((Token.FixedStringContainer)localObject, options);
      fixedString = (token == null ? null : token.getString());
      fixedStringOptions = options;
      if ((fixedString != null) && (fixedString.length() < 2)) {
        fixedString = null;
      }
      if (fixedString != null) {
        fixedStringTable = new BMPattern(fixedString, 256, isSet(fixedStringOptions, 2));
      }
    }
  }
  
  private static final boolean isSet(int paramInt1, int paramInt2)
  {
    return (paramInt1 & paramInt2) == paramInt2;
  }
  
  public RegularExpression(String paramString)
    throws ParseException
  {
    this(paramString, null);
  }
  
  public RegularExpression(String paramString1, String paramString2)
    throws ParseException
  {
    setPattern(paramString1, paramString2);
  }
  
  public RegularExpression(String paramString1, String paramString2, Locale paramLocale)
    throws ParseException
  {
    setPattern(paramString1, paramString2, paramLocale);
  }
  
  RegularExpression(String paramString, Token paramToken, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    regex = paramString;
    tokentree = paramToken;
    nofparen = paramInt1;
    options = paramInt2;
    hasBackReferences = paramBoolean;
  }
  
  public void setPattern(String paramString)
    throws ParseException
  {
    setPattern(paramString, Locale.getDefault());
  }
  
  public void setPattern(String paramString, Locale paramLocale)
    throws ParseException
  {
    setPattern(paramString, options, paramLocale);
  }
  
  private void setPattern(String paramString, int paramInt, Locale paramLocale)
    throws ParseException
  {
    regex = paramString;
    options = paramInt;
    RegexParser localRegexParser = isSet(options, 512) ? new ParserForXMLSchema(paramLocale) : new RegexParser(paramLocale);
    tokentree = localRegexParser.parse(regex, options);
    nofparen = parennumber;
    hasBackReferences = hasBackReferences;
    operations = null;
    context = null;
  }
  
  public void setPattern(String paramString1, String paramString2)
    throws ParseException
  {
    setPattern(paramString1, paramString2, Locale.getDefault());
  }
  
  public void setPattern(String paramString1, String paramString2, Locale paramLocale)
    throws ParseException
  {
    setPattern(paramString1, REUtil.parseOptions(paramString2), paramLocale);
  }
  
  public String getPattern()
  {
    return regex;
  }
  
  public String toString()
  {
    return tokentree.toString(options);
  }
  
  public String getOptions()
  {
    return REUtil.createOptionString(options);
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (!(paramObject instanceof RegularExpression)) {
      return false;
    }
    RegularExpression localRegularExpression = (RegularExpression)paramObject;
    return (regex.equals(regex)) && (options == options);
  }
  
  boolean equals(String paramString, int paramInt)
  {
    return (regex.equals(paramString)) && (options == paramInt);
  }
  
  public int hashCode()
  {
    return (regex + "/" + getOptions()).hashCode();
  }
  
  public int getNumberOfGroups()
  {
    return nofparen;
  }
  
  private static final int getWordType0(char paramChar, int paramInt)
  {
    if (!isSet(paramInt, 64))
    {
      if (isSet(paramInt, 32)) {
        return Token.getRange("IsWord", true).match(paramChar) ? 1 : 2;
      }
      return isWordChar(paramChar) ? 1 : 2;
    }
    switch (Character.getType(paramChar))
    {
    case 1: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
      return 1;
    case 6: 
    case 7: 
    case 16: 
      return 0;
    case 15: 
      switch (paramChar)
      {
      case '\t': 
      case '\n': 
      case '\013': 
      case '\f': 
      case '\r': 
        return 2;
      }
      return 0;
    }
    return 2;
  }
  
  private static final boolean isEOLChar(int paramInt)
  {
    return (paramInt == 10) || (paramInt == 13) || (paramInt == 8232) || (paramInt == 8233);
  }
  
  private static final boolean isWordChar(int paramInt)
  {
    if (paramInt == 95) {
      return true;
    }
    if (paramInt < 48) {
      return false;
    }
    if (paramInt > 122) {
      return false;
    }
    if (paramInt <= 57) {
      return true;
    }
    if (paramInt < 65) {
      return false;
    }
    if (paramInt <= 90) {
      return true;
    }
    return paramInt >= 97;
  }
  
  private static final boolean matchIgnoreCase(int paramInt1, int paramInt2)
  {
    if (paramInt1 == paramInt2) {
      return true;
    }
    if ((paramInt1 > 65535) || (paramInt2 > 65535)) {
      return false;
    }
    char c1 = Character.toUpperCase((char)paramInt1);
    char c2 = Character.toUpperCase((char)paramInt2);
    if (c1 == c2) {
      return true;
    }
    return Character.toLowerCase(c1) == Character.toLowerCase(c2);
  }
  
  static final class Context
  {
    int start;
    int limit;
    int length;
    Match match;
    boolean inuse = false;
    RegularExpression.ClosureContext[] closureContexts;
    private RegularExpression.StringTarget stringTarget;
    private RegularExpression.CharArrayTarget charArrayTarget;
    private RegularExpression.CharacterIteratorTarget characterIteratorTarget;
    RegularExpression.ExpressionTarget target;
    
    Context() {}
    
    private void resetCommon(int paramInt)
    {
      length = (limit - start);
      setInUse(true);
      match = null;
      if ((closureContexts == null) || (closureContexts.length != paramInt)) {
        closureContexts = new RegularExpression.ClosureContext[paramInt];
      }
      for (int i = 0; i < paramInt; i++) {
        if (closureContexts[i] == null) {
          closureContexts[i] = new RegularExpression.ClosureContext();
        } else {
          closureContexts[i].reset();
        }
      }
    }
    
    void reset(CharacterIterator paramCharacterIterator, int paramInt1, int paramInt2, int paramInt3)
    {
      if (characterIteratorTarget == null) {
        characterIteratorTarget = new RegularExpression.CharacterIteratorTarget(paramCharacterIterator);
      } else {
        characterIteratorTarget.resetTarget(paramCharacterIterator);
      }
      target = characterIteratorTarget;
      start = paramInt1;
      limit = paramInt2;
      resetCommon(paramInt3);
    }
    
    void reset(String paramString, int paramInt1, int paramInt2, int paramInt3)
    {
      if (stringTarget == null) {
        stringTarget = new RegularExpression.StringTarget(paramString);
      } else {
        stringTarget.resetTarget(paramString);
      }
      target = stringTarget;
      start = paramInt1;
      limit = paramInt2;
      resetCommon(paramInt3);
    }
    
    void reset(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3)
    {
      if (charArrayTarget == null) {
        charArrayTarget = new RegularExpression.CharArrayTarget(paramArrayOfChar);
      } else {
        charArrayTarget.resetTarget(paramArrayOfChar);
      }
      target = charArrayTarget;
      start = paramInt1;
      limit = paramInt2;
      resetCommon(paramInt3);
    }
    
    synchronized void setInUse(boolean paramBoolean)
    {
      inuse = paramBoolean;
    }
  }
  
  static final class ClosureContext
  {
    int[] offsets = new int[4];
    int currentIndex = 0;
    
    ClosureContext() {}
    
    boolean contains(int paramInt)
    {
      for (int i = 0; i < currentIndex; i++) {
        if (offsets[i] == paramInt) {
          return true;
        }
      }
      return false;
    }
    
    void reset()
    {
      currentIndex = 0;
    }
    
    void addOffset(int paramInt)
    {
      if (currentIndex == offsets.length) {
        offsets = expandOffsets();
      }
      offsets[(currentIndex++)] = paramInt;
    }
    
    private int[] expandOffsets()
    {
      int i = offsets.length;
      int j = i << 1;
      int[] arrayOfInt = new int[j];
      System.arraycopy(offsets, 0, arrayOfInt, 0, currentIndex);
      return arrayOfInt;
    }
  }
  
  static final class CharacterIteratorTarget
    extends RegularExpression.ExpressionTarget
  {
    CharacterIterator target;
    
    CharacterIteratorTarget(CharacterIterator paramCharacterIterator)
    {
      target = paramCharacterIterator;
    }
    
    final void resetTarget(CharacterIterator paramCharacterIterator)
    {
      target = paramCharacterIterator;
    }
    
    final char charAt(int paramInt)
    {
      return target.setIndex(paramInt);
    }
    
    final boolean regionMatches(boolean paramBoolean, int paramInt1, int paramInt2, String paramString, int paramInt3)
    {
      if ((paramInt1 < 0) || (paramInt2 - paramInt1 < paramInt3)) {
        return false;
      }
      return paramBoolean ? regionMatchesIgnoreCase(paramInt1, paramInt2, paramString, paramInt3) : regionMatches(paramInt1, paramInt2, paramString, paramInt3);
    }
    
    private final boolean regionMatches(int paramInt1, int paramInt2, String paramString, int paramInt3)
    {
      int i = 0;
      while (paramInt3-- > 0) {
        if (target.setIndex(paramInt1++) != paramString.charAt(i++)) {
          return false;
        }
      }
      return true;
    }
    
    private final boolean regionMatchesIgnoreCase(int paramInt1, int paramInt2, String paramString, int paramInt3)
    {
      int i = 0;
      while (paramInt3-- > 0)
      {
        char c1 = target.setIndex(paramInt1++);
        char c2 = paramString.charAt(i++);
        if (c1 != c2)
        {
          char c3 = Character.toUpperCase(c1);
          char c4 = Character.toUpperCase(c2);
          if ((c3 != c4) && (Character.toLowerCase(c3) != Character.toLowerCase(c4))) {
            return false;
          }
        }
      }
      return true;
    }
    
    final boolean regionMatches(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      if ((paramInt1 < 0) || (paramInt2 - paramInt1 < paramInt4)) {
        return false;
      }
      return paramBoolean ? regionMatchesIgnoreCase(paramInt1, paramInt2, paramInt3, paramInt4) : regionMatches(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    private final boolean regionMatches(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = paramInt3;
      while (paramInt4-- > 0) {
        if (target.setIndex(paramInt1++) != target.setIndex(i++)) {
          return false;
        }
      }
      return true;
    }
    
    private final boolean regionMatchesIgnoreCase(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = paramInt3;
      while (paramInt4-- > 0)
      {
        char c1 = target.setIndex(paramInt1++);
        char c2 = target.setIndex(i++);
        if (c1 != c2)
        {
          char c3 = Character.toUpperCase(c1);
          char c4 = Character.toUpperCase(c2);
          if ((c3 != c4) && (Character.toLowerCase(c3) != Character.toLowerCase(c4))) {
            return false;
          }
        }
      }
      return true;
    }
  }
  
  static final class CharArrayTarget
    extends RegularExpression.ExpressionTarget
  {
    char[] target;
    
    CharArrayTarget(char[] paramArrayOfChar)
    {
      target = paramArrayOfChar;
    }
    
    final void resetTarget(char[] paramArrayOfChar)
    {
      target = paramArrayOfChar;
    }
    
    char charAt(int paramInt)
    {
      return target[paramInt];
    }
    
    final boolean regionMatches(boolean paramBoolean, int paramInt1, int paramInt2, String paramString, int paramInt3)
    {
      if ((paramInt1 < 0) || (paramInt2 - paramInt1 < paramInt3)) {
        return false;
      }
      return paramBoolean ? regionMatchesIgnoreCase(paramInt1, paramInt2, paramString, paramInt3) : regionMatches(paramInt1, paramInt2, paramString, paramInt3);
    }
    
    private final boolean regionMatches(int paramInt1, int paramInt2, String paramString, int paramInt3)
    {
      int i = 0;
      while (paramInt3-- > 0) {
        if (target[(paramInt1++)] != paramString.charAt(i++)) {
          return false;
        }
      }
      return true;
    }
    
    private final boolean regionMatchesIgnoreCase(int paramInt1, int paramInt2, String paramString, int paramInt3)
    {
      int i = 0;
      while (paramInt3-- > 0)
      {
        char c1 = target[(paramInt1++)];
        char c2 = paramString.charAt(i++);
        if (c1 != c2)
        {
          char c3 = Character.toUpperCase(c1);
          char c4 = Character.toUpperCase(c2);
          if ((c3 != c4) && (Character.toLowerCase(c3) != Character.toLowerCase(c4))) {
            return false;
          }
        }
      }
      return true;
    }
    
    final boolean regionMatches(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      if ((paramInt1 < 0) || (paramInt2 - paramInt1 < paramInt4)) {
        return false;
      }
      return paramBoolean ? regionMatchesIgnoreCase(paramInt1, paramInt2, paramInt3, paramInt4) : regionMatches(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    private final boolean regionMatches(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = paramInt3;
      while (paramInt4-- > 0) {
        if (target[(paramInt1++)] != target[(i++)]) {
          return false;
        }
      }
      return true;
    }
    
    private final boolean regionMatchesIgnoreCase(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = paramInt3;
      while (paramInt4-- > 0)
      {
        char c1 = target[(paramInt1++)];
        char c2 = target[(i++)];
        if (c1 != c2)
        {
          char c3 = Character.toUpperCase(c1);
          char c4 = Character.toUpperCase(c2);
          if ((c3 != c4) && (Character.toLowerCase(c3) != Character.toLowerCase(c4))) {
            return false;
          }
        }
      }
      return true;
    }
  }
  
  static final class StringTarget
    extends RegularExpression.ExpressionTarget
  {
    private String target;
    
    StringTarget(String paramString)
    {
      target = paramString;
    }
    
    final void resetTarget(String paramString)
    {
      target = paramString;
    }
    
    final char charAt(int paramInt)
    {
      return target.charAt(paramInt);
    }
    
    final boolean regionMatches(boolean paramBoolean, int paramInt1, int paramInt2, String paramString, int paramInt3)
    {
      if (paramInt2 - paramInt1 < paramInt3) {
        return false;
      }
      return paramBoolean ? target.regionMatches(true, paramInt1, paramString, 0, paramInt3) : target.regionMatches(paramInt1, paramString, 0, paramInt3);
    }
    
    final boolean regionMatches(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      if (paramInt2 - paramInt1 < paramInt4) {
        return false;
      }
      return paramBoolean ? target.regionMatches(true, paramInt1, target, paramInt3, paramInt4) : target.regionMatches(paramInt1, target, paramInt3, paramInt4);
    }
  }
  
  static abstract class ExpressionTarget
  {
    ExpressionTarget() {}
    
    abstract char charAt(int paramInt);
    
    abstract boolean regionMatches(boolean paramBoolean, int paramInt1, int paramInt2, String paramString, int paramInt3);
    
    abstract boolean regionMatches(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }
}
