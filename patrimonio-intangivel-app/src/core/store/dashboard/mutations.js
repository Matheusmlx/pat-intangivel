import {mutationTypes} from '@/core/constants'

export default {
    [mutationTypes.DASHBOARD.SET_LICENCAS](state,licencas){
        state.licencas = licencas
    }
}
