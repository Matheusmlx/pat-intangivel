<template>
    <v-dialog
        v-model="dialog"
        width="90vh">
        <template v-slot:activator="{ on }">
            <a @click="dialog = true" v-on="on" class="text--secondary body-2">Ver Mais</a>
        </template>

        <v-card class="dialog">
            <v-card-title
                class="subtitle-1 font-weight-bold primary white--text"
                primary-title>
                Amortizações do Bem
                <v-spacer/>
                <v-btn
                    @click="dialog = false"
                    icon
                    x-small
                    dark>
                    <v-icon class="iconClose">fas fa-times</v-icon>
                </v-btn>
            </v-card-title>

            <v-card-text class=" d-flex justify-center align-center">
                <v-row>
                    <v-col cols="12">
                        <v-simple-table class="simple-table">
                            <v-responsive
                                class="overflow-y-auto"
                                max-height="50vh">
                                <thead>
                                <tr>
                                    <th :key="headers.value" class="font-weight-bold text-center"
                                        v-for="headers in amortizacoes.headers">
                                        {{getNomeCamposPersonalizados(headers.value,'VISUALIZACAO')}}
                                    </th>
                                </tr>
                                </thead>

                                <tbody>
                                <tr :key="item.id" v-for="item in amortizacoesDoBem" class="text-center">
                                    <td style="width: 14%" class="grey--text caption">
                                        {{item.orgaoSigla}}
                                    </td>
                                    <td style="width: 10%" class="grey--text caption">
                                        {{item.dataInicial|retornaMesAno(item.dataInicial)}}
                                    </td>
                                    <td style="width: 10%" class="grey--text caption">
                                        {{returnToLocaleStringTaxa(item.taxaAplicada)}}%
                                    </td>
                                    <td style="width: 23%" class="grey--text caption">
                                        {{returnToLocaleStringMoeda(item.valorAnterior)}}
                                    </td>
                                    <td style="width: 23%" class="grey--text caption">
                                        {{returnToLocaleStringMoeda(item.valorPosterior)}}
                                    </td>
                                    <td style="width: 20%" class="grey--text caption">
                                        {{returnToLocaleStringMoeda(item.valorSubtraido)}}
                                    </td>
                                </tr>
                                </tbody>
                            </v-responsive>
                        </v-simple-table>
                    </v-col>
                </v-row>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
    import {createNamespacedHelpers} from 'vuex'
    const {mapGetters} = createNamespacedHelpers('CamposPersonalizados')

    export default {
        name: 'ModalAmortizacoesDoBem',
        props: {
            amortizacoesDoBem: {
                required: true,
                type: Array,
                default: () => {
                    return []
                }
            },
            taxa: {
                required: true,
                default: '-'
            }
        },
        data() {
            return {
                dialog: false,
                amortizacoes: {
                    headers: [
                        {value: 'orgao'},
                        {value: 'mesAno'},
                        {value: 'taxa'},
                        {value: 'valorAnteriorAmortizacao'},
                        {value: 'valorAposAmortizacao'},
                        {value: 'valorAmortizado'}
                    ],
                }
            }
        },
        filters: {
            retornaMesAno(dataInicial) {
                const data = dataInicial.split('-')
                const ano = data[0]
                const mes = data[1]
                return dataInicial ? `${mes}/${ano}` : '-'
            }
        },
        methods: {
            returnToLocaleStringMoeda(dado) {
                if (typeof dado != 'undefined' && dado != null) {
                    return dado.toLocaleString('pt-br', {style: 'currency', currency: 'BRL'})
                }
                return '----'
            },
            returnToLocaleStringTaxa(dado) {
                if (typeof dado != 'undefined' && dado != null && dado !== 0) {
                    return dado.toLocaleString('pt-br', {minimumFractionDigits: 2})
                }
                return '----'
            }
        },
        computed:{
            ...mapGetters(['getNomeCamposPersonalizados'])
        }
    }
</script>

<style lang="stylus" scoped>
    a
        text-decoration: underline grey

    .iconClose
        opacity: 0.6

    .v-data-table td
        height: 48px

    .v-data-table th
        font-size: 0.75rem
        padding: 10px 2px
</style>
