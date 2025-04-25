import React, {useEffect, useState} from 'react'
import dayjs from 'dayjs';
import {
    GET_ALL_GOODS_LIST_URL, GET_GOODS_LIST_URL,GET_ALL_SECKILL_GOODS_LIST_URL,
    ADD_GOOD, UPDATE_GOOD, REMOVE_GOOD,INIT_SECKILL_STATUS,
    REMOVE_GOOD_SKU, UPDATE_GOOD_SKU, ADD_GOOD_SKU,
    ADD_SECKILL_GOOD, REMOVE_SECKILL_GOOD, UPDATE_SECKILL_GOOD
    // GET_QINIU_TOKEN_URL
} from "@/api";
import { MdEditor } from 'md-editor-rt';
import 'md-editor-rt/lib/style.css';
import {AxiosPost} from "@/utils";
import {
    Button,
    Pagination,
    Input,
    Space,
    List, message, Image, Popconfirm,
    DatePicker, DatePickerProps, Select
    // Upload,
    // UploadFile,
    // UploadProps, GetProp,
    // Image,
} from 'antd';
import Search from "antd/es/input/Search";
const { RangePicker } = DatePicker;
import {CloseCircleOutlined, SearchOutlined} from "@ant-design/icons";
import {RangePickerProps} from "antd/es/date-picker";

export type goodManageProps = object;

