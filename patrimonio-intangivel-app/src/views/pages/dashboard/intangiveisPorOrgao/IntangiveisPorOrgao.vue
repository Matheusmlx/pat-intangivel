<template>
    <v-flex class="md8 lg8 grafico-em-barra pl-2" v-if="exibirGrafico">
        <v-card class="mr-4" flat>
            <v-card-title class="text--darken-2 font-weight-bold grey--text text-sm-body-2 ml-3 mr-3 pa-2">
                Intangíveis por órgão
                <az-info-button>
                    Órgãos que não aparecem nessa listagem não tem patrimônios.
                </az-info-button>
                <v-spacer/>
                <v-btn class="call-to-action text-body-2 text--darken-2 grey--text" outlined text @click="verTodos">
                    Ver Todos
                </v-btn>
            </v-card-title>
            <v-divider/>
            <v-card-text>
                <grafico-em-barra :dados="dados" style="max-height: 220px"/>
            </v-card-text>
        </v-card>
    </v-flex>
</template>

<script>
    import GraficoEmBarra from './GraficoEmBarra'

    export default {
        name: 'IntangiveisPorOrgao',
        components: {GraficoEmBarra},
        props: {
            dados: {
                required: true,
                type: Array,
                default: () => {
                    return []
                }
            }
        },
        computed: {
            exibirGrafico() {
                return this.dados.length > 1
            }
        },
        methods: {
            verTodos() {
                this.$emit('verTodos')
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .grafico-em-barra
        margin-top -30px
        position absolute
        right 0
        width 100%
</style>
