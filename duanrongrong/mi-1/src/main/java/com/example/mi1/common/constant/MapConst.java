package com.example.mi1.common.constant;

import lombok.Getter;

/**
 * @author txh
 * @date 2023/10/21 21:47
 * @description
 */
public class MapConst {
    public enum MapSearchType {
        RECTANGLE("rectangle"),
        POINT("point"),
        POLYGON("polygon");

        @Getter
        private final String type;

        MapSearchType(String type) {
            this.type = type;
        }

    }
    public enum DataSearchType {
        DRAW("draw"),
        COORDINATE("coordinate"),
        REGION("region");

        @Getter
        private final String type;

        DataSearchType(String type) {
            this.type = type;
        }
    }

}
