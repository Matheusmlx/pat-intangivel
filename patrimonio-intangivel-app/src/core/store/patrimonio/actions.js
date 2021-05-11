import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {
    async [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]({commit}, dados) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const {data} = await api.patrimonio.atualizar(dados)
        commit(mutationTypes.PATRIMONIO.SET_PATRIMONIO, data)

        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]({ commit }, id) {
        const { data } = await api.patrimonio.buscarPorId(id)
        commit(mutationTypes.PATRIMONIO.SET_PATRIMONIO, data)
        return data
    },

    async [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]({ state }) {
        const { filtros, paginacao } = state.todosPatrimonios
        const { data } = await api.patrimonio.buscarTodos(filtros, paginacao)
        return data
    },

    async [actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]({ commit }, patrimonio) {
        const { data } = await api.patrimonio.cadastrar(patrimonio)
        commit(mutationTypes.PATRIMONIO.SET_PATRIMONIO, data)
        return data
    },

    async [actionTypes.PATRIMONIO.DELETAR_PATRIMONIO](context, id) {
        await api.patrimonio.deletar(id)
    },

    async [actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO]({ commit }, id) {
        const { data } = await api.patrimonio.buscarDocumento(id)
        return data.items
    },


    async [actionTypes.PATRIMONIO.DELETAR_PATRIMONIO_NAO_ALTERADO](context, id) {
        await api.patrimonio.deletarNaoAlterado(id)
    },

    async [actionTypes.PATRIMONIO.REVERTER_ATIVACAO](context, id) {
        await api.patrimonio.reverterAtivacao(id)
    },

    async [actionTypes.PATRIMONIO.ATIVA_PATRIMONIO](context, id) {
        await api.patrimonio.ativar(id)
    },

    async [actionTypes.PATRIMONIO.VISUALIZAR_PATRIMONIO_POR_ID]({ commit }, id) {
        const { data } = await api.patrimonio.visualizarPorId(id)
        commit(mutationTypes.PATRIMONIO.SET_PATRIMONIO, data)
        return data
    },

    async [actionTypes.PATRIMONIO.DOCUMENTO.CADASTRAR_DOCUMENTO](context, documento) {
        const { data } = await api.patrimonio.cadastrarDocumento(documento)
        return data
    },

    async [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS]({ commit }, id) {
        const { data } = await api.patrimonio.buscarDocumento(id)
        commit(mutationTypes.PATRIMONIO.SET_DOCUMENTOS, data.items)
        return data.items
    },
    async [actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO]({ commit }, id) {
        const { data } = await api.patrimonio.buscarDocumento(id)
        return data.items
    },

    async [actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]() {
        const { data } = await api.patrimonio.buscarTipoDocumento()
        return data.items
    },

    async [actionTypes.PATRIMONIO.DOCUMENTO.ATUALIZAR_DOCUMENTO]({ commit }, documento) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const { data } = await api.patrimonio.atualizarDocumento(documento)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_UPLOAD](context, documento) {
        let formData = new FormData()
        formData.append('file', documento.uriAnexo)
        const { data } = await api.patrimonio.uploadAnexoDocumento(formData)
        return data.uri
    },

    async [actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_DOWNLOAD](context, anexo) {
        const { data } = await api.patrimonio.downloadAnexo(anexo)
        let blob = new Blob([data], { type: ['img/pdf', 'img/jpeg', 'img/png'] })
        const downloadLink = document.createElement('a')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = 'Relatorio_de_Protocolos.pdf'
        downloadLink.click()
    },

    async [actionTypes.PATRIMONIO.DOCUMENTO.DELETAR_DOCUMENTO]({ state }, documento) {
        state.documentoBackup = documento
        await api.patrimonio.deletarDocumento(documento)
        const posicao = state.documentos.indexOf(documento, state.documentos[0])
        state.documentos.splice(posicao, 1)
    },

    async [actionTypes.PATRIMONIO.BUSCAR_AMORTIZACOES](context, id) {
        const { data } = await api.patrimonio.buscarAmortizacoes(id)
        return data.items
    },

    async [actionTypes.PATRIMONIO.BAIXAR_RELATORIO_LISTAGEM_PATRIMONIO_XLS]({state},totalElementos) {
        const {filtros,paginacao} = state.todosPatrimonios
        const paginacaoRelatorio = {
            page: 1,
            rowsPerPage: totalElementos,
            sortDesc: paginacao.sortDesc,
            sortBy: paginacao.sortBy
        }
        const { data } = await api.relatorio.baixarListagemPatrimonioXLS(filtros,paginacaoRelatorio)
        let blob = new Blob([data], {
            type: 'application/x-msexcel'
        })
        const downloadLink = document.createElement('a')
        document.body.appendChild(downloadLink)
        downloadLink.setAttribute('type', 'hidden')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = `relatorio_patrimonios.xls`
        downloadLink.click()
    }
}
