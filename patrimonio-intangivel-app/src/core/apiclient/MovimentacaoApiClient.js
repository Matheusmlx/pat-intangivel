import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'

class MovimentacaoApiClient {

    async atualizar(dados) {
        if (dados.orgaoDestino != null && typeof dados.orgaoDestino.id != 'undefined') {
            dados.orgaoDestino = dados.orgaoDestino.id
        }
        return axios.put(`api/movimentacao/${dados.id}`, dados)
    }

    async buscarMovimentacaoPorPatrimonio(dados){
        return axios.get(`api/movimentacoes-patrimonio/?idPatrimonio=${dados.idPatrimonio}`)
    }

    async cadastrar(dados) {
        return axios.post('api/movimentacao', dados)
    }

    async enviarMovimentacao(movimentacao){
        return axios.put(`api/movimentacao/${movimentacao.id}/enviar`, movimentacao)
    }

    async deletar(id) {
        return axios.delete(`api/movimentacao/${id}`)
    }

    async buscarPorId(id){
        return axios.get(`api/movimentacao/${id}`)
    }

    async buscarPatrimonioPorMovimentacao(id){
        return axios.get(`api/movimentacao/patrimonio/${id}`)
    }

    async buscarTodos(filtros, paginacao) {
        paginacao.descending = !paginacao.sortDesc[0]
        const url = AzSearchUrlBuilder.build(
            'api/movimentacoes',
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async buscarPendentes(filtros, paginacao) {
        paginacao.descending = !paginacao.sortDesc[0]
        filtros.conteudoExtra.value = 'AGUARDANDO_RECEBIMENTO'
        const url = AzSearchUrlBuilder.build(
            'api/movimentacoes',
            filtros,
            paginacao
        )
        return axios.get(url)
    }
    async receberMovimentacao(id){
        return axios.put(`api/movimentacao/${id}/receber`)
    }

    async rejeitarMovimentacao(id){
        return axios.put(`api/movimentacao/${id}/rejeitar`)
    }

    async cadastrarDocumento(documento) {
        return axios.post(`api/movimentacao/${documento.idMovimentacao}/documentos`, documento)
    }

    async atualizarDocumento(documento) {
        return axios.put(`api/movimentacao/${documento.idMovimentacao}/documentos/${documento.id}`, documento)
    }

    async uploadAnexoDocumento(formData){
        return axios.post('api/v1/arquivos',formData,{headers:{'Content-Type':'multipart/form-data'}})
    }

    async buscarTipoDocumento() {
        return axios.get('api/tiposdocumentos?page=1&size=12')
    }

    async buscarDocumento(movimentacaoId) {
        return axios.get(`api/movimentacao/${movimentacaoId}/documentos`)
    }

    async deletarDocumento(documento){
        return axios.delete(`api/movimentacao/${documento.idMovimentacao}/documentos/${documento.id}`)
    }
}

export default new MovimentacaoApiClient()
