<template>
    <az-container class="movimentacaoListagemTabela mt-0">
        <az-form class="pa-0">
            <v-data-table
                :headers="colunas"
                ref="itemTabela"
                :loading="false"
                :items="itens"
                :options.sync="paginacaoInterna"
                :server-items-length="totalItens"
                @click:row="tratarClick"
                class="az-table-list az-table-list-access tabela-movimentacao"
                hide-default-footer
                must-sort
                no-data-text="Não há movimentações">
                <template v-slot:header.codigo>
                    Código
                </template>
                <template v-slot:item.dataCadastro="{item}">
                    {{ item.dataCadastro | azDate() }}
                </template>
                <template v-slot:item.tipo="{item}">
                    {{ item.tipo | azEnum(tipoMovimentacao) }}
                </template>
                <template v-slot:item.situacao="{item}">
                    <div v-if="ehSituacaoEmElaboracao(item)">
                      <v-icon x-small color="#FF9F1A" left class="mb-1">fas fa-edit</v-icon> {{ item.situacao | azEnum(situacaoMovimentacao) }}
                    </div>
                    <div v-else>
                        {{ item.situacao | azEnum(situacaoMovimentacao) }}
                    </div>
                </template>
                <template v-slot:item.orgaoDestino="{item}">
                    {{ formatarOrgaoDestino(item) }}
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

    export default {
        name: 'MovimentacaoListagemTabela',
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
                type: Number,
                default: 0
            },
            totalItens: {
                required: true,
                type: Number,
                default: 0
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
                        text: 'Criado em',
                        value: 'dataCadastro',
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
                        width: '15%',
                        class: 'black--text'
                    },
                    {
                        text: 'Origem',
                        value: 'orgaoOrigem',
                        sortable: true,
                        align: 'left',
                        width: '15%',
                        class: 'black--text'
                    },
                    {
                        text: 'Destino',
                        value: 'orgaoDestino',
                        sortable: true,
                        align: 'left',
                        width: '15%',
                        class: 'black--text'
                    },
                    {
                        text: 'Situação',
                        value: 'situacao',
                        sortable: true,
                        align: 'left',
                        width: '20%',
                        class: 'black--text'
                    },
                    {
                        text: '',
                        value: 'colunaEscondida',
                        sortable: false,
                        align: 'center',
                        width: '5%',
                        class: 'black--text'
                    },
                ],
                paginacaoInterna: this.paginacao,
                linhasPorPagina: [12, 24, 64, 128]
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true
            }
        },
        methods: {
            ehSituacaoEmElaboracao(item) {
                return item.situacao === 'EM_ELABORACAO'
            },
            formatarOrgaoDestino(item) {
                return item.orgaoDestino ? item.orgaoDestino : '-'
            },
            resetaPage() {
                this.$store.commit(mutationTypes.MOVIMENTACAO.RESETA_PAGE)
            },
            tratarClick(item) {
                this.$emit('acessar', item)
            }
        }
    }
</script>
<style lang="stylus">
    .movimentacaoListagemTabela
        .az-table-list tbody tr
            cursor pointer !important

        tbody td
            padding 0 10px !important

    @media (max-width: 720px)
        .tabela-movimentacao
            .mobile
                display inline-block
                margin-left 3px

        .v-data-table__mobile-table-row
            cursor pointer !important

</style>
