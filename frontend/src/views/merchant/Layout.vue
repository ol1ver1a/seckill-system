<template>
  <div class="layout">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="logo">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M13 10V3L4 14h7v7l9-11h-7z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <div class="brand">
          <span class="brand-name">FlashDeal</span>
          <span class="brand-role">商家</span>
        </div>
      </div>

      <nav class="sidebar-nav">
        <div class="nav-section">
          <router-link to="/merchant" class="nav-item" :class="{ active: route.path === '/merchant' }">
            <el-icon><DataBoard /></el-icon>
            <span>控制台</span>
          </router-link>
          <router-link to="/merchant/goods" class="nav-item" :class="{ active: route.path.startsWith('/merchant/goods') }">
            <el-icon><Goods /></el-icon>
            <span>商品管理</span>
          </router-link>
          <router-link to="/merchant/activities" class="nav-item" :class="{ active: route.path.startsWith('/merchant/activities') }">
            <el-icon><Timer /></el-icon>
            <span>秒杀活动</span>
          </router-link>
          <router-link to="/merchant/orders" class="nav-item" :class="{ active: route.path.startsWith('/merchant/orders') }">
            <el-icon><Document /></el-icon>
            <span>订单管理</span>
          </router-link>
          <router-link to="/merchant/notices" class="nav-item" :class="{ active: route.path.startsWith('/merchant/notices') }">
            <el-icon><Bell /></el-icon>
            <span>通知公告</span>
          </router-link>
          <router-link to="/merchant/password" class="nav-item" :class="{ active: route.path === '/merchant/password' }">
            <el-icon><Lock /></el-icon>
            <span>修改密码</span>
          </router-link>
        </div>

        <div class="nav-section nav-bottom">
          <router-link to="/" class="nav-item">
            <el-icon><HomeFilled /></el-icon>
            <span>返回商城</span>
          </router-link>
          <div class="nav-item logout" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出登录</span>
          </div>
        </div>
      </nav>
    </aside>

    <!-- Main Content -->
    <div class="main-wrapper">
      <header class="header">
        <div class="header-left">
          <h1 class="page-title">{{ pageTitle }}</h1>
        </div>
        <div class="header-right">
          <el-dropdown trigger="click" @command="handleCommand">
            <button class="user-btn">
              <el-avatar :size="36" :icon="UserFilled" />
              <div class="user-info">
                <span class="username">{{ userStore.user?.nickname || userStore.user?.username }}</span>
                <span class="role">商家</span>
              </div>
              <el-icon class="arrow"><ArrowDown /></el-icon>
            </button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="home">
                  <el-icon><HomeFilled /></el-icon> 商城首页
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <main class="main">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { DataBoard, Goods, Timer, Document, Bell, Lock, HomeFilled, SwitchButton, UserFilled, ArrowDown } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const pageTitle = computed(() => {
  const titles = {
    '/merchant': '控制台',
    '/merchant/goods': '商品管理',
    '/merchant/goods/add': '添加商品',
    '/merchant/orders': '订单管理',
    '/merchant/activities': '秒杀活动',
    '/merchant/notices': '通知公告',
    '/merchant/password': '修改密码'
  }
  return titles[route.path] || 'Dashboard'
})

function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (cmd === 'home') {
    router.push('/')
  }
}

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
  background: var(--bg-main);
}

.sidebar {
  width: 260px;
  background: var(--bg-sidebar);
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 50;
}

.sidebar-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 24px 20px;
  border-bottom: 1px solid rgba(255,255,255,0.08);
}

.logo {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, var(--primary), var(--accent));
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
}

.logo svg {
  width: 24px;
  height: 24px;
  color: white;
}

.brand {
  display: flex;
  flex-direction: column;
}

.brand-name {
  font-size: 18px;
  font-weight: 700;
  color: white;
}

.brand-role {
  font-size: 12px;
  color: #64748b;
}

.sidebar-nav {
  flex: 1;
  padding: 16px 12px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.nav-section {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: var(--radius-md);
  color: #94a3b8;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.2s ease;
  text-decoration: none;
  cursor: pointer;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.06);
  color: white;
}

.nav-item.active {
  background: linear-gradient(135deg, var(--primary), var(--primary-dark));
  color: white;
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
}

.nav-item .el-icon {
  font-size: 20px;
}

.nav-bottom {
  margin-top: auto;
  border-top: 1px solid rgba(255,255,255,0.08);
  padding-top: 16px;
}

.logout {
  color: #f87171;
}

.logout:hover {
  background: rgba(248, 113, 113, 0.1);
  color: #f87171;
}

.main-wrapper {
  flex: 1;
  margin-left: 260px;
  display: flex;
  flex-direction: column;
}

.header {
  height: 72px;
  background: white;
  border-bottom: 1px solid var(--border);
  padding: 0 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: sticky;
  top: 0;
  z-index: 40;
}

.header-left {
  display: flex;
  align-items: center;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
}

.header-right {
  display: flex;
  align-items: center;
}

.user-btn {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 14px 6px 6px;
  background: var(--bg-main);
  border: 1px solid var(--border);
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.user-btn:hover {
  background: white;
  border-color: var(--primary-light);
  box-shadow: var(--shadow-md);
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.user-info .username {
  font-weight: 600;
  font-size: 14px;
  color: var(--text-primary);
}

.user-info .role {
  font-size: 11px;
  color: var(--text-muted);
}

.arrow {
  font-size: 12px;
  color: var(--text-muted);
}

.main {
  flex: 1;
  padding: 24px 32px;
}
</style>
