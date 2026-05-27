const api = require('../../utils/request')

Page({
  data: {
    userId: '',
    messages: [],
    inputText: '',
    scrollIntoView: ''
  },

  onLoad(options) {
    this.setData({ userId: options.userId })
    this.loadMessages()
  },

  loadMessages() {
    api.get('/message/' + this.data.userId).then(res => {
      this.setData({ messages: res.data || [] }, () => {
        if (this.data.messages.length > 0) {
          this.setData({ scrollIntoView: 'msg-' + (this.data.messages.length - 1) })
        }
      })
    })
  },

  onInput(e) {
    this.setData({ inputText: e.detail.value })
  },

  onSend() {
    const text = this.data.inputText.trim()
    if (!text) return
    api.post('/message', {
      toUserId: parseInt(this.data.userId),
      content: text
    }).then(() => {
      this.setData({ inputText: '' })
      this.loadMessages()
    })
  }
})
