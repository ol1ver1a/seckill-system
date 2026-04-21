<template>
  <div>
    <h2 class="page-title" style="display:flex;justify-content:space-between;align-items:center">
      <span>通知管理</span>
      <button class="btn btn-accent" @click="openForm()">发布</button>
    </h2>
    <div class="card">
      <div class="filter-bar">
        <el-input v-model="query.keyword" placeholder="搜索通知" style="width:200px" clearable />
        <el-select v-model="query.status" placeholder="状态" clearable style="width:120px;margin-left:12px">
          <el-option label="草稿" :value="0" />
          <el-option label="已发布" :value="1" />
        </el-select>
        <button class="btn btn-primary" style="margin-left:12px" @click="load">搜索</button>
      </div>
      <el-table :data="list" stripe v-loading="loading" class="modern-table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <span class="tag" :class="row.status === 1 ? 'tag-success' : 'tag-muted'">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <button class="btn btn-sm btn-outline" @click="openForm(row)">编辑</button>
            <button class="btn btn-sm" :class="row.status === 0 ? 'btn-outline-success' : 'btn-outline'" style="margin-left:4px" @click="togglePublish(row)">
              {{ row.status === 0 ? '发布' : '撤回' }}
            </button>
            <button class="btn btn-sm btn-accent" style="margin-left:4px" @click="handleDelete(row)">删除</button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="formVisible" :title="form.id ? '编辑通知' : '发布通知'" width="600px">
      <el-form :model="form" label-width="80px" size="large">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="0">草稿</el-radio>
            <el-radio :value="1">发布</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="btn btn-outline" @click="formVisible = false">取消</button>
        <button class="btn btn-accent" :loading="formLoading" @click="handleSubmit">确认</button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { listNotices, addNotice, updateNotice, deleteNotice } from '../../api/notice'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const loading = ref(false)
const query = reactive({ keyword: '', status: undefined })

const formVisible = ref(false)
const formLoading = ref(false)
const form = reactive({ id: null, title: '', content: '', status: 0 })

function openForm(row) {
  if (row) {
    Object.assign(form, { id: row.id, title: row.title, content: row.content, status: row.status })
  } else {
    Object.assign(form, { id: null, title: '', content: '', status: 0 })
  }
  formVisible.value = true
}

async function load() {
  loading.value = true
  try {
    const res = await listNotices({ ...query, page: 1, size: 50 })
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  formLoading.value = true
  try {
    if (form.id) {
      await updateNotice(form)
    } else {
      await addNotice(form)
    }
    ElMessage.success('操作成功')
    formVisible.value = false
    load()
  } finally {
    formLoading.value = false
  }
}

async function togglePublish(row) {
  await updateNotice({ id: row.id, title: row.title, content: row.content, status: row.status === 0 ? 1 : 0 })
  ElMessage.success(row.status === 0 ? '已发布' : '已撤回')
  load()
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('删除此通知？', '确认', { type: 'warning' })
    await deleteNotice(row.id)
    ElMessage.success('已删除')
    load()
  } catch {}
}

onMounted(load)
</script>

<style scoped>
.filter-bar { display: flex; margin-bottom: 20px; gap: 12px; flex-wrap: wrap; align-items: center; }
.btn-sm { padding: 6px 12px; font-size: 12px; }
.btn-outline { background: transparent; border: 1px solid var(--border); color: var(--text-primary); }
.btn-outline:hover { border-color: var(--primary); color: var(--primary); }
.btn-outline-success { background: transparent; border: 1px solid var(--success); color: var(--success); }
.btn-outline-success:hover { background: var(--success); color: white; }
</style>
