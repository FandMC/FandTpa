package com.fandtpa.util;

import java.util.HashMap;
import java.util.Map;

public class JSON {
    private final Map<String, Object> jsonMap = new HashMap<>();

    public JSON(String jsonString) {
        parse(jsonString);
    }

    private void parse(String jsonString) {
        jsonString = jsonString.trim();

        // 去掉开头和结尾的花括号
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1).trim();
        }

        // 分割键值对
        String[] pairs = jsonString.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", ""); // 去掉引号
                String value = keyValue[1].trim();

                // 判断值的类型
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    // 字符串
                    jsonMap.put(key, value.substring(1, value.length() - 1)); // 去掉引号
                } else if (value.matches("^-?\\d+(\\.\\d+)?$")) {
                    // 数字
                    jsonMap.put(key, Double.parseDouble(value)); // 解析为数字
                } else {
                    // 假设为字符串
                    jsonMap.put(key, value);
                }
            }
        }
    }

    public Object get(String key) {
        return jsonMap.get(key);
    }

    public Double getDouble(String key) {
        Object value = jsonMap.get(key);
        return (value instanceof Number) ? ((Number) value).doubleValue() : null;
    }

    public String getString(String key) {
        Object value = jsonMap.get(key);
        return (value instanceof String) ? (String) value : null;
    }
}
