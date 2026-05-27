const api = require('../../utils/request')

Page({
  data: {
    searchValue: '',
    categories: [],
    goodsList: [],
    page: 1,
    loading: false,
    hasMore: true
  },

  onLoad() {
    this.loadCategories()
    this.loadGoods()
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ active: 0 })
    }
  },

  onPullDownRefresh() {
    this.setData({ page: 1, goodsList: [], hasMore: true })
    this.loadGoods()
    wx.stopPullDownRefresh()
  },

  onReachBottom() {
    if (this.data.loading || !this.data.hasMore) return
    this.setData({ page: this.data.page + 1 })
    this.loadGoods()
  },

  loadCategories() {
    api.get('/category/list').then(res => {
      this.setData({ categories: res.data || [] })
    }).catch(() => {})
  },

  loadGoods() {
    this.setData({ loading: true })
    api.get('/goods', { page: this.data.page, size: 10 }).then(res => {
      const list = res.data?.records || []
      this.setData({
        goodsList: this.data.page === 1 ? list : [...this.data.goodsList, ...list],
        hasMore: list.length >= 10
      })
    }).catch(() => {}).finally(() => {
      this.setData({ loading: false })
    })
  },

  onSearch() {
    wx.navigateTo({ url: '/pages/goods/list?keyword=' + this.data.searchValue })
  },

  goDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: '/pages/goods/detail?id=' + id })
  },

  goCategory(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: '/pages/goods/list?categoryId=' + id })
  }
})
