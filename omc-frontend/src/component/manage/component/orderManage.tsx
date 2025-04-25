import React, {useEffect, useState} from 'react'
import {
    GET_ALL_ORDER_LIST, REMOVE_ORDER_BY_ID, UPDATE_ORDER_INFO
} from "@/api";
import 'md-editor-rt/lib/style.css';
import {AxiosPost} from "@/utils";
import {
    Pagination,
    Input,
    Space,
    List, message, Popconfirm, Select, Modal, Form

} from 'antd';
import Search from "antd/es/input/Search";
import {SearchOutlined} from "@ant-design/icons";
import {EOrderStatus, GENERAL_ORDER, getPayWayByNum, getStatusTextByNum, SECKILL_ORDER, TOrderInfo} from "@/constants";

export type orderManageProps = object;

type FieldType = {
    expressName?: string;
    orderSn?: string;
};

const OrderManage: React.FC<orderManageProps> = (props) =>{
    const [messageApi, contextHolder] = message.useMessage();
    void props;
    const [loading, setLoading] = useState<boolean>(false)
    //#region orderManage
    const [pageSize,setPageSize] = useState(10);
    const [currentPage,setCurrentPage] = useState(1);
    const [total,setTotal] = useState(0);
    const [searchStatus, setSearchStatus] = useState<EOrderStatus | undefined>(undefined);
    const onChangeStatusSelector = async (key: string) => {
        let status: undefined | EOrderStatus = undefined;
        if(key === '1'){//所有
            status = undefined;
        }else if(key === '2'){//待付款
            status = EOrderStatus.NEW_UNPAID
        }else if(key === '3'){//待发货
            status = EOrderStatus.UN_SHIP
        }else if(key === '4'){
            status = EOrderStatus.ALL_AFTERSALE
        }
        setSearchStatus(status)
    };
    const [searchGoodName, setSearchGoodName] = useState('');
    const [searchType, setSearchType] = useState<number | undefined>(undefined);
    const onChangeTypeSelector = async (key: string) => {
        let type: undefined | EOrderStatus = undefined;
        if(key === '1'){//所有
            type = undefined;
        }else if(key === '2'){//普通订单
            type = GENERAL_ORDER;
        }else if(key === '3'){//秒杀订单
            type = SECKILL_ORDER;
        }
        setSearchType(type)
    };
    const [searchOrderId, setSearchOrderId] = useState('');
    const [searchUserId, setSearchUserId] = useState('');
    const [searchPhone, setSearchPhone] = useState('');
    const [searchPayDate, setSearchPayDate] = useState('');

    const [orderList,setOrderList] = useState<TOrderInfo[]>([]);
    const onHandleGetAllOrderList = async (page = -1)=>{
        setLoading(true)
        let pageNum = page;
        if(page !== -1){
            setCurrentPage(page);
        }else{
            pageNum = currentPage;
        }
        const res = await AxiosPost(GET_ALL_ORDER_LIST,{
            pageSize,
            pageNum,
            orderId: searchOrderId || undefined,
            userId: searchUserId || undefined,
            ...(searchStatus === undefined ? {} : {status: searchStatus}),
            type: searchType === undefined ? undefined : searchType,
            phone: searchPhone || undefined,
            payDate: searchPayDate || undefined,
            goodName: searchGoodName || undefined,
        })
        setLoading(false)
        console.log('res:',res)
        if(res.code === 0 ){
            setTotal(res.data.total);
            setOrderList(res.data.rows);
        }
    }


    const onHandleSearchOrderList = async ()=>{
        setPageSize(10);
        setCurrentPage(1);
        await onHandleGetAllOrderList();
    }
    const [currentSelectOrder, setCurrentSelectOrder] = useState<undefined | TOrderInfo>(undefined);

    const handleCancel = () => {
        setCurrentSelectOrder(undefined);
    };
    const onRenderOrderManageList = (item: TOrderInfo, index: number)=>{
        const od = item.orderDetails;
        return <List.Item className="bg-white !p-0" key={index}>
            <div className="w-full h-60px RowBox align-items-center px-10px justify-between text-left box-border">
                <div className="RowBox" style={{width: 'calc(100%)'}}>
                    <div className={`ColumnBox w-20%`}>
                        <div
                            className={`nowrap-text text-#ff0000`}>{item['type'] === GENERAL_ORDER ? '普通订单' : '秒杀订单'}</div>
                        <div className={`nowrap-text`}>订单id: {item.orderId}</div>
                        <div className={`nowrap-text`}>用户id: {item.userId}</div>
                    </div>
                    <div className={`ColumnBox w-40% px-5px px-5px box-border`}
                         style={{borderLeft: '1px solid #ececec', borderRight: '1px solid #ececec'}}>
                        <div className={`RowBox w-full flex-wrap`}>
                            <div className={`ColumnBox w-70px`}>
                                订单商品:
                            </div>
                            <div className={`flex-1 nowrap-text`}>
                                {od[0].goodName}
                            </div>
                        </div>
                        <div className={`text-12px text-#6C6C6C text-right w-full`}>
                            {od[0]['goodInfo']['skuList'][0]['name']} x {od[0]['count']}
                            <span className={`text-red ml-5px`}>总价: ¥{item['sum']}</span>
                        </div>
                    </div>
                    {/*<div className={`ColumnBox ml-5px w-25% px-5px`} style={{borderRight: '1px solid #ececec'}}>*/}
                    {/*    <div className={`nowrap-text`}>收货人: {item['consignee']} <span>{item['phone']}</span></div>*/}
                    {/*    <div className={`lh-12px text-12px mt-5px`}>收货地址: {item['address']}</div>*/}
                    {/*</div>*/}
                    <div className={`ColumnBox w-10% px-5px bg-#AFE6FFFF box-border`} style={{borderRight: '1px solid #ececec'}}>
                        <div>备注: {item['remark']}</div>
                    </div>
                    <div className={`ColumnBox w-7% px-5px bg-#FFA6C0FF text-center text-12px box-border  justify-center`}
                         style={{borderRight: '1px solid #ececec'}}>
                        <div>{getStatusTextByNum(item['status'])}</div>
                    </div>
                    <div className={`ColumnBox w-10% px-5px text-center text-14px box-border  justify-center`}
                         style={{borderRight: '1px solid #ececec'}}>
                        <div>{getPayWayByNum(item['payWay'])}</div>
                    </div>
                    <div className="lh-50px  w-13% RowBox justify-around box-border">
                        <div className={`text-blue cursor-pointer`} onClick={(e) => {
                            e.stopPropagation();
                            setCurrentSelectOrder(item)
                        }}>发货
                        </div>
                        <div className={`text-blue cursor-pointer`} onClick={(e) => {
                            e.stopPropagation();
                            setCurrentSelectOrder(item)
                        }}>编辑
                        </div>
                        <Popconfirm
                            title="提示"
                            description="您确定要删除该商品吗"
                            onConfirm={() => {
                                onHandleDeleteOrder(item)
                            }}
                            okText="Yes"
                            cancelText="No"
                        >
                            <div className={`text-red cursor-pointer`}>删除</div>
                        </Popconfirm>
                    </div>
                </div>

            </div>
        </List.Item>
    }
    const [shipForm] = Form.useForm();
    const onHandleShip = async (values: FieldType) => {
        console.log('values', values)
        const res = await AxiosPost(UPDATE_ORDER_INFO,{
            orderId: currentSelectOrder?.orderId,
            ...values,
        })
        if(res.code === 0){
            messageApi.success('发货成功');
            setCurrentSelectOrder(undefined);
            await onHandleSearchOrderList()
        }else {
            messageApi.error('发货失败');
        }
    }

    const onHandleDeleteOrder = async (item: TOrderInfo) => {
        const res = await AxiosPost(REMOVE_ORDER_BY_ID,{id: item.orderId})
        if(res.code === 0){
            messageApi.success('删除成功');
            await onHandleSearchOrderList()
        }
    }
    //#endregion


    useEffect(() => {
        (async () => {
            await onHandleGetAllOrderList();
        })()
    }, [])

    return (
        <>
            {contextHolder}
            <div style={{height: 'calc(100vh - 100px)'}} className="ColumnBox justify-between" onClick={() => {

            }}>
                <div style={{height: 'calc(100% - 80px)'}} className="ColumnBox overflow-hidden">
                    <div className="h-40px w-full bg-white ColumnBox justify-center items-center">
                        <Space.Compact size="middle" className="w-96%">
                            <Input addonBefore={<SearchOutlined/>} placeholder="输入搜索的订单id" allowClear
                                   onChange={(e) => setSearchOrderId(e.target.value)}/>
                            <Input placeholder="输入搜索的用户id" allowClear
                                   onChange={(e) => setSearchUserId(e.target.value)}/>
                            <Input placeholder="输入搜索的收货手机号" allowClear
                                   onChange={(e) => setSearchPhone(e.target.value)}/>
                            <Select
                                defaultValue={'1'}
                                style={{ width: '180px' }}
                                onChange={onChangeTypeSelector}
                                options={[
                                    { value: '1', label: '所有订单' },
                                    { value: '2', label: '普通订单' },
                                    { value: '3', label: '秒杀订单' },
                                ]}
                            />
                        </Space.Compact>
                    </div>
                    <div className="h-40px w-full bg-white ColumnBox justify-center items-center">
                        <Space.Compact size="middle" className="w-96%">
                            <Select
                                defaultValue={'1'}
                                style={{ width: '180px' }}
                                onChange={onChangeStatusSelector}
                                options={[
                                    { value: '1', label: '所有订单' },
                                    { value: '2', label: '待付款' },
                                    { value: '3', label: '待发货' },
                                    { value: '4', label: '退款/售后'},
                                ]}
                            />
                            <Input  placeholder="输入搜索的支付日期" allowClear
                                   onChange={(e) => setSearchPayDate(e.target.value)}/>
                            <Search placeholder="输入搜索的商品名" enterButton="Search" size="middle"
                                    onInput={(e: any) => {
                                        setSearchGoodName(e.target.value)
                                    }} loading={loading} onSearch={onHandleSearchOrderList}/>
                        </Space.Compact>
                    </div>
                    <div style={{height: 'calc(100% - 40px)', width: 'calc(100% + 20px)'}}
                         className={`overflow-x-hidden overflow-y-scroll`}>
                        <List loading={loading}
                              itemLayout="horizontal"
                              dataSource={orderList}
                              renderItem={onRenderOrderManageList}
                        />
                    </div>
                    <Modal title="详细" open={currentSelectOrder && 'orderId' in currentSelectOrder}
                           modalRender={(dom)=>(
                                <Form onFinish={(values) => onHandleShip(values)} form={shipForm}>
                                    {dom}
                                </Form>
                           )}
                           okButtonProps={{ autoFocus: true, htmlType: 'submit' }} onCancel={handleCancel}
                    >
                        <Form.Item<FieldType>
                            label="物流公司"
                            name="expressName"
                            rules={[{ required: true, message: 'Please input expressName!' }]}
                        >
                            <Input />
                        </Form.Item>
                        <Form.Item<FieldType>
                            label="物流单号"
                            name="orderSn"
                            rules={[{ required: true, message: 'Please input orderSn!' }]}
                        >
                            <Input />
                        </Form.Item>
                    </Modal>
                </div>
                <div className="w-full h-80px ColumnBox justify-center">
                    <Pagination defaultCurrent={1} total={total} pageSize={pageSize} showSizeChanger
                                onChange={async (c) => {
                                    await onHandleGetAllOrderList(c);
                                }}
                                onShowSizeChange={async (_c, s) => {
                                    setPageSize(s)
                                    setCurrentPage(s)
                                    await onHandleGetAllOrderList();
                                }}/>
                </div>
            </div>
        </>
    )
}

export default OrderManage;