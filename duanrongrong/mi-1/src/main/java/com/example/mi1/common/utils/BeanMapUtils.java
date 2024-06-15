package com.example.mi1.common.utils;

import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author uchin/李玉勤
 * @date 2023/3/29 22:05
 * @description
 */
public class BeanMapUtils {
    /**
     * 对象转Map
     */
    public static Map<String, Object> beanToMap(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * map转对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        T object = null;
        try {
            object = beanClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert object != null;
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                try {
                    // 这里直接将 mapValue 放入指定字段可能会出现类型无法转换问题：map 存 Integer 区间的 Long 类型会将其转为 Integer
                    Object mapValue = map.get(field.getName());
                    field.set(object, ConvertUtils.convert(mapValue, field.getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;

    }

    public static List<Map<String, Object>> beansToMaps(List<Object> beanList) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (beanList != null && beanList.size() > 0) {
            Map<String, Object> map = null;
            for (Object t : beanList) {
                list.add(beanToMap(t));
            }
        }
        return list;
    }

    public static <T> List<T> mapsToBeans(List<Map<String, Object>> maps, Class<T> beanClass) {
        ArrayList<T> objects = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            objects.add(mapToBean(map, beanClass));
        }
        return objects;
    }
}
