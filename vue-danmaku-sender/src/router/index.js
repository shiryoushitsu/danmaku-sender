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
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router