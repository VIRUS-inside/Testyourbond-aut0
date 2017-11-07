package java_cup.runtime;

import java.io.PrintStream;
import java.util.Stack;
import java.util.Vector;





















































































































public abstract class lr_parser
{
  protected static final int _error_sync_size = 3;
  
  public lr_parser(Scanner paramScanner)
  {
    this();
    setScanner(paramScanner);
  }
  












  protected int error_sync_size()
  {
    return 3;
  }
  








































































  protected boolean _done_parsing = false;
  

  protected int tos;
  
  protected Symbol cur_token;
  

  public void done_parsing()
  {
    _done_parsing = true;
  }
  
















  protected Stack stack = new Stack();
  


  protected short[][] production_tab;
  


  protected short[][] action_tab;
  


  protected short[][] reduce_tab;
  

  private Scanner _scanner;
  

  protected Symbol[] lookahead;
  

  protected int lookahead_pos;
  


  public void setScanner(Scanner paramScanner)
  {
    _scanner = paramScanner;
  }
  
  public Scanner getScanner()
  {
    return _scanner;
  }
  














































  public Symbol scan()
    throws Exception
  {
    Symbol localSymbol = getScanner().next_token();
    return localSymbol != null ? localSymbol : new Symbol(EOF_sym());
  }
  













  public void report_fatal_error(String paramString, Object paramObject)
    throws Exception
  {
    done_parsing();
    

    report_error(paramString, paramObject);
    

    throw new Exception("Can't recover from previous error(s)");
  }
  











  public void report_error(String paramString, Object paramObject)
  {
    System.err.print(paramString);
    if ((paramObject instanceof Symbol)) {
      if (left != -1)
        System.err.println(" at character " + left + 
          " of input"); else
        System.err.println("");
    } else { System.err.println("");
    }
  }
  







  public void syntax_error(Symbol paramSymbol)
  {
    report_error("Syntax error", paramSymbol);
  }
  







  public void unrecovered_syntax_error(Symbol paramSymbol)
    throws Exception
  {
    report_fatal_error("Couldn't repair and continue parse", paramSymbol);
  }
  














  protected final short get_action(int paramInt1, int paramInt2)
  {
    short[] arrayOfShort = action_tab[paramInt1];
    
    int m;
    if (arrayOfShort.length < 20) {
      for (m = 0; m < arrayOfShort.length; m++)
      {

        int i = arrayOfShort[(m++)];
        if ((i == paramInt2) || (i == -1))
        {

          return arrayOfShort[m];
        }
      }
    }
    else
    {
      int j = 0;
      int k = (arrayOfShort.length - 1) / 2 - 1;
      while (j <= k)
      {
        m = (j + k) / 2;
        if (paramInt2 == arrayOfShort[(m * 2)])
          return arrayOfShort[(m * 2 + 1)];
        if (paramInt2 > arrayOfShort[(m * 2)]) {
          j = m + 1;
        } else {
          k = m - 1;
        }
      }
      
      return arrayOfShort[(arrayOfShort.length - 1)];
    }
    


    return 0;
  }
  













  protected final short get_reduce(int paramInt1, int paramInt2)
  {
    short[] arrayOfShort = reduce_tab[paramInt1];
    

    if (arrayOfShort == null) {
      return -1;
    }
    for (int j = 0; j < arrayOfShort.length; j++)
    {

      int i = arrayOfShort[(j++)];
      if ((i == paramInt2) || (i == -1))
      {

        return arrayOfShort[j];
      }
    }
    
    return -1;
  }
  











