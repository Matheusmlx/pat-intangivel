<template>
    <az-container class="patrimonioListagemTabela">
        <az-form>
            <v-data-table
                :headers="colunas"
                :items="itens"
                ref="itemTabela"
                :loading="false"
                :options.sync="paginacaoInterna"
                :server-items-length="totalItens"
                @click:row="tratarClick"
                class="az-table-list az-table-list-access tabela-patrimonio"
                hide-default-footer
                must-sort
                no-data-text="Não há patrimônios cadastrados">
                <template v-slot:header.codigo>
                    Código
                    <az-info-button>
                        <p class="ma-0">N° Patrimônio: cadastros ativos</p>
                        <p class="ma-0">Cód. Registro: cadastros em elaboração</p>
                    </az-info-button>
                </template>
                <template v-slot:item.codigo="{item}">
                    {{$options.filters.retornaCodigoOuNumero(item.id,item.numero)}}
                </template>
                <template v-slot:item.nome="{item}">
                    <span class="d-inline-block text-truncate" style="max-width: 30vw;">
                        {{item.nome | textoSemValor}}
                    </span>
                </template>
                <template v-slot:item.situacao="{ item }">
                    <v-icon
                        class="mr-1"
                        color="secondary"
                        size="13"
                        v-if="item.situacao === 'EM_ELABORACAO'">
                        fas fa-edit
                    </v-icon>
                    {{ item.situacao | azEnum(situacaoPatrimonio) }}
                </template>
                  <template v-slot:item.orgao="{item}">
                    <span class="d-inline-block text-truncate" style="max-width: 30vw;">
                        {{item.orgao | textoSemValor}}
                    </span>
                </template>
                <template v-slot:item.tipo="{item}">
                    <v-icon class="mr-1" size="13">
                        {{tipos[item.tipo].icone}}
                    </v-icon>
                    {{tipos[item.tipo].nome}}
                </template>
                <template v-slot:item.colunaEscondida="{  }">
                    <v-icon>keyboard_arrow_right</v-icon>
                    <span class="mobile">Acessar</span>
                </template>
            </v-data-table>

            <div class="az-pagination">
                <v-pagination
                    v-if="paginas > 1"
                    v-model="paginacaoInterna.page"
                    :length="paginas"
                    next-icon="fas fa-chevron-right"
                    prev-icon="fas fa-chevron-left"/>

                <v-spacer v-if="paginas <= 1"/>

                <div class="az-select-pagination">
                    <span>Linhas por página:</span>
                    <v-select :items="linhasPorPagina" @change="resetaPage" v-model="paginacaoInterna.rowsPerPage"/>
                </div>
            </div>
        </az-form>
    </az-container>
</template>

<script>
    import {mutationTypes} from '@/core/constants'
    import {mapState} from 'vuex'

    export default {
        name: 'PatrimonioListagemTabela',
        props: {
            itens: {
                required: true,
                type: Array,
                default: () => {
                    return []
                }
            },
            paginacao: {
                required: true,
                type: Object
            },
            paginas: {
                required: true,
                type: Number
            },
            totalItens: {
                required: true,
                type: Number
            },
            tipos: {
                required: true,
                type: Object
            }
        },
        data() {
            return {
                colunas: [
                    {
                        text: 'Código',
                        value: 'codigo',
                        sortable: true,
                        align: 'left',
                        width: '15%',
                        class: 'black--text'
                    },
                    {
                        text: 'Nome Patrimônio',
                        value: 'nome',
                        sortable: true,
                        align: 'left',
                        width: '25%',
                        class: 'black--text'
                    },
                    {
                        text:'Órgão',
                        value:'orgao',
                        sortable:true,
                        align:'left',
                        width:'10%',
                        class:'black--text'
                    },
                    {
                        text: 'Situação',
                        value: 'situacao',
                        sortable: true,
                        align: 'left',
                        width: '15%',
                        class: 'black--text'
                    },
                    {
                        text: 'Tipo',
                        value: 'tipo',
                        sortable: true,
                        align: 'left',
                        width: '25%',
                        class: 'black--text'
                    },
                    {
                        text: '',
                        value: 'colunaEscondida',
                        sortable: false,
                        align: 'center',
                        width: '10%',
                        class: 'black--text'
                    },
                ],
                paginacaoInterna: this.paginacao,
                linhasPorPagina: [12, 24, 64, 128]
            }
        },
        computed: {
            ...mapState(['loki'])
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true
            }
        },
        mounted() {
            this.$gtag.event('ListagemPatrimonios', { method: 'Google' })
        },
        methods: {
            resetaPage() {
                this.$store.commit(mutationTypes.PATRIMONIO.RESETA_PAGE)
            },
            tratarClick(item) {
                this.$emit('acessar', item)
            },
            tratarFiltro() {
                this.$emit('filtrarPorTipo', this.tipoPatrimonio)
            }
        },
        filters: {
            retornaCodigoOuNumero(codigo, numero) {
                if (numero) {
                    return numero
                }
                return codigo
            }
        }
    }
</script>

<style lang="stylus">
    .patrimonioListagemTabela
        .az-table-list tbody tr
            cursor pointer !important

    @media (max-width: 720px)
        .tabela-patrimonio
            .mobile
                display inline-block
                margin-left 3px

        .v-data-table__mobile-table-row
            cursor pointer !important

</style>
