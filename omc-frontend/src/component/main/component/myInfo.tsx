import React, {useEffect, useMemo, useState} from 'react'
import {AxiosPost, getUserInfo} from "@/utils";
import {
    Divider, List,
    message, Skeleton, Tabs, TabsProps,
    Image
} from 'antd';
import {GET_MY_ORDER_LIST} from "@/api";
import InfiniteScroll from "react-infinite-scroll-component";
import {EOrderStatus, GENERAL_ORDER, getStatusTextByNum, TOrderInfo} from "@/constants";

export type MyInfoProps = object;

const MyInfo: React.FC<MyInfoProps> = (props) =>{
    const [messageApi, contextHolder] = message.useMessage();
    void props;
    void messageApi;
    const [searchStatus, setSearchStatus] = useState<EOrderStatus | undefined>(undefined);
    const getMyUserInfo = async (page = -1,returnList = false) => {
        setLoading(true)
        let pageNum = page;
        if(page !== -1){
            setCurrentPage(page);
        }else{
            pageNum = currentPage;
        }
        const res = await AxiosPost(GET_MY_ORDER_LIST,{
            pageSize,
            pageNum,
            ...(searchStatus === undefined ? {} : {status: searchStatus})
        });
        setLoading(false)
        console.log('res:',res)
        if(res.code === 0 ){
            setTotal(res.data.total);
            const List = res.data.rows;
            if(returnList){
                return List
            }else{
                setOrderList(List);
            }
        }else{
            if(returnList){
                return []
            }
        }
    }

    useEffect(() => {
        (async () => {
            // await getMyUserInfo();
        })()
    }, [])
    const [currentTabKey, setCurrentTabKey] = useState('1');
    const pageSize = 5;
    const [orderList,setOrderList] = useState<TOrderInfo[]>([]);
    const [total,setTotal] = useState(0);
    const [currentPage,setCurrentPage] = useState(1);
    const getHasMore = useMemo(()=>{
        console.log('total:',total);
        console.log('orderList:',orderList.length);
        return total > orderList.length
    },[total,orderList.length,currentTabKey])
    const [loading, setLoading] = useState(false);
    const loadMoreData = async () => {
        if (loading) {
            return;
        }
        setLoading(true);
        const page = currentPage;
        let list = []
        try{
            list = await getMyUserInfo(page+1,true)
            setOrderList([...orderList,...list])
        }catch (e) {
            void e;
        }
        setLoading(false);
    };
    const renderListItem = (item: TOrderInfo,idx: number)=>{
        return <div key={idx} className={`mx-10px bg-white my-10px border-rd-10px box-border py-5px px-10px`}>
            <div className={`RowBox justify-between`}>
                <span>{item.type === GENERAL_ORDER ? '普通订单': '秒杀订单'}</span>
                <span className={`text-orange`}>{getStatusTextByNum(item['status'])}</span>
            </div>
            <div className={`RowBox`}>
                <Image src={item['orderDetails'][0]['goodInfo']['skuList'][0].media} preview={false} width="80px" height="80px"/>
                <div className={`h-80px RowBox box-border px-5px py-5px text-left`} style={{width: 'calc(100% - 80px)'}}>
                    <div className={`ColumnBox w-75%`}>
                        <div className={`font-400 nowrap-text`}>
                            {item['orderDetails'][0]['goodName']}
                        </div>
                        <div className={`text-#919191 font-400`}>
                            {item['orderDetails'][0]['goodInfo']['skuList'][0]['name']}
                        </div>
                    </div>
                    <div className={`ColumnBox w-25% text-right`}>
                        <div className={`font-400 nowrap-text`}>
                            ¥{item['orderDetails'][0]['uniPrice']}
                        </div>
                        <div className={`text-#919191 font-400`}>
                            x{item['orderDetails'][0]['count']}
                        </div>
                    </div>
                </div>
            </div>
            {
                item.status === EOrderStatus.REFUNDED ? <div className={`w-full RowBox`}>
                    <div className={`my-5px w-full bg-#ececec font-300 text-12px py-2px text-left px-10px`}>
                        <span className={`font-400`}>已退款</span> 极速退款成功 ¥{item['sum']}
                    </div>
                </div> : item.status === EOrderStatus.BARTER ? <div className={`w-full RowBox`}>
                    <div className={`my-5px w-full bg-#ececec font-300 text-12px py-2px text-left px-10px`}>
                        <span className={`font-400`}>换货成功</span>
                    </div>
                </div> : null
            }
            <div className={`w-full RowBox justify-end text-12px items-center h-25px`}>
                <div className={`font-300`}>实付款</div>
                <div className={`font-400 ml-10px text-15px`}>¥{item['sum'] || 0}</div>
            </div>
        </div>
    }
    const items: TabsProps['items'] = [
        {key: '1', label: '所有',},
        {key: '2', label: '待付款',},
        {key: '3',label: '待发货',},
        {key: '4',label: '退款/售后',},
        // {key: '5',label: '待评价',},
    ].map((item,idx)=>{
        return {
            ...item,
            children: <div key={idx} className={`w-full overflow-x-hidden`}>
                <div className="ColumnBox bg-#ececec overflow-x-hidden overflow-y-scroll"
                     style={{height: '60vh',width: 'calc(100% + 15px)'}}
                     id={`scrollableDiv${idx}`} key={idx}>
                    <InfiniteScroll
                        dataLength={orderList.length}
                        next={loadMoreData}
                        hasMore={getHasMore}
                        loader={<Skeleton avatar paragraph={{rows: 1}} active/>}
                        endMessage={<Divider plain>It is all, nothing more 🤐</Divider>}
                        scrollableTarget={`scrollableDiv${idx}`}
                        style={{overflowX: 'hidden'}}
                    >
                        <List
                            grid={{gutter: 5, column: 1}}
                            dataSource={orderList}
                            renderItem={(item, index) => renderListItem(item, index)}
                        />
                    </InfiniteScroll>
                </div>
            </div>
        }
    });
    const onChangeTab = async (key: string) => {
        setCurrentTabKey(key);
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
    useEffect(()=>{
        (async ()=>{
            await getMyUserInfo(1)
        })()
    },[searchStatus])
    return (
        <>
            {contextHolder}
            <div className={`bg-white mx-15px border-rd-5px py-10px px-10px mt-10px text-black`}>
                <div className={`w-full RowBox justify-between`}>
                    <div className={`RowBox`}>
                        <div
                            className={`w-60px h-60px bg-pink border-rd-50% ColumnBox justify-center items-center text-12px text-white`}>无头像
                        </div>
                        <div className={`w-50% h-60px ColumnBox text-left justify-between pl-10px`}>
                            <div className={`text-12px mt-10px`}>
                                <span className={`text-16px`}>{getUserInfo('userName')}</span>
                            </div>
                            <div>
                                <span className={`text-12px text-#727272FF`}>email:{getUserInfo('email')}</span>
                            </div>
                        </div>
                    </div>
                    <div className={`w-30% ColumnBox text-12px items-end pt-10px`}>
                        <span>余额：<span className={`text-16px`}>¥{getUserInfo('balance')}</span></span>
                    </div>
                </div>
            </div>

            <div className={`h-80% bg-white mx-15px border-rd-5px py-10px px-10px mt-10px`}>
                <Tabs activeKey={currentTabKey} items={items} onChange={onChangeTab}/>
            </div>

        </>
    )
}

export default MyInfo;