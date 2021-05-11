        <template>
            <div>
                <az-container>
                    <v-form class="az-form-content mt-0 form" lazy-validation ref="form">
                        <v-container class="white" fluid>
                            <v-row class="campos" justify="center">
                                <v-col cols="12" md="4" sm="6" xs="12">
                                    <v-autocomplete
                                        v-model="dadosGerais.orgao"
                                        :items="orgaos"
                                        item-text="sigla"
                                        item-value="id"
                                        v-validate="'required'"
                                        :error-messages="errors.collect('Órgão Responsável')"
                                        name="Órgão Responsável"
                                        no-data-text="Não há órgãos com este nome"
                                        :placeholder="'Selecione'"
                                        is-required
                                        return-object
                                        @change="verificarCamposObrigatorios($event)">
                                         <template v-slot:label>
                                    {{getNomeCamposPersonalizados('orgaoResponsavel','AMORTIZACAO_MANUAL')}}
                                    <span class="ml-1 red--text">*</span>
                        </template>
                                    </v-autocomplete>
                                </v-col>

                                <v-col cols="12" md="4" sm="6" xs="12">
                                    <az-text-view-edit-month
                                        v-model="dadosGerais.mesReferencia"
                                        :date-mask="'##/####'"
                                        :name="'Mês/Ano de Referência'"
                                        :validate="'required'"
                                        :placeholderDate="'__/____'"
                                        :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                                        @input="verificarCamposObrigatorios">
                                          <template v-slot:label>
                                    {{getNomeCamposPersonalizados('mesAnoDeReferencia','AMORTIZACAO_MANUAL')}}
                                    <span class="ml-1 red--text">*</span>
                        </template>
                                    </az-text-view-edit-month>
                                </v-col>
                            </v-row>

                            <span v-if="progresso" class="progresso">Progresso</span>
                            <v-progress-linear
                                v-if="progresso"
                                color="light-blue"
                                height="10"
                                striped
                                :indeterminate="carregando"/>

                            <v-row v-if="patrimonios.length > 0">
                                <v-col cols="12" md="9" sm="9" xs="8" >
                            <v-card class="documentos">
                                <v-expansion-panels v-model="panel" flat>
                                    <v-expansion-panel>
                                        <v-expansion-panel-header class="documentos-panel-header">
                                            <h4 class="session-title">Patrimônios Amortizados
                                                ({{patrimonios.length}})</h4>
                                        </v-expansion-panel-header>
                                        <v-responsive
                                            class="overflow-y-auto"
                                            max-height="40vh">
                                            <v-expansion-panel-content>
                                                <v-row>
                                                    <v-col v-for="patrimonio in patrimonios" :key="patrimonio.id"
                                                           cols="12" md="2" sm="4" xs="4">
                                                        <v-card class="elevation-0 card-patrimonio">
                                                            <v-card-text class="pa-1 text-center">
                                                                {{patrimonio.numero}}
                                                            </v-card-text>
                                                        </v-card>
                                                    </v-col>
                                                </v-row>
                                            </v-expansion-panel-content>
                                        </v-responsive>
                                    </v-expansion-panel>
                                </v-expansion-panels>
                            </v-card>
                                </v-col>
                                <v-col cols="12" md="3" sm="3" xs="4" >
                                    <v-text-field
                                        v-model="numero"
                                        :counter="10"
                                        maxlength="10"
                                        name="Numero"
                                        placeholder="Pesquisar..."
                                        @input="pesquisarPatrimonio"/>
                                </v-col>
                            </v-row>
                        </v-container>
                    </v-form>

                    <v-divider/>

                    <div class="az-actions-form">
                        <v-btn @click="tratarEventoVoltar" color="grey" text outlined>
                            <v-icon size="15" class="mr-1">fas fa-arrow-left</v-icon>
                            Voltar
                        </v-btn>
                        <v-spacer/>
                        <v-btn
                            :disabled="camposAceitos"
                            color="primary"
                            depressed
                            @click="tratarEventoSalvar">
                            Executar
                            <v-icon size="15" class="ml-1">done</v-icon>
                        </v-btn>
                    </div>
                </az-container>
            </div>
        </template>
