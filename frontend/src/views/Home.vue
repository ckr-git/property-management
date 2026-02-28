<template>
  <div class="home">
    <el-container>
      <!-- 头部导航 -->
      <el-header class="header">
        <div class="logo">
          <h1>🌲 森林家园</h1>
        </div>
        <div class="nav-buttons">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button type="primary" @click="$router.push('/register')">注册</el-button>
        </div>
      </el-header>

      <!-- 主要内容 -->
      <el-main>
        <!-- 轮播图区域 -->
        <div class="banner">
          <el-carousel height="300px">
            <el-carousel-item>
              <div class="banner-item">
                <h2>欢迎回到森林家园</h2>
                <p>绿色生活，温馨家园</p>
              </div>
            </el-carousel-item>
            <el-carousel-item>
              <div class="banner-item">
                <h2>在线缴费</h2>
                <p>足不出户，轻松缴纳各类费用</p>
              </div>
            </el-carousel-item>
            <el-carousel-item>
              <div class="banner-item">
                <h2>便民服务</h2>
                <p>报修申请、投诉建议一键搞定</p>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>

        <!-- 功能介绍 -->
        <div class="features">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card class="feature-card">
                <div class="feature-icon">
                  <el-icon size="40"><House /></el-icon>
                </div>
                <h3>房屋信息</h3>
                <p>查看房屋详细信息和相关资料</p>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="feature-card">
                <div class="feature-icon">
                  <el-icon size="40"><Money /></el-icon>
                </div>
                <h3>物业缴费</h3>
                <p>在线缴纳物业费、停车费等各类费用</p>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="feature-card">
                <div class="feature-icon">
                  <el-icon size="40"><Tools /></el-icon>
                </div>
                <h3>报修服务</h3>
                <p>提交报修申请，跟踪维修进度</p>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="feature-card">
                <div class="feature-icon">
                  <el-icon size="40"><ChatDotRound /></el-icon>
                </div>
                <h3>公告通知</h3>
                <p>及时了解小区最新公告和通知</p>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 最新公告 -->
        <div class="notices">
          <h2>最新公告</h2>
          <el-card v-for="notice in notices" :key="notice.id" class="notice-card">
            <div class="notice-title">{{ notice.title }}</div>
            <div class="notice-content">{{ notice.content }}</div>
            <div class="notice-time">{{ formatTime(notice.createTime) }}</div>
          </el-card>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { House, Money, Tools, ChatDotRound } from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'Home',
  components: {
    House,
    Money,
    Tools,
    ChatDotRound
  },
  setup() {
    const notices = ref([])

    const loadNotices = async () => {
      try {
        const response = await axios.get('/api/notice/list', { params: { pageNum: 1, pageSize: 5 } })
        if (response.data.code === 200) {
          const data = response.data.data
          notices.value = data.records || data
        }
      } catch (error) {
        console.error('获取公告失败:', error)
      }
    }

    const formatTime = (timeStr) => {
      return new Date(timeStr).toLocaleDateString()
    }

    onMounted(() => {
      loadNotices()
    })

    return {
      notices,
      formatTime
    }
  }
}
</script>

<style scoped>
.home {
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #1B5E20 0%, #2E7D32 100%);
  color: white;
  padding: 0 20px;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
}

.banner {
  margin-bottom: 40px;
}

.banner-item {
  text-align: center;
  padding: 80px 0;
  background: linear-gradient(135deg, #2E7D32 0%, #4CAF50 100%);
  color: white;
}

.banner-item h2 {
  font-size: 36px;
  margin-bottom: 20px;
}

.banner-item p {
  font-size: 18px;
}

.features {
  margin-bottom: 40px;
}

.feature-card {
  text-align: center;
  padding: 20px;
  height: 200px;
  cursor: pointer;
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  color: #2E7D32;
  margin-bottom: 15px;
}

.feature-card h3 {
  margin-bottom: 10px;
  color: #303133;
}

.feature-card p {
  color: #606266;
}

.notices h2 {
  margin-bottom: 20px;
  color: #303133;
}

.notice-card {
  margin-bottom: 15px;
}

.notice-title {
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 8px;
  color: #303133;
}

.notice-content {
  color: #606266;
  margin-bottom: 8px;
}

.notice-time {
  color: #909399;
  font-size: 12px;
}
</style>