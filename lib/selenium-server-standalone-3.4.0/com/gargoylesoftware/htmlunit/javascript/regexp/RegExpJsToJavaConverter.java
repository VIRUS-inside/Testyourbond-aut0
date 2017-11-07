package com.gargoylesoftware.htmlunit.javascript.regexp;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;



























public class RegExpJsToJavaConverter
{
  private static final String DIGITS = "0123456789";
  private Tape tape_;
  private boolean insideCharClass_;
  private boolean insideRepetition_;
  private Stack<Subexpresion> parsingSubexpressions_;
  private List<Subexpresion> subexpressions_;
  public RegExpJsToJavaConverter() {}
  
  private static class Tape
  {
    private final StringBuilder tape_;
    private int currentPos_;
    
    Tape(String input)
    {
      currentPos_ = 0;
      tape_ = new StringBuilder(input);
    }
    



    public void move(int offset)
    {
      currentPos_ += offset;
    }
    





    public int read()
    {
      if (currentPos_ < 0) {
        return -1;
      }
      if (currentPos_ >= tape_.length()) {
        return -1;
      }
      
      return tape_.charAt(currentPos_++);
    }
    




    public void insert(String token, int offset)
    {
      tape_.insert(currentPos_ + offset, token);
      currentPos_ += token.length();
    }
    




    public void insertAt(String token, int pos)
    {
      tape_.insert(pos, token);
      currentPos_ += token.length();
    }
    



    public void replace(String token)
    {
      tape_.replace(currentPos_, currentPos_ + 1, token);
    }
    





    public String toString()
    {
      return tape_.toString();
    }
  }
  
  private static final class Subexpresion {
    private boolean closed_;
    private boolean optional_;
    private boolean enhanced_;
    private int start_;
    private int end_;
    
    private Subexpresion() {
      closed_ = false;
      optional_ = false;
      enhanced_ = false;
      start_ = -1;
      end_ = -1;
    }
  }
  



















  public String convert(String input)
  {
    tape_ = new Tape(input);
    insideCharClass_ = false;
    insideRepetition_ = false;
    
    parsingSubexpressions_ = new Stack();
    subexpressions_ = new LinkedList();
    
    int current = tape_.read();
    while (current > -1) {
      if (92 == current) {
        processEscapeSequence();
      }
      else if (91 == current) {
        processCharClassStart();
      }
      else if (93 == current) {
        processCharClassEnd();
      }
      else if (123 == current) {
        processRepetitionStart();
      }
      else if (125 == current) {
        processRepetitionEnd();
      }
      else if (40 == current) {
        processSubExpressionStart();
      }
      else if (41 == current) {
        processSubExpressionEnd();
      }
      

      current = tape_.read();
    }
    
    return tape_.toString();
  }
  
  private void processCharClassStart() {
    if (insideCharClass_) {
      tape_.insert("\\", -1);
    }
    else {
      insideCharClass_ = true;
      
      int next = tape_.read();
      if (next < 0) {
        tape_.insert("\\", -1);
        return;
      }
      if (94 == next)
      {
        next = tape_.read();
        if (next < 0) {
          tape_.insert("\\", -2);
          return;
        }
        if (92 == next) {
          next = tape_.read();
          if ("0123456789".indexOf(next) < 0) {
            tape_.move(-2);
            return;
          }
          
          if (handleBackReferenceOrOctal(next)) {
            next = tape_.read();
            if (93 == next) {
              tape_.move(-3);
              tape_.replace("");
              tape_.replace("");
              tape_.replace(".");
              insideCharClass_ = false;
            }
          }
        }
        else {
          tape_.move(-1);
        }
      }
      else {
        tape_.move(-1);
      }
    }
  }
  
  private void processCharClassEnd() {
    insideCharClass_ = false;
  }
  
  private void processRepetitionStart() {
    int next = tape_.read();
    if (next < 0) {
      tape_.insert("\\", -1);
      return;
    }
    
    if ("0123456789".indexOf(next) > -1) {
      insideRepetition_ = true;
    }
    else {
      tape_.insert("\\", -2);
      tape_.move(-1);
    }
  }
  
  private void processRepetitionEnd() {
    if (insideRepetition_) {
      insideRepetition_ = false;
      return;
    }
    
    tape_.insert("\\", -1);
  }
  
