package net.sourceforge.htmlunit.corejs.javascript;







final class NativeMath
  extends IdScriptableObject
{
  static final long serialVersionUID = -8838847185801131569L;
  




  private static final Object MATH_TAG = "Math";
  private static final int Id_toSource = 1;
  
  static void init(Scriptable scope, boolean sealed) { NativeMath obj = new NativeMath();
    obj.activatePrototypeMap(27);
    obj.setPrototype(getObjectPrototype(scope));
    obj.setParentScope(scope);
    if (sealed) {
      obj.sealObject();
    }
    ScriptableObject.defineProperty(scope, "Math", obj, 2);
  }
  
  private static final int Id_abs = 2;
  private static final int Id_acos = 3;
  private static final int Id_asin = 4;
  
  private NativeMath() {}
  
  public String getClassName() { return "Math"; }
  
  private static final int Id_atan = 5;
  private static final int Id_atan2 = 6;
  private static final int Id_ceil = 7;
  protected void initPrototypeId(int id) { if (id <= 19) { String name;
      String name;
      String name;
      String name; String name; String name; String name; String name; String name; String name; String name; String name; String name; String name; String name; String name; String name; String name; String name; switch (id) {
      case 1: 
        int arity = 0;
        name = "toSource";
        break;
      case 2: 
        int arity = 1;
        name = "abs";
        break;
      case 3: 
        int arity = 1;
        name = "acos";
        break;
      case 4: 
        int arity = 1;
        name = "asin";
        break;
      case 5: 
        int arity = 1;
        name = "atan";
        break;
      case 6: 
        int arity = 2;
        name = "atan2";
        break;
      case 7: 
        int arity = 1;
        name = "ceil";
        break;
      case 8: 
        int arity = 1;
        name = "cos";
        break;
      case 9: 
        int arity = 1;
        name = "exp";
        break;
      case 10: 
        int arity = 1;
        name = "floor";
        break;
      case 11: 
        int arity = 1;
        name = "log";
        break;
      case 12: 
        int arity = 2;
        name = "max";
        break;
      case 13: 
        int arity = 2;
        name = "min";
        break;
      case 14: 
        int arity = 2;
        name = "pow";
        break;
      case 15: 
        int arity = 0;
        name = "random";
        break;
      case 16: 
        int arity = 1;
        name = "round";
        break;
      case 17: 
        int arity = 1;
        name = "sin";
        break;
      case 18: 
        int arity = 1;
        name = "sqrt";
        break;
      case 19: 
        int arity = 1;
        name = "tan";
        break;
      default: 
        throw new IllegalStateException(String.valueOf(id)); }
      int arity;
      String name; initPrototypeMethod(MATH_TAG, id, name, arity); } else { String name;
      String name;
      String name;
      String name;
      String name; String name; String name; String name; switch (id) {
      case 20: 
        double x = 2.718281828459045D;
        name = "E";
        break;
      case 21: 
        double x = 3.141592653589793D;
        name = "PI";
        break;
      case 22: 
        double x = 2.302585092994046D;
        name = "LN10";
        break;
      case 23: 
        double x = 0.6931471805599453D;
        name = "LN2";
        break;
      case 24: 
        double x = 1.4426950408889634D;
        name = "LOG2E";
        break;
      case 25: 
        double x = 0.4342944819032518D;
        name = "LOG10E";
        break;
      case 26: 
        double x = 0.7071067811865476D;
        name = "SQRT1_2";
        break;
      case 27: 
        double x = 1.4142135623730951D;
        name = "SQRT2";
        break;
      default: 
        throw new IllegalStateException(String.valueOf(id)); }
      double x;
      String name; initPrototypeValue(id, name, ScriptRuntime.wrapNumber(x), 7);
    }
  }
  
  private static final int Id_cos = 8;
  private static final int Id_exp = 9;
  private static final int Id_floor = 10;
  
  public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) { if (!f.hasTag(MATH_TAG)) {
      return super.execIdCall(f, cx, scope, thisObj, args);
    }
    
    int methodId = f.methodId();
    double x; switch (methodId) {
    case 1: 
      return "Math";
    
    case 2: 
      double x = ScriptRuntime.toNumber(args, 0);
      
      x = x < 0.0D ? -x : x == 0.0D ? 0.0D : x;
      break;
    
    case 3: 
    case 4: 
      double x = ScriptRuntime.toNumber(args, 0);
      if ((x == x) && (-1.0D <= x) && (x <= 1.0D)) {
        x = methodId == 3 ? Math.acos(x) : Math.asin(x);
      } else {
        x = NaN.0D;
      }
      break;
    
    case 5: 
      double x = ScriptRuntime.toNumber(args, 0);
      x = Math.atan(x);
      break;
    
    case 6: 
      double x = ScriptRuntime.toNumber(args, 0);
      x = Math.atan2(x, ScriptRuntime.toNumber(args, 1));
      break;
    
    case 7: 
      double x = ScriptRuntime.toNumber(args, 0);
      x = Math.ceil(x);
      break;
    
    case 8: 
      double x = ScriptRuntime.toNumber(args, 0);
      
      x = (x == Double.POSITIVE_INFINITY) || (x == Double.NEGATIVE_INFINITY) ? NaN.0D : Math.cos(x);
      break;
    
    case 9: 
      double x = ScriptRuntime.toNumber(args, 0);
      
      x = x == Double.NEGATIVE_INFINITY ? 0.0D : x == Double.POSITIVE_INFINITY ? x : Math.exp(x);
      break;
    
    case 10: 
      double x = ScriptRuntime.toNumber(args, 0);
      x = Math.floor(x);
      break;
    
    case 11: 
      double x = ScriptRuntime.toNumber(args, 0);
      
      x = x < 0.0D ? NaN.0D : Math.log(x);
      break;
    
    case 12: 
    case 13: 
      double x = methodId == 12 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
      
      for (int i = 0; i != args.length; i++) {
        double d = ScriptRuntime.toNumber(args[i]);
        if (d != d) {
          x = d;
          break;
        }
        if (methodId == 12)
        {
          x = Math.max(x, d);
        } else {
          x = Math.min(x, d);
        }
      }
      break;
    
    case 14: 
      double x = ScriptRuntime.toNumber(args, 0);
      x = js_pow(x, ScriptRuntime.toNumber(args, 1));
      break;
    
    case 15: 
      x = Math.random();
      break;
    
    case 16: 
      double x = ScriptRuntime.toNumber(args, 0);
      if ((x == x) && (x != Double.POSITIVE_INFINITY) && (x != Double.NEGATIVE_INFINITY))
      {

        long l = Math.round(x);
        if (l != 0L) {
          x = l;

        }
        else if (x < 0.0D) {
          x = ScriptRuntime.negativeZero;
        } else if (x != 0.0D) {
          x = 0.0D;
        }
      }
      break;
    

    case 17: 
      double x = ScriptRuntime.toNumber(args, 0);
      
      x = (x == Double.POSITIVE_INFINITY) || (x == Double.NEGATIVE_INFINITY) ? NaN.0D : Math.sin(x);
      break;
    
    case 18: 
      double x = ScriptRuntime.toNumber(args, 0);
      x = Math.sqrt(x);
      break;
    
    case 19: 
      double x = ScriptRuntime.toNumber(args, 0);
      x = Math.tan(x);
      break;
    
    default: 
      throw new IllegalStateException(String.valueOf(methodId)); }
    double x;
    return ScriptRuntime.wrapNumber(x); }
  
  private static final int Id_log = 11;
  private static final int Id_max = 12;
  private static final int Id_min = 13;
  private static final int Id_pow = 14;
  private double js_pow(double x, double y) { double result; double result; if (y != y)
    {
      result = y; } else { double result;
      if (y == 0.0D)
      {
        result = 1.0D; } else { double result;
        if (x == 0.0D) {
          double result;
          if (1.0D / x > 0.0D) {
            result = y > 0.0D ? 0.0D : Double.POSITIVE_INFINITY;
          }
          else {
            long y_long = y;
            double result; if ((y_long == y) && ((y_long & 1L) != 0L)) {
              result = y > 0.0D ? -0.0D : Double.NEGATIVE_INFINITY;
            } else {
              result = y > 0.0D ? 0.0D : Double.POSITIVE_INFINITY;
            }
          }
        } else {
          result = Math.pow(x, y);
          if (result != result)
          {

            if (y == Double.POSITIVE_INFINITY) {
              if ((x < -1.0D) || (1.0D < x)) {
                result = Double.POSITIVE_INFINITY;
              } else if ((-1.0D < x) && (x < 1.0D)) {
                result = 0.0D;
              }
            } else if (y == Double.NEGATIVE_INFINITY) {
              if ((x < -1.0D) || (1.0D < x)) {
                result = 0.0D;
              } else if ((-1.0D < x) && (x < 1.0D)) {
                result = Double.POSITIVE_INFINITY;
              }
            } else if (x == Double.POSITIVE_INFINITY) {
              result = y > 0.0D ? Double.POSITIVE_INFINITY : 0.0D;
            } else if (x == Double.NEGATIVE_INFINITY) {
              long y_long = y;
              if ((y_long == y) && ((y_long & 1L) != 0L))
              {
                result = y > 0.0D ? Double.NEGATIVE_INFINITY : -0.0D;
              } else
                result = y > 0.0D ? Double.POSITIVE_INFINITY : 0.0D;
            } }
        }
      }
    }
    return result; }
  
  private static final int Id_random = 15;
  private static final int Id_round = 16;
  private static final int Id_sin = 17;
  private static final int Id_sqrt = 18;
  private static final int Id_tan = 19;
  private static final int LAST_METHOD_ID = 19;
  private static final int Id_E = 20;
  
  protected int findPrototypeId(String s) { int id = 0;
    String X = null;
    
    switch (s.length()) {
    case 1: 
      if (s.charAt(0) == 'E')
        id = 20;
      break;
    

    case 2: 
      if ((s.charAt(0) == 'P') && (s.charAt(1) == 'I'))
        id = 21;
      break;
    

    case 3: 
      switch (s.charAt(0)) {
      case 'L': 
        if ((s.charAt(2) == '2') && (s.charAt(1) == 'N'))
          id = 23;
        break;
      

      case 'a': 
        if ((s.charAt(2) == 's') && (s.charAt(1) == 'b'))
          id = 2;
        break;
      

      case 'c': 
        if ((s.charAt(2) == 's') && (s.charAt(1) == 'o'))
          id = 8;
        break;
      

      case 'e': 
        if ((s.charAt(2) == 'p') && (s.charAt(1) == 'x'))
          id = 9;
        break;
      

      case 'l': 
        if ((s.charAt(2) == 'g') && (s.charAt(1) == 'o'))
          id = 11;
        break;
      

      case 'm': 
        int c = s.charAt(2);
        if (c == 110) {
          if (s.charAt(1) == 'i') {
            id = 13;
            return id;
          }
        } else if ((c == 120) && 
          (s.charAt(1) == 'a'))
          id = 12;
        break;
      


      case 'p': 
        if ((s.charAt(2) == 'w') && (s.charAt(1) == 'o'))
          id = 14;
        break;
      

      case 's': 
        if ((s.charAt(2) == 'n') && (s.charAt(1) == 'i'))
          id = 17;
        break;
      

      case 't': 
        if ((s.charAt(2) == 'n') && (s.charAt(1) == 'a')) {
          id = 19;
          return id;
        }
        break;
      }
      break;
    case 4: 
      switch (s.charAt(1)) {
      case 'N': 
        X = "LN10";
        id = 22;
        break;
      case 'c': 
        X = "acos";
        id = 3;
        break;
      case 'e': 
        X = "ceil";
        id = 7;
        break;
      case 'q': 
        X = "sqrt";
        id = 18;
        break;
      case 's': 
        X = "asin";
        id = 4;
        break;
      case 't': 
        X = "atan";
        id = 5;
      }
      
      break;
    case 5: 
      switch (s.charAt(0)) {
      case 'L': 
        X = "LOG2E";
        id = 24;
        break;
      case 'S': 
        X = "SQRT2";
        id = 27;
        break;
      case 'a': 
        X = "atan2";
        id = 6;
        break;
      case 'f': 
        X = "floor";
        id = 10;
        break;
      case 'r': 
        X = "round";
        id = 16;
      }
      
      break;
    case 6: 
      int c = s.charAt(0);
      if (c == 76) {
        X = "LOG10E";
        id = 25;
      } else if (c == 114) {
        X = "random";
        id = 15;
      }
      break;
    case 7: 
      X = "SQRT1_2";
      id = 26;
      break;
    case 8: 
      X = "toSource";
      id = 1;
      break;
    }
    if ((X != null) && (X != s) && (!X.equals(s))) {
      id = 0;
    }
    
    return id;
  }
  
  private static final int Id_PI = 21;
  private static final int Id_LN10 = 22;
  private static final int Id_LN2 = 23;
  private static final int Id_LOG2E = 24;
  private static final int Id_LOG10E = 25;
  private static final int Id_SQRT1_2 = 26;
  private static final int Id_SQRT2 = 27;
  private static final int MAX_ID = 27;
}
