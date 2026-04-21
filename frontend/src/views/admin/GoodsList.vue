<template>
  <div>
    <h2 class="page-title">Products</h2>
    <div class="card">
      <div class="filter-bar">
        <el-input v-model="query.keyword" placeholder="Search products" style="width:200px" clearable />
        <el-select v-model="query.status" placeholder="Status" clearable style="width:120px;margin-left:12px">
          <el-option label="Active" :value="1" />
          <el-option label="Inactive" :value="0" />
        </el-select>
        <button class="btn btn-primary" style="margin-left:12px" @click="load">Search</button>
      </div>
      <el-table :data="list" stripe v-loading="loading" class="modern-table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="goodsName" label="Name" min-width="160" />
        <el-table-column label="Price" width="120">
          <template #default="{ row }"><span class="price-normal">&yen;{{ row.goodsPrice }}</span></template>
        </el-table-column>
        <el-table-column prop="stockCount" label="Stock" width="80" />
        <el-table-column prop="category" label="Category" width="100" />
        <el-table-column label="Status" width="80">
          <template #default="{ row }">
            <span class="tag" :class="row.status === 1 ? 'tag-success' : 'tag-muted'">
              {{ row.status === 1 ? 'Active' : 'Inactive' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="150" fixed="right">
          <template #default="{ row }">
            <button class="btn btn-sm" :class="row.status === 1 ? 'btn-outline' : 'btn-outline-success'" @click="toggleStatus(row)">
              {{ row.status === 1 ? 'Deactivate' : 'Activate' }}
            </button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { listGoods, updateGoodsStatus } from '../../api/goods'
import { ElMessage } from 'element-plus'

const list = ref([])
const loading = ref(false)
const query = reactive({ keyword: '', status: undefined })

async function load() {
  loading.value = true
  try {
    const res = await listGoods({ ...query, page: 1, size: 50 })
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

onMounted(load)
</script>

<style scoped>
.filter-bar { display: flex; margin-bottom: 20px; gap: 12px; flex-wrap: wrap; align-items: center; }
.btn-sm { padding: 6px 12px; font-size: 12px; }
.btn-outline { background: transparent; border: 1px solid var(--warning); color: var(--warning); }
.btn-outline:hover { background: var(--warning); color: white; }
.btn-outline-success { background: transparent; border: 1px solid var(--success); color: var(--success); }
.btn-outline-success:hover { background: var(--success); color: white; }
</style>
