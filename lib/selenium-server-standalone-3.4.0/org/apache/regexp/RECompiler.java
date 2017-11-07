package org.apache.regexp;

import java.util.Hashtable;































































































public class RECompiler
{
  static int brackets = 0;
  static int[] bracketStart = null;
  static int[] bracketEnd = null;
  static int[] bracketMin = null;
  static int[] bracketOpt = null;
  



  static Hashtable hashPOSIX = new Hashtable();
  
  static {
    hashPOSIX.put("alnum", new Character('w'));
    hashPOSIX.put("alpha", new Character('a'));
    hashPOSIX.put("blank", new Character('b'));
    hashPOSIX.put("cntrl", new Character('c'));
    hashPOSIX.put("digit", new Character('d'));
    hashPOSIX.put("graph", new Character('g'));
    hashPOSIX.put("lower", new Character('l'));
    hashPOSIX.put("print", new Character('p'));
    hashPOSIX.put("punct", new Character('!'));
    hashPOSIX.put("space", new Character('s'));
    hashPOSIX.put("upper", new Character('u'));
    hashPOSIX.put("xdigit", new Character('x'));
    hashPOSIX.put("javastart", new Character('j'));
    hashPOSIX.put("javapart", new Character('k'));
  }
  






  char[] instruction = new char[''];
  int lenInstruction = 0;
  String pattern;
  int len;
  int idx;
  int parens;
  static final int NODE_NORMAL = 0;
  static final int NODE_NULLABLE = 1;
  static final int NODE_TOPLEVEL = 2;
  
  void ensure(int paramInt)
  {
    int i = instruction.length;
    

    if (lenInstruction + paramInt >= i)
    {

      while (lenInstruction + paramInt >= i)
      {
        i *= 2;
      }
      

      char[] arrayOfChar = new char[i];
      System.arraycopy(instruction, 0, arrayOfChar, 0, lenInstruction);
      instruction = arrayOfChar;
    }
  }
  





  void emit(char paramChar)
  {
    ensure(1);
    

    instruction[(lenInstruction++)] = paramChar;
  }
  








  void nodeInsert(char paramChar, int paramInt1, int paramInt2)
  {
    ensure(3);
    

    System.arraycopy(instruction, paramInt2, instruction, paramInt2 + 3, lenInstruction - paramInt2);
    instruction[paramInt2] = paramChar;
    instruction[(paramInt2 + 1)] = ((char)paramInt1);
    instruction[(paramInt2 + 2)] = '\000';
    lenInstruction += 3;
  }
  



  void setNextOfEnd(int paramInt1, int paramInt2)
  {
    int i;
    


    while ((i = instruction[(paramInt1 + 2)]) != 0)
    {
      paramInt1 += i;
    }
    

    instruction[(paramInt1 + 2)] = ((char)(short)(paramInt2 - paramInt1));
  }
  







  int node(char paramChar, int paramInt)
  {
    ensure(3);
    

    instruction[lenInstruction] = paramChar;
    instruction[(lenInstruction + 1)] = ((char)paramInt);
    instruction[(lenInstruction + 2)] = '\000';
    lenInstruction += 3;
    

    return lenInstruction - 3;
  }
  




  void internalError()
    throws Error
  {
    throw new Error("Internal error!");
  }
  



  void syntaxError(String paramString)
    throws RESyntaxException
  {
    throw new RESyntaxException(paramString);
  }
  




  void allocBrackets()
  {
    if (bracketStart == null)
    {

      bracketStart = new int[10];
      bracketEnd = new int[10];
      bracketMin = new int[10];
      bracketOpt = new int[10];
      

      for (int i = 0; i < 10; i++)
      {
        byte tmp60_59 = (bracketMin[i] = bracketOpt[i] = -1);bracketEnd[i] = tmp60_59;bracketStart[i] = tmp60_59;
      }
    }
  }
  




