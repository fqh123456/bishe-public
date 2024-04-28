import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";
import HomeView from '../views/Manage.vue'
import Manage from "@/views/Manage";

Vue.use(VueRouter)

const routes = [

  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  },
  {
    path: '/newsManagement',
    name: 'newsManagement',
    component: () => import(/* webpackChunkName: "about" */ '../views/NewsManagement.vue')
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/front/Front.vue'),
    children: [
      {
        path: 'home',
        name: 'FrontHome',
        component: () => import('../views/front/Home.vue')
      },
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


// 提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}


export const setRoutes=()=>{
  const storeMenus = localStorage.getItem("menus");
  if(storeMenus){


    const currentRouteNames=router.getRoutes().map(v=>v.name)
    if(!currentRouteNames.includes('Manage')){
      //拼装动态路由
      const manageRoute= { path: '/', name: 'Manage', redirect:'/home', component: () => import('../views/Manage.vue'),
        children: [
          { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
          // { path: 'handlePaper', name: '阅卷', component: () => import('../views/HandPaper.vue')},
          // { path: 'front/home', name: '学生端', component: () => import('../views/front/Home.vue')},
        ]}
      const menus=JSON.parse(storeMenus)

      menus.forEach(item => {
        if (item.path) {  // 当且仅当path不为空的时候才去设置路由
          let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
          manageRoute.children.push(itemMenu)
        } else if(item.children.length) {
          item.children.forEach(item => {
            if (item.path) {
              let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })

      //动态添加到现在的路由对象中去
      router.addRoute(manageRoute)
    }

  }
}

// 重置我就再set一次路由
setRoutes()

router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称
  store.commit("setPath")

  // 未找到路由的情况
  if (!to.matched.length) {
    const storeMenus = localStorage.getItem("menus")
    if (storeMenus) {
      next("/404")
    } else {
      // 跳回登录页面
      next("/login")
    }
  }
  // 其他的情况都放行
  next()

})


export default router
