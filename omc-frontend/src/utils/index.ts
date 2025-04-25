import type { AxiosError, AxiosRequestConfig } from 'axios'
import axios from 'axios'
import {ERROR_RESULT_CODE} from "@/constants";

export const REQUEST_TOKEN_KEY = 'Access-Token'
export type TPostData = {
    [key: string]: string | number | object | boolean | undefined
}


console.log('import.meta.env.VITE_REQUEST_URL:',import.meta.env.VITE_REQUEST_URL)
let baseURL: string = import.meta.env.VITE_REQUEST_URL;
if(import.meta.env.MODE === 'development') {
    baseURL = import.meta.env.VITE_REQUEST_URL + 'mockApi'
}
// 创建 axios 实例
const request = axios.create({
    // API 请求的默认前缀
    baseURL,
    timeout: 5000*3, // 请求超时时间
})

export type RequestError = AxiosError<{
    message?: string
    result?: never
    errorMessage?: string
}>

// 异常拦截处理器
const errorHandler = (error: RequestError): Promise<any> => {
    if (error.response) {
        const { data = {}, status, statusText } = error.response
        // 403 无权限
        if (status === 403) {
            console.log(((data && data.message) || statusText))
            window.location.href = window.location.origin + '/login'
        }
        // 401 未登录/未授权
        if (status === 401) {
            window.location.href = window.location.origin + '/login'
            // 如果你需要直接跳转登录页面
            // location.replace(loginRoutePath)
        }
    }
    return Promise.reject(error)
}

// 请求拦截器
const requestHandler = (config: AxiosRequestConfig | any): AxiosRequestConfig | Promise<AxiosRequestConfig> => {
    const savedToken = window.localStorage.getItem('token')
    // 如果 token 存在
    // 让每个请求携带自定义 token, 请根据实际情况修改
    // if (savedToken)
    //   config.headers[REQUEST_TOKEN_KEY] = savedToken
    // console.log('config',config)
    console.log('config:',config)
    if(savedToken){
        config['headers']['Authorization'] = `Bearer${savedToken}`
    }
    return config
}

// Add a request interceptor
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
request.interceptors.request.use(requestHandler, errorHandler)

// 响应拦截器
const responseHandler = (response: { data: any }) => {
    console.log('response:',response)
    if(response.data.resultcode === ERROR_RESULT_CODE){
        console.log("error response:",response.data);
        return {}
    }else{
        return response.data
    }
}

// Add a response interceptor
request.interceptors.response.use(responseHandler, errorHandler)

export const AxiosPost = async (url: string,prams: TPostData,config={}):Promise<any>=>{
    return request.post(url,{
        ...prams
    },config)
}
export const AxiosGet = async (url: string, params?: Record<string, any>): Promise<any> => {
    let queryString = '';
    if (params) {
        queryString = '?' + new URLSearchParams(params).toString();
    }
    return request.get(`${url}${queryString}`);
};


export const getUserInfo = (key: string) => {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    return key in userInfo ? userInfo[key] : ''
}


export const addDate0 = (time: number | string) => {
    if (time.toString().length == 1) {
        time = "0" + time.toString();
    }
    return time;
}