<template>
  <div class="home">
    <!-- Hero Banner -->
    <div class="banner">
      <div class="banner-content">
        <div class="banner-text">
          <h1>限时秒杀</h1>
          <p>优质商品，超值优惠</p>
          <div class="banner-stats">
            <div class="stat">
              <span class="stat-value">500+</span>
              <span class="stat-label">商品</span>
            </div>
            <div class="stat">
              <span class="stat-value">50K+</span>
              <span class="stat-label">用户</span>
            </div>
            <div class="stat">
              <span class="stat-value">99%</span>
              <span class="stat-label">满意度</span>
            </div>
          </div>
        </div>
        <div class="banner-visual">
          <div class="floating-card card-1">
            <el-icon><Lightning /></el-icon>
            <span>最高5折优惠</span>
          </div>
          <div class="floating-card card-2">
            <el-icon><Trophy /></el-icon>
            <span>优质品质</span>
          </div>
        </div>
      </div>
    </div>

    <div class="page-container">
      <!-- Flash Sales Section -->
      <section class="section animate-fadeInUp">
        <div class="section-header">
          <div class="section-title">
            <el-icon class="title-icon"><Lightning /></el-icon>
            <h2>限时秒杀</h2>
            <el-tag type="danger" effect="dark" round>进行中</el-tag>
          </div>
        </div>
        <div v-if="activities.length === 0" class="empty-state">
          <el-icon :size="48"><ShoppingCart /></el-icon>
          <p>暂无进行中的秒杀活动</p>
        </div>
        <div v-else class="goods-grid">
          <div
            v-for="(item, index) in activities"
            :key="item.id"
            class="goods-card"
            :style="{ animationDelay: `${index * 0.1}s` }"
            @click="$router.push(`/seckill/${item.id}`)"
          >
            <div class="goods-img">
              <el-icon :size="48"><Goods /></el-icon>
              <div class="seckill-badge">
                <el-icon><Lightning /></el-icon>
                SALE
              </div>
            </div>
            <div class="goods-info">
              <h3 class="goods-name">{{ item.goodsName || item.activityName }}</h3>
              <div class="goods-price">
                <span class="seckill-price">¥{{ item.seckillPrice }}</span>
                <span class="original-price">¥{{ item.goodsPrice }}</span>
              </div>
              <div class="goods-meta">
                <span class="stock">
                  <el-icon><Box /></el-icon>
                  {{ item.stockCount }} 件剩余
                </span>
                <span class="tag tag-danger">限量</span>
              </div>
              <button class="buy-btn">
                <el-icon><ShoppingCart /></el-icon>
                立即购买
              </button>
            </div>
          </div>
        </div>
      </section>

      <!-- All Products Section -->
      <section class="section animate-fadeInUp" style="animation-delay: 0.2s">
        <div class="section-header">
          <div class="section-title">
            <el-icon class="title-icon"><ShoppingBag /></el-icon>
            <h2>全部商品</h2>
          </div>
        </div>
        <div v-if="goods.length === 0" class="empty-state">
          <el-icon :size="48"><Goods /></el-icon>
          <p>暂无商品</p>
        </div>
        <div v-else class="goods-grid">
          <div
            v-for="(item, index) in goods"
            :key="item.id"
            class="goods-card"
            :style="{ animationDelay: `${(activities.length + index) * 0.1}s` }"
            @click="$router.push(`/goods/${item.id}`)"
          >
            <div class="goods-img">
              <el-icon :size="48"><Goods /></el-icon>
              <div v-if="item.seckillPrice" class="seckill-badge sale">
                <el-icon><Discount /></el-icon>
                SALE
              </div>
            </div>
            <div class="goods-info">
              <h3 class="goods-name">{{ item.goodsName }}</h3>
              <div class="goods-price">
                <span v-if="item.seckillPrice" class="seckill-price">¥{{ item.seckillPrice }}</span>
                <span class="price-normal">¥{{ item.seckillPrice || item.goodsPrice }}</span>
              </div>
              <div class="goods-meta">
                <span class="category-tag">
                  <el-icon><Collection /></el-icon>
                  {{ item.category || '普通' }}
                </span>
              </div>
              <button class="buy-btn secondary">
                <el-icon><View /></el-icon>
                查看详情
              </button>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listActiveActivities } from '../../api/seckill'
