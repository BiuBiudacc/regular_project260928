import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '控制台', icon: 'Odometer' }
      },
      {
        path: 'room',
        name: 'Room',
        component: () => import('@/views/room/index.vue'),
        meta: { title: '房间管理', icon: 'House' }
      },
      {
        path: 'batch',
        name: 'Batch',
        component: () => import('@/views/batch/index.vue'),
        meta: { title: '批次管理', icon: 'User' }
      },
      {
        path: 'class',
        name: 'Class',
        component: () => import('@/views/class/index.vue'),
        meta: { title: '班级管理', icon: 'User' }
      },
      {
        path: 'classRoom',
        name: 'ClassRoom',
        component: () => import('@/views/classRoom/index.vue'),
        meta: { title: '教室管理', icon: 'School' }
      },
      {
        path: 'student',
        name: 'Student',
        component: () => import('@/views/student/index.vue'),
        meta: { title: '学生管理', icon: 'UserFilled' }
      },
      {
        path: 'classRoomType',
        name: 'ClassRoomType',
        component: () => import('@/views/classRoomType/index.vue'),
        meta: { title: '教室型号码表', icon: 'Grid' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫:未登录访问受保护页面时重定向到登录页
router.beforeEach((to, from, next) => {
  document.title = to.meta.title
    ? `${to.meta.title} - 酒店入住后台管理系统`
    : '酒店入住后台管理系统'

  const authStore = useAuthStore()
  if (to.path !== '/login' && !authStore.isLoggedIn) {
    next('/login')
  } else if (to.path === '/login' && authStore.isLoggedIn) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
