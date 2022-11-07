import Vue from 'vue'
import VueRouter from 'vue-router'


import Login from '../views/Login.vue'

import Uindex from "../views/Uindex"
import Welcome from '../components/Welcome.vue'
import Sservice from '../components/Sservice.vue'
import boIndex from "../views/boIndex";
import contextIndex from "../views/contextIndex";
import ProcessFlow from "../components/ProcessFlow";
import search from "../views/search";

// 安装路由
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Uindex',
    component: Uindex,
    redirect:'/rzgsdu',
    children:[
        {path:'/rzgsdu',component:Welcome},
      {name: 'search', path: '/rzgsdu/search', component: search}]
  },
  {
    path:'/rzgsdu/boIndex',
    name: 'boIndex',
    component: boIndex
  },
  {
    path:'/rzgsdu/contextIndex',
    name: 'contextIndex',
    component: contextIndex
  }

]

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})


// 配置导出路由
export default router