  void bracket()
    throws RESyntaxException
  {
    if ((idx >= len) || (pattern.charAt(idx++) != '{'))
    {
      internalError();
    }
    

    if ((idx >= len) || (!Character.isDigit(pattern.charAt(idx))))
    {
      syntaxError("Expected digit");
    }
    

    StringBuffer localStringBuffer = new StringBuffer();
    while ((idx < len) && (Character.isDigit(pattern.charAt(idx))))
    {
      localStringBuffer.append(pattern.charAt(idx++));
    }
    try
    {
      bracketMin[brackets] = Integer.parseInt(localStringBuffer.toString());
    }
    catch (NumberFormatException localNumberFormatException1)
    {
      syntaxError("Expected valid number");
    }
    

    if (idx >= len)
    {
      syntaxError("Expected comma or right bracket");
    }
    

    if (pattern.charAt(idx) == '}')
    {
      idx += 1;
      bracketOpt[brackets] = 0;
      return;
    }
    

    if ((idx >= len) || (pattern.charAt(idx++) != ','))
    {
      syntaxError("Expected comma");
    }
    

    if (idx >= len)
    {
      syntaxError("Expected comma or right bracket");
    }
    

    if (pattern.charAt(idx) == '}')
    {
      idx += 1;
      bracketOpt[brackets] = -1;
      return;
    }
    

    if ((idx >= len) || (!Character.isDigit(pattern.charAt(idx))))
    {
      syntaxError("Expected digit");
    }
    

    localStringBuffer.setLength(0);
    while ((idx < len) && (Character.isDigit(pattern.charAt(idx))))
    {
      localStringBuffer.append(pattern.charAt(idx++));
    }
    try
    {
      bracketOpt[brackets] = (Integer.parseInt(localStringBuffer.toString()) - bracketMin[brackets]);
    }
    catch (NumberFormatException localNumberFormatException2)
    {
      syntaxError("Expected valid number");
    }
    

    if (bracketOpt[brackets] <= 0)
    {
      syntaxError("Bad range");
    }
    

    if ((idx >= len) || (pattern.charAt(idx++) != '}'))
    {
      syntaxError("Missing close brace");
    }
  }
  









  char escape()
    throws RESyntaxException
  {
    if (pattern.charAt(idx) != '\\')
    {
      internalError();
    }
    

    if (idx + 1 == len)
    {
      syntaxError("Escape terminates string");
    }
    

    idx += 2;
    char c = pattern.charAt(idx - 1);
    int i; switch (c)
    {
    case 'B': 
    case 'b': 
      return 65534;
    
    case 'D': 
    case 'S': 
    case 'W': 
    case 'd': 
    case 's': 
    case 'w': 
      return 65533;
    


    case 'u': 
    case 'x': 
      i = c == 'u' ? 4 : 2;
      

      int j = 0;
      for (; (idx < len) && (i-- > 0); idx += 1)
      {

        int k = pattern.charAt(idx);
        

        if ((k >= 48) && (k <= 57))
        {

          j = (j << 4) + k - 48;

        }
        else
        {
          int m = Character.toLowerCase(k);
          if ((m >= 97) && (m <= 102))
          {

            j = (j << 4) + (m - 97) + 10;

          }
          else
          {

            syntaxError("Expected " + i + " hexadecimal digits after \\" + c);
          }
        }
      }
      return (char)j;
    

    case 't': 
      return '\t';
    
    case 'n': 
      return '\n';
    
    case 'r': 
      return '\r';
    
    case 'f': 
      return '\f';
    


    case '0': 
    case '1': 
    case '2': 
    case '3': 
    case '4': 
    case '5': 
    case '6': 
    case '7': 
    case '8': 
    case '9': 
      if (((idx < len) && (Character.isDigit(pattern.charAt(idx)))) || (c == '0'))
      {

        i = c - '0';
        if ((idx < len) && (Character.isDigit(pattern.charAt(idx))))
        {
          i = (i << 3) + (pattern.charAt(idx++) - '0');
          if ((idx < len) && (Character.isDigit(pattern.charAt(idx))))
          {
            i = (i << 3) + (pattern.charAt(idx++) - '0');
          }
        }
        return (char)i;
      }
      

      return 65535;
    }
    
    

    return c;
  }
  






