<template>
  <div>
    <h2 class="page-title" style="display:flex;justify-content:space-between;align-items:center">
      <span>秒杀活动</span>
      <button class="btn btn-accent" @click="$router.push('/merchant/activities/add')">创建</button>
    </h2>
    <div class="card">
      <div class="filter-bar">
        <el-select v-model="query.status" placeholder="状态" clearable style="width:140px" @change="load">
          <el-option label="待审核" :value="0" />
          <el-option label="进行中" :value="1" />
          <el-option label="已结束" :value="2" />
          <el-option label="已通过" :value="3" />
          <el-option label="已拒绝" :value="4" />
        </el-select>
        <button class="btn btn-primary" style="margin-left:12px" @click="load">搜索</button>
      </div>
      <el-table :data="list" stripe v-loading="loading" class="modern-table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="activityName" label="活动名称" min-width="140" />
        <el-table-column prop="goodsName" label="商品" min-width="120" />
        <el-table-column label="价格" width="100">
          <template #default="{ row }"><span class="seckill-price">&yen;{{ row.seckillPrice }}</span></template>
        </el-table-column>
        <el-table-column prop="stockCount" label="库存" width="80" />
        <el-table-column label="时间" width="180">
          <template #default="{ row }">{{ formatTime(row.startTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <span class="tag" :class="activityStatusType(row.status)">{{ activityStatusText(row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <button class="btn btn-sm btn-outline" @click="$router.push(`/merchant/activities/edit/${row.id}`)">编辑</button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { listActivities } from '../../api/seckill'
import { useUserStore } from '../../stores/user'

const userStore = useUserStore()
const list = ref([])
const loading = ref(false)
const query = reactive({ status: undefined })

const statusMap = { 0: '待审核', 1: '进行中', 2: '已结束', 3: '已通过', 4: '已拒绝' }
const typeMap = { 0: 'tag-warning', 1: 'tag-danger', 2: 'tag-muted', 3: 'tag-success', 4: 'tag-primary' }
function activityStatusText(s) { return statusMap[s] || '未知' }
function activityStatusType(s) { return typeMap[s] || 'tag-muted' }
function formatTime(t) { return t ? new Date(t).toLocaleString('zh-CN') : '' }

async function load() {
  loading.value = true
  try {
    const res = await listActivities({ merchantId: userStore.user?.id, ...query, page: 1, size: 50 })
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.filter-bar { display: flex; margin-bottom: 20px; gap: 12px; flex-wrap: wrap; align-items: center; }
.btn-sm { padding: 6px 12px; font-size: 12px; }
.btn-outline { background: transparent; border: 1px solid var(--border); color: var(--text-primary); }
.btn-outline:hover { border-color: var(--primary); color: var(--primary); }
</style>
