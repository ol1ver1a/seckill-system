<template>
  <div>
    <h2 class="page-title" style="display:flex;justify-content:space-between;align-items:center">
      <span>商品管理</span>
      <button class="btn btn-accent" @click="$router.push('/merchant/goods/add')">添加商品</button>
    </h2>
    <div class="card">
      <div class="filter-bar">
        <el-input v-model="query.keyword" placeholder="搜索商品" style="width:200px" clearable @clear="load" />
        <el-select v-model="query.status" placeholder="状态" clearable style="width:120px;margin-left:12px" @change="load">
          <el-option label="上架" :value="1" />
          <el-option label="下架" :value="0" />
        </el-select>
        <button class="btn btn-primary" style="margin-left:12px" @click="load">搜索</button>
      </div>
      <el-table :data="list" stripe v-loading="loading" class="modern-table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="goodsName" label="名称" min-width="160" />
        <el-table-column label="价格" width="120">
          <template #default="{ row }"><span class="price-normal">&yen;{{ row.goodsPrice }}</span></template>
        </el-table-column>
        <el-table-column prop="stockCount" label="库存" width="80" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <span class="tag" :class="row.status === 1 ? 'tag-success' : 'tag-muted'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <button class="btn btn-sm btn-outline" @click="$router.push(`/merchant/goods/edit/${row.id}`)">编辑</button>
            <button class="btn btn-sm" :class="row.status === 1 ? 'btn-outline-warning' : 'btn-outline-success'" style="margin-left:4px" @click="toggleStatus(row)">
              {{ row.status === 1 ? '下架' : '上架' }}
            </button>
            <button class="btn btn-sm btn-accent" style="margin-left:4px" @click="handleDelete(row)">删除</button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { listGoods, updateGoodsStatus, deleteGoods } from '../../api/goods'
import { useUserStore } from '../../stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const list = ref([])
const loading = ref(false)
const query = reactive({ keyword: '', status: undefined })

async function load() {
  loading.value = true
  try {
    const res = await listGoods({ merchantId: userStore.user?.id, ...query, page: 1, size: 50 })
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function toggleStatus(row) {
  await updateGoodsStatus(row.id, row.status === 1 ? 0 : 1)
  ElMessage.success('操作成功')
  load()
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确认删除该商品？', '确认', { type: 'warning' })
    await deleteGoods(row.id)
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
.btn-outline-warning { background: transparent; border: 1px solid var(--warning); color: var(--warning); }
.btn-outline-warning:hover { background: var(--warning); color: white; }
.btn-outline-success { background: transparent; border: 1px solid var(--success); color: var(--success); }
.btn-outline-success:hover { background: var(--success); color: white; }
</style>
