<template>
    <div>
        <az-toolbar>
            <v-flex slot="simpleSearch">
                <az-search
                    :filter="filtrosInterno"
                    @remove-filter="tratarEventoRemoverFiltro"
                    @simple-search="tratarEventoBuscaSimples"
                    id="azSearch"
                    simple-search-placeholder="Busque por: Código, tipo, origem, destino ou situação">
                </az-search>
            </v-flex>
        </az-toolbar>
        <div class="az-form-content">
            <v-tabs v-model="tabs">
                <v-tab @click="redirecionaTodasMovimentacoes()" class="text-body-2">
                    Todas
                </v-tab>
                <v-tab @click="redirecionaMovimentacoesPendentes()">
                    <v-badge v-if="notificar"
                             color="red"
                             inline
                             :content="totalItens" class="text-body-2">
                        A Receber
                    </v-badge>
                    <div v-else>
                        A Receber
                    </div>
                </v-tab>
            </v-tabs>

            <v-tabs-items v-model="tabs">
                <v-tab-item v-for="rota in rotas" :key="rota.nome">
                    <router-view style="min-height: 66vh" @tratarEventoPaginar="tratarEventoPaginar"/>
                </v-tab-item>
            </v-tabs-items>
        </div>
    </div>
</template>

<script>
    import _ from 'lodash'
    import {actionTypes, mutationTypes} from '@/core/constants'

    export default {
        name: 'Movimentacao',
        data() {
            return {
                totalItens: 0,
                estaBuscandoPorFiltro: false,
                filtrosInterno: this.getFiltros(),
                rotas: [
                    {nome: 'Todas', rota: 'MovimentacaoListagem'},
                    {nome: 'A Receber ', rota: 'MovimentacaoListagemPendente'}
                ],
                tabs: null,
                notificar: false
            }
        },
        mounted() {
            this.$router.push({name: 'MovimentacaoListagem'})
            this.buscarMovimentacoesPendentes()
        },
        methods: {
            redirecionaTodasMovimentacoes() {
                this.$router.push({name: 'MovimentacaoListagem'})
            },
            redirecionaMovimentacoesPendentes() {
                this.$router.push({name: 'MovimentacaoListagemPendente'})
            },
            async buscarMovimentacoesPendentes() {
                await this.$store.commit(mutationTypes.MOVIMENTACAO.RESETA_EXTRA)
                const resultado = await this.$store.dispatch(actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACOES_PENDENTES, this.getFiltros())
                this.totalItens = resultado.totalElements
                if (!this.estaBuscandoPorFiltro) {
                    this.deveNotificarMovimentacaoPendente()
                }
            },
            deveNotificarMovimentacaoPendente() {
                if (this.totalItens !== 0) {
                    this.notificar = true
                    this.tabs = 1
                    this.redirecionaMovimentacoesPendentes()
                } else {
                    this.notificar = false
                    this.tabs = 0
                    this.redirecionaTodasMovimentacoes()
                }
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.movimentacao.todasMovimentacoes.filtros)
            },
            async tratarEventoBuscaSimples(valor) {
                await this.$store.commit(mutationTypes.MOVIMENTACAO.RESETA_PAGE)
                await this.$store.commit(mutationTypes.MOVIMENTACAO.RESETA_EXTRA)
                this.filtrosInterno.conteudo.value = valor
                this.estaBuscandoPorFiltro = true
                this.notificar = false
                await this.buscar()
            },
            async buscar() {
                await this.$store.commit(mutationTypes.MOVIMENTACAO.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES, this.getFiltrosInterno())
                await this.buscarMovimentacoesPendentes()
            },
            async tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                await this.buscar()
                this.estaBuscandoPorFiltro = false
                this.notificar = true
            },
            async tratarEventoPaginar(paginacao) {
                await this.$store.commit(mutationTypes.MOVIMENTACAO.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES, paginacao)
                await this.buscar()
            }
        }
    }
</script>

<style scoped lang="stylus">
    @media (max-width 500px)
        .az-form-content
            padding 0 !important

</style>
