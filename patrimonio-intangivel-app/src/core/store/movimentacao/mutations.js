import {mutationTypes} from '@/core/constants'

export default {

    [mutationTypes.MOVIMENTACAO.SET_MOVIMENTACAO](state, movimentacao) {
        state.movimentacao = movimentacao
        return state.movimentacao
    },

    [mutationTypes.MOVIMENTACAO.SET_MOVIMENTACAO_PENDENTES](state,movimentacao){
        state.movimentacaoPendente = movimentacao
    },

    [mutationTypes.MOVIMENTACAO.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES](state, filtros) {
        state.todasMovimentacoes.filtros = filtros
    },

    [mutationTypes.MOVIMENTACAO.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES](state, paginacao) {
        state.todasMovimentacoes.paginacao = paginacao
    },

    [mutationTypes.MOVIMENTACAO.RESETA_PAGE](state) {
        state.todasMovimentacoes.paginacao.page = 1
    },

    [mutationTypes.MOVIMENTACAO.RESETA_EXTRA](state) {
        state.todasMovimentacoes.filtros.conteudoExtra.value = ''
    },

    [mutationTypes.MOVIMENTACAO.DOCUMENTO.SET_DOCUMENTOS_MOVIMENTACOES](state, documentos) {
        state.documentos = documentos
    },

    [mutationTypes.MOVIMENTACAO.DOCUMENTO.REMOVER_DOCUMENTOS_MOVIMENTACAOES](state, documento) {
        let posicao = this.state.documentos.indexOf(documento, state.documentos[0])
        state.documentos.splice(posicao, 1)
    }




}
