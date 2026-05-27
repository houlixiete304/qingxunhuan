const BASE_URL = 'http://localhost:8080/api'

function request(options) {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token')
    wx.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': 'Bearer ' + token } : {}),
        ...options.header
      },
      success(res) {
        if (res.statusCode === 401) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          wx.reLaunch({ url: '/pages/index/index' })
          return
        }
        if (res.data.code === 200) {
          resolve(res.data)
        } else {
          wx.showToast({ title: res.data.message || '请求失败', icon: 'none' })
          reject(res.data)
        }
      },
      fail(err) {
        wx.showToast({ title: '网络错误', icon: 'none' })
        reject(err)
      }
    })
  })
}

module.exports = {
  get: (url, data) => request({ url, method: 'GET', data }),
  post: (url, data) => request({ url, method: 'POST', data }),
  put: (url, data) => request({ url, method: 'PUT', data }),
  del: (url, data) => request({ url, method: 'DELETE', data })
}
