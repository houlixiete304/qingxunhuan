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
import { ref } from 'vue'

const stats = ref([
  { label: '用户总数', value: '--' },
  { label: '商品总数', value: '--' },
  { label: '订单总数', value: '--' },
  { label: '成交额', value: '--' }
])
</script>

<style scoped>
h3 { margin: 0; }
.stat-value { font-size: 28px; font-weight: bold; color: #409eff; }
.stat-label { font-size: 14px; color: #909399; margin-top: 8px; }
</style>
