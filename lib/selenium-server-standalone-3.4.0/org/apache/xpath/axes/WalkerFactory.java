package org.apache.xpath.axes;

import java.io.PrintStream;
import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.Expression;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.OpMap;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.patterns.ContextMatchStepPattern;
import org.apache.xpath.patterns.FunctionPattern;
import org.apache.xpath.patterns.StepPattern;

















public class WalkerFactory
{
  static final boolean DEBUG_PATTERN_CREATION = false;
  static final boolean DEBUG_WALKER_CREATION = false;
  static final boolean DEBUG_ITERATOR_CREATION = false;
  public static final int BITS_COUNT = 255;
  public static final int BITS_RESERVED = 3840;
  public static final int BIT_PREDICATE = 4096;
  public static final int BIT_ANCESTOR = 8192;
  public static final int BIT_ANCESTOR_OR_SELF = 16384;
  public static final int BIT_ATTRIBUTE = 32768;
  public static final int BIT_CHILD = 65536;
  public static final int BIT_DESCENDANT = 131072;
  public static final int BIT_DESCENDANT_OR_SELF = 262144;
  public static final int BIT_FOLLOWING = 524288;
  public static final int BIT_FOLLOWING_SIBLING = 1048576;
  public static final int BIT_NAMESPACE = 2097152;
  public static final int BIT_PARENT = 4194304;
  public static final int BIT_PRECEDING = 8388608;
  public static final int BIT_PRECEDING_SIBLING = 16777216;
  public static final int BIT_SELF = 33554432;
  public static final int BIT_FILTER = 67108864;
  public static final int BIT_ROOT = 134217728;
  public static final int BITMASK_TRAVERSES_OUTSIDE_SUBTREE = 234381312;
  public static final int BIT_BACKWARDS_SELF = 268435456;
  public static final int BIT_ANY_DESCENDANT_FROM_ROOT = 536870912;
  public static final int BIT_NODETEST_ANY = 1073741824;
  public static final int BIT_MATCH_PATTERN = Integer.MIN_VALUE;
  
  public WalkerFactory() {}
  
  static AxesWalker loadOneWalker(WalkingIterator lpi, Compiler compiler, int stepOpCodePos)
    throws TransformerException
  {
    AxesWalker firstWalker = null;
    int stepType = compiler.getOp(stepOpCodePos);
    
    if (stepType != -1)
    {



      firstWalker = createDefaultWalker(compiler, stepType, lpi, 0);
      
      firstWalker.init(compiler, stepOpCodePos, stepType);
    }
    
    return firstWalker;
  }
  

















  static AxesWalker loadWalkers(WalkingIterator lpi, Compiler compiler, int stepOpCodePos, int stepIndex)
    throws TransformerException
  {
    AxesWalker firstWalker = null;
    AxesWalker prevWalker = null;
    
    int analysis = analyze(compiler, stepOpCodePos, stepIndex);
    int stepType;
    while (-1 != (stepType = compiler.getOp(stepOpCodePos)))
    {
      AxesWalker walker = createDefaultWalker(compiler, stepOpCodePos, lpi, analysis);
      
      walker.init(compiler, stepOpCodePos, stepType);
      walker.exprSetParent(lpi);
      

      if (null == firstWalker)
      {
        firstWalker = walker;
      }
      else
      {
        prevWalker.setNextWalker(walker);
        walker.setPrevWalker(prevWalker);
      }
      
      prevWalker = walker;
      stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
      
      if (stepOpCodePos < 0) {
        break;
      }
    }
    return firstWalker;
  }
  
  public static boolean isSet(int analysis, int bits)
  {
    return 0 != (analysis & bits);
  }
  
  public static void diagnoseIterator(String name, int analysis, Compiler compiler)
  {
    System.out.println(compiler.toString() + ", " + name + ", " + Integer.toBinaryString(analysis) + ", " + getAnalysisString(analysis));
  }
  

















