import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/common/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/common/Register.vue')
  },
  {
    path: '/',
    component: () => import('../views/consumer/Layout.vue'),
    meta: { requiresAuth: true, role: -1 },
    children: [
      { path: '', name: 'Home', component: () => import('../views/consumer/Home.vue') },
      { path: 'goods/:id', name: 'GoodsDetail', component: () => import('../views/consumer/GoodsDetail.vue') },
      { path: 'seckill/:id', name: 'SeckillDetail', component: () => import('../views/consumer/SeckillDetail.vue') },
      { path: 'orders', name: 'MyOrders', component: () => import('../views/consumer/MyOrders.vue') },
      { path: 'notices', name: 'ConsumerNotices', component: () => import('../views/consumer/Notices.vue') },
      { path: 'profile', name: 'Profile', component: () => import('../views/consumer/Profile.vue') },
    ]
  },
  {
    path: '/merchant',
    component: () => import('../views/merchant/Layout.vue'),
    meta: { requiresAuth: true, role: 1 },
    children: [
      { path: '', name: 'MerchantHome', component: () => import('../views/merchant/Dashboard.vue') },
      { path: 'goods', name: 'MerchantGoods', component: () => import('../views/merchant/GoodsList.vue') },
      { path: 'goods/add', name: 'MerchantGoodsAdd', component: () => import('../views/merchant/GoodsForm.vue') },
      { path: 'goods/edit/:id', name: 'MerchantGoodsEdit', component: () => import('../views/merchant/GoodsForm.vue') },
      { path: 'activities', name: 'MerchantActivities', component: () => import('../views/merchant/ActivityList.vue') },
      { path: 'activities/add', name: 'MerchantActivityAdd', component: () => import('../views/merchant/ActivityForm.vue') },
      { path: 'activities/edit/:id', name: 'MerchantActivityEdit', component: () => import('../views/merchant/ActivityForm.vue') },
      { path: 'orders', name: 'MerchantOrders', component: () => import('../views/merchant/OrderList.vue') },
      { path: 'notices', name: 'MerchantNotices', component: () => import('../views/merchant/Notices.vue') },
      { path: 'password', name: 'MerchantPassword', component: () => import('../views/shared/ChangePassword.vue') },
    ]
  },
  {
    path: '/admin',
    component: () => import('../views/admin/Layout.vue'),
    meta: { requiresAuth: true, role: 2 },
    children: [
      { path: '', name: 'AdminHome', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'users', name: 'AdminUsers', component: () => import('../views/admin/UserList.vue') },
      { path: 'merchants', name: 'AdminMerchants', component: () => import('../views/admin/MerchantList.vue') },
      { path: 'goods', name: 'AdminGoods', component: () => import('../views/admin/GoodsList.vue') },
      { path: 'activities', name: 'AdminActivities', component: () => import('../views/admin/ActivityList.vue') },
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/admin/OrderList.vue') },
      { path: 'notices', name: 'AdminNotices', component: () => import('../views/admin/NoticeList.vue') },
      { path: 'password', name: 'AdminPassword', component: () => import('../views/shared/ChangePassword.vue') },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  // Redirect root path to login if not authenticated
  if (to.path === '/') {
    if (!userStore.isLoggedIn) {
      next('/login')
      return
    }
    // If logged in, redirect to role-based home
    const rolePaths = { 0: '/', 1: '/merchant', 2: '/admin' }
    next(rolePaths[userStore.userRole] || '/login')
    return
  }

  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
    return
  }

  if (to.meta.role !== undefined && to.meta.role >= 0 && userStore.userRole !== to.meta.role) {
    const rolePaths = { 0: '/', 1: '/merchant', 2: '/admin' }
    next(rolePaths[userStore.userRole] || '/login')
    return
  }

  next()
})

export default router
