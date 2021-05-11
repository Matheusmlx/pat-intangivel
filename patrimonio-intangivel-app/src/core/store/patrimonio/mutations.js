import {mutationTypes} from '@/core/constants'

export default {
    [mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS](state, filtros) {
        state.todosPatrimonios.filtros = filtros
    },
    [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS](state, paginacao) {
        state.todosPatrimonios.paginacao = paginacao
    },
    [mutationTypes.PATRIMONIO.SET_PATRIMONIO](state, tipoPatrimonio) {
        state.patrimonio = tipoPatrimonio
        return state.patrimonio
    },

    [mutationTypes.PATRIMONIO.REMOVER_DOCUMENTOS](state, documento) {
        let posicao = state.documentos.indexOf(documento, state.documentos[0])
        state.documentos.splice(posicao, 1)
    },

    [mutationTypes.PATRIMONIO.SET_VISUALIZACAO_LISTAGEM](state, data) {
        state.isLista = data
    },

    [mutationTypes.PATRIMONIO.RESETA_PAGE](state) {
        state.todosPatrimonios.paginacao.page = 1
    },

    [mutationTypes.PATRIMONIO.SET_DOCUMENTOS](state, documentos) {
        state.documentos = documentos
    },
    [mutationTypes.PATRIMONIO.DOCUMENTO_DESFAZER_SETLAYOUT](state, { message, type , hasButtom, style ,  styleButtom , iconColor, mensageButtom,timeOut,styleButtomClose}){
        state.alert = { message, type , hasButtom, style , styleButtom, iconColor , mensageButtom,timeOut,styleButtomClose}
    },
    [mutationTypes.PATRIMONIO.DOCUMENTO_DESFAZER](){
        state.desfazerDocumento
    }
}
