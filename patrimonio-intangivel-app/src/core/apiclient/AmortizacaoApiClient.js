import axios from 'axios'
import { AzSearchUrlBuilder } from '@azinformatica/loki'

class AmortizacaoApiClient {

    async disparar(dados) {
        if (dados.orgao != null && typeof dados.orgao.id != 'undefined') {
            dados.orgao = dados.orgao.id
        }
        const url = AzSearchUrlBuilder.build('api/amortizacao-manual')
        return axios.post(url, dados)
    }

}

export default new AmortizacaoApiClient()
