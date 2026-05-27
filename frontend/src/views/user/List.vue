<template>
  <el-card>
    <template #header>用户管理</template>
    <el-table :data="[]" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="school" label="学校" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default>
          <el-button type="primary" size="small">编辑</el-button>
          <el-button type="danger" size="small">禁用</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
</script>
