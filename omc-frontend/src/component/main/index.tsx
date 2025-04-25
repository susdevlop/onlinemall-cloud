import React, { useEffect, useMemo, useState} from 'react'
import {useLocation, useNavigate, useSearchParams} from "react-router-dom";
import { MdPreview } from 'md-editor-rt';
export type MainProps = object
import InfiniteScroll from 'react-infinite-scroll-component';
import {Divider, Input, List, Skeleton, Space, Image, message, Col, Row, Button, Carousel, Radio,Spin} from 'antd';

import {addDate0, AxiosGet, AxiosPost, getUserInfo} from "@/utils";
import {
    GET_GOODS_LIST_URL,
    GET_SECKILL_GOODS_LIST_URL,
    GET_GOODS_INFO_BY_ID_URL,
    GET_SECKILL_GOODS_INFO_BY_ID_URL,
    NEW_ORDER,NEW_SECKILL_ORDER,GET_SECKILL_PATH,
    getMyUserInfo,GET_MY_ORDER_BY_ID
} from "@/api";
import {
    AccountBookOutlined,
    AlipayCircleOutlined,
    CaretLeftOutlined,
    CloseCircleOutlined,
    RightOutlined,
    SearchOutlined, WechatOutlined
} from "@ant-design/icons";
import Search from "antd/es/input/Search";
import DefaultGoodsImage from '@/assets/defaultGoodsImage.png';
import TextArea from "antd/es/input/TextArea";
import {EOrderStatus, GENERAL_ORDER, SECKILL_ORDER, TGoodsItem, TSeckillGoodsItem, TSkuItem} from "@/constants";
import MyInfo from "@/component/main/component/myInfo.tsx";

