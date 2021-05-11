<template>
    <movimentacao-listagem-tabela
        :itens="$store.state.movimentacao.movimentacaoPendente.items"
        :paginacao="$store.state.movimentacao.todasMovimentacoes.paginacao"
        :paginas="$store.state.movimentacao.movimentacaoPendente.totalPages"
        :total-itens="$store.state.movimentacao.movimentacaoPendente.totalElements"
        @acessar="tratarEventoAcessar"
        @paginar="tratarEventoPaginar"
    />
</template>

<script>
    import MovimentacaoListagemTabela from './MovimentacaoListagemTabela'

    export default {
        name: 'MovimentacaoListagemPendente',
        components: {MovimentacaoListagemTabela},
        methods: {
           async tratarEventoAcessar(item) {
                this.$router.push({
                    name: 'MovimentacaoDetalhe',
                    params: {id: item.id},
                    query:{rotaName:'MovimentacaoListagemPendente'}
                })
            },
            tratarEventoPaginar(paginacao) {
                this.$emit('tratarEventoPaginar', paginacao)
            }
        }
    }
</script>

