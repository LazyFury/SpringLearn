<template>
  <div>
    <ElCard shadow="never" v-if="meta.table">
      <div class="flex flex-row">
        <div>
          <h1 class="mb-0 mt-2">{{ meta.table.name || meta.name }}</h1>
          <p class="text-gray">{{ meta.table.description || "-" }}</p>
        </div>
        <div class="flex-grow">
          <div class="flex flex-row-reverse"></div>
        </div>
      </div>
      <ElDivider class="!mb-4 !mt-2"></ElDivider>

      <!-- meta filters  -->
      <div v-if="filters && filters.length > 0">
        <ElForm
          :model="searchForm"
          @submit.prevent.native="(e) => {}"
          class="mb-2"
        >
          <div v-for="f in filters || []" :key="f.name" class="mb-2">
            <ElFormItem :label="f.label">
              <FormItem
                :field="f"
                v-model="searchForm[f.name]"
                @change="handleFilterChange"
              ></FormItem>
            </ElFormItem>
          </div>
        </ElForm>

        <ElDivider class="!mb-4 !mt-4"></ElDivider>
      </div>

      <!-- search form  -->
      <div v-if="searchFormFields && searchFormFields.length > 0">
        <ElForm
          :inline="true"
          :model="searchForm"
          @submit.prevent.native="(e) => {}"
          class="mb-2"
        >
          <div v-for="fields in searchFormFields">
            <ElFormItem
              v-for="field in fields"
              :key="field.name"
              :label="field.label"
              :prop="field.field"
              :class="[]"
              :style="{ 'min-width': field.width || '100px' }"
            >
              <FormItem
                :field="field"
                v-model="searchForm[field.name]"
              ></FormItem>
            </ElFormItem>
          </div>
          <ElFormItem>
            <ElButton type="primary" @click="submitSearch">
              <Icon
                icon="heroicons-solid:magnifying-glass"
                class="mt-0px mr-4px"
              ></Icon>
              <span>{{ $t("search") }}</span>
            </ElButton>
            <!-- reset  -->
            <ElButton type="default" @click="resetSearchForm">
              <Icon icon="la:trash-restore-alt" class="mt-0px mr-4px"></Icon>
              <span>{{ $t("reset") }}</span>
            </ElButton>
          </ElFormItem>
        </ElForm>
      </div>

      <!-- betch actions  -->
      <div class="mb-4 flex flex-row items-center flex-wrap">
        <ElButton
          v-if="api.createAble"
          :disabled="!canAdd"
          type="primary"
          @click="add"
        >
          <Icon icon="ant-design:plus-outlined"></Icon>
          <span>添加</span>
        </ElButton>
        <ElButton :loading="loading" type="default" @click="load">
          <Icon v-if="!loading" icon="ant-design:reload-outlined"></Icon>
          <span>{{ $t("refresh") }}</span>
        </ElButton>

        <!-- divider vertical  -->
        <ElDivider direction="vertical" class="mx-4"></ElDivider>
        <ElButton
          :type="action.btnType"
          @click="batchAction(action.action)"
          v-for="action in meta.table?.batchActions || []"
          :key="action.name"
        >
          {{ action.label }}
        </ElButton>
        <ElButton type="danger" @click="handleBatchDelete()">
          <span>批量删除</span>
        </ElButton>

        <div class="flex-1"></div>

        <!-- 导出 -->
        <ElButton type="default" @click="exportData">
          <Icon icon="ant-design:export-outlined"></Icon>
          <span>导出</span>
        </ElButton>
        <ElButton type="default">
          <Icon icon="ant-design:exclamation-circle-outlined"></Icon>
          <span>表格设置</span>
        </ElButton>
      </div>
      <div class="overflow-x-auto">
        <ElTable
          ref="tableRef"
          size="default"
          v-loading="loading"
          :data="tableData"
          :border="true"
          stripe
          :tree-props="{
            hasChildren: 'hasChildren',
            children: 'children',
          }"
          row-key="id"
          @sort-change="handleSortChange"
        >
          <!-- selection  -->
          <ElTableColumn type="selection" width="55"></ElTableColumn>

          <!-- id  -->
          <slot name="tableIdColumn">
            <ElTableColumn
              v-if="!hasIdColumn(columns)"
              label="ID"
              width="80"
              prop="id"
            ></ElTableColumn>
          </slot>

          <ElTableColumn
            v-for="column in columns"
            :key="column.prop"
            :sortable="column.sortable ? 'custom' : false"
            :label="column.label"
            :width="column.width"
          >
            <template #default="{ row }" v-if="!column.slot">
              <div :class="[column.className]" v-if="column.type == 'render'">
                {{ column.render(row) }}
              </div>
              <!-- switch  -->
              <ElSwitch
                v-if="column.type == 'switch'"
                v-model="row[column.key]"
                inactive-color="#ff4949"
                active-text=""
                inactive-text=""
                disabled
              ></ElSwitch>
              <!-- checkbox  -->
              <div v-if="column.type == 'checkbox'">
                <ElCheckbox v-model="row[column.key]" disabled></ElCheckbox>
              </div>

              <!-- select  -->
              <ElSelect
                v-if="column.type == 'select'"
                v-model="row[column.key]"
                :placeholder="column.placeholder"
                clearable
              >
                <ElOption
                  v-for="option in column.options"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></ElOption>
              </ElSelect>
              <!-- icon  -->
              <Icon
                v-if="column.type == 'icon'"
                :icon="row[column.key]"
                :class="[column.className]"
              >
              </Icon>
              <!-- image  -->
              <ElImage
                v-if="column.type == 'image'"
                :src="$img(row[column.key])"
                fit="cover"
                :preview-teleported="true"
                :preview-src-list="
                  row[column.key] ? [$img(row[column.key])] : []
                "
                style="width: 50px; height: 50px"
              ></ElImage>
              <ElTag
                v-if="column.type == 'tag'"
                :type="column.epType || 'success'"
                >{{ row[column.key] }}
              </ElTag>

              <!-- link  -->
              <ElLink
                type="primary"
                v-if="column.type == 'link'"
                :underline="false"
                :href="makeUrl(row, column, column.link_key)"
                :target="column.url_target || '_self'"
              >
                {{ column.prefix }}{{ row[column.key] }}{{ column.suffix }}
              </ElLink>

              <!-- span  -->
              <span
                v-if="column.type == 'span'"
                v-html="row[column.key]"
              ></span>

              <!-- multi-tag split by , -->
              <div class="flex flex-row flex-wrap gap-2">
                <ElTag
                  v-if="column.type == 'string-multi-tag'"
                  :type="handleColumnEpType(row, column, tag)"
                  v-for="tag in row[column.key].split(',')"
                  :key="tag"
                  >{{ tag }}
                </ElTag>
              </div>
            </template>

            <template v-if="column.slot" #default="{ row }">
              <slot :name="column.slot" :row="row"></slot>
            </template>
          </ElTableColumn>

          <!-- actions  -->
          <ElTableColumn
            fixed="right"
            v-if="actions?.length"
            label="操作"
            min-width="150px"
          >
            <template #default="{ row }">
              <ElButton
                v-for="action in actions"
                link
                :key="action.key"
                :type="action.type || 'primary'"
                @click="action.handler(row)"
              >
                {{ action.title }}
              </ElButton>
            </template>
          </ElTableColumn>
        </ElTable>
      </div>
      <!-- pagination  -->
      <div class="flex mt-2">
        <ElPagination
          small
          layout="total,sizes, prev, pager, next, jumper"
          background
          :hide-on-single-page="false"
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[5, 8, 10, 20, 50, 100]"
          :total="pagination.total"
          @current-change="handleCurrentPageChange"
          @size-change="handlePageSizeChange"
        ></ElPagination>
      </div>
    </ElCard>

    <slot name="addModal">
      <ElDialog
        v-if="canAdd"
        title="提示"
        v-model="editModal"
        class="!md:w-640px !w-full !lg:w-960px"
      >
        <template #header>
          <div></div>
        </template>
        <slot name="addForm">
          <Form
            ref="formRef"
            :title="meta.title"
            :defaultForm="addFormDefault"
            :fields="addForm"
            @save="handleAddSubmit"
          >
          </Form>
        </slot>
      </ElDialog>
    </slot>
  </div>
