<template>
    <div>
        <div class="d-flex align-center justify-center">
            <v-timeline :class="!verMais ? 'mb-16' : 'mb-6'">
                <v-timeline-item
                    v-for="item in movimentacoes"
                    :key="item.id"
                    :color="ehMovimentacaoEmElaboracao(item) ? '#f9a825': 'primary'"
                    :icon="ehMovimentacaoEmElaboracao(item) ? 'priority_high' : 'done'"
                    fill-dot small>
                    <template v-slot:opposite>
                        <span class="text-left text--darken-1 grey--text text-body-2">{{item.dataDeAlteracao | azDate('DD/MM/YYYY')}}</span>
                    </template>
                    <v-card color="mx-auto" width="700"
                            v-if="ehMovimentacaoEmElaboracao(item)"
                            @click="formatarMovimentacao(item)">
                        <v-card-title
                            class="font-weight-bold text-body-1 pb-0 text--darken-3 yellow--text">
                            {{item.tipo | azEnum(tipoMovimentacao)}}
                        </v-card-title>
                        <v-card-text class="text-left text--darken-1 grey--text">
                            <span class="font-weight-bold">De: </span>{{item.orgaoOrigem.descricao}}<br/>
                            <span class="font-weight-bold">Para: </span>{{descricaoOrgaoDestino(item.orgaoDestino)}}<br/>
                            <span class="font-weight-bold">Situação: </span>{{item.situacao | azEnum(situacaoMovimentacao)}}
                        </v-card-text>
                    </v-card>
                    <v-card color="mx-auto" width="700" v-else
                    @click="redirecionaParaVisualizacao(item)">
                        <v-card-title
                            class="font-weight-bold text-body-1 pb-0 text--darken-1 primary--text">
                            {{item.tipo | azEnum(tipoMovimentacao)}}
                        </v-card-title>
                        <v-card-text class="text-left text--darken-1 grey--text">
                            <span class="font-weight-bold">De: </span>{{item.orgaoOrigem.descricao}}<br/>
                            <span class="font-weight-bold">Para: </span>{{descricaoOrgaoDestino(item.orgaoDestino)}}<br/>
                            <span class="font-weight-bold">Situação: </span>{{item.situacao | azEnum(situacaoMovimentacao)}}
                        </v-card-text>
                    </v-card>
                </v-timeline-item>
            </v-timeline>
        </div>
        <div class="d-flex align-center justify-center pb-16" v-if="verMais">
            <v-btn color="primary" class="call-to-action text-body-2" @click="verMaisLinhaDoTempo">
                Ver Mais
            </v-btn>
        </div>
    </div>
</template>

<script>

    export default {
        name: 'LinhaDoTempo',
        props: ['items', 'orgaos'],
        data() {
            return {
                movimentacoes: [],
                verMais: false,
                movimentacaoSelecionada: {}
            }
        },
        computed: {
            movimentacoesMaiorQueCinco() {
                return this.items.length > 5
            }
        },
        watch: {
            'items'() {
                this.formatarLinhaDoTempo()
            }
        },
        mounted() {
            this.$gtag.event('MovimentacoesLinhaTempo', { method: 'Google' })
            this.formatarLinhaDoTempo()
        },
        methods: {
            descricaoOrgaoDestino(item) {
                return item ? item.descricao : ''
            },
            ehMovimentacaoEmElaboracao(movimentacao) {
                return movimentacao.situacao === 'EM_ELABORACAO'
            },
            verMaisLinhaDoTempo() {
                this.movimentacoes = this.items
                this.verMais = false
            },
            formatarMovimentacao(item) {
                this.movimentacaoSelecionada = {
                    id: item.id,
                    orgaoDestino: item.orgaoDestino ? item.orgaoDestino : null,
                    orgaoOrigem: item.orgaoOrigem.id,
                    idPatrimonio: this.$route.params.id,
                    motivo: item.motivo,
                    tipo: item.tipo,
                    situacao: item.situacao
                }
                this.buscarOrgaos()
            },
            buscarOrgaos() {
                this.$emit('buscarOrgaos', this.movimentacaoSelecionada)
            },
            async formatarLinhaDoTempo() {
                this.verMais = false
                this.movimentacoes = this.items
                if (this.movimentacoesMaiorQueCinco) {
                    this.movimentacoes = this.items.slice(0, 5)
                    this.verMais = true
                }
            },
            async redirecionaParaVisualizacao(item) {
                this.$router.push({
                    name: 'MovimentacaoDetalhe',
                    params: {id: item.id},
                    query:{rotaName:'NovaMovimentacao'}
                })
            },
        }
    }
</script>
