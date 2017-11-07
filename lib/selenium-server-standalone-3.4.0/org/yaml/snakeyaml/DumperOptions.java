package org.yaml.snakeyaml;

import java.util.Map;
import java.util.TimeZone;
import org.yaml.snakeyaml.error.YAMLException;

























public class DumperOptions
{
  public DumperOptions() {}
  
  public static enum ScalarStyle
  {
    DOUBLE_QUOTED(Character.valueOf('"')),  SINGLE_QUOTED(Character.valueOf('\'')),  LITERAL(Character.valueOf('|')), 
    FOLDED(Character.valueOf('>')),  PLAIN(null);
    
    private Character styleChar;
    
    private ScalarStyle(Character style) { styleChar = style; }
    
    public Character getChar()
    {
      return styleChar;
    }
    
    public String toString()
    {
      return "Scalar style: '" + styleChar + "'";
    }
    
    public static ScalarStyle createStyle(Character style) {
      if (style == null) {
        return PLAIN;
      }
      switch (style.charValue()) {
      case '"': 
        return DOUBLE_QUOTED;
      case '\'': 
        return SINGLE_QUOTED;
      case '|': 
        return LITERAL;
      case '>': 
        return FOLDED;
      }
      throw new YAMLException("Unknown scalar style character: " + style);
    }
  }
  









  public static enum FlowStyle
  {
    FLOW(Boolean.TRUE),  BLOCK(Boolean.FALSE),  AUTO(null);
    
    private Boolean styleBoolean;
    
    private FlowStyle(Boolean flowStyle) {
      styleBoolean = flowStyle;
    }
    
    public Boolean getStyleBoolean() {
      return styleBoolean;
    }
    
    public String toString()
    {
      return "Flow style: '" + styleBoolean + "'";
    }
  }
  


  public static enum LineBreak
  {
    WIN("\r\n"),  MAC("\r"),  UNIX("\n");
    
    private String lineBreak;
    
    private LineBreak(String lineBreak) {
      this.lineBreak = lineBreak;
    }
    
    public String getString() {
      return lineBreak;
    }
    
    public String toString()
    {
      return "Line break: " + name();
    }
    
    public static LineBreak getPlatformLineBreak() {
      String platformLineBreak = System.getProperty("line.separator");
      for (LineBreak lb : values()) {
        if (lineBreak.equals(platformLineBreak)) {
          return lb;
        }
      }
      return UNIX;
    }
  }
  


  public static enum Version
  {
    V1_0(new Integer[] { Integer.valueOf(1), Integer.valueOf(0) }),  V1_1(new Integer[] { Integer.valueOf(1), Integer.valueOf(1) });
    
    private Integer[] version;
    
    private Version(Integer[] version) {
      this.version = version;
    }
    
    public int major() { return version[0].intValue(); }
    public int minor() { return version[1].intValue(); }
    
    public String getRepresentation() {
      return version[0] + "." + version[1];
    }
    
    public String toString()
    {
      return "Version: " + getRepresentation();
    }
  }
  
  private ScalarStyle defaultStyle = ScalarStyle.PLAIN;
  private FlowStyle defaultFlowStyle = FlowStyle.AUTO;
  private boolean canonical = false;
  private boolean allowUnicode = true;
  private boolean allowReadOnlyProperties = false;
  private int indent = 2;
  private int bestWidth = 80;
  private boolean splitLines = true;
  private LineBreak lineBreak = LineBreak.UNIX;
  private boolean explicitStart = false;
  private boolean explicitEnd = false;
  private TimeZone timeZone = null;
  
  private Version version = null;
  private Map<String, String> tags = null;
  private Boolean prettyFlow = Boolean.valueOf(false);
  
  public boolean isAllowUnicode() {
    return allowUnicode;
  }
  







  public void setAllowUnicode(boolean allowUnicode)
  {
    this.allowUnicode = allowUnicode;
  }
  
  public ScalarStyle getDefaultScalarStyle() {
    return defaultStyle;
  }
  






  public void setDefaultScalarStyle(ScalarStyle defaultStyle)
  {
    if (defaultStyle == null) {
      throw new NullPointerException("Use ScalarStyle enum.");
    }
    this.defaultStyle = defaultStyle;
  }
  
  public void setIndent(int indent) {
    if (indent < 1) {
      throw new YAMLException("Indent must be at least 1");
    }
    if (indent > 10) {
      throw new YAMLException("Indent must be at most 10");
    }
    this.indent = indent;
  }
  
  public int getIndent() {
    return indent;
  }
  
  public void setVersion(Version version) {
    this.version = version;
  }
  
  public Version getVersion() {
    return version;
  }
  





  public void setCanonical(boolean canonical)
  {
    this.canonical = canonical;
  }
  
  public boolean isCanonical() {
    return canonical;
  }
  






  public void setPrettyFlow(boolean prettyFlow)
  {
    this.prettyFlow = Boolean.valueOf(prettyFlow);
  }
  
  public boolean isPrettyFlow() {
    return prettyFlow.booleanValue();
  }
  







  public void setWidth(int bestWidth)
  {
    this.bestWidth = bestWidth;
  }
  
  public int getWidth() {
    return bestWidth;
  }
  






  public void setSplitLines(boolean splitLines)
  {
    this.splitLines = splitLines;
  }
  
  public boolean getSplitLines() {
    return splitLines;
  }
  
  public LineBreak getLineBreak() {
    return lineBreak;
  }
  
  public void setDefaultFlowStyle(FlowStyle defaultFlowStyle) {
    if (defaultFlowStyle == null) {
      throw new NullPointerException("Use FlowStyle enum.");
    }
    this.defaultFlowStyle = defaultFlowStyle;
  }
  
  public FlowStyle getDefaultFlowStyle() {
    return defaultFlowStyle;
  }
  




  public void setLineBreak(LineBreak lineBreak)
  {
    if (lineBreak == null) {
      throw new NullPointerException("Specify line break.");
    }
    this.lineBreak = lineBreak;
  }
  
  public boolean isExplicitStart() {
    return explicitStart;
  }
  
  public void setExplicitStart(boolean explicitStart) {
    this.explicitStart = explicitStart;
  }
  
  public boolean isExplicitEnd() {
    return explicitEnd;
  }
  
  public void setExplicitEnd(boolean explicitEnd) {
    this.explicitEnd = explicitEnd;
  }
  
  public Map<String, String> getTags() {
    return tags;
  }
  
  public void setTags(Map<String, String> tags)
  {
    this.tags = tags;
  }
  





  public boolean isAllowReadOnlyProperties()
  {
    return allowReadOnlyProperties;
  }
  







  public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties)
  {
    this.allowReadOnlyProperties = allowReadOnlyProperties;
  }
  
  public TimeZone getTimeZone() {
    return timeZone;
  }
  



  public void setTimeZone(TimeZone timeZone)
  {
    this.timeZone = timeZone;
  }
}
