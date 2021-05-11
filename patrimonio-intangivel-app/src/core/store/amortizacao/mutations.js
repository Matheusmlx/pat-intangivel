import { mutationTypes } from '@/core/constants'
export default {
    [mutationTypes.AMORTIZACAO.SET_PATRIMONIOS_AMORTIZADOS](state, patrimonios) {
        state.patrimonios = patrimonios
    }
}
