package net.sf.cglib.core;













public class DefaultNamingPolicy
  implements NamingPolicy
{
  public DefaultNamingPolicy() {}
  











  public static final DefaultNamingPolicy INSTANCE = new DefaultNamingPolicy();
  



  private static final boolean STRESS_HASH_CODE = Boolean.getBoolean("net.sf.cglib.test.stressHashCodes");
  
  public String getClassName(String prefix, String source, Object key, Predicate names) {
    if (prefix == null) {
      prefix = "net.sf.cglib.empty.Object";
    } else if (prefix.startsWith("java")) {
      prefix = "$" + prefix;
    }
    



    String base = prefix + "$$" + source.substring(source.lastIndexOf('.') + 1) + getTag() + "$$" + Integer.toHexString(STRESS_HASH_CODE ? 0 : key.hashCode());
    String attempt = base;
    int index = 2;
    while (names.evaluate(attempt))
      attempt = base + "_" + index++;
    return attempt;
  }
  



  protected String getTag()
  {
    return "ByCGLIB";
  }
  
  public int hashCode() {
    return getTag().hashCode();
  }
  
  public boolean equals(Object o) {
    return ((o instanceof DefaultNamingPolicy)) && (((DefaultNamingPolicy)o).getTag().equals(getTag()));
  }
}
