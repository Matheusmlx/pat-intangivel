import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'

class OrgaoApiClient {

    async buscarTodos() {
        const url = AzSearchUrlBuilder.build('api/unidades-organizacionais?sort=sigla&direction=ASC')
        return axios.get(url)
    }

}

export default new OrgaoApiClient()
