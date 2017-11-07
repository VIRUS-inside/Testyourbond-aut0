package org.apache.xalan.templates;

import java.io.PrintStream;
import java.util.Vector;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPath;
import org.apache.xpath.axes.AxesWalker;
import org.apache.xpath.axes.FilterExprIteratorSimple;
import org.apache.xpath.axes.FilterExprWalker;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.SelfIteratorNoPredicate;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.axes.WalkingIterator;
import org.apache.xpath.operations.Variable;
import org.apache.xpath.operations.VariableSafeAbsRef;
import org.w3c.dom.DOMException;


























public class RedundentExprEliminator
  extends XSLTVisitor
{
  Vector m_paths;
  Vector m_absPaths;
  boolean m_isSameContext;
  AbsPathChecker m_absPathChecker = new AbsPathChecker();
  
  private static int m_uniquePseudoVarID = 1;
  
  static final String PSUEDOVARNAMESPACE = "http://xml.apache.org/xalan/psuedovar";
  
  public static final boolean DEBUG = false;
  
  public static final boolean DIAGNOSE_NUM_PATHS_REDUCED = false;
  
  public static final boolean DIAGNOSE_MULTISTEPLIST = false;
  
  VarNameCollector m_varNameCollector = new VarNameCollector();
  



  public RedundentExprEliminator()
  {
    m_isSameContext = true;
    m_absPaths = new Vector();
    m_paths = null;
  }
  












  public void eleminateRedundentLocals(ElemTemplateElement psuedoVarRecipient)
  {
    eleminateRedundent(psuedoVarRecipient, m_paths);
  }
  








  public void eleminateRedundentGlobals(StylesheetRoot stylesheet)
  {
    eleminateRedundent(stylesheet, m_absPaths);
  }
  













  protected void eleminateRedundent(ElemTemplateElement psuedoVarRecipient, Vector paths)
  {
    int n = paths.size();
    int numPathsEliminated = 0;
    int numUniquePathsEliminated = 0;
    for (int i = 0; i < n; i++)
    {
      ExpressionOwner owner = (ExpressionOwner)paths.elementAt(i);
      if (null != owner)
      {
        int found = findAndEliminateRedundant(i + 1, i, owner, psuedoVarRecipient, paths);
        if (found > 0)
          numUniquePathsEliminated++;
        numPathsEliminated += found;
      }
    }
    
    eleminateSharedPartialPaths(psuedoVarRecipient, paths);
  }
  











  protected void eleminateSharedPartialPaths(ElemTemplateElement psuedoVarRecipient, Vector paths)
  {
    MultistepExprHolder list = createMultistepExprList(paths);
    if (null != list)
    {



      boolean isGlobal = paths == m_absPaths;
      


      int longestStepsCount = m_stepCount;
      for (int i = longestStepsCount - 1; i >= 1; i--)
      {
        MultistepExprHolder next = list;
        while (null != next)
        {
          if (m_stepCount < i)
            break;
          list = matchAndEliminatePartialPaths(next, list, isGlobal, i, psuedoVarRecipient);
          next = m_next;
        }
      }
    }
  }
  











  protected MultistepExprHolder matchAndEliminatePartialPaths(MultistepExprHolder testee, MultistepExprHolder head, boolean isGlobal, int lengthToTest, ElemTemplateElement varScope)
  {
    if (null == m_exprOwner) {
      return head;
    }
    
    WalkingIterator iter1 = (WalkingIterator)m_exprOwner.getExpression();
    if (partialIsVariable(testee, lengthToTest))
      return head;
    MultistepExprHolder matchedPaths = null;
    MultistepExprHolder matchedPathsTail = null;
    MultistepExprHolder meh = head;
    while (null != meh)
    {
      if ((meh != testee) && (null != m_exprOwner))
      {
        WalkingIterator iter2 = (WalkingIterator)m_exprOwner.getExpression();
        if (stepsEqual(iter1, iter2, lengthToTest))
        {
          if (null == matchedPaths)
          {
            try
            {
              matchedPaths = (MultistepExprHolder)testee.clone();
              m_exprOwner = null;
            }
            catch (CloneNotSupportedException cnse) {}
            matchedPathsTail = matchedPaths;
            m_next = null;
          }
          
          try
          {
            m_next = ((MultistepExprHolder)meh.clone());
            m_exprOwner = null;
          }
          catch (CloneNotSupportedException cnse) {}
          matchedPathsTail = m_next;
          m_next = null;
        }
      }
      meh = m_next;
    }
    
    int matchCount = 0;
    if (null != matchedPaths)
    {
      ElemTemplateElement root = isGlobal ? varScope : findCommonAncestor(matchedPaths);
      WalkingIterator sharedIter = (WalkingIterator)m_exprOwner.getExpression();
      WalkingIterator newIter = createIteratorFromSteps(sharedIter, lengthToTest);
      ElemVariable var = createPseudoVarDecl(root, newIter, isGlobal);
      

      while (null != matchedPaths)
      {
        ExpressionOwner owner = m_exprOwner;
        WalkingIterator iter = (WalkingIterator)owner.getExpression();
        



        LocPathIterator newIter2 = changePartToRef(var.getName(), iter, lengthToTest, isGlobal);
        
        owner.setExpression(newIter2);
        
        matchedPaths = m_next;
      }
    }
    


    return head;
  }
  




  boolean partialIsVariable(MultistepExprHolder testee, int lengthToTest)
  {
    if (1 == lengthToTest)
    {
      WalkingIterator wi = (WalkingIterator)m_exprOwner.getExpression();
      if ((wi.getFirstWalker() instanceof FilterExprWalker))
        return true;
    }
    return false;
  }
  



  protected void diagnoseLineNumber(Expression expr)
  {
    ElemTemplateElement e = getElemFromExpression(expr);
    System.err.println("   " + e.getSystemId() + " Line " + e.getLineNumber());
  }
  





  protected ElemTemplateElement findCommonAncestor(MultistepExprHolder head)
  {
    int numExprs = head.getLength();
    


    ElemTemplateElement[] elems = new ElemTemplateElement[numExprs];
    int[] ancestorCounts = new int[numExprs];
    


    MultistepExprHolder next = head;
    int shortestAncestorCount = 10000;
    for (int i = 0; i < numExprs; i++)
    {
      ElemTemplateElement elem = getElemFromExpression(m_exprOwner.getExpression());
      
      elems[i] = elem;
      int numAncestors = countAncestors(elem);
      ancestorCounts[i] = numAncestors;
      if (numAncestors < shortestAncestorCount)
      {
        shortestAncestorCount = numAncestors;
      }
      next = m_next;
    }
    

    for (int i = 0; i < numExprs; i++)
    {
      if (ancestorCounts[i] > shortestAncestorCount)
      {
        int numStepCorrection = ancestorCounts[i] - shortestAncestorCount;
        for (int j = 0; j < numStepCorrection; j++)
        {
          elems[i] = elems[i].getParentElem();
        }
      }
    }
    


    ElemTemplateElement first = null;
    while (shortestAncestorCount-- >= 0)
    {
      boolean areEqual = true;
      first = elems[0];
      for (int i = 1; i < numExprs; i++)
      {
        if (first != elems[i])
        {
          areEqual = false;
          break;
        }
      }
      

      if ((areEqual) && (isNotSameAsOwner(head, first)) && (first.canAcceptVariables()))
      {





        return first;
      }
      
      for (int i = 0; i < numExprs; i++)
      {
        elems[i] = elems[i].getParentElem();
      }
    }
    
    assertion(false, "Could not find common ancestor!!!");
    return null;
  }
  












  protected boolean isNotSameAsOwner(MultistepExprHolder head, ElemTemplateElement ete)
  {
    MultistepExprHolder next = head;
    while (null != next)
    {
      ElemTemplateElement elemOwner = getElemFromExpression(m_exprOwner.getExpression());
      if (elemOwner == ete)
        return false;
      next = m_next;
    }
    return true;
  }
  






  protected int countAncestors(ElemTemplateElement elem)
  {
    int count = 0;
    while (null != elem)
    {
      count++;
      elem = elem.getParentElem();
    }
    return count;
  }
  






  protected void diagnoseMultistepList(int matchCount, int lengthToTest, boolean isGlobal)
  {
    if (matchCount > 0)
    {
      System.err.print("Found multistep matches: " + matchCount + ", " + lengthToTest + " length");
      
      if (isGlobal) {
        System.err.println(" (global)");
      } else {
        System.err.println();
      }
    }
  }
  








  protected LocPathIterator changePartToRef(QName uniquePseudoVarName, WalkingIterator wi, int numSteps, boolean isGlobal)
  {
    Variable var = new Variable();
    var.setQName(uniquePseudoVarName);
    var.setIsGlobal(isGlobal);
    if (isGlobal) {
      ElemTemplateElement elem = getElemFromExpression(wi);
      StylesheetRoot root = elem.getStylesheetRoot();
      Vector vars = root.getVariablesAndParamsComposed();
      var.setIndex(vars.size() - 1);
    }
    

    AxesWalker walker = wi.getFirstWalker();
    for (int i = 0; i < numSteps; i++)
    {
      assertion(null != walker, "Walker should not be null!");
      walker = walker.getNextWalker();
    }
    
    if (null != walker)
    {

      FilterExprWalker few = new FilterExprWalker(wi);
      few.setInnerExpression(var);
      few.exprSetParent(wi);
      few.setNextWalker(walker);
      walker.setPrevWalker(few);
      wi.setFirstWalker(few);
      return wi;
    }
    

    FilterExprIteratorSimple feis = new FilterExprIteratorSimple(var);
    feis.exprSetParent(wi.exprGetParent());
    return feis;
  }
  









  protected WalkingIterator createIteratorFromSteps(WalkingIterator wi, int numSteps)
  {
    WalkingIterator newIter = new WalkingIterator(wi.getPrefixResolver());
    try
    {
      AxesWalker walker = (AxesWalker)wi.getFirstWalker().clone();
      newIter.setFirstWalker(walker);
      walker.setLocPathIterator(newIter);
      for (int i = 1; i < numSteps; i++)
      {
        AxesWalker next = (AxesWalker)walker.getNextWalker().clone();
        walker.setNextWalker(next);
        next.setLocPathIterator(newIter);
        walker = next;
      }
      walker.setNextWalker(null);
    }
    catch (CloneNotSupportedException cnse)
    {
      throw new WrappedRuntimeException(cnse);
    }
    return newIter;
  }
  










  protected boolean stepsEqual(WalkingIterator iter1, WalkingIterator iter2, int numSteps)
  {
    AxesWalker aw1 = iter1.getFirstWalker();
    AxesWalker aw2 = iter2.getFirstWalker();
    
    for (int i = 0; i < numSteps; i++)
    {
      if ((null == aw1) || (null == aw2)) {
        return false;
      }
      if (!aw1.deepEquals(aw2)) {
        return false;
      }
      aw1 = aw1.getNextWalker();
      aw2 = aw2.getNextWalker();
    }
    
    assertion((null != aw1) || (null != aw2), "Total match is incorrect!");
    
    return true;
  }
  











  protected MultistepExprHolder createMultistepExprList(Vector paths)
  {
    MultistepExprHolder first = null;
    int n = paths.size();
    for (int i = 0; i < n; i++)
    {
      ExpressionOwner eo = (ExpressionOwner)paths.elementAt(i);
      if (null != eo)
      {


        LocPathIterator lpi = (LocPathIterator)eo.getExpression();
        int numPaths = countSteps(lpi);
        if (numPaths > 1)
        {
          if (null == first) {
            first = new MultistepExprHolder(eo, numPaths, null);
          } else
            first = first.addInSortedOrder(eo, numPaths);
        }
      }
    }
    if ((null == first) || (first.getLength() <= 1)) {
      return null;
    }
    return first;
  }
  

















  protected int findAndEliminateRedundant(int start, int firstOccuranceIndex, ExpressionOwner firstOccuranceOwner, ElemTemplateElement psuedoVarRecipient, Vector paths)
    throws DOMException
  {
    MultistepExprHolder head = null;
    MultistepExprHolder tail = null;
    int numPathsFound = 0;
    int n = paths.size();
    
    Expression expr1 = firstOccuranceOwner.getExpression();
    

    boolean isGlobal = paths == m_absPaths;
    LocPathIterator lpi = (LocPathIterator)expr1;
    int stepCount = countSteps(lpi);
    for (int j = start; j < n; j++)
    {
      ExpressionOwner owner2 = (ExpressionOwner)paths.elementAt(j);
      if (null != owner2)
      {
        Expression expr2 = owner2.getExpression();
        boolean isEqual = expr2.deepEquals(lpi);
        if (isEqual)
        {
          LocPathIterator lpi2 = (LocPathIterator)expr2;
          if (null == head)
          {
            head = new MultistepExprHolder(firstOccuranceOwner, stepCount, null);
            tail = head;
            numPathsFound++;
          }
          m_next = new MultistepExprHolder(owner2, stepCount, null);
          tail = m_next;
          

          paths.setElementAt(null, j);
          

          numPathsFound++;
        }
      }
    }
    

    if ((0 == numPathsFound) && (isGlobal))
    {
      head = new MultistepExprHolder(firstOccuranceOwner, stepCount, null);
      numPathsFound++;
    }
    
    if (null != head)
    {
      ElemTemplateElement root = isGlobal ? psuedoVarRecipient : findCommonAncestor(head);
      LocPathIterator sharedIter = (LocPathIterator)m_exprOwner.getExpression();
      ElemVariable var = createPseudoVarDecl(root, sharedIter, isGlobal);
      

      QName uniquePseudoVarName = var.getName();
      while (null != head)
      {
        ExpressionOwner owner = m_exprOwner;
        

        changeToVarRef(uniquePseudoVarName, owner, paths, root);
        head = m_next;
      }
      

      paths.setElementAt(var.getSelect(), firstOccuranceIndex);
    }
    
    return numPathsFound;
  }
  






  protected int oldFindAndEliminateRedundant(int start, int firstOccuranceIndex, ExpressionOwner firstOccuranceOwner, ElemTemplateElement psuedoVarRecipient, Vector paths)
    throws DOMException
  {
    QName uniquePseudoVarName = null;
    boolean foundFirst = false;
    int numPathsFound = 0;
    int n = paths.size();
    Expression expr1 = firstOccuranceOwner.getExpression();
    

    boolean isGlobal = paths == m_absPaths;
    LocPathIterator lpi = (LocPathIterator)expr1;
    for (int j = start; j < n; j++)
    {
      ExpressionOwner owner2 = (ExpressionOwner)paths.elementAt(j);
      if (null != owner2)
      {
        Expression expr2 = owner2.getExpression();
        boolean isEqual = expr2.deepEquals(lpi);
        if (isEqual)
        {
          LocPathIterator lpi2 = (LocPathIterator)expr2;
          if (!foundFirst)
          {
            foundFirst = true;
            


            ElemVariable var = createPseudoVarDecl(psuedoVarRecipient, lpi, isGlobal);
            if (null == var)
              return 0;
            uniquePseudoVarName = var.getName();
            
            changeToVarRef(uniquePseudoVarName, firstOccuranceOwner, paths, psuedoVarRecipient);
            



            paths.setElementAt(var.getSelect(), firstOccuranceIndex);
            numPathsFound++;
          }
          
          changeToVarRef(uniquePseudoVarName, owner2, paths, psuedoVarRecipient);
          

          paths.setElementAt(null, j);
          

          numPathsFound++;
        }
      }
    }
    

    if ((0 == numPathsFound) && (paths == m_absPaths))
    {
      ElemVariable var = createPseudoVarDecl(psuedoVarRecipient, lpi, true);
      if (null == var)
        return 0;
      uniquePseudoVarName = var.getName();
      changeToVarRef(uniquePseudoVarName, firstOccuranceOwner, paths, psuedoVarRecipient);
      paths.setElementAt(var.getSelect(), firstOccuranceIndex);
      numPathsFound++;
    }
    return numPathsFound;
  }
  






  protected int countSteps(LocPathIterator lpi)
  {
    if ((lpi instanceof WalkingIterator))
    {
      WalkingIterator wi = (WalkingIterator)lpi;
      AxesWalker aw = wi.getFirstWalker();
      int count = 0;
      while (null != aw)
      {
        count++;
        aw = aw.getNextWalker();
      }
      return count;
    }
    
    return 1;
  }
  

















  protected void changeToVarRef(QName varName, ExpressionOwner owner, Vector paths, ElemTemplateElement psuedoVarRecipient)
  {
    Variable varRef = paths == m_absPaths ? new VariableSafeAbsRef() : new Variable();
    varRef.setQName(varName);
    if (paths == m_absPaths)
    {
      StylesheetRoot root = (StylesheetRoot)psuedoVarRecipient;
      Vector globalVars = root.getVariablesAndParamsComposed();
      

      varRef.setIndex(globalVars.size() - 1);
      varRef.setIsGlobal(true);
    }
    owner.setExpression(varRef);
  }
  
  private static synchronized int getPseudoVarID() {
    return m_uniquePseudoVarID++;
  }
  












  protected ElemVariable createPseudoVarDecl(ElemTemplateElement psuedoVarRecipient, LocPathIterator lpi, boolean isGlobal)
    throws DOMException
  {
    QName uniquePseudoVarName = new QName("http://xml.apache.org/xalan/psuedovar", "#" + getPseudoVarID());
    
    if (isGlobal)
    {
      return createGlobalPseudoVarDecl(uniquePseudoVarName, (StylesheetRoot)psuedoVarRecipient, lpi);
    }
    

    return createLocalPseudoVarDecl(uniquePseudoVarName, psuedoVarRecipient, lpi);
  }
  













  protected ElemVariable createGlobalPseudoVarDecl(QName uniquePseudoVarName, StylesheetRoot stylesheetRoot, LocPathIterator lpi)
    throws DOMException
  {
    ElemVariable psuedoVar = new ElemVariable();
    psuedoVar.setIsTopLevel(true);
    XPath xpath = new XPath(lpi);
    psuedoVar.setSelect(xpath);
    psuedoVar.setName(uniquePseudoVarName);
    
    Vector globalVars = stylesheetRoot.getVariablesAndParamsComposed();
    psuedoVar.setIndex(globalVars.size());
    globalVars.addElement(psuedoVar);
    return psuedoVar;
  }
  
















  protected ElemVariable createLocalPseudoVarDecl(QName uniquePseudoVarName, ElemTemplateElement psuedoVarRecipient, LocPathIterator lpi)
    throws DOMException
  {
    ElemVariable psuedoVar = new ElemVariablePsuedo();
    
    XPath xpath = new XPath(lpi);
    psuedoVar.setSelect(xpath);
    psuedoVar.setName(uniquePseudoVarName);
    
    ElemVariable var = addVarDeclToElem(psuedoVarRecipient, lpi, psuedoVar);
    
    lpi.exprSetParent(var);
    
    return var;
  }
  







  protected ElemVariable addVarDeclToElem(ElemTemplateElement psuedoVarRecipient, LocPathIterator lpi, ElemVariable psuedoVar)
    throws DOMException
  {
    ElemTemplateElement ete = psuedoVarRecipient.getFirstChildElem();
    
    lpi.callVisitors(null, m_varNameCollector);
    




    if (m_varNameCollector.getVarCount() > 0)
    {
      ElemTemplateElement baseElem = getElemFromExpression(lpi);
      ElemVariable varElem = getPrevVariableElem(baseElem);
      while (null != varElem)
      {
        if (m_varNameCollector.doesOccur(varElem.getName()))
        {
          psuedoVarRecipient = varElem.getParentElem();
          ete = varElem.getNextSiblingElem();
          break;
        }
        varElem = getPrevVariableElem(varElem);
      }
    }
    
    if ((null != ete) && (41 == ete.getXSLToken()))
    {

      if (isParam(lpi)) {
        return null;
      }
      while (null != ete)
      {
        ete = ete.getNextSiblingElem();
        if ((null != ete) && (41 != ete.getXSLToken()))
          break;
      }
    }
    psuedoVarRecipient.insertBefore(psuedoVar, ete);
    m_varNameCollector.reset();
    return psuedoVar;
  }
  



  protected boolean isParam(ExpressionNode expr)
  {
    while (null != expr)
    {
      if ((expr instanceof ElemTemplateElement))
        break;
      expr = expr.exprGetParent();
    }
    if (null != expr)
    {
      ElemTemplateElement ete = (ElemTemplateElement)expr;
      while (null != ete)
      {
        int type = ete.getXSLToken();
        switch (type)
        {
        case 41: 
          return true;
        case 19: 
        case 25: 
          return false;
        }
        ete = ete.getParentElem();
      }
    }
    return false;
  }
  












  protected ElemVariable getPrevVariableElem(ElemTemplateElement elem)
  {
    while (null != (elem = getPrevElementWithinContext(elem)))
    {
      int type = elem.getXSLToken();
      
      if ((73 == type) || (41 == type))
      {

        return (ElemVariable)elem;
      }
    }
    return null;
  }
  








  protected ElemTemplateElement getPrevElementWithinContext(ElemTemplateElement elem)
  {
    ElemTemplateElement prev = elem.getPreviousSiblingElem();
    if (null == prev)
      prev = elem.getParentElem();
    if (null != prev)
    {
      int type = prev.getXSLToken();
      if ((28 == type) || (19 == type) || (25 == type))
      {


        prev = null;
      }
    }
    return prev;
  }
  








  protected ElemTemplateElement getElemFromExpression(Expression expr)
  {
    ExpressionNode parent = expr.exprGetParent();
    while (null != parent)
    {
      if ((parent instanceof ElemTemplateElement))
        return (ElemTemplateElement)parent;
      parent = parent.exprGetParent();
    }
    throw new RuntimeException(XSLMessages.createMessage("ER_ASSERT_NO_TEMPLATE_PARENT", null));
  }
  







  public boolean isAbsolute(LocPathIterator path)
  {
    int analysis = path.getAnalysisBits();
    boolean isAbs = (WalkerFactory.isSet(analysis, 134217728)) || (WalkerFactory.isSet(analysis, 536870912));
    
    if (isAbs)
    {
      isAbs = m_absPathChecker.checkAbsolute(path);
    }
    return isAbs;
  }
  










  public boolean visitLocationPath(ExpressionOwner owner, LocPathIterator path)
  {
    if ((path instanceof SelfIteratorNoPredicate))
    {
      return true;
    }
    if ((path instanceof WalkingIterator))
    {
      WalkingIterator wi = (WalkingIterator)path;
      AxesWalker aw = wi.getFirstWalker();
      if (((aw instanceof FilterExprWalker)) && (null == aw.getNextWalker()))
      {
        FilterExprWalker few = (FilterExprWalker)aw;
        Expression exp = few.getInnerExpression();
        if ((exp instanceof Variable)) {
          return true;
        }
      }
    }
    if ((isAbsolute(path)) && (null != m_absPaths))
    {


      m_absPaths.addElement(owner);
    }
    else if ((m_isSameContext) && (null != m_paths))
    {


      m_paths.addElement(owner);
    }
    
    return true;
  }
  










  public boolean visitPredicate(ExpressionOwner owner, Expression pred)
  {
    boolean savedIsSame = m_isSameContext;
    m_isSameContext = false;
    

    pred.callVisitors(owner, this);
    
    m_isSameContext = savedIsSame;
    


    return false;
  }
  






  public boolean visitTopLevelInstruction(ElemTemplateElement elem)
  {
    int type = elem.getXSLToken();
    switch (type)
    {
    case 19: 
      return visitInstruction(elem);
    }
    return true;
  }
  









  public boolean visitInstruction(ElemTemplateElement elem)
  {
    int type = elem.getXSLToken();
    switch (type)
    {



    case 17: 
    case 19: 
    case 28: 
      if (type == 28)
      {
        ElemForEach efe = (ElemForEach)elem;
        
        Expression select = efe.getSelect();
        select.callVisitors(efe, this);
      }
      
      Vector savedPaths = m_paths;
      m_paths = new Vector();
      



      elem.callChildVisitors(this, false);
      eleminateRedundentLocals(elem);
      
      m_paths = savedPaths;
      

      return false;
    


    case 35: 
    case 64: 
      boolean savedIsSame = m_isSameContext;
      m_isSameContext = false;
      elem.callChildVisitors(this);
      m_isSameContext = savedIsSame;
      return false;
    }
    
    return true;
  }
  







  protected void diagnoseNumPaths(Vector paths, int numPathsEliminated, int numUniquePathsEliminated)
  {
    if (numPathsEliminated > 0)
    {
      if (paths == m_paths)
      {
        System.err.println("Eliminated " + numPathsEliminated + " total paths!");
        System.err.println("Consolodated " + numUniquePathsEliminated + " redundent paths!");

      }
      else
      {
        System.err.println("Eliminated " + numPathsEliminated + " total global paths!");
        System.err.println("Consolodated " + numUniquePathsEliminated + " redundent global paths!");
      }
    }
  }
  






  private final void assertIsLocPathIterator(Expression expr1, ExpressionOwner eo)
    throws RuntimeException
  {
    if (!(expr1 instanceof LocPathIterator)) {
      String errMsg;
      String errMsg;
      if ((expr1 instanceof Variable))
      {
        errMsg = "Programmer's assertion: expr1 not an iterator: " + ((Variable)expr1).getQName();

      }
      else
      {
        errMsg = "Programmer's assertion: expr1 not an iterator: " + expr1.getClass().getName();
      }
      
      throw new RuntimeException(errMsg + ", " + eo.getClass().getName() + " " + expr1.exprGetParent());
    }
  }
  








  private static void validateNewAddition(Vector paths, ExpressionOwner owner, LocPathIterator path)
    throws RuntimeException
  {
    assertion(owner.getExpression() == path, "owner.getExpression() != path!!!");
    int n = paths.size();
    
    for (int i = 0; i < n; i++)
    {
      ExpressionOwner ew = (ExpressionOwner)paths.elementAt(i);
      assertion(ew != owner, "duplicate owner on the list!!!");
      assertion(ew.getExpression() != path, "duplicate expression on the list!!!");
    }
  }
  



  protected static void assertion(boolean b, String msg)
  {
    if (!b)
    {
      throw new RuntimeException(XSLMessages.createMessage("ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR", new Object[] { msg }));
    }
  }
  


  class MultistepExprHolder
    implements Cloneable
  {
    ExpressionOwner m_exprOwner;
    

    final int m_stepCount;
    

    MultistepExprHolder m_next;
    

    public Object clone()
      throws CloneNotSupportedException
    {
      return super.clone();
    }
    







    MultistepExprHolder(ExpressionOwner exprOwner, int stepCount, MultistepExprHolder next)
    {
      m_exprOwner = exprOwner;
      RedundentExprEliminator.assertion(null != m_exprOwner, "exprOwner can not be null!");
      m_stepCount = stepCount;
      m_next = next;
    }
    








    MultistepExprHolder addInSortedOrder(ExpressionOwner exprOwner, int stepCount)
    {
      MultistepExprHolder first = this;
      MultistepExprHolder next = this;
      MultistepExprHolder prev = null;
      while (null != next)
      {
        if (stepCount >= m_stepCount)
        {
          MultistepExprHolder newholder = new MultistepExprHolder(RedundentExprEliminator.this, exprOwner, stepCount, next);
          if (null == prev) {
            first = newholder;
          } else {
            m_next = newholder;
          }
          return first;
        }
        prev = next;
        next = m_next;
      }
      
      m_next = new MultistepExprHolder(RedundentExprEliminator.this, exprOwner, stepCount, null);
      return first;
    }
    










    MultistepExprHolder unlink(MultistepExprHolder itemToRemove)
    {
      MultistepExprHolder first = this;
      MultistepExprHolder next = this;
      MultistepExprHolder prev = null;
      while (null != next)
      {
        if (next == itemToRemove)
        {
          if (null == prev) {
            first = m_next;
          } else {
            m_next = m_next;
          }
          m_next = null;
          
          return first;
        }
        prev = next;
        next = m_next;
      }
      
      RedundentExprEliminator.assertion(false, "unlink failed!!!");
      return null;
    }
    



    int getLength()
    {
      int count = 0;
      MultistepExprHolder next = this;
      while (null != next)
      {
        count++;
        next = m_next;
      }
      return count;
    }
    



    protected void diagnose()
    {
      System.err.print("Found multistep iterators: " + getLength() + "  ");
      MultistepExprHolder next = this;
      while (null != next)
      {
        System.err.print("" + m_stepCount);
        next = m_next;
        if (null != next)
          System.err.print(", ");
      }
      System.err.println();
    }
  }
}