  public static DTMIterator newDTMIterator(Compiler compiler, int opPos, boolean isTopLevel)
    throws TransformerException
  {
    int firstStepPos = OpMap.getFirstChildPos(opPos);
    int analysis = analyze(compiler, firstStepPos, 0);
    boolean isOneStep = isOneStep(analysis);
    
    DTMIterator iter;
    DTMIterator iter;
    if ((isOneStep) && (walksSelfOnly(analysis)) && (isWild(analysis)) && (!hasPredicate(analysis)))
    {






      iter = new SelfIteratorNoPredicate(compiler, opPos, analysis);
    } else {
      DTMIterator iter;
      if ((walksChildrenOnly(analysis)) && (isOneStep))
      {
        DTMIterator iter;
        
        if ((isWild(analysis)) && (!hasPredicate(analysis)))
        {




          iter = new ChildIterator(compiler, opPos, analysis);


        }
        else
        {


          iter = new ChildTestIterator(compiler, opPos, analysis);
        }
      } else {
        DTMIterator iter;
        if ((isOneStep) && (walksAttributes(analysis)))
        {





          iter = new AttributeIterator(compiler, opPos, analysis);
        } else { DTMIterator iter;
          if ((isOneStep) && (!walksFilteredList(analysis))) {
            DTMIterator iter;
            if ((!walksNamespaces(analysis)) && ((walksInDocOrder(analysis)) || (isSet(analysis, 4194304))))
            {






              iter = new OneStepIteratorForward(compiler, opPos, analysis);



            }
            else
            {


              iter = new OneStepIterator(compiler, opPos, analysis);
            }
          }
          else
          {
            DTMIterator iter;
            









            if (isOptimizableForDescendantIterator(compiler, firstStepPos, 0))
            {







              iter = new DescendantIterator(compiler, opPos, analysis);
            }
            else {
              DTMIterator iter;
              if (isNaturalDocOrder(compiler, firstStepPos, 0, analysis))
              {





                iter = new WalkingIterator(compiler, opPos, analysis, true);




              }
              else
              {



                iter = new WalkingIteratorSorted(compiler, opPos, analysis, true); }
            }
          } } } }
    if ((iter instanceof LocPathIterator)) {
      ((LocPathIterator)iter).setIsTopLevel(isTopLevel);
    }
    return iter;
  }
  















  public static int getAxisFromStep(Compiler compiler, int stepOpCodePos)
    throws TransformerException
  {
    int stepType = compiler.getOp(stepOpCodePos);
    
    switch (stepType)
    {
    case 43: 
      return 6;
    case 44: 
      return 7;
    case 46: 
      return 11;
    case 47: 
      return 12;
    case 45: 
      return 10;
    case 49: 
      return 9;
    case 37: 
      return 0;
    case 38: 
      return 1;
    case 39: 
      return 2;
    case 50: 
      return 19;
    case 40: 
      return 3;
    case 42: 
      return 5;
    case 41: 
      return 4;
    case 48: 
      return 13;
    case 22: 
    case 23: 
    case 24: 
    case 25: 
      return 20;
    }
    
    throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
  }
  






  public static int getAnalysisBitFromAxes(int axis)
  {
    switch (axis)
    {
    case 0: 
      return 8192;
    case 1: 
      return 16384;
    case 2: 
      return 32768;
    case 3: 
      return 65536;
    case 4: 
      return 131072;
    case 5: 
      return 262144;
    case 6: 
      return 524288;
    case 7: 
      return 1048576;
    case 8: 
    case 9: 
      return 2097152;
    case 10: 
      return 4194304;
    case 11: 
      return 8388608;
    case 12: 
      return 16777216;
    case 13: 
      return 33554432;
    case 14: 
      return 262144;
    
    case 16: 
    case 17: 
    case 18: 
      return 536870912;
    case 19: 
      return 134217728;
    case 20: 
      return 67108864;
    }
    return 67108864;
  }
  


  static boolean functionProximateOrContainsProximate(Compiler compiler, int opPos)
  {
    int endFunc = opPos + compiler.getOp(opPos + 1) - 1;
    opPos = OpMap.getFirstChildPos(opPos);
    int funcID = compiler.getOp(opPos);
    


    switch (funcID)
    {
    case 1: 
    case 2: 
      return true;
    }
    opPos++;
    int i = 0;
    for (int p = opPos; p < endFunc; i++)
    {
      int innerExprOpPos = p + 2;
      int argOp = compiler.getOp(innerExprOpPos);
      boolean prox = isProximateInnerExpr(compiler, innerExprOpPos);
      if (prox) {
        return true;
      }
      p = compiler.getNextOpPos(p);
    }
    







    return false;
  }
  
