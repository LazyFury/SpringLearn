import useTranslateStore from "@/pinia/translate.js"


export const trans = (key: string, ...args: any[]) => {
    return useTranslateStore().getKey(key, ...args)
}
export const $t = trans