<template>
    <v-dialog v-model="exibeModal" width="670"
              v-on:keydown.esc="clicarForaModal"
              v-on:click:outside="clicarForaModal">
        <v-card>
            <v-card-title
                class="body-2 font-weight-bold primary white--text"
                primary-title>
                Nova {{ tipo | azEnum(tipoMovimentacao) }}
                <v-spacer/>
                <v-btn icon small dark @click="fecharModalMovimentacao">
                    <v-icon>fas fa-times</v-icon>
                </v-btn>
            </v-card-title>
            <v-col class="d-inline-flex justify-end align-center pa-0 pr-3 pt-3">
                <az-remove-button @remover="tratarEventoDeletarMovimentacao"
                                  class="botao-excluir"
                                  v-if="this.ehMovimentacaoEmElaboracao"
                />
            </v-col>
            <v-form class="az-form-content form pt-0" lazy-validation ref="form">
                <v-autocomplete
                    :disabled="!verificaPermissaoEdicao"
                    v-model="movimentacaoModel.orgaoDestino"
                    :items="orgaos"
                    item-text="descricao"
                    item-value="id"
                    v-validate="'required'"
                    :error-messages="errors.collect('Órgão Destino')"
                    name="Órgão Destino"
                    no-data-text="Não há órgãos com este nome"
                    placeholder="Selecione"
                    return-object
                    @change="atualizarMovimentacao">
                    <template v-slot:label>
                        {{ getNomeCamposPersonalizados('orgaoDestino', 'MOVIMENTACAO') }}
                        <span class="ml-1 red--text">*</span>
                    </template>
                </v-autocomplete>
                <v-textarea
                    :disabled="!verificaPermissaoEdicao"
                    v-model="movimentacaoModel.motivo"
                    counter="500"
                    auto-grow
                    class="mt-5"
                    @change="atualizarMovimentacao"
                    v-validate="'required|max:500'"
                    data-vv-as="motivo"
                    name="motivo"
                    :maxlength="500"
                    type="text"
                    placeholder="Movimentação necessária."
                    :error-messages="errors.collect('motivo')">
                    <template v-slot:label>
                        {{ getNomeCamposPersonalizados('motivoObs', 'MOVIMENTACAO') }}
                        <span class="ml-1 red--text">*</span>
                    </template>
                </v-textarea>
            </v-form>
            <v-card-text class="pa-0 mb-9 body-1 text-md-center">
                <v-flex>
                    O patrimônio nº <span class="font-weight-bold">{{ patrimonio.numero }}</span> está sendo
                    <span class="font-weight-bold">{{ formatarTipoMovimentacao }}</span><br/> pelo valor líquido
                    <span class="font-weight-bold">{{ returnToLocaleStringMoeda(patrimonio.valorLiquido) }}</span>
                </v-flex>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
                <v-flex class="row pa-3 ma-0">
                    <v-btn color="grey" text outlined width="150" class="text-body-2"
                           @click="fecharModalMovimentacao">
                        Cancelar
                    </v-btn>
                    <v-spacer/>
                    <memorando-button
                        :class="podeGerarMemorando ? 'btn-disabled' : ''"
                        :disabled="podeGerarMemorando"
                        :movimentacao="true"
                        :tooltip="false"
                        :movimentacaoId="movimentacao.id"
                    />
                    <v-spacer/>
                    <v-btn v-if="validarCampos" @click="enviarMovimentacao" depressed width="150"
                           class="text-body-2" color="primary" :disabled="!verificaPermissaoEdicao">
                        Enviar
                    </v-btn>
                    <v-tooltip v-else nudge-top="0" top transition="scale-transition" origin="center center"
                               max-width="300">
                        <template v-slot:activator="{ on }">
                            <div v-on="on">
                                <v-btn v-on="on" disabled depressed width="150" class="text-body-2"
                                       color="primary">
                                    Enviar
                                </v-btn>
                            </div>
                        </template>
                        Preencha todos os campos obrigatórios para enviar uma movimentação.
                    </v-tooltip>
                </v-flex>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {createNamespacedHelpers, mapState} from 'vuex'
    import _ from 'lodash'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    const {mapGetters} = createNamespacedHelpers('CamposPersonalizados')

    export default {
        name: 'ModalMovimentacao',
        $_veeValidate: {
            validator: 'new',
        },
        props: ['movimentacao', 'tipo', 'orgaos', 'situacao', 'estaDeletando'],
        data() {
            return {
                exibeModal: true,
                movimentacaoModel: _.cloneDeep(this.movimentacao)
            }
        },
        computed: {
            ...mapGetters(['getNomeCamposPersonalizados']),
            ...mapState({patrimonio: state => state.patrimonio.patrimonio}),
            formatarTipoMovimentacao() {
                if (this.tipo === 'TRANSFERENCIA_DEFINITIVA') {
                    return 'transferido definitivamente'
                }
                return 'doado'
            },
            validarCampos() {
                return this.movimentacao.orgaoDestino && this.movimentacao.motivo
            },
            podeGerarMemorando() {
                return !(this.movimentacao.orgaoDestino || this.movimentacao.motivo)
            },
            ehMovimentacaoEmElaboracao() {
                return this.situacao === 'EM_ELABORACAO'
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            }
        },
        methods: {
            clicarForaModal() {
                if (!this.movimentacao.orgaoDestino && !this.movimentacao.motivo) {
                    this.tratarEventoDeletarMovimentacao()
                } else {
                    if (this.movimentacao.motivo) {
                        this.atualizarMovimentacao()
                    }
                    this.fecharModalMovimentacao()
                }
            },
            atualizarMovimentacao() {
                this.$emit('atualizarMovimentacao', this.movimentacaoModel)
            },
            fecharModalMovimentacao() {
                if (!this.movimentacao.orgaoDestino && !this.movimentacao.motivo) {
                    this.tratarEventoDeletarMovimentacao()
                } else {
                    this.$emit('fecharModalMovimentacao')
                }
            },
            returnToLocaleStringMoeda(dado) {
                if (typeof dado != 'undefined' && dado != null) {
                    return dado.toLocaleString('pt-br', {style: 'currency', currency: 'BRL'})
                }
                return '----'
            },
            enviarMovimentacao() {
                this.$emit('enviarMovimentacao', this.movimentacaoModel)
            },
            tratarEventoDeletarMovimentacao() {
                this.estaDeletando = true
                this.$emit('tratarEventoDeletarMovimentacao')
            }
        },
    }
</script>
