import {RouterProvider, createBrowserRouter} from "react-router-dom";
import './App.css'
import Main from "./component/main/index.tsx";
import Login from "./component/login/index.tsx";
import Manage from "./component/manage/index.tsx";

function App() {
    const router = createBrowserRouter([
        {
            path: '/',
            element: <Main />,
        },
        {
            path: '/login',
            element: <Login />,
        },
        {
            path: '/manage',
            element: <Manage />,
        }
    ])
    return (
    <div>
        <RouterProvider router={router} />
    </div>
    )
}

export default App
