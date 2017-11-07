package org.yaml.snakeyaml.constructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;





















public abstract class BaseConstructor
{
  protected final Map<NodeId, Construct> yamlClassConstructors = new EnumMap(NodeId.class);
  







  protected final Map<Tag, Construct> yamlConstructors = new HashMap();
  



  protected final Map<String, Construct> yamlMultiConstructors = new HashMap();
  
  private Composer composer;
  private final Map<Node, Object> constructedObjects;
  private final Set<Node> recursiveObjects;
  private final ArrayList<RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>> maps2fill;
  private final ArrayList<RecursiveTuple<Set<Object>, Object>> sets2fill;
  protected Tag rootTag;
  private PropertyUtils propertyUtils;
  private boolean explicitPropertyUtils;
  
  public BaseConstructor()
  {
    constructedObjects = new HashMap();
    recursiveObjects = new HashSet();
    maps2fill = new ArrayList();
    sets2fill = new ArrayList();
    rootTag = null;
    explicitPropertyUtils = false;
  }
  
  public void setComposer(Composer composer) {
    this.composer = composer;
  }
  





  public boolean checkData()
  {
    return composer.checkNode();
  }
  





  public Object getData()
  {
    composer.checkNode();
    Node node = composer.getNode();
    if (rootTag != null) {
      node.setTag(rootTag);
    }
    return constructDocument(node);
  }
  







  public Object getSingleData(Class<?> type)
  {
    Node node = composer.getSingleNode();
    if (node != null) {
      if (Object.class != type) {
        node.setTag(new Tag(type));
      } else if (rootTag != null) {
        node.setTag(rootTag);
      }
      return constructDocument(node);
    }
    return null;
  }
  







  private Object constructDocument(Node node)
  {
    Object data = constructObject(node);
    fillRecursive();
    constructedObjects.clear();
    recursiveObjects.clear();
    return data;
  }
  
  private void fillRecursive() {
    if (!maps2fill.isEmpty()) {
      for (RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>> entry : maps2fill) {
        RecursiveTuple<Object, Object> key_value = (RecursiveTuple)entry._2();
        ((Map)entry._1()).put(key_value._1(), key_value._2());
      }
      maps2fill.clear();
    }
    if (!sets2fill.isEmpty()) {
      for (RecursiveTuple<Set<Object>, Object> value : sets2fill) {
        ((Set)value._1()).add(value._2());
      }
      sets2fill.clear();
    }
  }
  







  protected Object constructObject(Node node)
  {
    if (constructedObjects.containsKey(node)) {
      return constructedObjects.get(node);
    }
    if (recursiveObjects.contains(node)) {
      throw new ConstructorException(null, null, "found unconstructable recursive node", node.getStartMark());
    }
    
    recursiveObjects.add(node);
    Construct constructor = getConstructor(node);
    Object data = constructor.construct(node);
    constructedObjects.put(node, data);
    recursiveObjects.remove(node);
    if (node.isTwoStepsConstruction()) {
      constructor.construct2ndStep(node, data);
    }
    return data;
  }
  








  protected Construct getConstructor(Node node)
  {
    if (node.useClassConstructor()) {
      return (Construct)yamlClassConstructors.get(node.getNodeId());
    }
    Construct constructor = (Construct)yamlConstructors.get(node.getTag());
    if (constructor == null) {
      for (String prefix : yamlMultiConstructors.keySet()) {
        if (node.getTag().startsWith(prefix)) {
          return (Construct)yamlMultiConstructors.get(prefix);
        }
      }
      return (Construct)yamlConstructors.get(null);
    }
    return constructor;
  }
  
  protected Object constructScalar(ScalarNode node)
  {
    return node.getValue();
  }
  
  protected List<Object> createDefaultList(int initSize) {
    return new ArrayList(initSize);
  }
  
  protected Set<Object> createDefaultSet(int initSize) {
    return new LinkedHashSet(initSize);
  }
  
  protected Object createArray(Class<?> type, int size) {
    return Array.newInstance(type.getComponentType(), size);
  }
  
  protected List<? extends Object> constructSequence(SequenceNode node)
  {
    List<Object> result;
    if ((List.class.isAssignableFrom(node.getType())) && (!node.getType().isInterface())) {
      try
      {
        result = (List)node.getType().newInstance();
      } catch (Exception e) {
        throw new YAMLException(e);
      }
    } else {
      result = createDefaultList(node.getValue().size());
    }
    constructSequenceStep2(node, result);
    return result;
  }
  

  protected Set<? extends Object> constructSet(SequenceNode node)
  {
    Set<Object> result;
    if (!node.getType().isInterface()) {
      try
      {
        result = (Set)node.getType().newInstance();
      } catch (Exception e) {
        throw new YAMLException(e);
      }
    } else {
      result = createDefaultSet(node.getValue().size());
    }
    constructSequenceStep2(node, result);
    return result;
  }
  
