// 登录
import {AxiosPost} from "@/utils";

export const LOGIN_URL = "/openApi/user/login"
export const REGISTER_URL = "/openApi/user/register"




//管理后台
export const USER_LIST_URL = "/openApi/user/getUserInfoList"
export const GET_MY_USER_INFO_URL = "/openApi/user/getMyUserInfo"
export const PERMISSION_LIST_URL = "/openApi/user/getPermissionsList"
export const GET_CURRENT_PERMISSION = "/openApi/user/getCurrentPermission"
export const GET_USER_PERMISSIONS_LIST_URL = "/openApi/user/getUserPermissionByUserId"
export const set_USER_PERMISSIONS_LIST_URL = "/openApi/user/setUserPermissionByUserId"
export const GET_QINIU_TOKEN_URL = "/openApi/user/getUploadToken"

export const GET_ALL_GOODS_LIST_URL = "/openApi/goods/getAllGoodInfoList"
export const GET_GOODS_LIST_URL = "/openApi/goods/getGoodInfoList"
export const GET_GOODS_INFO_BY_ID_URL = "/openApi/goods/getGoodInfoById"
export const GET_SECKILL_GOODS_LIST_URL = "/openApi/goods/getSeckillGoodList"
export const GET_ALL_SECKILL_GOODS_LIST_URL = "/openApi/goods/getAllSeckillGoodList"
export const GET_SECKILL_GOODS_INFO_BY_ID_URL = "/openApi/goods/getSeckillGoodInfoById"
export const ADD_GOOD = "/openApi/goods/addGood"
export const UPDATE_GOOD = "/openApi/goods/updateGoodInfo"
export const REMOVE_GOOD = "/openApi/goods/removeGood"
export const ADD_GOOD_SKU = "/openApi/goods/addGoodSku"
export const UPDATE_GOOD_SKU = "/openApi/goods/updateGoodSku"
export const REMOVE_GOOD_SKU = "/openApi/goods/removeGoodSku"
export const ADD_SECKILL_GOOD = "/openApi/goods/addSeckillGood"
export const UPDATE_SECKILL_GOOD = "/openApi/goods/updateSeckillGood"
export const REMOVE_SECKILL_GOOD = "/openApi/goods/removeSeckillGood"




export const NEW_ORDER = "/openApi/order/newOrder"
export const UPDATE_ORDER_INFO = "/openApi/order/updateOrderInfo"
export const GET_ALL_ORDER_LIST = "/openApi/order/getAllOrderList"
export const GET_MY_ORDER_LIST = "/openApi/order/getMyOrderList"
export const GET_ORDER_BY_ID = "/openApi/order/getOrderById"
export const GET_MY_ORDER_BY_ID = "/openApi/order/getMyOrderById"
export const GET_SECKILL_PATH = "/openApi/order/getSeckillPath"
export const NEW_SECKILL_ORDER = (path: string)=> `/openApi/order/${path}/newSeckillOrder`

export const INIT_SECKILL_STATUS = "/openApi/order/initSeckillStatus"
// export const GET_MQTT_TOKEN = "/openApi/order/getMqttToken"

export const REMOVE_ORDER_BY_ID = "/openApi/order/deleteOrder"

export const getMyUserInfo = async ()=>{
    const res = await AxiosPost(GET_MY_USER_INFO_URL,{})
    if(res.code === 0){
        window.localStorage.setItem('userInfo',JSON.stringify(res.data))
    }
}