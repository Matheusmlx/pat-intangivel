<template>
    <div>
        <v-form class="az-form-content mt-0" ref="form">
            <v-container class="white" fluid>
                <v-row>
                    <v-col cols="12" md="4" sm="6" xs="12" :class="[{active: !verificaPermissaoEdicao}, 'nome']">
                        <v-text-field
                            v-model="dadosDeEntrada.nome"
                            v-validate="getObrigatorioCamposPersonalizados('nome', 'DADOS_DE_ENTRADA_EDICAO') ? 'required|max:100' : ''"
                            :disabled="!verificaPermissaoEdicao"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            :counter="100"
                            autofocus
                            :error-messages="errors.collect('Nome do intangível')"
                            maxlength="100"
                            name="Nome do intangível"
                            placeholder="Informe o nome"
                            v-on:mouseleave="salvarFormulario()">
                            <template v-slot:label>
                                {{ getNomeCamposPersonalizados('nome', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span v-if="getObrigatorioCamposPersonalizados('nome', 'DADOS_DE_ENTRADA_EDICAO')"
                                      class="ml-1 red--text">*</span>
                                <az-info-button v-if="getTooltipCamposPersonalizados('nome','DADOS_DE_ENTRADA_EDICAO')">
                                    {{ getTooltipCamposPersonalizados('nome', 'DADOS_DE_ENTRADA_EDICAO') }}
                                </az-info-button>
                            </template>
                        </v-text-field>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12" :class="[{active: !verificaPermissaoEdicao}, 'orgao']">
                        <v-autocomplete
                            v-model="dadosDeEntrada.orgao"
                            :items="orgaos"
                            :disabled="!verificaPermissaoEdicao"
                            :class="{desativado:!verificaPermissaoEdicao}"
                            item-text="descricao"
                            item-value="id"
                            :error-messages="errors.collect('Órgão Responsável')"
                            v-validate="'required'"
                            name="Órgão Responsável"
                            no-data-text="Não há orgãos com este nome"
                            placeholder="Selecione"
                            return-object
                            @change="salvarFormulario($event),selecionaOrgao($event)">
                            <template v-slot:label>
                                {{ getNomeCamposPersonalizados('orgao', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span class="ml-1 red--text">*</span>
                            </template>
                        </v-autocomplete>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12"
                           :class="[{active: !dadosDeEntrada.orgao || !verificaPermissaoEdicao}, 'setor']">
                        <v-autocomplete
                            v-model="dadosDeEntrada.setor"
                            :class="{desativado:!dadosDeEntrada.orgao || !verificaPermissaoEdicao}"
                            :disabled="(!dadosDeEntrada.orgao || ( setores === null || setores.length === 0)) || !verificaPermissaoEdicao"
                            :loading="carregarSetor"
                            :items="setores"
                            item-text="descricao"
                            item-value="id"
                            :error-messages="!dadosDeEntrada.setor ? errors.collect('Setor') : ''"
                            v-validate="dadosDeEntrada.orgao && getObrigatorioCamposPersonalizados('setor', 'DADOS_DE_ENTRADA_EDICAO') ? 'required' : ''"
                            name="Setor"
                            no-data-text="Não há setores com este nome"
                            :placeholder="!dadosDeEntrada.orgao ? ' ' : 'Selecione'"
                            return-object
                            @change="salvarFormulario">
                            <template v-slot:label>
                                {{ getNomeCamposPersonalizados('setor', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span
                                    v-if="dadosDeEntrada.orgao && getObrigatorioCamposPersonalizados('setor', 'DADOS_DE_ENTRADA_EDICAO')"
                                    class="ml-1 red--text"
                                >*</span>
                                <az-info-button
                                    v-if="getTooltipCamposPersonalizados('setor','DADOS_DE_ENTRADA_EDICAO')">
                                    {{ getTooltipCamposPersonalizados('setor', 'DADOS_DE_ENTRADA_EDICAO') }}
                                </az-info-button>
                            </template>
                            <template
                                v-if="dadosDeEntrada.setor && !getObrigatorioCamposPersonalizados('setor', 'DADOS_DE_ENTRADA_EDICAO')"
                                v-slot:append>
                                <v-tooltip top>
                                    <template v-slot:activator="{ on }">
                                        <v-btn v-on="on" icon>
                                            <v-icon small @click="removerSetor">fas fa-times</v-icon>
                                        </v-btn>
                                    </template>
                                    {{ labelBtnCancel }}
                                </v-tooltip>
                            </template>
                        </v-autocomplete>
                    </v-col>

                    <v-col cols="12" :class="[{active: !verificaPermissaoEdicao}, 'descricao']">
                        <v-textarea
                            v-model="dadosDeEntrada.descricao"
                            :disabled="!verificaPermissaoEdicao"
                            :class="{desativado:!verificaPermissaoEdicao}"
                            :counter="500"
                            :error-messages="errors.collect('Descrição')"
                            auto-grow
                            rows="1"
                            maxlength="500"
                            name="Descrição"
                            placeholder="Digite a descrição"
                            v-validate="getObrigatorioCamposPersonalizados('descricao', 'DADOS_DE_ENTRADA_EDICAO') ? 'required' : ''"
                            @focusout="salvarFormulario">
                            <template v-slot:label>
                                {{ getNomeCamposPersonalizados('descricao', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span v-if="getObrigatorioCamposPersonalizados('descricao', 'DADOS_DE_ENTRADA_EDICAO')"
                                      class="ml-1 red--text">*</span>
                            </template>
                        </v-textarea>
                    </v-col>

                    <v-col md="4" sm="12" xs="12" :class="[{active: !verificaPermissaoEdicao}, 'estado']">
                        <v-radio-group
                            v-model="dadosDeEntrada.estado"
                            :disabled="!verificaPermissaoEdicao"
                            :class="{desativado:!verificaPermissaoEdicao}"
                            class="az-text"
                            name="estado"
                            row
                            v-validate="'required'"
                            :error-messages="mostraErroSituacao ? errors.collect('estado') : ''"
                            @change="alteraDadosDeAcordoComASituacao()">
                            <template v-slot:label>
                                {{ getNomeCamposPersonalizados('estado', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span class="ml-1 red--text">*</span>
                                <az-info-button
                                    v-if="getTooltipCamposPersonalizados('estado','DADOS_DE_ENTRADA_EDICAO')">
                                    {{ getTooltipCamposPersonalizados('estado', 'DADOS_DE_ENTRADA_EDICAO') }}
                                </az-info-button>
                            </template>
                            <v-radio label="Em desenvolvimento" value="EM_DESENVOLVIMENTO" name="EM_DESENVOLVIMENTO"/>
                            <v-radio label="Pronto para uso" value="PRONTO_PARA_USO" name="PRONTO_PARA_USO"
                                     @blur="chamarFuncao()"/>
                        </v-radio-group>
                    </v-col>

                    <v-col :class="[{active: estadoEmDesenvolvimento() || !verificaPermissaoEdicao}, 'valorAquisicao']"
                           cols="12" md="4" sm="6" xs="12">
                        <az-money
                            v-if="dadosDeEntrada.id"
                            v-model="dadosDeEntrada.valorAquisicao"
                            :class="{desativado: estadoEmDesenvolvimento() || !verificaPermissaoEdicao}"
                            :disabled="estadoEmDesenvolvimento() || !verificaPermissaoEdicao"
                            :maxLength="18"
                            :event-submit="'blur' "
                            :required="!estadoEmDesenvolvimento()"
                            class="botaoExcluirMoney"
                            :placeholder="estadoEmDesenvolvimento() ? ' ' : 'Informe o valor'"
                            @input="atualizarValorInserido($event)">
                            <template v-slot:label>
                                {{ getNomeCamposPersonalizados('valorAquisicao', 'DADOS_DE_ENTRADA_EDICAO') }} (R$)
                                <span class="ml-1 red--text"
                                      v-if="!estadoEmDesenvolvimento()">*</span>
                            </template>
                        </az-money>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" v-on:focusout="salvarFormulario" xs="12"
                           :class="[{ active: !verificaPermissaoEdicao}, 'reconhecimento']">
                        <az-combo-enum
                            v-model="dadosDeEntrada.reconhecimento"
                            :disabled="!verificaPermissaoEdicao"
                            :class="{desativado:!verificaPermissaoEdicao}"
                            :enum-object="reconhecimento"
                            :insertNullItem="false"
                            :is-required="true"
                            name="Reconhecimento"
                            placeholder="Selecione"
                            @input="resetarEBloquearVencimentoLicenca(), salvarFormulario($event)">
                            <template v-slot:label>
                                {{ getNomeCamposPersonalizados('reconhecimento', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span class="ml-1 red--text">*</span>
                                <az-info-button
                                    v-if="getTooltipCamposPersonalizados('reconhecimento','DADOS_DE_ENTRADA_EDICAO')">
                                    {{ getTooltipCamposPersonalizados('reconhecimento', 'DADOS_DE_ENTRADA_EDICAO') }}
                                </az-info-button>
                            </template>
                        </az-combo-enum>
                    </v-col>

                    <v-col :class="[{active: estadoEmDesenvolvimento() || !verificaPermissaoEdicao}, 'dataAquisicao']"
                           cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="dadosDeEntrada.dataAquisicao"
                            v-mask="masks.date"
                            :class="{desativado:estadoEmDesenvolvimento() || !verificaPermissaoEdicao}"
                            :isDisabled="estadoEmDesenvolvimento() || !verificaPermissaoEdicao"
                            :is-required="getObrigatorioCamposPersonalizados('dataAquisicao', 'DADOS_DE_ENTRADA_EDICAO') && !estadoEmDesenvolvimento()"
                            :limparData="estadoEmDesenvolvimento()"
                            :placeholderDate="estadoEmDesenvolvimento() ? ' ' : placeholders.date"
                            :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            date
                            name-date="Data Aquisicao"
                            @input="salvarFormulario">
                            <template v-slot:label-date>
                                {{ getNomeCamposPersonalizados('dataAquisicao', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span
                                    v-if="!estadoEmDesenvolvimento() && getObrigatorioCamposPersonalizados('dataAquisicao', 'DADOS_DE_ENTRADA_EDICAO')"
                                    class="ml-1 red--text">*</span>
                            </template>
                        </az-date>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12" v-if="verificaPermissaoRetroativa"
                           :class="[{active: !verificaPermissaoEdicao || estadoEmDesenvolvimento()}, 'dataAtivacao']">
                        <az-date
                            v-model="dadosDeEntrada.dataAtivacao"
                            :is-disabled="!verificaPermissaoEdicao || estadoEmDesenvolvimento() || permissaoRetroativaExpirada()"
                            :class="{desativado:!verificaPermissaoEdicao}"
                            v-mask="masks.date"
                            :limparData="estadoEmDesenvolvimento() || !verificaPermissaoRetroativa"
                            :placeholderDate="placeholders.date"
                            :min-date="verificarDataDeCorteInicioRetroativa"
                            :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            :is-required="!estadoEmDesenvolvimento()"
                            date
                            name-date="Data Ativação"
                            @input="validarDataDeAtivacaoEVencimento">
                            <template v-slot:label-date>
                                {{ getNomeCamposPersonalizados('dataAtivacao', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span v-if="!estadoEmDesenvolvimento()" class="ml-1 red--text">*</span>
                                <az-info-button
                                    v-if="getTooltipCamposPersonalizados('dataAtivacao','DADOS_DE_ENTRADA_EDICAO')">
                                    {{ getTooltipCamposPersonalizados('dataAtivacao', 'DADOS_DE_ENTRADA_EDICAO') }}
                                </az-info-button>
                            </template>
                        </az-date>
                    </v-col>

                    <v-col :class="[{active: estadoEmDesenvolvimento() || !verificaPermissaoEdicao}, 'dataNL']"
                           cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="dadosDeEntrada.dataNL"
                            date
                            name-date="Data da NL"
                            :placeholderDate="placeholders.date"
                            v-mask="masks.date"
                            :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            :isDisabled="estadoEmDesenvolvimento() || !verificaPermissaoEdicao"
                            :is-required="!estadoEmDesenvolvimento() && getObrigatorioCamposPersonalizados('dataNL', 'DADOS_DE_ENTRADA_EDICAO')"
                            @input="salvarFormulario">
                            <template v-slot:label-date>
                                {{ getNomeCamposPersonalizados('dataNL', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span class="ml-1 red--text"
                                      v-if="!estadoEmDesenvolvimento() && getObrigatorioCamposPersonalizados('dataNL', 'DADOS_DE_ENTRADA_EDICAO')">*</span>
                            </template>
                        </az-date>
                    </v-col>

                    <v-col :class="[{active: estadoEmDesenvolvimento() || !verificaPermissaoEdicao}, 'numeroNL']"
                           cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="dadosDeEntrada.numeroNL"
                            v-mask="masks.numeroNL"
                            :max-length="100"
                            name="Número da NL"
                            placeholder="_ _ _ _NL_ _ _ _ _ _"
                            v-validate="!estadoEmDesenvolvimento() && getObrigatorioCamposPersonalizados('numeroNL', 'DADOS_DE_ENTRADA_EDICAO') ? 'required|max:12|min:12': ''"
                            :error-messages="errors.collect('Número da NL')"
                            :disabled="estadoEmDesenvolvimento() || !verificaPermissaoEdicao"
                            :class="{desativado:!verificaPermissaoEdicao}"
                            @change="salvarFormulario">
                            <template v-slot:label>
                                {{ getNomeCamposPersonalizados('numeroNL', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span class="ml-1 red--text"
                                      v-if="!estadoEmDesenvolvimento() && getObrigatorioCamposPersonalizados('numeroNL', 'DADOS_DE_ENTRADA_EDICAO')">*</span>
                            </template>
                        </v-text-field>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12" :class="[{active: !verificaPermissaoEdicao}, 'fornecedor']">
                        <v-autocomplete
                            style="max-height:47px;"
                            v-model="dadosDeEntrada.fornecedor"
                            :disabled="!verificaPermissaoEdicao"
                            :class="{desativado:!verificaPermissaoEdicao}"
                            :items="fornecedores"
                            :loading="estaBuscandoFornecedores"
                            :return-object="false"
                            :search-input.sync="buscaFornecedorDinamicamente"
                            :item-text="retornaItemText"
                            item-value="id"
                            v-validate="getObrigatorioCamposPersonalizados('fornecedor', 'DADOS_DE_ENTRADA_EDICAO')? 'required' : ''"
                            name="CNPJ Fornecedor"
                            :error-messages="errors.collect('CNPJ Fornecedor')"
                            no-data-text="Não há fornecedores com este cnpj"
                            placeholder="Informe a razão social ou o CNPJ"
                            @blur="salvarFormulario">
                            <template v-slot:label>
                                {{ getNomeCamposPersonalizados('fornecedor', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span class="ml-1 red--text"
                                      v-if="getObrigatorioCamposPersonalizados('fornecedor', 'DADOS_DE_ENTRADA_EDICAO')">*</span>
                            </template>
                            <template slot="item" slot-scope="data">
                                <span style="max-width: 330px" class="text-truncate">{{
                                        formataCnpj(data.item.cpfCnpj)
                                    }} - {{ data.item.nome }}</span>
                            </template>
                        </v-autocomplete>
                        <az-text-view
                            :text="dadosDeEntrada.fornecedor ? $options.filters.fornecedorFilter(dadosDeEntrada.fornecedor, fornecedores) : ''"/>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12"
                           :class="[{active: estadoEmDesenvolvimento() ||  dadosDeEntrada.vidaIndefinida || reconhecimentoSelecionado || !verificaPermissaoEdicao}, 'dataVencimento']">
                        <az-date
                            v-model="dadosDeEntrada.dataVencimento"
                            v-mask="masks.date"
                            :class="{desativado: dadosDeEntrada.vidaIndefinida || emDesenvolvimentoOuReconhecimentoSelecionado || !verificaPermissaoEdicao}"
                            :is-required="(!dadosDeEntrada.vidaIndefinida && !estadoEmDesenvolvimento() && !reconhecimentoSelecionado)"
                            :isDisabled="dadosDeEntrada.vidaIndefinida || emDesenvolvimentoOuReconhecimentoSelecionado || !verificaPermissaoEdicao"
                            :limparData="dadosDeEntrada.vidaIndefinida || emDesenvolvimentoOuReconhecimentoSelecionado"
                            :placeholderDate="(dadosDeEntrada.vidaIndefinida || emDesenvolvimentoOuReconhecimentoSelecionado) ? ' ' : placeholders.date"
                            :min-date="moment(tratarVencimentoLicenca).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            date
                            label="Vencimento da Licença"
                            name-date="Vencimento da Licença"
                            @input="validarDataDeAtivacaoEVencimento">
                            <template v-slot:label-date>
                                {{ getNomeCamposPersonalizados('dataVencimento', 'DADOS_DE_ENTRADA_EDICAO') }}
                                <span class="ml-1 red--text"
                                      v-if="!dadosDeEntrada.vidaIndefinida && !estadoEmDesenvolvimento() && !reconhecimentoSelecionado">*</span>
                                <az-info-button
                                    v-if="getTooltipCamposPersonalizados('dataVencimento','DADOS_DE_ENTRADA_EDICAO')">
                                    {{ getTooltipCamposPersonalizados('dataVencimento', 'DADOS_DE_ENTRADA_EDICAO') }}
                                </az-info-button>
                            </template>
                        </az-date>

                        <v-checkbox v-if="verificarSeMostrarBotaoVidaUtilIndefinida"
                            v-model="dadosDeEntrada.vidaIndefinida"
                            :class="[{desabilitado: emDesenvolvimentoOuReconhecimentoSelecionado || !verificaPermissaoEdicao}, 'vidaIndefinida']"
                            :disabled="emDesenvolvimentoOuReconhecimentoSelecionado || !verificaPermissaoEdicao"
                            class="botaoVidaIndefinida"
                            label="Vida Útil Indefinida"
                            name="vidaIndefinida"
                            @change="tratarEventoAnularData"/>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12" class="active">
                        <v-text-field v-model="contaContabil" placeholder=" " disabled>
                            <template v-slot:label>
                                {{ getNomeCamposPersonalizados('contaContabil', 'DADOS_DE_ENTRADA_EDICAO') }}
                            </template>
                        </v-text-field>
                    </v-col>
                </v-row>
            </v-container>
        </v-form>

        <div class="az-actions-form">
            <v-btn @click="voltarParaTipoEdicao" color="grey" text outlined>
                <v-icon size="15" class="mr-1">fas fa-arrow-left</v-icon>
                VOLTAR
            </v-btn>
            <v-btn v-if="camposAceitos" color="primary" depressed @click="tratarEventoContinuar">
                Continuar
                <v-icon size="15" class="ml-1">fas fa-arrow-right</v-icon>
            </v-btn>
            <v-tooltip v-else top nudge-top="30" max-width="200">
                <template v-slot:activator="{ on }">
                    <div v-on="on" :class="{'btn-disabled': !camposAceitos}">
                        <v-btn
                            :disabled="!camposAceitos"
                            color="primary"
                            depressed
                            @click="tratarEventoContinuar">
                            Continuar
                            <v-icon size="15" class="ml-1">fas fa-arrow-right</v-icon>
                        </v-btn>
                    </div>
                </template>
                É necessário que todos os campos obrigatórios sejam preenchidos para continuar.
            </v-tooltip>
        </div>
    </div>
</template>

<script>
    import {actionTypes, reconhecimento} from '@/core/constants'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import {createNamespacedHelpers} from 'vuex'
    import moment from 'moment'

    const {mapActions, mapGetters} = createNamespacedHelpers('CamposPersonalizados')

    export default {
        name: 'DadosDeEntrada',
        $_veeValidate: {
            validator: 'new',
        },
        data() {
            return {
                estadoValidacao: true,
                reconhecimento: reconhecimento,
                masks: {
                    numeroNL: '####NL######',
                    date: '##/##/####',
                },
                placeholders: {
                    date: '__/__/____',
                },
                dadosDeEntrada: {
                    dataAquisicao: '',
                    dataNL: ''
                },
                orgaos: [],
                mostraErroSituacao: false,
                setores: [],
                fornecedores: [],
                estadoBemIntangivel: '',
                projetoId: null,
                buscaFornecedorDinamicamente: null,
                camposAceitos: true,
                fornecedorSelecionado: true,
                ehUmNome: false,
                TIPO_PERMITIDO: false,
                estaBuscandoFornecedores: false,
                reconhecimentoSelecionado: false,
                cnpjPesquisadoContemMascara: false,
                carregarSetor: false,
                labelBtnCancel: 'Remover'
            }
        },
        computed: {
            ...mapGetters(['getObjetoValidado', 'getNomeCamposPersonalizados', 'getObrigatorioCamposPersonalizados', 'getTooltipCamposPersonalizados']),
            contaContabil: {
                get() {

                    const pattern = '##.###.##.##'
                    const value = this.dadosDeEntrada.contaContabil
                    if (value && value.codigo) {
                        let i = 0
                        return `${pattern.replace(
                            /#/g,
                            _ => value.codigo[i++]
                        )} - ${value.descricao}`
                    }
                    return null
                }
            },
            emDesenvolvimentoOuReconhecimentoSelecionado() {
                return (this.estadoEmDesenvolvimento() || this.reconhecimentoSelecionado)
            },
            verificaPermissaoRetroativa() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Retroativo'])
            },
            verificarDataDeCorteInicioRetroativa(){
                if (this.$store.state.parametros.dataCorteInicioCadastroRetroativo) {
                    return this.$store.state.parametros.dataCorteInicioCadastroRetroativo
                }
                return moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')
            },
            tratarVencimentoLicenca() {
                return this.verificaPermissaoRetroativa ?
                    (this.dadosDeEntrada && this.dadosDeEntrada.dataAtivacao) ||
                    new Date(2020, 1, 1) : new Date()
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Patrimonio.Normal',
                ])
            },
            verificarSeMostrarBotaoVidaUtilIndefinida() {
                return this.$store.state.parametros.mostrarBotaoVidaUtilIndefinida
            }

        },
        watch: {
            buscaFornecedorDinamicamente(val) {
                this.fornecedorSelecionado = false
                this.verificaSeValorContemMascara(val)
                if (val) {
                    if (this.estaBuscandoFornecedores) return

                    if (val.length > 1) {
                        this.estaBuscandoFornecedores = true

                        this.$store
                            .dispatch(actionTypes.COMUM.BUSCAR_FORNECEDORES, val)
                            .then(resultado => {
                                this.fornecedores = resultado.items
                                this.estaBuscandoFornecedores = false
                            })
                    }
                } else {
                    this.dadosDeEntrada.fornecedor = null
                    this.fornecedores = []
                }
            }
        },
        methods: {
            ...mapActions(['getAllCamposPersonlizados']),
            estadoEmDesenvolvimento() {
                return this.dadosDeEntrada.estado === 'EM_DESENVOLVIMENTO'
            },
            verificarSeDataDeAtivacaoEhMaiorQueDataDeVencimento() {
                if (moment(this.dadosDeEntrada.dataAtivacao).isAfter(moment(this.dadosDeEntrada.dataVencimento))) {
                    this.mostrarNotificacaoErro('A data de ativação não pode ser maior que a data de vencimento')
                    this.dadosDeEntrada.dataAtivacao = null
                }
            },
            async validarDataDeAtivacaoEVencimento() {
                if (moment(this.dadosDeEntrada.dataAtivacao).isAfter(this.dadosDeEntrada.dataVencimento)) {
                    this.dadosDeEntrada.dataAtivacao = await null
                    this.mostrarNotificacaoErro('A data de Vencimento não pode ser menor que a data de Ativação')
                    return
                }
                if (moment(this.dadosDeEntrada.dataAtivacao).isSame(this.dadosDeEntrada.dataVencimento, 'day')) {
                    this.dadosDeEntrada.dataVencimento = await null
                    this.mostrarNotificacaoErro('A data de Vencimento não pode ser no mesmo dia que a data de Ativação')
                    return
                }
                this.salvarFormulario()
            },
            removerSetor() {
                this.dadosDeEntrada.setor = null
                this.salvarFormulario()
            },
            estadoOrgaoSetorENome() {
                return this.dadosDeEntrada.estado && this.dadosDeEntrada.orgao &&
                    (this.getObrigatorioCamposPersonalizados('setor', 'DADOS_DE_ENTRADA_EDICAO') ? this.dadosDeEntrada.setor : true)
                    && (this.getObrigatorioCamposPersonalizados('nome', 'DADOS_DE_ENTRADA_EDICAO') ? this.dadosDeEntrada.nome : true)
            },
            chamarFuncao() {
                this.mostraErroSituacao = true
            },
            verificaPermissaoDataAtivacao() {
                if (!this.verificaPermissaoRetroativa) {
                    this.dadosDeEntrada.dataAtivacao = null
                }
            },
            verificaSeValorContemMascara(val) {
                if (val) {
                    if (
                        val.indexOf('.') !== -1 ||
                        val.indexOf('/') !== -1 ||
                        val.indexOf('-') !== -1
                    ) {
                        this.cnpjPesquisadoContemMascara = true
                    } else {
                        this.cnpjPesquisadoContemMascara = false
                        this.verificaSeValorEhUmNome(val)
                    }
                }
            },
            verificaSeValorEhUmNome(val) {
                this.ehUmNome = isNaN(val) && !isFinite(val)
            },
            retornaItemText(item) {
                if (
                    this.cnpjPesquisadoContemMascara ||
                    this.fornecedorSelecionado
                ) {
                    return `${this.formataCnpj(item.cpfCnpj)}`
                }
                if (this.ehUmNome) {
                    return `${item.nome}`
                }
                return `${item.cpfCnpj}`
            },
            formataCnpj(cnpj = '') {
                cnpj = cnpj.replace(/\D/g, '')
                cnpj = cnpj.replace(
                    /(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/,
                    '$1.$2.$3/$4-$5'
                )
                return cnpj
            },
            resetarEBloquearVencimentoLicenca() {
                const reconhecimentoEhGeracaoOuTransacao =
                    this.dadosDeEntrada.reconhecimento === 'GERACAO_INTERNA' ||
                    this.dadosDeEntrada.reconhecimento ===
                    'TRANSACAO_SEM_CONTRAPRESTACAO'
                this.reconhecimentoSelecionado = false
                if (reconhecimentoEhGeracaoOuTransacao) {
                    this.dadosDeEntrada.dataVencimento = null
                    this.reconhecimentoSelecionado = true
                    this.dadosDeEntrada.vidaIndefinida = false
                }
            },
            tratarEventoAnularData() {
                if (this.dadosDeEntrada.vidaIndefinida) {
                    this.dadosDeEntrada.dataVencimento = null
                }
                this.salvarFormulario()
            },
            atualizarValorInserido(value) {
                this.dadosDeEntrada.valorAquisicao = value
                this.salvarFormulario()
            },
            validaCamposObrigatorios() {
                const validacaoHabilitaEmDesenvolvimento = (this.estadoOrgaoSetorENome() && this.descricaoFornecedor() && this.dadosDeEntrada.reconhecimento)
                this.camposAceitos = true
                if (this.estadoEmDesenvolvimento() && validacaoHabilitaEmDesenvolvimento) {
                    return this.$emit('habilitaPasso3')
                } else if (this.getObjetoValidado(this.dadosDeEntrada, 'DADOS_DE_ENTRADA_EDICAO', this.verificaPermissaoRetroativa, this.permissaoRetroativaExpirada())
                    && this.dadosDeEntrada.estado) {
                    return this.$emit('habilitaPasso3')
                }

                this.camposAceitos = false
                this.$emit('desabilitaPasso3')
            },
            descricaoFornecedor() {
                return (this.getObrigatorioCamposPersonalizados('descricao', 'DADOS_DE_ENTRADA_EDICAO') ? this.dadosDeEntrada.descricao : true)
                    && (this.getObrigatorioCamposPersonalizados('fornecedor', 'DADOS_DE_ENTRADA_EDICAO') ? this.dadosDeEntrada.fornecedor : true)
            },
            alteraDadosDeAcordoComASituacao() {
                if (this.dadosDeEntrada.estado === 'EM_DESENVOLVIMENTO') {
                    this.dadosDeEntrada.valorAquisicao = null
                    this.dadosDeEntrada.dataVencimento = null
                    this.dadosDeEntrada.dataAtivacao = null
                    this.dadosDeEntrada.vidaIndefinida = null
                    this.dadosDeEntrada.dataAtivacao = null
                    this.dadosDeEntrada.dataAquisicao = null
                    this.dadosDeEntrada.dataNL = null
                    this.dadosDeEntrada.numeroNL = null
                }
                this.dadosDeEntrada.vidaIndefinida = false
                this.tratarEventoSalvar()
            },
            async buscaPatrimonio() {

                this.dadosDeEntrada = await this.$store.dispatch(
                    actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID,
                    this.$route.params.id
                )
                this.verificaPermissaoDataAtivacao()
                await this.buscaFornecedor()
                await this.buscarSetores()
            },
            async tratarEventoContinuar() {
                this.$emit('habilitaPasso3')
                await this.$router.push({
                    name: 'DocumentosEdicao',
                    params: {id: this.$route.params.id},
                })
            },
            async tratarEventoSalvar() {
                if (this.verificaPermissaoEdicao) {
                    this.fornecedorSelecionado = true
                    if (typeof this.dadosDeEntrada.fornecedor === 'undefined') {
                        this.dadosDeEntrada.fornecedor = null
                    }
                    this.dadosDeEntrada.vidaIndefinida = false
                    await this.salvarFormulario()
                }
            },
            async buscaOrgaos() {
                const resultado = await this.$store.dispatch(
                    actionTypes.COMUM.BUSCAR_TODOS_ORGAOS
                )
                this.orgaos = resultado.items
                this.selecionaOrgaoSeArrayConterApenasUmOrgao(this.orgaos)
            },
            async buscaFornecedor() {
                if (this.dadosDeEntrada.fornecedor) {
                    const resultado = await this.$store.dispatch(
                        actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID,
                        this.dadosDeEntrada.fornecedor
                    )
                    this.fornecedores.push(resultado)
                }
            },
            async salvarFormulario() {
                if (this.dadosDeEntrada.orgao === undefined && this.getObrigatorioCamposPersonalizados('setor', 'DADOS_DE_ENTRADA_EDICAO')) {
                    this.$nextTick(() => {
                        this.errors.clear()
                        this.$nextTick(() => {
                            this.$validator.reset()
                        })
                    })
                }
                if (!this.verificaPermissaoEdicao) return

                this.fornecedorSelecionado = true
                this.dadosDeEntrada.fornecedor = typeof this.dadosDeEntrada.fornecedor === 'undefined' ? null : this.dadosDeEntrada.fornecedor

                this.dadosDeEntrada.nome = this.dadosDeEntrada.nome && this.dadosDeEntrada.nome.length ? this.dadosDeEntrada.nome.trim() : null

                this.dadosDeEntrada.descricao = this.dadosDeEntrada.descricao && this.dadosDeEntrada.descricao.length ? this.dadosDeEntrada.descricao.trim() : null

                this.validaCamposObrigatorios()

                this.dadosDeEntrada = await this.$store.dispatch(actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO, this.dadosDeEntrada)

            },
            async buscarSetores() {
                if (this.dadosDeEntrada.orgao) {
                    const resultado = await this.$store.dispatch(
                        actionTypes.COMUM.BUSCAR_SETORES,
                        this.dadosDeEntrada.orgao
                    )
                    this.setores = resultado.items
                    this.selecionaSetorSeArrayConterApenasUmSetor(this.setores)
                }
            },
            async selecionaOrgao() {
                this.dadosDeEntrada.setor = null
                this.setores = null
                if (this.dadosDeEntrada.orgao) {
                    this.carregarSetor = true
                    const resultado = await this.$store.dispatch(
                        actionTypes.COMUM.BUSCAR_SETORES,
                        this.dadosDeEntrada.orgao
                    )
                    this.setores = resultado.items
                    this.carregarSetor = false
                    this.selecionaSetorSeArrayConterApenasUmSetor(this.setores)
                }
            },
            selecionaSetorSeArrayConterApenasUmSetor(setores) {
                if (setores.length === 1) {
                    this.dadosDeEntrada.setor = setores[0]
                    this.salvarFormulario()
                }
            },
            selecionaOrgaoSeArrayConterApenasUmOrgao(orgaos) {
                if (orgaos.length === 1) {
                    this.dadosDeEntrada.orgao = orgaos[0].id
                    this.salvarFormulario()
                    this.buscarSetores()
                }
            },
            voltarParaTipoEdicao() {
                this.$router.push({
                    name: 'TipoEdicao',
                    params: {id: this.$route.params.id},
                })
            },
            permissaoRetroativaExpirada() {
                let dataLimite = new Date(this.$store.state.parametros.dataLimiteRetroativo)
                return new Date().getTime() > dataLimite.getTime()
            }
        },
        filters: {
            fornecedorFilter(id, fornecedores) {
                const encontados = fornecedores.filter(f => f.id === id)
                return encontados.length > 0 ? encontados[0].nome : ''
            },
        },
        async mounted() {
            this.$gtag.event('PatrimonioEdicaoDadosGerais', {method: 'Google'})
            await this.buscaPatrimonio()
            await this.buscaOrgaos()
            this.resetarEBloquearVencimentoLicenca()
            this.validaCamposObrigatorios()
        }
    }
</script>

<style lang="stylus">
    .v-autocomplete__content.v-menu__content
        max-width 20%

    .botaoVidaIndefinida
        margin-top 0
        padding-top 0

        label
            font-size 0.9em;

    .v-input--is-disabled:not(.v-input--checkbox)
        color grey

        .v-input__slot
            background-color #EAEAEA
            border-style none

    .active
        cursor not-allowed

    .botaoExcluirMoney .v-icon.v-icon
        font-size 16px !important

</style>
