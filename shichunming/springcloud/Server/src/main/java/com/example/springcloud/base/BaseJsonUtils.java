package com.example.springcloud.base;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @ClassName BaseJsonUtils
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 21:30
 **/
@Slf4j
public class BaseJsonUtils {

    @SuppressWarnings({ "rawtypes" })
    public static final Set<Class> JSON_IGNORED_CLASSES = ImmutableSet.of(InputStream.class, OutputStream.class, File.class,
            Logger.class, java.util.logging.Logger.class, Class.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectMapper MAPPER_IGNORE_UNKNOWN = new ObjectMapper();
    private static final ObjectMapper MAPPER_SCAN_ANNO = new ObjectMapper();
    private static final SimpleModule MODULE;

    static {
        SimpleSerializers simpleSerializers = new SimpleSerializers();
        SimpleDeserializers simpleDeserializers = new SimpleDeserializers();
        JSON_IGNORED_CLASSES.forEach(cls -> simpleSerializers.addSerializer(new ClassNameSerializer(cls)));
        MODULE = new SimpleModule();
        simpleSerializers.addSerializer(LocalDateTimeSerializer.INSTANCE);
        simpleSerializers.addSerializer(LocalDateSerializer.INSTANCE);
        simpleDeserializers.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        simpleDeserializers.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
        MODULE.setSerializers(simpleSerializers);
        MODULE.setDeserializers(simpleDeserializers);

        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector() {
            private static final long serialVersionUID = 279707890468846409L;

            @Override
            public Object findSerializer(Annotated a) {
                return super.findSerializer(a);
            }
        };

        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(FAIL_ON_UNKNOWN_PROPERTIES, true);
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        MAPPER.registerModule(MODULE);

        MAPPER_IGNORE_UNKNOWN.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER_IGNORE_UNKNOWN.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER_IGNORE_UNKNOWN.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER_IGNORE_UNKNOWN.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        MAPPER_IGNORE_UNKNOWN.registerModule(MODULE);

        MAPPER_SCAN_ANNO.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER_SCAN_ANNO.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER_SCAN_ANNO.configure(FAIL_ON_UNKNOWN_PROPERTIES, true);
        MAPPER_SCAN_ANNO.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        MAPPER_SCAN_ANNO.setAnnotationIntrospector(introspector);
        MAPPER_SCAN_ANNO.registerModule(MODULE);
    }

    public static ObjectMapper defaultMapper() {
        return MAPPER_IGNORE_UNKNOWN;
    }

    public static <T> T readValue(String s, Class<T> cls) {
        return readValue(s, cls, true);
    }

    /**
     * 读map
     *
     * @param json json数据
     * @return map
     */
    public static Map<String, Object> readValueMap(String json) {
        return readValue(json, Map.class, true);
    }

    public static <T> T readValueChecked(String s, Class<T> cls) throws MyException, JsonProcessingException {
        return readValueChecked(s, cls, true);
    }

    public static <T> T readValue(String s, Class<T> cls, boolean ignoreUnknown) {
        Preconditions.checkArgument(cls != null, "Class类型不能为空");
        if (cls == String.class) {
            return (T) s;
        }

        if (StrUtil.isBlank(s)) {
            return null;
        }

        try {
            if (ignoreUnknown) {
                return MAPPER_IGNORE_UNKNOWN.readValue(s, cls);
            } else {
                return MAPPER.readValue(s, cls);
            }
        } catch (Throwable e) {
            log.error("无法将{}转换为类型为[{}]的对象: {}", s, cls.getSimpleName(), e);
            return null;
        }
    }

    public static <T> T readValueChecked(String s, Class<T> cls, boolean ignoreUnknown) throws MyException, JsonProcessingException {
        Preconditions.checkArgument(StrUtil.isNotBlank(s), "字串不能为空");
        Preconditions.checkArgument(cls != null, "Class类型不能为空");
        if (cls == String.class) {
            return (T) s;
        }

        if (ignoreUnknown) {
            return MAPPER_IGNORE_UNKNOWN.readValue(s, cls);
        } else {
            return MAPPER.readValue(s, cls);
        }
    }

    @SuppressWarnings({ "rawtypes" })
    public static <T> T readValue(String s, TypeReference typeReference) {
        return readValue(s, typeReference, true);
    }

    @SuppressWarnings({ "rawtypes" })
    public static <T> T readValue(String s, TypeReference typeReference, boolean ignoreUnknown) {
        Preconditions.checkArgument(StrUtil.isNotBlank(s), "字串不能为空");
        Preconditions.checkArgument(typeReference != null, "TypeWrapper类型不能为空");

        try {
            if (ignoreUnknown) {
                return (T) MAPPER_IGNORE_UNKNOWN.readValue(s, typeReference);
            } else {
                return (T) MAPPER.readValue(s, typeReference);
            }
        } catch (Throwable e) {
            log.error("无法将{}转换为类型为[{}]的对象: {}", s, typeReference.getType(), e);
            return null;
        }
    }

    public static <T> T readValue(Map<String, Object> map, Class<T> cls) {
        return readValue(map, cls, true);
    }

    public static <T> T readValue(Map<String, Object> map, Class<T> cls, boolean ignoreUnknown) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        Preconditions.checkArgument(cls != null, "Class类型不能为空");
        if (cls == String.class) {
            return (T) BaseJsonUtils.writeValue(map);
        }

        try {
            if (ignoreUnknown) {
                return MAPPER_IGNORE_UNKNOWN.convertValue(map, cls);
            } else {
                return MAPPER.convertValue(map, cls);
            }
        } catch (Throwable e) {
            log.error("无法将Map{}转换为类型为[{}]的对象: {}", map, cls.getSimpleName(), e);
            return null;
        }
    }

