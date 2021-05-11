<template>
    <div style="min-height:66vh;">
        <botao-nova-movimentacao
            :exibirLinhaDoTempo="exibirLinhaDoTempo"
            :exibirTooltipMovimentacao="exibirTooltipMovimentacao"
            @cadastrarMovimentacao="cadastrarMovimentacao"
        />
        <div v-if="exibirLinhaDoTempo">
            <linha-do-tempo
                :items="items"
                :orgaos="orgaos"
                @buscarOrgaos="prepararParaBuscarOrgaos"
            />
        </div>
        <router-view
            :movimentacao="movimentacao"
            :tipo="tipo"
            :situacao="situacao"
            :orgaos="orgaos"
            :estaDeletando="estaDeletando"
            @fecharModalMovimentacao="cancelarMovimentacao"
            @atualizarMovimentacao="atualizarMovimentacao"
            @enviarMovimentacao="enviarMovimentacao"
            @tratarEventoDeletarMovimentacao="tratarEventoDeletarMovimentacao"/>
    </div>
</template>

<script>
    import BotaoNovaMovimentacao from './BotaoNovaMovimentacao'
    import LinhaDoTempo from './LinhaDoTempo'
    import {actionTypes} from '@/core/constants'

    export default {
        name: 'NovaMovimentacao',
        components: {BotaoNovaMovimentacao, LinhaDoTempo},
        data() {
            return {
                movimentacao: {},
                patrimonioId: null,
                orgaos: [],
                items: [],
                tipo: null,
                situacao: null,
                estaDeletando: false,
                estaInserindo: false,
                exibirLinhaDoTempo: false,
                exibirTooltipMovimentacao: false
            }
        },
        beforeMount() {
            this.buscarMovimentacoes()
            this.setarIdPatrimonio()
            this.buscarPatrimonio()
        },
        methods: {
            async buscarPatrimonio() {
                await this.$store.dispatch(actionTypes.PATRIMONIO.VISUALIZAR_PATRIMONIO_POR_ID, this.patrimonioId)
            },
            setarIdPatrimonio() {
                this.patrimonioId = this.$route.params.id
            },
            setarSituacaoMovimentacao(movimentacao) {
                this.situacao = movimentacao.situacao
            },
            async atualizarMovimentacao(movimentacao) {
                this.estaInserindo = false
                this.movimentacao = await this.$store.dispatch(actionTypes.MOVIMENTACAO.ATUALIZAR_MOVIMENTACAO, movimentacao)
            },
            async cadastrarMovimentacao(tipo) {
                this.movimentacao = await this.$store.dispatch(actionTypes.MOVIMENTACAO.CADASTRAR_MOVIMENTACAO, {
                    tipo: tipo, idPatrimonio: this.$route.params.id
                })
                this.estaInserindo = true
                this.tipo = tipo
                this.setarSituacaoMovimentacao(this.movimentacao)
                await this.buscarOrgaos()
                this.redirecionaParaModal()
            },
            async buscarMovimentacoes() {
                if (!this.estaInserindo) {
                    const resultado = await this.$store.dispatch(actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_PATRIMONIO, {idPatrimonio: this.$route.params.id})
                    this.items = resultado.items
                    this.deveExibirLinhaDoTempo()
                    this.deveExibirTooltipMovimentacao()
                } else {
                    this.estaInserindo = false
                }
            },
            deveExibirLinhaDoTempo() {
                this.exibirLinhaDoTempo = this.items.length !== 0
            },
            deveExibirTooltipMovimentacao() {
                if (this.exibirLinhaDoTempo) {
                    let ehMovimentacaoEmElaboracao = this.items.filter((item) => {
                        return item.situacao !== 'FINALIZADO' && item.situacao !== 'REJEITADO'
                    })
                    this.exibirTooltipMovimentacao = ehMovimentacaoEmElaboracao.length !== 0
                }
            },
            async enviarMovimentacao(movimentacao) {
                await this.$store.dispatch(actionTypes.MOVIMENTACAO.ENVIAR_MOVIMENTACAO, movimentacao)
                await this.cancelarMovimentacao()
            },
            async cancelarMovimentacao() {
                await this.$router.push({
                    name: 'NovaMovimentacao',
                    params: {id: this.patrimonioId}
                })
                await this.buscarMovimentacoes()
            },
            async buscarOrgaos() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.$store.dispatch(actionTypes.MOVIMENTACAO.BUSCAR_ORGAOS_DESTINO, this.movimentacao)
                this.orgaos = resultado.items
                this.habilitarLoadingGlobal()
            },
            prepararParaBuscarOrgaos(movimentacao) {
                this.movimentacao = movimentacao
                this.tipo = movimentacao.tipo
                this.setarSituacaoMovimentacao(movimentacao)
                this.buscarOrgaos()
                this.redirecionaParaModal()
            },
            redirecionaParaModal() {
                this.$router.push({
                    name: 'ModalMovimentacao',
                    params: {movimentacaoId: this.movimentacao.id}
                })
            },
            async tratarEventoDeletarMovimentacao() {
                await this.cancelarMovimentacao()
                await this.$store.dispatch(actionTypes.MOVIMENTACAO.DELETAR_MOVIMENTACAO, this.movimentacao.id)
                this.estaDeletando = false
            }
        }
    }
</script>
