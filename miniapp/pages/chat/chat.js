const api = require('../../utils/request')

Page({
  data: {
    conversations: []
  },

  onLoad() {
    this.loadConversations()
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ active: 3 })
    }
    this.loadConversations()
  },

  loadConversations() {
    api.get('/message/list').then(res => {
      this.setData({ conversations: res.data || [] })
    }).catch(() => {})
  },

  goChat(e) {
    const userId = e.currentTarget.dataset.userId
    wx.navigateTo({ url: '/pages/chat/detail?userId=' + userId })
  }
})
