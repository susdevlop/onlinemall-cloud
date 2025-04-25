package us.sushome.onlinemallcloud.omccommon.constants;

public class OrderConstants {
    public static final Integer GENERAL_ORDER = 0;
    public static final Integer SECKILL_ORDER = 1;

    public static final String UN_CREATE_ORDER = "UN_CREATE_ORDER";

    public static final Integer NEW_UNPAID = 0;//新建未支付
    public static final Integer PAID = 1;//已支付
    public static final Integer UN_SHIP = 2;//未发货
    public static final Integer SHIPPED = 3;//已发货
    public static final Integer REFUNDED = 4;//已退款
    public static final Integer DONE = 5;//已完成
    public static final Integer BARTER = 6;//已换货
    public static final Integer BARTERING = 7;//换货中
    public static final Integer ALL_AFTERSALE = 8;//所有售后
    public static final Integer UN_SHIP_AFTERSALE = 20;//未发货售后中
    public static final Integer UN_SHIP_AFTERSALE_REFUNDED = 24;//未发货售后退款中
    public static final Integer SHIPPED_AFTERSALE = 30;//已发货售后中
    public static final Integer SHIPPED_AFTERSALE_REFUNDED = 34;//已发货售后退款中
    public static final Integer SHIPPED_AFTERSALE_BARTERING = 37;//已发货售后换货中
    public static final Integer DONE_AFTERSALE = 40;//已完成售后
    public static final Integer DONE_AFTERSALE_REFUNDED = 44;//已完成售后退款
    public static final Integer DONE_AFTERSALE_BARTERING = 47;//已完成售后换货
    public static final Integer CANCELED =-1;//已关闭


    public static final Integer ALIPAY = 1;
    public static final Integer WECHAT = 2;
    public static final Integer BALANCE = 3;

    public static final Integer SECKILL_OPENING = 1;
    public static final Integer SECKILL_OPEN_EVERYDAY = 2;
    public static final Integer SECKILL_CLOSING = 0;

}
