<template>
    <div class="fluxo white">
        <div :class="ehMovimentacaoFinalizada || ehMovimentacaoRejeitada ? 'etapa-concluida' : 'etapa'">
            <v-tooltip top>
                <template v-slot:activator="{ on, attrs }">
                    <v-icon v-bind="attrs" v-on="on" color="#487b9c">check_circle</v-icon>
                    <div class="text-body-2 grey--text text--darken-1 font-weight-bold">Envio</div>
                </template>
                <span>{{movimentacao.dataDeEnvio | azDate()}}</span>
            </v-tooltip>
        </div>
        <div class="etapa-final" v-if="!ehMovimentacaoRejeitada && ehMovimentacaoFinalizada">
            <v-tooltip top>
                <template v-slot:activator="{ on, attrs }">
                    <v-icon v-bind="attrs" v-on="on" color="#487b9c">check_circle</v-icon>
                    <div class="text-body-2 grey--text text--darken-1 font-weight-bold">Recebimento</div>
                </template>
                <span>{{movimentacao.dataDeFinalizacao | azDate()}}</span>
            </v-tooltip>
        </div>
        <div class="etapa-final" v-else-if="ehMovimentacaoRejeitada">
            <v-tooltip top>
                <template v-slot:activator="{ on, attrs }">
                    <v-icon v-bind="attrs" v-on="on" color="#d40d0d">fa-times-circle</v-icon>
                    <div class="text-body-2 grey--text text--darken-1 font-weight-bold mx-5">Rejeição</div>
                </template>
                <span>{{movimentacao.dataDeFinalizacao | azDate()}}</span>
            </v-tooltip>
        </div>
        <div class="etapa-final" v-else>
            <v-icon color="#ccc">check_circle</v-icon>
            <div class="text-body-2 grey--text text--darken-1 font-weight-bold">Recebimento</div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'MovimentacaoDetalheLinhaDoTempo',
        props: ['movimentacao'],
        computed: {
            ehMovimentacaoRejeitada() {
                return this.movimentacao.situacao === 'REJEITADO'
            },
            ehMovimentacaoFinalizada() {
                return this.movimentacao.situacao === 'FINALIZADO'
            }
        }
    }
</script>

<style scoped lang="stylus">
    .fluxo
        display flex
        justify-content center

        .etapa-final
            margin-top 10px
            margin-bottom 10px
            display block
            text-align center
            position relative
            z-index 2

        .etapa
            margin-top 10px
            margin-bottom 10px
            display block
            text-align center
            position relative
            z-index 2
            margin-right 200px

            &::after
                content ""
                width 118px
                background-color #487b9c
                height 4px
                position absolute
                top 10px
                left 29px
                z-index 1

            &::before
                content ""
                width 236px
                background-color #ccc
                height 4px
                position absolute
                top 10px
                left 42px
                z-index 1

        .etapa-concluida
            margin-top 10px
            margin-bottom 10px
            display block
            text-align center
            position relative
            z-index 2
            margin-right 200px

            &::after
                content ""
                width 250px
                background-color #487b9c
                height 4px
                position absolute
                top 10px
                left 29px
                z-index 1
</style>
