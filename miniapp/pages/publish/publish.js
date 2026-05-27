const api = require('../../utils/request')

Page({
  data: {
    images: [],
    title: '',
    description: '',
    price: '',
    originalPrice: '',
    categoryId: '',
    condition: '',
    categories: [],
    conditionOptions: ['全新', '几乎全新', '轻微使用', '明显使用痕迹'],
    showCondition: false
  },

  onLoad() {
    api.get('/category/list').then(res => {
      this.setData({ categories: res.data || [] })
    }).catch(() => {})
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ active: 2 })
    }
  },

  onUploadImage() {
    wx.chooseImage({
      count: 9 - this.data.images.length,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        this.setData({ images: [...this.data.images, ...res.tempFilePaths] })
      }
    })
  },

  onDeleteImage(e) {
    const index = e.detail.index
    const images = this.data.images
    images.splice(index, 1)
    this.setData({ images })
  },

  onSelectCategory(e) {
    this.setData({ categoryId: e.detail.value })
  },

  onSelectCondition(e) {
    this.setData({ condition: e.detail.value, showCondition: false })
  },

  onSubmit() {
    const { title, price, categoryId, condition, images, description } = this.data
    if (!title) return wx.showToast({ title: '请输入标题', icon: 'none' })
    if (!price) return wx.showToast({ title: '请输入价格', icon: 'none' })
    if (!categoryId) return wx.showToast({ title: '请选择分类', icon: 'none' })
    if (!condition) return wx.showToast({ title: '请选择成色', icon: 'none' })

    api.post('/goods', { title, price: parseFloat(price), originalPrice: parseFloat(this.data.originalPrice) || null, categoryId, condition, description, images: [] }).then(() => {
      wx.showToast({ title: '发布成功' })
      setTimeout(() => wx.switchTab({ url: '/pages/index/index' }), 1500)
    }).catch(() => {})
  }
})