  public Symbol parse()
    throws Exception
  {
    Symbol localSymbol = null;
    





    production_tab = production_table();
    action_tab = action_table();
    reduce_tab = reduce_table();
    

    init_actions();
    

    user_init();
    

    cur_token = scan();
    

    stack.removeAllElements();
    stack.push(new Symbol(0, start_state()));
    tos = 0;
    

    for (_done_parsing = false; !_done_parsing;)
    {

      if (cur_token.used_by_parser) {
        throw new Error("Symbol recycling detected (fix your scanner).");
      }
      


      int i = get_action(stack.peek()).parse_state, cur_token.sym);
      

      if (i > 0)
      {

        cur_token.parse_state = (i - 1);
        cur_token.used_by_parser = true;
        stack.push(cur_token);
        tos += 1;
        

        cur_token = scan();

      }
      else if (i < 0)
      {

        localSymbol = do_action(-i - 1, this, stack, tos);
        

        int k = production_tab[(-i - 1)][0];
        int j = production_tab[(-i - 1)][1];
        

        for (int m = 0; m < j; m++)
        {
          stack.pop();
          tos -= 1;
        }
        

        i = get_reduce(stack.peek()).parse_state, k);
        

        parse_state = i;
        used_by_parser = true;
        stack.push(localSymbol);
        tos += 1;

      }
      else if (i == 0)
      {

        syntax_error(cur_token);
        

        if (!error_recovery(false))
        {

          unrecovered_syntax_error(cur_token);
          

          done_parsing();
        } else {
          localSymbol = (Symbol)stack.peek();
        }
      }
    }
    return localSymbol;
  }
  







  public void debug_message(String paramString)
  {
    System.err.println(paramString);
  }
  



  public void dump_stack()
  {
    if (stack == null)
    {
      debug_message("# Stack dump requested, but stack is null");
      return;
    }
    
    debug_message("============ Parse Stack Dump ============");
    

    for (int i = 0; i < stack.size(); i++)
    {
      debug_message("Symbol: " + stack.elementAt(i)).sym + 
        " State: " + stack.elementAt(i)).parse_state);
    }
    debug_message("==========================================");
  }
  








  public void debug_reduce(int paramInt1, int paramInt2, int paramInt3)
  {
    debug_message("# Reduce with prod #" + paramInt1 + " [NT=" + paramInt2 + 
      ", " + "SZ=" + paramInt3 + "]");
  }
  






  public void debug_shift(Symbol paramSymbol)
  {
    debug_message("# Shift under term #" + sym + 
      " to state #" + parse_state);
  }
  



  public void debug_stack()
  {
    StringBuffer localStringBuffer = new StringBuffer("## STACK:");
    for (int i = 0; i < stack.size(); i++) {
      Symbol localSymbol = (Symbol)stack.elementAt(i);
      localStringBuffer.append(" <state " + parse_state + ", sym " + sym + ">");
      if ((i % 3 == 2) || (i == stack.size() - 1)) {
        debug_message(localStringBuffer.toString());
        localStringBuffer = new StringBuffer("         ");
      }
    }
  }
  











  public Symbol debug_parse()
    throws Exception
  {
    Symbol localSymbol = null;
    




    production_tab = production_table();
    action_tab = action_table();
    reduce_tab = reduce_table();
    
    debug_message("# Initializing parser");
    

    init_actions();
    

    user_init();
    

    cur_token = scan();
    
    debug_message("# Current Symbol is #" + cur_token.sym);
    

    stack.removeAllElements();
    stack.push(new Symbol(0, start_state()));
    tos = 0;
    

    for (_done_parsing = false; !_done_parsing;)
    {

      if (cur_token.used_by_parser) {
        throw new Error("Symbol recycling detected (fix your scanner).");
      }
      



      int i = get_action(stack.peek()).parse_state, cur_token.sym);
      

      if (i > 0)
      {

        cur_token.parse_state = (i - 1);
        cur_token.used_by_parser = true;
        debug_shift(cur_token);
        stack.push(cur_token);
        tos += 1;
        

        cur_token = scan();
        debug_message("# Current token is " + cur_token);

      }
      else if (i < 0)
      {

        localSymbol = do_action(-i - 1, this, stack, tos);
        

        int k = production_tab[(-i - 1)][0];
        int j = production_tab[(-i - 1)][1];
        
        debug_reduce(-i - 1, k, j);
        

        for (int m = 0; m < j; m++)
        {
          stack.pop();
          tos -= 1;
        }
        

        i = get_reduce(stack.peek()).parse_state, k);
        debug_message("# Reduce rule: top state " + 
          stack.peek()).parse_state + 
          ", lhs sym " + k + " -> state " + i);
        

        parse_state = i;
        used_by_parser = true;
        stack.push(localSymbol);
        tos += 1;
        
        debug_message("# Goto state #" + i);

      }
      else if (i == 0)
      {

        syntax_error(cur_token);
        

        if (!error_recovery(true))
        {

          unrecovered_syntax_error(cur_token);
          

          done_parsing();
        } else {
          localSymbol = (Symbol)stack.peek();
        }
      }
    }
    return localSymbol;
  }
  


























  protected boolean error_recovery(boolean paramBoolean)
    throws Exception
  {
    if (paramBoolean) { debug_message("# Attempting error recovery");
    }
    

    if (!find_recovery_config(paramBoolean))
    {
      if (paramBoolean) debug_message("# Error recovery fails");
      return false;
    }
    

    read_lookahead();
    


    for (;;)
    {
      if (paramBoolean) debug_message("# Trying to parse ahead");
      if (try_parse_ahead(paramBoolean)) {
        break;
      }
      


      if (lookahead[0].sym == EOF_sym())
      {
        if (paramBoolean) debug_message("# Error recovery fails at EOF");
        return false;
      }
      






      if (paramBoolean)
        debug_message("# Consuming Symbol #" + lookahead[0].sym);
      restart_lookahead();
    }
    

    if (paramBoolean) { debug_message("# Parse-ahead ok, going back to normal parse");
    }
    
    parse_lookahead(paramBoolean);
    

    return true;
  }
  






  protected boolean shift_under_error()
  {
    return get_action(stack.peek()).parse_state, error_sym()) > 0;
  }
  












  protected boolean find_recovery_config(boolean paramBoolean)
  {
    if (paramBoolean) { debug_message("# Finding recovery state on stack");
    }
    
    int j = stack.peek()).right;
    int k = stack.peek()).left;
    

    while (!shift_under_error())
    {

      if (paramBoolean)
        debug_message("# Pop stack by one, state was # " + 
          stack.peek()).parse_state);
      k = stack.pop()).left;
      tos -= 1;
      

      if (stack.empty())
      {
        if (paramBoolean) debug_message("# No recovery state found on stack");
        return false;
      }
    }
    

    int i = get_action(stack.peek()).parse_state, error_sym());
    if (paramBoolean)
    {
      debug_message("# Recover state found (#" + 
        stack.peek()).parse_state + ")");
      debug_message("# Shifting on error to state #" + (i - 1));
    }
    

    Symbol localSymbol = new Symbol(error_sym(), k, j);
    parse_state = (i - 1);
    used_by_parser = true;
    stack.push(localSymbol);
    tos += 1;
    
    return true;
  }
  













  protected void read_lookahead()
    throws Exception
  {
    lookahead = new Symbol[error_sync_size()];
    

    for (int i = 0; i < error_sync_size(); i++)
    {
      lookahead[i] = cur_token;
      cur_token = scan();
    }
    

    lookahead_pos = 0;
  }
  

  protected Symbol cur_err_token()
  {
    return lookahead[lookahead_pos];
  }
  





  protected boolean advance_lookahead()
  {
    lookahead_pos += 1;
    

    return lookahead_pos < error_sync_size();
  }
  





  protected void restart_lookahead()
    throws Exception
  {
    for (int i = 1; i < error_sync_size(); i++) {
      lookahead[(i - 1)] = lookahead[i];
    }
    




    lookahead[(error_sync_size() - 1)] = cur_token;
    cur_token = scan();
    

    lookahead_pos = 0;
  }
  















  protected boolean try_parse_ahead(boolean paramBoolean)
    throws Exception
  {
    virtual_parse_stack localVirtual_parse_stack = new virtual_parse_stack(stack);
    


    for (;;)
    {
      int i = get_action(localVirtual_parse_stack.top(), cur_err_tokensym);
      

      if (i == 0) { return false;
      }
      
      if (i > 0)
      {

        localVirtual_parse_stack.push(i - 1);
        
        if (paramBoolean) { debug_message("# Parse-ahead shifts Symbol #" + 
            cur_err_tokensym + " into state #" + (i - 1));
        }
        
        if (!advance_lookahead()) { return true;
        }
        
      }
      else
      {
        if (-i - 1 == start_production())
        {
          if (paramBoolean) debug_message("# Parse-ahead accepts");
          return true;
        }
        

        int j = production_tab[(-i - 1)][0];
        int k = production_tab[(-i - 1)][1];
        

        for (int m = 0; m < k; m++) {
          localVirtual_parse_stack.pop();
        }
        if (paramBoolean) {
          debug_message("# Parse-ahead reduces: handle size = " + 
            k + " lhs = #" + j + " from state #" + localVirtual_parse_stack.top());
        }
        
        localVirtual_parse_stack.push(get_reduce(localVirtual_parse_stack.top(), j));
        if (paramBoolean) {
          debug_message("# Goto state #" + localVirtual_parse_stack.top());
        }
      }
    }
  }
  














  protected void parse_lookahead(boolean paramBoolean)
    throws Exception
  {
    Symbol localSymbol = null;
    




    lookahead_pos = 0;
    
    if (paramBoolean)
    {
      debug_message("# Reparsing saved input with actions");
      debug_message("# Current Symbol is #" + cur_err_tokensym);
      debug_message("# Current state is #" + 
        stack.peek()).parse_state);
    }
    

    while (!_done_parsing)
    {



      int i = 
        get_action(stack.peek()).parse_state, cur_err_tokensym);
      

      if (i > 0)
      {

        cur_err_tokenparse_state = (i - 1);
        cur_err_tokenused_by_parser = true;
        if (paramBoolean) debug_shift(cur_err_token());
        stack.push(cur_err_token());
        tos += 1;
        

        if (!advance_lookahead())
        {
          if (paramBoolean) { debug_message("# Completed reparse");
          }
          






          return;
        }
        
        if (paramBoolean) {
          debug_message("# Current Symbol is #" + cur_err_tokensym);
        }
      }
      else if (i < 0)
      {

        localSymbol = do_action(-i - 1, this, stack, tos);
        

        int k = production_tab[(-i - 1)][0];
        int j = production_tab[(-i - 1)][1];
        
        if (paramBoolean) { debug_reduce(-i - 1, k, j);
        }
        
        for (int m = 0; m < j; m++)
        {
          stack.pop();
          tos -= 1;
        }
        

        i = get_reduce(stack.peek()).parse_state, k);
        

        parse_state = i;
        used_by_parser = true;
        stack.push(localSymbol);
        tos += 1;
        
        if (paramBoolean) { debug_message("# Goto state #" + i);
        }
        

      }
      else if (i == 0)
      {
        report_fatal_error("Syntax error", localSymbol); return;
      }
    }
  }
  







  protected static short[][] unpackFromStrings(String[] paramArrayOfString)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramArrayOfString[0]);
    for (int i = 1; i < paramArrayOfString.length; i++)
      localStringBuffer.append(paramArrayOfString[i]);
    int j = 0;
    int k = localStringBuffer.charAt(j) << '\020' | localStringBuffer.charAt(j + 1);j += 2;
    short[][] arrayOfShort = new short[k][];
    for (int m = 0; m < k; m++) {
      int n = localStringBuffer.charAt(j) << '\020' | localStringBuffer.charAt(j + 1);j += 2;
      arrayOfShort[m] = new short[n];
      for (int i1 = 0; i1 < n; i1++)
        arrayOfShort[m][i1] = ((short)(localStringBuffer.charAt(j++) - '\002'));
    }
    return arrayOfShort;
  }
  
  public lr_parser() {}
  
  public abstract int EOF_sym();
  
  public abstract short[][] action_table();
  
  public abstract Symbol do_action(int paramInt1, lr_parser paramLr_parser, Stack paramStack, int paramInt2)
    throws Exception;
  
  public abstract int error_sym();
  
  protected abstract void init_actions()
    throws Exception;
  
  public abstract short[][] production_table();
  
  public abstract short[][] reduce_table();
  
  public abstract int start_production();
  
  public abstract int start_state();
  
  public void user_init()
    throws Exception
  {}
}
