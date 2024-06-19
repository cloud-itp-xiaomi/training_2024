package com.txh.xiaomi2024.work.service.util;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class BeanMapUtil {
    /**
     * map转对象
     */
    public static <T> T mapToBean(Map<String, Object> map,
                                  Class<T> beanClass) {
        T object = null;
        try {
            object = beanClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert object != null;
        Field[] fields = object.getClass()
                .getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod)
                    || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                try {
                    // 这里直接将 mapValue 放入指定字段可能会出现类型无法转换问题：map 存 Integer 区间的 Long 类型会将其转为 Integer
                    Object mapValue = map.get(field.getName());
                    field.set(
                            object,
                            ConvertUtils.convert(
                                    mapValue,
                                    field.getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;

    }
}
