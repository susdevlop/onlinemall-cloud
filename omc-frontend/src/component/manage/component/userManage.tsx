import React, {useEffect, useMemo, useRef, useState} from 'react'
import {
    GET_USER_PERMISSIONS_LIST_URL,
    USER_LIST_URL,
    set_USER_PERMISSIONS_LIST_URL,
} from "@/api";
import 'md-editor-rt/lib/style.css';
import {AxiosPost} from "@/utils";
import {
    Tree,
    Button,
    ConfigProvider,
    Col,
    Row,
} from 'antd';
import type { TreeDataNode, TreeProps } from 'antd';



export type userManageProps = {
    permissionList: any[]
}

const UserManage: React.FC<userManageProps> = (props)=>{
    const {permissionList} = props;
    //#region userManage
    const [userList,setUserList] = useState([]);
    const [showUserPermissionModal, setShowUserPermissionModal] = useState(false);
    const treeRef = useRef(null);
    const [currentSelectedUser, setCurrentSelectedUser] = useState<any>();
    const showModalSetting = async (item:any)=>{
        setCurrentSelectedUser(item)
        const res = await AxiosPost(GET_USER_PERMISSIONS_LIST_URL,{
            userId: item['userId']
        })
        console.log('res:',res)
        console.log('getPermissionTree:',getPermissionTree)
        setShowUserPermissionModal(true);
        const keys: string[] = [];
        res.data.forEach((item: number)=>{
            if(item >=20){
                const isHead = item % 10 === 0;
                const belongRoleId = getFirstCombination(item);
                if(isHead){
                    keys.push(String(belongRoleId))
                }else{
                    keys.push(String(`${belongRoleId}-${item}`))
                }
            }else if(item === 0){
                keys.push(String(`${item}`))
            }
        })
        setCheckedKeys(keys)
        console.log("checkedKeys:",checkedKeys)

    }
    const [checkedKeys, setCheckedKeys] = useState<string[]>([]);
    function getFirstCombination(number: number) {
        // 如果输入是个位数，直接返回 0
        if (number < 10) {
            return 0;
        }
        // 将数值转为字符串，取十位数和个位数
        const numStr = number.toString();
        const tenDigit = numStr.charAt(0); // 十位数
        // 返回十位数和 0 组合
        return Number(tenDigit + '0');
    }

    const getPermissionTree = useMemo((): TreeDataNode[] =>{
        const treeData: TreeDataNode[] = [{
            title: '系统管理员',
            key: '0'
        }];
        permissionList.forEach((item: {omPermissionRoleid: number,omPermissionDesc: string,omPermissionId
                : string,omPermissionPath: string})=>{
            const currentRoleId = item['omPermissionRoleid'];
            const isHead = currentRoleId % 10 === 0;
            const belongRoleId = getFirstCombination(currentRoleId);
            const existIndex = treeData.findIndex((treeItem)=>treeItem['key'] === String(belongRoleId));
            if(existIndex !== -1){
                let isExistSameKeyCount = 0
                if('children' in treeData[existIndex] && Array.isArray(treeData[existIndex].children)){
                    treeData[existIndex].children.forEach((childItem:any)=>{
                        if(childItem['key'].includes(`${belongRoleId}-${currentRoleId}`)){
                            isExistSameKeyCount++;
                        }
                    })
                }
                treeData[existIndex].children?.push({
                    key: `${belongRoleId}-${currentRoleId}${isExistSameKeyCount > 0 ? '-'+isExistSameKeyCount : '' }`,
                    title: item['omPermissionDesc'],
                })
                if(isHead){
                    treeData[existIndex].title = item['omPermissionDesc'];
                }
            }else{
                treeData.push({
                    key: String(belongRoleId),
                    title: isHead ? item['omPermissionDesc'] : '',
                    children: isHead ? [] : [{
                        key: String(`${belongRoleId}-${currentRoleId}`),
                        title: item['omPermissionDesc'],
                    }]
                })
            }
        })
        console.log('treeData:',treeData)
        return treeData;
    },[permissionList])

    const onCheckTree: TreeProps['onCheck'] = (checkedKeys, info) => {
        console.log('onCheck', checkedKeys, info);
        console.log('getPermissionTree:',getPermissionTree)
        console.log('treeRef',treeRef.current)
        console.log("checkedKeys:",checkedKeys)
        const actionKey = info.node.key as string;
        const newKey: any[] = [];
        const allKey = (treeRef.current as any)?.state?.flattenNodes.map((item:any)=>item.key) || []
        const sameKeyHead = actionKey.split('-').slice(0,2).join('-');
        if(info.checked){// 选中
            newKey.push(...(checkedKeys as string[]))
            allKey.forEach((item:any)=>{
                if(item.includes(sameKeyHead)){
                    if(!newKey.includes(item))newKey.push(item)
                }
            })
        }else{//取消选中
            (checkedKeys as string[]).forEach((item:any)=>{
                if(!item.includes(sameKeyHead)){
                    newKey.push(item)
                }
            })
        }
        setCheckedKeys(newKey)
    };
    const onHandleSubmitPermission = async ()=>{
        console.log('treeRef',treeRef)
        console.log("checkedKeys:",checkedKeys)
        let allKey: any[] = [];
        try{
            allKey = (treeRef?.current as any).state.checkedKeys || []
        }catch(e){
            void e;
        }
        const selectedKey:string[] = [];
        allKey.forEach((item:any)=>{
            const keyArr = item.split('-');
            if(keyArr.length === 1){
                if(!selectedKey.includes(keyArr[0])){
                    selectedKey.push(keyArr[0])
                }
            }else if(keyArr.length === 2 || keyArr.length === 3){
                if(!selectedKey.includes(keyArr[1])){
                    selectedKey.push(keyArr[1])
                }
            }
        })
        console.log('selectedKey:',selectedKey)
        const res = await AxiosPost(set_USER_PERMISSIONS_LIST_URL,{
            userId: currentSelectedUser['userId'],
            roleIds: selectedKey || []
        })
        if(res.code === 0 && res.data === true){
            alert('设置成功')
        }else{
            alert('设置失败')
        }
    }
    //#endregion


    useEffect(()=>{
        (async()=>{
            const res = await AxiosPost(USER_LIST_URL,{})
            console.log('res:',res)
            if(res.code === 0){
                setUserList(res.data);
            }
        })()
    },[])
    return (
        <>
            {
                userList.map((item: any, index: number) => {
                    return <Row key={index} className="RowBox w-full justify-around mb-5px" gutter={1}>
                        <Col span={8}>{item['userName']}</Col>
                        <Col span={6}>{item['phone']}</Col>
                        <Col span={6}>{item['email']}</Col>
                        <Col span={4}>
                            <Button onClick={() => showModalSetting(item)}>权限管理</Button>
                        </Col>
                    </Row>
                })
            }
            {
                showUserPermissionModal && <div
                    className="fixed w-60vw h-42vh top-0 left-0 bottom-0 right-0 ma bg-#141414 ColumnBox justify-around">
                    <div className="w-full h-35vh overflow-y-auto">
                        {
                            getPermissionTree.length &&
                            <ConfigProvider theme={{token: {colorBgContainer: '#141414', colorText: '#ffffff'}}}>
                                <Tree
                                    ref={treeRef}
                                    checkable
                                    defaultExpandedKeys={[]}
                                    defaultSelectedKeys={[]}
                                    checkedKeys={checkedKeys} // 受控属性
                                    onCheck={onCheckTree}
                                    treeData={getPermissionTree}
                                    showLine={true}
                                />
                            </ConfigProvider>
                        }
                    </div>
                    <div className="w-full RowBox justify-center">
                        <Button className="mr-10px" type="primary" onClick={onHandleSubmitPermission}>确认</Button>
                        <Button onClick={() => setShowUserPermissionModal(false)}>取消</Button>
                    </div>
                </div>
            }
        </>
    )
}

export default UserManage