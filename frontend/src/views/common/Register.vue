<template>
  <div class="login-page">
    <div class="bg-animation">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
      <div class="blob blob-3"></div>
    </div>
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M13 10V3L4 14h7v7l9-11h-7z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <h1>注册账号</h1>
          <p>创建您的秒杀商城账号</p>
        </div>
        <el-form :model="form" :rules="rules" ref="formRef" size="large" class="login-form">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名">
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password>
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" placeholder="请确认密码" show-password>
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="nickname">
            <el-input v-model="form.nickname" placeholder="请输入昵称">
              <template #prefix><el-icon><UserFilled /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="phone">
            <el-input v-model="form.phone" placeholder="请输入手机号">
              <template #prefix><el-icon><Phone /></el-icon></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="role">
            <el-select v-model="form.role" placeholder="请选择角色" style="width:100%">
              <el-option label="消费者" :value="0">
                <div class="role-option">
                  <el-icon><ShoppingCart /></el-icon>
                  <span>消费者</span>
                </div>
              </el-option>
              <el-option label="商家" :value="1">
                <div class="role-option">
                  <el-icon><Shop /></el-icon>
                  <span>商家</span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <button type="button" class="login-btn" :class="{ loading }" :disabled="loading" @click="handleRegister">
              <span v-if="!loading">注 册</span>
              <span v-else class="loader"></span>
            </button>
          </el-form-item>
        </el-form>
        <div class="login-footer">
          <span>已有账号？</span>
          <router-link to="/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock, UserFilled, Phone, ShoppingCart, Shop } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  phone: '',
  role: 0
})

const validateConfirm = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' }
  ],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

async function handleRegister() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await userStore.register(form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch {
    // error handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #0f0f1a;
  position: relative;
  overflow: hidden;
}

.bg-animation {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.6;
  animation: float 20s ease-in-out infinite;
}

.blob-1 {
  width: 500px;
  height: 500px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  top: -10%;
  right: -10%;
}

.blob-2 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #f43f5e, #f97316);
  bottom: -10%;
  left: -10%;
  animation-delay: -7s;
}

.blob-3 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #10b981, #06b6d4);
  top: 30%;
  left: 60%;
  animation-delay: -14s;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(30px, -30px) scale(1.05); }
  50% { transform: translate(-20px, 20px) scale(0.95); }
  75% { transform: translate(20px, 30px) scale(1.02); }
}

.login-container {
  position: relative;
  z-index: 10;
}

.login-card {
  width: 440px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  padding: 40px 36px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  width: 56px;
  height: 56px;
  margin: 0 auto 16px;
  background: linear-gradient(135deg, #6366f1, #f43f5e);
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10px 30px rgba(99, 102, 241, 0.4);
}

.logo svg {
  width: 28px;
  height: 28px;
  color: white;
}

.login-header h1 {
  font-size: 26px;
  font-weight: 800;
  background: linear-gradient(135deg, #fff, #94a3b8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 6px;
}

.login-header p {
  color: #64748b;
  font-size: 14px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 16px;
}

.login-form :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.08) !important;
  border: 1px solid rgba(255, 255, 255, 0.15) !important;
  border-radius: 12px !important;
  padding: 4px 16px !important;
  box-shadow: none !important;
  transition: all 0.3s ease !important;
  height: 44px !important;
}

.login-form :deep(.el-input__wrapper:hover),
.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #6366f1 !important;
  background: rgba(99, 102, 241, 0.1) !important;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.2) !important;
}

.login-form :deep(.el-input__inner) {
  color: #fff !important;
  font-size: 14px !important;
}

.login-form :deep(.el-input__inner::placeholder) {
  color: #64748b !important;
}

.login-form :deep(.el-input__prefix .el-icon) {
  color: #64748b;
}

.login-form :deep(.el-select__wrapper) {
  background: rgba(255, 255, 255, 0.08) !important;
  border: 1px solid rgba(255, 255, 255, 0.15) !important;
  border-radius: 12px !important;
  box-shadow: none !important;
  padding: 4px 12px !important;
  min-height: 44px !important;
}

.login-form :deep(.el-select__placeholder) {
  color: #64748b !important;
}

.role-option {
  display: flex;
  align-items: center;
  gap: 10px;
}

.login-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  border: none;
  border-radius: 12px;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
  height: 48px;
}

.login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(99, 102, 241, 0.5);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loader {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255,255,255,0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  color: #64748b;
  font-size: 14px;
}

.login-footer a {
  color: #6366f1;
  font-weight: 500;
  margin-left: 4px;
}

.login-footer a:hover {
  color: #818cf8;
}
</style>
