import { mensagens, mutationTypes, notificacoesDefault } from '@/core/constants'

export default {
    methods: {
        mostrarNotificacaoErro(message) {
            if (message === 'GENERAL') {
                message = mensagens.DISCONNECTED
            }
            this.$store.commit(mutationTypes.LOKI.SHOW_ALERT, {
                message,
                type: 'error'
            })
        },
        mostrarNotificacaoErroDefault() {
            this.$store.commit(mutationTypes.LOKI.SHOW_ALERT, {
                message: notificacoesDefault.ERRO_DEFAULT,
                type: 'error'
            })
        },
        mostrarNotificacaoSucesso(message) {
            this.$store.commit(mutationTypes.LOKI.SHOW_ALERT, {
                message,
                type: 'success'
            })
        },
        mostrarNotificacaoSucessoDefault() {
            this.$store.commit(mutationTypes.LOKI.SHOW_ALERT, {
                message: notificacoesDefault.SUCESSO_DEFAULT,
                type: 'success'
            })
        },
        mostrarNotificacaoAviso(message) {
            this.$store.commit(mutationTypes.LOKI.SHOW_ALERT, {
                message,
                type: 'warning'
            })
        },

    }
}
