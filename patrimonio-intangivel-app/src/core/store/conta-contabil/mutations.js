import { mutationTypes } from '@/core/constants'

export default {
    [mutationTypes.CONTA_CONTABIL.SET_FILTROS_BUSCA_TODAS_CONTAS_CONTABEIS](state, filtros) {
        state.todasContasContabeis.filtros = filtros
    },

    [mutationTypes.CONTA_CONTABIL.SET_PAGINACAO_BUSCA_TODAS_CONTAS_CONTABEIS](state, paginacao) {
        state.todasContasContabeis.paginacao = paginacao
    },

    [mutationTypes.CONTA_CONTABIL.SET_CONTA_CONTABIL](state, contaContabil) {
        state.contaContabil = contaContabil
    },
}