  static boolean isProximateInnerExpr(Compiler compiler, int opPos)
  {
    int op = compiler.getOp(opPos);
    int innerExprOpPos = opPos + 2;
    boolean isProx; switch (op)
    {
    case 26: 
      if (isProximateInnerExpr(compiler, innerExprOpPos)) {
        return true;
      }
      break;
    case 21: case 22: 
    case 27: 
    case 28: 
      break;
    case 25: 
      isProx = functionProximateOrContainsProximate(compiler, opPos);
      if (isProx) {
        return true;
      }
      break;
    case 5: case 6: 
    case 7: 
    case 8: 
    case 9: 
      int leftPos = OpMap.getFirstChildPos(op);
      int rightPos = compiler.getNextOpPos(leftPos);
      isProx = isProximateInnerExpr(compiler, leftPos);
      if (isProx)
        return true;
      isProx = isProximateInnerExpr(compiler, rightPos);
      if (isProx)
        return true;
      break;
    case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 20: case 23: case 24: default: 
      return true;
    }
    return false;
  }
  




  public static boolean mightBeProximate(Compiler compiler, int opPos, int stepType)
    throws TransformerException
  {
    boolean mightBeProximate = false;
    
    int argLen;
    switch (stepType)
    {
    case 22: 
    case 23: 
    case 24: 
    case 25: 
      argLen = compiler.getArgLength(opPos);
      break;
    default: 
      argLen = compiler.getArgLengthOfStep(opPos);
    }
    
    int predPos = compiler.getFirstPredicateOpPos(opPos);
    int count = 0;
    
    while (29 == compiler.getOp(predPos))
    {
      count++;
      
      int innerExprOpPos = predPos + 2;
      int predOp = compiler.getOp(innerExprOpPos);
      boolean isProx;
      switch (predOp)
      {
      case 22: 
        return true;
      case 28: 
        break;
      
      case 19: 
      case 27: 
        return true;
      case 25: 
        isProx = functionProximateOrContainsProximate(compiler, innerExprOpPos);
        
        if (isProx) {
          return true;
        }
        break;
      case 5: case 6: 
      case 7: 
      case 8: 
      case 9: 
        int leftPos = OpMap.getFirstChildPos(innerExprOpPos);
        int rightPos = compiler.getNextOpPos(leftPos);
        isProx = isProximateInnerExpr(compiler, leftPos);
        if (isProx)
          return true;
        isProx = isProximateInnerExpr(compiler, rightPos);
        if (isProx)
          return true;
        break;
      case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17: case 18: case 20: case 21: case 23: case 24: case 26: default: 
        return true;
      }
      
      predPos = compiler.getNextOpPos(predPos);
    }
    
    return mightBeProximate;
  }
  

















  private static boolean isOptimizableForDescendantIterator(Compiler compiler, int stepOpCodePos, int stepIndex)
    throws TransformerException
  {
    int stepCount = 0;
    boolean foundDorDS = false;
    boolean foundSelf = false;
    boolean foundDS = false;
    
    int nodeTestType = 1033;
    int stepType;
    while (-1 != (stepType = compiler.getOp(stepOpCodePos)))
    {


      if ((nodeTestType != 1033) && (nodeTestType != 35)) {
        return false;
      }
      stepCount++;
      if (stepCount > 3) {
        return false;
      }
      boolean mightBeProximate = mightBeProximate(compiler, stepOpCodePos, stepType);
      if (mightBeProximate) {
        return false;
      }
      switch (stepType)
      {
      case 22: 
      case 23: 
      case 24: 
      case 25: 
      case 37: 
      case 38: 
      case 39: 
      case 43: 
      case 44: 
      case 45: 
      case 46: 
      case 47: 
      case 49: 
      case 51: 
      case 52: 
      case 53: 
        return false;
      case 50: 
        if (1 != stepCount)
          return false;
        break;
      case 40: 
        if ((!foundDS) && ((!foundDorDS) || (!foundSelf)))
          return false;
        break;
      case 42: 
        foundDS = true;
      case 41: 
        if (3 == stepCount)
          return false;
        foundDorDS = true;
        break;
      case 48: 
        if (1 != stepCount)
          return false;
        foundSelf = true;
        break;
      case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: default: 
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
      }
      
      
      nodeTestType = compiler.getStepTestType(stepOpCodePos);
      
      int nextStepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
      
      if (nextStepOpCodePos < 0) {
        break;
      }
      if (-1 != compiler.getOp(nextStepOpCodePos))
      {
        if (compiler.countPredicates(stepOpCodePos) > 0)
        {
          return false;
        }
      }
      
      stepOpCodePos = nextStepOpCodePos;
    }
    
    return true;
  }
  


















