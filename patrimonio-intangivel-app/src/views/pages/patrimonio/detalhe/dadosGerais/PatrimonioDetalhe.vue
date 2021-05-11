<template>
    <div>
        <patrimonio-detalhe-cabecalho
            :patrimonio="patrimonio"
            @cancelarAtivacao="tratarEventoCancelarAtivacao"/>

        <patrimonio-detalhe-campos
            :patrimonio="patrimonio"
            :orgao="orgao"
            :setor="setor"
            :setores="setores"
            @salvar="tratarEventoSalvar"/>

        <patrimonio-detalhe-amortizacao
            v-if="patrimonio.configAmortizacao"
            :configAmortizacao="patrimonio.configAmortizacao"
            :amortizacoesDoBem="amortizacoesDoBem"
            :valorAjustado="patrimonio.valorLiquido"/>

        <patrimonio-detalhe-documentos/>
    </div>
</template>

<script>
    import PatrimonioDetalheCampos from './PatrimonioDetalheCampos'
    import PatrimonioDetalheAmortizacao from './PatrimonioDetalheAmortizacao'
    import PatrimonioDetalheCabecalho from './PatrimonioDetalheCabecalho'
    import PatrimonioDetalheDocumentos from './PatrimonioDetalheDocumentos'
    import {actionTypes} from '@/core/constants'

    export default {
        components: {
            PatrimonioDetalheCampos,
            PatrimonioDetalheAmortizacao,
            PatrimonioDetalheCabecalho,
            PatrimonioDetalheDocumentos
        },
        name: 'PatrimonioEdicao',
        data() {
            return {
                rotaOrigem: this.$store.state.rota.origem,
                patrimonio: {},
                orgao: {},
                setor: {},
                setores:[],
                amortizacoesDoBem: [],
                tabs: null
            }
        },
        async mounted() {
            this.$gtag.event('FichaIndividual', { method: 'Google' })
            try {
                await this.buscaPatrimonio()
                await this.buscaAmortizacoes()
                await this.buscarSetores()
            } catch (e) {
                await this.$router.push({name: this.rotaOrigem})
            }
        },
        methods: {
            async buscaPatrimonio() {
                this.patrimonio = await this.$store.dispatch(actionTypes.PATRIMONIO.VISUALIZAR_PATRIMONIO_POR_ID, this.$route.params.id)
                this.orgao = this.patrimonio.orgao
                this.setor = this.patrimonio.setor
            },
            async tratarEventoSalvar(patrimonio) {
                this.desabilitarLoadingGlobal()
                try {
                    await this.$store.dispatch(actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO, patrimonio)
                } catch (e) {
                    this.mostrarNotificacaoErro(this.extraiMensagemDeErro(e))
                    await this.buscaPatrimonio()
                }
                this.habilitarLoadingGlobal()
            },
            extraiMensagemDeErro(e) {
                return e.toString().split(':')[1]
            },
            async tratarEventoCancelarAtivacao() {
                await this.$store.dispatch(actionTypes.PATRIMONIO.REVERTER_ATIVACAO, this.patrimonio.id)
                this.mostrarNotificacaoSucessoDefault()
                this.salvaPatrimonioCancelado(this.patrimonio.id)
                await this.$router.push({name: 'DadosDeEntradaEdicao', params: {id: this.patrimonio.id}})
            },
            salvaPatrimonioCancelado(patrimonioId) {
                this.$store.state.patrimonio.ativacaoCanceladaPatrimonioId = patrimonioId
            },
            async buscaAmortizacoes() {
                this.desabilitarLoadingGlobal()
                this.amortizacoesDoBem = await this.$store.dispatch(actionTypes.PATRIMONIO.BUSCAR_AMORTIZACOES, this.patrimonio.id)
                this.habilitarLoadingGlobal()
            },
            async buscarSetores(){
                if(this.orgao){
                    const resultado =await  this.$store.dispatch( actionTypes.COMUM.BUSCAR_SETORES,this.orgao.id)
                    this.setores = resultado.items
                }
            }
        }
    }
</script>
