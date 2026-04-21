<template>
  <div>
    <h2 class="page-title">{{ isEdit ? '编辑秒杀活动' : '创建秒杀活动' }}</h2>
    <div class="card form-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" size="large">
        <el-form-item label="活动名称" prop="activityName">
          <el-input v-model="form.activityName" placeholder="输入活动名称" />
        </el-form-item>
        <el-form-item label="商品ID" prop="goodsId">
          <el-input-number v-model="form.goodsId" :min="1" style="width:100%" />
        </el-form-item>
        <el-form-item label="秒杀价格" prop="seckillPrice">
          <el-input-number v-model="form.seckillPrice" :min="0" :precision="2" style="width:100%" />
        </el-form-item>
        <el-form-item label="库存" prop="stockCount">
          <el-input-number v-model="form.stockCount" :min="1" style="width:100%" />
        </el-form-item>
        <el-form-item label="限购数量" prop="limitCount">
          <el-input-number v-model="form.limitCount" :min="1" style="width:100%" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" style="width:100%" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" style="width:100%" />
        </el-form-item>
        <el-form-item>
          <button class="btn btn-accent" :loading="loading" @click="handleSubmit">{{ isEdit ? '保存' : '创建' }}</button>
          <button class="btn btn-outline" style="margin-left:12px" @click="$router.back()">取消</button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createActivity, updateActivity, getActivityDetail } from '../../api/seckill'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const loading = ref(false)

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  id: null, activityName: '', goodsId: null, seckillPrice: 0,
  stockCount: 1, limitCount: 1, startTime: '', endTime: '', status: 0
})

const rules = {
  activityName: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  goodsId: [{ required: true, message: '请输入商品ID', trigger: 'blur' }],
  seckillPrice: [{ required: true, message: '请输入秒杀价格', trigger: 'blur' }],
  stockCount: [{ required: true, message: '请输入库存', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
}

onMounted(async () => {
  if (isEdit.value) {
    try {
      const res = await getActivityDetail(route.params.id)
      Object.assign(form, res.data)
    } catch {}
  }
})

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    if (isEdit.value) {
      await updateActivity(form)
    } else {
      await createActivity(form)
    }
    ElMessage.success(isEdit.value ? '已保存' : '已创建')
    router.push('/merchant/activities')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.form-card { max-width: 700px; }
</style>