  private void processSubExpressionStart() {
    int next = tape_.read();
    if (next < 0) {
      return;
    }
    
    if (63 != next) {
      Subexpresion sub = new Subexpresion(null);
      start_ = tape_.currentPos_;
      parsingSubexpressions_.push(sub);
      subexpressions_.add(sub);
      
      tape_.move(-1);
      return;
    }
    
    next = tape_.read();
    if (next < 0) {
      return;
    }
    if (58 != next) {
      Subexpresion sub = new Subexpresion(null);
      start_ = tape_.currentPos_;
      parsingSubexpressions_.push(sub);
      subexpressions_.add(sub);
      
      tape_.move(-1);
      return;
    }
    
    Subexpresion sub = new Subexpresion(null);
    start_ = tape_.currentPos_;
    parsingSubexpressions_.push(sub);
  }
  
  private void processSubExpressionEnd() {
    if (parsingSubexpressions_.isEmpty()) {
      return;
    }
    Subexpresion sub = (Subexpresion)parsingSubexpressions_.pop();
    closed_ = true;
    end_ = tape_.currentPos_;
    
    int next = tape_.read();
    optional_ = (63 == next);
    tape_.move(-1);
  }
  
  private void processEscapeSequence() {
    int escapeSequence = tape_.read();
    if (escapeSequence < 0) {
      return;
    }
    
    if (120 == escapeSequence)
    {

      tape_.move(2);
      return;
    }
    
    if (117 == escapeSequence)
    {

      tape_.move(4);
      return;
    }
    
    if ("ACEFGHIJKLMNOPQRTUVXYZaeghijklmpqyz".indexOf(escapeSequence) > -1)
    {
      tape_.move(-2);
      tape_.replace("");
      tape_.move(1);
      return;
    }
    
    if ((insideCharClass_) && (98 == escapeSequence))
    {
      tape_.move(-1);
      tape_.replace("cH");
      tape_.move(2);
      return;
    }
    
    if ("0123456789".indexOf(escapeSequence) > -1) {
      handleBackReferenceOrOctal(escapeSequence);
    }
  }
  
  private boolean handleBackReferenceOrOctal(int aFirstChar)
  {
    StringBuilder tmpNo = new StringBuilder(Character.toString((char)aFirstChar));
    int tmpInsertPos = -1;
    int next = tape_.read();
    if (next > -1) {
      if ("0123456789".indexOf(next) > -1) {
        tmpNo.append(next);
        tmpInsertPos--;
        next = tape_.read();
        if (next > -1) {
          if ("0123456789".indexOf(next) > -1) {
            tmpNo.append(next);
            tmpInsertPos--;
          }
          else {
            tape_.move(-1);
          }
        }
      }
      else {
        tape_.move(-1);
        if (48 == aFirstChar)
        {
          tape_.insert("x0", -1);
          return false;
        }
      }
    }
    
    if (tmpNo.charAt(0) == '0')
    {
      return false;
    }
    
    int value = Integer.parseInt(tmpNo.toString());
    if (value > subexpressions_.size())
    {
      tape_.insert("0", tmpInsertPos);
      return false;
    }
    


    if ((insideCharClass_) || 
      ((value > 0) && (value <= subexpressions_.size()) && (!subexpressions_.get(value - 1)).closed_)) || 
      (value > subexpressions_.size()))
    {
      for (int i = tmpInsertPos; i <= 0; i++) {
        tape_.move(-1);
        tape_.replace("");
      }
    }
    

    Subexpresion back = (Subexpresion)subexpressions_.get(value - 1);
    if ((optional_) && (!enhanced_))
    {
      int insertPos = start_ - 1;
      tape_.insertAt("(?:", insertPos);
      for (Subexpresion subexp : subexpressions_) {
        if (start_ > insertPos) {
          start_ += 3;
        }
        if (end_ > insertPos) {
          end_ += 3;
        }
      }
      
      insertPos = end_ + 1;
      tape_.insertAt(")", insertPos);
      for (Subexpresion subexp : subexpressions_) {
        if (start_ > insertPos) {
          start_ += 1;
        }
        if (end_ > insertPos) {
          end_ += 1;
        }
      }
    }
    return true;
  }
}
