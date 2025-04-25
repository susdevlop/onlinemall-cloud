package us.sushome.onlinemallcloud.omccommon.result;

import java.io.Serializable;

/**
 * 响应结果状态码
 *
 * @author noodle
 */
public class CodeMsg implements Serializable {

    private int code;
    private String msg;

    /**
     * 通用异常
     */
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
    public static CodeMsg VERITF_FAIL = new CodeMsg(500103, "校验失败，请重新输入表达式结果或刷新校验码重新输入");
    public static CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500104, "访问太频繁！");

    /**
     * 用户模块 5002XX
     */
    public static CodeMsg TOKEN_ERROR = new CodeMsg(500210, "token不存在或者已经失效，请返回登录！");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg USER_EXIST = new CodeMsg(500216, "用户已经存在，无需重复注册");
    public static CodeMsg REGISTER_SUCCESS = new CodeMsg(500217, "注册成功");
    public static CodeMsg REGISTER_FAIL = new CodeMsg(500218, "注册异常");
    public static CodeMsg FILL_REGISTER_INFO = new CodeMsg(500219, "请填写注册信息");
    public static CodeMsg WAIT_REGISTER_DONE = new CodeMsg(500220, "等待注册完成");
    public static CodeMsg USER_PASSWORD_EMPTY = new CodeMsg(500221, "手机号或密码不能为空");
    public static CodeMsg USER_NOT_FOUND = new CodeMsg(500222, "用户不存在");
    public static CodeMsg PRAMS_ERROR = new CodeMsg(500223, "参数错误");

    //登录模块 5002XX

    //商品模块 5003XX
    public static CodeMsg GOODS_STATUS_ERROR = new CodeMsg(500300, "商品状态异常");
    public static CodeMsg GOODS_STOCK_NOT_ENOUGH = new CodeMsg(500301, "商品库存不足");
    public static CodeMsg GOODS_NOT_EXIST = new CodeMsg(500302, "商品不存在");

    //订单模块 5004XX
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");
    public static CodeMsg ORDER_BALANCE_NOT_ENOUGH = new CodeMsg(500401, "余额不足");
    public static CodeMsg ORDER_PAYWAY_UNKNOW = new CodeMsg(500402, "未知支付方式");
    public static CodeMsg ORDER_PAY_AFTER_NEW_ORDER = new CodeMsg(500402, "等待订单创建后请求订单支付");
    public static CodeMsg ORDER_PAYWAY_NOT_OPEN = new CodeMsg(500403, "支付方式未开放");
    public static CodeMsg ORDER_ILLEGALITY_ORDER = new CodeMsg(500403, "非法订单");
    public static CodeMsg ORDER_PAY_SUCCESS = new CodeMsg(0, "支付成功");
    public static CodeMsg ORDER_UPDATE_FAILED = new CodeMsg(500404, "订单更新失败");
    public static CodeMsg ORDER_CREATE_FAILED = new CodeMsg(500405, "订单创建失败");
    public static CodeMsg ORDER_PHONE_IS_NULL = new CodeMsg(500405, "收货电话为空");
    public static CodeMsg ORDER_ADDRESS_IS_NULL = new CodeMsg(500405, "收货地址为空");
    public static CodeMsg ORDER_CONSIGNEE_IS_NULL = new CodeMsg(500405, "收货人为空");
    public static CodeMsg ORDER_TOO_MANY = new CodeMsg(500405, "当前商品下单人数过多，请重试");

    /**
     * 秒杀模块 5005XX
     */
    public static CodeMsg SECKILL_OVER = new CodeMsg(500500, "商品已经抢购完毕");
    public static CodeMsg REPEATE_SECKILL = new CodeMsg(500501, "不能重复抢购");
    public static CodeMsg SECKILL_FAIL = new CodeMsg(500502, "抢购失败");
    public static CodeMsg SECKILL_PARM_ILLEGAL = new CodeMsg(500503, "抢购请求参数异常：%s");
    public static CodeMsg SECKILL_NOT_START = new CodeMsg(500504, "商品已经抢购完毕");
    public static CodeMsg SECKILL_NOT_OPEN = new CodeMsg(500504, "商品未开启秒杀");


    /**
     * 构造器定义为private是为了防止controller直接new
     *
     * @param code
     * @param msg
     */
    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 动态地填充msg字段
     *
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);// 将arg格式化到msg中，组合成一个message
        return new CodeMsg(code, message);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

