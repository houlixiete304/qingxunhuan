const api = require('../../utils/request')
const storage = require('../../utils/storage')

Page({
  data: {
    id: '',
    goods: null,
    collected: false
  },

  onLoad(options) {
    this.setData({ id: options.id })
    this.loadDetail()
  },

  loadDetail() {
    api.get('/goods/' + this.data.id).then(res => {
      this.setData({ goods: res.data })
      if (res.data.images) {
        try { this.setData({ images: JSON.parse(res.data.images) }) } catch {}
      }
      this.checkCollected()
    })
  },

  checkCollected() {
    api.get('/goods/' + this.data.id + '/collected').then(res => {
      this.setData({ collected: res.data?.collected || false })
    }).catch(() => {})
  },

  onCollect() {
    const action = this.data.collected ? 'del' : 'post'
    api[action]('/goods/' + this.data.id + '/collect').then(() => {
      this.setData({ collected: !this.data.collected })
      wx.showToast({ title: this.data.collected ? '已收藏' : '已取消收藏' })
    })
  },

  onBuy() {
    wx.navigateTo({ url: '/pages/order/create?goodsId=' + this.data.id })
  },

  onContact() {
    const userId = this.data.goods?.userId
    if (userId) {
      wx.navigateTo({ url: '/pages/chat/detail?userId=' + userId })
    }
  }
})
