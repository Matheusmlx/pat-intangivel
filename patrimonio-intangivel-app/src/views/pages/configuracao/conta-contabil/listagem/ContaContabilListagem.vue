<template>
    <div>
        <az-toolbar>
            <az-search :filter="filtrosInterno"
                       @remove-filter="tratarEventoRemoverFiltro"
                       @simple-search="tratarEventoBuscaSimples"
                       simple-search-placeholder="Busque por: conta ou código contábil"
                       slot="simpleSearch">
            </az-search>
        </az-toolbar>

        <conta-contabil-listagem-tabela
            :itens="itens"
            :paginacao="$store.state.contaContabil.todasContasContabeis.paginacao"
            :paginas="paginas"
            :total-itens="totalItens"
            @paginar="tratarEventoPaginar"
            @salvar="tratarEventoSalvar"
            @editar="tratarEventoEditar"/>
    </div>
</template>

<script>
    import _ from 'lodash'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import ContaContabilListagemTabela from './ContaContabilListagemTabela'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'ContaContabilListagem',
        components: {
            ContaContabilListagemTabela
        },
        mounted(){
            if(!this.verificaPermissaoEdicao()){
                this.$router.push({ name: this.rotaOrigem })
            }
        },
        data() {
            return {
                filtrosInterno: this.getFiltros(),
                itens: [],
                paginas: 0,
                totalItens: 0,
                idItemParaDeletar: null,
                rotaOrigem: this.$store.state.rota.origem,
            }
        },
        methods: {
            async buscar() {
                this.$store.commit(mutationTypes.CONTA_CONTABIL.SET_FILTROS_BUSCA_TODAS_CONTAS_CONTABEIS, this.getFiltrosInterno())
                this.setMensagemLoading('Carregando ...')
                const resultado = await this.$store.dispatch(actionTypes.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS)
                this.itens = resultado.items
                this.paginas = resultado.totalPages
                this.totalItens = resultado.totalElements
            },
            tratarEventoPaginar(paginacao) {
                this.$store.commit(mutationTypes.CONTA_CONTABIL.SET_PAGINACAO_BUSCA_TODAS_CONTAS_CONTABEIS, paginacao)
                this.buscar()
            },
            tratarEventoBuscaSimples(valor) {
                this.filtrosInterno.conteudo.value = valor
                this.buscar()
            },
            tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                this.buscar()
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.contaContabil.todasContasContabeis.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            async tratarEventoSalvar(contaContabil) {
                await this.$store.dispatch(actionTypes.CONTA_CONTABIL.SALVAR_CONTA_CONTABIL, contaContabil)
                this.buscar()
            },
            async tratarEventoEditar(contaContabil){
                await this.$store.dispatch(actionTypes.CONTA_CONTABIL.EDITAR_CONTA_CONTABIL, contaContabil)
                this.buscar()
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['ContaContabil.Normal'])
            }
        }
    }
</script>