const GoodManage: React.FC<goodManageProps> = (props) =>{
    const [messageApi, contextHolder] = message.useMessage();
    void props;
    const [loading, setLoading] = useState<boolean>(false)
    //#region goodsManage
    const [pageSize,setPageSize] = useState(10);
    const [currentPage,setCurrentPage] = useState(1);
    const [total,setTotal] = useState(0);
    const onHandleGetGoodList = async (page = -1)=>{
        setLoading(true)
        let pageNum = page;
        if(page !== -1){
            setCurrentPage(page);
        }else{
            pageNum = currentPage;
        }
        const res = await AxiosPost(GET_ALL_GOODS_LIST_URL,{
            goodsName: searchName,
            goodsType: searchType,
            pageSize,
            pageNum,
        })
        setLoading(false)
        console.log('res:',res)
        if(res.code === 0 ){
            setTotal(res.data.total);
            setGoodList(res.data.rows);
        }
    }
    const onHandleGetSeckillGoodList = async (page = -1)=>{
        setLoading(true)
        let pageNum = page;
        if(page !== -1){
            setCurrentPage(page);
        }else{
            pageNum = currentPage;
        }
        const res = await AxiosPost(GET_ALL_SECKILL_GOODS_LIST_URL,{
            goodsName: searchName,
            goodsType: searchType,
            pageSize,
            pageNum
        })
        setLoading(false)
        console.log('res:',res)
        if(res.code === 0 ){
            setTotal(res.data.total);
            setSeckillGoodList(res.data.rows);
        }
    }
    const [isShowGoodList,setIsShowGoodList] = useState(true);//true is good list false is seckill good list
    const [goodList,setGoodList] = useState([]);
    const [seckillGoodList,setSeckillGoodList] = useState([]);
    const [searchName, setSearchName] = useState('');
    const [searchType, setSearchType] = useState(undefined);
    const CLOSE = 0;
    const ADD = 1;
    const EDIT = 2;
    const [editBoxMode, setEditBoxMode] = useState(CLOSE);//1为新增 2为编辑
    const DETAIL = 0;
    const SKU_DETAIL = 1;
    const [editBoxTab, setEditBoxTab] = useState(DETAIL);//0为商品详细，1为 sku 页
    const onHandleCloseEditBox = async() => {
        setEditBoxMode(CLOSE);
        setEditBoxTab(DETAIL)
        if(isShowGoodList){
            await onHandleGetGoodList();
        }else{
            await onHandleGetSeckillGoodList();
        }
    }
    const onHandleShowGoodList = async ()=>{
        setIsShowGoodList(true);
        setPageSize(10);
        setCurrentPage(1);
        await onHandleGetGoodList();
    }
    const onHandleShowSeckillGoodList = async ()=>{
        setIsShowGoodList(false);
        setPageSize(10);
        setCurrentPage(1);
        await onHandleGetSeckillGoodList();
    }
    const onHandleSearchGoodManageList = async ()=>{
        setPageSize(10);
        setCurrentPage(1);
        if(isShowGoodList){
            await onHandleShowGoodList();
        }else{
            await onHandleShowSeckillGoodList();
        }
    }
    const onRenderGoodManageList = (item: any, index: number)=>{
        let sumInventory = 0;
        let name = '';
        let priceStr = '';
        let imageUrl = '';
        let shipAddress = '';
        // console.log('onRenderGoodManageList item:',item)
        try{
            if(isShowGoodList){
                name = item['name'];
                const skuList = item['skuList'];
                let MaxPrice = 0;
                let MinPrice = 0;
                shipAddress = item['shipAddress'];
                if(item['mediaList']){
                    const mediaListArr = item['mediaList'].split('||split||');
                    if(Array.isArray(mediaListArr) && mediaListArr.length > 0){
                        imageUrl = mediaListArr[0];
                    }
                }
                skuList.forEach((skuItem:any)=>{
                    sumInventory += skuItem['inventory'];
                    const currentPrice = skuItem['price'];
                    if(currentPrice >= MaxPrice){
                        MaxPrice = currentPrice;
                    }else if(currentPrice <= MinPrice){
                        MinPrice = currentPrice;
                    }
                })
                if(skuList.length !== 0){
                    if(MinPrice === 0){
                        priceStr = `￥${MaxPrice}`;
                    }else{
                        priceStr = `￥${MinPrice} - ￥${MaxPrice}`;
                    }
                }
            }else{
                const goodInfo = item['goodInfo'];
                if(goodInfo['mediaList']){
                    const mediaListArr = goodInfo['mediaList'].split('||split||');
                    if(Array.isArray(mediaListArr) && mediaListArr.length > 0){
                        imageUrl = mediaListArr[0];
                    }
                }
                name = item['name'];
                priceStr = `¥${item['price']}`;
                sumInventory = item['stock'];
                shipAddress = goodInfo['shipAddress'];
            }
        }catch (e) {
            void e;
        }
        return <List.Item className="bg-#b3b3b3" key={index}>
            <div className="w-full h-50px RowBox align-items-center px-10px justify-between">
                <div className="RowBox" style={{width: 'calc(100% - 50px)'}}>
                    <div className="w-50px h-50px bg-white">
                        {
                            imageUrl && <Image src={imageUrl} className={`w-full h-full`}/>
                        }
                    </div>
                    <div className="w-420px text-left px-10px">{name}</div>
                    <div className="ColumnBox text-right box-border pr-10px" style={{width: 'calc(100% - 490px)'}}>
                        <div className="text-red">{priceStr ? priceStr : '未定义sku'}</div>
                        <div>发货地:{shipAddress}    总库存:{sumInventory}</div>
                    </div>
                </div>
                <div className="lh-50px  w-90px RowBox justify-around">
                    <div className={`text-blue cursor-pointer`} onClick={(e)=>{
                        e.stopPropagation();
                        onHandleEditInfo(item)
                    }}>编辑</div>
                    <Popconfirm
                        title="提示"
                        description="您确定要删除该商品吗"
                        onConfirm={()=>onHandleRemoveInfo(item)}
                        okText="Yes"
                        cancelText="No"
                    >
                        <div className={`text-red cursor-pointer`}>删除</div>
                    </Popconfirm>
                </div>
            </div>
        </List.Item>
    }
    const defaultGoodInfo = {
        goodsId: undefined,
        name: undefined,
        goodsType: undefined,
        mediaList: undefined,
        mediaListArr: [],
        content: undefined,
        skuList: [
            // {
            //     "id": "2A7BE941211C4E489F60E04C56E5E0B4",
            //     "goodsId": "50A94A73323B45F5A3762A70E52A2088",
            //     "name": "飞天茅台53度酱香型白酒",
            //     "price": 1499,
            //     "inventory": 2,
            //     "media": null,
            //     "addTime": "2024-12-05T10:59:52",
            //     "updateTime": "2024-12-05T10:59:52"
            // }
        ],
        addTime: undefined,
        updateTime: undefined,
        shipAddress: undefined,
    }
    const [goodInfo, setGoodInfo] = useState<any>(defaultGoodInfo)
    const defaultSkuInfo = {
        id: undefined,
        goodsId: undefined,
        name: undefined,
        price: undefined,
        inventory: undefined,
        media: undefined,
        addTime: undefined,
        updateTime: undefined
    }
    const onHandleEditInfo = async (item: any) => {
        if(isShowGoodList){
            console.log('item:', item);
            const list = item['mediaList'];
            setGoodInfo(Object.assign(item, {
                mediaListArr: list ? list.split('||split||') : []
            }))
        }else{
            setSeckillGoodInfo(item)
            await onSearchSeckillGoods("")
            setSeckillGoodInfoOptions([item['goodInfo']])
            setSelectGoodOption([item['goodInfo']].map((item: any, index: number) => {
                return {
                    label: item['name'],
                    value: index
                }
            }))
            setSelectSeckillGoodIndex(0)
            setSelectSeckillGoodSkuIndex(0)
        }
        setEditBoxMode(EDIT);
    };//点击GoodInfo 的编辑按钮
    const updateGoodInfo = (key:any,val: any,index = -1) => {
      if(key in goodInfo){
          setGoodInfo((prevState: any)=>{
              return {
                  ...prevState,
                  [key]: Array.isArray(prevState[key]) ? (()=>{
                      const arr = [...goodInfo[key]]
                      arr[index] = val;
                      return arr;
                  })() : val
              }
          })
      }
    }//goodInfo的输入处理
    const onHandleRemoveInfo = async (item:any) => {
        if(isShowGoodList){
            console.log('item:', item);
            const res = await AxiosPost(REMOVE_GOOD,{
                goodsId: item['goodsId']
            })
            if(res.code === 0){
                messageApi.success('删除成功')
                await onHandleGetGoodList();
            }
        }else{
            const res = await AxiosPost(REMOVE_SECKILL_GOOD,{
                id: item['id']
            })
            if(res.code === 0){
                messageApi.success('删除成功')
                await onHandleGetSeckillGoodList();
            }
        }
    }//请求删除 GoodInfo
    const onHandleSaveGoodInfo = async() => {
        console.log('goodInfo:',goodInfo)
        if(editBoxMode === ADD){
            const res = await AxiosPost(ADD_GOOD,Object.assign(goodInfo,{
                mediaList: goodInfo.mediaListArr.join('||split||'),
            }))
            if(res.code === 0){
                messageApi.success('新增成功')
                setEditBoxMode(CLOSE)
            }
        }else{
            const res = await AxiosPost(UPDATE_GOOD,Object.assign(goodInfo,{
                mediaList: goodInfo.mediaListArr.join('||split||'),
            }))
            if(res.code === 0){
                messageApi.success('保存成功')
                setEditBoxMode(CLOSE)
            }else{
                console.log('res',res)
                messageApi.error('保存失败')
            }
        }
        await onHandleGetGoodList()
    }//点击保存 goodInfo 执行新增或保存逻辑
    const [editSkuIndex, setEditSkuIndex] = useState(-1); //当前选择的 skuList 的 index
    const onHandleSaveGoodSku = async () => {
        if(editSkuIndex !== -1){
            const currentItem = goodInfo['skuList'][editSkuIndex];
            console.log('currentItem:',currentItem)
            if(!currentItem['name'] || !currentItem['price'] || !currentItem['inventory'] || !currentItem['media']){
                messageApi.error('请填写完整信息');
                return;
            }
            if(currentItem['id']){
                const res = await AxiosPost(UPDATE_GOOD_SKU,currentItem);
                console.log('edit res',res)
                if(res.code === 0){
                    setEditSkuIndex(-1);
                    messageApi.success('修改成功')
                }else{
                    console.log('res',res)
                    messageApi.error('修改失败')
                }
            }else{
                const res = await AxiosPost(ADD_GOOD_SKU,currentItem);
                console.log('add res:',res)
                if(res.code === 0){
                    const newSkuList = [...goodInfo.skuList.map((item:any, idx:number) => {
                        if (idx === editSkuIndex) {
                            return res.data;
                        }
                        return item;
                    })]
                    console.log('newSkuList:',newSkuList)
                    setGoodInfo((prevGoodInfo:any) => ({
                        ...prevGoodInfo,
                        skuList: JSON.parse(JSON.stringify(newSkuList)),
                    }));
                    messageApi.success('新增成功')
                    setEditSkuIndex(-1);
                }else{
                    console.log('res',res)
                    messageApi.error('新增失败')
                }
            }
        }else{
            messageApi.error('状态出现错误');
        }
    }//点击保存 sku 或新增 sku
    const onHandleAddGoodSku = () => {
        console.log('goodInfo:',goodInfo)
        const list = [...goodInfo['skuList']];
        list.push(Object.assign(defaultSkuInfo,{
            goodsId: goodInfo['goodsId'],
        }))
        setEditSkuIndex(list.length - 1);
        setGoodInfo((prevGoodInfo:any) => ({
            ...prevGoodInfo,
            skuList: list,
        }));
    }//点击新增 sku
    const onHandleEditGoodSku = (_item: any,idx:number) => {
        setEditSkuIndex(idx)
    }//编辑所点击的sku
    const onHandleRemoveGoodSku = async (item:any,idx:number) => {
        console.log('item',item,"idx",idx)
        if(item['id']){
            const res = await AxiosPost(REMOVE_GOOD_SKU,{goodsSkuId: item['id']})
            if(res.code === 0){
                messageApi.success('删除成功')
            }else{
                messageApi.error('删除失败')
                return false;
            }
        }
        const list = [...goodInfo['skuList']];
        list.splice(idx,1);
        setGoodInfo((prev:any)=>({
            ...prev,
            skuList: list
        }))
        setEditSkuIndex(-1)
    }//删除点击的 sku
    const onHandleEditSkuInfo = (e:any,key:string) => {
        setGoodInfo((prevGoodInfo:any) => ({
            ...prevGoodInfo,
            skuList: prevGoodInfo.skuList.map((item:any, idx:number) => {
                if (idx === editSkuIndex) {
                    return { ...item, [key]: e.target.value }; // 创建新的对象副本并修改对应的属性
                }
                return item;
            })
        }));
    }//sku Info 的输入处理
    const onHandleClickAddInfo = ()=>{
        setEditBoxMode(ADD)
        if(isShowGoodList){
            setGoodInfo(defaultGoodInfo)
        }else{
            setSeckillGoodInfo(defaultSeckillGoodInfo)
            setSelectSeckillGoodIndex(undefined)
            setSelectSeckillGoodSkuIndex(undefined)
            onSearchSeckillGoods("")
        }
    }//点击右上新增
    const defaultSeckillGoodInfo = {
        id: undefined,
        goodId: undefined,
        goodsType: undefined,
        goodSkuId: undefined,
        name: undefined,
        price: undefined,
        stock: undefined,
        startTime: undefined,
        endTime: undefined,
        addTime: undefined,
        updateTime: undefined,
        goodInfo: {},
        status: undefined
    }
    const [seckillGoodInfo, setSeckillGoodInfo] = useState<any>(defaultSeckillGoodInfo);
    const updateSeckillGoodInfo = (key: string,val:any) => {
        if(key in seckillGoodInfo){
            setSeckillGoodInfo((prevState: any)=>{
                return {
                    ...prevState,
                    [key]: val
                }
            })
        }
    }//SeckillGoodInfo 的输入处理
    const onOkSelectDate = (value: DatePickerProps['value'] | RangePickerProps['value'] | any) => {
        console.log('onOk: ', value);
        if(Array.isArray(value) && value[0] && value[1]) {
            setSeckillGoodInfo((prev:any)=>{
                return {
                    ...prev,
                    startTime: value[0].format('YYYY-MM-DDTHH:mm:ss'),
                    endTime: value[1].format('YYYY-MM-DDTHH:mm:ss'),
                }
            })
        }
    };

    const onHandleSaveSeckillGood = async () => {
        if(selectSeckillGoodIndex === undefined || selectSeckillGoodSkuIndex === undefined){
            messageApi.error('请先选择商品和sku')
        }else{
            let goodId = undefined;
            let goodsType = undefined;
            let goodSkuId = undefined;
            try{
                const goodInfo = seckillGoodInfoOptionList[selectSeckillGoodIndex]
                const skuInfo = goodInfo['skuList'][selectSeckillGoodSkuIndex]
                goodId = goodInfo['goodsId']
                goodsType = goodInfo['goodsType']
                goodSkuId = skuInfo['id']
                if(skuInfo['inventory'] < seckillGoodInfo['stock']){
                    messageApi.error('秒杀库存不能大于商品库存')
                    return false;
                }
            }catch (e) {
                messageApi.error("商品编辑时发生错误")
                void e;
                return false;
            }
            if(goodId !== undefined && goodsType !== undefined && goodSkuId !== undefined) {
                const newInfo = Object.assign(seckillGoodInfo,{
                    goodId,
                    goodsType,
                    goodSkuId,
                    goodInfo: undefined
                })
                if(editBoxMode === ADD){
                    const res = await AxiosPost(ADD_SECKILL_GOOD,newInfo)
                    if(res.code === 0){
                        messageApi.success('新增成功')
                        setSeckillGoodInfo(res.data)
                    }else{
                        messageApi.error('新增失败,可能该商品已存在一个秒杀记录')
                    }
                }else{
                    const res = await AxiosPost(UPDATE_SECKILL_GOOD,newInfo)
                    if(res.code === 0){
                        messageApi.success('修改成功')
                        setSeckillGoodInfo(res.data)
                    }else{
                        messageApi.error('修改失败')
                    }
                }
            }else{
                messageApi.error("获取商品id出错")
                return false;
            }
        }
    }
    const [seckillGoodInfoOptionList,setSeckillGoodInfoOptions] = useState<any>([]);
    const [selectGoodOption,setSelectGoodOption] = useState<any>([])
    const [selectSeckillGoodIndex,setSelectSeckillGoodIndex] = useState<number | undefined>(undefined);
    const [selectSeckillGoodSkuIndex,setSelectSeckillGoodSkuIndex] = useState<number | undefined>(undefined);
    const onChangeSeckillGoods = (value: any) => {
        setSelectSeckillGoodIndex(value)
        setSelectSeckillGoodSkuIndex(undefined)
    }
    const onSearchSeckillGoods = async (value: string) => {
        console.log('onSearchSeckillGoods')
        setSelectSeckillGoodIndex(undefined)
        setSelectSeckillGoodSkuIndex(undefined)
        const res = await AxiosPost(GET_GOODS_LIST_URL,{
            goodsName: value,
            pageSize:50,
            pageNum: 1
        })
        if(res.code === 0){
            const list = res.data['rows']
            setSeckillGoodInfoOptions(list)
            setSelectGoodOption(list.map((item: any, index: number) => {
                return {
                    label: item['name'],
                    value: index
                }
            }))
        }
    }
    //#endregion
    //#region upoload
    // const [previewOpen, setPreviewOpen] = useState(false);
    // const [previewImage, setPreviewImage] = useState('');
    // const [uploadToken, setUploadToken] = useState('');
    // const getUploadToken = async()=>{
    //     const res = await AxiosPost(GET_QINIU_TOKEN_URL,{})
    //     console.log('res:',res)
    //     if(res.code === 0){
    //         setUploadToken(res.data);
    //     }
    // }
    // const [fileList, setFileList] = useState<UploadFile[]>([]);
    //
    // const handlePreview = async (file: UploadFile) => {
    //     if (!file.url && !file.preview) {
    //         file.preview = await getBase64(file.originFileObj as FileType);
    //     }
    //
    //     setPreviewImage(file.url || (file.preview as string));
    //     setPreviewOpen(true);
    // };
    //
    // const handleChange: UploadProps['onChange'] = ({ fileList: newFileList }) =>
    //     setFileList(newFileList);
    //
    // const uploadButton = (
    //     <button style={{ border: 0, background: 'none' }} type="button" className="text-black">
    //         <PlusOutlined />
    //         <div style={{ marginTop: 8 }}>Upload</div>
    //     </button>
    // );
    // const onHandleUpload = async ({ file, onSuccess, onError }:any)=> {
    //     const formData = new FormData();
    //     formData.append('file', file);  // 将文件附加到 formData 中
    //     formData.append('token', uploadToken);  // 添加上传凭证
    //     formData.append('key', `mall/${file.name}`);
    //
    //     // 上传到七牛云
    //     fetch('https://up-z2.qiniup.com', {
    //         method: 'POST',
    //         body: formData,
    //     })
    //         .then((response) => response.json())
    //         .then((data: any) => {
    //             if (data.key) {
    //                 messageApi.success('上传成功')
    //                 onSuccess(data, file); // 上传成功
    //             } else {
    //                 messageApi.error(data.message);
    //                 onError(new Error('上传失败'));
    //             }
    //         })
    //         .catch((error) => {
    //             onError(error);  // 上传失败处理
    //         });
    // }
    // const uploadHtmlCode = ()=> {
    //     return <div>
    //         <div>
    //             商品头图:<br/>
    //         </div>
    //         <Upload
    //             customRequest={onHandleUpload}
    //             listType="picture-card"
    //             fileList={fileList}
    //             onPreview={handlePreview}
    //             onChange={handleChange}
    //         >
    //             {fileList.length >= 8 ? null : uploadButton}
    //         </Upload>
    //         {previewImage && (
    //             <Image
    //                 wrapperStyle={{display: 'none'}}
    //                 preview={{
    //                     visible: previewOpen,
    //                     onVisibleChange: (visible) => setPreviewOpen(visible),
    //                     afterOpenChange: (visible) => !visible && setPreviewImage(''),
    //                 }}
    //                 src={previewImage}
    //             />
    //         )}
    //     </div>
    // }
    //#endregion

    useEffect(() => {
        (async () => {
            await onHandleGetGoodList();
        })()
    }, [])
    const onHandleLog = ()=>{
        if(isShowGoodList) {
            console.log('goodInfo',goodInfo)
        }else{
            console.log('seckillGoodinfo',seckillGoodInfo)
            console.log('goodIndex',selectSeckillGoodIndex)
            console.log('skuIndex',selectSeckillGoodSkuIndex)
            console.log('seckillGoodInfoOptionList',seckillGoodInfoOptionList)
            console.log('selectGoodOption:',selectGoodOption)
        }
    }

    const onHandleUpdateSeckillStatus = async() =>{
        const result = await AxiosPost(INIT_SECKILL_STATUS,{});
        if(result.code === 0){
            messageApi.success('更新成功')
        }
    }
    return (
        <>
            {contextHolder}
            <div className="RowBox h-30px justify-between px-5px">
                <div className="RowBox h-30px bg-white lh-30px px-10px">
                    <div
                        className={`mr-15px cursor-pointer ${isShowGoodList ? 'text-black' : 'text-blue text-12px lh-35px'}`}
                        onClick={onHandleShowGoodList}>普通商品
                    </div>
                    <div
                        className={`cursor-pointer ${isShowGoodList ? 'text-blue text-12px lh-35px' : 'text-black'}`}
                        onClick={onHandleShowSeckillGoodList}>秒杀商品
                    </div>
                    <Button type={`link`} onClick={onHandleUpdateSeckillStatus}>更新秒杀商品状态</Button>
                </div>
                <div className="cursor-pointer text-blue lh-30px" onClick={onHandleClickAddInfo}>新增</div>
            </div>
            <div style={{height: 'calc(100vh - 130px)'}} className="ColumnBox justify-between" onClick={() => {
                setEditBoxMode(CLOSE);
                setEditBoxTab(DETAIL)
            }}>
                <div style={{height: 'calc(100% - 80px)'}} className="ColumnBox overflow-hidden">
                    <div className="h-40px w-full bg-white ColumnBox justify-center items-center">
                        <Space.Compact size="middle" className="w-96%">
                            <Input addonBefore={<SearchOutlined/>} placeholder="输入搜索的商品名"
                                   onChange={(e) => setSearchName(e.target.value)}/>
                            <Search placeholder="输入商品类别编号" enterButton="Search" size="middle"
                                    onInput={(e: any) => {
                                        setSearchType(e.target.value)
                                    }} loading={loading} onSearch={onHandleSearchGoodManageList}/>
                        </Space.Compact>
                    </div>
                    <div style={{height: 'calc(100% - 40px)',width: 'calc(100% + 20px)'}} className={`overflow-x-hidden overflow-y-scroll`}>
                        <List loading={loading}
                              itemLayout="horizontal"
                              dataSource={isShowGoodList ? goodList : seckillGoodList}
                              renderItem={onRenderGoodManageList}
                        />
                    </div>
                </div>
                <div className="w-full h-80px ColumnBox justify-center">
                    <Pagination defaultCurrent={1} total={total} pageSize={pageSize} showSizeChanger
                                onChange={async (c) => {
                                    if(isShowGoodList){
                                        await onHandleGetGoodList(c);
                                    }else{
                                        await onHandleGetSeckillGoodList(c);
                                    }
                                }}
                                onShowSizeChange={async (_c,s)=> {
                                    setPageSize(s)
                                    setCurrentPage(s)
                                    if(isShowGoodList){
                                        await onHandleGetGoodList();
                                    }else{
                                        await onHandleGetSeckillGoodList();
                                    }
                                }}/>
                </div>
            </div>
            {
                editBoxMode !== 0 && <div
                    className="fixed w-85% h-62vh top-0 left-0 bottom-0 right-0 ma bg-white ColumnBox" style={{border: '1px solid black'}}>
                    <div className="w-full h-30px RowBox items-center relative text-white justify-center bg-gray">
                        <div onClick={onHandleLog}>{editBoxMode === ADD ? '新增' : '编辑'}</div>
                        <div onClick={onHandleCloseEditBox}
                             className="absolute w-26px h-26px right-5px ColumnBox justify-center items-center cursor-pointer">
                            <CloseCircleOutlined />
                        </div>
                    </div>
                    {
                        isShowGoodList ? <div style={{height: 'calc(100% - 30px)'}} className="w-full RowBox">
                            <div className="w-45% h-full ColumnBox" style={{borderRight: '1px solid gray'}}>
                                <div className="w-full h-36px RowBox lh-36px" style={{borderBottom: '0.5px solid gray'}}>
                                    <div onClick={() => setEditBoxTab(DETAIL)}
                                         className={`cursor-pointer w-50% h-36px ${editBoxTab === DETAIL ? 'text-white bg-sky' : 'text-black text-12px lh-40px '}`}>商品详细
                                    </div>
                                    {
                                        editBoxMode !== ADD && <div onClick={() => {setEditBoxTab(SKU_DETAIL);setEditSkuIndex(-1)}}
                                            className={`cursor-pointer w-50% h-36px ${editBoxTab === DETAIL ? 'text-black text-12px lh-40px' : 'bg-sky text-white '}`}>商品sku</div>
                                    }
                                </div>
                                <div className="w-full ColumnBox overflow-x-hidden relative"
                                     style={{height: 'calc(100% - 36.5px)'}}>
                                    <div className="h-full overflow-y-scroll" style={{width: 'calc(100% + 20px)'}}>
                                        <div style={{width: 'calc(100% - 5px)'}}
                                             className={`${editBoxTab === DETAIL ? 'py-10px' : ''} ColumnBox items-center text-black overflow-x-hidden`}>
                                            {
                                                editBoxTab === DETAIL && <div className="w-96% text-black text-left">
                                                    <Input addonBefore="商品名" className="mb-5px" value={goodInfo['name']}
                                                           onInput={(e: any) => updateGoodInfo('name', e.target.value)}/>
                                                    <Input addonBefore="商品类型" className="mb-5px"
                                                           value={goodInfo['goodsType']}
                                                           onInput={(e: any) => updateGoodInfo('goodsType', e.target.value)}/>
                                                    <Input addonBefore="发货地" className="mb-5px"
                                                           value={goodInfo['shipAddress']}
                                                           onInput={(e: any) => updateGoodInfo('shipAddress', e.target.value)}/>
                                                    <Input addonBefore="头图链接1" className="mb-5px"
                                                           value={goodInfo['mediaListArr'][0]}
                                                           onInput={(e: any) => updateGoodInfo('mediaListArr', e.target.value, 0)}/>
                                                    <Input addonBefore="头图链接2" className="mb-5px"
                                                           value={goodInfo['mediaListArr'][1]}
                                                           onInput={(e: any) => updateGoodInfo('mediaListArr', e.target.value, 1)}/>
                                                    <Input addonBefore="头图链接3" className="mb-5px"
                                                           value={goodInfo['mediaListArr'][2]}
                                                           onInput={(e: any) => updateGoodInfo('mediaListArr', e.target.value, 2)}/>
                                                    <Input addonBefore="头图链接4" className="mb-5px"
                                                           value={goodInfo['mediaListArr'][3]}
                                                           onInput={(e: any) => updateGoodInfo('mediaListArr', e.target.value, 3)}/>
                                                    <Input addonBefore="头图链接5" className="mb-5px"
                                                           value={goodInfo['mediaListArr'][4]}
                                                           onInput={(e: any) => updateGoodInfo('mediaListArr', e.target.value, 4)}/>
                                                </div>
                                            }
                                            {
                                                editBoxTab === SKU_DETAIL &&
                                                <div className="w-full text-black text-left pt-30px">
                                                    <div
                                                        className="w-full h-30px bg-gray text-white lh-30px RowBox justify-between px-10px box-border absolute top-0">
                                                        <div>当前商品拥有{goodInfo['skuList'].length}个 sku</div>
                                                        <div className="cursor-pointer text-white"
                                                             onClick={onHandleAddGoodSku}>新增
                                                        </div>
                                                    </div>
                                                    <div className="w-full text-black text-left">
                                                        {
                                                            goodInfo['skuList'].map((item: any, idx: number) => {
                                                                return (<div
                                                                    className="w-full h-65px bg-#dedede RowBox justify-between mb-5px"
                                                                    key={idx}>
                                                                    <div
                                                                        className="h-65px box-border RowBox items-center px-10px"
                                                                        style={{width: 'calc(100% - 50px)'}}>
                                                                        <div className="w-55px h-55px bg-gray mr-8px">
                                                                            <Image src={item['media']}
                                                                                   className="w-full h-full"/>
                                                                        </div>
                                                                        <div style={{width: 'calc(100% - 65px)'}}
                                                                             className="ColumnBox text-12px">
                                                                            <div>{item['name']}</div>
                                                                            <div
                                                                                className="RowBox w-full justify-between">
                                                                                <div
                                                                                    className="text-red">{item['price'] ? `¥${item['price']}` : ''}</div>
                                                                                <div>{item['inventory'] ? `库存: ${item['inventory']}` : ''}</div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div
                                                                        className="w-50px h-65px ColumnBox justify-around box-border py-10px">
                                                                        <div className="text-blue cursor-pointer"
                                                                             onClick={() => onHandleEditGoodSku(item, idx)}>编辑
                                                                        </div>
                                                                        <Popconfirm
                                                                            title="提示"
                                                                            description="您确定要删除该sku吗"
                                                                            onConfirm={() => onHandleRemoveGoodSku(item, idx)}
                                                                            okText="Yes"
                                                                            cancelText="No"
                                                                        >
                                                                            <div
                                                                                className={`text-red cursor-pointer`}>删除
                                                                            </div>
                                                                        </Popconfirm>
                                                                    </div>
                                                                </div>)
                                                            })
                                                        }
                                                    </div>
                                                </div>
                                            }
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="w-55% h-full ColumnBox">
                                {
                                    editBoxTab === DETAIL && <div className="w-full h-full ColumnBox text-left">
                                        <MdEditor value={goodInfo['content']}
                                                  onChange={(str) => updateGoodInfo('content', str)}
                                                  style={{height: 'calc(100% - 45px)'}}/>
                                        <div className="w-full h-45px RowBox items-center justify-end">
                                            <Button type="primary" className="mr-10px"
                                                    onClick={onHandleSaveGoodInfo}>保存</Button>
                                        </div>
                                    </div>
                                }
                                {
                                    editBoxTab === SKU_DETAIL && (editSkuIndex === -1 ? <div
                                        className={`w-full h-full ColumnBox items-center justify-center text-30px text-gray`}>
                                        请点击新增或选中sku进行编辑
                                    </div> : <div className="w-full h-full ColumnBox text-left">
                                        <div style={{height: 'calc(100% - 45px)'}}
                                             className="w-full box-border px-10px py-10px">
                                            <Input addonBefore="产品名" className="my-5px"
                                                   value={goodInfo['skuList'][editSkuIndex]['name']}
                                                   onInput={(e: any) => onHandleEditSkuInfo(e, 'name')}/>
                                            <Input addonBefore="产品图" className="mb-5px"
                                                   value={goodInfo['skuList'][editSkuIndex]['media']}
                                                   onInput={(e: any) => onHandleEditSkuInfo(e, 'media')}/>
                                            <Input type="number" addonBefore="产品价格" className="mb-5px"
                                                   value={goodInfo['skuList'][editSkuIndex]['price']}
                                                   onInput={(e: any) => onHandleEditSkuInfo(e, 'price')}/>
                                            <Input type="number" addonBefore="库存" className="mb-5px"
                                                   value={goodInfo['skuList'][editSkuIndex]['inventory']}
                                                   onInput={(e: any) => onHandleEditSkuInfo(e, 'inventory')}/>

                                        </div>
                                        <div className="w-full h-45px RowBox items-center justify-end">
                                            <Button type="primary" className="mr-10px"
                                                    onClick={onHandleSaveGoodSku}>保存</Button>
                                        </div>
                                    </div>)
                                }
                            </div>
                        </div> : <div
                            style={{height: 'calc(100% - 30px)'}} className="w-full overflow-hidden">
                            <div className={`overflow-x-hidden overflow-y-scroll`} style={{width: 'calc(100% + 36px)',height: 'calc(100% - 46px)'}}>
                                <div className={`RowBox items-center flex-wrap justify-start py-10px`} style={{width: 'calc(100% - 30px)'}}>
                                    <Input addonBefore="商品名" className="mb-10px w-45% mx-3.3%" value={seckillGoodInfo['name']}
                                           onInput={(e: any) => updateSeckillGoodInfo('name', e.target.value)}/>
                                    <Input type="number" addonBefore="商品价格" className="mb-10px w-45%" value={seckillGoodInfo['price']}
                                           onInput={(e: any) => !isNaN(Number(e.target.value)) && updateSeckillGoodInfo('price', Number(e.target.value))}/>
                                    <Input addonBefore="商品库存" className="mb-10px w-45%  mx-3.3%" value={seckillGoodInfo['stock']}
                                           onInput={(e: any) => !isNaN(Number(e.target.value)) && updateSeckillGoodInfo('stock', Number(e.target.value))}/>
                                    <Space.Compact className={`w-45% RowBox mb-10px`}>
                                        <div className={`text-13px text-black w-25% ColumnBox items-center justify-center bg-#00000005`}
                                             style={{border: '1px solid #dcd7d7',borderRight: '0px',borderTopLeftRadius: '5px',borderBottomLeftRadius: '5px'}}>状态:</div>
                                        <Select
                                            defaultValue={seckillGoodInfo['status']}
                                            style={{width: 120}}
                                            onChange={(val)=>!isNaN(Number(val)) && updateSeckillGoodInfo('status', Number(val))}
                                            options={[
                                                {value: 0, label: '关闭'},
                                                {value: 1, label: '开启'},
                                                {value: 2, label: '每日开启'},
                                            ]}
                                        />
                                    </Space.Compact>
                                    <Space.Compact className={`w-45% RowBox  mx-3.3% mb-10px`}>
                                        <div className={`text-13px text-black w-25% ColumnBox items-center justify-center bg-#00000005`}
                                             style={{border: '1px solid #dcd7d7',borderRight: '0px',borderTopLeftRadius: '5px',borderBottomLeftRadius: '5px'}}>开始结束时间:</div>
                                        <RangePicker className={`w-75%`}
                                            showTime={{ format: 'HH:mm' }}
                                            format="YYYY-MM-DD HH:mm"
                                            defaultValue={editBoxMode === EDIT ? [dayjs(seckillGoodInfo['startTime']), dayjs(seckillGoodInfo['endTime'])] : undefined}
                                            onChange={(value, dateString) => {
                                                console.log('Selected Time: ', value);
                                                console.log('Formatted Selected Time: ', dateString);
                                            }}
                                            onOk={onOkSelectDate}
                                        />
                                    </Space.Compact>

                                    <Space.Compact className={`w-90% mx-3.3% h-32px`}>
                                        <div
                                            className={`text-13px text-black w-25% ColumnBox items-center justify-center bg-#00000005`}
                                            style={{
                                                border: '1px solid #dcd7d7',
                                                borderRight: '0px',
                                                borderTopLeftRadius: '5px',
                                                borderBottomLeftRadius: '5px'
                                            }}>选择开启秒杀的商品:
                                        </div>
                                        <Select className={`w-40%`}
                                            showSearch
                                            value={selectSeckillGoodIndex}
                                            placeholder="选择或输入搜索商品"
                                            optionFilterProp="label"
                                            onChange={onChangeSeckillGoods}
                                            onSearch={onSearchSeckillGoods}
                                            options={selectGoodOption}
                                            allowClear={true}
                                        />
                                        {
                                            selectSeckillGoodIndex !== undefined && selectSeckillGoodIndex >= 0
                                            && seckillGoodInfoOptionList.length > 0 && <Select className={`w-40%`}
                                               value={selectSeckillGoodSkuIndex}
                                               placeholder="选择商品sku"
                                               optionFilterProp="label"
                                               onClear={()=>setSelectSeckillGoodSkuIndex(undefined)}
                                               onChange={(val:any)=>{
                                                   setSelectSeckillGoodSkuIndex(val)
                                                   console.log('onChange', val)
                                               }}
                                               options={seckillGoodInfoOptionList[selectSeckillGoodIndex]['skuList'].map((item: any,index:number)=>{
                                                   return {
                                                       label: item['name'],
                                                       value: index
                                                   }
                                               })}
                                               allowClear={true}
                                            />
                                        }
                                    </Space.Compact>
                                </div>
                            </div>
                            <div className={`w-full h-46px bg-blue  RowBox items-center justify-end`}>
                                <Button type="primary" className="mr-10px"
                                        onClick={onHandleSaveSeckillGood}>保存</Button>
                            </div>
                        </div>
                    }
                </div>
            }
        </>
    )
}

export default GoodManage;