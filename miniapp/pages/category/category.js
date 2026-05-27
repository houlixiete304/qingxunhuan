const api = require('../../utils/request')

Page({
  data: {
    categories: [],
    activeIndex: 0
  },

  onLoad() {
    api.get('/category/list').then(res => {
      this.setData({ categories: res.data || [] })
    }).catch(() => {})
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ active: 1 })
    }
  },

  onTapCategory(e) {
    const index = e.currentTarget.dataset.index
    this.setData({ activeIndex: index })
  },

  goGoodsList(e) {
    const categoryId = e.currentTarget.dataset.id
    wx.navigateTo({ url: '/pages/goods/list?categoryId=' + categoryId })
  }
})
