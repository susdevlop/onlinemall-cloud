package us.sushome.onlinemallcloud.omccommon.result;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户接口返回结果
 *
 * @param <T> 数据实体类型
 * @author noodle
 */
public class Result<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态短语
     */
    private String msg;

    /**
     * 数据实体
     */
    private T data;

    /**
     * 定义为private是为了在防止在controller中直接new
     *
     * @param data
     */
    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg codeMsg) {
        if (codeMsg == null)
            return;
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
        this.data = null;
    }

    private Result(CodeMsg codeMsg,T data) {
        if (codeMsg == null)
            return;
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
        this.data = data;
    }

    /**
     * 只有get没有set，是为了防止在controller使用set对结果修改，从而达到一个更好的封装效果
     *
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     * 业务处理成功返回结果，直接返回业务数据
     *
     * @param data
     * @return
     */

    public static <T> Result<T> resultByPrams(CodeMsg codeMsg, T data) {
        return new Result<T>(codeMsg,data);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * 业务处理信息
     *
     * @param serverError
     * @param <T>
     * @return
     */
    public static <T> Result<T> info(CodeMsg serverError) {
        return new Result<T>(serverError);
    }

    /**
     * 业务处理成功
     *
     * @param serverError
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(CodeMsg serverError) {
        return new Result<T>(serverError);
    }

    /**
     * 业务处理失败
     *
     * @param serverError
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMsg serverError) {
        return new Result<T>(serverError);
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> Result<Map<String, Object>> list(IPage<T> page) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", page.getTotal());//总条数
        List<T> list = page.getRecords();
        data.put("rows", list);
        data.put("pages", page.getPages());//总页数
        data.put("page", page.getCurrent());//当前页
        data.put("pageSize", page.getSize());//每页显示条数
        return new Result<>(data);
    }
}
