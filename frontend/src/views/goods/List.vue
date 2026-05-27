<template>
  <el-card>
    <template #header>商品管理</template>
    <el-table :data="[]" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="商品名称" />
      <el-table-column prop="price" label="价格" width="120" />
      <el-table-column prop="categoryName" label="分类" width="120" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '在售' : '已下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default>
          <el-button type="primary" size="small">查看</el-button>
          <el-button type="danger" size="small">下架</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
</script>
