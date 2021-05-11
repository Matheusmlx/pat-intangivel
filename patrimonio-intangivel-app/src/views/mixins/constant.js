import {
    estadoPatrimonio,
    exceptions,
    formatosDefault,
    fusoHorario,
    mensagens,
    metodoCalculoContaContabil,
    notificacoesDefault,
    produto,
    reconhecimento,
    situacaoPatrimonio,
    tipoContaContabil,
    tipoPatrimonio,
    tipoMovimentacao,
    situacaoMovimentacao
} from '@/core/constants'

export default {
    data() {
        return {
            estadoPatrimonio,
            exceptions,
            formatosDefault,
            fusoHorario,
            mensagens,
            metodoCalculoContaContabil,
            notificacoesDefault,
            produto,
            reconhecimento,
            situacaoPatrimonio,
            tipoContaContabil,
            tipoPatrimonio,
            tipoMovimentacao,
            situacaoMovimentacao
        }
    },
    methods: {
        formatarLista(constant) {
            const list = Object.getOwnPropertyNames(constant).sort()
            if (list.indexOf('__ob__') >= 0) {
                list.splice(list.indexOf('__ob__'), 1)
            }
            return list
        }
    }
}
