import Router from 'vue-router'
import goTo from 'vuetify/es5/services/goto'
import comum from './routes/comum'
import patrimonio from './routes/patrimonio'
import contaContabil from './routes/conta-contabil'
import relatorios from './routes/relatorios'
import movimentacaoExterna from './routes/movimentacao-externa'
import painel from './routes/painel'

const index = new Router({
    routes: [
        {
            path: '/',
            name: 'Inicial',
            redirect: () => {
                return { name: 'Painel' }
            },
        },
        ...painel,
        ...patrimonio,
        ...movimentacaoExterna,
        ...contaContabil,
        ...relatorios,
        ...comum,
    ],
    scrollBehavior: (to, from, savedPosition) => {
        let scrollTo = 0

        if (to.hash) {
            scrollTo = to.hash
        } else if (savedPosition) {
            scrollTo = savedPosition.y
        }

        return goTo(scrollTo)
    },
})

export default index
