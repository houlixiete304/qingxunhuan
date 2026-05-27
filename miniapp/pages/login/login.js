const api = require('../../utils/request')
const storage = require('../../utils/storage')

Page({
  data: {
    canLogin: true
  },

  onGetUserInfo(e) {
    if (!e.detail.userInfo) return
    wx.showLoading({ title: '登录中...' })
    wx.login({
      success: (loginRes) => {
        api.post('/user/wx-login', {
          code: loginRes.code,
          nickname: e.detail.userInfo.nickName,
          avatar: e.detail.userInfo.avatarUrl
        }).then(res => {
          storage.setToken(res.data.token)
          storage.setUserInfo(res.data.userInfo)
          wx.hideLoading()
          wx.showToast({ title: '登录成功' })
          setTimeout(() => wx.switchTab({ url: '/pages/index/index' }), 1000)
        }).catch(() => {
          wx.hideLoading()
        })
      },
      fail: () => {
        wx.hideLoading()
        wx.showToast({ title: '登录失败', icon: 'none' })
      }
    })
  },

  onQuickLogin() {
    wx.showLoading({ title: '登录中...' })
    wx.login({
      success: (loginRes) => {
        api.post('/user/wx-login', { code: loginRes.code }).then(res => {
          storage.setToken(res.data.token)
          storage.setUserInfo(res.data.userInfo)
          wx.hideLoading()
          wx.showToast({ title: '登录成功' })
          setTimeout(() => wx.switchTab({ url: '/pages/index/index' }), 1000)
        }).catch(() => wx.hideLoading())
      },
      fail: () => {
        wx.hideLoading()
        wx.showToast({ title: '登录失败', icon: 'none' })
      }
    })
  }
})