  int characterClass()
    throws RESyntaxException
  {
    if (pattern.charAt(idx) != '[')
    {
      internalError();
    }
    

    if ((idx + 1 >= len) || (pattern.charAt(++idx) == ']'))
    {
      syntaxError("Empty or unterminated class");
    }
    

    if ((idx < len) && (pattern.charAt(idx) == ':'))
    {

      idx += 1;
      

      i = idx;
      while ((idx < len) && (pattern.charAt(idx) >= 'a') && (pattern.charAt(idx) <= 'z'))
      {
        idx += 1;
      }
      

      if ((idx + 1 < len) && (pattern.charAt(idx) == ':') && (pattern.charAt(idx + 1) == ']'))
      {

        String str = pattern.substring(i, idx);
        

        localCharacter2 = (Character)hashPOSIX.get(str);
        if (localCharacter2 != null)
        {

          idx += 2;
          

          return node('P', localCharacter2.charValue());
        }
        syntaxError("Invalid POSIX character class '" + str + "'");
      }
      syntaxError("Invalid POSIX character class syntax");
    }
    

    int i = node('[', 0);
    

    Character localCharacter1 = 65535;
    Character localCharacter2 = localCharacter1;
    int k = 0;
    boolean bool = true;
    int m = 0;
    int n = idx;
    int i1 = 0;
    
    RERange localRERange = new RERange();
    while ((idx < len) && (pattern.charAt(idx) != ']'))
    {
      int i2;
      


      switch (pattern.charAt(idx))
      {
      case '^': 
        bool ^= true;
        if (idx == n)
        {
          localRERange.include(0, 65535, true);
        }
        idx += 1;
        break;
      



      case '\\': 
        switch (i4 = escape())
        {


        case '￾': 
        case '￿': 
          syntaxError("Bad character class");
        


        case '�': 
          if (m != 0)
          {
            syntaxError("Bad character class");
          }
          

          switch (pattern.charAt(idx - 1))
          {
          case 'D': 
          case 'S': 
          case 'W': 
            syntaxError("Bad character class");
          
          case 's': 
            localRERange.include('\t', bool);
            localRERange.include('\r', bool);
            localRERange.include('\f', bool);
            localRERange.include('\n', bool);
            localRERange.include('\b', bool);
            localRERange.include(' ', bool);
            break;
          
          case 'w': 
            localRERange.include(97, 122, bool);
            localRERange.include(65, 90, bool);
            localRERange.include('_', bool);
          


          case 'd': 
            localRERange.include(48, 57, bool);
            break;
          }
          
          
          localCharacter2 = localCharacter1;
          break;
        


        default: 
          k = i4; }
        break;
      





      case '-': 
        if (m != 0)
        {
          syntaxError("Bad class range");
        }
        m = 1;
        

        i2 = localCharacter2 == localCharacter1 ? 0 : localCharacter2;
        

        if ((idx + 1 >= len) || (pattern.charAt(++idx) != ']'))
          continue;
        k = 65535;
        break;
      


      default: 
        k = pattern.charAt(idx++);
      }
      
      
      int j;
      if (m != 0)
      {

        int i3 = k;
        

        if (i2 >= i3)
        {
          syntaxError("Bad character class");
        }
        localRERange.include(i2, i3, bool);
        

        j = localCharacter1;
        m = 0;

      }
      else
      {
        if ((idx + 1 >= len) || (pattern.charAt(idx + 1) != '-'))
        {
          localRERange.include(k, bool);
        }
        j = k;
      }
    }
    

    if (idx == len)
    {
      syntaxError("Unterminated character class");
    }
    

    idx += 1;
    

    instruction[(i + 1)] = ((char)num);
    for (int i4 = 0; i4 < num; i4++)
    {
      emit((char)minRange[i4]);
      emit((char)maxRange[i4]);
    }
    return i;
  }
  








