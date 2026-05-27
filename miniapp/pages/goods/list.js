const api = require('../../utils/request')

Page({
  data: {
    keyword: '',
    categoryId: '',
    sortType: 0,
    goodsList: [],
    page: 1,
    hasMore: true,
    loading: false
  },

  onLoad(options) {
    if (options.keyword) this.setData({ keyword: options.keyword })
    if (options.categoryId) this.setData({ categoryId: options.categoryId })
    this.loadGoods()
  },

  onReachBottom() {
    if (this.data.loading || !this.data.hasMore) return
    this.setData({ page: this.data.page + 1 })
    this.loadGoods()
  },

  onPullDownRefresh() {
    this.setData({ page: 1, goodsList: [], hasMore: true })
    this.loadGoods()
    wx.stopPullDownRefresh()
  },

  loadGoods() {
    this.setData({ loading: true })
    api.get('/goods', {
      page: this.data.page,
      size: 10,
      categoryId: this.data.categoryId || undefined,
      keyword: this.data.keyword || undefined
    }).then(res => {
      const records = res.data?.records || []
      this.setData({
        goodsList: this.data.page === 1 ? records : [...this.data.goodsList, ...records],
        hasMore: records.length >= 10
      })
    }).finally(() => this.setData({ loading: false }))
  },

  onSearch(e) {
    this.setData({ keyword: e.detail, page: 1, goodsList: [], hasMore: true })
    this.loadGoods()
  },

  goDetail(e) {
    wx.navigateTo({ url: '/pages/goods/detail?id=' + e.currentTarget.dataset.id })
  }
})
