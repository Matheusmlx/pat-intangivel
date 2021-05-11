import Vuex from 'vuex'
import {actions, getters, mutations, state} from './comum'
import contaContabil from './conta-contabil'
import patrimonio from './patrimonio'
import amortizacao from './amortizacao'
import relatorio from './relatorio'
import CamposPersonalizados from './camposPersonalizados'
import movimentacao from './movimentacao'
import dashboard from './dashboard'

export default new Vuex.Store({
    state,
    getters,
    mutations,
    actions,
    modules: {
        contaContabil,
        patrimonio,
        amortizacao,
        relatorio,
        CamposPersonalizados,
        movimentacao,
        dashboard
    }
})
