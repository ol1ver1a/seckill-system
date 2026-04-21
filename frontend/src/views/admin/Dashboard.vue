<template>
  <div>
    <h2 class="page-title">控制台</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #6366f1, #818cf8);">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-value">{{ stats.users }}</div>
          <div class="stat-label">用户总数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #10b981, #34d399);">
            <el-icon><Goods /></el-icon>
          </div>
          <div class="stat-value">{{ stats.goods }}</div>
          <div class="stat-label">商品总数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #f59e0b, #fbbf24);">
            <el-icon><Timer /></el-icon>
          </div>
          <div class="stat-value">{{ stats.activities }}</div>
          <div class="stat-label">秒杀活动</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #f43f5e, #fb7185);">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-value">{{ stats.orders }}</div>
          <div class="stat-label">订单总数</div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { listUsers } from '../../api/admin'
import { listGoods } from '../../api/goods'
import { listActivities, listOrders } from '../../api/seckill'
import { User, Goods, Timer, Document } from '@element-plus/icons-vue'

const stats = reactive({ users: 0, goods: 0, activities: 0, orders: 0 })

onMounted(async () => {
  try {
    const [u, g, a, o] = await Promise.all([
      listUsers({ page: 1, size: 1 }),
      listGoods({ page: 1, size: 1 }),
      listActivities({ page: 1, size: 1 }),
      listOrders({ page: 1, size: 1 })
    ])
    stats.users = u.data?.length || 0
    stats.goods = g.data?.length || 0
    stats.activities = a.data?.length || 0
    stats.orders = o.data?.length || 0
  } catch {}
})
</script>

<style scoped>
</style>