  private static int analyze(Compiler compiler, int stepOpCodePos, int stepIndex)
    throws TransformerException
  {
    int stepCount = 0;
    int analysisResult = 0;
    int stepType;
    while (-1 != (stepType = compiler.getOp(stepOpCodePos)))
    {
      stepCount++;
      





      boolean predAnalysis = analyzePredicate(compiler, stepOpCodePos, stepType);
      

      if (predAnalysis) {
        analysisResult |= 0x1000;
      }
      switch (stepType)
      {
      case 22: 
      case 23: 
      case 24: 
      case 25: 
        analysisResult |= 0x4000000;
        break;
      case 50: 
        analysisResult |= 0x8000000;
        break;
      case 37: 
        analysisResult |= 0x2000;
        break;
      case 38: 
        analysisResult |= 0x4000;
        break;
      case 39: 
        analysisResult |= 0x8000;
        break;
      case 49: 
        analysisResult |= 0x200000;
        break;
      case 40: 
        analysisResult |= 0x10000;
        break;
      case 41: 
        analysisResult |= 0x20000;
        break;
      

      case 42: 
        if ((2 == stepCount) && (134217728 == analysisResult))
        {
          analysisResult |= 0x20000000;
        }
        
        analysisResult |= 0x40000;
        break;
      case 43: 
        analysisResult |= 0x80000;
        break;
      case 44: 
        analysisResult |= 0x100000;
        break;
      case 46: 
        analysisResult |= 0x800000;
        break;
      case 47: 
        analysisResult |= 0x1000000;
        break;
      case 45: 
        analysisResult |= 0x400000;
        break;
      case 48: 
        analysisResult |= 0x2000000;
        break;
      case 51: 
        analysisResult |= 0x80008000;
        break;
      case 52: 
        analysisResult |= 0x80002000;
        break;
      case 53: 
        analysisResult |= 0x80400000;
        break;
      case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: default: 
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
      }
      
      
      if (1033 == compiler.getOp(stepOpCodePos + 3))
      {
        analysisResult |= 0x40000000;
      }
      
      stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
      
      if (stepOpCodePos < 0) {
        break;
      }
    }
    analysisResult |= stepCount & 0xFF;
    
    return analysisResult;
  }
  








  public static boolean isDownwardAxisOfMany(int axis)
  {
    return (5 == axis) || (4 == axis) || (6 == axis) || (11 == axis);
  }
  







































