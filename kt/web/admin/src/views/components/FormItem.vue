<template>
  <!-- textarea  -->
  <ElInput
    @change="handleUpdate"
    v-model="value"
    v-if="fieldType == 'textarea'"
    type="textarea"
    :placeholder="field.placeholder"
  ></ElInput>
  <!-- password  -->
  <ElInput
    @change="handleUpdate"
    v-model="value"
    v-if="fieldType == 'password'"
    type="password"
    :placeholder="field.placeholder"
  >
  </ElInput>
  <!-- select  -->
  <div v-if="fieldType == 'select'" class="flex flex-row items-center w-full">
    <ElSelect
      class="w-full"
      filterable
      @change="handleUpdate"
      v-model="value"
      :placeholder="field.placeholder"
    >
      <ElOption
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      ></ElOption>
    </ElSelect>
    <div class="mr-2"></div>
    <!-- refresh  -->
    <ElButton @click="getOptions" type="text" link>
      <Icon icon="el:refresh" class=""></Icon>
    </ElButton>
  </div>

  <!-- select multi  -->
  <ElSelect
    v-if="fieldType == 'select-multi'"
    filterable
    @change="handleUpdate"
    v-model="value"
    multiple
    :placeholder="field.placeholder"
  >
    <ElOption
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    ></ElOption>
  </ElSelect>

  <!-- switch -->
  <ElSwitch
    @change="handleUpdate"
    v-model="value"
    v-if="fieldType == 'ElSwitch'"
    :active-text="field.checkedChildren"
  >
  </ElSwitch>
  <!-- checkbox only one -->
  <ElCheckbox
    @change="handleUpdate"
    v-model="value"
    v-if="fieldType == 'checkbox' && !field.multiple"
    :label="field.placeholder"
  >
  </ElCheckbox>

  <!-- Cascader -->
  <ElCascader
    @change="handleUpdateCascader"
    v-model="value"
    v-if="fieldType == 'cascader'"
    :options="options"
    filterable
    clearable
    :props="{
      ...field.props,
      checkStrictly: true,
    }"
    :placeholder="field.placeholder"
  >
  </ElCascader>

  <!-- cascader-multi  -->
  <ElCascader
    @change="handleUpdateCascader"
    v-model="value"
    v-if="fieldType == 'cascader-multi'"
    :options="options"
    filterable
    clearable
    :props="{
      ...field.props,
      checkStrictly: true,
      multiple: true,
    }"
    :placeholder="field.placeholder"
  >
  </ElCascader>

  <!-- radio-button-group  -->
  <ElRadioGroup
    @change="handleUpdate"
    v-model="value"
    v-if="fieldType == 'ElRadioButtonGroup'"
  >
    <ElRadioButton
      v-for="item in options"
      :key="item.value"
      :label="item.value"
      >{{ item.label }}</ElRadioButton
    >
  </ElRadioGroup>

  <!-- input  -->
  <ElInput
    v-model="value"
    :disabled="field.disabled"
    @change="handleUpdate"
    :type="field.epInputType || 'text'"
    v-if="!fieldType || fieldType == 'ElInput'"
    :placeholder="field.component?.placeholder"
  >
    <!-- suffix  -->
    <template v-if="field.suffix" #suffix>
      <div>
        {{ field.suffix }}
      </div>
    </template>
    <!-- prefix -->
    <template v-if="field.prefix" #prefix>
      <div>
        {{ field.prefix }}
      </div>
    </template>
  </ElInput>
</template>
<script>
import { request } from "@/api/request";
import { ElCheckbox } from "element-plus";
export default {
  emits: ["update:modelValue", "change"],
  components: { ElCheckbox },
  props: {
    field: {
      type: Object,
      default: () => ({}),
    },
    modelValue: {
      type: [String, Number, Array, Object],
      default: () => "",
    },
  },
  data() {
    return {
      options: [],
      value: "",
    };
  },
  watch: {
    modelValue: {
      handler(val) {
        if (this.fieldType == "ElSwitch") return (this.value = !!val);
        this.value = val;
      },
      immediate: true,
    },
    value: {
      handler(val) {
        // this.$emit('update:modelValue', val)
      },
    },
    field: {
      handler() {
        // this.getOptions();
      },
      deep: true,
    },
  },
  computed: {
    fieldType() {
      return this.field?.component?.componentName;
    },
  },
  methods: {
    handleUpdate(val) {
      this.$emit("update:modelValue", val);
      this.$emit("change", val);
    },
    /**
     * 单选级联选择器，取值最后一项
     */
    handleUpdateCascader(val) {
      let value = null;
      if (val && val.length > 0) {
        value = val[val.length - 1];
      }
      this.$emit("update:modelValue", value);
      this.$emit("change", value);
    },
    progressOptions(options, level = 1) {
      if (level <= 0) return [];
      return options.map((v) => {
        let rollback_label_names = ["name", "title", "label", "username"];
        let rollback_value_names = ["id", "value"];

        let label_name = this.field.props?.label || "name";
        let value_name = this.field.props?.value || "id";

        let label = "";
        let value = "";

        if (v[label_name]) {
          label = v[label_name];
        } else {
          for (let i = 0; i < rollback_label_names.length; i++) {
            if (
              v[rollback_label_names[i]] !== undefined &&
              v[rollback_label_names[i]] !== null
            ) {
              label = v[rollback_label_names[i]];
              break;
            }
          }
        }

        if (v[value_name]) {
          value = v[value_name];
        } else {
          for (let i = 0; i < rollback_value_names.length; i++) {
            if (
              v[rollback_value_names[i]] !== undefined &&
              v[rollback_value_names[i]] !== null
            ) {
              value = v[rollback_value_names[i]];
              break;
            }
          }
        }

        return {
          label,
          value,
          children: this.progressOptions(v.children, level - 1),
        };
      });
    },
    getOptions() {
      let level = this.field.cascaderLevel || 2;
      // if (this.fieldType == "select") level = 1;
      // if (this.fieldType == "select-multi") level = 1;
      // // radio-button-group
      // if (this.fieldType == "radio-button-group") level = 1;
      if (
        [
          "select",
          "select-multi",
          "radio-button-group",
          "ElRadioButtonGroup",
        ].includes(this.fieldType)
      ) {
        level = 1;
      }
      if (this.field.remoteDataApi) {
        request
          .get(this.field.remoteDataApi, {
            params: {
              page: 1,
              limit: 1000,
            },
          })
          .then((res) => {
            let result = res.data?.data;
            let arr = [];
            if (result instanceof Array) {
              arr = result;
            } else {
              arr = result?.data || [];
            }
            this.options = this.progressOptions(arr, level);
          });
      } else {
        console.log("this.field.options", this.field);
        this.options = this.progressOptions(
          this.field.component?.options || [],
          level
        );
      }
    },
    refresh() {
      this.getOptions();
    },
  },
  created() {},
  mounted() {
    this.getOptions();
  },
};
</script>
<style lang="scss" scoped></style>