<script>
    import {actionTypes} from '@/core/constants'
    import AzTextViewEditMonth from '@/views/components/AzTextViewEditMonth'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import { createNamespacedHelpers } from 'vuex'
    const { mapGetters } = createNamespacedHelpers('CamposPersonalizados')

    export default {
        name: 'AmortizacaoManual',
        components: {AzTextViewEditMonth},
        $_veeValidate: {
            validator: 'new'
        },
        data() {
            return {
                camposAceitos: true,
                dadosGerais: {},
                orgaos: [],
                patrimonios: [],
                patrimoniosBackup: [],
                panel: 1,
                carregando: false,
                rotaOrigem: this.$store.state.rota.origem,
                progresso:  false
            }
        },
        computed:{
            ...mapGetters(['getNomeCamposPersonalizados'])
        },
        async mounted() {
            if(!this.verificaPermissaoEdicao()){
                this.$router.push({ name: this.rotaOrigem })
            }
            await this.buscaOrgao()
        },
        methods: {
            pesquisarPatrimonio(numero) {
                this.patrimonios = this.patrimoniosBackup
                this.patrimonios = this.patrimonios.filter(element => element.numero.match(numero))
            },
            verificarCamposObrigatorios() {
                if (this.dadosGerais.mesReferencia && this.dadosGerais.orgao) {
                    this.camposAceitos = false
                    return true
                } else {
                    return false
                }
            },
            tratarEventoVoltar() {
                this.$router.push({name: 'Inicial'})
            },
            async buscaOrgao() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.$store.dispatch(actionTypes.COMUM.BUSCAR_TODOS_ORGAOS)
                this.habilitarLoadingGlobal()
                this.orgaos = resultado.items
            },
            async tratarEventoSalvar() {
                this.desabilitarLoadingGlobal()
                this.escondePanel()
                this.mostraLinhaDeProgresso()
                this.setaLinhaDeProgressoCarregando()
                const dados = this.criaDados()
                try{
                    const patrimonios = await this.$store.dispatch(actionTypes.AMORTIZACAO.GERAR_AMORTIZACAO_MANUAL, dados)
                    this.patrimonios = patrimonios.items
                    this.patrimoniosBackup = this.patrimonios
                    this.paraLinhaDeProgresso()
                    this.mostrarNotificacaoSucessoDefault()
                }catch (e) {
                    this.mostrarNotificacaoErro(this.extraiMensagemDeErro(e))
                    this.paraLinhaDeProgresso()
                }
                this.abrePanel()
                this.habilitarLoadingGlobal()
            },
            extraiMensagemDeErro(e){
                return e.toString().split(':')[1]
            },
            setaLinhaDeProgressoCarregando(){
                this.carregando = true
            },
            paraLinhaDeProgresso(){
                this.carregando = false
            },
            mostraLinhaDeProgresso(){
                this.progresso = true
            },
            abrePanel() {
                if (this.patrimonios.length > 0) {
                    this.panel = 0
                }
            },
            escondePanel(){
                this.patrimonios = []
            },
            criaDados(){
                const [year, month] = this.dadosGerais.mesReferencia.split('-')
                const dados = {
                    ano: year,
                    mes: month,
                    orgao: this.dadosGerais.orgao
                }
                return dados
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['ContaContabil.Normal'])
            }
        }
    }
</script>

<style scoped lang="stylus">
    .form
        height: 80vh

    .campos
        align-content: center
        padding: 5% 0% 0% 5%

    .session-title
        font-size 15px
        font-weight bold
        color #777 !important

    .documentos
        border solid 1px #e7e7e7 !important
        border-radius 5px !important
        box-shadow none

    .documentos-panel
        border-radius 5px !important

        .v-expansion-panel-content__wrap
            padding 0 10px 16px !important

    .documentos-panel-header
        border-bottom solid 1px #e7e7e7 !important
        background-color #F6F6F6
        border-radius 5px !important
        min-height 48px !important

    .card-patrimonio
        background-color #F6F6F6
        border-radius 5px !important

    .expansionPanel
        max-height 200px

    .progresso
        font-size 14px
        font-weight bold
        color #777 !important
</style>
