import axios from 'axios'

class DashboardApiClient {

    async buscarTotalizadores(){
        return axios.get('api/dashboard/totalizadores')
    }
    async buscarLicencasProximasAVencer(quantidade=5){
        return axios.get(`api/dashboard/licenca/proximas-vencer?quantidade=${quantidade}`)
    }
    async buscarIntangiveisPorOrgao(){
        return axios.get('api/dashboard/metricas-por-orgao')
    }
    async buscarIntangiveisPorTipo(){
        return axios.get('api/dashboard/totalizadores-por-tipo')
    }
}

export default new DashboardApiClient()