  static StepPattern loadSteps(MatchPatternIterator mpi, Compiler compiler, int stepOpCodePos, int stepIndex)
    throws TransformerException
  {
    StepPattern step = null;
    StepPattern firstStep = null;StepPattern prevStep = null;
    int analysis = analyze(compiler, stepOpCodePos, stepIndex);
    int stepType;
    while (-1 != (stepType = compiler.getOp(stepOpCodePos)))
    {
      step = createDefaultStepPattern(compiler, stepOpCodePos, mpi, analysis, firstStep, prevStep);
      

      if (null == firstStep)
      {
        firstStep = step;

      }
      else
      {

        step.setRelativePathPattern(prevStep);
      }
      
      prevStep = step;
      stepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
      
      if (stepOpCodePos < 0) {
        break;
      }
    }
    int axis = 13;
    int paxis = 13;
    StepPattern tail = step;
    for (StepPattern pat = step; null != pat; 
        pat = pat.getRelativePathPattern())
    {
      int nextAxis = pat.getAxis();
      
      pat.setAxis(axis);
      























      int whatToShow = pat.getWhatToShow();
      if ((whatToShow == 2) || (whatToShow == 4096))
      {

        int newAxis = whatToShow == 2 ? 2 : 9;
        
        if (isDownwardAxisOfMany(axis))
        {
          StepPattern attrPat = new StepPattern(whatToShow, pat.getNamespace(), pat.getLocalName(), newAxis, 0);
          



          XNumber score = pat.getStaticScore();
          pat.setNamespace(null);
          pat.setLocalName("*");
          attrPat.setPredicates(pat.getPredicates());
          pat.setPredicates(null);
          pat.setWhatToShow(1);
          StepPattern rel = pat.getRelativePathPattern();
          pat.setRelativePathPattern(attrPat);
          attrPat.setRelativePathPattern(rel);
          attrPat.setStaticScore(score);
          




          if (11 == pat.getAxis()) {
            pat.setAxis(15);
          }
          else if (4 == pat.getAxis()) {
            pat.setAxis(5);
          }
          pat = attrPat;
        }
        else if (3 == pat.getAxis())
        {


          pat.setAxis(2);
        }
      }
      axis = nextAxis;
      
      tail = pat;
    }
    
    if (axis < 16)
    {
      StepPattern selfPattern = new ContextMatchStepPattern(axis, paxis);
      
      XNumber score = tail.getStaticScore();
      tail.setRelativePathPattern(selfPattern);
      tail.setStaticScore(score);
      selfPattern.setStaticScore(score);
    }
    






    return step;
  }
  
























  private static StepPattern createDefaultStepPattern(Compiler compiler, int opPos, MatchPatternIterator mpi, int analysis, StepPattern tail, StepPattern head)
    throws TransformerException
  {
    int stepType = compiler.getOp(opPos);
    boolean simpleInit = false;
    boolean prevIsOneStepDown = true;
    
    int whatToShow = compiler.getWhatToShow(opPos);
    StepPattern ai = null;
    int axis;
    int predicateAxis;
    switch (stepType)
    {
    case 22: 
    case 23: 
    case 24: 
    case 25: 
      prevIsOneStepDown = false;
      
      Expression expr;
      
      switch (stepType)
      {
      case 22: 
      case 23: 
      case 24: 
      case 25: 
        expr = compiler.compile(opPos);
        break;
      default: 
        expr = compiler.compile(opPos + 2);
      }
      
      axis = 20;
      predicateAxis = 20;
      ai = new FunctionPattern(expr, axis, predicateAxis);
      simpleInit = true;
      break;
    case 50: 
      whatToShow = 1280;
      

      axis = 19;
      predicateAxis = 19;
      ai = new StepPattern(1280, axis, predicateAxis);
      

      break;
    case 39: 
      whatToShow = 2;
      axis = 10;
      predicateAxis = 2;
      
      break;
    case 49: 
      whatToShow = 4096;
      axis = 10;
      predicateAxis = 9;
      
      break;
    case 37: 
      axis = 4;
      predicateAxis = 0;
      break;
    case 40: 
      axis = 10;
      predicateAxis = 3;
      break;
    case 38: 
      axis = 5;
      predicateAxis = 1;
      break;
    case 48: 
      axis = 13;
      predicateAxis = 13;
      break;
    case 45: 
      axis = 3;
      predicateAxis = 10;
      break;
    case 47: 
      axis = 7;
      predicateAxis = 12;
      break;
    case 46: 
      axis = 6;
      predicateAxis = 11;
      break;
    case 44: 
      axis = 12;
      predicateAxis = 7;
      break;
    case 43: 
      axis = 11;
      predicateAxis = 6;
      break;
    case 42: 
      axis = 1;
      predicateAxis = 5;
      break;
    case 41: 
      axis = 0;
      predicateAxis = 4;
      break;
    case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: default: 
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
    }
    
    if (null == ai)
    {
      whatToShow = compiler.getWhatToShow(opPos);
      ai = new StepPattern(whatToShow, compiler.getStepNS(opPos), compiler.getStepLocalName(opPos), axis, predicateAxis);
    }
    












    int argLen = compiler.getFirstPredicateOpPos(opPos);
    
    ai.setPredicates(compiler.getCompiledPredicates(argLen));
    
    return ai;
  }
  








