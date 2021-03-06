package org.yaml.snakeyaml.constructor;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;



















public class SafeConstructor
  extends BaseConstructor
{
  public static final ConstructUndefined undefinedConstructor = new ConstructUndefined();
  
  public SafeConstructor() {
    yamlConstructors.put(Tag.NULL, new ConstructYamlNull());
    yamlConstructors.put(Tag.BOOL, new ConstructYamlBool());
    yamlConstructors.put(Tag.INT, new ConstructYamlInt());
    yamlConstructors.put(Tag.FLOAT, new ConstructYamlFloat());
    yamlConstructors.put(Tag.BINARY, new ConstructYamlBinary());
    yamlConstructors.put(Tag.TIMESTAMP, new ConstructYamlTimestamp());
    yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
    yamlConstructors.put(Tag.PAIRS, new ConstructYamlPairs());
    yamlConstructors.put(Tag.SET, new ConstructYamlSet());
    yamlConstructors.put(Tag.STR, new ConstructYamlStr());
    yamlConstructors.put(Tag.SEQ, new ConstructYamlSeq());
    yamlConstructors.put(Tag.MAP, new ConstructYamlMap());
    yamlConstructors.put(null, undefinedConstructor);
    yamlClassConstructors.put(NodeId.scalar, undefinedConstructor);
    yamlClassConstructors.put(NodeId.sequence, undefinedConstructor);
    yamlClassConstructors.put(NodeId.mapping, undefinedConstructor);
  }
  
  protected void flattenMapping(MappingNode node)
  {
    if (node.isMerged()) {
      node.setValue(mergeNode(node, true, new HashMap(), new ArrayList()));
    }
  }
  















  private List<NodeTuple> mergeNode(MappingNode node, boolean isPreffered, Map<Object, Integer> key2index, List<NodeTuple> values)
  {
    List<NodeTuple> nodeValue = node.getValue();
    
    Collections.reverse(nodeValue);
    for (Iterator<NodeTuple> iter = nodeValue.iterator(); iter.hasNext();) {
      NodeTuple nodeTuple = (NodeTuple)iter.next();
      Node keyNode = nodeTuple.getKeyNode();
      Node valueNode = nodeTuple.getValueNode();
      if (keyNode.getTag().equals(Tag.MERGE)) {
        iter.remove();
        switch (1.$SwitchMap$org$yaml$snakeyaml$nodes$NodeId[valueNode.getNodeId().ordinal()]) {
        case 1: 
          MappingNode mn = (MappingNode)valueNode;
          mergeNode(mn, false, key2index, values);
          break;
        case 2: 
          SequenceNode sn = (SequenceNode)valueNode;
          List<Node> vals = sn.getValue();
          for (Node subnode : vals) {
            if (!(subnode instanceof MappingNode)) {
              throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping for merging, but found " + subnode.getNodeId(), subnode.getStartMark());
            }
            


            MappingNode mnode = (MappingNode)subnode;
            mergeNode(mnode, false, key2index, values);
          }
          break;
        default: 
          throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping or list of mappings for merging, but found " + valueNode.getNodeId(), valueNode.getStartMark());
        
        }
        
      }
      else
      {
        Object key = constructObject(keyNode);
        if (!key2index.containsKey(key)) {
          values.add(nodeTuple);
          
          key2index.put(key, Integer.valueOf(values.size() - 1));
        } else if (isPreffered)
        {

          values.set(((Integer)key2index.get(key)).intValue(), nodeTuple);
        }
      }
    }
    return values;
  }
  
  protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
    flattenMapping(node);
    super.constructMapping2ndStep(node, mapping);
  }
  
  protected void constructSet2ndStep(MappingNode node, Set<Object> set)
  {
    flattenMapping(node);
    super.constructSet2ndStep(node, set);
  }
  
  public class ConstructYamlNull extends AbstractConstruct { public ConstructYamlNull() {}
    
    public Object construct(Node node) { constructScalar((ScalarNode)node);
      return null;
    }
  }
  
  private static final Map<String, Boolean> BOOL_VALUES = new HashMap();
  
  static { BOOL_VALUES.put("yes", Boolean.TRUE);
    BOOL_VALUES.put("no", Boolean.FALSE);
    BOOL_VALUES.put("true", Boolean.TRUE);
    BOOL_VALUES.put("false", Boolean.FALSE);
    BOOL_VALUES.put("on", Boolean.TRUE);
    BOOL_VALUES.put("off", Boolean.FALSE);
  }
  
  public class ConstructYamlBool extends AbstractConstruct { public ConstructYamlBool() {}
    
    public Object construct(Node node) { String val = (String)constructScalar((ScalarNode)node);
      return SafeConstructor.BOOL_VALUES.get(val.toLowerCase());
    }
  }
  
  public class ConstructYamlInt extends AbstractConstruct { public ConstructYamlInt() {}
    
    public Object construct(Node node) { String value = constructScalar((ScalarNode)node).toString().replaceAll("_", "");
      int sign = 1;
      char first = value.charAt(0);
      if (first == '-') {
        sign = -1;
        value = value.substring(1);
      } else if (first == '+') {
        value = value.substring(1);
      }
      int base = 10;
      if ("0".equals(value))
        return Integer.valueOf(0);
      if (value.startsWith("0b")) {
        value = value.substring(2);
        base = 2;
      } else if (value.startsWith("0x")) {
        value = value.substring(2);
        base = 16;
      } else if (value.startsWith("0")) {
        value = value.substring(1);
        base = 8;
      } else { if (value.indexOf(':') != -1) {
          String[] digits = value.split(":");
          int bes = 1;
          int val = 0;
          int i = 0; for (int j = digits.length; i < j; i++) {
            val = (int)(val + Long.parseLong(digits[(j - i - 1)]) * bes);
            bes *= 60;
          }
          return SafeConstructor.this.createNumber(sign, String.valueOf(val), 10);
        }
        return SafeConstructor.this.createNumber(sign, value, 10);
      }
      return SafeConstructor.this.createNumber(sign, value, base);
    }
  }
  
  private Number createNumber(int sign, String number, int radix)
  {
    if (sign < 0)
      number = "-" + number;
    Number result;
    try {
      result = Integer.valueOf(number, radix);
    } catch (NumberFormatException e) {
      try {
        result = Long.valueOf(number, radix);
      } catch (NumberFormatException e1) {
        result = new BigInteger(number, radix);
      }
    }
    return result;
  }
  
  public class ConstructYamlFloat extends AbstractConstruct { public ConstructYamlFloat() {}
    
    public Object construct(Node node) { String value = constructScalar((ScalarNode)node).toString().replaceAll("_", "");
      int sign = 1;
      char first = value.charAt(0);
      if (first == '-') {
        sign = -1;
        value = value.substring(1);
      } else if (first == '+') {
        value = value.substring(1);
      }
      String valLower = value.toLowerCase();
      if (".inf".equals(valLower))
        return new Double(sign == -1 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
      if (".nan".equals(valLower))
        return new Double(NaN.0D);
      if (value.indexOf(':') != -1) {
        String[] digits = value.split(":");
        int bes = 1;
        double val = 0.0D;
        int i = 0; for (int j = digits.length; i < j; i++) {
          val += Double.parseDouble(digits[(j - i - 1)]) * bes;
          bes *= 60;
        }
        return new Double(sign * val);
      }
      Double d = Double.valueOf(value);
      return new Double(d.doubleValue() * sign);
    }
  }
  
  public class ConstructYamlBinary extends AbstractConstruct {
    public ConstructYamlBinary() {}
    
    public Object construct(Node node) { byte[] decoded = Base64Coder.decode(constructScalar((ScalarNode)node).toString().toCharArray());
      
      return decoded;
    }
  }
  
  public class ConstructYamlNumber extends AbstractConstruct { public ConstructYamlNumber() {}
    
    private final NumberFormat nf = NumberFormat.getInstance();
    
    public Object construct(Node node) {
      ScalarNode scalar = (ScalarNode)node;
      try {
        return nf.parse(scalar.getValue());
      } catch (ParseException e) {
        String lowerCaseValue = scalar.getValue().toLowerCase();
        if ((lowerCaseValue.contains("inf")) || (lowerCaseValue.contains("nan")))
        {





          return (Number)((Construct)yamlConstructors.get(Tag.FLOAT)).construct(node);
        }
        throw new IllegalArgumentException("Unable to parse as Number: " + scalar.getValue());
      }
    }
  }
  


  private static final Pattern TIMESTAMP_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
  
  private static final Pattern YMD_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");
  
  public static class ConstructYamlTimestamp extends AbstractConstruct {
    private Calendar calendar;
    
    public ConstructYamlTimestamp() {}
    
    public Calendar getCalendar() { return calendar; }
    
    public Object construct(Node node)
    {
      ScalarNode scalar = (ScalarNode)node;
      String nodeValue = scalar.getValue();
      Matcher match = SafeConstructor.YMD_REGEXP.matcher(nodeValue);
      if (match.matches()) {
        String year_s = match.group(1);
        String month_s = match.group(2);
        String day_s = match.group(3);
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.set(1, Integer.parseInt(year_s));
        
        calendar.set(2, Integer.parseInt(month_s) - 1);
        calendar.set(5, Integer.parseInt(day_s));
        return calendar.getTime();
      }
      match = SafeConstructor.TIMESTAMP_REGEXP.matcher(nodeValue);
      if (!match.matches()) {
        throw new YAMLException("Unexpected timestamp: " + nodeValue);
      }
      String year_s = match.group(1);
      String month_s = match.group(2);
      String day_s = match.group(3);
      String hour_s = match.group(4);
      String min_s = match.group(5);
      
      String seconds = match.group(6);
      String millis = match.group(7);
      if (millis != null) {
        seconds = seconds + "." + millis;
      }
      double fractions = Double.parseDouble(seconds);
      int sec_s = (int)Math.round(Math.floor(fractions));
      int usec = (int)Math.round((fractions - sec_s) * 1000.0D);
      
      String timezoneh_s = match.group(8);
      String timezonem_s = match.group(9);
      TimeZone timeZone;
      TimeZone timeZone; if (timezoneh_s != null) {
        String time = timezonem_s != null ? ":" + timezonem_s : "00";
        timeZone = TimeZone.getTimeZone("GMT" + timezoneh_s + time);
      }
      else {
        timeZone = TimeZone.getTimeZone("UTC");
      }
      calendar = Calendar.getInstance(timeZone);
      calendar.set(1, Integer.parseInt(year_s));
      
      calendar.set(2, Integer.parseInt(month_s) - 1);
      calendar.set(5, Integer.parseInt(day_s));
      calendar.set(11, Integer.parseInt(hour_s));
      calendar.set(12, Integer.parseInt(min_s));
      calendar.set(13, sec_s);
      calendar.set(14, usec);
      return calendar.getTime();
    }
  }
  
  public class ConstructYamlOmap extends AbstractConstruct
  {
    public ConstructYamlOmap() {}
    
    public Object construct(Node node) {
      Map<Object, Object> omap = new LinkedHashMap();
      if (!(node instanceof SequenceNode)) {
        throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a sequence, but found " + node.getNodeId(), node.getStartMark());
      }
      

      SequenceNode snode = (SequenceNode)node;
      for (Node subnode : snode.getValue()) {
        if (!(subnode instanceof MappingNode)) {
          throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a mapping of length 1, but found " + subnode.getNodeId(), subnode.getStartMark());
        }
        

        MappingNode mnode = (MappingNode)subnode;
        if (mnode.getValue().size() != 1) {
          throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a single mapping item, but found " + mnode.getValue().size() + " items", mnode.getStartMark());
        }
        

        Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
        Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
        Object key = constructObject(keyNode);
        Object value = constructObject(valueNode);
        omap.put(key, value);
      }
      return omap;
    }
  }
  
  public class ConstructYamlPairs extends AbstractConstruct
  {
    public ConstructYamlPairs() {}
    
    public Object construct(Node node) {
      if (!(node instanceof SequenceNode)) {
        throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a sequence, but found " + node.getNodeId(), node.getStartMark());
      }
      
      SequenceNode snode = (SequenceNode)node;
      List<Object[]> pairs = new ArrayList(snode.getValue().size());
      for (Node subnode : snode.getValue()) {
        if (!(subnode instanceof MappingNode)) {
          throw new ConstructorException("while constructingpairs", node.getStartMark(), "expected a mapping of length 1, but found " + subnode.getNodeId(), subnode.getStartMark());
        }
        

        MappingNode mnode = (MappingNode)subnode;
        if (mnode.getValue().size() != 1) {
          throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a single mapping item, but found " + mnode.getValue().size() + " items", mnode.getStartMark());
        }
        

        Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
        Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
        Object key = constructObject(keyNode);
        Object value = constructObject(valueNode);
        pairs.add(new Object[] { key, value });
      }
      return pairs;
    }
  }
  
  public class ConstructYamlSet implements Construct { public ConstructYamlSet() {}
    
    public Object construct(Node node) { if (node.isTwoStepsConstruction()) {
        return createDefaultSet();
      }
      return constructSet((MappingNode)node);
    }
    

    public void construct2ndStep(Node node, Object object)
    {
      if (node.isTwoStepsConstruction()) {
        constructSet2ndStep((MappingNode)node, (Set)object);
      } else
        throw new YAMLException("Unexpected recursive set structure. Node: " + node);
    }
  }
  
  public class ConstructYamlStr extends AbstractConstruct {
    public ConstructYamlStr() {}
    
    public Object construct(Node node) { return constructScalar((ScalarNode)node); }
  }
  
  public class ConstructYamlSeq implements Construct {
    public ConstructYamlSeq() {}
    
    public Object construct(Node node) { SequenceNode seqNode = (SequenceNode)node;
      if (node.isTwoStepsConstruction()) {
        return createDefaultList(seqNode.getValue().size());
      }
      return constructSequence(seqNode);
    }
    

    public void construct2ndStep(Node node, Object data)
    {
      if (node.isTwoStepsConstruction()) {
        constructSequenceStep2((SequenceNode)node, (List)data);
      } else
        throw new YAMLException("Unexpected recursive sequence structure. Node: " + node);
    }
  }
  
  public class ConstructYamlMap implements Construct {
    public ConstructYamlMap() {}
    
    public Object construct(Node node) { if (node.isTwoStepsConstruction()) {
        return createDefaultMap();
      }
      return constructMapping((MappingNode)node);
    }
    

    public void construct2ndStep(Node node, Object object)
    {
      if (node.isTwoStepsConstruction()) {
        constructMapping2ndStep((MappingNode)node, (Map)object);
      } else
        throw new YAMLException("Unexpected recursive mapping structure. Node: " + node);
    }
  }
  
  public static final class ConstructUndefined extends AbstractConstruct {
    public ConstructUndefined() {}
    
    public Object construct(Node node) { throw new ConstructorException(null, null, "could not determine a constructor for the tag " + node.getTag(), node.getStartMark()); }
  }
}
