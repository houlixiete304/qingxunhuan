const api = require('../../utils/request')

Page({
  data: {
    goodsId: '',
    goods: null,
    remark: ''
  },

  onLoad(options) {
    this.setData({ goodsId: options.goodsId })
    api.get('/goods/' + options.goodsId).then(res => {
      this.setData({ goods: res.data })
    })
  },

  onSubmit() {
    api.post('/order', {
      goodsId: this.data.goodsId,
      remark: this.data.remark
    }).then(() => {
      wx.showToast({ title: '下单成功' })
      setTimeout(() => wx.switchTab({ url: '/pages/mine/mine' }), 1000)
    })
  }
})