  static boolean analyzePredicate(Compiler compiler, int opPos, int stepType)
    throws TransformerException
  {
    int argLen;
    






    switch (stepType)
    {
    case 22: 
    case 23: 
    case 24: 
    case 25: 
      argLen = compiler.getArgLength(opPos);
      break;
    default: 
      argLen = compiler.getArgLengthOfStep(opPos);
    }
    
    int pos = compiler.getFirstPredicateOpPos(opPos);
    int nPredicates = compiler.countPredicates(pos);
    
    return nPredicates > 0;
  }
  















  private static AxesWalker createDefaultWalker(Compiler compiler, int opPos, WalkingIterator lpi, int analysis)
  {
    AxesWalker ai = null;
    int stepType = compiler.getOp(opPos);
    








    boolean simpleInit = false;
    int totalNumberWalkers = analysis & 0xFF;
    boolean prevIsOneStepDown = true;
    
    switch (stepType)
    {
    case 22: 
    case 23: 
    case 24: 
    case 25: 
      prevIsOneStepDown = false;
      




      ai = new FilterExprWalker(lpi);
      simpleInit = true;
      break;
    case 50: 
      ai = new AxesWalker(lpi, 19);
      break;
    case 37: 
      prevIsOneStepDown = false;
      ai = new ReverseAxesWalker(lpi, 0);
      break;
    case 38: 
      prevIsOneStepDown = false;
      ai = new ReverseAxesWalker(lpi, 1);
      break;
    case 39: 
      ai = new AxesWalker(lpi, 2);
      break;
    case 49: 
      ai = new AxesWalker(lpi, 9);
      break;
    case 40: 
      ai = new AxesWalker(lpi, 3);
      break;
    case 41: 
      prevIsOneStepDown = false;
      ai = new AxesWalker(lpi, 4);
      break;
    case 42: 
      prevIsOneStepDown = false;
      ai = new AxesWalker(lpi, 5);
      break;
    case 43: 
      prevIsOneStepDown = false;
      ai = new AxesWalker(lpi, 6);
      break;
    case 44: 
      prevIsOneStepDown = false;
      ai = new AxesWalker(lpi, 7);
      break;
    case 46: 
      prevIsOneStepDown = false;
      ai = new ReverseAxesWalker(lpi, 11);
      break;
    case 47: 
      prevIsOneStepDown = false;
      ai = new ReverseAxesWalker(lpi, 12);
      break;
    case 45: 
      prevIsOneStepDown = false;
      ai = new ReverseAxesWalker(lpi, 10);
      break;
    case 48: 
      ai = new AxesWalker(lpi, 13);
      break;
    case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: default: 
      throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
    }
    
    
    if (simpleInit)
    {
      ai.initNodeTest(-1);
    }
    else
    {
      int whatToShow = compiler.getWhatToShow(opPos);
      







      if ((0 == (whatToShow & 0x1043)) || (whatToShow == -1))
      {

        ai.initNodeTest(whatToShow);
      }
      else {
        ai.initNodeTest(whatToShow, compiler.getStepNS(opPos), compiler.getStepLocalName(opPos));
      }
    }
    

    return ai;
  }
  
