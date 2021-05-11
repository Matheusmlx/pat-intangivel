<template>
    <v-container fluid style="min-height:75vh;">
        <az-toolbar>
            <az-call-to-action
                v-if="verificaPermissaoNovo()"
                slot="actions"
                class="btn-novo"
                hideBorder
                @click="tratarEventoNovo">
                <v-icon>add_circle</v-icon>Novo
            </az-call-to-action>

            <v-flex mr-3 slot="simpleSearch">
                <v-tooltip top>
                    <template v-slot:activator="{ on }">
                        <v-btn
                            @click="tratarEventoListaOuGrade"
                            color="grey"
                            icon
                            v-if="$store.state.patrimonio.isLista"
                            v-on="on">
                            <v-icon size="20">fas fa-th</v-icon>
                        </v-btn>
                        <v-btn @click="tratarEventoListaOuGrade" color="grey" icon v-else v-on="on">
                            <v-icon size="20">fas fa-th-list</v-icon>
                        </v-btn>
                    </template>
                    <span v-if="$store.state.patrimonio.isLista">Visualizar em grade</span>
                    <span v-else>Visualizar em lista</span>
                </v-tooltip>
            </v-flex>

            <v-flex slot="simpleSearch">
                <az-search
                    :filter="filtrosInterno"
                    @remove-filter="tratarEventoRemoverFiltro"
                    @simple-search="tratarEventoBuscaSimples"
                    id="azSearch"
                    simple-search-placeholder="Busque por: Código, nome, situação ou tipo">
                </az-search>
            </v-flex>
            <v-flex>
                <az-menu></az-menu>
            </v-flex>
        </az-toolbar>

        <patrimonio-listagem-tabela class="tabela-listagem"
            :itens="itens"
            :paginacao="$store.state.patrimonio.todosPatrimonios.paginacao"
            :paginas="paginas"
            :tipos="tipos"
            :total-itens="totalItens"
            @acessar="tratarEventoAcessar"
            @filtrarPorTipo="tratarEventoFiltrarPorTipo"
            @paginar="tratarEventoPaginar"
            v-if="$store.state.patrimonio.isLista"/>

        <patrimonio-listagem-grade
            :itens="itens"
            :paginacao="$store.state.patrimonio.todosPatrimonios.paginacao"
            :paginas="paginas"
            :tipos="tipos"
            :total-itens="totalItens"
            @acessar="tratarEventoAcessar"
            @paginar="tratarEventoPaginar"
            v-else/>

        <az-exportar-relatorio-xls v-if="totalItens"  :mensagem="'Exportar XLS'" @exportarXls="exportarRelatorioXls"/>
    </v-container>
</template>

<script>
    import _ from 'lodash'
    import {actionTypes, mutationTypes, reconhecimento, tipoPatrimonio} from '@/core/constants'
    import PatrimonioListagemGrade from './PatrimonioListagemGrade'
    import PatrimonioListagemTabela from './PatrimonioListagemTabela'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import {createNamespacedHelpers} from 'vuex'
    import constants from '@/core/constants/action-types'

    const { mapGetters, mapActions } = createNamespacedHelpers(
        'CamposPersonalizados'
    )

    export default {
        name: 'PatrimonioListagem',
        components: { PatrimonioListagemTabela, PatrimonioListagemGrade},
        data() {
            return {
                filtrosInterno: this.getFiltros(),
                filtrosAvancados:{},
                tipoPatrimonio,
                itens: [],
                paginas: 0,
                totalItens: 0,
                idItemParaDeletar: null,
                tipos: {
                    DIREITOS_AUTORAIS: {
                        nome: 'Direitos Autorais',
                        icone: 'far fa-copyright',
                    },
                    RECEITAS_FORMULAS_PROJETOS: {
                        nome: 'Receitas, Fórmulas E Projetos',
                        icone: 'fas fa-file-alt'
                    },
                    TITULOS_DE_PUBLICACAO: {
                        nome: 'Títulos de Publicação',
                        icone: 'fas fa-book-open'
                    },
                    LICENCAS: {
                        nome: 'Licenças',
                        icone: 'fas fa-key'
                    },
                    SOFTWARES: {
                        nome: 'Softwares',
                        icone: 'mouse'
                    },
                    MARCAS: {
                        nome: 'Marcas',
                        icone: 'far fa-registered'
                    },
                    reconhecimento:reconhecimento
                },
            }
        },
        computed: {
            ...mapGetters(['getObjetoValidado']),
            verificaPermissaoRetroativa() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Patrimonio.Retroativo',
                ])
            },
        },
        methods: {
            ...mapActions([
                constants.CAMPOS_PERSONALIZADOS.GET_ALL_CAMPOS_PERSONALIZADOS,
            ]),
            async exportarRelatorioXls(){
                await this.$store.dispatch(actionTypes.PATRIMONIO.BAIXAR_RELATORIO_LISTAGEM_PATRIMONIO_XLS,this.totalItens)
            },
            async tratarEventoBuscaSimples(valor) {
                this.$store.commit(mutationTypes.PATRIMONIO.RESETA_PAGE)
                this.filtrosInterno.conteudo.value = valor
                this.buscar()
            },
            async buscar() {
                this.$store.commit(mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS,this.getFiltrosInterno())
                await this.buscaTodosPatrimonios()
            },
            async buscaTodosPatrimonios() {
                const resultado = await this.$store.dispatch(actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS)
                this.itens = resultado.items
                this.paginas = resultado.totalPages
                this.totalItens = resultado.totalElements
            },
            tratarEventoListaOuGrade() {
                const isLista = this.$store.state.patrimonio.isLista
                this.$store.commit(mutationTypes.PATRIMONIO.SET_VISUALIZACAO_LISTAGEM,!isLista)
            },
            tratarEventoNovo() {
                this.$router.push({ name: 'TipoNovo' })
            },
            tratarEventoPaginar(paginacao) {
                this.$store.commit(mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS,paginacao)
                this.buscar()
            },
            tratarEventoFiltrarPorTipo() {
                this.buscar()
            },
            tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                this.buscar()
            },
            tratarEventoAcessar(item) {
                const id = item.id
                if (item.situacao === 'ATIVO') {
                    this.$router.push({ name: 'FichaPatrimonio', params: { id } })
                } else {
                    const valido = this.getObjetoValidado(item, 'DADOS_DE_ENTRADA_EDICAO')
                    if (valido) {
                        this.$router.push({
                            name: 'DocumentosEdicao',
                            params: { id },
                        })
                    } else {
                        this.$router.push({
                            name: 'DadosDeEntradaEdicao',
                            params: { id },
                        })
                    }
                }
            },
            getFiltros() {
                return _.cloneDeep(
                    this.$store.state.patrimonio.todosPatrimonios.filtros
                )
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            verificaPermissaoNovo() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Patrimonio.Normal',
                ])
            },
        },
        async mounted() {
            await this.buscaTodosPatrimonios()
            await this[constants.CAMPOS_PERSONALIZADOS.GET_ALL_CAMPOS_PERSONALIZADOS]()
        },
    }
</script>

<style lang="stylus" scoped>
    .btn-novo
        font-size 16px

    .tabela-listagem
        min-height 66vh
</style>
