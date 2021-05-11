<template>
    <div>
        <v-row class="header ml-0 mr-0">
            <v-row class="pl-5">
                <v-col class="white--text text-body-2 " xs="12" sm="1" md="1" lg="1" xl="1">
                    <span class="font-weight-bold">Código</span>
                    <p class="mb-0">{{movimentacao.codigo}}</p>
                </v-col>
                <v-col class="white--text text-body-2 styleColuna" xs="12" sm="1" md="2" lg="3" xl="2">
                    <span class="font-weight-bold">Criado por</span>
                    <p class="mb-0">{{movimentacao.usuarioCadastro}}</p>
                </v-col>
                <v-col class="white--text text-body-2 " xs="12" sm="1" md="2" lg="2" xl="2">
                    <span class="font-weight-bold">Data de Envio</span>
                    <p class="mb-0">{{movimentacao.dataDeEnvio | azDate()}}</p>
                </v-col>
                <v-col class="white--text text-body-2" xs="12" sm="1" md="3" lg="2" xl="2">
                    <span class="font-weight-bold">Data de Finalização</span>
                    <p class="mb-0">{{movimentacao.dataDeFinalizacao | azDate()}}</p>
                </v-col>
                <v-col class="white--text text-body-2" xs="12" sm="1" md="3" lg="3" xl="2">
                    <span class="font-weight-bold">Situação</span>
                    <p class="mb-0">{{movimentacao.situacao | azEnum(situacaoMovimentacao)}}</p>
                </v-col>
            </v-row>

            <div>
                <v-row class="mr-3">
                    <v-col v-if="ehDestinatario && !ehMovimentacaoFinalizada && !ehMovimentacaoRejeitada" class="ml-2">
                        <az-remove-button @remover="rejeitarMovimentacao"
                                          :textButton="'Rejeitar'"
                                          :icon="''"
                                          :text="true"
                                          stylus="styleButton"
                                          :textButtonConfirm="'Sim'"
                        />
                    </v-col>
                    <v-col v-if="verificaPermissaoEdicao && ehDestinatario && !ehMovimentacaoFinalizada && !ehMovimentacaoRejeitada">
                        <v-btn class="styleButton"  color="white" outlined width="85" @click="receberMovimentacao">
                            Receber
                        </v-btn>
                    </v-col>
                    <v-col v-if="!ehMovimentacaoRejeitada">
                        <memorando-button
                            :movimentacao="true"
                            :tooltip="true"
                            :sizeButton="28"
                            :movimentacaoId="movimentacao.id"
                        />
                    </v-col>
                </v-row>
            </div>
        </v-row>
    </div>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'MovimentacaoDetalheCabecalho',
        props: ['movimentacao', 'ehDestinatario'],
        computed: {
            ehMovimentacaoFinalizada() {
                return this.movimentacao.situacao === 'FINALIZADO'
            },
            ehMovimentacaoRejeitada() {
                return this.movimentacao.situacao === 'REJEITADO'
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            }
        },
        methods: {
            receberMovimentacao() {
                this.$emit('receberMovimentacao', this.movimentacao.id)
            },
            rejeitarMovimentacao() {
                this.$emit('rejeitarMovimentacao', this.movimentacao.id)
            }
        }
    }
</script>

<style scoped lang="stylus">
    .styleColuna
        margin-right -120px
    .styleButton
        font-size 10px
</style>
