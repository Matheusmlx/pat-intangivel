<template>
    <v-card flat min-height="305" class="mr-1">
        <v-card-title class="text--darken-2 font-weight-bold grey--text text-sm-body-2 ml-3 mr-3 pa-2">
            Intangíveis por Tipo
            <v-spacer/>
            <v-btn class="call-to-action text-body-2 text--darken-2 grey--text" @click="verTodos" outlined text>
                Ver Todos
            </v-btn>
        </v-card-title>
        <v-divider/>
        <v-card-text>
            <grafico-em-rosca v-if="exibirGrafico" :dados="patrimoniosPorTipo" class="grafico-em-rosca"/>
        </v-card-text>
    </v-card>
</template>

<script>
    import GraficoEmRosca from './GraficoEmRosca'

    export default {
        name: 'IntangivelPorTipo',
        components: {GraficoEmRosca},
        props: {
            patrimoniosPorTipo: {
                required: true,
                type: Array,
                default: () => {
                    return []
                }
            }
        },
        computed: {
            exibirGrafico() {
                let exibirGrafico = this.patrimoniosPorTipo.filter(patrimonio => patrimonio.quantidade !== 0)
                return exibirGrafico.length > 0
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
    .grafico-em-rosca
        height 220px

    .canvas-con
        display flex
        align-items center
        justify-content center
        min-height 365px
        position relative

    .canvas-con-inner
        height 100%

    .canvas-con-inner, .legend-con
        display inline-block
</style>
