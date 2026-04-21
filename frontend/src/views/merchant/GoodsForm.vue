<template>
  <div>
    <h2 class="page-title">{{ isEdit ? '编辑商品' : '添加商品' }}</h2>
    <div class="card form-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" size="large">
        <el-form-item label="商品名称" prop="goodsName">
          <el-input v-model="form.goodsName" placeholder="输入商品名称" />
        </el-form-item>
        <el-form-item label="商品标题" prop="goodsTitle">
          <el-input v-model="form.goodsTitle" placeholder="输入商品标题" />
        </el-form-item>
        <el-form-item label="价格" prop="goodsPrice">
          <el-input-number v-model="form.goodsPrice" :min="0" :precision="2" style="width:100%" />
        </el-form-item>
        <el-form-item label="库存" prop="stockCount">
          <el-input-number v-model="form.stockCount" :min="0" style="width:100%" />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="form.category" placeholder="如：电子产品、食品、服装" />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="form.goodsImg" placeholder="输入图片地址" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="form.goodsDetail" type="textarea" :rows="4" placeholder="输入商品详情" />
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
import { addGoods, updateGoods, getGoodsDetail } from '../../api/goods'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const loading = ref(false)

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  id: null, goodsName: '', goodsTitle: '', goodsPrice: 0,
  stockCount: 0, category: '', goodsImg: '', goodsDetail: '', status: 1
})

const rules = {
  goodsName: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  goodsPrice: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  stockCount: [{ required: true, message: '请输入库存', trigger: 'blur' }],
}

onMounted(async () => {
  if (isEdit.value) {
    try {
      const res = await getGoodsDetail(route.params.id)
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
      await updateGoods(form)
    } else {
      await addGoods(form)
    }
    ElMessage.success(isEdit.value ? '已保存' : '已创建')
    router.push('/merchant/goods')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.form-card { max-width: 700px; }
</style>
