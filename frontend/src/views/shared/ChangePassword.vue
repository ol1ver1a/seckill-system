<template>
  <div>
    <h2 class="page-title">修改密码</h2>
    <div class="card form-card">
      <el-form :model="form" label-width="120px" size="large">
        <el-form-item label="当前密码">
          <el-input v-model="form.oldPassword" type="password" show-password placeholder="输入当前密码" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="form.newPassword" type="password" show-password placeholder="输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="form.confirmPassword" type="password" show-password placeholder="确认新密码" />
        </el-form-item>
        <el-form-item>
          <button class="btn btn-accent" :loading="loading" @click="handleSubmit">确认</button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { changePassword } from '../../api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const form = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

async function handleSubmit() {
  if (!form.oldPassword || !form.newPassword || !form.confirmPassword) {
    ElMessage.warning('请填写所有字段')
    return
  }
  if (form.newPassword !== form.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  if (form.newPassword.length < 6) {
    ElMessage.error('密码至少6个字符')
    return
  }
  loading.value = true
  try {
    await changePassword({ oldPassword: form.oldPassword, newPassword: form.newPassword })
    ElMessage.success('已修改，请重新登录')
    userStore.logout()
    router.push('/login')
  } catch {
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.form-card { max-width: 500px; }
</style>
