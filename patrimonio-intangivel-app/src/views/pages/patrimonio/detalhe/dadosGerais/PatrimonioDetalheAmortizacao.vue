<template>
    <v-row class="white pl-3 pr-3 ml-0 mr-0">
        <v-col cols="12">
            <span class="session-title">Dados de Amortização</span>
        </v-col>
        <v-col cols="12">
            <v-simple-table class="simple-table">
                <thead>
                <tr>
                    <th :key="headers.value" class="font-weight-bold"
                        v-for="(headers, index) in DadosAmortizados.headers">
                        {{getNomeCamposPersonalizados(headers.value,'VISUALIZACAO')}}
                        <az-info-button
                            v-if="getTooltipCamposPersonalizados('taxaAmortizacao','VISUALIZACAO') && index === 1">
                            {{getTooltipCamposPersonalizados('taxaAmortizacao','VISUALIZACAO')}}
                        </az-info-button>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="grey--text">{{configAmortizacao.metodo|metodo(configAmortizacao.metodo)}}</td>
                    <td class="grey--text">{{returnToLocaleStringTaxa(configAmortizacao.taxa)}}%</td>
                    <td class="grey--text">{{returnToLocaleStringMoeda(configAmortizacao.valorAmortizadoMensal)}}</td>
                    <td class="grey--text">{{returnToLocaleStringMoeda(configAmortizacao.valorAmortizadoAcumulado)}}
                    </td>
                    <td class="grey--text">{{returnToLocaleStringMoeda(valorAjustado)}}</td>
                </tr>
                </tbody>
            </v-simple-table>
        </v-col>
        <v-col align="end" cols="12" v-if="amortizacoesDoBem && amortizacoesDoBem.length > 0">
            <modal-amortizacoes-do-bem :amortizacoesDoBem="amortizacoesDoBem" :taxa="configAmortizacao.taxa"/>
        </v-col>
    </v-row>
</template>
<script>
    import ModalAmortizacoesDoBem from '@/views/components/ModalAmortizacoesDoBem'
    import {createNamespacedHelpers} from 'vuex'

    const {mapGetters} = createNamespacedHelpers('CamposPersonalizados')

    export default {
        components: {ModalAmortizacoesDoBem},
        props: {
            configAmortizacao: {
                required: true,
                type: Object,
                default: () => {
                    return {}
                }
            },
            amortizacoesDoBem: {
                required: true,
                type: Array,
                default: () => {
                    return []
                }
            },
            valorAjustado: {
                required: true,
                default: () => {
                    return null
                }
            },
        },
        data() {
            return {
                DadosAmortizados: {
                    headers: [
                        {value: 'metodo'},
                        {value: 'taxaAmortizacao'},
                        {value: 'valorAmortizadoMensal'},
                        {value: 'valorAmortizadoAcumulado'},
                        {value: 'valorLiquido'},
                    ],
                }
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
        filters: {
            metodo(value, item) {
                switch (item) {
                case 'QUOTAS_CONSTANTES':
                    return 'Quotas Constantes'
                case 'SOMA_DIGITOS':
                    return 'Soma Dos Dígitos'
                }
                return value
            }
        },
        computed: {
            ...mapGetters(['getNomeCamposPersonalizados', 'getTooltipCamposPersonalizados'])
        }
    }
</script>

<style scoped lang="stylus">

    .session-title
        font-size 15px
        font-weight bold
        color #777 !important


</style>

<style lang="stylus">
    .theme--light.v-data-table thead tr th
        color: rgba(0, 0, 0, 0.6) !important
</style>
