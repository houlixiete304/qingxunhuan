const storage = require('../../utils/storage')

Page({
  data: {
    userInfo: null,
    isLogin: false
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ active: 4 })
    }
    const userInfo = storage.getUserInfo()
    this.setData({
      userInfo,
      isLogin: !!userInfo
    })
  },

  onLogin() {
    wx.navigateTo({ url: '/pages/login/login' })
  },

  goMyGoods() {
    wx.navigateTo({ url: '/pages/goods/my-list' })
  },

  goMyOrders() {
    wx.navigateTo({ url: '/pages/order/list' })
  },

  onLogout() {
    wx.showModal({
      title: '提示',
      content: '确定退出登录？',
      success: (res) => {
        if (res.confirm) {
          storage.clearAuth()
          this.setData({ userInfo: null, isLogin: false })
        }
      }
    })
  }
})
