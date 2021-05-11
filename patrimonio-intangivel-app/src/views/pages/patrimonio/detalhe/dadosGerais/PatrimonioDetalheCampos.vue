<template>
    <v-row class="white pl-3 pr-3 ml-0 mr-0">
        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view-edit
                v-model="patrimonio.nome"
                :label="getNomeCamposPersonalizados('nome','DADOS_DE_ENTRADA_EDICAO')"
                name="Nome"
                maxlength="100"
                :validate="getObrigatorioCamposPersonalizados('nome', 'DADOS_DE_ENTRADA_EDICAO') ? 'required|max:100' : ''"
                :counter="100"
                :disabled="!verificaPermissaoEdicao"
                @input="tratarEventoSalvar"/>
        </v-col>
        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view
                :label="getNomeCamposPersonalizados('orgao','DADOS_DE_ENTRADA_EDICAO')"
                :text="orgaoFormatado"/>
        </v-col>
        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view-edit-select-enum
                v-model="setor"
                :label="getNomeCamposPersonalizados('setor','DADOS_DE_ENTRADA_EDICAO')"
                :items="setores"
                name="Setor"
                :noDatatext="'Não há Setores com este nome'"
                :validate="getObrigatorioCamposPersonalizados('setor', 'DADOS_DE_ENTRADA_EDICAO') ? 'required' : ''"
                placeholder="Selecione"
                @input="tratarEventoSalvar"
            />
        </v-col>
        <v-col cols="12">
            <az-text-view-edit-text-area
                v-model="patrimonio.descricao"
                :label="getNomeCamposPersonalizados('descricao','DADOS_DE_ENTRADA_EDICAO')"
                name="Descrição"
                :maxlength="500"
                :counter="500"
                placeholder="Digite a descrição"
                :validate="getObrigatorioCamposPersonalizados('descricao', 'DADOS_DE_ENTRADA_EDICAO') ? 'required' : ''"
                :disabled="!verificaPermissaoEdicao"
                @input="tratarEventoSalvar"/>
        </v-col>


        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view
                :label="getNomeCamposPersonalizados('valorAquisicao','DADOS_DE_ENTRADA_EDICAO')"
                :text="returnToLocaleStringMoeda(patrimonio.valorAquisicao)"/>
        </v-col>

        <v-col cols="12" md="4" sm="6" xs="12">
            <div class="az-text">
                <label>{{getNomeCamposPersonalizados('valorEntrada','DADOS_DE_ENTRADA_EDICAO')}}</label>
                <az-info-button class="ml-1">
                    <p class="ma-0">{{getTooltipCamposPersonalizados('valorEntrada','DADOS_DE_ENTRADA_EDICAO')}}</p>
                </az-info-button>
                <p>{{returnToLocaleStringMoeda(patrimonio.valorEntrada)}}</p>
            </div>
        </v-col>

        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view
                :label="getNomeCamposPersonalizados('reconhecimento','DADOS_DE_ENTRADA_EDICAO')"
                :text="$options.filters.azEnum(patrimonio.reconhecimento, reconhecimento)"/>
        </v-col>

        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view-edit-date
                v-model="patrimonio.dataAquisicao"
                :label="getNomeCamposPersonalizados('dataAquisicao','DADOS_DE_ENTRADA_EDICAO')"
                name="Data Aquisição"
                :max-date="moment(patrimonio.dataAtivacao).format('YYYY-MM-DDTHH:mm:ssZZ')"
                :disabled="!verificaPermissaoEdicao"
                :required="getObrigatorioCamposPersonalizados('dataAquisicao', 'DADOS_DE_ENTRADA_EDICAO')"
                @input="tratarEventoSalvar"/>
        </v-col>

        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view
                :text="$options.filters.azDate(patrimonio.dataAtivacao)"
                :label="getNomeCamposPersonalizados('dataAtivacao','DADOS_DE_ENTRADA_EDICAO')"/>
        </v-col>

        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view-edit-date
                v-model="patrimonio.dataNL"
                :label="getNomeCamposPersonalizados('dataNL','DADOS_DE_ENTRADA_EDICAO')"
                name="Data NL"
                :required="getObrigatorioCamposPersonalizados('dataNL', 'DADOS_DE_ENTRADA_EDICAO')"
                :disabled="!verificaPermissaoEdicao"
                :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                @input="tratarEventoSalvar"/>
        </v-col>

        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view-edit
                v-model="patrimonio.numeroNL"
                :label="getNomeCamposPersonalizados('numeroNL','DADOS_DE_ENTRADA_EDICAO')"
                name="Número NL"
                placeholder="_ _ _ _NL_ _ _ _ _ _"
                mask="####NL######"
                maxlength="12"
                :counter="12"
                :validate="getObrigatorioCamposPersonalizados('numeroNL', 'DADOS_DE_ENTRADA_EDICAO') ? 'required|max:12|min:12' : ''"
                :disabled="!verificaPermissaoEdicao"
                @keydown.enter="tratarEventoSalvar"
                @input="tratarEventoSalvar"/>
        </v-col>

        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view-edit-auto-complete-fornecedor
                id="fornecedor"
                v-model="patrimonio.fornecedor"
                :label="getNomeCamposPersonalizados('fornecedor','DADOS_DE_ENTRADA_EDICAO')"
                name="CNPJ"
                placeholder="Informe a razão social ou o CNPJ"
                :items="fornecedores"
                :cnpjPesquisadoContemMascara="cnpjPesquisadoContemMascara"
                :fornecedorSelecionado="fornecedorSelecionado"
                :eUmNome="eUmNome"
                item-value="id"
                no-data-text="Não há fornecedores com este CNPJ"
                :disabled="!verificaPermissaoEdicao"
                :validate="getObrigatorioCamposPersonalizados('fornecedor', 'DADOS_DE_ENTRADA_EDICAO')? 'required' : ''"
                :search="tratarEventoBuscaFornecedores"
                @input="tratarEventoSalvar"/>
        </v-col>

        <v-col cols="12" md="4" sm="6" xs="12" v-if="patrimonio.inicioVidaUtil && patrimonio.fimVidaUtil">
            <az-text-view
                :label="getNomeCamposPersonalizados('periodoLicenca','VISUALIZACAO')"
                :text="retornaTextoPeriodoVidaUtil(patrimonio.periodoVidaUtil.dia,patrimonio.periodoVidaUtil.mes, patrimonio.inicioVidaUtil, patrimonio.fimVidaUtil)"/>
        </v-col>

        <v-col cols="12" md="4" sm="6" xs="12">
            <az-text-view
                :label="getNomeCamposPersonalizados('contaContabil','DADOS_DE_ENTRADA_EDICAO')"
                :text="$options.filters.contaContabil(patrimonio.contaContabil)"/>
        </v-col>
    </v-row>
