<template>
    <v-container fluid>
        <div>
            <az-desfazer :show="showDesfazer" text='Documento removido!' @desfazer="desfazerExclusao" @closeButtom="showDesfazer=false"/>
        </div>
        <v-row class="white pl-3 pr-3 ml-0 mr-0 pb-5">
            <v-col cols="12">
                <v-card class="documentos" flat>
                    <v-expansion-panels active-class="" flat>
                        <v-expansion-panel>
                            <v-expansion-panel-header @click="abrirPanelDocumentos" class="documentos-panel-header">
                                <h4 class="session-title">Documentos ({{ documentos.length }})</h4>
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                                <v-col cols="12" class="ml-n7">
                                    <az-call-to-action
                                        v-if="verificaPermissaoEdicao"
                                        slot="actions"
                                        class="btn-novo"
                                        hideBorder
                                        @click="novoDocumento()|abrirPanelDocumentos()">
                                        <v-icon>add_circle</v-icon>
                                        Novo
                                    </az-call-to-action>
                                </v-col>
                                <v-col cols="12" md="12" class="mt-0 pt-0 ml-n3">
                                    <v-simple-table class="simple-table">
                                        <thead>
                                        <tr>
                                            <th :key="titulo.text" class="font-weight-bold"
                                                v-for="titulo in documentosHeaders">
                                                {{ getNomeCamposPersonalizados(titulo.value, 'DOCUMENTOS') }}
                                            </th>
                                        </tr>
                                        </thead>

                                        <tbody id="listagemTabela">
                                        <tr :key="documento.id" v-for="(documento,index) in documentos">
                                            <td>
                                                <az-text-view-edit
                                                    v-model="documento.numero"
                                                    :estaAdicionando="adicionando"
                                                    :disabled="!verificaPermissaoEdicao"
                                                    name="Numero"
                                                    maxlength="50"
                                                    :counter="50"
                                                    :validate="'required|max:50'"
                                                    placeholder="Informe o número"
                                                    @input="tratarEventoSalvar(documento),validarNumeroUnico(documento)"
                                                    @setaEditando="setaEditando"
                                                    @retiraEditando="retiraEditando"
                                                    @estaAdicionando="estaAdicionando"/>
                                            </td>
                                            <td>
                                                <az-text-view-edit-date
                                                    v-model="documento.data"
                                                    :estaAdicionando="adicionando"
                                                    :required="valorDataObrigatorio || getObrigatorioCamposPersonalizados('data', 'DOCUMENTOS')"
                                                    :disabled="!verificaPermissaoEdicao"
                                                    :editar="valorDataObrigatorio && documento.id === documentoObrigatorioId"
                                                    name="Data NL"
                                                    @verificaObrigatoriedade="verificaValorDataObrigatorio(documento)"
                                                    @input="tratarEventoSalvar(documento)"
                                                    @setaEditando="setaEditando"
                                                    @retiraEditando="retiraEditando"
                                                    @estaAdicionando="estaAdicionando"/>
                                            </td>

                                            <td>
                                                <az-text-view-edit-money
                                                    class="ma-0 pa-0"
                                                    v-model="documento.valor"
                                                    :estaAdicionando="adicionando"
                                                    :disabled="!verificaPermissaoEdicao"
                                                    :required="valorDataObrigatorio || getObrigatorioCamposPersonalizados('valor', 'DOCUMENTOS')"
                                                    :required-message="'O campo é obrigatório'"
                                                    :validation-field="validateRequeridAzMoney"
                                                    :editar="valorDataObrigatorio && documento.id === documentoObrigatorioId"
                                                    name="Valor"
                                                    placeholder="Informe o valor"
                                                    @verificaObrigatoriedade="verificaValorDataObrigatorio(documento)"
                                                    @input="tratarEventoSalvar(documento)"
                                                    @setaEditando="setaEditando"
                                                    @retiraEditando="retiraEditando"
                                                    @estaAdicionando="estaAdicionando"/>
                                            </td>

                                            <td>
                                                <az-text-view-edit-select-tipo-documento
                                                    v-model="documento.idTipoDocumento"
                                                    :estaAdicionando="adicionando"
                                                    :disabled="!verificaPermissaoEdicao"
                                                    name="Tipo"
                                                    :items="tiposDocumento"
                                                    placeholder="Selecione"
                                                    no-data-text="Não há tipos com esse nome"
                                                    :error-messages="errors.collect('Tipo')"
                                                    :validate="'required'"
                                                    @input="tratarEventoSalvar(documento),validarNumeroUnico(documento)"
                                                    @setaEditando="setaEditando"
                                                    @retiraEditando="retiraEditando"
                                                    @estaAdicionando="estaAdicionando"/>
                                            </td>

                                            <td class="larguraMinima">
                                                <az-text-view-edit-file-input
                                                    v-model="documento.uriAnexo"
                                                    :value="documento"
                                                    :estaAdicionando="adicionando"
                                                    :disabled="!verificaPermissaoEdicao"
                                                    :validate="getObrigatorioCamposPersonalizados('uriAnexo', 'DOCUMENTOS') ? 'required' : ''"
                                                    name="Anexo"
                                                    stylus="mt-4"
                                                    placeholder="Selecione o Arquivo"
                                                    @input="tratarEventoSalvarAnexo(documento)"
                                                    @setaEditando="setaEditando"
                                                    @retiraEditando="retiraEditando"
                                                    @estaAdicionando="estaAdicionando"/>
                                            </td>
                                            <az-remove-button @remover="removerDocumento(documento)"
                                                              icon="fas fa-minus-circle"/>
                                        </tr>

                                        <tr v-if="adicionando">
                                            <td>
                                                <v-text-field
                                                    v-model="docNovo.numero"
                                                    v-validate="'required|max:50'"
                                                    maxlength="50"
                                                    name="Numero"
                                                    :counter="50"
                                                    placeholder="Informe o número"
                                                    :error-messages="errors.collect('Numero')"
                                                    @blur="tratarEventoSalvar(docNovo)|validarNumeroUnico(docNovo)"/>
                                            </td>
                                            <td>
                                                <az-date
                                                    v-model="docNovo.data"
                                                    :is-required="valorDataObrigatorio ||
                                                    getObrigatorioCamposPersonalizados('data', 'DOCUMENTOS')"
                                                    date
                                                    placeholderDate="__/__/____"
                                                    v-mask="'##/##/####'"
                                                    name-date="Data"
                                                    @input="tratarEventoSalvar(docNovo)"/>
                                            </td>
                                            <td @focusout="tratarEventoSalvar(docNovo)">
                                                <az-money
                                                    v-model="docNovo.valor"
                                                    :maxLength="18"
                                                    name="Valor"
                                                    :required="valorDataObrigatorio ||
                                                    getObrigatorioCamposPersonalizados('valor', 'DOCUMENTOS')"

                                                    :requeridMessage="valorDataObrigatorio ||
                                                    getObrigatorioCamposPersonalizados('valor', 'DOCUMENTOS') ?

                                                    'O campo é obrigatório' : ' '"
                                                    :validation-field="validateRequeridAzMoney"
                                                    placeholder="Informe o valor"/>
                                            </td>
                                            <td>
                                                <v-autocomplete
                                                    v-model="docNovo.idTipoDocumento"
                                                    :items="tiposDocumento"
                                                    item-text="descricao"
                                                    item-value="id"
                                                    name="Tipo"
                                                    no-data-text="Não há tipos com esse nome"
                                                    placeholder="Selecione"
                                                    :error-messages="errors.collect('Tipo')"
                                                    v-validate="'required'"
                                                    @input="tratarEventoSalvar(docNovo),validarNumeroUnico(docNovo)"/>
                                            </td>
                                            <td>
                                                <az-text-view-edit-file-input-novo
                                                    v-model="docNovo.uriAnexo"
                                                    :value="docNovo"
                                                    :validate="getObrigatorioCamposPersonalizados('uriAnexo', 'DOCUMENTOS') ? 'required' : ''"
                                                    name="Anexo"
                                                    placeholder="Selecione o Arquivo"
                                                    @input="tratarEventoSalvarAnexo(docNovo)"/>
                                            </td>
                                            <az-remove-button @remover="tratarEventoDeletarDocumentoNovo" stylus="mt-5"
                                                              icon="fas fa-minus-circle"/>
                                        </tr>
                                        </tbody>
                                    </v-simple-table>
                                </v-col>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </v-expansion-panels>
                </v-card>
            </v-col>
        </v-row>
    </v-container>

