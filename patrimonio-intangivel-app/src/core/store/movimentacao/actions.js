import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {
    async [actionTypes.MOVIMENTACAO.ATUALIZAR_MOVIMENTACAO]({commit}, dados) {

        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const {data} = await api.movimentacao.atualizar(dados)
        commit(mutationTypes.MOVIMENTACAO.SET_MOVIMENTACAO, data)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_PATRIMONIO](context, dados) {
        const {data} = await api.movimentacao.buscarMovimentacaoPorPatrimonio(dados)
        return data
    },

    async [actionTypes.MOVIMENTACAO.CADASTRAR_MOVIMENTACAO]({commit}, dados) {
        const {data} = await api.movimentacao.cadastrar(dados)
        commit(mutationTypes.MOVIMENTACAO.SET_MOVIMENTACAO, data)
        return data
    },

    async [actionTypes.MOVIMENTACAO.ENVIAR_MOVIMENTACAO](context, movimentacao) {
        const {data} = await api.movimentacao.enviarMovimentacao(movimentacao)
        return data
    },

    async [actionTypes.MOVIMENTACAO.BUSCAR_ORGAOS_DESTINO]() {
        const {data} = await api.orgao.buscarTodos()
        return data
    },

    async [actionTypes.MOVIMENTACAO.DELETAR_MOVIMENTACAO](context, id) {
        await api.movimentacao.deletar(id)
    },

    async [actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_ID](context, id) {
        const {data} = await api.movimentacao.buscarPorId(id)
        return data
    },

    async [actionTypes.MOVIMENTACAO.BUSCAR_PATRIMONIO_POR_MOVIMENTACAO](context, id) {
        const {data} = await api.movimentacao.buscarPatrimonioPorMovimentacao(id)
        return data
    },

    async [actionTypes.MOVIMENTACAO.BUSCAR_TODAS_MOVIMENTACOES]({state}, filtros) {
        const {paginacao} = state.todasMovimentacoes
        const {data} = await api.movimentacao.buscarTodos(filtros, paginacao)
        return data
    },

    async [actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACOES_PENDENTES]({commit, state}, filtros) {
        const {paginacao} = state.todasMovimentacoesPendentes
        const {data} = await api.movimentacao.buscarPendentes(filtros, paginacao)
        commit(mutationTypes.MOVIMENTACAO.SET_MOVIMENTACAO_PENDENTES, data)
        return data
    },

    async [actionTypes.MOVIMENTACAO.RECEBER_MOVIMENTACAO](context, idMovimentacao) {
        const {data} = await api.movimentacao.receberMovimentacao(idMovimentacao)
        return data
    },

    async [actionTypes.MOVIMENTACAO.REJEITAR_MOVIMENTACAO](context, id) {
        await api.movimentacao.rejeitarMovimentacao(id)
    },

    async [actionTypes.MOVIMENTACAO.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO](context, documento) {
        const {data} = await api.movimentacao.cadastrarDocumento(documento)
        return data
    },

    async [actionTypes.MOVIMENTACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO_MOVIMENTACAO]({commit}, documento) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const {data} = await api.movimentacao.atualizarDocumento(documento)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.MOVIMENTACAO.DOCUMENTO.DOCUMENTO_UPLOAD_MOVIMENTACAO](context, documento) {
        let formData = new FormData()
        formData.append('file', documento.uriAnexo)
        const {data} = await api.movimentacao.uploadAnexoDocumento(formData)
        return data.uri
    },

    async [actionTypes.MOVIMENTACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO_MOVIMENTACAO]() {
        const {data} = await api.movimentacao.buscarTipoDocumento()
        return data.items
    },

    async [actionTypes.MOVIMENTACAO.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO]({commit}, id) {
        const {data} = await api.movimentacao.buscarDocumento(id)
        commit(mutationTypes.MOVIMENTACAO.DOCUMENTO.SET_DOCUMENTOS_MOVIMENTACOES, data.items)
        return data.items
    },

    async [actionTypes.MOVIMENTACAO.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO]({state}, documento) {
        state.documentoBackup = documento
        await api.movimentacao.deletarDocumento(documento)
        const posicao = state.documentos.indexOf(documento, state.documentos)
        state.documentos.splice(posicao, 1)
    }
}