    public static <T> List<T> readValues(String s, Class<T> cls) {
        return readValues(s, cls, true);
    }

    @SuppressWarnings({ "rawtypes" })
    public static <T> List<T> readValues(String s, TypeReference ref, boolean ignoreUnknown) {
        Preconditions.checkArgument(StrUtil.isNotBlank(s), "字串不能为空");
        Preconditions.checkArgument(ref != null, "Class类型不能为空");

        try {
            if (ignoreUnknown) {
                return (List<T>) MAPPER_IGNORE_UNKNOWN.readValue(s, ref);
            } else {
                return (List<T>) MAPPER.readValue(s, ref);
            }
        } catch (Throwable e) {
            log.error("无法将{}转换为数组对象: {}", s, e);
            return Collections.emptyList();
        }
    }

    @SuppressWarnings({ "rawtypes" })
    public static <T> List<T> readValues(String s, Class<T> cls, boolean ignoreUnknown) {
        Preconditions.checkArgument(StrUtil.isNotBlank(s), "字串不能为空");
        Preconditions.checkArgument(cls != null, "Class类型不能为空");

        List list;
        try {
            if (ignoreUnknown) {
                list = MAPPER_IGNORE_UNKNOWN.readValue(s, List.class);
            } else {
                list = MAPPER.readValue(s, List.class);
            }
        } catch (Throwable e) {
            log.error("无法将{}转换为数组对象: {}", s, e);
            return Collections.emptyList();
        }

        if (cls == List.class) {
            return list;
        }

        return (List<T>) list.stream().map(ele -> {
            if (ele instanceof Map) {
                return readValue((Map) ele, cls);
            } else {
                return readValue(ele.toString(), cls);
            }
        }).collect(Collectors.toList());
    }

    public static String writeValue(Object obj) {
        return writeValue(obj, false);
    }

    public static String writeValue(Object obj, boolean scanAnno) {
        if (obj == null) {
            return null;
        }

        if (obj.getClass().isPrimitive()) {
            return obj.toString();
        }

        try {
            if (scanAnno) {
                return MAPPER_SCAN_ANNO.writeValueAsString(obj);
            } else {
                return MAPPER.writeValueAsString(obj);
            }
        } catch (Throwable e) {
            log.error("json转换异常", e);
            return null;
        }
    }

    public static SimpleModule getSimpleModule() {
        return MODULE;
    }

    public static ObjectMapper getMapper(boolean ignoreUnknown) {
        return ignoreUnknown ? MAPPER_IGNORE_UNKNOWN : MAPPER;
    }

    @SuppressWarnings({ "rawtypes" })
    private static JsonStructure flatJsonValue(Object value) {
        if (value instanceof List) {
            Object item = ((List) value).get(0);
            JsonStructure jsonStructure = flatJsonValue(item);
            jsonStructure.setDepth(jsonStructure.getDepth() + 1);
            return jsonStructure;
        } else {
            return new JsonStructure(value);
        }
    }

    private static String wrapWithDepth(int depth, String type) {
        if (depth == 0) {
            return type;
        }

        StringBuilder buf = new StringBuilder("List");
        IntStream.range(0, depth).forEach(i -> buf.append("<"));
        buf.append(type);
        IntStream.range(0, depth).forEach(i -> buf.append(">"));
        return buf.toString();
    }


    @Data
    static class JsonStructure {
        private Object value;
        private int depth = 0;

        public JsonStructure(Object value) {
            this.value = value;
        }
    }

    @SuppressWarnings({ "rawtypes" })
    public static class ClassNameSerializer extends StdSerializer {
        private static final long serialVersionUID = -3184203999342210098L;
        private final Class cls;

        public ClassNameSerializer(Class cls) {
            super(cls);
            this.cls = cls;
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value == null) {
                gen.writeString("null");
            } else {
                gen.writeString(cls.getSimpleName());
            }
        }
    }
}
