const api = require('../../utils/request')

Page({
  data: {
    orders: [],
    activeTab: 0,
    tabs: ['全部', '待付款', '已取消', '已完成']
  },

  onLoad() {
    this.loadOrders()
  },

  onPullDownRefresh() {
    this.loadOrders()
    wx.stopPullDownRefresh()
  },

  loadOrders() {
    const statusMap = { 0: '', 1: 'PENDING', 2: 'CANCELLED', 3: 'COMPLETED' }
    api.get('/order', { status: statusMap[this.data.activeTab] || '' }).then(res => {
      this.setData({ orders: res.data?.records || [] })
    })
  },

  onTabChange(e) {
    this.setData({ activeTab: e.detail.index })
    this.loadOrders()
  },

  onCancel(e) {
    const id = e.currentTarget.dataset.id
    api.put('/order/' + id + '/cancel').then(() => this.loadOrders())
  }
})