</template>

<script>
import {actionTypes, mutationTypes} from '@/core/constants'
import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
import {createNamespacedHelpers} from 'vuex'

const {mapGetters} = createNamespacedHelpers('CamposPersonalizados')
export default {
    props: ['patrimonio'],
    data() {
        return {
            showDesfazer: false,
            documentosHeaders: [
                {value: 'numero'},
                {value: 'data'},
                {value: 'valor'},
                {value: 'tipoDocumento'},
                {value: 'uriAnexo'},
            ],
            documentos: [],
            tiposDocumento: [],
            docNovo: {},
            adicionando: false,
            estaEditando: [],
            documentoVazio:true,
            valorDataObrigatorio: false,
            validateRequeridAzMoney: 0,
            documentoObrigatorioId: null
        }
    },
    async created() {
        await this.buscarTipoDocumentos()
        await this.buscarDocumentos()
    },
    methods: {
        setaIdObrigatorio(id) {
            this.documentoObrigatorioId = id
        },
        async verificaValorDataObrigatorio(doc) {
            const documentoEncontrado = this.tiposDocumento.find(element => element.id === doc.idTipoDocumento)
            if (documentoEncontrado && documentoEncontrado.descricao === 'Nota Fiscal') {
                this.valorDataObrigatorio = true
                this.validateRequeridAzMoney++
                await this.$validator._base.validateAll()
                return true
            } else {
                this.valorDataObrigatorio = false
                return true
            }
        },
        setaEditando(value) {
            this.estaEditando.push(value)
        },
        retiraEditando(value) {
            const index = this.estaEditando.indexOf(value)
            this.estaEditando.splice(index, 1)
        },
        abrirPanelDocumentos() {
            setTimeout(() => {
                let options = {duration: 945, offset: -1, easing: 'linear',}
                this.$vuetify.goTo('#listagemTabela', options)
            }, 250)
        },
        novoDocumento() {
            if (this.validaSeEstaEditando()) {
                if (this.validarCadastrosFinalizados(this.docNovo)) {
                    this.adicionando = true
                    this.docNovo = {idMovimentacao: this.$route.params.id}
                } else {
                    this.mostrarNotificacaoAviso('Finalize o cadastro para cadastrar novos documentos')
                }
            }
        },
        tratarEventoDeletarDocumentoNovo(){
            this.adicionando = false
            this.docNovo = {}
        },
        validarCadastrosFinalizados(obj) {
            return Object.keys(obj).length === 0
        },
        async validarNumeroUnico(documentoValidar) {
            if (!documentoValidar.id || documentoValidar.id) {
                const documentosCadastrados = await this.$store.state.movimentacao.documentos
                const documentoEncontrado = documentosCadastrados.find(element => {
                    return element.numero === documentoValidar.numero && element.idTipoDocumento === documentoValidar.idTipoDocumento && element.id !== documentoValidar.id
                })
                if (!documentoEncontrado) {
                    return
                } else {
                    documentoValidar.numero = null
                    this.mostrarNotificacaoErro('Já existe um documento com esse número')
                    this.$validator._base.validateAll()
                }
            }
        },
        validaSeEstaEditando() {
            if (this.estaEditando.length !== 0) {
                this.mostrarNotificacaoAviso('Finalize a edição do documento antes de criar um novo')
                return false
            }
            return true
        },
        estaAdicionando() {
            this.mostrarNotificacaoAviso('Finalize o cadastro para realizar a edição')
        },
        async buscarTipoDocumentos() {
            this.tiposDocumento = await this.$store.dispatch(actionTypes.MOVIMENTACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO_MOVIMENTACAO)
        },
        async tratarEventoSalvar(documento) {
            if (this.valorDataObrigatorio || this.getObrigatorioCamposPersonalizados('valor', 'DOCUMENTOS')) {
                this.validateRequeridAzMoney++
            }
            this.setaIdObrigatorio(documento.id)
            if (await this.verificaValorDataObrigatorio(documento)) {
                documento = this.retiraEspacosVazios(documento)
                this.desabilitarLoadingGlobal()
                if (documento.valor === undefined || documento.valor === 0) {
                    this.docNovo.valor = null
                }
                const estaValidado = await this.$validator._base.validateAll()
                if (estaValidado && (!this.valorDataObrigatorio || documento.valor && documento.data)) {
                    if (typeof documento.id == 'undefined') {
                        documento.idPatrimonio = this.patrimonio.id
                        await this.$store.dispatch(actionTypes.MOVIMENTACAO.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO, documento)
                        this.docNovo = {}
                        this.adicionando = false
                        await this.buscarDocumentos()
                    } else {
                        await this.$store.dispatch(actionTypes.MOVIMENTACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO_MOVIMENTACAO, documento)
                    }
                }
                this.habilitarLoadingGlobal()
            }
        },
        async buscarDocumentos() {
            this.documentos = await this.$store.dispatch(actionTypes.MOVIMENTACAO.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO, this.$route.params.id)
        },
        retiraEspacosVazios(documento) {
            if (documento.numero) {
                documento.numero = documento.numero.trim()
            }
            return documento
        },
        async tratarEventoSalvarAnexo(anexo) {
            this.desabilitarLoadingGlobal()
            if (anexo.uriAnexo) {
                anexo.uriAnexo = await this.$store.dispatch(actionTypes.MOVIMENTACAO.DOCUMENTO.DOCUMENTO_UPLOAD_MOVIMENTACAO, anexo)
                await this.tratarEventoSalvar(anexo)
            }
            this.habilitarLoadingGlobal()
        },
        async removerDocumento(documento) {
          if(typeof documento.numero !== 'undefined' && typeof documento.id !== 'undefined') {
              await this.$store.dispatch(actionTypes.MOVIMENTACAO.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO,documento)
              this.showDesfazer = true
              setTimeout(() => {
                  this.showDesfazer=false
              }, 6000)
          } else {
              this.$store.commit(mutationTypes.MOVIMENTACAO.DOCUMENTO.REMOVER_DOCUMENTOS_MOVIMENTACAOES,documento)
              this.documentoVazio = true
          }
        },
        async desfazerExclusao() {
            const documentoBackup = this.$store.state.movimentacao.documentoBackup
            await this.$store.dispatch(actionTypes.MOVIMENTACAO.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO,documentoBackup)
            this.documentoVazio = true
            await this.buscarDocumentos()
            this.showDesfazer = false
        }
    },
    computed: {
        ...mapGetters(['getNomeCamposPersonalizados', 'getObrigatorioCamposPersonalizados']),
        verificaPermissaoEdicao() {
            return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
        }
    }
}
</script>

<style lang="stylus">

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

.alturaRolagem
    overflow-y auto

.larguraMinima
    padding 0 16px 0 16px !important
    width 204px

.tamanhoPermitidoClass
    color #a5a5a5
    margin-top -10px
    font-size 12px

.btn-novo
    font-size 16px

</style>
