package org.openqa.selenium.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



















public class Color
{
  private final int red;
  private final int green;
  private final int blue;
  private double alpha;
  private static final Converter[] CONVERTERS = { new RgbConverter(null), new RgbPctConverter(null), new RgbaConverter(null), new RgbaPctConverter(null), new HexConverter(null), new Hex3Converter(null), new HslConverter(null), new HslaConverter(null), new NamedColorConverter(null) };
  












  public static Color fromString(String value)
  {
    for (Converter converter : CONVERTERS) {
      Color color = converter.getColor(value);
      if (color != null) {
        return color;
      }
    }
    
    throw new IllegalArgumentException(String.format("Did not know how to convert %s into color", new Object[] { value }));
  }
  
  Color(int red, int green, int blue, double alpha)
  {
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.alpha = alpha;
  }
  
  public void setOpacity(double alpha) {
    this.alpha = alpha;
  }
  

  public String asRgb() { return String.format("rgb(%d, %d, %d)", new Object[] { Integer.valueOf(red), Integer.valueOf(green), Integer.valueOf(blue) }); }
  
  public String asRgba() {
    String alphaString;
    String alphaString;
    if (alpha == 1.0D) {
      alphaString = "1"; } else { String alphaString;
      if (alpha == 0.0D) {
        alphaString = "0";
      } else
        alphaString = Double.toString(alpha);
    }
    return String.format("rgba(%d, %d, %d, %s)", new Object[] { Integer.valueOf(red), Integer.valueOf(green), Integer.valueOf(blue), alphaString });
  }
  
  public String asHex() {
    return String.format("#%02x%02x%02x", new Object[] { Integer.valueOf(red), Integer.valueOf(green), Integer.valueOf(blue) });
  }
  


  public java.awt.Color getColor()
  {
    return new java.awt.Color(red, green, blue, (int)(alpha * 255.0D));
  }
  
  public String toString()
  {
    return "Color: " + asRgba();
  }
  
  public boolean equals(Object other)
  {
    if (other == null) {
      return false;
    }
    
    if (!(other instanceof Color)) {
      return false;
    }
    
    return asRgba().equals(((Color)other).asRgba());
  }
  


  public int hashCode()
  {
    int result = red;
    result = 31 * result + green;
    result = 31 * result + blue;
    long temp = alpha != 0.0D ? Double.doubleToLongBits(alpha) : 0L;
    result = 31 * result + (int)(temp ^ temp >>> 32);
    return result;
  }
  
  private static abstract class Converter { private Converter() {}
    
    public Color getColor(String value) { Matcher matcher = getPattern().matcher(value);
      if (matcher.find()) {
        double a = 1.0D;
        if (matcher.groupCount() == 4) {
          a = Double.parseDouble(matcher.group(4));
        }
        return createColor(matcher, a);
      }
      return null;
    }
    
    protected Color createColor(Matcher matcher, double a) {
      return new Color(
        fromMatchGroup(matcher, 1), 
        fromMatchGroup(matcher, 2), 
        fromMatchGroup(matcher, 3), a);
    }
    


    protected short fromMatchGroup(Matcher matcher, int index) { return Short.parseShort(matcher.group(index), 10); }
    
    protected abstract Pattern getPattern();
  }
  
  private static class RgbConverter extends Color.Converter {
    private RgbConverter() { super(); }
    private static final Pattern RGB_PATTERN = Pattern.compile("^\\s*rgb\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*\\)\\s*$");
    





    protected Pattern getPattern() { return RGB_PATTERN; }
  }
  
  private static class RgbPctConverter extends Color.Converter {
    private RgbPctConverter() { super(); }
    private static final Pattern RGBPCT_PATTERN = Pattern.compile("^\\s*rgb\\(\\s*(\\d{1,3}|\\d{1,2}\\.\\d+)%\\s*,\\s*(\\d{1,3}|\\d{1,2}\\.\\d+)%\\s*,\\s*(\\d{1,3}|\\d{1,2}\\.\\d+)%\\s*\\)\\s*$");
    



    protected Pattern getPattern()
    {
      return RGBPCT_PATTERN;
    }
    
    protected short fromMatchGroup(Matcher matcher, int index)
    {
      double n = Double.parseDouble(matcher.group(index)) / 100.0D * 255.0D;
      return (short)(int)n;
    }
  }
  
  private static class RgbaConverter extends Color.RgbConverter { private RgbaConverter() { super(); }
    private static final Pattern RGBA_PATTERN = Pattern.compile("^\\s*rgba\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*(0|1|0\\.\\d+)\\s*\\)\\s*$");
    




