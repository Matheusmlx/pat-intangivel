import axios from 'axios'

class CamposPersonalizadosApiClient {

    async getAll() {
        return axios.get('api/rotulosPersonalizados')
    }
}

export default new CamposPersonalizadosApiClient()
