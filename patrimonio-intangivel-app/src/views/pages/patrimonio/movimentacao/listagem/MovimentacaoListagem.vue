<template>
    <movimentacao-listagem-tabela
        :itens="itens"
        :paginacao="$store.state.movimentacao.todasMovimentacoes.paginacao"
        :paginas="paginas"
        :total-itens="totalItens"
        @acessar="tratarEventoAcessar"
        @paginar="tratarEventoPaginar"
    />
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import MovimentacaoListagemTabela from './MovimentacaoListagemTabela'
    import _ from 'lodash'

    export default {
        name: 'MovimentacaoListagem',
        components: {MovimentacaoListagemTabela},
        data() {
            return {
                filtrosInterno: this.getFiltros(),
                itens: [],
                paginas: 0,
                totalItens: 0
            }
        },
        watch: {
            '$store.state.movimentacao.todasMovimentacoes.filtros'() {
                this.buscarTodasMovimentacoes()
            }
        },
        async mounted() {
            this.$gtag.event('MovimentacoesListagem', {method: 'Google'})
        },
        methods: {
            getFiltros() {
                return _.cloneDeep(this.$store.state.movimentacao.todasMovimentacoes.filtros)
            },
            async buscarTodasMovimentacoes() {
                this.$store.commit(mutationTypes.MOVIMENTACAO.RESETA_EXTRA)
                const resultado = await this.$store.dispatch(actionTypes.MOVIMENTACAO.BUSCAR_TODAS_MOVIMENTACOES, this.getFiltros())
                this.itens = resultado.items
                this.paginas = resultado.totalPages
                this.totalItens = resultado.totalElements
            },
             tratarEventoAcessar(item) {
                if (item.situacao === 'EM_ELABORACAO') {
                    this.$router.push({
                        name: 'NovaMovimentacao',
                        params: {id: item.patrimonio},
                        query:{rotaName: 'MovimentacaoListagem'}
                    })
                } else {
                    this.$router.push({
                        name: 'MovimentacaoDetalhe',
                        params: {id: item.id},
                        query:{rotaName:'MovimentacaoListagem'}
                    })
                }
            },
            async tratarEventoPaginar(paginacao) {
                this.$store.commit(mutationTypes.MOVIMENTACAO.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES, paginacao)
                await this.buscarTodasMovimentacoes()
            }
        }
    }
</script>
