import request from '@/utils/request'

export function getRsaKey() {
  return request({
    url: '/encrypt/rsa/public/key',
    method: 'get'
  })
}
