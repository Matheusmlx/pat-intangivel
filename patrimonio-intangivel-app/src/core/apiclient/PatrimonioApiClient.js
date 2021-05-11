import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'

class PatrimonioApiClient {

    async cadastrarDocumento(documento) {
        return axios.post(`api/patrimonios/${documento.idPatrimonio}/documentos`, documento)
    }

    async atualizarDocumento(documento) {
        return axios.put(`api/patrimonios/${documento.idPatrimonio}/documentos/${documento.id}`, documento)
    }

    async buscarDocumento(patrimonioId) {
        return axios.get(`api/patrimonios/${patrimonioId}/documentos`)
    }

    async uploadAnexoDocumento(formData){
        return axios.post('api/v1/arquivos',formData,{headers:{'Content-Type':'multipart/form-data'}})
    }

    async downloadAnexo(anexo){
        return axios.get('api/v1/arquivos',{params:{'uri':anexo}})
    }

    async buscarTipoDocumento() {
        return axios.get('api/tiposdocumentos?page=1&size=12')
    }

    async deletarDocumento(documento){
        return axios.delete(`api/patrimonios/${documento.idPatrimonio}/documentos/${documento.id}`)
    }

    async buscarTodos(filtros, paginacao) {
        paginacao.descending = !paginacao.sortDesc[0]
        const url = AzSearchUrlBuilder.build(
            'api/patrimonios',
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async buscarPorId(id) {
        return axios.get(`api/patrimonios/${id}`)
    }

    async visualizarPorId(id) {
        return axios.get(`api/patrimonios/${id}/visualizacao-completa`)
    }

    async atualizar(dados) {
        if (dados.orgao != null && typeof dados.orgao.id != 'undefined') {
            dados.orgao = dados.orgao.id
        }
        if (dados.setor != null && typeof dados.setor.id != 'undefined') {
            dados.setor = dados.setor.id
        }
        return axios.put(`api/patrimonios/${dados.id}`, dados)
    }

    async cadastrar(dados) {
        return axios.post('api/patrimonios', dados)
    }

    async deletar(id) {
        return axios.delete(`api/patrimonios/${id}`)
    }

    async deletarNaoAlterado(id) {
        return axios.delete(`api/patrimonios/${id}/nao-alterado`)
    }

    async reverterAtivacao(id) {
        return axios.patch(`api/patrimonios/${id}/desativar`)
    }

    async ativar(id) {
        return axios.patch(`api/patrimonios/${id}`)
    }

    async buscarAmortizacoes(id) {
        return axios.get(`api/amortizacoes/${id}`)
    }
}

export default new PatrimonioApiClient()