  protected Object constructArray(SequenceNode node)
  {
    return constructArrayStep2(node, createArray(node.getType(), node.getValue().size()));
  }
  
  protected void constructSequenceStep2(SequenceNode node, Collection<Object> collection) {
    for (Node child : node.getValue()) {
      collection.add(constructObject(child));
    }
  }
  
  protected Object constructArrayStep2(SequenceNode node, Object array) {
    Class<?> componentType = node.getType().getComponentType();
    
    int index = 0;
    for (Node child : node.getValue())
    {
      if (child.getType() == Object.class) {
        child.setType(componentType);
      }
      
      Object value = constructObject(child);
      
      if (componentType.isPrimitive())
      {
        if (value == null) {
          throw new NullPointerException("Unable to construct element value for " + child);
        }
        

        if (Byte.TYPE.equals(componentType)) {
          Array.setByte(array, index, ((Number)value).byteValue());
        }
        else if (Short.TYPE.equals(componentType)) {
          Array.setShort(array, index, ((Number)value).shortValue());
        }
        else if (Integer.TYPE.equals(componentType)) {
          Array.setInt(array, index, ((Number)value).intValue());
        }
        else if (Long.TYPE.equals(componentType)) {
          Array.setLong(array, index, ((Number)value).longValue());
        }
        else if (Float.TYPE.equals(componentType)) {
          Array.setFloat(array, index, ((Number)value).floatValue());
        }
        else if (Double.TYPE.equals(componentType)) {
          Array.setDouble(array, index, ((Number)value).doubleValue());
        }
        else if (Character.TYPE.equals(componentType)) {
          Array.setChar(array, index, ((Character)value).charValue());
        }
        else if (Boolean.TYPE.equals(componentType)) {
          Array.setBoolean(array, index, ((Boolean)value).booleanValue());
        }
        else {
          throw new YAMLException("unexpected primitive type");
        }
      }
      else
      {
        Array.set(array, index, value);
      }
      
      index++;
    }
    return array;
  }
  
  protected Map<Object, Object> createDefaultMap()
  {
    return new LinkedHashMap();
  }
  
  protected Set<Object> createDefaultSet()
  {
    return new LinkedHashSet();
  }
  
  protected Set<Object> constructSet(MappingNode node) {
    Set<Object> set = createDefaultSet();
    constructSet2ndStep(node, set);
    return set;
  }
  
  protected Map<Object, Object> constructMapping(MappingNode node) {
    Map<Object, Object> mapping = createDefaultMap();
    constructMapping2ndStep(node, mapping);
    return mapping;
  }
  
  protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
    List<NodeTuple> nodeValue = node.getValue();
    for (NodeTuple tuple : nodeValue) {
      Node keyNode = tuple.getKeyNode();
      Node valueNode = tuple.getValueNode();
      Object key = constructObject(keyNode);
      if (key != null) {
        try {
          key.hashCode();
        } catch (Exception e) {
          throw new ConstructorException("while constructing a mapping", node.getStartMark(), "found unacceptable key " + key, tuple.getKeyNode().getStartMark(), e);
        }
      }
      

      Object value = constructObject(valueNode);
      if (keyNode.isTwoStepsConstruction())
      {





        maps2fill.add(0, new RecursiveTuple(mapping, new RecursiveTuple(key, value)));
      }
      else
      {
        mapping.put(key, value);
      }
    }
  }
  
  protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
    List<NodeTuple> nodeValue = node.getValue();
    for (NodeTuple tuple : nodeValue) {
      Node keyNode = tuple.getKeyNode();
      Object key = constructObject(keyNode);
      if (key != null) {
        try {
          key.hashCode();
        } catch (Exception e) {
          throw new ConstructorException("while constructing a Set", node.getStartMark(), "found unacceptable key " + key, tuple.getKeyNode().getStartMark(), e);
        }
      }
      
      if (keyNode.isTwoStepsConstruction())
      {





        sets2fill.add(0, new RecursiveTuple(set, key));
      } else {
        set.add(key);
      }
    }
  }
  
  public void setPropertyUtils(PropertyUtils propertyUtils) {
    this.propertyUtils = propertyUtils;
    explicitPropertyUtils = true;
  }
  
  public final PropertyUtils getPropertyUtils() {
    if (propertyUtils == null) {
      propertyUtils = new PropertyUtils();
    }
    return propertyUtils;
  }
  
  private static class RecursiveTuple<T, K> {
    private final T _1;
    private final K _2;
    
    public RecursiveTuple(T _1, K _2) {
      this._1 = _1;
      this._2 = _2;
    }
    
    public K _2() {
      return _2;
    }
    
    public T _1() {
      return _1;
    }
  }
  
  public final boolean isExplicitPropertyUtils() {
    return explicitPropertyUtils;
  }
}
