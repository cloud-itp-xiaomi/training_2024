package com.example.demo01.bean;

import lombok.*;

import java.io.Serializable;

@ToString
@NoArgsConstructor
@AllArgsConstructor
//使用@Data还会自动生成equals和hashCode方法，这在某些情况下可能并不是你所期望的，
// 尤其是当你的类作为集合的元素或者需要放入HashSet、HashMap等基于哈希的集合中时，自动生成的equals和hashCode可能不符合业务逻辑。
// 在这种情况下，你可能需要更细致地控制这些方法的行为，此时单独使用@Getter和@Setter搭配@ToString或其他注解会更加灵活。
public class R<T> implements Serializable {

    //用于序列化过程中的版本控制字段，序列化和反序列化
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int code;//0成功，1失败

    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, 0, "ok");
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, 0, "ok");
    }

    public static <T> R<T> failed() {
        return restResult(null, 1, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, 1, msg);
    }

    //统一返回结果
    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}