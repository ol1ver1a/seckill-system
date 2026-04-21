<template>
  <div>
    <h2 class="page-title">用户管理</h2>
    <div class="card">
      <div class="filter-bar">
        <el-input v-model="query.keyword" placeholder="搜索用户" style="width:200px" clearable />
        <el-select v-model="query.role" placeholder="角色" clearable style="width:140px;margin-left:12px">
          <el-option label="消费者" :value="0" />
          <el-option label="商家" :value="1" />
        </el-select>
        <button class="btn btn-primary" style="margin-left:12px" @click="load">搜索</button>
      </div>
      <el-table :data="list" stripe v-loading="loading" class="modern-table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column label="角色" width="100">
          <template #default="{ row }">
            <span class="tag" :class="row.role === 0 ? 'tag-muted' : row.role === 1 ? 'tag-warning' : 'tag-danger'">
              {{ roleText(row.role) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <span class="tag" :class="row.status === 1 ? 'tag-success' : 'tag-danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <button class="btn btn-sm" :class="row.status === 1 ? 'btn-outline' : 'btn-outline-success'" @click="toggleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </button>
            <button class="btn btn-sm btn-outline-warning" @click="handleResetPwd(row)">重置</button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { listUsers, updateUserStatus, resetPassword } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const loading = ref(false)
const query = reactive({ keyword: '', role: undefined })

function roleText(r) { return { 0: '消费者', 1: '商家', 2: '管理员' }[r] || '未知' }

async function load() {
  loading.value = true
  try {
    const res = await listUsers({ ...query, page: 1, size: 50 })
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function toggleStatus(row) {
  await updateUserStatus(row.id, row.status === 1 ? 0 : 1)
  ElMessage.success('操作成功')
  load()
}

async function handleResetPwd(row) {
  try {
    await ElMessageBox.confirm(`确认重置用户 ${row.username} 的密码？`, '提示', { type: 'warning' })
    await resetPassword(row.id)
    ElMessage.success('密码已重置为 123456')
  } catch {}
}

onMounted(load)
</script>

<style scoped>
.filter-bar { display: flex; margin-bottom: 20px; gap: 12px; flex-wrap: wrap; align-items: center; }
.btn-sm { padding: 6px 12px; font-size: 12px; }
.btn-outline { background: transparent; border: 1px solid var(--border); color: var(--danger); border-color: var(--danger); }
.btn-outline:hover { background: var(--danger); color: white; }
.btn-outline-success { background: transparent; border: 1px solid var(--success); color: var(--success); }
.btn-outline-success:hover { background: var(--success); color: white; }
.btn-outline-warning { background: transparent; border: 1px solid var(--warning); color: var(--warning); }
.btn-outline-warning:hover { background: var(--warning); color: white; }
</style>
