<template>
  <div class="page-container">
    <div v-if="activity" class="card seckill-detail">
      <el-row :gutter="40">
        <el-col :span="10">
          <div class="detail-img">
            <el-image v-if="activity.goodsImg" :src="activity.goodsImg" fit="cover" style="width:100%;height:400px;border-radius:var(--radius-lg)" />
            <div v-else class="img-placeholder">
              <el-icon :size="80"><Timer /></el-icon>
            </div>
          </div>
        </el-col>
        <el-col :span="14">
          <h1 class="detail-title">{{ activity.goodsName || activity.activityName }}</h1>
          <p class="detail-subtitle">{{ activity.activityName }}</p>

          <div class="price-box">
            <span class="label">秒杀价</span>
            <span class="seckill-price">&yen;{{ activity.seckillPrice }}</span>
            <span class="original-price">&yen;{{ activity.goodsPrice }}</span>
            <span class="tag tag-danger">限量</span>
          </div>

          <div class="info-row">
            <span class="label">时间</span>
            <span>{{ formatTime(activity.startTime) }} ~ {{ formatTime(activity.endTime) }}</span>
          </div>

          <div class="info-row">
            <span class="label">剩余库存</span>
            <el-progress :percentage="stockPercent" :color="stockPercent < 20 ? '#f43f5e' : '#10b981'" :stroke-width="18" :text-inside="true" style="width:200px" />
          </div>

          <div class="info-row">
            <span class="label">限购</span>
            <span>{{ activity.limitCount }} 件/人</span>
          </div>

          <div class="countdown-section">
            <template v-if="status === 'waiting'">
              <p class="countdown-label">距离开始</p>
              <div class="countdown-box">
                <span class="time-block">{{ countdown.h }}</span>
                <span class="time-sep">:</span>
                <span class="time-block">{{ countdown.m }}</span>
                <span class="time-sep">:</span>
                <span class="time-block">{{ countdown.s }}</span>
              </div>
              <button class="btn btn-outline btn-lg" style="margin-top:20px;width:240px" disabled>
                未开始
              </button>
            </template>
            <template v-else-if="status === 'active'">
              <p class="countdown-label-active">立即抢购</p>
              <div class="countdown-box">
                <span class="time-block">{{ countdown.h }}</span>
                <span class="time-sep">:</span>
                <span class="time-block">{{ countdown.m }}</span>
                <span class="time-sep">:</span>
                <span class="time-block">{{ countdown.s }}</span>
              </div>
              <button class="btn btn-accent btn-lg seckill-btn-animate" style="margin-top:20px;width:240px;font-size:18px" :loading="seckillLoading" @click="handleSeckill">
                立即购买
              </button>
            </template>
            <template v-else>
              <p class="countdown-label">已结束</p>
              <button class="btn btn-outline btn-lg" style="margin-top:20px;width:240px" disabled>
                已结束
              </button>
            </template>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getActivityDetail, executeSeckill } from '../../api/seckill'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Timer } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const activity = ref(null)
const seckillLoading = ref(false)
const countdown = reactive({ h: '00', m: '00', s: '00' })
let timer = null

const stockPercent = computed(() => {
  if (!activity.value) return 0
  return Math.max(0, Math.min(100, (activity.value.stockCount / (activity.value.stockCount + 10)) * 100))
})

const status = computed(() => {
  if (!activity.value) return 'ended'
  const now = new Date().getTime()
  const start = new Date(activity.value.startTime).getTime()
  const end = new Date(activity.value.endTime).getTime()
  if (now < start) return 'waiting'
  if (now > end) return 'ended'
  return 'active'
})

function updateCountdown() {
  if (!activity.value) return
  const now = new Date().getTime()
  const start = new Date(activity.value.startTime).getTime()
  const end = new Date(activity.value.endTime).getTime()
  let target = status.value === 'waiting' ? start : end
  let diff = Math.max(0, Math.floor((target - now) / 1000))
  countdown.h = String(Math.floor(diff / 3600)).padStart(2, '0')
  countdown.m = String(Math.floor((diff % 3600) / 60)).padStart(2, '0')
  countdown.s = String(diff % 60).padStart(2, '0')
}

function formatTime(t) {
  if (!t) return ''
  return new Date(t).toLocaleString('zh-CN')
}

async function handleSeckill() {
  try {
    await ElMessageBox.confirm('确认购买？', '确认', { type: 'warning' })
    seckillLoading.value = true
    const res = await executeSeckill(activity.value.id)
    ElMessage.success(res.data || '订单已提交，请查看订单')
    router.push('/orders')
  } catch {
  } finally {
    seckillLoading.value = false
  }
}

onMounted(async () => {
  try {
    const res = await getActivityDetail(route.params.id)
    activity.value = res.data
    updateCountdown()
    timer = setInterval(updateCountdown, 1000)
  } catch {}
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.seckill-detail {
  padding: 32px;
}

.img-placeholder {
  width: 100%;
  height: 400px;
  background: linear-gradient(135deg, #fff1f2, #ffe4e6);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent);
}

.detail-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
  color: var(--text-primary);
}

.detail-subtitle {
  color: var(--text-secondary);
  font-size: 14px;
  margin-bottom: 24px;
}

.price-box, .info-row, .countdown-section {
  margin-bottom: 16px;
}

.price-box, .info-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.label {
  color: var(--text-muted);
  font-size: 14px;
  min-width: 80px;
}

.countdown-section {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid var(--border);
}

.countdown-label {
  font-size: 14px;
  color: var(--text-muted);
  margin-bottom: 8px;
}

.countdown-label-active {
  color: var(--accent);
  font-weight: 600;
  font-size: 16px;
}

.btn-lg { padding: 14px 32px; font-size: 16px; }
</style>