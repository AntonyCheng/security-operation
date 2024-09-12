import request from '@/utils/request'

export function adminPageProject(data) {
  return request({
    url: '/admin/project/page',
    method: 'get',
    params: data
  })
}

export function adminAddProject(data) {
  return request({
    url: '/admin/project/add',
    method: 'post',
    data
  })
}

export function adminAddProjectUser(data) {
  return request({
    url: '/admin/project/add/user',
    method: 'post',
    data
  })
}

export function adminDeleteProject(data) {
  return request({
    url: '/admin/project/delete/' + data,
    method: 'delete'
  })
}

export function adminDeleteProjectUser(data) {
  return request({
    url: '/admin/project/delete/user',
    method: 'delete',
    data
  })
}

export function adminUpdateProject(data) {
  return request({
    url: '/admin/project/update',
    method: 'put',
    data
  })
}

export function adminExportExcel() {
  return request({
    url: '/admin/project/export',
    method: 'get',
    // 后端传来二进制流是需要修改为blob类型
    responseType: 'blob'
  })
}
