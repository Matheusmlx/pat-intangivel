import axios from 'axios'
import { AzSearchUrlBuilder } from '@azinformatica/loki'

class SetorApiClient {

    async buscarTodos(orgaoId) {
        const url = AzSearchUrlBuilder.build(`api/unidades-organizacionais/${orgaoId}`)
        return axios.get(url)
    }
}

export default new SetorApiClient()
