export const ERROR_RESULT_CODE = 404
export const GENERAL_ORDER = 0;
export const SECKILL_ORDER = 1;

export enum EOrderStatus {
    NEW_UNPAID = 0,             // 新建未支付
    PAID = 1,                   // 已支付
    UN_SHIP = 2,                // 未发货
    SHIPPED = 3,                // 已发货
    REFUNDED = 4,               // 已退款
    DONE = 5,                   // 已完成
    BARTER = 6,                 // 已换货
    BARTERING = 7,              // 换货中
    ALL_AFTERSALE = 8,          // 所有售后
    UN_SHIP_AFTERSALE = 20,     // 未发货售后中
    UN_SHIP_AFTERSALE_REFUNDED = 24,  // 未发货售后退款中
    SHIPPED_AFTERSALE = 30,     // 已发货售后中
    SHIPPED_AFTERSALE_REFUNDED = 34,  // 已发货售后退款中
    SHIPPED_AFTERSALE_BARTERING = 37, // 已发货售后换货中
    DONE_AFTERSALE = 40,        // 已完成售后
    DONE_AFTERSALE_REFUNDED = 44,     // 已完成售后退款
    DONE_AFTERSALE_BARTERING = 47,    // 已完成售后换货
    CANCELED = -1               // 已关闭
}

//1 支付宝 2微信支付 3余额支付
export const getPayWayByNum = (num: number)=>{
    switch (num){
        case 1:
            return '支付宝';
        case 2:
            return '微信支付';
        case 3:
            return '余额支付';
        default:
            return '未知';
    }
}

export const getStatusTextByNum = (num: EOrderStatus): string => {
    switch (num) {
        case EOrderStatus.NEW_UNPAID:
            return '待付款';
        case EOrderStatus.PAID:
            return '待发货';
        case EOrderStatus.UN_SHIP:
            return '未发货';
        case EOrderStatus.SHIPPED:
            return '已发货';
        case EOrderStatus.REFUNDED:
            return '已退款';
        case EOrderStatus.DONE:
            return '已完成';
        case EOrderStatus.BARTER:
            return '已换货';
        case EOrderStatus.BARTERING:
            return '换货中';
        case EOrderStatus.ALL_AFTERSALE:
            return '所有售后';
        case EOrderStatus.UN_SHIP_AFTERSALE:
            return '未发货售后中';
        case EOrderStatus.UN_SHIP_AFTERSALE_REFUNDED:
            return '未发货售后退款中';
        case EOrderStatus.SHIPPED_AFTERSALE:
            return '已发货售后中';
        case EOrderStatus.SHIPPED_AFTERSALE_REFUNDED:
            return '已发货售后退款中';
        case EOrderStatus.SHIPPED_AFTERSALE_BARTERING:
            return '已发货售后换货中';
        case EOrderStatus.DONE_AFTERSALE:
            return '已完成售后';
        case EOrderStatus.DONE_AFTERSALE_REFUNDED:
            return '已完成售后退款';
        case EOrderStatus.DONE_AFTERSALE_BARTERING:
            return '已完成售后换货';
        case EOrderStatus.CANCELED:
            return '交易关闭';
        default:
            return '未知状态';
    }
}
// goodInfo
export type TGoodsItem = {
    goodsId: string,
    name: string,
    goodsType: number,
    mediaList: string,
    mediaListArr: string[],
    content: string,
    skuList: TSkuItem[],
    addTime: string,
    updateTime: string,
    shipAddress: string,
}
export type TSkuItem = {
    "id": string,
    "goodsId": string,
    "name": string,
    "price": number,
    "inventory": number,
    "media": string,
    "addTime": string,
    "updateTime": string
}


export type TSeckillGoodsItem = {
    id: string,
    goodId: string,
    goodsType: number,
    goodSkuId: string,
    name: string,
    price: number,
    stock: number,
    startTime: string,
    endTime: string,
    addTime: string,
    updateTime: string,
    goodInfo: TGoodsItem,
    status: number
}

export type TOrderDetail = {
    "orderDetailId": null,
    "orderId": string,
    "goodName": string,
    "skuId": string,
    "count": number,
    "goodInfo": TGoodsItem
    "uniPrice": number,
    "sumPrice": number,
    "updateTime": string,
    "addTime": string
}
export type TOrderInfo = {
    "orderId": string,
    "status": number,
    "sum": number,
    "userId": string,
    "consignee": string,
    "phone": string,
    "address": string,
    "date": string,
    "updateTime": string,
    "addTime": string,
    "couponName": string,
    "couponId": string,
    "type": number,
    "payDate": string,
    "payWay": number,
    "orderDetails": TOrderDetail[],
    "orderSn": string,
    "expressName": string,
    "remark": string
}