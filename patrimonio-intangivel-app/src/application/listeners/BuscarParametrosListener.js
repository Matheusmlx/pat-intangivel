import store from '@/core/store'
import { actionTypes } from '@/core/constants'

class BuscarParametrosListener {
    async execute() {
        return store.dispatch(actionTypes.COMUM.BUSCAR_PARAMETROS)
    }
}

export default new BuscarParametrosListener()
