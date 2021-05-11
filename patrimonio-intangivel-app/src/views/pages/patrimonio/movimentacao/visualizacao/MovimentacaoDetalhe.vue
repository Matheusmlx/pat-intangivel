<template>
    <div>
        <az-back-button :route="{name: buscarRotaAnterior, params: {id: movimentacao.patrimonio}}"
                        :text="filtrarRotaAnterior"/>
        <div class="az-form-content">
            <movimentacao-detalhe-cabecalho
                :movimentacao="movimentacao"
                :eh-destinatario="ehDestinatario"
                @receberMovimentacao="receberMovimentacao"
                @rejeitarMovimentacao="rejeitarMovimentacao"
            />
            <movimentacao-detalhe-linha-do-tempo
                :movimentacao="movimentacao"
            />
            <v-divider/>
            <movimentacao-detalhe-campos
                :movimentacao="movimentacao"
                :patrimonio="patrimonio"
                @atualizar="atualizar"
            />
            <movimentacao-detalhe-patrimonios
                :patrimonio="patrimonio"
            />
            <movimentacao-detalhe-aba-documentos
                :patrimonio="patrimonio"/>
        </div>
    </div>
</template>

<script>
    import MovimentacaoDetalheCabecalho from './MovimentacaoDetalheCabecalho'
    import MovimentacaoDetalheLinhaDoTempo from './MovimentacaoDetalheLinhaDoTempo'
    import MovimentacaoDetalheCampos from './MovimentacaoDetalheCampos'
    import MovimentacaoDetalhePatrimonios from './MovimentacaoDetalhePatrimonios'
    import MovimentacaoDetalheAbaDocumentos from './MovimentacaoDetalheAbaDocumentos'
    import {actionTypes} from '@/core/constants'
    import moment from 'moment'

    export default {
        name: 'MovimentacaoDetalhe',
        components: {
            MovimentacaoDetalheCabecalho,
            MovimentacaoDetalheLinhaDoTempo,
            MovimentacaoDetalheCampos,
            MovimentacaoDetalhePatrimonios,
            MovimentacaoDetalheAbaDocumentos
        },
        data() {
            return {
                movimentacao: {
                    orgaoOrigem: {
                        descricao: ''
                    },
                    orgaoDestino: {
                        descricao: ''
                    }
                },
                patrimonio: {
                    setor: ''
                },
                rotaOrigem: 'NovaMovimentacao',
                movimentacaoId: '',
                ehDestinatario: false
            }
        },
        async mounted() {
            this.$gtag.event('MovimentacoesDetalhe', {method: 'Google'})
            await this.setarMovimentacaoId()
            await this.buscarMovimentacaoPorId()
            await this.buscarPatrimonioPorMovimentacao()
            await this.validarDataEnvioSemRecebimento()
            await this.validarEhDestinatario()
        },
        computed:{
            buscarRotaAnterior(){
                return this.$route.query.rotaName
            },
            filtrarRotaAnterior(){
                return this.configurarNomeRotaAnterior()
            }
        },
        methods: {
            async buscarMovimentacaoPorId() {
                this.movimentacao = await this.$store.dispatch(actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_ID, this.movimentacaoId)
            },
            async buscarPatrimonioPorMovimentacao() {
                this.patrimonio = await this.$store.dispatch(actionTypes.MOVIMENTACAO.BUSCAR_PATRIMONIO_POR_MOVIMENTACAO, this.movimentacao.patrimonio)
            },
            configurarNomeRotaAnterior() {
                let rotaAnterior =  this.buscarRotaAnterior
                if(rotaAnterior === 'NovaMovimentacao'){
                    return 'Voltar para histórico'
                }
                if(rotaAnterior === 'MovimentacaoListagem' || rotaAnterior === 'MovimentacaoListagemPendente'){
                    return 'Voltar para Listagem de Movimentações'
                }
                return `Voltar para ${rotaAnterior}`
            },
            setarMovimentacaoId() {
                this.movimentacaoId = this.$route.params.id
            },
            async atualizar(movimentacao) {
                try {
                    await this.$store.dispatch(actionTypes.MOVIMENTACAO.ATUALIZAR_MOVIMENTACAO, movimentacao)
                    await this.buscarMovimentacaoPorId()
                } catch (e) {
                    this.mostrarNotificacaoErro(this.extraiMensagemDeErro(e))
                    await this.buscarMovimentacaoPorId()
                }
            },
            extraiMensagemDeErro(e) {
                return e.toString().split(':')[1]
            },
            validarDataEnvioSemRecebimento() {
                if (this.validarMesAno() && !(this.movimentacao.situacao === 'FINALIZADO' || this.movimentacao.situacao === 'REJEITADO')) {
                    this.mostrarNotificacaoAviso('Essa movimentação está sem recebimento desde o mês passado.')
                }
            },
            validarMesAno() {
                const dataEnvio = moment(this.movimentacao.dataDeEnvio)
                return (dataEnvio.month() < moment().month() || dataEnvio.year() < moment().year())
            },
            async validarEhDestinatario() {
                let resultado = await this.buscarOrgaos()
                let orgaoDestino = this.movimentacao.orgaoDestino

                resultado.items.forEach(element => {
                    if (element.id === orgaoDestino.id) {
                        this.ehDestinatario = true
                        return
                    }
                })
            },
            async buscarOrgaos() {
                return await this.$store.dispatch(actionTypes.COMUM.BUSCAR_TODOS_ORGAOS)
            },
            async receberMovimentacao(idMovimentacao) {
                await this.$store.dispatch(actionTypes.MOVIMENTACAO.RECEBER_MOVIMENTACAO, idMovimentacao)
                this.mostrarNotificacaoSucesso('Movimentação recebida com sucesso')
                await this.buscarMovimentacaoPorId()
            },
            async rejeitarMovimentacao(id) {
                await this.$store.dispatch(actionTypes.MOVIMENTACAO.REJEITAR_MOVIMENTACAO, id)
                this.mostrarNotificacaoSucesso('Movimentação rejeitada com sucesso')
                await this.buscarMovimentacaoPorId()
            }
        }
    }
</script>
