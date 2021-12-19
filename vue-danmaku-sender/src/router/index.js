import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'index',
    component: () => import('@/views/index.vue')
  },
  {
    path: '/m7',
    name: 'm7',
    component: () => import('@/views/m7.vue')
  },
  {
    path: '/xml',
    name: 'xml',
    component: () => import('@/views/xml.vue')
  },
  {
    path: '/ascii',
    name: 'ascii',
    component: () => import('@/views/ascii.vue')
  },
  {
    path: '/ascii-text',
    name: 'ascii-text',
    component: () => import('@/views/ascii-text.vue')
  },
  {
    path: '/lrc-exo',
    name: 'lrc-exo',
    component: () => import('@/views/lrc-exo.vue')
  },
  {
    path: '/exo-xml',
    name: 'exo-xml',
    component: () => import('@/views/exo-xml.vue')
  },
  {
    path: '/guide',
    name: 'guide',
    component: () => import('@/views/index.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
