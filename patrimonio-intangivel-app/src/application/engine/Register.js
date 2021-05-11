import Vue from 'vue'
import '@babel/polyfill'
import '@/plugins'
import mixins from '@/views/mixins'
import filters from '@/views/filters'
import VueGtag from 'vue-gtag'

class Register {

    registerAll() {
        Vue.use(mixins)
        Vue.use(filters)
        Vue.use(VueGtag, {
            config: { id: 'UA-175310039-1' }
        })
    }

}

export default new Register()
