package com.github.bg.admin.core.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linzf
 * @since 2019-04-25
 * 类描述：json转换通用工具类
 */
public class JsonUtils {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final MapType MAP = MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
    private static final MapType STRING_MAP = MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
    private static final JavaType LIST_MAP = MAPPER.getTypeFactory().constructParametricType(ArrayList.class, MAP);

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtils() {
    }

    public static <T> T map2object(Map map,Class<T> beanType) {
        try {
            return MAPPER.readValue( MAPPER.writeValueAsString(map), beanType);
        } catch (Exception e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }


    public static String objToJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }

    public static <T> T jsonToPojo(String json, Class<T> beanType) {
        try {
            return MAPPER.readValue(json, beanType);
        } catch (IOException e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }

    public static <T> T objToPojo(Object obj, Class<T> valueType) {
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(obj), valueType);
        } catch (IOException e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }

    public static <T> ArrayList<T> objToList(Object obj, Class<T> valueType) {
        JavaType listType = MAPPER.getTypeFactory().constructParametricType(ArrayList.class, valueType);
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(obj), listType);
        } catch (IOException e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }

    public static <T> ArrayList<T> jsonToList(String json, Class<T> valueType) {
        JavaType listType = MAPPER.getTypeFactory().constructParametricType(ArrayList.class, valueType);
        try {
            return MAPPER.readValue(json, listType);
        } catch (IOException e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }

    public static HashMap<String, Object> objToMap(Object obj) {
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(obj), MAP);
        } catch (IOException e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }

    public static HashMap<String, Object> jsonToMap(String json) {
        try {
            return MAPPER.readValue(json, MAP);
        } catch (IOException e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }

    public static HashMap<String, String> jsonToStringMap(String json) {
        try {
            return MAPPER.readValue(json, STRING_MAP);
        } catch (IOException e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }

    public static ArrayList<HashMap<String, Object>> objToMapList(Object obj) {
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(obj), LIST_MAP);
        } catch (IOException e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }

    public static ArrayList<HashMap<String, Object>> jsonToMapList(String json) {
        try {
            return MAPPER.readValue(json, LIST_MAP);
        } catch (IOException e) {
            throw new RuntimeException("Jackson处理出现错误", e);
        }
    }
}
