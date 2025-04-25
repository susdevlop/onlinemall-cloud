import React, {useEffect, useState} from 'react'
import {
    GET_CURRENT_PERMISSION,
    PERMISSION_LIST_URL,
} from "@/api";
import 'md-editor-rt/lib/style.css';
import {AxiosPost} from "@/utils";
import {useNavigate,useSearchParams} from "react-router-dom";
import UserManage from "@/component/manage/component/userManage.tsx";
import GoodManage from "@/component/manage/component/goodManage.tsx";
import OrderManage from "@/component/manage/component/orderManage.tsx";

export type ManageProps = object

// type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

// const getBase64 = (file: FileType): Promise<string> =>
//     new Promise((resolve, reject) => {
//         const reader = new FileReader();
//         reader.readAsDataURL(file);
//         reader.onload = () => resolve(reader.result as string);
//         reader.onerror = (error) => reject(error);
//     });

const ManageComponent: React.FC<ManageProps> = () => {
    const [tab,setTab] = useState<number>(0)
    const navigate = useNavigate()
    const [searchParams] = useSearchParams();
    const currentTab = searchParams.get('tab');


    //#region page loading
    const showUserManage = async ()=>{
        navigate('/manage?tab=0')
        setTab(0)

    }
    const showGoodManage = async ()=>{
        navigate('/manage?tab=1')
        setTab(1)
        // await getUploadToken();
    }
    const showOrderManage = ()=>{
        navigate('/manage?tab=2')
        setTab(2)
    }
    const [currentUserPermission,setCurrentUserPermission] = useState<number[]>([]);
    const getIsShowByPermission = (showKey: number[])=>{
        let isShow = false;
        currentUserPermission.forEach((item:number)=>{
            if(showKey.includes(item)){
                isShow = true;
            }
        })
        return isShow;
    }
    const [permissionList,setPermissionList] = useState([]);

    useEffect(() => {
        setTimeout(async ()=>{
            const currentUserPermissionRes = await AxiosPost(GET_CURRENT_PERMISSION,{})
            if(currentUserPermissionRes.code === 0){
                setCurrentUserPermission(currentUserPermissionRes.data);
                console.log('currentUserPermissionRes:',currentUserPermissionRes)
                if(currentUserPermissionRes.data.includes(0) || currentUserPermissionRes.data.includes(20)){
                    const permissionListRes = await AxiosPost(PERMISSION_LIST_URL,{})
                    if(permissionListRes.code === 0){
                        setPermissionList(permissionListRes.data);
                    }
                }
            }
            if(currentTab !== null){
                if(currentTab === '0'){
                    showUserManage();
                }else if(currentTab === '1'){
                    showGoodManage();
                }else if(currentTab === '2'){
                    showOrderManage();
                }

            }
        },0)
    },[])
    const getUserInfo = () => {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        return userInfo['userName']
    }
    //#endregion
    const onHandleGoHome = ()=>{
        navigate('/')
    }
    return (
        <div className="w-full h-full relative">
            <div className="RowBox justify-between">
                <div className="RowBox text-blue">
                    <div className={`mr-10px`} onClick={onHandleGoHome}>主页</div>
                    {
                        getIsShowByPermission([0, 20]) &&
                        <div onClick={showUserManage} className="mr-10px">用户管理</div>
                    }
                    {
                        getIsShowByPermission([0, 30]) &&
                        <div onClick={showGoodManage} className="mr-10px">商品管理</div>
                    }
                    {
                        getIsShowByPermission([0, 40]) && <div onClick={showOrderManage}>订单管理</div>
                    }
                </div>
                <div className={`RowBox`}>
                    <div className={`mr-5px`}>{getUserInfo()}</div>
                    <div className={`text-red`} onClick={() => {
                        localStorage.removeItem('token');
                        navigate('/login')
                    }}>退出</div>
                </div>
            </div>
            <div className="ColumnBox w-95vw bg-#6B6B6BFF" style={{height: 'calc(100vh - 100px)'}}>
                {
                    (getIsShowByPermission([0, 20]) && tab === 0) && <div>
                        <UserManage permissionList={permissionList} />
                    </div>
                }
                {
                    (getIsShowByPermission([0, 30]) && tab === 1) && <div className="ColumnBox">
                        <GoodManage />
                    </div>
                }
                {
                    (getIsShowByPermission([0, 40]) && tab === 2) && <div className="ColumnBox">
                        <OrderManage />
                    </div>
                }
            </div>
        </div>
    )
};

export default ManageComponent
