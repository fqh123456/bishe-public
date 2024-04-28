import App from './App'

// #ifndef VUE3
<<<<<<< HEAD
import Vue from 'vue'

import uView from '@/uni_modules/uview-ui'
Vue.use(uView)
import './uni.promisify.adaptor'
import { request } from '@/utils/request.js'
=======
import Vue from 'vue'

import uView from '@/uni_modules/uview-ui'
Vue.use(uView)
import './uni.promisify.adaptor'
import { request } from '@/utils/request.js'
>>>>>>> df5742f475218251bd4c1536b5da73f88340e071
Vue.prototype.request = request
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif