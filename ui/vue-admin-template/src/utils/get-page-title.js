import defaultSettings from '@/settings'

const title = defaultSettings.title || '安全运维管理平台'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
