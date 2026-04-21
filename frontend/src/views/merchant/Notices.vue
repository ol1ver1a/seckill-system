<template>
  <div class="page-container">
    <h2 class="page-title">系统通知</h2>
    <div v-if="notices.length === 0" class="empty-state card">
      <el-icon :size="48"><Bell /></el-icon>
      <p>暂无通知</p>
    </div>
    <div v-for="item in notices" :key="item.id" class="card notice-card" @click="showDetail(item)">
      <div class="notice-title">{{ item.title }}</div>
      <div class="notice-time">{{ item.createTime }}</div>
    </div>
    <el-dialog v-model="dialogVisible" :title="currentNotice?.title" width="600px">
      <div class="dialog-content">{{ currentNotice?.content }}</div>
      <template #footer>
        <button class="btn btn-outline" @click="dialogVisible = false">关闭</button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listPublishedNotices } from '../../api/notice'
import { Bell } from '@element-plus/icons-vue'

const notices = ref([])
const dialogVisible = ref(false)
const currentNotice = ref(null)

function showDetail(item) {
  currentNotice.value = item
  dialogVisible.value = true
}

onMounted(async () => {
  try {
    const res = await listPublishedNotices()
    notices.value = res.data || []
  } catch {}
})
</script>

<style scoped>
.notice-card {
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 12px;
}
.notice-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}
.notice-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 6px;
}
.notice-time {
  color: var(--text-muted);
  font-size: 13px;
}
.dialog-content {
  color: var(--text-secondary);
  line-height: 1.8;
}
</style>