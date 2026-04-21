<template>
  <div>
    <h2 class="page-title">秒杀活动审核</h2>
    <div class="card">
      <div class="filter-bar">
        <el-select v-model="query.status" placeholder="状态" clearable style="width:140px" @change="load">
          <el-option label="待审核(0)" :value="0" />
          <el-option label="进行中(1)" :value="1" />
          <el-option label="已结束(2)" :value="2" />
          <el-option label="已通过(3)" :value="3" />
          <el-option label="已拒绝(4)" :value="4" />
        </el-select>
        <button class="btn btn-primary" style="margin-left:12px" @click="load">搜索</button>
      </div>
      <el-table :data="list" stripe v-loading="loading" class="modern-table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="activityName" label="活动名称" min-width="140" />
        <el-table-column prop="goodsName" label="商品" min-width="120" />
        <el-table-column label="秒杀价" width="100">
          <template #default="{ row }"><span class="seckill-price">&yen;{{ row.seckillPrice }}</span></template>
        </el-table-column>
        <el-table-column prop="stockCount" label="库存" width="80" />
        <el-table-column label="时间" width="180">
          <template #default="{ row }">{{ formatTime(row.startTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <span class="tag" :class="actStatusType(row.status)">{{ actStatusText(row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <button v-if="row.status === 0" class="btn btn-sm btn-primary" @click="handleAudit(row, 3)">通过</button>
            <button v-if="row.status === 0" class="btn btn-sm btn-accent" style="margin-left:4px" @click="handleAudit(row, 4)">拒绝</button>
            <button v-if="row.status === 1" class="btn btn-sm btn-outline" @click="handleStop(row)">停止</button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { listActivities, auditActivity, updateActivityStatus } from '../../api/seckill'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const loading = ref(false)
const query = reactive({ status: undefined })

const statusMap = { 0: '待审核', 1: '进行中', 2: '已结束', 3: '已通过', 4: '已拒绝' }
const typeMap = { 0: 'tag-warning', 1: 'tag-danger', 2: 'tag-muted', 3: 'tag-success', 4: 'tag-primary' }
function actStatusText(s) { return statusMap[s] || '未知' }
function actStatusType(s) { return typeMap[s] || 'tag-muted' }
function formatTime(t) { return t ? new Date(t).toLocaleString('zh-CN') : '' }

async function load() {
  loading.value = true
  try {
    const res = await listActivities({ ...query, page: 1, size: 50 })
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function handleAudit(row, status) {
  try {
    await ElMessageBox.confirm(
      status === 3 ? `通过活动"${row.activityName}"？` : `拒绝活动"${row.activityName}"？`,
      '确认'
    )
    await auditActivity(row.id, status)
    ElMessage.success('操作成功')
    load()
  } catch {}
}

async function handleStop(row) {
  try {
    await ElMessageBox.confirm(`停止活动"${row.activityName}"？`, '确认', { type: 'warning' })
    await updateActivityStatus(row.id, 2)
    ElMessage.success('已停止')
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
</style>
