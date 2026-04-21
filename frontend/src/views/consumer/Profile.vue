<template>
  <div class="page-container">
    <h2 class="page-title">个人资料</h2>
    <el-row :gutter="24">
      <el-col :span="12">
        <div class="card form-card">
          <h3 class="card-title">基本信息</h3>
          <el-form :model="profileForm" label-width="100px" size="large">
            <el-form-item label="用户名">
              <el-input v-model="profileForm.username" disabled />
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="profileForm.nickname" placeholder="输入昵称" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone" placeholder="输入手机号" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email" placeholder="输入邮箱" />
            </el-form-item>
            <el-form-item>
              <button class="btn btn-accent" @click="handleUpdateProfile" :loading="profileLoading">保存</button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card form-card">
          <h3 class="card-title">修改密码</h3>
          <el-form :model="pwdForm" label-width="100px" size="large">
            <el-form-item label="当前密码">
              <el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="当前密码" />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="新密码" />
            </el-form-item>
            <el-form-item>
              <button class="btn btn-accent" @click="handleChangePassword" :loading="pwdLoading">修改</button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { updateProfile, changePassword } from '../../api/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const profileLoading = ref(false)
const pwdLoading = ref(false)

const profileForm = reactive({ username: '', nickname: '', phone: '', email: '' })
const pwdForm = reactive({ oldPassword: '', newPassword: '' })

onMounted(() => {
  const u = userStore.user
  if (u) {
    profileForm.username = u.username || ''
    profileForm.nickname = u.nickname || ''
    profileForm.phone = u.phone || ''
    profileForm.email = u.email || ''
  }
})

async function handleUpdateProfile() {
  profileLoading.value = true
  try {
    await updateProfile(profileForm)
    ElMessage.success('已保存')
    userStore.fetchCurrentUser()
  } finally {
    profileLoading.value = false
  }
}

async function handleChangePassword() {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) {
    ElMessage.warning('请填写所有字段')
    return
  }
  pwdLoading.value = true
  try {
    await changePassword(pwdForm)
    ElMessage.success('已修改，请重新登录')
    userStore.logout()
  } finally {
    pwdLoading.value = false
  }
}
</script>

<style scoped>
.form-card {
  padding: 24px;
}
.card-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 24px;
  color: var(--text-primary);
}
</style>