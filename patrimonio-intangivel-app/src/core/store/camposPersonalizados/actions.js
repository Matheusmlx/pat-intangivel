import api from '@/core/apiclient'
import constants from '@/core/constants/action-types'

export default {
    async [constants.CAMPOS_PERSONALIZADOS.GET_ALL_CAMPOS_PERSONALIZADOS] ({commit}){
        const {data} = await api.camposPersonalizados.getAll()
        commit('SET_ROTULOS_PERSONALIZADOS', data.rotulosPersonalizados.i18n)
    }
}