  public static String getAnalysisString(int analysis)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("count: " + getStepCount(analysis) + " ");
    if ((analysis & 0x40000000) != 0)
    {
      buf.append("NTANY|");
    }
    if ((analysis & 0x1000) != 0)
    {
      buf.append("PRED|");
    }
    if ((analysis & 0x2000) != 0)
    {
      buf.append("ANC|");
    }
    if ((analysis & 0x4000) != 0)
    {
      buf.append("ANCOS|");
    }
    if ((analysis & 0x8000) != 0)
    {
      buf.append("ATTR|");
    }
    if ((analysis & 0x10000) != 0)
    {
      buf.append("CH|");
    }
    if ((analysis & 0x20000) != 0)
    {
      buf.append("DESC|");
    }
    if ((analysis & 0x40000) != 0)
    {
      buf.append("DESCOS|");
    }
    if ((analysis & 0x80000) != 0)
    {
      buf.append("FOL|");
    }
    if ((analysis & 0x100000) != 0)
    {
      buf.append("FOLS|");
    }
    if ((analysis & 0x200000) != 0)
    {
      buf.append("NS|");
    }
    if ((analysis & 0x400000) != 0)
    {
      buf.append("P|");
    }
    if ((analysis & 0x800000) != 0)
    {
      buf.append("PREC|");
    }
    if ((analysis & 0x1000000) != 0)
    {
      buf.append("PRECS|");
    }
    if ((analysis & 0x2000000) != 0)
    {
      buf.append(".|");
    }
    if ((analysis & 0x4000000) != 0)
    {
      buf.append("FLT|");
    }
    if ((analysis & 0x8000000) != 0)
    {
      buf.append("R|");
    }
    return buf.toString();
  }
  









  public static boolean hasPredicate(int analysis)
  {
    return 0 != (analysis & 0x1000);
  }
  
  public static boolean isWild(int analysis)
  {
    return 0 != (analysis & 0x40000000);
  }
  
  public static boolean walksAncestors(int analysis)
  {
    return isSet(analysis, 24576);
  }
  
  public static boolean walksAttributes(int analysis)
  {
    return 0 != (analysis & 0x8000);
  }
  
  public static boolean walksNamespaces(int analysis)
  {
    return 0 != (analysis & 0x200000);
  }
  
  public static boolean walksChildren(int analysis)
  {
    return 0 != (analysis & 0x10000);
  }
  
  public static boolean walksDescendants(int analysis)
  {
    return isSet(analysis, 393216);
  }
  
  public static boolean walksSubtree(int analysis)
  {
    return isSet(analysis, 458752);
  }
  
  public static boolean walksSubtreeOnlyMaybeAbsolute(int analysis)
  {
    return (walksSubtree(analysis)) && (!walksExtraNodes(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis));
  }
  




  public static boolean walksSubtreeOnly(int analysis)
  {
    return (walksSubtreeOnlyMaybeAbsolute(analysis)) && (!isAbsolute(analysis));
  }
  


  public static boolean walksFilteredList(int analysis)
  {
    return isSet(analysis, 67108864);
  }
  
  public static boolean walksSubtreeOnlyFromRootOrContext(int analysis)
  {
    return (walksSubtree(analysis)) && (!walksExtraNodes(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && (!isSet(analysis, 67108864));
  }
  





  public static boolean walksInDocOrder(int analysis)
  {
    return ((walksSubtreeOnlyMaybeAbsolute(analysis)) || (walksExtraNodesOnly(analysis)) || (walksFollowingOnlyMaybeAbsolute(analysis))) && (!isSet(analysis, 67108864));
  }
  




  public static boolean walksFollowingOnlyMaybeAbsolute(int analysis)
  {
    return (isSet(analysis, 35127296)) && (!walksSubtree(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis));
  }
  




  public static boolean walksUp(int analysis)
  {
    return isSet(analysis, 4218880);
  }
  
  public static boolean walksSideways(int analysis)
  {
    return isSet(analysis, 26738688);
  }
  

  public static boolean walksExtraNodes(int analysis)
  {
    return isSet(analysis, 2129920);
  }
  
  public static boolean walksExtraNodesOnly(int analysis)
  {
    return (walksExtraNodes(analysis)) && (!isSet(analysis, 33554432)) && (!walksSubtree(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && (!isAbsolute(analysis));
  }
  






  public static boolean isAbsolute(int analysis)
  {
    return isSet(analysis, 201326592);
  }
  
  public static boolean walksChildrenOnly(int analysis)
  {
    return (walksChildren(analysis)) && (!isSet(analysis, 33554432)) && (!walksExtraNodes(analysis)) && (!walksDescendants(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && ((!isAbsolute(analysis)) || (isSet(analysis, 134217728)));
  }
  







  public static boolean walksChildrenAndExtraAndSelfOnly(int analysis)
  {
    return (walksChildren(analysis)) && (!walksDescendants(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && ((!isAbsolute(analysis)) || (isSet(analysis, 134217728)));
  }
  





  public static boolean walksDescendantsAndExtraAndSelfOnly(int analysis)
  {
    return (!walksChildren(analysis)) && (walksDescendants(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && ((!isAbsolute(analysis)) || (isSet(analysis, 134217728)));
  }
  





  public static boolean walksSelfOnly(int analysis)
  {
    return (isSet(analysis, 33554432)) && (!walksSubtree(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && (!isAbsolute(analysis));
  }
  






  public static boolean walksUpOnly(int analysis)
  {
    return (!walksSubtree(analysis)) && (walksUp(analysis)) && (!walksSideways(analysis)) && (!isAbsolute(analysis));
  }
  




  public static boolean walksDownOnly(int analysis)
  {
    return (walksSubtree(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && (!isAbsolute(analysis));
  }
  




  public static boolean walksDownExtraOnly(int analysis)
  {
    return (walksSubtree(analysis)) && (walksExtraNodes(analysis)) && (!walksUp(analysis)) && (!walksSideways(analysis)) && (!isAbsolute(analysis));
  }
  




  public static boolean canSkipSubtrees(int analysis)
  {
    return isSet(analysis, 65536) | walksSideways(analysis);
  }
  

  public static boolean canCrissCross(int analysis)
  {
    if (walksSelfOnly(analysis))
      return false;
    if ((walksDownOnly(analysis)) && (!canSkipSubtrees(analysis)))
      return false;
    if (walksChildrenAndExtraAndSelfOnly(analysis))
      return false;
    if (walksDescendantsAndExtraAndSelfOnly(analysis))
      return false;
    if (walksUpOnly(analysis))
      return false;
    if (walksExtraNodesOnly(analysis))
      return false;
    if ((walksSubtree(analysis)) && ((walksSideways(analysis)) || (walksUp(analysis)) || (canSkipSubtrees(analysis))))
    {


      return true;
    }
    return false;
  }
  










  public static boolean isNaturalDocOrder(int analysis)
  {
    if ((canCrissCross(analysis)) || (isSet(analysis, 2097152)) || (walksFilteredList(analysis)))
    {
      return false;
    }
    if (walksInDocOrder(analysis)) {
      return true;
    }
    return false;
  }
  















  private static boolean isNaturalDocOrder(Compiler compiler, int stepOpCodePos, int stepIndex, int analysis)
    throws TransformerException
  {
    if (canCrissCross(analysis)) {
      return false;
    }
    

    if (isSet(analysis, 2097152)) {
      return false;
    }
    





    if ((isSet(analysis, 1572864)) && (isSet(analysis, 25165824)))
    {
      return false;
    }
    





    int stepCount = 0;
    boolean foundWildAttribute = false;
    



    int potentialDuplicateMakingStepCount = 0;
    int stepType;
    while (-1 != (stepType = compiler.getOp(stepOpCodePos)))
    {
      stepCount++;
      
      switch (stepType)
      {
      case 39: 
      case 51: 
        if (foundWildAttribute) {
          return false;
        }
        


        String localName = compiler.getStepLocalName(stepOpCodePos);
        
        if (localName.equals("*"))
        {
          foundWildAttribute = true;
        }
        break;
      case 22: 
      case 23: 
      case 24: 
      case 25: 
      case 37: 
      case 38: 
      case 41: 
      case 42: 
      case 43: 
      case 44: 
      case 45: 
      case 46: 
      case 47: 
      case 49: 
      case 52: 
      case 53: 
        if (potentialDuplicateMakingStepCount > 0)
          return false;
        potentialDuplicateMakingStepCount++;
      case 40: 
      case 48: 
      case 50: 
        if (foundWildAttribute)
          return false;
        break;
      case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: default: 
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", new Object[] { Integer.toString(stepType) }));
      }
      
      
      int nextStepOpCodePos = compiler.getNextStepPos(stepOpCodePos);
      
      if (nextStepOpCodePos < 0) {
        break;
      }
      stepOpCodePos = nextStepOpCodePos;
    }
    
    return true;
  }
  
  public static boolean isOneStep(int analysis)
  {
    return (analysis & 0xFF) == 1;
  }
  
  public static int getStepCount(int analysis)
  {
    return analysis & 0xFF;
  }
}
