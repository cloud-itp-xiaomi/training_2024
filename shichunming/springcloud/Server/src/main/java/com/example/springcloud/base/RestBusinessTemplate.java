package com.example.springcloud.base;


import com.example.springcloud.base.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * controller接口模版
 *
 */
@Slf4j
public class RestBusinessTemplate {

    public RestBusinessTemplate() {}

    public static <T> Response<T> execute(Callback<T> callback) {
        Response<T> response = new Response<>();
        try {

            response.setCode(ErrorCode.SUCCESS.getCode());
            T object = callback.execute();
            if (object != null) {
                response.setData(object);
            }
            response.setSuccess(true);
            response.setMsg("成功");

        } catch (MyException be) {

            String errorMsg;
            if (StringUtils.isNotBlank(be.getMsg())) {
                errorMsg = be.getMsg();
            } else {
                errorMsg = be.getMessage();
            }

            log.error("catch biz exception: {}", errorMsg, be);
            response.setCode(be.getCode() == null ? ErrorCode.SYSTEM_ERROR.getCode() : be.getCode());
            response.setMsg(errorMsg);
            response.setSuccess(false);

        } catch (Exception e) {
            log.error("catch unknown exception", e);
            response.setCode(ErrorCode.SYSTEM_ERROR.getCode());
            response.setMsg(ErrorCode.SYSTEM_ERROR.getMsg());
            response.setSuccess(false);
        }
        return response;
    }

    public static Response<Void> execute(VoidCallback callback) {
        Response<Void> response = new Response<>();
        try {

            callback.execute();
            response.setCode(ErrorCode.SUCCESS.getCode());
            response.setMsg(ErrorCode.SUCCESS.getMsg());
            response.setSuccess(true);

        } catch (MyException be) {

            String errorMsg;
            if (StringUtils.isNotBlank(be.getMsg())) {
                errorMsg = be.getMsg();
            } else {
                errorMsg = be.getMessage();
            }

            log.error("catch biz exception: {}", errorMsg);
            response.setCode(be.getCode() == null ? ErrorCode.SYSTEM_ERROR.getCode() : be.getCode());
            response.setMsg(errorMsg);
            response.setSuccess(false);

        } catch (Exception e) {
            log.error("catch unknown exception: {}", e.getMessage(), e);
            response.setCode(ErrorCode.SYSTEM_ERROR.getCode());
            response.setMsg(ErrorCode.SYSTEM_ERROR.getMsg());
            response.setSuccess(false);
        }
        return response;
    }

    /**
     * 无返回值回调
     */
    public interface VoidCallback {
        void execute();
    }

    /**
     * 带返回值回调
     *
     * @param <T> 回调范型
     */
    public interface Callback<T> {
        T execute();
    }
}
