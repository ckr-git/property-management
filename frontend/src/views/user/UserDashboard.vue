<template>
  <div class="user-dashboard">
    <el-container class="layout-container">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <h3>🌲 森林家园</h3>
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
                    <el-icon size="30" color="#2E7D32"><Money /></el-icon>
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
                    <el-icon size="30" color="#4CAF50"><CircleCheck /></el-icon>
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
                    <el-icon size="30" color="#FFB74D"><Clock /></el-icon>
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
                    <el-icon size="30" color="#EF5350"><Bell /></el-icon>
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

          <!-- 缴费记录 -->
          <div v-else-if="activeMenu === 'payments'" class="payments-content">
            <el-table :data="payments" style="width: 100%">
              <el-table-column prop="paymentMonth" label="月份" width="100" />
              <el-table-column prop="paymentType" label="类型" width="100">
                <template #default="{ row }">
                  {{ getPaymentTypeText(row.paymentType) }}
                </template>
              </el-table-column>
              <el-table-column prop="shouldPayAmount" label="应缴(元)" width="100" />
              <el-table-column prop="status" label="状态" width="80">
                <template #default="{ row }">
                  <el-tag :type="row.status === 1 ? 'success' : 'warning'">
                    {{ row.status === 1 ? '已缴' : '未缴' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="{ row }">
                  <el-button v-if="row.status === 0" size="small" type="primary" @click="payBill(row)">缴费</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 报修申请 -->
          <div v-else-if="activeMenu === 'repairs'" class="repairs-content">
            <div class="toolbar">
              <el-button type="primary" @click="showRepairDialog = true">提交报修</el-button>
            </div>
            <el-table :data="repairs" style="width: 100%">
              <el-table-column prop="description" label="问题描述" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="提交时间" width="160">
                <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
              </el-table-column>
            </el-table>
            <el-dialog v-model="showRepairDialog" title="提交报修" width="500px">
              <el-form :model="repairForm" label-width="80px">
                <el-form-item label="类型">
                  <el-select v-model="repairForm.repairType">
                    <el-option label="水电" :value="1" />
                    <el-option label="门窗" :value="2" />
                    <el-option label="电梯" :value="3" />
                    <el-option label="公共" :value="4" />
                    <el-option label="其他" :value="5" />
                  </el-select>
                </el-form-item>
                <el-form-item label="描述">
                  <el-input v-model="repairForm.description" type="textarea" rows="3" />
                </el-form-item>
              </el-form>
              <template #footer>
                <el-button @click="showRepairDialog = false">取消</el-button>
                <el-button type="primary" @click="submitRepair">提交</el-button>
              </template>
            </el-dialog>
          </div>

          <!-- 个人信息 -->
          <div v-else-if="activeMenu === 'profile'" class="profile-content">
            <el-card>
              <el-form label-width="100px">
                <el-form-item label="用户名">
                  <el-input :value="user?.username" disabled />
                </el-form-item>
                <el-form-item label="姓名">
                  <el-input :value="user?.realName" disabled />
                </el-form-item>
                <el-form-item label="电话">
                  <el-input :value="user?.phone" disabled />
                </el-form-item>
              </el-form>
            </el-card>
          </div>

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
    const payments = ref([])
    const repairs = ref([])
    const showRepairDialog = ref(false)
    const repairForm = reactive({
      repairType: 1,
      description: ''
    })

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

    const getPaymentTypeText = (type) => {
      const types = { 1: '物业费', 2: '停车费', 3: '水费', 4: '电费', 5: '燃气费' }
      return types[type] || '其他'
    }

    const getStatusType = (status) => {
      const types = { 0: 'danger', 1: 'warning', 2: 'success' }
      return types[status] || ''
    }

    const getStatusText = (status) => {
      const texts = { 0: '待处理', 1: '处理中', 2: '已完成' }
      return texts[status] || '未知'
    }

    const loadPayments = async () => {
      if (!user.value?.id) return
      try {
        const response = await axios.get(`/api/payment/owner/${user.value.id}`)
        if (response.data.code === 200) {
          payments.value = response.data.data
        }
      } catch (error) {
        console.error('获取缴费失败:', error)
      }
    }

    const loadRepairs = async () => {
      if (!user.value?.id) return
      try {
        const response = await axios.get(`/api/repair/applicant/${user.value.id}`)
        if (response.data.code === 200) {
          repairs.value = response.data.data
        }
      } catch (error) {
        console.error('获取报修失败:', error)
      }
    }

    const payBill = async (row) => {
      try {
        await axios.put(`/api/payment/${row.id}/pay`, { amount: row.shouldPayAmount })
        ElMessage.success('缴费成功')
        await loadPayments()
      } catch (error) {
        ElMessage.error('缴费失败')
      }
    }

    const submitRepair = async () => {
      try {
        await axios.post('/api/repair/submit', {
          ...repairForm,
          applicantId: user.value.id,
          applicantName: user.value.realName,
          applicantPhone: user.value.phone
        })
        ElMessage.success('提交成功')
        showRepairDialog.value = false
        repairForm.description = ''
        await loadRepairs()
      } catch (error) {
        ElMessage.error('提交失败')
      }
    }

    onMounted(() => {
      loadNotices()
      loadPayments()
      loadRepairs()
    })

    return {
      activeMenu,
      user,
      notices,
      payments,
      repairs,
      showRepairDialog,
      repairForm,
      getPageTitle,
      handleMenuSelect,
      logout,
      formatTime,
      getPaymentTypeText,
      getStatusType,
      getStatusText,
      payBill,
      submitRepair
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