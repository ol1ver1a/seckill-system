<template>
  <div>
    <h2 class="page-title">订单管理</h2>
    <div class="card">
      <div class="filter-bar">
        <el-input v-model="query.orderNo" placeholder="搜索订单号" style="width:200px" clearable />
        <el-select v-model="query.status" placeholder="状态" clearable style="width:140px;margin-left:12px">
          <el-option label="待支付" :value="0" />
          <el-option label="已支付" :value="1" />
          <el-option label="已发货" :value="2" />
          <el-option label="已完成" :value="3" />
          <el-option label="已取消" :value="4" />
        </el-select>
        <button class="btn btn-primary" style="margin-left:12px" @click="load">搜索</button>
      </div>
      <el-table :data="list" stripe v-loading="loading" class="modern-table">
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column prop="username" label="用户" width="120" />
        <el-table-column prop="goodsName" label="商品" min-width="140" />
        <el-table-column label="价格" width="100">
          <template #default="{ row }"><span class="price-normal">&yen;{{ row.goodsPrice }}</span></template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <span class="tag" :class="statusType(row.status)">{{ statusText(row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { listOrders } from '../../api/seckill'

const list = ref([])
const loading = ref(false)
const query = reactive({ orderNo: '', status: undefined })

const statusMap = { 0: '待支付', 1: '已支付', 2: '已发货', 3: '已完成', 4: '已取消', 5: '已退款' }
const typeMap = { 0: 'tag-warning', 1: 'tag-success', 2: 'tag-primary', 3: 'tag-success', 4: 'tag-muted', 5: 'tag-danger' }
function statusText(s) { return statusMap[s] || '未知' }
function statusType(s) { return typeMap[s] || 'tag-muted' }

async function load() {
  loading.value = true
  try {
    const res = await listOrders({ ...query, page: 1, size: 50 })
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.filter-bar { display: flex; margin-bottom: 20px; gap: 12px; flex-wrap: wrap; align-items: center; }
</style>
