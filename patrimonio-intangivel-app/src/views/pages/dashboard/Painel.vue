<template>
    <v-container fluid>
        <v-row>
            <v-col cols="12">
                <totalizadores :totalizadores="totalizadores"
                               @filtrarPatrimonioPorTotalizadores="filtrarPatrimonioPorTotalizadores"/>
            </v-col>
            <v-col class="ml-4 estiloProximasLicencas" cols="4">
                <vencimentos-licencas @acessarLicenca="acessarLicenca" :licencas="licencas"/>
            </v-col>
            <v-col cols="6" v-if="dados.length > 1">
                <intangiveis-por-orgao
                    :dados="dados"
                    @verTodos="verTodos"
                    @buscarDadosParaOGrafico="buscarDadosParaOGrafico"/>
            </v-col>
            <v-col cols="4" v-if="patrimoniosPorTipo !== 0" class="pt-1 ml-4"
                   :class="{'ml-n4': dados.length <= 1}">
                <IntangiveisPorTipo
                    :class="{estiloIntangiveisPorTipo :dados.length <= 1}"
                    :patrimoniosPorTipo="patrimoniosPorTipo"
                    @verTodos="verTodosPorTipo"/>
            </v-col>
        </v-row>
    </v-container>

</template>

<script>
    import Totalizadores from './totalizadores/Totalizadores'
    import IntangiveisPorOrgao from './intangiveisPorOrgao/IntangiveisPorOrgao'
    import VencimentosLicencas from './vencimentolicencas/ProximasLicencasAVencer'
    import IntangiveisPorTipo from './instangiveisPorTipo/IntangiveisPorTipo'
    import {mapState} from 'vuex'
    import {actionTypes} from '@/core/constants'

    export default {
        name: 'Painel',
        components: {Totalizadores, IntangiveisPorOrgao, VencimentosLicencas, IntangiveisPorTipo},
        data() {
            return {
                dados: [],
                licencas: [],
                totalizadores: {},
                patrimoniosPorTipo: []
            }
        },
        computed: {
            ...mapState({filtros: state => state.patrimonio.todosPatrimonios.filtros})
        },
        async mounted() {
            await this.buscarTotalizadores()
            await this.buscarLicencasProximasAVencer()
            await this.buscarDadosParaOGrafico()
            await this.buscarPatrimoniosPorTipo()
        },
        methods: {
            acessarLicenca(id) {
                this.$router.push({
                    name: 'FichaPatrimonio',
                    params: {id}
                })
            },
            async buscarDadosParaOGrafico() {
                this.dados = await this.$store.dispatch(actionTypes.DASHBOARD.BUSCAR_INTANGIVEIS_POR_ORGAO)
            },
            async buscarPatrimoniosPorTipo() {
                this.patrimoniosPorTipo = await this.$store.dispatch(actionTypes.DASHBOARD.BUSCAR_INTANGIEIS_POR_TIPO)
            },
            async buscarLicencasProximasAVencer() {
                this.licencas = await this.$store.dispatch(actionTypes.DASHBOARD.BUSCAR_LICENCAS_PROXIMAS_A_VENCER)
            },
            async buscarTotalizadores() {
                this.totalizadores = await this.$store.dispatch(actionTypes.DASHBOARD.BUSCAR_TOTALIZADORES)
            },
            filtrarPatrimonioPorTotalizadores(filtro) {
                if (filtro !== undefined) {
                    this.filtros.conteudo.value = filtro
                    this.$router.push({name: 'TodosPatrimonios'})
                } else {
                    this.limparFiltro()
                    this.$router.push({name: 'TodosPatrimonios'})
                }
            },
            limparFiltro() {
                this.filtros.conteudo.value = null
            },
            verTodos() {
                this.limparFiltro()
                this.ordenarListaPorOrgao()
                this.$router.push({name: 'TodosPatrimonios'})
            },
            verTodosPorTipo() {
                this.limparFiltro()
                this.ordenarListaPorTipo()
                this.$router.push({name: 'TodosPatrimonios'})
            },
            ordenarListaPorOrgao() {
                let paginacao = this.$store.state.patrimonio.todosPatrimonios.paginacao
                paginacao.sortBy[0] = 'orgao'
                paginacao.descending = false
                paginacao.sortDesc[0] = true
            },
            ordenarListaPorTipo() {
                let paginacao = this.$store.state.patrimonio.todosPatrimonios.paginacao
                paginacao.sortBy[0] = 'tipo'
                paginacao.descending = false
                paginacao.sortDesc[0] = true
            }
        }
    }
</script>

<style lang="stylus">
    .estiloProximasLicencas
        margin-top -30px

    .estiloIntangiveisPorTipo
        top: -22px
</style>
