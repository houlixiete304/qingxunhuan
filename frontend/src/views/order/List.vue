<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>订单管理</span>
        <el-select v-model="statusFilter" placeholder="状态筛选" clearable style="width:160px" @change="loadData">
          <el-option label="全部" value="" />
          <el-option label="待付款" value="PENDING" />
          <el-option label="已取消" value="CANCELLED" />
          <el-option label="已完成" value="COMPLETED" />
        </el-select>
      </div>
    </template>
    <el-table :data="tableData" border v-loading="loading">
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="amount" label="金额" width="120" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180" />
    </el-table>
    <el-pagination
      style="margin-top:16px;justify-content:flex-end"
      v-model:current-page="page"
      :page-size="size"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="loadData"
    />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const tableData = ref([])
const loading = ref(false)
const statusFilter = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)

onMounted(() => loadData())

function loadData() {
  loading.value = true
  const params = { page: page.value, size: size.value }
  if (statusFilter.value) params.status = statusFilter.value
  request.get('/admin/order', { params }).then(res => {
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  }).finally(() => loading.value = false)
}

function statusType(s) {
  return { PENDING: 'warning', COMPLETED: 'success', CANCELLED: 'info' }[s] || ''
}
function statusText(s) {
  return { PENDING: '待付款', COMPLETED: '已完成', CANCELLED: '已取消' }[s] || s
}
</script>

<style scoped>
.header-row { display: flex; justify-content: space-between; align-items: center; }
</style>
