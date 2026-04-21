<template>
  <div class="page-container">
    <h2 class="page-title">我的订单</h2>
    <div class="card">
      <div class="filter-bar">
        <el-select v-model="query.status" placeholder="状态" clearable style="width:160px" @change="loadOrders">
          <el-option label="待支付" :value="0" />
          <el-option label="已支付" :value="1" />
          <el-option label="已发货" :value="2" />
          <el-option label="已完成" :value="3" />
          <el-option label="已取消" :value="4" />
          <el-option label="已退款" :value="5" />
        </el-select>
      </div>
      <el-table :data="orders" stripe v-loading="loading" class="modern-table">
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column prop="goodsName" label="商品" min-width="160" />
        <el-table-column label="价格" width="120">
          <template #default="{ row }">
            <span class="price-normal">&yen;{{ row.goodsPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <span class="tag" :class="statusType(row.status)">{{ statusText(row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <button v-if="row.status === 0" class="btn btn-sm btn-accent" @click="handlePay(row)">支付</button>
            <button v-if="row.status === 0" class="btn btn-sm btn-outline" style="margin-left:4px" @click="handleCancel(row)">取消</button>
            <button v-if="row.status === 2" class="btn btn-sm btn-primary" @click="handleComplete(row)">收货</button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { myOrders, payOrder, cancelOrder, completeOrder } from '../../api/seckill'
import { ElMessage, ElMessageBox } from 'element-plus'

const orders = ref([])
const loading = ref(false)
const query = reactive({ status: undefined })

const statusMap = { 0: '待支付', 1: '已支付', 2: '已发货', 3: '已完成', 4: '已取消', 5: '已退款' }
const statusTypeMap = { 0: 'tag-warning', 1: 'tag-success', 2: 'tag-primary', 3: 'tag-success', 4: 'tag-muted', 5: 'tag-danger' }

function statusText(s) { return statusMap[s] || '未知' }
function statusType(s) { return statusTypeMap[s] || 'tag-muted' }

async function loadOrders() {
  loading.value = true
  try {
    const res = await myOrders({ status: query.status, page: 1, size: 50 })
    orders.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function handlePay(row) {
  try {
    await ElMessageBox.confirm('确认支付？', '确认')
    await payOrder(row.id)
    ElMessage.success('已支付')
    loadOrders()
  } catch {}
}

async function handleCancel(row) {
  try {
    await ElMessageBox.confirm('取消该订单？', '确认', { type: 'warning' })
    await cancelOrder(row.id)
    ElMessage.success('已取消')
    loadOrders()
  } catch {}
}

async function handleComplete(row) {
  try {
    await completeOrder(row.id)
    ElMessage.success('已收货')
    loadOrders()
  } catch {}
}

onMounted(loadOrders)
</script>

<style scoped>
.filter-bar {
  margin-bottom: 20px;
}
.btn-sm { padding: 6px 12px; font-size: 12px; }
.btn-outline { background: transparent; border: 1px solid var(--border); color: var(--text-primary); }
.btn-outline:hover { border-color: var(--primary); color: var(--primary); }
</style>