</template>

<script>
    import {actionTypes} from '@/core/constants'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import {createNamespacedHelpers} from 'vuex'

    const {mapGetters} = createNamespacedHelpers('CamposPersonalizados')
    export default {

        props: {
            patrimonio: {
                required: true
            },
            orgao: {
                required: true
            },
            setor: {
                required: true
            },
            setores: {
                required: true
            }
        },
        $_veeValidate: {
            validator: 'new'
        },
        data() {
            return {
                desativado: true,
                fornecedores: [],
                fornecedorLabel: '',
                estaBuscandoFornecedores: false,
                itemTextFornecedor: 'cpfCnpj',
                rotaOrigem: this.$store.state.rota.origem,
                cnpjPesquisadoContemMascara: false,
                fornecedorSelecionado: true,
                eUmNome: false
            }
        },
        watch: {
            patrimonio() {
                this.buscaFornecedor()
            }
        },
        computed: {
            ...mapGetters(['getObjetoValidado', 'getNomeCamposPersonalizados', 'getObrigatorioCamposPersonalizados', 'getTooltipCamposPersonalizados']),
            orgaoFormatado() {
                return this.orgao ? `${this.orgao.sigla} - ${this.orgao.nome}` : ''
            },
            setorFormatado() {
                return this.setor ? this.setor.nome : ''
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            }
        },
        methods: {
            setarCampo() {
                this.$refs.campoNome.focus()
            },
            async tratarEventoSalvar() {
                this.mostrarNotificacaoAviso('Alterações feitas impactam o memorando. Gere novamente esse memorando!')
                if (this.verificaPermissaoEdicao) {
                    this.fornecedorSelecionado = true
                    this.patrimonio.setor = this.setor
                    this.$emit('salvar', this.patrimonio)
                }
            },
            async buscaFornecedor() {
                if (this.patrimonio.fornecedor) {
                    const resultado = await this.$store.dispatch(actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID, this.patrimonio.fornecedor)
                    this.fornecedores.push(resultado)
                }
            },
            verificaSeValorContemMascara(val) {
                if (val) {
                    if (val.indexOf('.') !== -1 || val.indexOf('/') !== -1 || val.indexOf('-') !== -1) {
                        this.cnpjPesquisadoContemMascara = true
                    } else {
                        this.cnpjPesquisadoContemMascara = false
                        this.verificaSeValorEUmNome(val)
                    }
                }
            },
            verificaSeValorEUmNome(val) {
                if (!isNaN(val) && isFinite(val)) {
                    this.eUmNome = false
                } else {
                    this.eUmNome = true
                }
            },
            async tratarEventoBuscaFornecedores(val) {
                this.fornecedorSelecionado = false
                this.verificaSeValorContemMascara(val)
                if (val) {
                    if (this.estaBuscandoFornecedores) return

                    if (val.length > 1) {
                        this.desabilitarLoadingGlobal()
                        this.estaBuscandoFornecedores = true

                        this.$store.dispatch(actionTypes.COMUM.BUSCAR_FORNECEDORES, val).then(resultado => {
                            this.fornecedores = resultado.items

                            this.estaBuscandoFornecedores = false
                            this.habilitarLoadingGlobal()
                        })
                    }
                } else {
                    this.patrimonio.fornecedor = null
                    this.fornecedores = []
                    this.buscaFornecedor()
                }
            },
            retornaTextoPeriodoVidaUtil(dias, meses, dataInicio, dataFim) {
                let mesesTexto
                let diasTexto

                if (meses > 1) {
                    mesesTexto = meses + ' meses '
                } else {
                    mesesTexto = meses + ' mês '
                }

                if (dias > 1) {
                    diasTexto = dias + ' dias '
                } else {
                    if (dias === 0) {
                        diasTexto = ''
                    } else {
                        diasTexto = dias + ' dia '
                    }
                }

                if (dias >= 30) {
                    let meses = parseInt(mesesTexto.match(/\d/g).join(''))
                    mesesTexto = meses + 1 + ' meses'
                    diasTexto = ''
                }

                return `${this.$options.filters.azDate(dataInicio)} a ${this.$options.filters.azDate(dataFim)}`
            },
            returnToLocaleStringMoeda(dado) {
                if (typeof dado != 'undefined' && dado != null) {
                    return dado.toLocaleString('pt-br', {style: 'currency', currency: 'BRL'})
                }
                return '----'
            },

        },
        filters: {
            contaContabil(value) {
                const pattern = '##.###.##.##'
                if (value && value.codigo) {
                    let i = 0
                    return `${pattern.replace(/#/g, _ => value.codigo[i++])} - ${value.descricao}`
                }
                return ''
            },
            fornecedor(value, fornecedores) {
                if (value && fornecedores && fornecedores.length > 0) {
                    const found = fornecedores.filter(forn => forn.id === value)
                    if (found.length > 0) {
                        return found[0].nome + ' - ' + found[0].cpfCnpj
                    }
                }
                return ''
            }
        }
    }
</script>

<style scoped lang="stylus">
    #fornecedor p
        margin-bottom 0 !important
        padding-bottom 0 !important
</style>
