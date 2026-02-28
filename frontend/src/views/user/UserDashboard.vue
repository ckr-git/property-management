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
                    <div class="stat-value">￥{{ dashStats.unpaidAmount }}</div>
                    <div class="stat-label">待缴费用</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#4CAF50"><CircleCheck /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ dashStats.completedRepairs }}</div>
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
                    <div class="stat-value">{{ dashStats.pendingRepairs }}</div>
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
                    <div class="stat-value">{{ dashStats.noticeCount }}</div>
                    <div class="stat-label">最新公告</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>

            <el-row :gutter="20" style="margin-top: 20px;">
              <el-col :span="12">
                <el-card>
                  <template #header>最新公告</template>
                  <div v-if="notices.length === 0" style="color: #909399; text-align: center; padding: 20px;">暂无公告</div>
                  <div v-for="notice in notices.slice(0, 5)" :key="notice.id" class="notice-item">
                    <div class="notice-title">{{ notice.title }}</div>
                    <div class="notice-time">{{ formatTime(notice.createTime) }}</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <template #header>房屋信息</template>
                  <div v-if="houseInfo" class="house-info">
                    <p><strong>楼栋：</strong>{{ houseInfo.buildingNo }}</p>
                    <p><strong>单元：</strong>{{ houseInfo.unitNo }}</p>
                    <p><strong>房号：</strong>{{ houseInfo.roomNo }}</p>
                    <p><strong>面积：</strong>{{ houseInfo.area }}㎡</p>
                    <p><strong>类型：</strong>{{ houseInfo.houseType === 1 ? '住宅' : houseInfo.houseType === 2 ? '商铺' : '车位' }}</p>
                  </div>
                  <div v-else style="color: #909399; text-align: center; padding: 20px;">暂无绑定房屋</div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 公告通知 -->
          <div v-else-if="activeMenu === 'notices'" class="notices-content">
            <div v-if="notices.length === 0" style="color: #909399; text-align: center; padding: 40px;">暂无公告通知</div>
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
              <el-table-column prop="actualPayAmount" label="实缴(元)" width="100" />
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
              <el-table-column prop="repairType" label="类型" width="100">
                <template #default="{ row }">
                  {{ getRepairTypeText(row.repairType) }}
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="提交时间" width="160">
                <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
              </el-table-column>
            </el-table>
            <el-dialog v-model="showRepairDialog" title="提交报修" width="500px" @close="resetRepairForm">
              <el-form :model="repairForm" :rules="repairRules" ref="repairFormRef" label-width="80px">
                <el-form-item label="类型" prop="repairType">
                  <el-select v-model="repairForm.repairType">
                    <el-option label="水电维修" :value="1" />
                    <el-option label="门窗维修" :value="2" />
                    <el-option label="电梯维修" :value="3" />
                    <el-option label="公共设施" :value="4" />
                    <el-option label="其他" :value="5" />
                  </el-select>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                  <el-input v-model="repairForm.description" type="textarea" rows="3" placeholder="请详细描述问题" />
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
              <template #header>
                <div style="display: flex; justify-content: space-between; align-items: center;">
                  <span>个人信息</span>
                  <div>
                    <el-button v-if="!isEditingProfile" size="small" @click="startEditProfile">编辑</el-button>
                    <el-button size="small" @click="showPasswordDialog = true">修改密码</el-button>
                  </div>
                </div>
              </template>
              <el-form v-if="!isEditingProfile" label-width="100px">
                <el-form-item label="用户名">
                  <el-input :value="user?.username" disabled />
                </el-form-item>
                <el-form-item label="姓名">
                  <el-input :value="user?.realName" disabled />
                </el-form-item>
                <el-form-item label="电话">
                  <el-input :value="user?.phone" disabled />
                </el-form-item>
                <el-form-item label="身份证号">
                  <el-input :value="user?.idCard" disabled />
                </el-form-item>
              </el-form>
              <el-form v-else :model="profileForm" label-width="100px">
                <el-form-item label="用户名">
                  <el-input :value="user?.username" disabled />
                </el-form-item>
                <el-form-item label="姓名">
                  <el-input v-model="profileForm.realName" />
                </el-form-item>
                <el-form-item label="电话">
                  <el-input v-model="profileForm.phone" />
                </el-form-item>
                <el-form-item label="身份证号">
                  <el-input v-model="profileForm.idCard" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveProfile">保存</el-button>
                  <el-button @click="cancelEditProfile">取消</el-button>
                </el-form-item>
              </el-form>
            </el-card>

            <!-- 修改密码对话框 -->
            <el-dialog v-model="showPasswordDialog" title="修改密码" width="400px" @close="resetPasswordForm">
              <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input v-model="passwordForm.oldPassword" type="password" show-password />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="passwordForm.newPassword" type="password" show-password />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
                </el-form-item>
              </el-form>
              <template #footer>
                <el-button @click="showPasswordDialog = false">取消</el-button>
                <el-button type="primary" @click="submitPasswordChange">确认修改</el-button>
              </template>
            </el-dialog>
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
import { ElMessage, ElMessageBox } from 'element-plus'
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
    const repairFormRef = ref(null)
    const repairForm = reactive({
      repairType: 1,
      description: ''
    })
    const repairRules = {
      repairType: [{ required: true, message: '请选择报修类型', trigger: 'change' }],
      description: [{ required: true, message: '请描述问题', trigger: 'blur' }]
    }

    // Dashboard stats
    const dashStats = reactive({
      unpaidAmount: '0.00',
      completedRepairs: 0,
      pendingRepairs: 0,
      noticeCount: 0
    })
    const houseInfo = ref(null)

    // Profile editing
    const isEditingProfile = ref(false)
    const profileForm = reactive({ realName: '', phone: '', idCard: '' })

    // Password change
    const showPasswordDialog = ref(false)
    const passwordFormRef = ref(null)
    const passwordForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })
    const passwordRules = {
      oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
      newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, message: '密码至少6位', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认新密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== passwordForm.newPassword) {
              callback(new Error('两次输入密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }

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
        const response = await axios.get('/api/notice/list', { params: { pageNum: 1, pageSize: 100 } })
        if (response.data.code === 200) {
          const data = response.data.data
          notices.value = data.records || data
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

    const getRepairTypeText = (type) => {
      const types = { 1: '水电维修', 2: '门窗维修', 3: '电梯维修', 4: '公共设施', 5: '其他' }
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

    const loadDashboardStats = async () => {
      if (!user.value?.id) return
      try {
        const [paymentRes, repairRes] = await Promise.all([
          axios.get(`/api/payment/owner/${user.value.id}`),
          axios.get(`/api/repair/applicant/${user.value.id}`)
        ])
        if (paymentRes.data.code === 200) {
          const paymentList = paymentRes.data.data || []
          const unpaid = paymentList.filter(p => p.status === 0)
          dashStats.unpaidAmount = unpaid.reduce((sum, p) => sum + (p.shouldPayAmount || 0), 0).toFixed(2)
        }
        if (repairRes.data.code === 200) {
          const repairList = repairRes.data.data || []
          dashStats.completedRepairs = repairList.filter(r => r.status === 2).length
          dashStats.pendingRepairs = repairList.filter(r => r.status === 0 || r.status === 1).length
        }
        dashStats.noticeCount = notices.value.length
      } catch (error) {
        console.error('加载统计失败:', error)
      }
    }

    const loadHouseInfo = async () => {
      if (!user.value?.id) return
      try {
        const response = await axios.get(`/api/house/owner-id/${user.value.id}`)
        if (response.data.code === 200 && response.data.data) {
          houseInfo.value = response.data.data
        }
      } catch (error) {
        console.error('获取房屋信息失败:', error)
      }
    }

    const payBill = async (row) => {
      try {
        await ElMessageBox.confirm(
          `确认支付 ${row.shouldPayAmount} 元（${getPaymentTypeText(row.paymentType)} - ${row.paymentMonth}）？`,
          '缴费确认',
          { confirmButtonText: '确认支付', cancelButtonText: '取消', type: 'warning' }
        )
        await axios.put(`/api/payment/${row.id}/pay`, { amount: row.shouldPayAmount })
        ElMessage.success('缴费成功')
        await loadPayments()
        await loadDashboardStats()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('缴费失败')
        }
      }
    }

    const resetRepairForm = () => {
      repairForm.repairType = 1
      repairForm.description = ''
    }

    const submitRepair = async () => {
      if (repairFormRef.value) {
        try {
          await repairFormRef.value.validate()
        } catch {
          return
        }
      }
      try {
        await axios.post('/api/repair/submit', {
          ...repairForm,
          applicantId: user.value.id,
          applicantName: user.value.realName,
          applicantPhone: user.value.phone
        })
        ElMessage.success('提交成功')
        showRepairDialog.value = false
        resetRepairForm()
        await loadRepairs()
        await loadDashboardStats()
      } catch (error) {
        ElMessage.error('提交失败')
      }
    }

    // Profile editing methods
    const startEditProfile = () => {
      profileForm.realName = user.value?.realName || ''
      profileForm.phone = user.value?.phone || ''
      profileForm.idCard = user.value?.idCard || ''
      isEditingProfile.value = true
    }

    const cancelEditProfile = () => {
      isEditingProfile.value = false
    }

    const saveProfile = async () => {
      try {
        const response = await axios.put(`/api/user/${user.value.id}`, {
          realName: profileForm.realName,
          phone: profileForm.phone,
          idCard: profileForm.idCard
        })
        if (response.data.code === 200) {
          ElMessage.success('更新成功')
          user.value = { ...user.value, ...profileForm }
          localStorage.setItem('user', JSON.stringify(user.value))
          isEditingProfile.value = false
        } else {
          ElMessage.error(response.data.message || '更新失败')
        }
      } catch (error) {
        ElMessage.error('更新失败')
      }
    }

    // Password change methods
    const resetPasswordForm = () => {
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    }

    const submitPasswordChange = async () => {
      if (passwordFormRef.value) {
        try {
          await passwordFormRef.value.validate()
        } catch {
          return
        }
      }
      try {
        const response = await axios.put(`/api/user/${user.value.id}/password`, {
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword,
          confirmPassword: passwordForm.confirmPassword
        })
        if (response.data.code === 200) {
          ElMessage.success('密码修改成功，请重新登录')
          showPasswordDialog.value = false
          resetPasswordForm()
          localStorage.removeItem('user')
          router.push('/login')
        } else {
          ElMessage.error(response.data.message || '密码修改失败')
        }
      } catch (error) {
        ElMessage.error('密码修改失败')
      }
    }

    onMounted(async () => {
      await loadNotices()
      loadPayments()
      loadRepairs()
      loadHouseInfo()
      loadDashboardStats()
    })

    return {
      activeMenu,
      user,
      notices,
      payments,
      repairs,
      showRepairDialog,
      repairFormRef,
      repairForm,
      repairRules,
      dashStats,
      houseInfo,
      isEditingProfile,
      profileForm,
      showPasswordDialog,
      passwordFormRef,
      passwordForm,
      passwordRules,
      getPageTitle,
      handleMenuSelect,
      logout,
      formatTime,
      getPaymentTypeText,
      getRepairTypeText,
      getStatusType,
      getStatusText,
      payBill,
      submitRepair,
      resetRepairForm,
      startEditProfile,
      cancelEditProfile,
      saveProfile,
      resetPasswordForm,
      submitPasswordChange,
      loadDashboardStats,
      loadPayments,
      loadRepairs
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
