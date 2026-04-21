<template>
  <div class="layout">
    <!-- Modern Header -->
    <header class="header">
      <div class="header-content">
        <div class="header-left">
          <div class="logo" @click="$router.push('/')">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M13 10V3L4 14h7v7l9-11h-7z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <span>FlashDeal</span>
          </div>
          <nav class="nav">
            <router-link to="/" class="nav-item" :class="{ active: route.path === '/' }">
              <el-icon><HomeFilled /></el-icon>
              <span>首页</span>
            </router-link>
            <router-link to="/orders" class="nav-item" :class="{ active: route.path === '/orders' }">
              <el-icon><Document /></el-icon>
              <span>我的订单</span>
            </router-link>
            <router-link to="/notices" class="nav-item" :class="{ active: route.path === '/notices' }">
              <el-icon><Bell /></el-icon>
              <span>通知</span>
            </router-link>
          </nav>
        </div>
        <div class="header-right">
          <el-dropdown trigger="click" @command="handleCommand">
            <button class="user-btn">
              <el-avatar :size="36" :icon="UserFilled" />
              <span class="username">{{ userStore.user?.nickname || userStore.user?.username }}</span>
              <el-icon class="arrow"><ArrowDown /></el-icon>
            </button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon> 个人资料
                </el-dropdown-item>
                <el-dropdown-item v-if="userStore.userRole === 1" command="merchant">
                  <el-icon><Shop /></el-icon> 商家面板
                </el-dropdown-item>
                <el-dropdown-item v-if="userStore.userRole === 2" command="admin">
                  <el-icon><Setting /></el-icon> 管理面板
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { HomeFilled, Document, Bell, UserFilled, User, Shop, Setting, ArrowDown, SwitchButton } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

function handleCommand(cmd) {
  if (cmd === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (cmd === 'profile') {
    router.push('/profile')
  } else if (cmd === 'merchant') {
    router.push('/merchant')
  } else if (cmd === 'admin') {
    router.push('/admin')
  }
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background: var(--bg-main);
}

.header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid var(--border);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 48px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  text-decoration: none;
}

.logo svg {
  width: 32px;
  height: 32px;
  color: var(--primary);
}

.logo span {
  font-size: 22px;
  font-weight: 800;
  background: linear-gradient(135deg, var(--primary), var(--accent));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  font-weight: 500;
  font-size: 14px;
  transition: all 0.2s ease;
  text-decoration: none;
}

.nav-item:hover {
  background: rgba(99, 102, 241, 0.08);
  color: var(--primary);
}

.nav-item.active {
  background: rgba(99, 102, 241, 0.12);
  color: var(--primary);
}

.nav-item .el-icon {
  font-size: 18px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 14px 6px 6px;
  background: rgba(255, 255, 255, 0.8);
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

.username {
  font-weight: 500;
  color: var(--text-primary);
  font-size: 14px;
}

.arrow {
  font-size: 12px;
  color: var(--text-muted);
}

.main {
  min-height: calc(100vh - 72px);
}
</style>
