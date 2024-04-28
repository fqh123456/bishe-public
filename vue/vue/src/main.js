import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import request from "@/utils/request";
import pyrequest from "@/utils/pyrequest";
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css';
import axios from "axios";

Vue.config.productionTip = false

Vue.use(ElementUI, {size:"small"});

Vue.prototype.request=request
Vue.prototype.pyrequest=pyrequest





new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
