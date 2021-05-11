<template>
    <v-row>
        <v-col cols="9" lg="11" xs="12">
            <v-card class="elevation-0 documento">
                <v-container fluid>
                    <v-form
                        enctype="multipart/form-data"
                        class="az-form-content pb-0"
                        lazy-validation
                        ref="form">
                        <v-row class="ma-0">
                            <v-col
                                cols="12"
                                lg="2"
                                sm="12"
                                xs="12"
                                md="2"
                                :class="{ active: !verificaPermissaoEdicao}">
                                <v-text-field
                                    v-model="value.numero"
                                    name="Número"
                                    maxlength="50"
                                    v-validate="'required|max:50'"
                                    :error-messages="errors.collect('Número')"
                                    :counter="50"
                                    :disabled="!verificaPermissaoEdicao"
                                    placeholder="Informe o número"
                                    @focusout="tratarEventoEdicaoCampo,validarNumeroUnico()"
                                    @keyup.enter="tratarEventoEdicaoCampo,validarNumeroUnico()"
                                    @blur="tratarEventoEdicaoCampo(),validarNumeroUnico()">
                                    <template v-slot:label>
                                        {{ getNomeCamposPersonalizados('numero', 'DOCUMENTOS') }}
                                        <span class="ml-1 red--text">*</span>
                                    </template>
                                </v-text-field>
                            </v-col>
                            <v-col
                                cols="12"
                                lg="2"
                                sm="12"
                                xs="12"
                                md="2"
                                :class="{ active: !verificaPermissaoEdicao}"
                            >
                                <az-date
                                    v-model="value.data"
                                    date
                                    :is-disabled="!verificaPermissaoEdicao"
                                    v-mask="'##/##/####'"
                                    name-date="Data"
                                    @input="tratarEventoEdicaoCampo"
                                    :is-required="valorDataObrigatorio || getObrigatorioCamposPersonalizados('data', 'DOCUMENTOS')"
                                >
                                    <template v-slot:label-date>
                                        {{ getNomeCamposPersonalizados('data', 'DOCUMENTOS') }}
                                        <span
                                            class="ml-1 red--text"
                                            v-if="valorDataObrigatorio || getObrigatorioCamposPersonalizados('data', 'DOCUMENTOS')"
                                        >*</span>
                                    </template>
                                </az-date>
                            </v-col>
                            <v-col
                                cols="12"
                                lg="2"
                                sm="12"
                                xs="12"
                                md="2"
                                @focusout="tratarEventoEdicaoCampo"
                                :class="{ active: !verificaPermissaoEdicao}"
                            >
                                <az-money
                                    v-model="value.valor"
                                    :required="valorDataObrigatorio || getObrigatorioCamposPersonalizados('valor', 'DOCUMENTOS')"
                                    requeridMessage="O campo é obrigatório"
                                    :validation-field="validateRequeridAzMoney"
                                    :disabled="!verificaPermissaoEdicao"
                                    :maxLength="18"
                                    :name="'valorTotal'+index"
                                    placeholder="Informe o valor"
                                >
                                    <template v-slot:label>
                                        {{ getNomeCamposPersonalizados('valor', 'DOCUMENTOS') }}
                                        <span
                                            class="ml-1 red--text"
                                            v-if="valorDataObrigatorio || getObrigatorioCamposPersonalizados('valor', 'DOCUMENTOS')"
                                        >*</span>
                                    </template>
                                </az-money>
                            </v-col>
                            <v-col
                                cols="12"
                                lg="3"
                                sm="12"
                                xs="12"
                                md="2"
                                :class="{ active: !verificaPermissaoEdicao}"
                            >
                                <v-autocomplete
                                    v-model="value.idTipoDocumento"
                                    :items="tipoDocumento"
                                    item-text="descricao"
                                    item-value="id"
                                    name="Tipo"
                                    no-data-text="Não há tipos com esse nome"
                                    placeholder="Selecione"
                                    :error-messages="errors.collect('Tipo')"
                                    :disabled="!verificaPermissaoEdicao"
                                    v-validate="'required'"
                                    @input="tratarEventoEdicaoCampo,validarNumeroUnico()"
                                    @keyup.enter="tratarEventoEdicaoCampo,validarNumeroUnico()"
                                    @blur="tratarEventoEdicaoCampo(),validarNumeroUnico()"
                                >
                                    <template v-slot:label>
                                        {{ getNomeCamposPersonalizados('tipoDocumento', 'DOCUMENTOS') }}
                                        <span class="ml-1 red--text">*</span>
                                    </template>
                                </v-autocomplete>
                            </v-col>
                            <v-col
                                cols="12"
                                lg="3"
                                sm="12"
                                xs="12"
                                md="4"
                                :class="{ active: !verificaPermissaoEdicao}">
                                <v-file-input
                                    class="mt-3"
                                    v-if="!value.uriAnexo"
                                    :error-messages="errorTamanhoExcedido ==1 ? 'Tamanho permitido: 15MB' : errors.collect('Anexo')"
                                    v-model="value.uriAnexo"
                                    :disabled="!verificaPermissaoEdicao"
                                    name="Anexo"
                                    file
                                    dense
                                    outlined
                                    append-icon="fa-file-upload"
                                    v-validate="getObrigatorioCamposPersonalizados('uriAnexo', 'DOCUMENTOS') ? 'required' : ''"
                                    prepend-icon
                                    accept="image/png, image/jpeg, image/jpg, application/pdf"
                                    placeholder="Selecione o Arquivo"
                                    @change="tratarEventoSalvarAnexo(),validarNumeroUnico()"
                                >
                                    <template v-slot:label>
                                        {{ getNomeCamposPersonalizados('uriAnexo', 'DOCUMENTOS') }}
                                        <span
                                            class="ml-1 red--text"
                                            v-if="getObrigatorioCamposPersonalizados('uriAnexo', 'DOCUMENTOS')"
                                        >*</span>
                                    </template>
                                </v-file-input>
                                <p
                                    class="tamanhoPermitidoClass"
                                    v-if=" !value.uriAnexo && errorTamanhoExcedido!==1 && errorObrigatoriedadeAnexo!==1"
                                >Tamanho permitido: 15MB</p>
                                <v-text-field
                                    outlined
                                    readonly
                                    :disabled="!verificaPermissaoEdicao"
                                    v-if="value.uriAnexo"
                                    :value="nomeAnexo"
                                >
                                    <template v-slot:label>
                                        {{ getNomeCamposPersonalizados('uriAnexo', 'DOCUMENTOS') }}
                                        <span
                                            class="ml-1 red--text"
                                            v-if="getObrigatorioCamposPersonalizados('uriAnexo', 'DOCUMENTOS')"
                                        >*</span>
                                    </template>
                                    <template v-slot:append>
                                        <v-tooltip top>
                                            <template v-slot:activator="{ on }">
                                                <a
                                                    v-on="on"
                                                    slot="activator"
                                                    v-on:click="anularCampoAnexo"
                                                    target="_blank"
                                                    class="az-action-link-icon mt-1"
                                                >
                                                    <i v-if="EhUsuarioNormal" class="fas fa-times-circle"></i>
                                                </a>
                                            </template>
                                            Remover
                                        </v-tooltip>
                                    </template>
                                    <template v-slot:append-outer>
                                        <v-tooltip v-if="value.id" top>
                                            <template v-slot:activator="{ on }">
                                                <a
                                                    v-on="on"
                                                    slot="activator"
                                                    :href="criarLinkDownload(value.uriAnexo)"
                                                    target="_blank"
                                                    class="az-action-link-icon mt-1"
                                                >
                                                    <i class="fas fa-download"></i>
                                                </a>
                                            </template>
                                            Download
                                        </v-tooltip>
                                    </template>
                                </v-text-field>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-container>
            </v-card>
        </v-col>
        <v-col align="center" class="mt-12" lg="1" md="1" ml-1 pl-5 sm="1" xs="12">
            <az-remove-button
                :color="deletarBotao"
                icon="fas fa-minus-circle"
                size="22"
                @remover="removerDocumento(value)"
            />
        </v-col>
    </v-row>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import {createNamespacedHelpers} from 'vuex'

    const {mapGetters} = createNamespacedHelpers('CamposPersonalizados')

    export default {
        props: {
            value: {
                default: () => {
                    return {}
                },
            },
            tipoDocumento: {
                type: Array,
                default: () => {
                    return []
                },
            },
            index: {
                type: Number,
            },
        },
        $_veeValidate: {
            validator: 'new',
        },
        data() {
            return {
                EhUsuarioNormal: true,
                errorObrigatoriedadeAnexo: 0,
                errorTamanhoExcedido: 0,
                regrasAnexo: '',
                nomeAnexo: '',
                deletarBotao: 'grey',
                apagar: true,
                validateRequeridAzMoney: 0,
                valorDataObrigatorio: false,
            }
        },
        mounted() {
            this.tratarPerfilEhUsuarioNormal()
            this.tratarCampoAnexo()
        },
        watch: {
            async 'value.uriAnexo'() {
                this.tratarCampoAnexo()
                if (typeof this.value.uriAnexo == 'string') {
                    await this.tratarEventoEdicaoCampo()
                }
            },
        },
        computed: {
            ...mapGetters([
                'getNomeCamposPersonalizados',
                'getObrigatorioCamposPersonalizados',
            ]),
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Patrimonio.Normal',
                ])
            },
        },
        methods: {
            tratarPerfilEhUsuarioNormal() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal',]) ? this.EhUsuarioNormal = true : this.EhUsuarioNormal = false

            },
            tratarCampoAnexo() {
                if (typeof this.value.uriAnexo == 'string') {
                    this.nomeAnexo = this.value.uriAnexo.split('/')[1]
                }
            },
            anularCampoAnexo() {
                if (this.verificaPermissaoEdicao) {
                    this.value.uriAnexo = null
                    this.errorObrigatoriedadeAnexo = 0
                    this.errorTamanhoExcedido = 0
                }
            },
            async verificaValorDataObrigatorio() {
                const documentoEncontrado = this.tipoDocumento.find(
                    element => element.id === this.value.idTipoDocumento
                )
                if (
                    documentoEncontrado &&
                    documentoEncontrado.descricao === 'Nota Fiscal'
                ) {
                    this.valorDataObrigatorio = true
                    this.validateRequeridAzMoney++
                    await this.$validator._base.validateAll()
                    return true
                } else {
                    this.valorDataObrigatorio = false
                    return true
                }
            },
            async tratarEventoEdicaoCampo() {

                if (
                    this.valorDataObrigatorio ||
                    this.getObrigatorioCamposPersonalizados('valor', 'DOCUMENTOS')
                ) {
                    this.validateRequeridAzMoney++
                }
                if (this.errors.collect('Anexo')) {
                    this.errorObrigatoriedadeAnexo = 1
                }
                const verificaValorDataObrigatorio = await this.verificaValorDataObrigatorio()
                if (verificaValorDataObrigatorio && this.verificaPermissaoEdicao) {
                    this.retiraEspacosVazios()
                    const estaValidado = await this.$validator._base.validateAll()

                    if (
                        estaValidado &&
                        (!this.valorDataObrigatorio || this.value.valor)
                    ) {
                        this.$emit('salvar', this.value)
                    }
                }
            },
            retiraEspacosVazios() {
                if (this.value.numero) {
                    this.value.numero = this.value.numero.trim()
                }
            },
            baixarAnexo() {
                this.$emit('baixarAnexo', this.value.uriAnexo)
            },
            async tratarEventoSalvarAnexo() {
                if (this.extensaoArquivoValida(this.value.uriAnexo) === -1) {
                    this.mostrarNotificacaoErro(
                        'Extensão do arquivo inválida. Envie arquivos nos seguintes formatos .pdf .jpg .png'
                    )
                    this.value.uriAnexo = null
                } else {
                    if (this.validarTamanhoMaximo(this.value.uriAnexo.size)) {
                        this.errorTamanhoExcedido = 1
                        this.value.uriAnexo = await null
                        setTimeout(() => {
                            this.errorTamanhoExcedido = 0
                            this.errorObrigatoriedadeAnexo = 1
                            this.$validator._base.validateAll()
                        }, 3000)
                    } else {
                        await this.$emit('salvarAnexo')
                    }
                }
            },
            extensaoArquivoValida(nomeArquivo) {
                const extensaoAceitas = [
                    '.pdf',
                    '.PDF',
                    '.jpg',
                    '.JPG',
                    '.png',
                    '.PNG',
                    '.jpeg',
                    '.JPEG',
                ]
                const extensao = nomeArquivo.name.substr(
                    nomeArquivo.name.lastIndexOf('.'),
                    nomeArquivo.name.length
                )
                return extensaoAceitas.indexOf(extensao)
            },
            validarNumeroUnico() {
                this.$emit('validarUnicoNumero')
            },
            validarTamanhoMaximo(tamanhoAnexo) {
                return tamanhoAnexo >= 15116247

            },
            removerDocumento(documento) {
                this.$emit('removerDocumento', documento)
            },
        },
    }
</script>

<style lang="stylus" scoped>
    .active
        cursor not-allowed

    .documento
        border 1px solid #d9d9d9

    .tamanhoPermitidoClass
        color #a5a5a5
        margin-top -10px
        font-size 12px
</style>