  int atom()
    throws RESyntaxException
  {
    int i = node('A', 0);
    

    int j = 0;
    




    while (idx < len) {
      int k;
      int m;
      if (idx + 1 < len)
      {
        k = pattern.charAt(idx + 1);
        

        if (pattern.charAt(idx) == '\\')
        {
          m = idx;
          escape();
          if (idx < len)
          {
            k = pattern.charAt(idx);
          }
          idx = m;
        }
        

        switch (k)
        {



        case 42: 
        case 43: 
        case 63: 
        case 123: 
          if (j != 0) {
            break;
          }
        }
        
      }
      

      switch (pattern.charAt(idx))
      {












      case '*': 
      case '+': 
      case '?': 
      case '{': 
        if (j != 0) {
          break label365;
        }
        syntaxError("Missing operand to closure");
        
        break;
      



      case '\\': 
        k = idx;
        m = escape();
        

        if ((m & 0xFFF0) == 65520)
        {

          idx = k;
          
          break label365;
        }
        
        emit(m);
        j++;
        
        break;
      


      default: 
        emit(pattern.charAt(idx++));
        j++;
      }
      
    }
    
    label365:
    if (j == 0)
    {
      internalError();
    }
    

    instruction[(i + 1)] = ((char)j);
    return i;
  }
  





  int terminal(int[] paramArrayOfInt)
    throws RESyntaxException
  {
    switch (pattern.charAt(idx))
    {
    case '$': 
    case '.': 
    case '^': 
      return node(pattern.charAt(idx++), 0);
    
    case '[': 
      return characterClass();
    
    case '(': 
      return expr(paramArrayOfInt);
    
    case ')': 
      syntaxError("Unexpected close paren");
    
    case '|': 
      internalError();
    
    case ']': 
      syntaxError("Mismatched class");
    
    case '\000': 
      syntaxError("Unexpected end of input");
    
    case '*': 
    case '+': 
    case '?': 
    case '{': 
      syntaxError("Missing operand to closure");
    


    case '\\': 
      int i = idx;
      

      switch (escape())
      {
      case '�': 
      case '￾': 
        paramArrayOfInt[0] &= 0xFFFFFFFE;
        return node('\\', pattern.charAt(idx - 1));
      

      case '￿': 
        int j = (char)(pattern.charAt(idx - 1) - '0');
        if (parens <= j)
        {
          syntaxError("Bad backreference");
        }
        paramArrayOfInt[0] |= 0x1;
        return node('#', j);
      }
      
      



      idx = i;
      paramArrayOfInt[0] &= 0xFFFFFFFE;
      break;
    }
    
    



    paramArrayOfInt[0] &= 0xFFFFFFFE;
    return atom();
  }
  






  int closure(int[] paramArrayOfInt)
    throws RESyntaxException
  {
    int i = idx;
    

    int[] arrayOfInt = new int[1];
    

    int j = terminal(arrayOfInt);
    

    paramArrayOfInt[0] |= arrayOfInt[0];
    

    if (idx >= len)
    {
      return j;
    }
    int k = 1;
    int m = pattern.charAt(idx);
    int n; switch (m)
    {


    case 42: 
    case 63: 
      paramArrayOfInt[0] |= 0x1;
    


    case 43: 
      idx += 1;
    


    case 123: 
      n = instruction[j];
      if ((n == 94) || (n == 36))
      {
        syntaxError("Bad closure operand");
      }
      if ((arrayOfInt[0] & 0x1) != 0)
      {
        syntaxError("Closure operand can't be nullable");
      }
      break;
    }
    
    
    if ((idx < len) && (pattern.charAt(idx) == '?'))
    {
      idx += 1;
      k = 0;
    }
    
    if (k != 0) {}
    

    switch (m)
    {


    case 123: 
      n = 0;
      
      allocBrackets();
      for (int i1 = 0; i1 < brackets; i1++)
      {
        if (bracketStart[i1] == idx)
        {
          n = 1;
          break;
        }
      }
      

      if (n == 0)
      {
        if (brackets >= 10)
        {
          syntaxError("Too many bracketed closures (limit is 10)");
        }
        bracketStart[brackets] = idx;
        bracket();
        bracketEnd[brackets] = idx;
        i1 = brackets++;
      }
      

      if (bracketMin[i1] -= 1 > 0)
      {

        idx = i;



      }
      else if (bracketOpt[i1] == -2)
      {


        m = 42;
        bracketOpt[i1] = 0;
        idx = bracketEnd[i1];

      }
      else if (bracketOpt[i1] == -1)
      {
        idx = i;
        bracketOpt[i1] = -2;
      }
      else
      {
        int tmp439_437 = i1; int[] tmp439_434 = bracketOpt; int tmp441_440 = tmp439_434[tmp439_437];tmp439_434[tmp439_437] = (tmp441_440 - 1); if (tmp441_440 > 0)
        {

          idx = i;
          m = 63;

        }
        else
        {
          idx = bracketEnd[i1]; } }
      break;
    





    case 42: 
    case 63: 
      if (k != 0)
      {



        if (m == 63)
        {

          nodeInsert('|', 0, j);
          setNextOfEnd(j, node('|', 0));
          n = node('N', 0);
          setNextOfEnd(j, n);
          setNextOfEnd(j + 3, n);
        }
        
        if (m != 42) {
          break label778;
        }
        nodeInsert('|', 0, j);
        setNextOfEnd(j + 3, node('|', 0));
        setNextOfEnd(j + 3, node('G', 0));
        setNextOfEnd(j + 3, j);
        setNextOfEnd(j, node('|', 0));
        setNextOfEnd(j, node('N', 0));
      }
      break;
    



    case 43: 
      n = node('|', 0);
      setNextOfEnd(j, n);
      setNextOfEnd(node('G', 0), j);
      setNextOfEnd(n, node('|', 0));
      setNextOfEnd(j, node('N', 0));
      

      break label778;
      

      break label778;
      
      setNextOfEnd(j, node('E', 0));
      

      switch (m)
      {
      case 63: 
        nodeInsert('/', 0, j);
        break;
      
      case 42: 
        nodeInsert('8', 0, j);
        break;
      
      case 43: 
        nodeInsert('=', 0, j);
        break;
      }
      
      
      setNextOfEnd(j, lenInstruction); }
    label778:
    return j;
  }
  







