import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Element from 'element-ui'
import axios from 'axios'
import JsonViewer from 'vue-json-viewer'
import './icons'

import '../public/iconfont/iconfont.js'

import './style/x-index.css'

import './permission.js' // 路由拦截

import mavonEditor from 'mavon-editor'

import "element-ui/lib/theme-chalk/index.css"
import 'mavon-editor/dist/css/index.css'
import "./axios.js"

// import RightDrawer from "./components/RightDrawer";
// Vue.component("RightDrawer", RightDrawer) //注册全局组件

Vue.use(Element)
Vue.use(mavonEditor)
Vue.use(JsonViewer)


Vue.config.productionTip = false
Vue.prototype.$axios = axios

// Vue.http.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