</template>

<script>
/**
 * @name TableView
 * @description
 * 1. 通用表格组件
 * 2. 支持搜索、分页、批量操作、导出
 * 3. 支持自定义列、自定义操作
 * 4. 支持自定义表单
 * 5. 支持自定义过滤器
 * 6. !!! 请注意，该组件依赖 element-plus
 * 7. 该组件需要在路由的 meta 中配置 table 字段
 * 8. 支持继承组件,支持dialog  edit form 插槽，支持tableIdColumn(column.prop是插槽名)插槽
 */

import { ElDivider, ElForm, ElPagination, ElRadioGroup } from "element-plus";
import { request } from "@/api/request";
import Form from "@/views/components/Form.vue";
import FormItem from "./components/FormItem.vue";

export default {
  components: { ElPagination, Form, FormItem },
  props: {},
  data() {
    return {
      meta: this.$route.meta.menu || {},
      searchForm: {},
      pagination: {
        currentPage: 1,
        pageSize: this.$route.meta?.table?.pageSize || 10,
        total: 0,
      },
      tableData: [],
      loading: false,
      editModal: false,
    };
  },
  watch: {},
  computed: {
    table() {
      return this.meta.table || {};
    },
    api() {
      return this.table.api;
    },
    searchFormFields() {
      return this.meta?.table?.search?.rows;
    },
    addForm() {
      return this.meta?.table?.edit?.rows;
    },
    canAdd() {
      return this.addForm && this.addForm.length > 0;
    },
    addFormDefault() {
      let obj = {};
      this.addForm.forEach((arr) => {
        arr.forEach((field) => {
          obj[field.name] = field.defaultValue;
          if (
            typeof field.defaultValue == null ||
            field.defaultValue == undefined
          ) {
            obj[field.name] = "";
          }
        });
      });
      return obj;
    },
    filters() {
      return this.meta.table?.filters;
    },
    columns() {
      let columns = this.meta.table?.columns || this.meta.columns || [];
      if (typeof columns === "string") {
        columns = JSON.parse(columns);
      }
      if (typeof columns === "string") {
        return;
      }
      console.log(columns);
      columns =
        columns
          ?.map((v) => ({
            ...v,
            dataIndex: v.dataIndex ? v.dataIndex : 999,
          }))
          .sort((a, b) => a.dataIndex - b.dataIndex) || [];
      return (columns || []).map((column) => {
        return {
          ...column,
          type: column.type || "render",
          width: column.width || "auto",
          render: (row) => {
            if (column.slot) return "";
            if (column.type === "" || column.type === "render") {
              let {
                valueType: type,
                key,
                mapping_key,
                data = [],
                def,
                formatStr = "",
                prefix = "",
                suffix = "",
              } = column || {};
              let formatConfig = column.formatter;
              if (type === "mapping") {
                console.log(data);
                console.log(row[column.key]);
                return (
                  data.find((item) => item[key] == row[column.key])?.[
                    mapping_key
                  ] ||
                  def ||
                  ""
                );
              }

              if (type === "date") {
                return row[column.key]
                  ? this.$dayjs(row[column.key]).format("YYYY-MM-DD HH:mm:ss")
                  : "";
              }

              if (type === "datetime") {
                return row[column.key]
                  ? this.$dayjs(row[column.key]).format("YYYY-MM-DD HH:mm:ss")
                  : "";
              }
              // number
              if (type === "number") {
                let result = row[column.key]
                  ? this.$numeral(row[column.key]).format(formatStr || "0,0.00")
                  : "";
                return `${prefix}${result}${suffix}`;
              }

              // bool
              if (type === "boolean") {
                return row[column.key]
                  ? formatConfig.trueText || "是"
                  : formatConfig.falseText || "否";
              }
            }
            return row[column.key];
          },
        };
      });
    },
    actions() {
      return (
        this.meta.table?.actions || [
          ...[
            this.api.updateAble && {
              key: "edit",
              title: "编辑",
              type: "primary",
            },
          ],
          ...[
            this.api.deleteAble && {
              key: "delete",
              title: "删除",
              type: "danger",
            },
          ],
        ]
      ).map((action) => {
        return {
          ...action,
          handler: (row) => {
            console.log(row);
            if (action.key === "delete") {
              this.handleBatchDelete([row.id]);
            }
            if (action.key === "edit") {
              this.editModal = true;
              this.$nextTick(() => {
                this.$refs.formRef?.edit(row);
                this.$emit("edit", row);
              });
            }
          },
        };
      });
    },
  },
  methods: {
    add() {
      this.editModal = true;
      this.$nextTick(() => {
        this.$refs.formRef?.add({});
        this.$emit("add");
      });
    },
    submitSearch() {
      console.log(this.searchForm);
      this.load();
    },
    resetSearchForm() {
      this.searchForm = {};
      this.setDefaultSearchForm();

      this.load();

      // reset table sort
      this.$refs.tableRef.clearSort();
    },
    handlePageSizeChange(val) {
      this.pagination.pageSize = val;
      this.load();

      let url = new URL(location.href);
      localStorage.setItem("pageSize_" + btoa(url), val);
    },
    handleCurrentPageChange(val) {
      this.pagination.currentPage = val;
      this.load();
    },
    load() {
      this.loading = true;
      let form = JSON.parse(JSON.stringify(this.searchForm));
      for (let key in form) {
        if (form[key] === "") {
          delete form[key];
        }
        if (form[key] == "skip__all") {
          delete form[key];
        }
        // if is array
        if (Array.isArray(form[key])) {
          form[key] = form[key].join(",");
        }
      }
      request({
        url: this.api.list,
        method: "get",
        params: {
          page: this.pagination.currentPage,
          limit: this.pagination.pageSize,
          ...form,
        },
      })
        .then((res) => {
          let data = res.data?.data || {};
          this.tableData = data?.data || [];
          let { size, page, total } = data;
          this.pagination = {
            pageSize: size,
            currentPage: page,
            total,
          };
        })
        .catch((err) => {
          this.tableData = [];
        })
        .finally(() => {
          setTimeout(() => {
            this.loading = false;
          }, 200);
        });
    },
    handleSortChange({ column, order }) {
      if (!order) return;
      let { no } = column;
      let col = this.table.columns[no - 2]; // 1 个 id，1 个操作，所以减 2
      console.log(col);
      let key = `${col.prop}__sort`;
      this.searchForm[key] = order === "ascending" ? "asc" : "desc";
      this.load();
    },
    getTableSelection() {
      return this.$refs.tableRef?.getSelectionRows() || [];
    },
    getTableSelectionIds() {
      return this.getTableSelection().map((item) => item.id);
    },
    batchAction(key) {
      let actionMap = {
        delete: () => this.handleBatchDelete(),
      };
      actionMap[key]?.();
    },
    handleBatchDelete(ids = null) {
      if (!ids) ids = this.getTableSelectionIds();
      if (!ids.length) return;
      this.$confirm("确定删除选中的数据吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          console.log(ids);
          request
            .delete(this.api.delete, {
              data: {
                ids,
              },
            })
            .then((res) => {
              if (res.data.code == 200) {
                this.$message.success("删除成功");
                this.load();
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    exportData() {
      this.$confirm("确定导出当前数据吗？", "提示", {
        confirmButtonText: this.$t("export"),
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.exportDataApi();
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消导出",
          });
        });
    },
    exportDataApi() {
      console.log("export");
      let type = "xlsx";
      let contentType = {
        xlsx: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        xls: "application/vnd.ms-excel",
      }[type];

      let fileExt = {
        xlsx: ".xlsx",
        xls: ".xls",
      }[type];

      request({
        url: this.api.export,
        method: "get",
        responseType: "blob",
        params: {
          ...this.searchForm,
        },
        binary: true,
      }).then((res) => {
        let blob = new Blob([res.data], { type: contentType });
        let url = window.URL.createObjectURL(blob);
        let link = document.createElement("a");
        link.style.display = "none";
        link.href = url;
        let filename = `${this.meta.name}-导出-${this.$dayjs().format(
          "YYYYMMDDHHmmss"
        )}`;
        link.setAttribute("download", `${filename}.${fileExt}`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      });
    },
    handleAddSubmit(form) {
      console.log(form);
      if (form.id) {
        request.put(this.api.update, form).then((res) => {
          if (res.data?.code == 200) {
            this.$message.success("修改成功");
            this.editModal = false;
            this.load();
          }
        });
        return;
      }

      request.post(this.api.create, form).then((res) => {
        if (res.data?.code == 200) {
          this.$message.success("添加成功");
          this.editModal = false;
          this.load();
        }
      });
    },
    makeUrl(row, column, key) {
      let url = column.url_prefix || "";
      return url + row[key];
    },
    handleQueryToSearchForm(query) {
      for (let key in query) {
        if (this.searchFormFields.find((v) => v.name === key)) {
          // if num try
          if (query[key] && !isNaN(query[key])) {
            query[key] = Number(query[key]);
          }

          // if array
          if (/\,/.test(query[key])) {
            query[key] = (query[key].split(",") || [])
              .filter((v) => v)
              .map((v) => {
                if (v && !isNaN(v)) {
                  return Number(v);
                }
                return v;
              });
          }

          this.searchForm[key] = query[key];
        }
      }

      this.setDefaultSearchForm();

      this.load();
    },
    setDefaultSearchForm() {
      let searchFields = (this.searchFormFields || []).reduce((acc, cur) => {
        return acc.concat(cur);
      }, []);

      for (let field of searchFields) {
        if (field.default) {
          this.searchForm[field.name] = field.default;
        }
      }

      let fillterFields = this.filters || [];
      for (let field of fillterFields) {
        if (field.default) {
          this.searchForm[field.name] = field.default;
        }
      }
    },
    hasIdColumn(columns) {
      return columns.find((v) => v.key === "id");
    },

    handleColumnEpType(row, column, tag) {
      let type = column.epType || "success";
      // if type == rand
      if (type === "rand") {
        let map = {
          0: "info",
          1: "success",
          2: "warning",
          3: "danger",
        };
        let hash = btoa(tag);
        let index = (hash.charCodeAt(0) + hash.charCodeAt(1)) % 4;
        type = map[index];
      }
      return type;
    },

    handleFilterChange(val) {
      this.load();
    },
  },
  created() {},
  mounted() {
    // load pageSize from localStorage
    let url = new URL(location.href);
    let pageSize = localStorage.getItem("pageSize_" + btoa(url));
    if (pageSize) {
      this.pagination.pageSize = pageSize;
    }

    this.handleQueryToSearchForm(this.$route.query);
    // this.load()
  },
};
</script>

<style lang="scss" scoped></style>
