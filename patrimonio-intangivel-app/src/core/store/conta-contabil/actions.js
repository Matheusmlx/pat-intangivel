import api from '@/core/apiclient'
import { actionTypes, mutationTypes } from '@/core/constants'

export default {
    async [actionTypes.CONTA_CONTABIL.BUSCAR_CONTA_CONTABIL_POR_ID]({ commit }, id) {
        const { data } = await api.contaContabil.buscarPorId(id)
        commit(mutationTypes.CONTA_CONTABIL.SET_CONTA_CONTABIL, data)
        return data
    },

    async [actionTypes.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS]({ state }) {
        const { filtros, paginacao } = state.todasContasContabeis
        const { data } = await api.contaContabil.buscarTodos(filtros, paginacao)
        return data
    },

    async [actionTypes.CONTA_CONTABIL.SALVAR_CONTA_CONTABIL]({ commit }, contaContabil) {
        const { data } = await api.contaContabil.salvar(contaContabil)
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.CONTA_CONTABIL.SET_CONTA_CONTABIL, data)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    },

    async [actionTypes.CONTA_CONTABIL.EDITAR_CONTA_CONTABIL]({ commit }, contaContabil) {
        const { data } = await api.contaContabil.editar(contaContabil)
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.CONTA_CONTABIL.SET_CONTA_CONTABIL, data)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    }
}