const MainComponent: React.FC<MainProps> = () => {
    const [requesting, setRequesting] = useState<boolean>(false);
    const [messageApi, contextHolder] = message.useMessage();
    //#region 获取列表
    const [pageSize,setPageSize] = useState(20);
    const [currentPage,setCurrentPage] = useState(1);
    const [total,setTotal] = useState(0);
    const onHandleGetGoodList = async (page = -1,returnList = false)=>{
        setLoading(true)
        let pageNum = page;
        if(page !== -1){
            setCurrentPage(page);
        }else{
            pageNum = currentPage;
        }
        const res = await AxiosPost(GET_GOODS_LIST_URL,{
            goodsName: searchName,
            goodsType: searchType,
            pageSize,
            pageNum,
        })
        setLoading(false)
        console.log('res:',res)
        if(res.code === 0 ){
            setTotal(res.data.total);
            const List = res.data.rows.map((item:any)=>{
                return {
                    ...item,
                    mediaListArr: item.mediaList.split('||split||')
                }
            });
            if(returnList){
                return List
            }else{
                setGoodList(List);
            }
        }else{
            if(returnList){
                return []
            }
        }
    }
    const onHandleGetSeckillGoodList = async (page = -1,returnList = false)=>{
        setLoading(true)
        let pageNum = page;
        if(page !== -1){
            setCurrentPage(page);
        }else{
            pageNum = currentPage;
        }
        const res = await AxiosPost(GET_SECKILL_GOODS_LIST_URL,{
            goodsName: searchName,
            goodsType: searchType,
            pageSize,
            pageNum
        })
        setLoading(false)
        if(res.code === 0 ){
            setTotal(res.data.total);
            const List = res.data.rows.map((item:any)=>{
                return {
                    ...item,
                    goodInfo: {
                        ...item.goodInfo,
                        mediaListArr: item.goodInfo.mediaList.split('||split||')
                    }
                }
            })
            if(returnList){
                return List
            }else{
                setSeckillGoodList(List);
            }
        }else{
            if(returnList){
                return []
            }
        }
    }
    const [goodList,setGoodList] = useState<TGoodsItem[]>([]);
    const [currentSelectGoodItem,setCurrentSelectGoodItem] = useState<TGoodsItem | undefined>(undefined)
    const [selectedGoodSkuIndex,setSelectedGoodSkuIndex] = useState<number>(-1)
    const [selectedGoodCount,setSelectedGoodCount] = useState<number>(1)
    const [selectedPayway,setSelectedPayway] = useState<number>(1)
    const [orderRemark, setOrderRemark] = useState<string>('')
    const [currentSelectSeckillGoodItem,setCurrentSelectSeckillGoodItem] = useState<TSeckillGoodsItem | undefined>(undefined)
    const [seckillGoodList,setSeckillGoodList] = useState<TSeckillGoodsItem[]>([]);

    const [searchName, setSearchName] = useState('');
    const [searchType, setSearchType] = useState(undefined);
    const onHandleGetList = async ()=>{
        setCurrentPage(1);
        setPageSize(9);
        if([0,3,5].includes(showMode)){
            await onHandleGetGoodList()
        }else if([1,4,6].includes(showMode)){
            await onHandleGetSeckillGoodList()
        }
    }
    //#endregion
    const onHandleChangeTab = async (key:number) => {
        setTotal(0)
        setCurrentPage(1)
        setShowMode(key)
        if(key === 2){
            navigate(`${location.pathname}?page=my`, { replace: true });
        }else{
            navigate(`${location.pathname}`, { replace: true });
        }
    }
    const navigate = useNavigate()
    const location = useLocation();
    const [searchParams,setSearchParams] = useSearchParams();


    const [showMode,setShowMode] = useState<number>(0);//0 商城list 1 限时秒杀list 2我的 3 商城detail 4 限时秒杀detail 5 goodDetailConfirm 6 seckillDetailConfirm
    useEffect(()=>{
        if([0,1,3,4,5,6].includes(showMode)){
            onHandleGetList()
        }
    },[showMode])
    const getHasMore = useMemo(()=>{
        if([0,3,5].includes(showMode)){
            return total > goodList.length
        }else if([1,4,6].includes(showMode)){
            return total > seckillGoodList.length
        }else{
            return false
        }
    },[goodList,seckillGoodList,total,showMode])
    const [loading, setLoading] = useState(false);
    const loadMoreData = async () => {
        if (loading) {
            return;
        }
        setLoading(true);
        const page = currentPage;
        let list = []
        try{
            if([0,3,5].includes(showMode)){
                list = await onHandleGetGoodList(page+1,true)
                setGoodList([...goodList,...list])
            }else if([1,4,6].includes(showMode)){
                list = await onHandleGetSeckillGoodList(page+1,true)
                setSeckillGoodList([...seckillGoodList,...list])
            }
        }catch (e) {
            void e;
        }
        setLoading(false);
        // fetch('https://randomuser.me/api/?results=10&inc=name,gender,email,nat,picture&noinfo')
        //     .then((res) => res.json())
        //     .then((body) => {
        //         setData([...data, ...body.results]);
        //         setLoading(false);
        //     })
        //     .catch(() => {
        //         setLoading(false);
        //     });
    };

    const [currentTime, setCurrentTime] = useState<Date | null>(null)
    useEffect(()=>{
        const timer = setInterval(()=>{
            if([4,6].includes(showMode)){
                const d = new Date();
                setCurrentTime(d)
            }
        },1000);
        return ()=>{
            clearInterval(timer);
        }
    },[showMode])
    useEffect(() => {
        (async ()=>{
            // await AxiosPost(GET_MQTT_TOKEN,{});
            await onHandleGetList()
            console.log('searchParams:',searchParams)
            const id = searchParams.get('id')
            const seckill_id = searchParams.get('seckill_id')
            const page = searchParams.get('page')
            console.log('id:',id)
            console.log('seckill_id:',seckill_id)
            if(id){
                if(currentSelectGoodItem && currentSelectGoodItem.goodsId === id){
                    setShowMode(3)
                }else{
                    const res = await AxiosPost(GET_GOODS_INFO_BY_ID_URL,{id})
                    if(res.code === 0){
                        setCurrentSelectGoodItem({
                            ...res.data,
                            mediaListArr: res.data.mediaList.split('||split||')
                        })
                        setShowMode(3)
                    }else{
                        searchParams.delete('id'); // 删除 id 参数
                        setSearchParams(searchParams); // 更新 URL
                    }
                }
            }else if(seckill_id){
                if(currentSelectSeckillGoodItem && currentSelectSeckillGoodItem.id === seckill_id){
                    setShowMode(4)
                }else{
                    const res = await AxiosPost(GET_SECKILL_GOODS_INFO_BY_ID_URL,{
                        seckillId: seckill_id,
                    })
                    if(res.code === 0){
                        setCurrentSelectSeckillGoodItem({
                            ...res.data,
                            goodInfo: {
                                ...res.data.goodInfo,
                                mediaListArr: res.data.goodInfo.mediaList.split('||split||')
                            }
                        })
                        setShowMode(4)
                    }else{
                        searchParams.delete('seckill_id'); // 删除 id 参数
                        setSearchParams(searchParams); // 更新 URL
                    }
                }
            }if(page){
                setShowMode(2)
            }
        })()
    }, []);
    const onHandleCloseModel = () => {
        if([0,3,5].includes(showMode)){
            searchParams.delete('id'); // 删除 id 参数
            setShowMode(0)
        }else if([1,4,6].includes(showMode)){
            searchParams.delete('seckill_id'); // 删除 id 参数
            setShowMode(1)
        }
        setSelectedGoodCount(1)
        setSelectedGoodSkuIndex(-1)
        setSelectedPayway(1)
        setSearchParams(searchParams); // 更新 URL

    }
    const onClickTest = ()=>{
        console.log('test',getHasMore)
        console.log('goodList:',goodList)
        console.log('seckillGoodList:',seckillGoodList)
        console.log('total:,',total)
    }
    const renderListItem = (item: TGoodsItem | TSeckillGoodsItem,index:number)=>{
        let image_url = '';
        let price = '';
        if([0, 3, 5].includes(showMode)){
            const g = item as TGoodsItem;
            image_url = g.mediaListArr[0];
            price = String(g.skuList[0].price);
        }else if([1, 4, 6].includes(showMode)){
            const g = item as TSeckillGoodsItem
            image_url = g.goodInfo.mediaListArr[0]
            price = g.price.toString();
        }
        const click = ()=>{
            if([0].includes(showMode)){
                setCurrentSelectGoodItem(item as TGoodsItem)
                setShowMode(3)
                navigate(`${location.pathname}?id=${(item as TGoodsItem).goodsId}`, { replace: true });
            }else if([1].includes(showMode)){
                setCurrentSeckillPath("")
                setCurrentSelectSeckillGoodItem(item as TSeckillGoodsItem)
                setShowMode(4)
                navigate(`${location.pathname}?seckill_id=${(item as TSeckillGoodsItem).id}`, { replace: true });
            }
        }
        return <List.Item key={index} style={{marginBottom: '10px'}}>
            <div className={`w-full px-6px box-border`}>
                <div className={`w-full h-full bg-gray-5 py-10px box-border`}>
                    <div className={`w-full box-border px-10px cursor-pointer relative pt-full`} onClick={click}>
                        <div className={`absolute inset-0`}>
                            <Image preview={false} src={image_url} width={`100%`} height={`100%`} fallback={DefaultGoodsImage as any} />
                        </div>
                    </div>
                    <div className={`w-full box-border text-left px-10px h-45px ColumnBox`}>
                        <div className={`overflow-hidden cursor-pointer`} onClick={click}>{item['name']}</div>
                        <div className={`RowBox`}>
                            <div>
                                ¥<span className={`text-#ff0000 text-16px ml-5px`}>{price}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </List.Item>
    }

    const [id] = useState('preview-only');
    const [showSkuSelector, setShowSkuSelector] = useState(false)

    const [currentSeckillPath,setCurrentSeckillPath] = useState('')

    const renderModel = ()=>{
        //#region detail
        let price = '';
        let name = '';
        let content = ''
        let shipAddress = ''
        let skuLength = 1
        let skuList = [] as TSkuItem[]
        let mediaListArr = [] as string[]

        if([0, 3,5].includes(showMode)){
            const g = currentSelectGoodItem as TGoodsItem;
            // image_url = getImageUrl();
            name = g.name;
            price = String(g.skuList[0].price);
            content = g.content
            shipAddress = g.shipAddress
            skuLength = g.skuList.length
            skuList = g.skuList
            mediaListArr = g.mediaListArr
        }else if([1, 4,6].includes(showMode)){
            const g = currentSelectSeckillGoodItem as TSeckillGoodsItem
            price = g.price.toString();
            name = g.name;
            content = g.goodInfo.content
            shipAddress = g.goodInfo.shipAddress
            skuList = g.goodInfo.skuList
            mediaListArr = g.goodInfo.mediaListArr
        }
        const getImageUrl = ()=>{
            if(selectedGoodSkuIndex !== -1){
                return skuList[selectedGoodSkuIndex].media
            }else{
                return skuList[0]?.media
            }
        }
        const getSkuMessage = ()=>{
            if(selectedGoodSkuIndex === -1){
                return currentSelectGoodItem?.skuList.length + '种规格可选'
            }else{
                return '已选择' + currentSelectGoodItem?.skuList[selectedGoodSkuIndex].name
            }
        }
        const getSkuPrice = ()=>{
            if([0, 3].includes(showMode)){
                if(selectedGoodSkuIndex === -1){
                    const list = (currentSelectGoodItem as TGoodsItem).skuList
                    let min = list[0].price;
                    for (let i = 1; i < list.length; i++) {
                        if (list[i].price < min) {
                            min = list[i].price; // 更新最小值
                        }
                    }
                    return min.toString()
                }else{
                    return (currentSelectGoodItem?.skuList[selectedGoodSkuIndex].price as number) * selectedGoodCount
                }
            }else{
                return currentSelectSeckillGoodItem?.price.toString()
            }
        }
        const renderMarkdown = ()=> {
            return <div className={`w-full text-left`}>
                <div className={`p-5px bg-white`}>商品详细</div>
                <div>
                    <MdPreview id={id} modelValue={content}/>
                </div>
            </div>
        }
        const handleChangeCount = (count: number)=>{
            if(selectedGoodSkuIndex === -1){
                messageApi.error('请选择商品规格')
            }else{
                const currentSku = (currentSelectGoodItem as TGoodsItem).skuList[selectedGoodSkuIndex]
                if(count < 1){
                    setSelectedGoodCount(1)
                }else if(count > currentSku.inventory){
                    messageApi.error('库存不足')
                }else{
                    setSelectedGoodCount(count)
                }
            }
        }
        const onHandleConfirm = ()=>{
            if(selectedGoodSkuIndex === -1){
                messageApi.error('请选择商品规格')
            }else{
                if([0, 3].includes(showMode)){
                    setShowMode(5)
                }else if([1, 4].includes(showMode)){
                    setShowMode(6)
                }
            }
        }
        //#endregion
        //#region confirm
        const backToDetail = async () => {
            if([6].includes(showMode) && currentSelectSeckillGoodItem) {
                const res = await AxiosPost(GET_SECKILL_GOODS_INFO_BY_ID_URL, {
                    seckillId: currentSelectSeckillGoodItem.id,
                })
                if (res.code === 0) {
                    setCurrentSelectSeckillGoodItem({
                        ...res.data,
                        goodInfo: {
                            ...res.data.goodInfo,
                            mediaListArr: res.data.goodInfo.mediaList.split('||split||')
                        }
                    })
                }
            }
            setShowMode(showMode === 5 ? 3 : showMode === 6 ? 4 : 0)
        }
        const selectedSku = skuList[selectedGoodSkuIndex];
        const onHandleConfirmOrder = async ()=>{
            setRequesting(true)
            if([5].includes(showMode)){
                const res = await AxiosPost(NEW_ORDER,{
                    consignee: getUserInfo('customName'),
                    phone: getUserInfo('phone'),
                    address: getUserInfo('address'),
                    payWay: selectedPayway,
                    remark: orderRemark,
                    orderDetails: [{
                        skuId: selectedSku.id,
                        count: selectedGoodCount
                    }],
                    type: GENERAL_ORDER,
                })
                console.log('res:',res)
                if(res.code === 0){
                    //如果未支付跳转支付页面
                    messageApi.success('下单成功')
                    await backToDetail()
                    setShowSkuSelector(false)
                }else{
                    messageApi.error(res.msg)
                }
            }else if([6].includes(showMode) && currentSelectSeckillGoodItem){
                const res = await AxiosPost(NEW_SECKILL_ORDER(currentSeckillPath),{
                    seckillGoodId: currentSelectSeckillGoodItem.id,
                    order: {
                        consignee: getUserInfo('customName'),
                        phone: getUserInfo('phone'),
                        address: getUserInfo('address'),
                        payWay: selectedPayway,
                        remark: orderRemark,
                        orderDetails: [{
                            seckillId: currentSelectSeckillGoodItem.id,
                        }],
                        type: SECKILL_ORDER
                    }
                })
                if(res.code === 0){
                    console.log('抢购商品请求成功:',res)
                    await onHandleGetSeckillStatus(res.data)
                }else{
                    messageApi.error(`${res.code} ${res.msg}`)
                }
            }
            setRequesting(false);
            await getMyUserInfo();
        }
        const getSeckillStatusText = () => {
            const calculateTimePassed = (difference: number) => {
                const h = Math.floor(difference / (1000 * 60 * 60));
                const m = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));
                const s = Math.floor((difference % (1000 * 60)) / 1000);
                return `${addDate0(h)}:${addDate0(m)}:${addDate0(s)}`;
            };

            const getTimeOnly = (date: Date) => {
                return new Date(0, 0, 0, date.getHours(), date.getMinutes(), date.getSeconds());
            };

            let seckilling = false;
            let statusText = '秒杀未开启';

            if (currentTime) {
                const { startTime, endTime } = currentSelectSeckillGoodItem as TSeckillGoodsItem;
                const st = new Date(startTime);
                const et = new Date(endTime);

                // 只比较时分秒部分
                const currentTimeOnly = getTimeOnly(currentTime);
                const startTimeOnly = getTimeOnly(st);
                const endTimeOnly = getTimeOnly(et);

                if (currentTimeOnly < startTimeOnly) {
                    const difference = startTimeOnly.getTime() - currentTimeOnly.getTime();
                    statusText = `距抢购开始\n${calculateTimePassed(difference)}`;
                }

                if (currentTimeOnly >= startTimeOnly && currentTimeOnly <= endTimeOnly) {
                    seckilling = true;
                    const difference = endTimeOnly.getTime() - currentTimeOnly.getTime();
                    statusText = `距抢购结束\n${calculateTimePassed(difference)}`;
                }

                if (currentTimeOnly > endTimeOnly) {
                    statusText = '抢购已结束';
                }
            }

            return {
                seckilling,
                statusText
            };
        };
        const seckillNotStart = [4].includes(showMode) && !getSeckillStatusText()['seckilling']
        const onHandleClickBuy = async ()=>{
            if([4].includes(showMode) && currentSelectSeckillGoodItem){
                const res = await AxiosGet(GET_SECKILL_PATH,{
                    seckillGoodId: currentSelectSeckillGoodItem.id
                })
                if (res.code === 0) {
                    setCurrentSeckillPath(res.data)
                    setShowSkuSelector(true)
                }else{
                    messageApi.error(res.msg)
                }
            }else{
                setShowSkuSelector(true)
            }
        }
        //#endregion
        return <div className={`w-100vw h-100vh bg-#0000004D fixed left-0 top-0 RowBox justify-center items-center`}>
            {
                [3, 4].includes(showMode) &&
                <div className={`w-60vw h-75vh bg-#E1E1E1 overflow-x-hidden text-black relative ColumnBox`}>
                    <div className={`absolute left-15px top-15px p-5px z-999999 text-black`}
                         onClick={onHandleCloseModel}>
                        <CaretLeftOutlined style={{fontSize: '20px'}}/>
                    </div>
                    <div style={{width: 'calc(100% + 20px)', height: 'calc(100vh - 55px)'}}
                         className={`overflow-y-scroll overflow-x-hidden bg-#c6c9c8`}>
                        <div style={{width: 'calc(100% - 5px)'}} className={`h-full `}>
                            <Row className={`w-full h-full`}>
                                <Col xs={24} sm={24} md={12} lg={12}
                                     xl={12} className={`mb-1px`}>
                                    <div className={`w-full text-left`}>
                                        <Carousel autoplay className={`w-full bg-white`} draggable>
                                            {
                                                mediaListArr.map((picItem: string, picIndex: number) => {
                                                    return (
                                                        <div key={`${picItem}-${picIndex}`} className={`w-full h-full`}>
                                                            <Image preview={false} src={picItem}
                                                                   fallback={DefaultGoodsImage as any}/>
                                                        </div>
                                                    )
                                                })
                                            }
                                        </Carousel>
                                        {
                                            [3].includes(showMode) && <div
                                                className={`w-full box-border p-5px text-20px RowBox justify-between items-center bg-white`}>
                                                <div className={`text-red `}>¥{price}</div>
                                                <div className={`text-14px`}>已售 0</div>
                                            </div>
                                        }
                                        {
                                            [4].includes(showMode) && <div
                                                className={`w-full box-border p-5px text-20px RowBox justify-between items-center bg-red-7 text-white`}>
                                                <div className={`text-white ColumnBox`}>
                                                    <div>¥{price}</div>
                                                    <div className={`text-14px`}>已售 0</div>
                                                </div>
                                                <div className={`text-14px`}>
                                                    {getSeckillStatusText()['statusText']}
                                                </div>
                                            </div>
                                        }
                                        <div className={`w-full box-border p-5px text-16px bg-white`}>
                                            {name}
                                        </div>
                                        <div
                                            className={`w-full box-border p-5px text-14px RowBox justify-between items-center bg-white`}>
                                            <div className={`text-12px`}>发货地: {shipAddress}</div>
                                        </div>
                                        {
                                            [0, 3].includes(showMode) && <div onClick={() => setShowSkuSelector(true)}
                                                                              className={`w-full box-border p-5px text-14px RowBox justify-between items-center bg-white cursor-pointer`}>
                                                <div className={`text-12px`}>选择规格: {getSkuMessage()}</div>
                                                <RightOutlined/>
                                            </div>
                                        }
                                    </div>
                                </Col>
                                <Col xs={0} sm={0} md={12} lg={12} xl={{flex: '50%'}} className={`h-full`}
                                     style={{borderLeft: '1px solid gray'}}>
                                    <div className={`h-full w-full overflow-x-hidden`}>
                                        <div className={`h-full overflow-y-scroll `}
                                             style={{width: 'calc(100% + 15px)'}}>
                                            {renderMarkdown()}
                                        </div>
                                    </div>
                                </Col>
                                <Col xs={24} sm={24} md={0} lg={0} xl={0}>
                                    {renderMarkdown()}
                                </Col>
                            </Row>
                        </div>
                    </div>
                    <div className={`h-55px bg-white RowBox justify-end items-center box-border px-10px`}>
                        <Button type="default" style={{background: seckillNotStart ? "gray" :"orange", color: "white"}}
                                disabled={seckillNotStart}
                                onClick={() => onHandleClickBuy()}>购买</Button>
                    </div>
                    {
                        showSkuSelector &&
                        <div className={`w-full absolute bottom-0 left-0 h-full ColumnBox justify-end bg-#0000005E`}>
                            <div className={`ColumnBox w-full h-70% bg-#E1E1E1 relative`}>
                                <div className={`absolute right-15px top-1px p-5px z-999999 text-black`}
                                     onClick={() => setShowSkuSelector(false)}>
                                    <CloseCircleOutlined style={{fontSize: '20px'}}/>
                                </div>
                                <div className={`text-center h-30px text-12px lh-30px bg-white`}>
                                    确定信息
                                </div>
                                <div className={`text-left text-12px ColumnBox px-5px bg-white`}>
                                    <div className={`text-14px`}>
                                        {getUserInfo('userName')} &nbsp;
                                        {getUserInfo('address')}
                                    </div>
                                    <div>包邮 今天付款，预计今天发货</div>
                                </div>
                                {/*pic and count selector*/}
                                <div className={`mt-5px`}>
                                    <div className={`RowBox px-10px bg-white`}>
                                        <Image preview={false} src={getImageUrl()} width={`30%`}
                                               fallback={DefaultGoodsImage as any}/>
                                        <div
                                            className={`text-left text-12px ColumnBox p-5px bg-white pt-10px justify-between`}>
                                            <div className={`text-20px text-red`}>
                                                ¥{getSkuPrice()}
                                            </div>
                                            <div className={`RowBox bg-#E1E1E1 py-5px mt-10px`}>
                                                <div className={`w-20px text-center h-10px lh-10px text-16px`}
                                                     style={{borderRight: '1px solid #CCCCCC'}}
                                                     onClick={() => handleChangeCount(selectedGoodCount - 1)}>-
                                                </div>
                                                <input disabled={true}
                                                       className={`w-24px border-none text-center text-black bg-#E1E1E1 h-10px lh-10px text-12px`}
                                                       value={selectedGoodCount}/>
                                                <div className={`w-20px text-center h-10px lh-10px text-16px`}
                                                     style={{borderLeft: '1px solid #CCCCCC'}}
                                                     onClick={() => handleChangeCount(selectedGoodCount + 1)}>+
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                {/*sku selector and remark*/}
                                <div className={`ColumnBox h-220px overflow-x-hidden`}>
                                    <div className={`ColumnBox px-10px box-border text-left bg-white text-14px py-5px h-220px overflow-y-scroll`}
                                        style={{width: 'calc(100% + 15px)'}}>
                                        <div>规格({skuLength})</div>
                                        <div className={`RowBox flex-wrap mt-5px`}>
                                            {
                                                skuList.map((item, idx) => {
                                                    return <div key={idx} className={`RowBox bg-#E1E1E1 mb-5px mr-5px`}
                                                                onClick={() => setSelectedGoodSkuIndex(idx)}
                                                                style={{
                                                                    border: `${idx === selectedGoodSkuIndex ? '1px solid orange' : 'none'}`,
                                                                    borderRadius: '5px'
                                                                }}>
                                                        <Image preview={false} src={item.media} width="25px"
                                                               height="25px" fallback={DefaultGoodsImage as any}/>
                                                        <span
                                                            className={`lh-25px mx-5px ${idx === selectedGoodSkuIndex ? 'text-orange ' : ''}`}>{item.name} ¥{item.price}</span>
                                                    </div>
                                                })
                                            }
                                        </div>
                                        <div className={`w-full mt-10px RowBox py-10px justify-between`}
                                             style={{borderTop: '1px solid #E1E1E1'}}>
                                            <div className={`w-100px`}>订单备注</div>
                                            <TextArea className={`w-70%`}
                                                      value={orderRemark}
                                                      onChange={(e) => setOrderRemark(e.target.value)}
                                                      placeholder="输入订单备注"
                                                      autoSize={{minRows: 2, maxRows: 3}}
                                            />
                                        </div>
                                    </div>
                                </div>
                                <div className={`w-full h-45px bg-white ColumnBox items-center justify-center absolute bottom-0`}>
                                    <Button style={{background: 'orange', color: 'white'}} className={`w-80%`}
                                            onClick={onHandleConfirm}>购买</Button>
                                </div>
                            </div>
                        </div>
                    }
                </div>
            }
            {
                [5, 6].includes(showMode) &&
                <div className={`w-60vw h-75vh bg-#E1E1E1 overflow-x-hidden text-black relative ColumnBox`}>
                    <div className={`p-5px z-999999 text-black w-full text-left h-20px lh-20px`}
                         onClick={backToDetail}>
                        <CaretLeftOutlined style={{fontSize: '20px'}}/>
                    </div>
                    {/*地址选择*/}
                    <div className={`text-left text-12px RowBox justify-between items-center px-10px py-10px bg-white mx-5px border-rd-5px`}>
                        <div className={`ColumnBox`}>
                            <div>{getUserInfo('address')}</div>
                            <div className={`text-14px`}>
                                {getUserInfo('userName')} &nbsp; {getUserInfo('phone')}
                            </div>
                        </div>
                        <div><RightOutlined/></div>
                    </div>
                    {/*sku 展示*/}
                    <div className={`text-left text-12px ColumnBox px-10px py-10px bg-white mx-5px border-rd-5px mt-10px`}>
                        <div className={`RowBox`}>
                            <Image src={selectedSku?.media} preview={false} width="80px" height="80px"
                                   fallback={DefaultGoodsImage as any} className={`border-rd-5px`}/>
                            <div className={`w-60% ColumnBox box-border p-5px justify-between`}>
                                <div>{selectedSku?.name}</div>
                                <div className={`RowBox text-8px text-orange`}>
                                    <span
                                        className={`border-solid border-1px border-orange mr-5px px-3px`}>七天无理由退货</span>
                                    <span className={`border-solid border-1px border-orange px-3px`}>极速退款</span>
                                </div>
                            </div>
                            <div className={`ColumnBox text-orange items-end text-18px`}
                                 style={{width: 'calc(100% - 60% - 80px)'}}>
                                ¥{selectedSku?.price}
                            </div>
                        </div>
                        <div className={`w-full mt-10px RowBox py-10px justify-between`}
                             style={{borderTop: '1px solid #E1E1E1'}}>
                            <div className={`w-100px`}>订单备注</div>
                            <TextArea className={`w-70%`}
                                      value={orderRemark}
                                      onChange={(e) => setOrderRemark(e.target.value)}
                                      placeholder="输入订单备注"
                                      autoSize={{minRows: 2, maxRows: 3}}
                            />
                        </div>
                    </div>
                    {/*支付方式选择*/}
                    <div className={`text-left text-12px ColumnBox px-10px py-10px bg-white mx-5px border-rd-5px mt-10px text-14px font-500`}>
                        <div className={`RowBox justify-between mb-10px`} onClick={()=>setSelectedPayway(1)}>
                            <span className={`RowBox justify-center`}>
                                <AlipayCircleOutlined className={`text-blue text-20px mr-6px`}/> 支付宝
                            </span>
                            <Radio checked={selectedPayway === 1} />
                        </div>
                        <div className={`RowBox justify-between mb-10px`} onClick={()=>setSelectedPayway(2)}>
                            <span className={`RowBox justify-center`}>
                                <WechatOutlined className={`text-green text-20px  mr-6px`}/> 微信支付
                            </span>
                            <Radio checked={selectedPayway === 2} />
                        </div>
                        <div className={`RowBox justify-between`} onClick={()=>getUserInfo('balance') && setSelectedPayway(3)}>
                            <span className={`RowBox justify-center`}>
                                <AccountBookOutlined className={`text-yellow text-20px  mr-6px`}/> 余额支付 <span className={`text-orange`}>(¥{getUserInfo('balance')})</span>
                            </span>
                            <Radio checked={selectedPayway === 3} disabled={getUserInfo('balance') === 0} />
                        </div>
                    </div>
                    <div className={`absolute bottom-0 w-full h-45px bg-white RowBox justify-end px-10px items-center box-border`}>
                        <Button style={{background: 'orange',color: 'white'}} className={`w-60%`} onClick={onHandleConfirmOrder}>确定订单</Button>
                    </div>
                </div>
            }
        </div>
    }

    const onHandleGetSeckillStatus = async (id: string,retryCount = 0) =>{
        const res = await AxiosPost(GET_MY_ORDER_BY_ID,{id})
        console.log('res:',res)
        if(res.code === 0 && res.data){
            //查询到支付成功
            messageApi.success('抢购成功')
            if(res.data.status === EOrderStatus.NEW_UNPAID){
                messageApi.success('请支付')
                await onHandleChangeTab(2)
            }else{
                await onHandleChangeTab(2)
            }
        }else{
            if(retryCount < 5){
                setTimeout(()=>{
                    onHandleGetSeckillStatus(id,retryCount + 1)
                },800 * retryCount)
            }else{
                messageApi.error('抢购失败')
            }
        }
        //判定是否支付成功，如果成功则显示抢购成功，如果未付款请求付款接口，如果是余额付款余额不足即提示充值余额
    }
    return (
        <div className="w-full h-full relative">
            {contextHolder}
            {
                requesting && <div
                    className={`bg-[#9a9a9aba] RowBox justify-center items-center absolute top-0 w-full h-full z-9999999`}>
                    <div className={`ColumnBox`}>
                        <Spin size="large"/>
                        <div className={`mt-10px`}>请求处理中</div>
                    </div>
                </div>
            }
            <div className="RowBox  justify-between">
                <div className="RowBox text-blue">
                    <div onClick={onClickTest}>test</div>
                    <div onClick={() => onHandleChangeTab(0)} className="mr-10px">商城</div>
                    <div onClick={() => onHandleChangeTab(1)} className="mr-10px">限时秒杀</div>
                    <div onClick={() => onHandleChangeTab(2)} className="mr-10px">我的</div>
                    <div onClick={() => navigate("/manage")}>管理后台</div>
                </div>
                <div className={`RowBox`}>
                    <div className={`mr-5px`}>{getUserInfo('userName')}</div>
                    <div className={`text-red`} onClick={() => {
                        localStorage.removeItem('token');
                        navigate('/login')
                    }}>退出
                    </div>
                </div>
            </div>
            {
                [0, 1, 3, 4, 5, 6].includes(showMode) &&
                <div className="h-40px w-full bg-white ColumnBox justify-center items-center">
                    <Space.Compact size="middle" className="w-96%">
                        <Input addonBefore={<SearchOutlined/>} placeholder="输入搜索的商品名"
                               onChange={(e) => setSearchName(e.target.value)}/>
                        <Search placeholder="输入商品类别编号" enterButton="Search" size="middle"
                                onInput={(e: any) => {
                                    setSearchType(e.target.value)
                                }} loading={loading} onSearch={onHandleGetList}/>
                    </Space.Compact>
                </div>
            }
            {
                [2].includes(showMode) && <div className="ColumnBox w-80vw bg-gray"
                    style={{height: 'calc(100vh - 140px)'}}>
                    <MyInfo />
                </div>
            }
            {
                [0, 1, 3, 4, 5, 6].includes(showMode) &&
                <div className="ColumnBox w-80vw bg-gray overflow-x-hidden overflow-y-scroll"
                     style={{height: 'calc(100vh - 140px)'}}
                     id="scrollableDiv">
                    <InfiniteScroll
                        dataLength={[0, 3, 5].includes(showMode) ? goodList.length : [1, 4, 6].includes(showMode) ? seckillGoodList.length : 0}
                        next={loadMoreData}
                        hasMore={getHasMore}
                        loader={<Skeleton avatar paragraph={{rows: 1}} active/>}
                        endMessage={<Divider plain>It is all, nothing more 🤐</Divider>}
                        scrollableTarget="scrollableDiv"
                    >
                        <List
                            grid={{gutter: 5, column: 3}}
                            dataSource={([0, 3,5].includes(showMode) ? goodList : [1, 4,6].includes(showMode) ? seckillGoodList : []) as []}
                            renderItem={(item: TGoodsItem | TSeckillGoodsItem, index) => renderListItem(item, index)}
                        />
                    </InfiniteScroll>
                </div>
            }
            {
                [3, 4, 5,6].includes(showMode) && renderModel()
            }
        </div>
    )
};

export default MainComponent
