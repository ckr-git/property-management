<template>
  <div class="admin-dashboard">
    <el-container class="layout-container">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <h3>管理中心</h3>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="home">
            <el-icon><Monitor /></el-icon>
            <span>仪表板</span>
          </el-menu-item>
          <el-menu-item index="users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="houses">
            <el-icon><House /></el-icon>
            <span>房屋管理</span>
          </el-menu-item>
          <el-menu-item index="notices">
            <el-icon><Bell /></el-icon>
            <span>公告管理</span>
          </el-menu-item>
          <el-menu-item index="payments">
            <el-icon><Money /></el-icon>
            <span>缴费管理</span>
          </el-menu-item>
          <el-menu-item index="repairs">
            <el-icon><Tools /></el-icon>
            <span>报修管理</span>
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
            <span>管理员：{{ user?.realName }}</span>
            <el-button @click="logout">退出登录</el-button>
          </div>
        </el-header>

        <!-- 主内容 -->
        <el-main class="main-content">
          <!-- 仪表板首页 -->
          <div v-if="activeMenu === 'home'" class="dashboard-content">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#409eff"><User /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">1,258</div>
                    <div class="stat-label">总用户数</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#67c23a"><House /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">368</div>
                    <div class="stat-label">房屋总数</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#e6a23c"><Money /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">￥68,520</div>
                    <div class="stat-label">本月收入</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#f56c6c"><Tools /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">12</div>
                    <div class="stat-label">待处理报修</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>

            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <el-card>
                  <template #header>最近报修</template>
                  <el-table :data="recentRepairs" style="width: 100%">
                    <el-table-column prop="applicantName" label="申请人" width="100" />
                    <el-table-column prop="description" label="问题描述" />
                    <el-table-column prop="status" label="状态" width="80">
                      <template #default="{ row }">
                        <el-tag :type="getStatusType(row.status)">
                          {{ getStatusText(row.status) }}
                        </el-tag>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <template #header>快速操作</template>
                  <div class="quick-actions">
                    <el-button type="primary" @click="activeMenu = 'notices'">
                      <el-icon><Bell /></el-icon>
                      发布公告
                    </el-button>
                    <el-button type="success" @click="activeMenu = 'users'">
                      <el-icon><User /></el-icon>
                      用户管理
                    </el-button>
                    <el-button type="warning" @click="activeMenu = 'repairs'">
                      <el-icon><Tools /></el-icon>
                      处理报修
                    </el-button>
                    <el-button type="info" @click="activeMenu = 'houses'">
                      <el-icon><House /></el-icon>
                      房屋管理
                    </el-button>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 公告管理 -->
          <div v-else-if="activeMenu === 'notices'" class="notices-management">
            <div class="toolbar">
              <el-button type="primary" @click="showNoticeDialog = true">
                <el-icon><Plus /></el-icon>
                发布公告
              </el-button>
            </div>
            
            <el-table :data="notices" style="width: 100%">
              <el-table-column prop="title" label="标题" />
              <el-table-column prop="type" label="类型" width="100">
                <template #default="{ row }">
                  {{ getNoticeTypeText(row.type) }}
                </template>
              </el-table-column>
              <el-table-column prop="publisherName" label="发布人" width="100" />
              <el-table-column prop="isTop" label="置顶" width="80">
                <template #default="{ row }">
                  <el-tag v-if="row.isTop" type="danger" size="small">置顶</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="发布时间" width="150">
                <template #default="{ row }">
                  {{ formatTime(row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template #default="{ row }">
                  <el-button size="small" @click="editNotice(row)">编辑</el-button>
                  <el-button size="small" type="danger" @click="deleteNotice(row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 发布公告对话框 -->
            <el-dialog v-model="showNoticeDialog" title="发布公告" width="600px">
              <el-form :model="noticeForm" label-width="80px">
                <el-form-item label="标题">
                  <el-input v-model="noticeForm.title" />
                </el-form-item>
                <el-form-item label="类型">
                  <el-select v-model="noticeForm.type">
                    <el-option label="通知公告" :value="1" />
                    <el-option label="停水停电" :value="2" />
                    <el-option label="活动通知" :value="3" />
                    <el-option label="温馨提示" :value="4" />
                  </el-select>
                </el-form-item>
                <el-form-item label="内容">
                  <el-input v-model="noticeForm.content" type="textarea" rows="5" />
                </el-form-item>
                <el-form-item label="置顶">
                  <el-switch v-model="noticeForm.isTop" :active-value="1" :inactive-value="0" />
                </el-form-item>
              </el-form>
              <template #footer>
                <el-button @click="showNoticeDialog = false">取消</el-button>
                <el-button type="primary" @click="publishNotice">发布</el-button>
              </template>
            </el-dialog>
          </div>

          <!-- 其他内容区域 -->
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Monitor, User, House, Bell, Money, Tools, Plus
} from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'AdminDashboard',
  components: {
    Monitor, User, House, Bell, Money, Tools, Plus
  },
  setup() {
    const router = useRouter()
    const activeMenu = ref('home')
    const user = ref(JSON.parse(localStorage.getItem('user')))
    const notices = ref([])
    const recentRepairs = ref([
      { applicantName: '张先生', description: '水龙头漏水', status: 0 },
      { applicantName: '李女士', description: '电梯故障', status: 1 },
      { applicantName: '王先生', description: '门锁损坏', status: 2 }
    ])
    const showNoticeDialog = ref(false)
    
    const noticeForm = reactive({
      title: '',
      content: '',
      type: 1,
      isTop: 0,
      publisherId: user.value?.id,
      publisherName: user.value?.realName
    })

    const getPageTitle = () => {
      const titles = {
        home: '仪表板',
        users: '用户管理',
        houses: '房屋管理',
        notices: '公告管理',
        payments: '缴费管理',
        repairs: '报修管理'
      }
      return titles[activeMenu.value] || '仪表板'
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

    const publishNotice = async () => {
      try {
        const response = await axios.post('/api/notice/publish', noticeForm)
        if (response.data.code === 200) {
          ElMessage.success('发布成功')
          showNoticeDialog.value = false
          // 重置表单
          Object.assign(noticeForm, {
            title: '',
            content: '',
            type: 1,
            isTop: 0,
            publisherId: user.value?.id,
            publisherName: user.value?.realName
          })
          await loadNotices()
        } else {
          ElMessage.error(response.data.message)
        }
      } catch (error) {
        ElMessage.error('发布失败')
      }
    }

    const editNotice = (notice) => {
      // 编辑公告功能
      ElMessage.info('编辑功能开发中')
    }

    const deleteNotice = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除这条公告吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
        
        const response = await axios.delete(`/api/notice/${id}`)
        if (response.data.code === 200) {
          ElMessage.success('删除成功')
          await loadNotices()
        } else {
          ElMessage.error(response.data.message)
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    const getStatusType = (status) => {
      const types = { 0: 'danger', 1: 'warning', 2: 'success' }
      return types[status] || ''
    }

    const getStatusText = (status) => {
      const texts = { 0: '待处理', 1: '处理中', 2: '已完成' }
      return texts[status] || '未知'
    }

    const getNoticeTypeText = (type) => {
      const types = { 1: '通知公告', 2: '停水停电', 3: '活动通知', 4: '温馨提示' }
      return types[type] || '其他'
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
      recentRepairs,
      showNoticeDialog,
      noticeForm,
      getPageTitle,
      handleMenuSelect,
      logout,
      publishNotice,
      editNotice,
      deleteNotice,
      getStatusType,
      getStatusText,
      getNoticeTypeText,
      formatTime
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
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

.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.quick-actions .el-button {
  justify-self: stretch;
}

.toolbar {
  margin-bottom: 20px;
}

.coming-soon {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}
</style>