import { listOnSale } from '../../api/goods'
import { Lightning, Trophy, ShoppingCart, Goods, Box, ShoppingBag, Discount, Collection, View } from '@element-plus/icons-vue'

const activities = ref([])
const goods = ref([])

onMounted(async () => {
  try {
    const [actRes, goodsRes] = await Promise.all([
      listActiveActivities(),
      listOnSale()
    ])
    activities.value = actRes.data || []
    goods.value = goodsRes.data || []
  } catch {
    // handled by interceptor
  }
})
</script>

<style scoped>
.home {
  background: var(--bg-main);
}

/* Banner */
.banner {
  background: linear-gradient(135deg, #0f0f1a 0%, #1a1a2e 50%, #16213e 100%);
  padding: 80px 24px;
  position: relative;
  overflow: hidden;
}

.banner::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 50%;
  height: 100%;
  background: radial-gradient(circle at 70% 50%, rgba(99, 102, 241, 0.3), transparent 60%);
  pointer-events: none;
}

.banner-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
  z-index: 1;
}

.banner-text h1 {
  font-size: 56px;
  font-weight: 800;
  background: linear-gradient(135deg, #fff 0%, #a5b4fc 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 16px;
  line-height: 1.1;
}

.banner-text p {
  font-size: 20px;
  color: #94a3b8;
  margin-bottom: 32px;
}

.banner-stats {
  display: flex;
  gap: 48px;
}

.banner-stats .stat {
  display: flex;
  flex-direction: column;
}

.banner-stats .stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #fff;
}

.banner-stats .stat-label {
  font-size: 14px;
  color: #64748b;
}

.banner-visual {
  position: relative;
  width: 300px;
  height: 200px;
}

.floating-card {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  padding: 20px 24px;
  display: flex;
  align-items: center;
  gap: 12px;
  color: white;
  font-weight: 600;
  animation: float 6s ease-in-out infinite;
}

.floating-card .el-icon {
  font-size: 24px;
}

.card-1 {
  top: 0;
  left: 0;
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.8), rgba(139, 92, 246, 0.8));
  animation-delay: 0s;
}

.card-2 {
  bottom: 0;
  right: 0;
  background: linear-gradient(135deg, rgba(244, 63, 94, 0.8), rgba(249, 115, 22, 0.8));
  animation-delay: -3s;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-15px); }
}

/* Section */
.section {
  margin-top: 48px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.section-title h2 {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
}

.title-icon {
  font-size: 28px;
  color: var(--primary);
}

/* Goods Grid */
.goods-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.goods-card {
  background: var(--bg-card);
  border-radius: var(--radius-xl);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid var(--border);
  animation: fadeInUp 0.5s ease forwards;
  opacity: 0;
}

.goods-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.12);
  border-color: transparent;
}

.goods-card .goods-img {
  position: relative;
  width: 100%;
  height: 200px;
  background: linear-gradient(135deg, #f8fafc, #e2e8f0);
  display: flex;
  align-items: center;
  justify-content: center;
}

.goods-card .goods-img .el-icon {
  color: #cbd5e1;
  font-size: 48px;
}

.seckill-badge {
  position: absolute;
  top: 16px;
  left: 16px;
  background: linear-gradient(135deg, var(--accent), #f97316);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 4px;
  box-shadow: 0 4px 12px rgba(244, 63, 94, 0.4);
}

.seckill-badge.sale {
  background: linear-gradient(135deg, var(--primary), #8b5cf6);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.4);
}

.goods-card .goods-info {
  padding: 20px;
}

.goods-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goods-price {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 16px;
}

.goods-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.stock, .category-tag {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-muted);
}

.buy-btn {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, var(--primary), var(--primary-dark));
  border: none;
  border-radius: var(--radius-md);
  color: white;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.buy-btn:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
}

.buy-btn.secondary {
  background: transparent;
  border: 1px solid var(--border);
  color: var(--text-primary);
}

.buy-btn.secondary:hover {
  border-color: var(--primary);
  color: var(--primary);
  box-shadow: none;
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
