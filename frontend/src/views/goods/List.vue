<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>商品管理</span>
        <el-input v-model="keyword" placeholder="搜索商品" style="width:240px" clearable @change="loadData" />
      </div>
    </template>
    <el-table :data="tableData" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="商品名称" />
      <el-table-column prop="price" label="价格" width="120" />
      <el-table-column prop="school" label="学校" width="120" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '在售' : '已下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-popconfirm title="确定下架该商品？" @confirm="offShelf(row.id)">
            <template #reference>
              <el-button type="danger" size="small" :disabled="row.status === 0">下架</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
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
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const tableData = ref([])
const loading = ref(false)
const keyword = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)

onMounted(() => loadData())

function loadData() {
  loading.value = true
  request.get('/admin/goods', { params: { page: page.value, size: size.value, keyword: keyword.value } }).then(res => {
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  }).finally(() => loading.value = false)
}

function offShelf(id) {
  request.put(`/admin/goods/${id}/off-shelf`).then(() => {
    ElMessage.success('已下架')
    loadData()
  })
}
</script>

<style scoped>
.header-row { display: flex; justify-content: space-between; align-items: center; }
</style>
