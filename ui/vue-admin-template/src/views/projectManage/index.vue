<template>
  <div class="user-manage-container">
    <template>
      <el-collapse accordion>
        <el-collapse-item>
          <template slot="title">
            <span style="font-size: 16px"><b>查询条件</b></span>
          </template>
          <el-card style="max-width: 100%;">
            <template #default>
              <el-row>
                <el-col :span="18">
                  <el-form
                    :inline="true"
                    :model="queryForm"
                    class="demo-form-inline"
                    size="small"
                    label-width="80px"
                  >
                    <el-form-item label="名称">
                      <el-input v-model="queryForm.name" placeholder="请输入项目名称" style="width: 200px" />
                    </el-form-item>
                    <el-form-item label="描述">
                      <el-input v-model="queryForm.description" placeholder="请输入项目描述" style="width: 200px" />
                    </el-form-item>
                    <el-form-item label="地址">
                      <el-input v-model="queryForm.url" placeholder="请输入项目仓库地址" style="width: 200px" />
                    </el-form-item>
                    <el-form-item v-if="role === 'admin'" label="项目经理">
                      <el-select
                        v-model="queryForm.managerId"
                        clearable
                        filterable
                        remote
                        reserve-keyword
                        placeholder="请搜索项目经理"
                        :remote-method="(query) => getUserByName(query)"
                        :loading="queryListLoading"
                        style="width: 240px"
                      >
                        <el-option v-for="item in userList" :key="item.id" :label="item.name+'（'+(item.role === 'manager'? '项目经理':'普通用户')+'）'" :value="item.id">
                          <span style="float: left">{{ item.name }}</span>
                          <el-divider direction="vertical" />
                          <span style="float: none;color: #909399;font-size: 13px;">
                            工号：{{ item.workId }}
                          </span>
                          <el-divider direction="vertical" />
                          <span style="float: none;color: #909399;font-size: 13px;">
                            账号：{{ item.account }}
                          </span>
                          <el-divider direction="vertical" />
                          <span style="float: none;color: #909399;font-size: 13px;">
                            角色：{{ item.role === 'manager'? '项目经理':'普通用户' }}
                          </span>
                        </el-option>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="普通用户">
                      <el-select
                        v-model="queryForm.userId"
                        clearable
                        filterable
                        remote
                        reserve-keyword
                        placeholder="请搜索普通用户"
                        :remote-method="(query) => getUserByName(query)"
                        :loading="queryListLoading"
                        style="width: 240px"
                      >
                        <el-option v-for="item in userList" :key="item.id" :label="item.name+'（'+(item.role === 'manager'? '项目经理':'普通用户')+'）'" :value="item.id">
                          <span style="float: left">{{ item.name }}</span>
                          <el-divider direction="vertical" />
                          <span style="float: none;color: #909399;font-size: 13px;">
                            工号：{{ item.workId }}
                          </span>
                          <el-divider direction="vertical" />
                          <span style="float: none;color: #909399;font-size: 13px;">
                            账号：{{ item.account }}
                          </span>
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-form>
                </el-col>
                <el-col :span="6" style="text-align: center">
                  <template>
                    <el-button :loading="queryLoading" type="primary" size="small" style="width: 80px" @click="handleQuery">查询</el-button>
                  </template>
                  <template>
                    <el-button :loading="queryLoading" size="small" style="width: 80px" @click="handleReset">重置</el-button>
                  </template>
                </el-col>
              </el-row>
            </template>
          </el-card>
        </el-collapse-item>
        <el-collapse-item>
          <template slot="title">
            <span style="font-size: 16px"><b>更多操作</b></span>
          </template>
          <template>
            <template>
              <el-button v-if="role === 'manager'" type="primary" size="small" style="width: 150px" @click="openAddProjectDialog">添加项目信息</el-button>
            </template>
            <template>
              <el-button type="success" size="small" style="width: 150px" @click="handleExport">导出项目信息</el-button>
            </template>
          </template>
        </el-collapse-item>
      </el-collapse>
    </template>
    <template>
      <el-table
        v-loading="pageLoading"
        :data="queryResult.records"
        stripe
        style="width: 100%"
      >
        <el-table-column
          v-if="false"
          prop="id"
          label="ID"
          align="center"
        />
        <el-table-column
          prop="name"
          label="名称"
          align="center"
        />
        <el-table-column
          prop="description"
          label="描述"
          align="center"
        />
        <el-table-column
          prop="url"
          label="仓库地址"
          align="center"
        >
          <template #default="scope">
            <el-link v-if="scope.row.url" :href="scope.row.url" type="primary" target="_blank">点击前往</el-link>
            <el-link v-else type="danger" target="_blank" disabled>无仓库地址</el-link>
          </template>
        </el-table-column>
        <el-table-column
          prop="managerName"
          label="项目经理"
          align="center"
        />
        <el-table-column
          prop="userInfoJson"
          label="项目成员"
          align="center"
        >
          <template #default="scope">
            <el-table
              v-if="scope.row.userInfoJson && Object.keys(scope.row.userInfoJson).length > 0"
              :show-header="false"
              :data="formatUserInfoJson(scope.row.userInfoJson)"
            >
              <el-table-column v-if="false" prop="key" label="ID" align="center" />
              <el-table-column prop="value" label="名称" align="center" />
            </el-table>
            <div v-else>暂无成员信息</div>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          align="center"
        />
        <el-table-column
          fixed="right"
          label="操作"
          width="350"
        >
          <template #default="scope">
            <el-button v-if="role === 'manager'" type="primary" size="mini" @click="openUpdateDialog(scope.row)">修改</el-button>
            <el-divider v-if="role === 'manager'" direction="vertical" />
            <el-button v-if="role === 'manager'" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
            <el-divider v-if="role === 'manager'" direction="vertical" />
            <el-dropdown v-if="role === 'manager'" @command="(data)=>void openMemberDialog(data,scope.row)">
              <el-button type="primary" size="mini">
                成员信息
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="add">新增</el-dropdown-item>
                <el-dropdown-item command="delete">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </template>
    <template>
      <el-pagination
        :current-page="Number.parseInt(queryResult.current)"
        :page-sizes="[10, 30, 50, 100]"
        :page-size="10"
        layout="total, sizes, prev, pager, next, jumper"
        :total="Number.parseInt(queryResult.total)"
        style="text-align: right;margin-top: 10px"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </template>
    <template>
      <el-dialog :show-close="false" title="添加项目" :visible.sync="addProjectDialogVisible" @close="handleCancelAdd">
        <el-form ref="addProjectForm" :model="addProjectForm" label-width="80px">
          <el-form-item
            label="项目名称"
            prop="name"
            :rules="[
              {required:true,message:'项目名称不能为空',trigger: 'blur'}
            ]"
          >
            <el-input v-model="addProjectForm.name" autocomplete="off" placeholder="请输入项目名称" />
          </el-form-item>
          <el-form-item
            label="项目描述"
            prop="description"
            :rules="[
              {required:true,message:'项目描述不能为空',trigger: 'blur'}
            ]"
          >
            <el-input v-model="addProjectForm.description" autocomplete="off" placeholder="请输入项目描述" />
          </el-form-item>
          <el-form-item
            label="仓库地址"
            prop="url"
          >
            <el-input v-model="addProjectForm.url" autocomplete="off" placeholder="请输入仓库地址（请以 http:// 或 https:// 开头）" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="handleCancelAdd">取 消</el-button>
          <el-button type="primary" :loading="addLoading" @click="handleAddProject">添 加</el-button>
        </div>
      </el-dialog>
    </template>
    <template>
      <el-dialog :show-close="false" title="修改项目" :visible.sync="updateDialogVisible" @close="handleCancelUpdate">
        <el-form ref="updateForm" :model="updateForm" label-width="80px">
          <el-form-item
            label="ID"
            prop="id"
            :rules="[
              {required:true,message:'项目ID不能为空',trigger: 'blur'}
            ]"
          >
            <el-input v-model="updateForm.id" disabled />
          </el-form-item>
          <el-form-item
            label="项目名称"
            prop="name"
            :rules="[
              {required:true,message:'项目名称不能为空',trigger: 'blur'}
            ]"
          >
            <el-input v-model="updateForm.name" autocomplete="off" placeholder="请输入项目名称" />
          </el-form-item>
          <el-form-item
            label="项目描述"
            prop="description"
            :rules="[
              {required:true,message:'项目描述不能为空',trigger: 'blur'}
            ]"
          >
            <el-input v-model="updateForm.description" autocomplete="off" placeholder="请输入项目描述" />
          </el-form-item>
          <el-form-item
            label="仓库地址"
            prop="url"
          >
            <el-input v-model="updateForm.url" autocomplete="off" placeholder="请输入仓库地址（请以 http:// 或 https:// 开头）" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="handleCancelUpdate">取 消</el-button>
          <el-button type="primary" :loading="updateLoading" @click="handleUpdate">修 改</el-button>
        </div>
      </el-dialog>
    </template>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { Loading, Message } from 'element-ui'
