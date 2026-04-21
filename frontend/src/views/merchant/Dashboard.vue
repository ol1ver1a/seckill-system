<template>
  <div>
    <h2 class="page-title">控制台</h2>
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #f43f5e, #fb7185);">
            <el-icon><Goods /></el-icon>
          </div>
          <div class="stat-value">{{ stats.goodsCount }}</div>
          <div class="stat-label">商品总数</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #f59e0b, #fbbf24);">
            <el-icon><Timer /></el-icon>
          </div>
          <div class="stat-value">{{ stats.activityCount }}</div>
          <div class="stat-label">秒杀活动</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #10b981, #34d399);">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-value">{{ stats.orderCount }}</div>
          <div class="stat-label">订单数</div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { listGoods } from '../../api/goods'
import { listActivities } from '../../api/seckill'
import { listOrders } from '../../api/seckill'
import { useUserStore } from '../../stores/user'
import { Goods, Timer, Document } from '@element-plus/icons-vue'

const userStore = useUserStore()
const stats = reactive({ goodsCount: 0, activityCount: 0, orderCount: 0 })

onMounted(async () => {
  try {
    const [g, a, o] = await Promise.all([
      listGoods({ merchantId: userStore.user?.id, page: 1, size: 1 }),
      listActivities({ merchantId: userStore.user?.id, page: 1, size: 1 }),
      listOrders({ page: 1, size: 1 })
    ])
    stats.goodsCount = g.data?.length || 0
    stats.activityCount = a.data?.length || 0
    stats.orderCount = o.data?.length || 0
  } catch {}
})
</script>

<style scoped>
</style>
