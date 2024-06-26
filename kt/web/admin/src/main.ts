
import { createApp } from 'vue'
import { createPinia } from 'pinia'

import './assets/scss/main.scss'
// unocss 
import 'virtual:uno.css'

// element-plus
import ElementPlus from 'element-plus'
import './assets/scss/element-variables.scss'
// dark theme 
import 'element-plus/theme-chalk/dark/css-vars.css'

import ElzhCn from 'element-plus/dist/locale/zh-cn.mjs'
// end element-plus

// iconify
import { Icon } from '@iconify/vue';
// end iconify

// @ts-ignore 
import App from './App.vue'
import router from './router/index'

// request
import { request } from '@/api/request'

// translate
import useTranslateStore from './pinia/translate'
import zhCN from './i18n/zh-cn/main'
// end translate

import config from './config'
import dayjs from 'dayjs'
import numeral from 'numeral'

import NProgress from 'nprogress'
NProgress.configure({ showSpinner: true })

const app = createApp(App)
app.use(ElementPlus, { locale: ElzhCn })
app.use(createPinia())
app.mixin({
    methods: {
        trans,
        $t: trans
    }
})
app.use(function (vm) {

    vm.config.globalProperties.$dayjs = dayjs
    vm.config.globalProperties.$numeral = numeral

    vm.config.globalProperties.$img = (url) => {
        if (/[http|data|blob]/.test(url)) {
            return url
        }
        console.log(url)
        const target = config.url.IMG_URL + url
        console.log(target)
        return target
    }


    router.beforeEach((to, from, next) => {
        next()
    })

    const translateStore = useTranslateStore()
    vm.component("Icon", Icon)
    vm.config.globalProperties.$t = translateStore.getKey
    setTimeout(() => {
        const serverData = {
            "zh-cn": {
                "AdminTitle": "Element Vite Admin ｜ EVA",
                "首页": "控制台",
                "welcome.documentation": "欢迎 {user} 使用文档",
            },
        }
        translateStore.setMessages("zh-cn", {
            ...zhCN,
            ...(serverData["zh-cn"] || {})
        })
    }, 200);

    translateStore.setLocale("zh-cn")

    // setTimeout(() => {
    //     translateStore.setLocale("en")
    // }, 3000);
})


const getComponents = () => {
    const components = import.meta.glob('./views/**/*.vue')
    return components
}

const components = getComponents()
console.log("components", components)

const registerRoute = (menu,parent="") => {
    console.log("menu", menu)
    if (menu.component) {
        // register router
        const component = components[`./views/${menu.component}.vue`]
        const route = {
            path: menu.path,
            name: menu.name,
            component: component,
            meta: {
                title: menu.title,
                key: menu.name,
                api: menu.api || "",
                ...(menu || {})
            }
        }
        if (!menu.parent) {
            menu.parent =  parent || 'layout' //default layout
        }
        if (menu.parent) {
            const parentRoute = router.getRoutes().find(el => el.name === menu.parent)
            if (parentRoute) {
                if (!parentRoute.children) {
                    parentRoute.children = []
                }
                parentRoute.children.push(route)
            }
            router && router.addRoute(parentRoute.name, route)
        }
    }
    if (menu.children) {
        console.log("menu.children for",menu.name, menu.children)
        for (let i = 0; i < menu.children.length; i++) {
            registerRoute(menu.children[i],menu.name)
        }
    }
}

request.get('/menu/list', {
    noMsgAlert: true
}).then(async res => {
    let menus = res.data.data || []
    for (let i = 0; i < menus.length; i++) {
        let el = menus[i]
        registerRoute(el)
    }

}).finally(() => {
    console.log("router", router.getRoutes())
    app.use(router)
    app.mount('#app')
})