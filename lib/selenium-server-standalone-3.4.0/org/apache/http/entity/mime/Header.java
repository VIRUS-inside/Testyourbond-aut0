package org.apache.http.entity.mime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;






























public class Header
  implements Iterable<MinimalField>
{
  private final List<MinimalField> fields;
  private final Map<String, List<MinimalField>> fieldMap;
  
  public Header()
  {
    fields = new LinkedList();
    fieldMap = new HashMap();
  }
  
  public void addField(MinimalField field) {
    if (field == null) {
      return;
    }
    String key = field.getName().toLowerCase(Locale.ROOT);
    List<MinimalField> values = (List)fieldMap.get(key);
    if (values == null) {
      values = new LinkedList();
      fieldMap.put(key, values);
    }
    values.add(field);
    fields.add(field);
  }
  
  public List<MinimalField> getFields() {
    return new ArrayList(fields);
  }
  
  public MinimalField getField(String name) {
    if (name == null) {
      return null;
    }
    String key = name.toLowerCase(Locale.ROOT);
    List<MinimalField> list = (List)fieldMap.get(key);
    if ((list != null) && (!list.isEmpty())) {
      return (MinimalField)list.get(0);
    }
    return null;
  }
  
  public List<MinimalField> getFields(String name) {
    if (name == null) {
      return null;
    }
    String key = name.toLowerCase(Locale.ROOT);
    List<MinimalField> list = (List)fieldMap.get(key);
    if ((list == null) || (list.isEmpty())) {
      return Collections.emptyList();
    }
    return new ArrayList(list);
  }
  
  public int removeFields(String name)
  {
    if (name == null) {
      return 0;
    }
    String key = name.toLowerCase(Locale.ROOT);
    List<MinimalField> removed = (List)fieldMap.remove(key);
    if ((removed == null) || (removed.isEmpty())) {
      return 0;
    }
    fields.removeAll(removed);
    return removed.size();
  }
  
  public void setField(MinimalField field) {
    if (field == null) {
      return;
    }
    String key = field.getName().toLowerCase(Locale.ROOT);
    List<MinimalField> list = (List)fieldMap.get(key);
    if ((list == null) || (list.isEmpty())) {
      addField(field);
      return;
    }
    list.clear();
    list.add(field);
    int firstOccurrence = -1;
    int index = 0;
    for (Iterator<MinimalField> it = fields.iterator(); it.hasNext(); index++) {
      MinimalField f = (MinimalField)it.next();
      if (f.getName().equalsIgnoreCase(field.getName())) {
        it.remove();
        if (firstOccurrence == -1) {
          firstOccurrence = index;
        }
      }
    }
    fields.add(firstOccurrence, field);
  }
  
  public Iterator<MinimalField> iterator()
  {
    return Collections.unmodifiableList(fields).iterator();
  }
  
  public String toString()
  {
    return fields.toString();
  }
}
