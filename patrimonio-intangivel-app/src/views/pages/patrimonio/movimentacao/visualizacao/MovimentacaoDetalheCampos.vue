<template>
    <div>
        <v-row class="white pl-3 pr-3 ml-0 mr-0">
            <v-col  md="4" sm="6" xs="12">
                <az-text-view
                    label="Tipo"
                    :text="movimentacao.tipo | azEnum(tipoMovimentacao)"/>
            </v-col>
            <v-col  md="4" sm="6" xs="12">
                <az-text-view
                    label="Órgão de Origem"
                    :text="movimentacao.orgaoOrigem.descricao"/>
            </v-col>
            <v-col  md="4" sm="6" xs="12">
                <az-text-view
                    label="Setor Origem"
                    :text="patrimonio.setor || '---'"/>
            </v-col>
            <v-col  md="4" sm="6" xs="12">
                <az-text-view
                    label="Órgão de Destino"
                    :text="movimentacao.orgaoDestino.descricao"/>
            </v-col>
            <v-col  md="4" sm="6" xs="12">
                <az-text-view-edit
                    v-model="movimentacao.numeroNL"
                    name="Número NL"
                    :label="getNomeCamposPersonalizados('numeroNL','MOVIMENTACAO')"
                    :validate="getObrigatorioCamposPersonalizados('numeroNL', 'MOVIMENTACAO') ? 'required|max:12|min:12' : ''"
                    placeholder="_ _ _ _NL_ _ _ _ _ _"
                    mask="####NL######"
                    maxlength="12"
                    :counter="12"
                    :disabled="!verificaPermissaoEdicao"
                    @keydown.enter="tratarEventoSalvar"
                    @input="tratarEventoSalvar"/>
            </v-col>
            <v-col  md="4" sm="6" xs="12">
                <az-text-view-edit-date
                    v-model="movimentacao.dataNL"
                    :label="getNomeCamposPersonalizados('dataNL','MOVIMENTACAO')"
                    name="Data NL"
                    :disabled="!verificaPermissaoEdicao"
                    :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                    :required="getObrigatorioCamposPersonalizados('dataNL', 'MOVIMENTACAO')"
                    @input="tratarEventoSalvar"
                />
            </v-col>
            <v-col v-if="validarSeMovimentacaoFinalizada || validarSeMovimentacaoRejeitada" md="4" sm="4" xs="4">
                <az-text-view
                    :label="setarLabelDeAcordoComASituacao()"
                    :text="movimentacao.usuarioFinalizacao"
                />
            </v-col>
            <v-col md="4" sm="4" xs="4">
                <az-text-view
                    :label="getNomeCamposPersonalizados('motivoObs','MOVIMENTACAO')"
                    :text="movimentacao.motivo"
                />
            </v-col>
        </v-row>
    </div>
</template>

<script>
    import {createNamespacedHelpers} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    const {mapGetters} = createNamespacedHelpers('CamposPersonalizados')

    export default {
        name: 'MovimentacaoDetalheCampos',
        props: ['movimentacao', 'patrimonio'],
        computed: {
            ...mapGetters(['getNomeCamposPersonalizados', 'getObrigatorioCamposPersonalizados']),
            validarSeMovimentacaoFinalizada() {
                return this.movimentacao.situacao === 'FINALIZADO'
            },
            validarSeMovimentacaoRejeitada() {
                return this.movimentacao.situacao === 'REJEITADO'
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            }
        },
        methods: {
            tratarEventoSalvar() {
                this.$emit('atualizar',this.movimentacao)
            },
            setarLabelDeAcordoComASituacao(){
                return this.validarSeMovimentacaoFinalizada ? 'Recebido por' : 'Rejeitado por '
            }
        }
    }
</script>
