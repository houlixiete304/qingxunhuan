<template>
  <div>
    <h3>仪表盘</h3>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="6" v-for="stat in stats" :key="stat.label">
        <el-card>
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const stats = ref([
  { label: '用户总数', value: 0 },
  { label: '商品总数', value: 0 },
  { label: '订单总数', value: 0 }
])

onMounted(async () => {
  try {
    const res = await request.get('/admin/statistics/overview')
    if (res.data) {
      stats.value[0].value = res.data.userCount || 0
      stats.value[1].value = res.data.goodsCount || 0
      stats.value[2].value = res.data.orderCount || 0
    }
  } catch {}
})
</script>

<style scoped>
h3 { margin: 0; }
.stat-value { font-size: 28px; font-weight: bold; color: #409eff; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>
