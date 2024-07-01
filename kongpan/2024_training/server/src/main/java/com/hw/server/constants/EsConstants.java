package com.hw.server.constants;

/**
 * @author mrk
 * @create 2024-06-08-22:33
 */
public class EsConstants {

    /**
     * 索引库字段映射
     */
    public static final String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"hostname\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"file\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"log\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    /**
     * 索引库名称
     */
    public static final String INDEX_NAME = "logs";
}
