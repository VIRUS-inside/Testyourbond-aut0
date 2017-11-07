package org.apache.bcel.verifier;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.verifier.statics.Pass1Verifier;
import org.apache.bcel.verifier.statics.Pass2Verifier;
import org.apache.bcel.verifier.statics.Pass3aVerifier;
import org.apache.bcel.verifier.structurals.Pass3bVerifier;











































































public class Verifier
{
  private final String classname;
  private Pass1Verifier p1v;
  private Pass2Verifier p2v;
  private HashMap p3avs = new HashMap();
  
  private HashMap p3bvs = new HashMap();
  
  public VerificationResult doPass1()
  {
    if (p1v == null) {
      p1v = new Pass1Verifier(this);
    }
    return p1v.verify();
  }
  
  public VerificationResult doPass2()
  {
    if (p2v == null) {
      p2v = new Pass2Verifier(this);
    }
    return p2v.verify();
  }
  
  public VerificationResult doPass3a(int method_no)
  {
    String key = Integer.toString(method_no);
    
    Pass3aVerifier p3av = (Pass3aVerifier)p3avs.get(key);
    if (p3avs.get(key) == null) {
      p3av = new Pass3aVerifier(this, method_no);
      p3avs.put(key, p3av);
    }
    return p3av.verify();
  }
  
  public VerificationResult doPass3b(int method_no)
  {
    String key = Integer.toString(method_no);
    
    Pass3bVerifier p3bv = (Pass3bVerifier)p3bvs.get(key);
    if (p3bvs.get(key) == null) {
      p3bv = new Pass3bVerifier(this, method_no);
      p3bvs.put(key, p3bv);
    }
    return p3bv.verify();
  }
  


  private Verifier()
  {
    classname = "";
  }
  




  Verifier(String fully_qualified_classname)
  {
    classname = fully_qualified_classname;
    flush();
  }
  






  public final String getClassName()
  {
    return classname;
  }
  





  public void flush()
  {
    p1v = null;
    p2v = null;
    p3avs.clear();
    p3bvs.clear();
  }
  



  public String[] getMessages()
  {
    ArrayList messages = new ArrayList();
    
    if (p1v != null) {
      String[] p1m = p1v.getMessages();
      for (int i = 0; i < p1m.length; i++) {
        messages.add("Pass 1: " + p1m[i]);
      }
    }
    if (p2v != null) {
      String[] p2m = p2v.getMessages();
      for (int i = 0; i < p2m.length; i++) {
        messages.add("Pass 2: " + p2m[i]);
      }
    }
    Iterator p3as = p3avs.values().iterator();
    String[] p3am; int i; for (; p3as.hasNext(); 
        


        i < p3am.length)
    {
      Pass3aVerifier pv = (Pass3aVerifier)p3as.next();
      p3am = pv.getMessages();
      int meth = pv.getMethodNo();
      i = 0; continue;
      messages.add("Pass 3a, method " + meth + " ('" + Repository.lookupClass(classname).getMethods()[meth] + "'): " + p3am[i]);i++;
    }
    

    Iterator p3bs = p3bvs.values().iterator();
    String[] p3bm; int i; for (; p3bs.hasNext(); 
        


        i < p3bm.length)
    {
      Pass3bVerifier pv = (Pass3bVerifier)p3bs.next();
      p3bm = pv.getMessages();
      int meth = pv.getMethodNo();
      i = 0; continue;
      messages.add("Pass 3b, method " + meth + " ('" + Repository.lookupClass(classname).getMethods()[meth] + "'): " + p3bm[i]);i++;
    }
    


    String[] ret = new String[messages.size()];
    for (int i = 0; i < messages.size(); i++) {
      ret[i] = ((String)messages.get(i));
    }
    
    return ret;
  }
  









  public static void main(String[] args)
  {
    System.out.println("JustIce by Enver Haase, (C) 2001. http://bcel.sourceforge.net\n");
    for (int k = 0; k < args.length; k++)
    {
      if (args[k].endsWith(".class")) {
        int dotclasspos = args[k].lastIndexOf(".class");
        if (dotclasspos != -1) { args[k] = args[k].substring(0, dotclasspos);
        }
      }
      args[k] = args[k].replace('/', '.');
      
      System.out.println("Now verifiying: " + args[k] + "\n");
      
      Verifier v = VerifierFactory.getVerifier(args[k]);
      

      VerificationResult vr = v.doPass1();
      System.out.println("Pass 1:\n" + vr);
      
      vr = v.doPass2();
      System.out.println("Pass 2:\n" + vr);
      
      if (vr == VerificationResult.VR_OK) {
        JavaClass jc = Repository.lookupClass(args[k]);
        for (int i = 0; i < jc.getMethods().length; i++) {
          vr = v.doPass3a(i);
          System.out.println("Pass 3a, method " + i + " ['" + jc.getMethods()[i] + "']:\n" + vr);
          
          vr = v.doPass3b(i);
          System.out.println("Pass 3b, method number " + i + " ['" + jc.getMethods()[i] + "']:\n" + vr);
        }
      }
      
      System.out.println("Warnings:");
      String[] warnings = v.getMessages();
      if (warnings.length == 0) System.out.println("<none>");
      for (int j = 0; j < warnings.length; j++) {
        System.out.println(warnings[j]);
      }
      
      System.out.println("\n");
      

      v.flush();
      Repository.clearCache();
      System.gc();
    }
  }
}