import {
  adminAddProject,
  adminDeleteProject,
  adminExportExcel,
  adminPageProject,
  adminUpdateProject
} from '@/api/project'
import { adminListUser } from '@/api/user'

export default {
  name: 'ProjectManage',
  data() {
    return {
      fileList: [],
      addLoading: false,
      queryLoading: false,
      queryListLoading: false,
      updateLoading: false,
      pageLoading: false,
      queryResult: [],
      userList: [],
      queryForm: {
        name: undefined,
        description: undefined,
        url: undefined,
        managerId: undefined,
        userId: undefined,
        page: 1,
        size: 10,
        allowDeep: false
      },
      addProjectDialogVisible: false,
      addProjectForm: {
        name: undefined,
        description: undefined,
        url: undefined
      },
      importDialogVisible: false,
      updateDialogVisible: false,
      updateType: undefined,
      updateForm: {
        id: undefined,
        name: undefined,
        description: undefined,
        url: undefined
      }
    }
  },
  computed: {
    ...mapGetters([
      'role',
      'id'
    ])
  },
  created() {
    this.pageLoading = true
    adminPageProject(this.queryForm).then(response => {
      this.queryResult = response.data
      this.pageLoading = false
    })
  },
  methods: {
    formatUserInfoJson(userInfoJson) {
      try {
        const parsedData = JSON.parse(userInfoJson) // 解析 JSON 字符串
        return Object.keys(parsedData).map(key => ({
          key: key,
          value: parsedData[key]
        }))
      } catch (error) {
        console.error('Invalid JSON format:', error)
        return []
      }
    },
    handleQuery() {
      if (
        this.queryForm.name === undefined &&
        this.queryForm.description === undefined &&
        this.queryForm.url === undefined &&
        this.queryForm.managerId === undefined &&
        this.queryForm.userId === undefined
      ) {
        return
      }
      this.queryForm.page = 1
      this.pageLoading = true
      this.queryLoading = true
      adminPageProject(this.queryForm).then(response => {
        this.queryResult = response.data
        this.pageLoading = false
        this.queryLoading = false
      })
    },
    handleReset() {
      if (
        this.queryForm.name === undefined &&
        this.queryForm.description === undefined &&
        this.queryForm.url === undefined &&
        this.queryForm.managerId === undefined &&
        this.queryForm.userId === undefined
      ) {
        return
      }
      this.resetQueryForm()
      this.pageLoading = true
      adminPageProject(this.queryForm).then(response => {
        this.queryResult = response.data
        this.pageLoading = false
      })
    },
    async getUserByName(query) {
      if (query) {
        const data = {
          name: query
        }
        this.userList = await adminListUser(data).then(response => {
          return response.data
        })
      }
    },
    handleExport() {
      const downloadLoadingInstance = Loading.service({
        text: '正在下载数据，请稍候',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      adminExportExcel().then(async data => {
        if (data) {
          const blob = new Blob([data.data])
          const url = URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = decodeURIComponent(data.headers['download-filename'])
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          URL.revokeObjectURL(url)
        } else {
          Message.error('下载失败')
        }
        downloadLoadingInstance.close()
      }).catch(() => {
        Message.error('下载失败')
        downloadLoadingInstance.close()
      })
    },
    openAddProjectDialog() {
      this.addProjectDialogVisible = true
    },
    handleAddProject() {
      this.$refs['addProjectForm'].validate(async valid => {
        if (valid) {
          this.addLoading = true
          const data = {
            name: this.addProjectForm.name,
            description: this.addProjectForm.description,
            url: this.addProjectForm.url
          }
          adminAddProject(data).then(response => {
            if (this.queryResult.total !== 0 && this.queryResult.total % this.queryResult.size === 0) {
              this.queryForm.page++
            }
            this.pageLoading = true
            adminPageProject(this.queryForm).then(response => {
              this.queryResult = response.data
              console.log(this.queryResult)
              this.pageLoading = false
            })
            this.resetAddProjectForm()
            this.addProjectDialogVisible = false
            Message.success(response.msg)
          }).finally(() => {
            this.addLoading = false
          })
        }
      })
    },
    handleCancelAdd() {
      this.addProjectDialogVisible = false
      this.resetAddProjectForm()
      this.addLoading = false
    },
    openUpdateDialog(data) {
      this.updateForm.id = data.id
      this.updateForm.name = data.name
      this.updateForm.description = data.description
      this.updateForm.url = data.url
      this.updateDialogVisible = true
    },
    handleUpdate() {
      this.$refs['updateForm'].validate(valid => {
        if (valid) {
          this.updateLoading = true
          const data = {
            id: this.updateForm.id,
            name: this.updateForm.name,
            description: this.updateForm.description,
            url: this.updateForm.url
          }
          adminUpdateProject(data).then(response => {
            this.pageLoading = true
            adminPageProject(this.queryForm).then(response => {
              this.queryResult = response.data
              this.pageLoading = false
            })
            this.resetUpdateForm()
            this.updateDialogVisible = false
            Message.success(response.msg)
          }).finally(() => {
            this.updateLoading = false
          })
        }
      })
    },
    handleCancelUpdate() {
      this.$refs['updateForm'].resetFields()
      this.updateDialogVisible = false
      this.resetUpdateForm()
      this.updateLoading = false
    },
    handleDelete(data) {
      this.$confirm('此操作将删除该项目, 是否继续?', '确认删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        adminDeleteProject(data.id).then(response => {
          if (this.queryResult.total % this.queryResult.size === 1) {
            this.queryForm.page--
          }
          this.pageLoading = true
          adminPageProject(this.queryForm).then(response => {
            this.queryResult = response.data
            this.pageLoading = false
          })
          Message.success(response.msg)
        })
      })
    },
    handleSizeChange(val) {
      this.queryForm.size = val
      this.pageLoading = true
      adminPageProject(this.queryForm).then(response => {
        this.queryResult = response.data
        this.pageLoading = false
      })
    },
    handleCurrentChange(val) {
      this.queryForm.page = val
      this.pageLoading = true
      adminPageProject(this.queryForm).then(response => {
        this.queryResult = response.data
        this.pageLoading = false
      })
    },
    async resetQueryForm() {
      this.queryForm.name = undefined
      this.queryForm.description = undefined
      this.queryForm.url = undefined
      this.queryForm.managerId = undefined
      this.queryForm.userId = undefined
      this.queryForm.page = 1
    },
    async resetAddProjectForm() {
      this.addProjectForm.name = undefined
      this.addProjectForm.description = undefined
      this.addProjectForm.url = undefined
    },
    async resetUpdateForm() {
      this.updateForm.id = undefined
      this.updateForm.name = undefined
      this.updateForm.description = undefined
      this.updateForm.url = undefined
    }
  }
}
</script>

<style lang="scss" scoped>
$dark_gray: #889aa4;

.user-manage {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

.avatar-class {
  img {
    width: 100%;
    background-size: cover;
  }
}

.show-pwd {
  position: absolute;
  right: 10px;
  top: 7px;
  font-size: 16px;
  color: $dark_gray;
  cursor: pointer;
  user-select: none;
}
</style>
