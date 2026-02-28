<template>
  <div class="admin-dashboard">
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
                    <el-icon size="30" color="#2E7D32"><User /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ stats.userCount }}</div>
                    <div class="stat-label">总用户数</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#4CAF50"><House /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ stats.houseCount }}</div>
                    <div class="stat-label">房屋总数</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#FFB74D"><Money /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">￥{{ stats.monthIncome }}</div>
                    <div class="stat-label">本月收入</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card class="stat-card">
                  <div class="stat-icon">
                    <el-icon size="30" color="#EF5350"><Tools /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ stats.pendingRepairs }}</div>
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
              <el-form :inline="true" class="search-form">
                <el-form-item>
                  <el-input v-model="noticeQuery.keyword" placeholder="搜索公告" clearable @keyup.enter="loadNotices" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="loadNotices">搜索</el-button>
                  <el-button @click="noticeQuery.keyword = ''; noticeQuery.pageNum = 1; loadNotices()">重置</el-button>
                </el-form-item>
              </el-form>
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
              <el-table-column label="置顶" width="80">
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
            <el-pagination
              v-model:current-page="noticeQuery.pageNum"
              v-model:page-size="noticeQuery.pageSize"
              :page-sizes="[10, 20, 50]"
              layout="total, sizes, prev, pager, next"
              :total="noticeTotal"
              @size-change="loadNotices"
              @current-change="loadNotices"
              style="margin-top: 15px;"
            />

            <!-- 发布公告对话框 -->
            <el-dialog v-model="showNoticeDialog" :title="editingNoticeId ? '编辑公告' : '发布公告'" width="600px" @close="resetNoticeForm">
              <el-form :model="noticeForm" :rules="noticeRules" ref="noticeFormRef" label-width="80px">
                <el-form-item label="标题" prop="title">
                  <el-input v-model="noticeForm.title" />
                </el-form-item>
                <el-form-item label="类型" prop="type">
                  <el-select v-model="noticeForm.type">
                    <el-option label="通知公告" :value="1" />
                    <el-option label="停水停电" :value="2" />
                    <el-option label="活动通知" :value="3" />
                    <el-option label="温馨提示" :value="4" />
                  </el-select>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                  <el-input v-model="noticeForm.content" type="textarea" rows="5" />
                </el-form-item>
                <el-form-item label="置顶">
                  <el-switch v-model="noticeForm.isTop" :active-value="1" :inactive-value="0" />
                </el-form-item>
              </el-form>
              <template #footer>
                <el-button @click="showNoticeDialog = false">取消</el-button>
                <el-button type="primary" @click="publishNotice">{{ editingNoticeId ? '保存' : '发布' }}</el-button>
              </template>
            </el-dialog>
          </div>

          <!-- 用户管理 -->
          <div v-else-if="activeMenu === 'users'" class="users-management">
            <div class="toolbar">
              <el-form :inline="true" class="search-form">
                <el-form-item>
                  <el-input v-model="userQuery.keyword" placeholder="搜索用户名/姓名/电话" clearable @keyup.enter="loadUsers" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="loadUsers">搜索</el-button>
                  <el-button @click="userQuery.keyword = ''; userQuery.pageNum = 1; loadUsers()">重置</el-button>
                </el-form-item>
              </el-form>
            </div>
            <el-table :data="users" style="width: 100%">
              <el-table-column prop="username" label="用户名" width="120" />
              <el-table-column prop="realName" label="姓名" width="100" />
              <el-table-column prop="phone" label="电话" width="130" />
              <el-table-column prop="userType" label="角色" width="80">
                <template #default="{ row }">
                  <el-tag :type="row.userType === 0 ? 'danger' : ''">
                    {{ row.userType === 0 ? '管理员' : '业主' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="80">
                <template #default="{ row }">
                  <el-tag :type="row.status === 0 ? 'success' : 'info'">
                    {{ row.status === 0 ? '正常' : '禁用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120">
                <template #default="{ row }">
                  <el-button size="small" @click="viewUser(row)">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              v-model:current-page="userQuery.pageNum"
              v-model:page-size="userQuery.pageSize"
              :page-sizes="[10, 20, 50]"
              layout="total, sizes, prev, pager, next"
              :total="userTotal"
              @size-change="loadUsers"
              @current-change="loadUsers"
              style="margin-top: 15px;"
            />

            <!-- 用户详情对话框 -->
            <el-dialog v-model="viewUserDialogVisible" title="用户详情" width="500px">
              <el-descriptions :column="1" border v-if="selectedUser">
                <el-descriptions-item label="用户名">{{ selectedUser.username }}</el-descriptions-item>
                <el-descriptions-item label="真实姓名">{{ selectedUser.realName }}</el-descriptions-item>
                <el-descriptions-item label="手机号">{{ selectedUser.phone || '未填写' }}</el-descriptions-item>
                <el-descriptions-item label="身份证号">{{ selectedUser.idCard || '未填写' }}</el-descriptions-item>
                <el-descriptions-item label="角色">{{ selectedUser.userType === 0 ? '管理员' : '业主' }}</el-descriptions-item>
                <el-descriptions-item label="状态">{{ selectedUser.status === 0 ? '正常' : '禁用' }}</el-descriptions-item>
              </el-descriptions>
            </el-dialog>
          </div>

          <!-- 房屋管理 -->
          <div v-else-if="activeMenu === 'houses'" class="houses-management">
            <div class="toolbar">
              <el-form :inline="true" class="search-form">
                <el-form-item>
                  <el-input v-model="houseQuery.keyword" placeholder="搜索楼栋/房号/业主" clearable @keyup.enter="loadHouses" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="loadHouses">搜索</el-button>
                  <el-button @click="houseQuery.keyword = ''; houseQuery.pageNum = 1; loadHouses()">重置</el-button>
                </el-form-item>
              </el-form>
              <el-button type="primary" @click="showHouseDialog = true">
                <el-icon><Plus /></el-icon>新增房屋
              </el-button>
            </div>
            <el-table :data="houses" style="width: 100%">
              <el-table-column prop="buildingNo" label="楼栋" width="80" />
              <el-table-column prop="unitNo" label="单元" width="80" />
              <el-table-column prop="roomNo" label="房号" width="80" />
              <el-table-column prop="area" label="面积(㎡)" width="100" />
              <el-table-column prop="ownerName" label="业主" width="100" />
              <el-table-column prop="ownerPhone" label="电话" width="130" />
              <el-table-column label="操作" width="150">
                <template #default="{ row }">
                  <el-button size="small" @click="editHouse(row)">编辑</el-button>
                  <el-button size="small" type="danger" @click="deleteHouse(row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              v-model:current-page="houseQuery.pageNum"
              v-model:page-size="houseQuery.pageSize"
              :page-sizes="[10, 20, 50]"
              layout="total, sizes, prev, pager, next"
              :total="houseTotal"
              @size-change="loadHouses"
              @current-change="loadHouses"
              style="margin-top: 15px;"
            />

            <el-dialog v-model="showHouseDialog" :title="editingHouseId ? '编辑房屋' : '新增房屋'" width="500px" @close="resetHouseForm">
              <el-form :model="houseForm" :rules="houseRules" ref="houseFormRef" label-width="80px">
                <el-form-item label="楼栋" prop="buildingNo"><el-input v-model="houseForm.buildingNo" /></el-form-item>
                <el-form-item label="单元" prop="unitNo"><el-input v-model="houseForm.unitNo" /></el-form-item>
                <el-form-item label="房号" prop="roomNo"><el-input v-model="houseForm.roomNo" /></el-form-item>
                <el-form-item label="面积(㎡)" prop="area"><el-input v-model.number="houseForm.area" /></el-form-item>
                <el-form-item label="业主"><el-input v-model="houseForm.ownerName" /></el-form-item>
                <el-form-item label="电话"><el-input v-model="houseForm.ownerPhone" /></el-form-item>
              </el-form>
              <template #footer>
                <el-button @click="showHouseDialog = false">取消</el-button>
                <el-button type="primary" @click="saveHouse">保存</el-button>
              </template>
            </el-dialog>
          </div>

          <!-- 报修管理 -->
          <div v-else-if="activeMenu === 'repairs'" class="repairs-management">
            <div class="toolbar">
              <el-form :inline="true" class="search-form">
                <el-form-item>
                  <el-input v-model="repairQuery.keyword" placeholder="搜索申请人/描述" clearable @keyup.enter="loadRepairs" />
                </el-form-item>
                <el-form-item>
                  <el-select v-model="repairQuery.status" placeholder="状态" clearable>
                    <el-option label="待处理" :value="0" />
                    <el-option label="处理中" :value="1" />
                    <el-option label="已完成" :value="2" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="loadRepairs">搜索</el-button>
                  <el-button @click="repairQuery.keyword = ''; repairQuery.status = null; repairQuery.pageNum = 1; loadRepairs()">重置</el-button>
                </el-form-item>
              </el-form>
            </div>
            <el-table :data="repairs" style="width: 100%">
              <el-table-column prop="applicantName" label="申请人" width="100" />
              <el-table-column prop="applicantPhone" label="电话" width="130" />
              <el-table-column prop="description" label="问题描述" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="getStatusType(row.status)">
                    {{ getStatusText(row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template #default="{ row }">
                  <el-button v-if="row.status === 0" size="small" type="primary" @click="handleRepair(row)">处理</el-button>
                  <el-button v-if="row.status === 1" size="small" type="success" @click="completeRepair(row)">完成</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              v-model:current-page="repairQuery.pageNum"
              v-model:page-size="repairQuery.pageSize"
              :page-sizes="[10, 20, 50]"
              layout="total, sizes, prev, pager, next"
              :total="repairTotal"
              @size-change="loadRepairs"
              @current-change="loadRepairs"
              style="margin-top: 15px;"
            />
          </div>

          <!-- 缴费管理 -->
          <div v-else-if="activeMenu === 'payments'" class="payments-management">
            <div class="toolbar">
              <el-form :inline="true" class="search-form">
                <el-form-item>
                  <el-select v-model="paymentQuery.status" placeholder="缴费状态" clearable>
                    <el-option label="未缴" :value="0" />
                    <el-option label="已缴" :value="1" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-select v-model="paymentQuery.paymentType" placeholder="费用类型" clearable>
                    <el-option label="物业费" :value="1" />
                    <el-option label="停车费" :value="2" />
                    <el-option label="水费" :value="3" />
                    <el-option label="电费" :value="4" />
                    <el-option label="燃气费" :value="5" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="loadPayments">搜索</el-button>
                  <el-button @click="paymentQuery.status = null; paymentQuery.paymentType = null; paymentQuery.pageNum = 1; loadPayments()">重置</el-button>
                </el-form-item>
              </el-form>
            </div>
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
            </el-table>
            <el-pagination
              v-model:current-page="paymentQuery.pageNum"
              v-model:page-size="paymentQuery.pageSize"
              :page-sizes="[10, 20, 50]"
              layout="total, sizes, prev, pager, next"
              :total="paymentTotal"
              @size-change="loadPayments"
              @current-change="loadPayments"
              style="margin-top: 15px;"
            />
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
  Monitor, User, House, Bell, Money, Tools, Plus, Search
} from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'AdminDashboard',
  components: {
    Monitor, User, House, Bell, Money, Tools, Plus, Search
  },
  setup() {
    const router = useRouter()
    const activeMenu = ref('home')
    const user = ref(JSON.parse(localStorage.getItem('user')))
    const notices = ref([])
    const recentRepairs = ref([])
    const showNoticeDialog = ref(false)
    const noticeFormRef = ref(null)
    const users = ref([])
    const houses = ref([])
    const repairs = ref([])
    const payments = ref([])
    const viewUserDialogVisible = ref(false)
    const selectedUser = ref(null)
    const showHouseDialog = ref(false)
    const houseFormRef = ref(null)
    const editingHouseId = ref(null)
    const houseForm = reactive({ buildingNo: '', unitNo: '', roomNo: '', area: '', ownerName: '', ownerPhone: '' })
    const stats = reactive({ userCount: 0, houseCount: 0, monthIncome: '0', pendingRepairs: 0 })

    // Pagination query objects
    const userQuery = reactive({ pageNum: 1, pageSize: 10, keyword: '' })
    const userTotal = ref(0)
    const houseQuery = reactive({ pageNum: 1, pageSize: 10, keyword: '' })
    const houseTotal = ref(0)
    const noticeQuery = reactive({ pageNum: 1, pageSize: 10, keyword: '' })
    const noticeTotal = ref(0)
    const repairQuery = reactive({ pageNum: 1, pageSize: 10, keyword: '', status: null })
    const repairTotal = ref(0)
    const paymentQuery = reactive({ pageNum: 1, pageSize: 10, status: null, paymentType: null })
    const paymentTotal = ref(0)

    // Form validation rules
    const houseRules = {
      buildingNo: [{ required: true, message: '请输入楼栋号', trigger: 'blur' }],
      roomNo: [{ required: true, message: '请输入房号', trigger: 'blur' }],
      area: [{ required: true, message: '请输入面积', trigger: 'blur' }]
    }
    const noticeRules = {
      title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
      content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }],
      type: [{ required: true, message: '请选择公告类型', trigger: 'change' }]
    }

    const noticeForm = reactive({
      title: '', content: '', type: 1, isTop: 0,
      publisherId: user.value?.id, publisherName: user.value?.realName
    })

    const getPageTitle = () => {
      const titles = { home: '仪表板', users: '用户管理', houses: '房屋管理', notices: '公告管理', payments: '缴费管理', repairs: '报修管理' }
      return titles[activeMenu.value] || '仪表板'
    }

    const handleMenuSelect = (index) => { activeMenu.value = index }

    const logout = () => {
      localStorage.removeItem('user')
      ElMessage.success('退出成功')
      router.push('/')
    }

    const loadNotices = async () => {
      try {
        const response = await axios.get('/api/notice/list', { params: noticeQuery })
        if (response.data.code === 200) {
          const data = response.data.data
          notices.value = data.records || data
          noticeTotal.value = data.total || 0
        }
      } catch (error) {
        console.error('获取公告失败:', error)
      }
    }

    const editingNoticeId = ref(null)

    const resetNoticeForm = () => {
      editingNoticeId.value = null
      Object.assign(noticeForm, { title: '', content: '', type: 1, isTop: 0, publisherId: user.value?.id, publisherName: user.value?.realName })
    }

    const publishNotice = async () => {
      if (noticeFormRef.value) {
        try { await noticeFormRef.value.validate() } catch { return }
      }
      try {
        let response
        if (editingNoticeId.value) {
          response = await axios.put(`/api/notice/${editingNoticeId.value}`, noticeForm)
        } else {
          response = await axios.post('/api/notice/publish', noticeForm)
        }
        if (response.data.code === 200) {
          ElMessage.success(editingNoticeId.value ? '更新成功' : '发布成功')
          showNoticeDialog.value = false
          resetNoticeForm()
          await loadNotices()
        } else {
          ElMessage.error(response.data.message)
        }
      } catch (error) {
        ElMessage.error(editingNoticeId.value ? '更新失败' : '发布失败')
      }
    }

    const editNotice = (notice) => {
      editingNoticeId.value = notice.id
      Object.assign(noticeForm, { title: notice.title, content: notice.content, type: notice.type, isTop: notice.isTop || 0, publisherId: user.value?.id, publisherName: user.value?.realName })
      showNoticeDialog.value = true
    }

    const deleteNotice = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除这条公告吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
        const response = await axios.delete(`/api/notice/${id}`)
        if (response.data.code === 200) {
          ElMessage.success('删除成功')
          await loadNotices()
        } else {
          ElMessage.error(response.data.message)
        }
      } catch (error) {
        if (error !== 'cancel') ElMessage.error('删除失败')
      }
    }

    const getStatusType = (status) => ({ 0: 'danger', 1: 'warning', 2: 'success' }[status] || '')
    const getStatusText = (status) => ({ 0: '待处理', 1: '处理中', 2: '已完成' }[status] || '未知')
    const getNoticeTypeText = (type) => ({ 1: '通知公告', 2: '停水停电', 3: '活动通知', 4: '温馨提示' }[type] || '其他')
    const formatTime = (timeStr) => new Date(timeStr).toLocaleString()
    const getPaymentTypeText = (type) => ({ 1: '物业费', 2: '停车费', 3: '水费', 4: '电费', 5: '燃气费' }[type] || '其他')

    const loadUsers = async () => {
      try {
        const response = await axios.get('/api/user/list', { params: userQuery })
        if (response.data.code === 200) {
          const data = response.data.data
          users.value = data.records || data
          userTotal.value = data.total || 0
        }
      } catch (error) {
        console.error('获取用户失败:', error)
      }
    }

    const loadHouses = async () => {
      try {
        const response = await axios.get('/api/house/list', { params: houseQuery })
        if (response.data.code === 200) {
          const data = response.data.data
          houses.value = data.records || data
          houseTotal.value = data.total || 0
        }
      } catch (error) {
        console.error('获取房屋失败:', error)
      }
    }

    const loadRepairs = async () => {
      try {
        const params = { ...repairQuery }
        if (params.status === null) delete params.status
        const response = await axios.get('/api/repair/list', { params })
        if (response.data.code === 200) {
          const data = response.data.data
          repairs.value = data.records || data
          repairTotal.value = data.total || 0
          recentRepairs.value = (data.records || data).slice(0, 5)
        }
      } catch (error) {
        console.error('获取报修失败:', error)
      }
    }

    const loadPayments = async () => {
      try {
        const params = { ...paymentQuery }
        if (params.status === null) delete params.status
        if (params.paymentType === null) delete params.paymentType
        const response = await axios.get('/api/payment/list', { params })
        if (response.data.code === 200) {
          const data = response.data.data
          payments.value = data.records || data
          paymentTotal.value = data.total || 0
        }
      } catch (error) {
        console.error('获取缴费失败:', error)
      }
    }

    const viewUser = (row) => {
      selectedUser.value = { ...row }
      viewUserDialogVisible.value = true
    }

    const editHouse = (row) => {
      editingHouseId.value = row.id
      Object.assign(houseForm, { buildingNo: row.buildingNo, unitNo: row.unitNo, roomNo: row.roomNo, area: row.area, ownerName: row.ownerName, ownerPhone: row.ownerPhone })
      showHouseDialog.value = true
    }

    const resetHouseForm = () => {
      editingHouseId.value = null
      Object.assign(houseForm, { buildingNo: '', unitNo: '', roomNo: '', area: '', ownerName: '', ownerPhone: '' })
    }

    const saveHouse = async () => {
      if (houseFormRef.value) {
        try { await houseFormRef.value.validate() } catch { return }
      }
      try {
        let response
        if (editingHouseId.value) {
          response = await axios.put(`/api/house/${editingHouseId.value}`, houseForm)
        } else {
          response = await axios.post('/api/house/add', houseForm)
        }
        if (response.data.code === 200) {
          ElMessage.success(editingHouseId.value ? '更新成功' : '添加成功')
          showHouseDialog.value = false
          resetHouseForm()
          await loadHouses()
        } else {
          ElMessage.error(response.data.message)
        }
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }

    const deleteHouse = async (id) => {
      try {
        await ElMessageBox.confirm('确定删除?', '提示')
        await axios.delete(`/api/house/${id}`)
        ElMessage.success('删除成功')
        await loadHouses()
      } catch (error) {
        if (error !== 'cancel') ElMessage.error('删除失败')
      }
    }

    const handleRepair = async (row) => {
      try {
        await axios.put(`/api/repair/${row.id}/handle`, { handlerId: user.value.id, handlerName: user.value.realName, remark: '已开始处理' })
        ElMessage.success('已开始处理')
        await loadRepairs()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }

    const completeRepair = async (row) => {
      try {
        await axios.put(`/api/repair/${row.id}/complete`, { remark: '已完成' })
        ElMessage.success('已完成')
        await loadRepairs()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }

    const loadStats = async () => {
      try {
        const [u, h, r, p] = await Promise.all([
          axios.get('/api/user/list', { params: { pageNum: 1, pageSize: 1 } }),
          axios.get('/api/house/list', { params: { pageNum: 1, pageSize: 1 } }),
          axios.get('/api/repair/list', { params: { pageNum: 1, pageSize: 1, status: 0 } }),
          axios.get('/api/payment/list', { params: { pageNum: 1, pageSize: 1000, status: 1 } })
        ])
        if (u.data.code === 200) stats.userCount = u.data.data.total || 0
        if (h.data.code === 200) stats.houseCount = h.data.data.total || 0
        if (r.data.code === 200) stats.pendingRepairs = r.data.data.total || 0
        if (p.data.code === 200) {
          const paid = p.data.data.records || []
          stats.monthIncome = paid.reduce((s, i) => s + (i.actualPayAmount || 0), 0).toFixed(2)
        }
      } catch (e) { console.error('加载统计失败:', e) }
    }

    onMounted(() => {
      loadNotices()
      loadUsers()
      loadHouses()
      loadRepairs()
      loadPayments()
      loadStats()
    })

    return {
      activeMenu, user, notices, recentRepairs, showNoticeDialog, noticeFormRef, noticeForm, noticeRules,
      users, houses, repairs, payments, showHouseDialog, houseFormRef, stats,
      userQuery, userTotal, houseQuery, houseTotal, noticeQuery, noticeTotal,
      repairQuery, repairTotal, paymentQuery, paymentTotal,
      houseRules,
      getPageTitle, handleMenuSelect, logout,
      publishNotice, editNotice, editingNoticeId, resetNoticeForm, deleteNotice,
      getStatusType, getStatusText, getNoticeTypeText, getPaymentTypeText, formatTime,
      loadUsers, loadHouses, loadRepairs, loadPayments, loadNotices, loadStats,
      viewUser, viewUserDialogVisible, selectedUser,
      editHouse, editingHouseId, houseForm, resetHouseForm, saveHouse, deleteHouse,
      handleRepair, completeRepair
    }
  }
}
</script>

<style scoped>
.admin-dashboard { height: 100vh; }
.layout-container { height: 100%; }
.sidebar { background: #001529; color: white; }
.logo { padding: 20px; text-align: center; border-bottom: 1px solid #2c3e50; }
.logo h3 { margin: 0; color: white; }
.sidebar-menu { border: none; }
.header { display: flex; justify-content: space-between; align-items: center; background: white; border-bottom: 1px solid #e8e8e8; padding: 0 20px; }
.header-left h2 { margin: 0; color: #303133; }
.header-right { display: flex; align-items: center; gap: 15px; }
.main-content { background: #f5f5f5; }
.stat-card .el-card__body { display: flex; align-items: center; padding: 15px; }
.stat-icon { margin-right: 15px; }
.stat-value { font-size: 24px; font-weight: bold; color: #303133; }
.stat-label { color: #606266; font-size: 14px; }
.quick-actions { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
.quick-actions .el-button { justify-self: stretch; }
.toolbar { margin-bottom: 20px; display: flex; justify-content: space-between; align-items: flex-start; flex-wrap: wrap; gap: 10px; }
.search-form { display: inline-flex; }
.coming-soon { display: flex; justify-content: center; align-items: center; height: 400px; }
</style>
