package org.openqa.grid.common;

















public enum GridRole
{
  NOT_GRID,  HUB,  NODE;
  
  private static final String WD_S = "wd";
  private static final String WEBDRIVER_S = "webdriver";
  
  private GridRole() {}
  
  public static GridRole get(String role)
  {
    if ((role == null) || (role.equals(""))) {
      return NOT_GRID;
    }
    switch (role) {
    case "wd": 
    case "webdriver": 
    case "node": 
      return NODE;
    
    case "hub": 
      return HUB;
    
    case "standalone": 
      return NOT_GRID;
    }
    
    return null; }
  
  private static final String NODE_S = "node";
  private static final String HUB_S = "hub";
  private static final String STANDALONE_S = "standalone";
  public String toString() { switch (1.$SwitchMap$org$openqa$grid$common$GridRole[ordinal()]) {
    case 1: 
      return "node";
    
    case 2: 
      return "hub";
    
    case 3: 
      return "standalone";
    }
    
    throw new IllegalStateException("Unrecognized GridRole");
  }
}
