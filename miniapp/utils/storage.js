function get(key, defaultValue) {
  const value = wx.getStorageSync(key)
  return value || defaultValue
}

function set(key, value) {
  wx.setStorageSync(key, value)
}

function remove(key) {
  wx.removeStorageSync(key)
}

function getToken() {
  return get('token', '')
}

function setToken(token) {
  set('token', token)
}

function getUserInfo() {
  return get('userInfo', null)
}

function setUserInfo(info) {
  set('userInfo', info)
}

function clearAuth() {
  remove('token')
  remove('userInfo')
}

module.exports = {
  get,
  set,
  remove,
  getToken,
  setToken,
  getUserInfo,
  setUserInfo,
  clearAuth
}
