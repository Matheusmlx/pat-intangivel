import store from '@/core/store'
import {actionTypes} from '@/core/constants'

class AtualizarDominioPerfilUsuarioListener {
    async execute() {
        return await store.dispatch(actionTypes.COMUM.ATUALIZAR_SESSAO_DOMINIO)
    }
}

export default new AtualizarDominioPerfilUsuarioListener()
