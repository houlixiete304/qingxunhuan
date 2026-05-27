<template>
  <el-card>
    <template #header>订单管理</template>
    <el-table :data="[]" border>
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="goodsTitle" label="商品" />
      <el-table-column prop="amount" label="金额" width="120" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag>{{ row.statusText }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default>
          <el-button type="primary" size="small">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
</script>
