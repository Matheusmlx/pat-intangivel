import api from '@/core/apiclient'
import { actionTypes} from '@/core/constants'

export default {
    async [actionTypes.AMORTIZACAO.GERAR_AMORTIZACAO_MANUAL](context, patrimonio) {
        const { data } =  await  api.amortizacao.disparar(patrimonio)
        return data
    }
}
