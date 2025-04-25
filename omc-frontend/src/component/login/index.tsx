import React, {useState} from 'react'
import {AxiosPost} from "@/utils";
import {LOGIN_URL, REGISTER_URL} from "@/api";
import {useNavigate} from "react-router-dom";
export type LoginProps = object

const LoginComponent: React.FC<LoginProps> = () => {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [email, setEmail] = useState('')
    const [phone, setPhone] = useState('')
    const [address, setAddress] = useState('')

    const [remember, setRemember] = useState<boolean>(true)
    const [isLogin, setIsLogin] = useState(true)
    const navigate = useNavigate()
    const login = async () => {
        if (username.length <= 3) {
            alert('用户名长度不能小于6')
            return
        }
        if (password.length <= 3) {
            alert('密码长度不能小于6')
            return
        }
        const res = await AxiosPost(LOGIN_URL,{
            userName: username,
            userPasswd: password,
            remember: remember
        })
        if(res && 'code' in res && res.code === 0){
            window.localStorage.setItem('token', res.data['token'])
            window.localStorage.setItem('userInfo',JSON.stringify(res.data['userInfo']))
            navigate("/")
        }
        console.log("res:",res)
    }
    const register = async () => {
        if (username.length <= 3) {
            alert('用户名长度不能小于6')
            return
        }
        if (password.length <= 3) {
            alert('密码长度不能小于3')
            return
        }
        if(email.split('@').length !== 2){
            alert('邮箱格式不正确')
            return
        }
        if(phone.length !== 11){
            alert('手机号格式不正确')
            return
        }
        const res = await AxiosPost(REGISTER_URL,{
            userName: username,
            password: password,
            phone,
            email,
            address
        })
        if(res && 'code' in res && res.code === 0){
            window.localStorage.setItem('token', res.data)
            navigate("/")
        }
        console.log("res:",res)
    }
    return (
        <div className="w-full h-full ColumnBox">
            登录
            <div className="RowBox">
                用户名
                <input type="text" onChange={(e) => setUsername(e.target.value)}/>
            </div>
            <div className="RowBox">
                密码
                <input type="password" onChange={(e) => setPassword(e.target.value)}/>
            </div>
            {
                !isLogin && <>
                    <div className="RowBox">
                        email
                        <input type="text" onChange={(e) => setEmail(e.target.value)}/>
                    </div>
                    <div className="RowBox">
                        地址
                        <input type="text" onChange={(e) => setAddress(e.target.value)}/>
                    </div>
                    <div className="RowBox">
                        手机号
                        <input type="text" onChange={(e) => setPhone(e.target.value)}/>
                    </div>
                </>
            }

            {
                isLogin &&
                <div className="RowBox">
                    记住我
                    <input type="checkbox" checked={remember} onChange={(e) => setRemember(e.target.checked)}/>
                </div>
            }
            {
                isLogin && <button onClick={() => setIsLogin(false)}>注册账号</button>
            }
            {
                !isLogin && <button onClick={() => register()}>注册</button>
            }
            {
                !isLogin && <button onClick={() => setIsLogin(true)}>去登录</button>
            }
            {
                isLogin && <button onClick={() => login()}>登录</button>
            }
        </div>
    )
};

export default LoginComponent
