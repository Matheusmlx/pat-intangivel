import axios from 'axios'
import { AzSearchUrlBuilder } from '@azinformatica/loki'

class FornecedorApiClient {
    async buscarPorCNPJ(conteudo) {
        const paginacao = {
            page: 1,
            rowsPerPage: 100
        }
        const filtros = {
            conteudo: {
                value: conteudo
            }
        }
        const url = AzSearchUrlBuilder.build('api/fornecedores', filtros, paginacao)
        return axios.get(url)
    }

    async buscarPorId(id) {
        return axios.get(`api/fornecedores/${id}`)
    }
}

export default new FornecedorApiClient()
