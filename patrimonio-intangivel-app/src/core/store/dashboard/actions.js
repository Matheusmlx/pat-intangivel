import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'


export default {
    async [actionTypes.DASHBOARD.BUSCAR_LICENCAS_PROXIMAS_A_VENCER]({ commit } , quantidade) {
        const {data} = await api.dashboard.buscarLicencasProximasAVencer(quantidade)
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.DASHBOARD.SET_LICENCAS, data.itens)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data.itens
    },
    async [actionTypes.DASHBOARD.BUSCAR_TOTALIZADORES]() {
        const {data} = await api.dashboard.buscarTotalizadores()
        return data
    },

    async [actionTypes.DASHBOARD.BUSCAR_INTANGIVEIS_POR_ORGAO]() {
        const {data} = await api.dashboard.buscarIntangiveisPorOrgao()
        return data.itens
    },

    async [actionTypes.DASHBOARD.BUSCAR_INTANGIEIS_POR_TIPO]() {
        const {data} = await api.dashboard.buscarIntangiveisPorTipo()
        return data.itens
    }
}