  int branch(int[] paramArrayOfInt)
    throws RESyntaxException
  {
    int j = node('|', 0);
    int k = -1;
    int[] arrayOfInt = new int[1];
    int m = 1;
    while ((idx < len) && (pattern.charAt(idx) != '|') && (pattern.charAt(idx) != ')'))
    {

      arrayOfInt[0] = 0;
      int i = closure(arrayOfInt);
      if (arrayOfInt[0] == 0)
      {
        m = 0;
      }
      

      if (k != -1)
      {
        setNextOfEnd(k, i);
      }
      

      k = i;
    }
    

    if (k == -1)
    {
      node('N', 0);
    }
    

    if (m != 0)
    {
      paramArrayOfInt[0] |= 0x1;
    }
    return j;
  }
  







  int expr(int[] paramArrayOfInt)
    throws RESyntaxException
  {
    int i = 0;
    int j = -1;
    int k = parens;
    if (((paramArrayOfInt[0] & 0x2) == 0) && (pattern.charAt(idx) == '('))
    {
      idx += 1;
      i = 1;
      j = node('(', parens++);
    }
    paramArrayOfInt[0] &= 0xFFFFFFFD;
    

    int m = branch(paramArrayOfInt);
    if (j == -1)
    {
      j = m;
    }
    else
    {
      setNextOfEnd(j, m);
    }
    

    while ((idx < len) && (pattern.charAt(idx) == '|'))
    {
      idx += 1;
      m = branch(paramArrayOfInt);
      setNextOfEnd(j, m);
    }
    
    int n;
    
    if (i != 0)
    {
      if ((idx < len) && (pattern.charAt(idx) == ')'))
      {
        idx += 1;
      }
      else
      {
        syntaxError("Missing close paren");
      }
      n = node(')', k);
    }
    else
    {
      n = node('E', 0);
    }
    

    setNextOfEnd(j, n);
    

    int i1 = -1; for (int i2 = j; i1 != 0; i2 += i1)
    {

      if (instruction[i2] == '|')
      {
        setNextOfEnd(i2 + 3, n);
      }
      i1 = instruction[(i2 + 2)];
    }
    







    return j;
  }
  

  static final char ESC_MASK = '￰';
  
  static final char ESC_BACKREF = '￿';
  
