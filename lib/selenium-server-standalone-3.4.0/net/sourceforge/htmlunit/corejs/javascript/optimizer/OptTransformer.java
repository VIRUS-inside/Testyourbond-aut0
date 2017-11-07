package net.sourceforge.htmlunit.corejs.javascript.optimizer;

import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.Kit;
import net.sourceforge.htmlunit.corejs.javascript.Node;
import net.sourceforge.htmlunit.corejs.javascript.NodeTransformer;
import net.sourceforge.htmlunit.corejs.javascript.ObjArray;
import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;




class OptTransformer
  extends NodeTransformer
{
  private Map<String, OptFunctionNode> possibleDirectCalls;
  private ObjArray directCallTargets;
  
  OptTransformer(Map<String, OptFunctionNode> possibleDirectCalls, ObjArray directCallTargets)
  {
    this.possibleDirectCalls = possibleDirectCalls;
    this.directCallTargets = directCallTargets;
  }
  
  protected void visitNew(Node node, ScriptNode tree)
  {
    detectDirectCall(node, tree);
    super.visitNew(node, tree);
  }
  
  protected void visitCall(Node node, ScriptNode tree)
  {
    detectDirectCall(node, tree);
    super.visitCall(node, tree);
  }
  
  private void detectDirectCall(Node node, ScriptNode tree) {
    if (tree.getType() == 109) {
      Node left = node.getFirstChild();
      

      int argCount = 0;
      Node arg = left.getNext();
      while (arg != null) {
        arg = arg.getNext();
        argCount++;
      }
      
      if (argCount == 0) {
        getitsContainsCalls0 = true;
      }
      









      if (possibleDirectCalls != null) {
        String targetName = null;
        if (left.getType() == 39) {
          targetName = left.getString();
        } else if (left.getType() == 33) {
          targetName = left.getFirstChild().getNext().getString();
        } else if (left.getType() == 34) {
          throw Kit.codeBug();
        }
        if (targetName != null)
        {
          OptFunctionNode ofn = (OptFunctionNode)possibleDirectCalls.get(targetName);
          if ((ofn != null) && (argCount == fnode.getParamCount()) && 
            (!fnode.requiresActivation()))
          {


            if (argCount <= 32) {
              node.putProp(9, ofn);
              if (!ofn.isTargetOfDirectCall()) {
                int index = directCallTargets.size();
                directCallTargets.add(ofn);
                ofn.setDirectTargetIndex(index);
              }
            }
          }
        }
      }
    }
  }
}
