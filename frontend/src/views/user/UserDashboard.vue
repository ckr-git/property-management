<template>
  <div class="user-dashboard">
    <el-container class="layout-container">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <h3>业主中心</h3>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="notices">
            <el-icon><Bell /></el-icon>
            <span>公告通知</span>
          </el-menu-item>
          <el-menu-item index="payments">
            <el-icon><Money /></el-icon>
            <span>缴费记录</span>
          </el-menu-item>
          <el-menu-item index="repairs">
            <el-icon><Tools /></el-icon>
            <span>报修申请</span>
          </el-menu-item>
          <el-menu-item index="profile">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主要内容区 -->
      <el-container>
        <!-- 顶部栏 -->
        <el-header class="header">
          <div class="header-left">
            <h2>{{ getPageTitle() }}</h2>
          </div>
          <div class="header-right">
            <span>欢迎，{{ user?.realName }}</span>
            <el-button @click="logout">退出登录</el-button>
          </div>
        </el-header>

        <!-- 主内容 -->
        <el-main class="main-content">
          <!-- 首页 -->
          <div v-if="activeMenu === 'home'" class="home-content">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#409eff"><Money /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">￥2,580</div>
                    <div class="stat-label">本月应缴费用</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#67c23a"><CircleCheck /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">5</div>
                    <div class="stat-label">已完成报修</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#e6a23c"><Clock /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">2</div>
                    <div class="stat-label">待处理报修</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#f56c6c"><Bell /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">3</div>
                    <div class="stat-label">未读公告</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>

            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <el-card>
                  <template #header>最新公告</template>
                  <div v-for="notice in notices" :key="notice.id" class="notice-item">
                    <div class="notice-title">{{ notice.title }}</div>
                    <div class="notice-time">{{ formatTime(notice.createTime) }}</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <template #header>房屋信息</template>
                  <div class="house-info">
                    <p><strong>楼栋：</strong>1号楼</p>
                    <p><strong>单元：</strong>2单元</p>
                    <p><strong>房号：</strong>301室</p>
                    <p><strong>面积：</strong>120.5㎡</p>
                    <p><strong>类型：</strong>住宅</p>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 公告通知 -->
          <div v-else-if="activeMenu === 'notices'" class="notices-content">
            <el-card v-for="notice in notices" :key="notice.id" class="notice-card">
              <div class="notice-header">
                <h3>{{ notice.title }}</h3>
                <el-tag v-if="notice.isTop" type="danger" size="small">置顶</el-tag>
              </div>
              <div class="notice-content">{{ notice.content }}</div>
              <div class="notice-footer">
                <span>发布人：{{ notice.publisherName }}</span>
                <span>发布时间：{{ formatTime(notice.createTime) }}</span>
              </div>
            </el-card>
          </div>

          <!-- 其他内容区域可以继续添加 -->
          <div v-else class="coming-soon">
            <el-result icon="info" title="功能开发中" sub-title="该功能正在开发中，敬请期待">
            </el-result>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  House, Bell, Money, Tools, User, CircleCheck, Clock
} from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'UserDashboard',
  components: {
    House, Bell, Money, Tools, User, CircleCheck, Clock
  },
  setup() {
    const router = useRouter()
    const activeMenu = ref('home')
    const user = ref(JSON.parse(localStorage.getItem('user')))
    const notices = ref([])

    const getPageTitle = () => {
      const titles = {
        home: '首页',
        notices: '公告通知',
        payments: '缴费记录',
        repairs: '报修申请',
        profile: '个人信息'
      }
      return titles[activeMenu.value] || '首页'
    }

    const handleMenuSelect = (index) => {
      activeMenu.value = index
    }

    const logout = () => {
      localStorage.removeItem('user')
      ElMessage.success('退出成功')
      router.push('/')
    }

    const loadNotices = async () => {
      try {
        const response = await axios.get('/api/notice/list')
        if (response.data.code === 200) {
          notices.value = response.data.data
        }
      } catch (error) {
        console.error('获取公告失败:', error)
      }
    }

    const formatTime = (timeStr) => {
      return new Date(timeStr).toLocaleString()
    }

    onMounted(() => {
      loadNotices()
    })

    return {
      activeMenu,
      user,
      notices,
      getPageTitle,
      handleMenuSelect,
      logout,
      formatTime
    }
  }
}
</script>

<style scoped>
.user-dashboard {
  height: 100vh;
}

.layout-container {
  height: 100%;
}

.sidebar {
  background: #001529;
  color: white;
}

.logo {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #2c3e50;
}

.logo h3 {
  margin: 0;
  color: white;
}

.sidebar-menu {
  border: none;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-bottom: 1px solid #e8e8e8;
  padding: 0 20px;
}

.header-left h2 {
  margin: 0;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.main-content {
  background: #f5f5f5;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stat-card .el-card__body {
  display: flex;
  align-items: center;
  padding: 15px;
}

.stat-icon {
  margin-right: 15px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  color: #606266;
  font-size: 14px;
}

.notice-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.notice-item:last-child {
  border-bottom: none;
}

.notice-title {
  font-weight: bold;
}

.notice-time {
  color: #909399;
  font-size: 12px;
}

.house-info p {
  margin: 8px 0;
  color: #606266;
}

.notices-content .notice-card {
  margin-bottom: 15px;
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.notice-header h3 {
  margin: 0;
}

.notice-content {
  margin-bottom: 15px;
  line-height: 1.6;
}

.notice-footer {
  display: flex;
  justify-content: space-between;
  color: #909399;
  font-size: 12px;
}

.coming-soon {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}
</style>