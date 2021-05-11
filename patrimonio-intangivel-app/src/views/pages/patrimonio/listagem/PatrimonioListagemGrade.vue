<template>
    <az-form>
        <v-row>
            <v-flex text-center v-if="!itens.length">
                <h2 class="titulo-vazio">Não há patrimônios cadastrados</h2>
            </v-flex>

            <v-col
                :key="index"
                @click="$emit('acessar', item)"
                cols="12"
                md="3"
                sm="6"
                v-for="(item, index) in itens"
                xs="12">
                <v-card
                    class="cardEmDesenvolvimentoSelecionado"
                    flat
                    min-height="110">
                    <v-layout class="layoutEmDesenvolvimento" justify-end md12 sm12 xs12>
                        <v-icon
                            class="icon-em-elaboracao ma-1"
                            color="white"
                            size="15"
                            v-if="item.situacao === 'EM_ELABORACAO'">
                            fas fa-edit
                        </v-icon>
                    </v-layout>

                    <v-card-title class="ml-4 mt-0 pt-0 pb-8 grey--text text--darken-2 ">
                        <span class="card-titulo text-truncate">{{item.nome | textoSemValor}}</span>
                        <span class="card-subtitulo body-2 ">
                            <v-icon size="17">{{tipos[item.tipo].icone}}</v-icon>
                            {{tipos[item.tipo].nome}}
                        </span>
                        <span class="card-titulo body-2 mt-2 mb-2">N° {{item.numero | textoSemValor}}</span>
                    </v-card-title>
                </v-card>
            </v-col>
        </v-row>

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
</template>

<script>
    import {mutationTypes} from '@/core/constants'

    export default {
        name: 'TodosPatrimoniosGrade',
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
        mounted() {
            this.$gtag.event('GradePatrimonios', { method: 'Google' })
        },
        methods: {
            resetaPage() {
                this.$store.commit(mutationTypes.PATRIMONIO.RESETA_PAGE)
            },
        }
    }
</script>

<style lang="stylus" scoped>

    .icon-em-elaboracao
        background-color #ff9800
        border-radius 3px
        height 30px
        width 30px
        padding-left 2px
        padding-bottom 2px

    .cardEmDesenvolvimentoSelecionado:hover
        background-color #e7e7e7
        cursor pointer

    .card-titulo
        font-size 15px
        line-height 15px
        display block
        width 100%
        margin-bottom 3px
        max-width 90%

    .card-subtitulo
        font-size 13px
        line-height 13px
        margin-top 5px

    .titulo-vazio
        color #777
        font-size 18px

    .layoutEmDesenvolvimento
        min-height 35px
</style>
