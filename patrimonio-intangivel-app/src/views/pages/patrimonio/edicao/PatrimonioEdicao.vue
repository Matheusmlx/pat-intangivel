<template>
    <div>
        <az-back-button :route="{ name: 'TodosPatrimonios' }" text="Voltar para listagem"/>

        <az-container class="patrimonio-container">
            <v-stepper v-model="passoAtual.numero">
                <v-stepper-header class="elevation-0 barra-passo">
                    <v-spacer/>
                    <v-stepper-step
                        :complete="passos[0].numero < passoAtual.numero"
                        :disabled="passos[0].desabilitado"
                        :editable="!passos[0].desabilitado"
                        :key="`${passos[0].numero}-header`"
                        :step="passos[0].numero"
                        @click="tratarEventoClick(passos[0])"
                        edit-icon="fas fa-check">
                        {{ passos[0].titulo }}
                    </v-stepper-step>

                    <v-divider/>

                    <v-tooltip
                        nudge-left="10"
                        nudge-top="-22"
                        max-width="200"
                        top>
                        <template v-slot:activator="{ on }">
                            <div v-on="on" v-show="passos[1].desabilitado">
                                <v-stepper-step
                                    :complete="passos[1].numero < passoAtual.numero"
                                    :disabled="passos[1].desabilitado"
                                    :editable="!passos[1].desabilitado"
                                    :key="`${passos[1].numero}-header`"
                                    :step="passos[1].numero"
                                    @click="tratarEventoClick(passos[1])"
                                    edit-icon="fas fa-check"
                                >{{ passos[1].titulo }}
                                </v-stepper-step>
                            </div>
                            <v-stepper-step
                                :complete="passos[1].numero < passoAtual.numero"
                                :disabled="passos[1].desabilitado"
                                :editable="!passos[1].desabilitado"
                                :key="`${passos[1].numero}-header`"
                                :step="passos[1].numero"
                                @click="tratarEventoClick(passos[1])"
                                edit-icon="fas fa-check"
                                v-show="!passos[1].desabilitado"
                            >{{ passos[1].titulo }}
                            </v-stepper-step>
                        </template>
                        É necessário selecionar um tipo de bem intangível para continuar.
                    </v-tooltip>

                    <v-divider/>

                    <v-tooltip
                        nudge-left="10"
                        nudge-top="-20"
                        max-width="200"
                        top>
                        <template v-slot:activator="{ on }">
                            <div v-on="on" v-show="passos[2].desabilitado && !passos[1].desabilitado">
                                <v-stepper-step
                                    :complete="passos[2].numero < passoAtual.numero"
                                    :disabled="passos[2].desabilitado"
                                    :editable="!passos[2].desabilitado"
                                    :key="`${passos[2].numero}-header`"
                                    :step="passos[2].numero"
                                    @click="tratarEventoClick(passos[2])"
                                    edit-icon="fas fa-check"
                                >{{ passos[2].titulo }}
                                </v-stepper-step>
                            </div>
                            <v-stepper-step
                                :complete="passos[2].numero < passoAtual.numero"
                                :disabled="passos[2].desabilitado"
                                :editable="!passos[2].desabilitado"
                                :key="`${passos[2].numero}-header`"
                                :step="passos[2].numero"
                                @click="tratarEventoClick(passos[2])"
                                edit-icon="fas fa-check"
                                v-show="!passos[2].desabilitado || passos[1].desabilitado"
                            >{{ passos[2].titulo }}
                            </v-stepper-step>
                        </template>
                        É necessário que todos os campos obrigatórios sejam preenchidos para continuar.
                    </v-tooltip>

                    <v-spacer/>

                    <v-col  md="1" sm="2" xs="4" class="d-flex justify-end align-center">
                        <memorando-button class="mr-3" v-if="passoAtual.numero !== 1" :text="false" :dark="false" outlined tooltip :patrimonioId="this.patrimonioId"/>

                        <az-remove-button @remover="tratarEventoDeletarPatrimonio"
                                       class="botao-excluir"
                                       v-if="patrimonioId && verificaPermissaoEdicao()"/>
                    </v-col>
                </v-stepper-header>

                <v-divider/>

                <v-stepper-items>
                    <v-stepper-content
                        :key="`${passo.numero}-content`"
                        :step="passo.numero"
                        v-for="passo in passos">
                    </v-stepper-content>
                    <router-view @desabilitaPasso3="desabilitaPasso3"
                                 @habilitaPasso3="habilitaPasso3"/>
                </v-stepper-items>
            </v-stepper>
        </az-container>
    </div>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import MemorandoButton from '@/views/components/MemorandoButton'

    export default {
        name: 'PatrimonioEdicao',
        components: {MemorandoButton},
        data() {
            return {
                passos: [
                    {
                        titulo: 'Bem Intangível',
                        rotaPadrao: 'TipoVisualizacao',
                        rotaNovo: 'TipoNovo',
                        rotaEdicao: 'TipoEdicao',
                        desabilitado: false,
                        numero: 1,
                    },
                    {
                        titulo: 'Dados da Entrada',
                        rotaPadrao: 'DadosDeEntradaEdicao',
                        rotaEdicao: 'DadosDeEntradaEdicao',
                        desabilitado: true,
                        numero: 2,
                    },
                    {
                        titulo: 'Documentos',
                        rotaPadrao: 'DocumentosVisualizacao',
                        rotaEdicao: 'DocumentosEdicao',
                        desabilitado: true,
                        numero: 3,
                    },
                ],
                passoAtual: {},
                patrimonioId: null,
                rotaOrigem: 'TodosPatrimonios',
                valueToolTip: false,
                patrimonioDeletado: false
            }
        },
        watch: {
            $route() {
                this.setPatrimonioId()
                this.setPassoAtual(this.$route)
            },
        },
        async mounted() {
            this.$store.commit(mutationTypes.COMUM.SET_RETRAIR_MENU)
            this.setPatrimonioId()
            this.setPassoAtual(this.$route)
            try {
                await this.buscaPatrimonio()
            } catch (e) {
                await this.$router.push({ name: this.rotaOrigem })
            }
        },
        methods: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            },
            async buscaPatrimonio() {
                if(this.patrimonioId) {
                    await this.$store.dispatch(actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID, this.patrimonioId)
                }
            },
            habilitaPasso3() {
                this.passos[2].desabilitado = false
            },
            desabilitaPasso3() {
                this.passos[2].desabilitado = true
            },
            async tratarEventoDeletarPatrimonio() {
                await this.$store.dispatch(
                    actionTypes.PATRIMONIO.DELETAR_PATRIMONIO,
                    this.patrimonioId
                )
                this.patrimonioDeletado = true
                this.mostrarNotificacaoSucessoDefault()
                await this.$router.push({ name: this.rotaOrigem })
            },
            tratarEventoClick(item) {
                if (item.desabilitado === false) {
                    if (this.patrimonioId) {
                        this.$router.push({
                            name: item.rotaEdicao,
                        })
                    } else {
                        this.$router.push({
                            name: item.rotaNovo,
                        })
                    }
                }
            },
            setPatrimonioId() {
                this.patrimonioId = this.$route.params.id
            },
            setPassoAtual(route) {
                const encontrado = this.passos.filter(
                    passo =>
                        passo.rotaPadrao === route.name ||
                        passo.rotaEdicao === route.name ||
                        passo.rotaNovo === route.name
                )
                this.passoAtual = encontrado[0]
                this.contralaAcessoAosPassosEmCadaRota()
            },

            contralaAcessoAosPassosEmCadaRota() {
                if (this.$route.name === 'DadosDeEntradaEdicao') {
                    this.passos[1].desabilitado = false
                    this.passos[2].desabilitado = true
                }
                if (this.$route.name === 'TipoEdicao') {
                    this.passos[1].desabilitado = false
                }
                if (this.$route.name === 'DocumentosEdicao') {
                    this.passos[0].desabilitado = false
                    this.passos[1].desabilitado = false
                    this.passos[2].desabilitado = false
                }
            },
        },
        async destroyed() {
            if(this.patrimonioId && !this.patrimonioDeletado) {
                await this.$store.dispatch(actionTypes.PATRIMONIO.DELETAR_PATRIMONIO_NAO_ALTERADO, this.patrimonioId)
            }
            this.$store.commit(mutationTypes.COMUM.SET_EXPANDIR_MENU)
        }
    }
</script>

<style lang="stylus">
    .az-back-button
        .v-btn
            letter-spacing: 0px !important
    .patrimonio-container
        .v-stepper
            box-shadow none

            &__content
                padding 0 15% 0 60%

            &__step__step .v-icon.v-icon
                font-size .9rem

        .container
            background-color #fff

        .az-actions-form
            border-top solid thin rgba(0,0,0,.12)
            padding-top 20px

        &-conteudo
            min-height 350px

    .botao-excluir
        padding 0 15% 2% 60%

    .barra-passo
        padding 0 0 0 5%
</style>
