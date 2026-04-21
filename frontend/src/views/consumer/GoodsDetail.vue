<template>
  <div class="page-container">
    <div v-if="goods" class="card goods-detail">
      <el-row :gutter="40">
        <el-col :span="10">
          <div class="detail-img">
            <el-image v-if="goods.goodsImg" :src="goods.goodsImg" fit="cover" style="width:100%;height:400px;border-radius:var(--radius-lg)" />
            <div v-else class="img-placeholder">
              <el-icon :size="80"><Goods /></el-icon>
            </div>
          </div>
        </el-col>
        <el-col :span="14">
          <h1 class="detail-title">{{ goods.goodsName }}</h1>
          <p class="detail-subtitle">{{ goods.goodsTitle }}</p>
          <div class="price-box">
            <span class="label">价格</span>
            <span class="price-normal">&yen;{{ goods.goodsPrice }}</span>
            <span v-if="goods.seckillPrice" class="original-price">&yen;{{ goods.seckillPrice }}</span>
          </div>
          <div class="info-row">
            <span class="label">分类</span>
            <span class="tag tag-muted">{{ goods.category || '普通' }}</span>
          </div>
          <div class="info-row">
            <span class="label">库存</span>
            <span>{{ goods.stockCount }} 件</span>
          </div>
          <div class="info-row">
            <span class="label">状态</span>
            <span class="tag" :class="goods.status === 1 ? 'tag-success' : 'tag-muted'">
              {{ goods.status === 1 ? '上架' : '下架' }}
            </span>
          </div>
          <el-divider />
          <div v-if="goods.goodsDetail" class="detail-content" v-html="goods.goodsDetail"></div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getGoodsDetail } from '../../api/goods'
import { Goods } from '@element-plus/icons-vue'

const route = useRoute()
const goods = ref(null)

onMounted(async () => {
  try {
    const res = await getGoodsDetail(route.params.id)
    goods.value = res.data
  } catch {}
})
</script>

<style scoped>
.goods-detail {
  padding: 32px;
}

.img-placeholder {
  width: 100%;
  height: 400px;
  background: linear-gradient(135deg, #f1f5f9, #e2e8f0);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #cbd5e1;
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

.price-box, .info-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.label {
  color: var(--text-muted);
  font-size: 14px;
  min-width: 60px;
}

.detail-content {
  color: var(--text-secondary);
  line-height: 1.8;
  font-size: 14px;
}
</style>