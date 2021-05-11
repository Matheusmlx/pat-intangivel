<template>
    <div>
        <v-container class="white ma-0 pa-0" v-if="exibirLinhaDoTempo">
            <div class="pt-7 pl-5">
                <v-tooltip top max-width="550" v-if="exibirTooltipMovimentacao">
                    <template v-slot:activator="{ on, attrs }">
                        <div v-on="on" class="btn-disabled d-inline-flex">
                            <v-btn color="primary" disabled class="call-to-action text-body-2">
                                Nova Movimentação
                                <v-icon right dark>
                                    arrow_drop_down
                                </v-icon>
                            </v-btn>
                        </div>
                    </template>
                    <span>{{getTooltipCamposPersonalizados('tooltip','MOVIMENTACAO')}}</span>
                </v-tooltip>
                <v-menu offset-y :close-on-click="true" v-else>
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn color="primary" v-bind="attrs" class="call-to-action text-body-2" v-on="on" :disabled="!verificaPermissaoEdicao">
                            Nova Movimentação
                            <v-icon right dark>
                                arrow_drop_down
                            </v-icon>
                        </v-btn>
                    </template>
                    <v-card>
                        <v-list>
                            <v-list-item-group v-model="tipo">
                                <div @click="abrirModalNovaMovimentacao" v-for="(text, value) in tipoMovimentacao"
                                     :key="value">
                                    <v-list-item :value="value">
                                        <v-list-item-title class="text-center text--darken-1 grey--text text-body-2">
                                            {{text}}
                                        </v-list-item-title>
                                    </v-list-item>
                                    <v-divider/>
                                </div>
                            </v-list-item-group>
                        </v-list>
                    </v-card>
                </v-menu>
            </div>
        </v-container>
        <v-container class="white d-flex justify-center align-center" style="min-height:66vh;" v-else>
            <div class="text-center mb-12">
                <div class="pb-3">
                    <v-icon x-large>
                        fa-inbox
                    </v-icon>
                </div>
                <p class="az-title__subtitle">Nenhuma <span class="session-title">movimentação</span> realizada, clique
                    para
                    adicionar</p>
                <v-menu offset-y :close-on-click="true">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn color="primary" v-bind="attrs" class="call-to-action text-body-2" v-on="on" :disabled="!verificaPermissaoEdicao">
                            Nova Movimentação
                            <v-icon right dark>
                                arrow_drop_down
                            </v-icon>
                        </v-btn>
                    </template>
                    <v-card>
                        <v-list>
                            <v-list-item-group v-model="tipo">
                                <div @click="abrirModalNovaMovimentacao" v-for="(text, value) in tipoMovimentacao"
                                     :key="value">
                                    <v-list-item :value="value">
                                        <v-list-item-title class="text-center text--darken-1 grey--text">
                                            {{text}}
                                        </v-list-item-title>
                                    </v-list-item>
                                    <v-divider/>
                                </div>
                            </v-list-item-group>
                        </v-list>
                    </v-card>
                </v-menu>
            </div>
        </v-container>
    </div>
</template>

<script>
    import {createNamespacedHelpers} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    const {mapGetters} = createNamespacedHelpers('CamposPersonalizados')

    export default {
        name: 'BotaoNovaMovimentacao',
        props: ['exibirLinhaDoTempo', 'exibirTooltipMovimentacao'],
        data() {
            return {
                tipo: ''
            }
        },
        computed: {
            ...mapGetters(['getTooltipCamposPersonalizados']),
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            }
        },
        methods: {
            async abrirModalNovaMovimentacao() {
                await this.cadastrarMovimentacao()
            },
            cadastrarMovimentacao() {
                this.$emit('cadastrarMovimentacao', this.tipo)
                this.tipo = ''
            }
        }
    }
</script>