  static final char ESC_COMPLEX = '￾';
  static final char ESC_CLASS = '�';
  static final int maxBrackets = 10;
  static final int bracketUnbounded = -1;
  static final int bracketFinished = -2;
  public REProgram compile(String paramString)
    throws RESyntaxException
  {
    pattern = paramString;
    len = paramString.length();
    idx = 0;
    lenInstruction = 0;
    parens = 1;
    brackets = 0;
    

    int[] arrayOfInt = { 2 };
    

    expr(arrayOfInt);
    

    if (idx != len)
    {
      if (paramString.charAt(idx) == ')')
      {
        syntaxError("Unmatched close paren");
      }
      syntaxError("Unexpected input remains");
    }
    

    char[] arrayOfChar = new char[lenInstruction];
    System.arraycopy(instruction, 0, arrayOfChar, 0, lenInstruction);
    return new REProgram(arrayOfChar);
  }
  

  public RECompiler() {}
  
  class RERange
  {
    int size = 16;
    int[] minRange = new int[size];
    int[] maxRange = new int[size];
    int num = 0;
    





    void delete(int paramInt)
    {
      if ((num == 0) || (paramInt >= num))
      {
        return;
      }
      

      while (paramInt++ < num)
      {
        if (paramInt - 1 >= 0)
        {
          minRange[(paramInt - 1)] = minRange[paramInt];
          maxRange[(paramInt - 1)] = maxRange[paramInt];
        }
      }
      

      num -= 1;
    }
    






    void merge(int paramInt1, int paramInt2)
    {
      for (int i = 0; i < num; i++)
      {

        if ((paramInt1 >= minRange[i]) && (paramInt2 <= maxRange[i]))
        {
          return;
        }
        

        if ((paramInt1 <= minRange[i]) && (paramInt2 >= maxRange[i]))
        {
          delete(i);
          merge(paramInt1, paramInt2);
          return;
        }
        

        if ((paramInt1 >= minRange[i]) && (paramInt1 <= maxRange[i]))
        {
          delete(i);
          paramInt1 = minRange[i];
          merge(paramInt1, paramInt2);
          return;
        }
        

        if ((paramInt2 >= minRange[i]) && (paramInt2 <= maxRange[i]))
        {
          delete(i);
          paramInt2 = maxRange[i];
          merge(paramInt1, paramInt2);
          return;
        }
      }
      

      if (num >= size)
      {
        size *= 2;
        int[] arrayOfInt1 = new int[size];
        int[] arrayOfInt2 = new int[size];
        System.arraycopy(minRange, 0, arrayOfInt1, 0, num);
        System.arraycopy(maxRange, 0, arrayOfInt2, 0, num);
        minRange = arrayOfInt1;
        maxRange = arrayOfInt2;
      }
      minRange[num] = paramInt1;
      maxRange[num] = paramInt2;
      num += 1;
    }
    






    void remove(int paramInt1, int paramInt2)
    {
      for (int i = 0; i < num; i++)
      {

        if ((minRange[i] >= paramInt1) && (maxRange[i] <= paramInt2))
        {
          delete(i);
          i--;
          return;
        }
        

        if ((paramInt1 >= minRange[i]) && (paramInt2 <= maxRange[i]))
        {
          int j = minRange[i];
          int k = maxRange[i];
          delete(i);
          if (j < paramInt1 - 1)
          {
            merge(j, paramInt1 - 1);
          }
          if (paramInt2 + 1 < k)
          {
            merge(paramInt2 + 1, k);
          }
          return;
        }
        

        if ((minRange[i] >= paramInt1) && (minRange[i] <= paramInt2))
        {
          minRange[i] = (paramInt2 + 1);
          return;
        }
        

        if ((maxRange[i] >= paramInt1) && (maxRange[i] <= paramInt2))
        {
          maxRange[i] = (paramInt1 - 1); return;
        }
      }
    }
    







    void include(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        merge(paramInt1, paramInt2);
      }
      else
      {
        remove(paramInt1, paramInt2);
      }
    }
    





    void include(char paramChar, boolean paramBoolean)
    {
      include(paramChar, paramChar, paramBoolean);
    }
    
    RERange() {}
  }
}
