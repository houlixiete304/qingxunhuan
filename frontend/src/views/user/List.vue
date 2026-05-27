<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>用户管理</span>
        <el-input v-model="keyword" placeholder="搜索昵称" style="width:200px" clearable @change="loadData" />
      </div>
    </template>
    <el-table :data="tableData" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="school" label="学校" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-popconfirm :title="row.status === 1 ? '确定禁用？' : '确定启用？'" @confirm="toggleStatus(row)">
            <template #reference>
              <el-button :type="row.status === 1 ? 'danger' : 'success'" size="small">
                {{ row.status === 1 ? '禁用' : '启用' }}
              </el-button>
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
  request.get('/admin/user', { params: { page: page.value, size: size.value, keyword: keyword.value || undefined } }).then(res => {
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  }).finally(() => loading.value = false)
}

function toggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  request.put(`/admin/user/${row.id}/status`, null, { params: { status: newStatus } }).then(() => {
    ElMessage.success('操作成功')
    loadData()
  })
}
</script>

<style scoped>
.header-row { display: flex; justify-content: space-between; align-items: center; }
</style>
