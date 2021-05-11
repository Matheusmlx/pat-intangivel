<template>
    <v-form class="az-form-content mt-0 layoutTamanho" lazy-validation ref="form">
        <v-container class="white campos" fluid>
            <v-row >
                 <v-col cols="12" md="4" lg="4" sm="12" xs="12">
                    <v-autocomplete
                        v-model="relatorio.orgao.value"
                        :items="orgaos"
                        item-text="descricao"
                        item-value="id"
                        v-validate="'required'"
                        :error-messages="errors.collect('Órgão Responsável')"
                        name="Órgão Responsável"
                        is-required
                        no-data-text="Não há orgãos com este nome"
                        placeholder="Selecione"
                        @change="informarObrigatoriedadeCampos"
                        @keyup.enter="informarObrigatoriedadeCampos">
                        <template v-slot:label>
                                    {{getNomeCamposPersonalizados('orgaoResponsavel','INVENTARIO')}}
                                    <span class="ml-1 red--text">*</span>
                        </template>
                    </v-autocomplete>
                </v-col>
                <v-col cols="12"  md="4" lg="4" sm="12" xs="12">
                    <az-text-view-edit-month
                        v-model="relatorio.mesReferencia.value"
                        :date-mask="'##/####'"
                        :name="'Mês/Ano de Referência'"
                        :validate="'required'"
                        required
                        :placeholderDate="'__/____'"
                        :min-date="verificarDataDeCorteRetroativa || moment(new Date(2020, 0, 1)).format('YYYY-MM-DDTHH:mm:ssZZ')"
                        :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                        @input="informarObrigatoriedadeCampos">
                        <template v-slot:label>
                                    {{getNomeCamposPersonalizados('mesAnoReferencia','INVENTARIO')}}
                                    <span class="ml-1 red--text">*</span>
                        </template>
                    </az-text-view-edit-month>
                </v-col>
                <v-col cols="12" md="4" lg="4" sm="12" xs="12">
                    <v-radio-group
                        v-model="relatorio.tipo.value"
                        class="az-text"
                        row
                        v-validate="'required'"
                        :error-messages="errors.collect('tipoInventario')"
                        name="tipoInventario"
                        @change="informarObrigatoriedadeCampos()" >
                        <v-radio
                            mark
                            label="Sintético"
                            value="SINTETICO"/>
                        <v-radio
                            label="Analitico"
                            value="ANALITICO"/>
                         <template v-slot:label>
                                    {{getNomeCamposPersonalizados('tipo','INVENTARIO')}}
                                    <span class="ml-1 red--text">*</span>
                        </template>
                    </v-radio-group>
                </v-col>
                <v-col cols="6" xl="1" lg="2" md="2"  sm="3" xs="12">
                <v-tooltip
                        :disabled="!desabilitarBotao"
                        max-width="250"
                        nudge-left="-10"
                        top>
                        <template v-slot:activator="{ on }">
                            <div v-on="on">
                                <v-btn :disabled="desabilitarBotao"
                                    color="primary"
                                    class="ml-1 mr-3"
                                    @click="gerarRelatorioPDF">
                                    GERAR PDF
                                </v-btn>
                            </div>
                        </template>
                        Preencha todos os campos obrigatórios para gerar o relatório
                    </v-tooltip>
                </v-col>
                <v-col cols="4" xl="1" lg="2" md="2"  sm="3" xs="12">
                     <v-tooltip
                        :disabled="!desabilitarBotao"
                        max-width="250"
                        nudge-left="-10"
                        top>
                        <template v-slot:activator="{ on }">
                            <div v-on="on">
                                <v-btn :disabled="desabilitarBotao"
                                    color="primary"
                                    class="mb-9"
                                    @click="gerarRelatorioXLS">
                                    GERAR XLS
                                </v-btn>
                            </div>
                        </template>
                        Preencha todos os campos obrigatórios para gerar o relatório
                    </v-tooltip>
                </v-col>
            </v-row>
        </v-container>
    </v-form>
</template>

<script>
    import _ from 'lodash'
    import {actionTypes} from '@/core/constants'
    import {createNamespacedHelpers} from 'vuex'

    const { mapGetters } = createNamespacedHelpers('CamposPersonalizados')

    export default {
        data(){
            return{
                orgaos:[],
                relatorio: this.getFiltros(),
                informadoObrigatoriedadeCampos: false
            }
        },
        computed: {
            ...mapGetters(['getNomeCamposPersonalizados']),
            desabilitarBotao() {
                return !(this.relatorio.orgao.value && this.relatorio.mesReferencia.value && this.relatorio.tipo.value)
            },
            verificarDataDeCorteRetroativa(){
                return this.$store.state.parametros.dataCorteInicioCadastroRetroativo
            },
        },
        async mounted(){
            this.$gtag.event('Inventarios', { method: 'Google' })
            await this.buscaOrgao()
        },
        methods:{
            async buscaOrgao() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.$store.dispatch(actionTypes.COMUM.BUSCAR_TODOS_ORGAOS)
                this.habilitarLoadingGlobal()
                this.orgaos = resultado.items
            },
            getFiltros() {
                return _.cloneDeep(
                    this.$store.state.relatorio.inventario.filtros
                )
            },
            async informarObrigatoriedadeCampos() {
                if(!this.informadoObrigatoriedadeCampos) {
                    await this.$validator._base.validateAll()
                    this.informadoObrigatoriedadeCampos = true
                }
            },
            async gerarRelatorioPDF() {
                if(this.relatorio.tipo.value === 'SINTETICO') {
                    this.relatorio.formato.value = 'PDF'
                    await this.$store.dispatch(actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_PDF, this.relatorio)
                } else {
                    this.relatorio.formato.value = 'PDF'
                    await this.$store.dispatch(actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_PDF, this.relatorio)
                }
            },
            async gerarRelatorioXLS() {
                if(this.relatorio.tipo.value === 'SINTETICO') {
                    this.relatorio.formato.value = 'XLS'
                    await this.$store.dispatch(actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_XLS, this.relatorio)
                } else {
                    this.relatorio.formato.value = 'XLS'
                    await this.$store.dispatch(actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_XLS, this.relatorio)
                }
            }
        }
    }
</script>

<style scoped lang="stylus">
    .form
        height: 80vh

    .campos
        align-content: center
        padding: 3% 0 0 3%

    .session-title
        font-size 15px
        font-weight bold
        color #777 !important
    .layoutTamanho
        height: 698px !important
</style>
