package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.PrintStream;



























public final class Version
{
  private Version() {}
  
  public static void main(String[] args)
    throws Exception
  {
    if ((args.length == 1) && ("-SanityCheck".equals(args[0]))) {
      runSanityCheck();
      return;
    }
    System.out.println(getProductName());
    System.out.println(getCopyright());
    System.out.println("Version: " + getProductVersion());
  }
  


  private static void runSanityCheck()
    throws Exception
  {
    Object localObject1 = null;Object localObject4 = null; Object localObject3; label79: try { webClient = new WebClient();
    } finally {
      WebClient webClient;
      HtmlPage page;
      localObject3 = localThrowable; break label79; if (localObject3 != localThrowable) { localObject3.addSuppressed(localThrowable);
      }
    }
  }
  

  public static String getProductName()
  {
    return "HtmlUnit";
  }
  



  public static String getProductVersion()
  {
    return Version.class.getPackage().getImplementationVersion();
  }
  



  public static String getCopyright()
  {
    return "Copyright (c) 2002-2017 Gargoyle Software Inc. All rights reserved.";
  }
}
