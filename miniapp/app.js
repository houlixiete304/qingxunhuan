App({
  onLaunch() {
    // 检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      // 未登录，后续进入需要登录的页面时再引导
    }
  },
  globalData: {
    userInfo: null,
    token: ''
  }
})