    protected Pattern getPattern() { return RGBA_PATTERN; }
  }
  
  private static class RgbaPctConverter extends Color.RgbPctConverter {
    private RgbaPctConverter() { super(); }
    private static final Pattern RGBAPCT_PATTERN = Pattern.compile("^\\s*rgba\\(\\s*(\\d{1,3}|\\d{1,2}\\.\\d+)%\\s*,\\s*(\\d{1,3}|\\d{1,2}\\.\\d+)%\\s*,\\s*(\\d{1,3}|\\d{1,2}\\.\\d+)%\\s*,\\s*(0|1|0\\.\\d+)\\s*\\)\\s*$");
    






    protected Pattern getPattern() { return RGBAPCT_PATTERN; }
  }
  
  private static class HexConverter extends Color.Converter {
    private HexConverter() { super(); }
    private static final Pattern HEX_PATTERN = Pattern.compile("#(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{2})");
    


    protected Pattern getPattern()
    {
      return HEX_PATTERN;
    }
    


    protected short fromMatchGroup(Matcher matcher, int index) { return Short.parseShort(matcher.group(index), 16); }
  }
  
  private static class Hex3Converter extends Color.Converter {
    private Hex3Converter() { super(); }
    private static final Pattern HEX3_PATTERN = Pattern.compile("#(\\p{XDigit}{1})(\\p{XDigit}{1})(\\p{XDigit}{1})");
    


    protected Pattern getPattern()
    {
      return HEX3_PATTERN;
    }
    
    protected short fromMatchGroup(Matcher matcher, int index)
    {
      return Short.parseShort(matcher.group(index) + matcher.group(index), 16);
    }
  }
  
  private static class HslConverter extends Color.Converter {
    private HslConverter() { super(); }
    private static final Pattern HSL_PATTERN = Pattern.compile("^\\s*hsl\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\%\\s*,\\s*(\\d{1,3})\\%\\s*\\)\\s*$");
    



    protected Pattern getPattern()
    {
      return HSL_PATTERN;
    }
    
    protected Color createColor(Matcher matcher, double a)
    {
      double h = Double.parseDouble(matcher.group(1)) / 360.0D;
      double s = Double.parseDouble(matcher.group(2)) / 100.0D;
      double l = Double.parseDouble(matcher.group(3)) / 100.0D;
      double b;
      double r;
      double g; double b; if (s == 0.0D) {
        double r = l;
        double g = r;
        b = r;
      } else {
        double luminocity2 = l < 0.5D ? l * (1.0D + s) : l + s - l * s;
        double luminocity1 = 2.0D * l - luminocity2;
        r = hueToRgb(luminocity1, luminocity2, h + 0.3333333333333333D);
        g = hueToRgb(luminocity1, luminocity2, h);
        b = hueToRgb(luminocity1, luminocity2, h - 0.3333333333333333D);
      }
      
      return new Color((short)(int)Math.round(r * 255.0D), 
        (short)(int)Math.round(g * 255.0D), 
        (short)(int)Math.round(b * 255.0D), a);
    }
    
    private double hueToRgb(double luminocity1, double luminocity2, double hue)
    {
      if (hue < 0.0D) hue += 1.0D;
      if (hue > 1.0D) hue -= 1.0D;
      if (hue < 0.16666666666666666D) return luminocity1 + (luminocity2 - luminocity1) * 6.0D * hue;
      if (hue < 0.5D) return luminocity2;
      if (hue < 0.6666666666666666D) return luminocity1 + (luminocity2 - luminocity1) * (0.6666666666666666D - hue) * 6.0D;
      return luminocity1;
    }
  }
  
  private static class HslaConverter extends Color.HslConverter {
    private HslaConverter() { super(); }
    private static final Pattern HSLA_PATTERN = Pattern.compile("^\\s*hsla\\(\\s*(\\d{1,3})\\s*,\\s*(\\d{1,3})\\%\\s*,\\s*(\\d{1,3})\\%\\s*,\\s*(0|1|0\\.\\d+)\\s*\\)\\s*$");
    




    protected Pattern getPattern()
    {
      return HSLA_PATTERN;
    }
  }
  
  private static class NamedColorConverter extends Color.Converter {
    private NamedColorConverter() { super(); }
    
    public Color getColor(String value) {
      return Colors.valueOf(value.toUpperCase()).getColorValue();
    }
    
    public Pattern getPattern()
    {
      throw new UnsupportedOperationException("getPattern is unsupported");
    }
  }
}
