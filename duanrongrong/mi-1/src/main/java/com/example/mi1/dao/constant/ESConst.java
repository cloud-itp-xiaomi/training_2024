package com.example.mi1.dao.constant;

/**
 * @author txh
 * @date 2024/5/7 21:47
 * @description
 */
public class ESConst {

    public static final int ES_MAX_RESULT_WINDOW = 10000; // Es 目前支持的最大的 skip 值；当 from + size > max_result_window 时，es 将返回错误

    public enum Index {
        // data
        LOG("mi_log"),
        ;
        private final String indexName;

        Index(String indexName) {
            this.indexName = indexName;
        }

        public String value() {
            return indexName;
        }
    }

}
