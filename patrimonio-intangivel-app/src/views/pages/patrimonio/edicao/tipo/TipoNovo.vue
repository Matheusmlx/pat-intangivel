<template>
    <div>
        <tipo-selecao
            @setarPatrimonio="tratarEventoSelecaoTipoBem"
            v-model="patrimonio.tipo"/>

        <div class="az-actions-form">
            <v-btn @click="tratarEventoVoltar" text color="grey">Cancelar</v-btn>
            <v-btn
                v-if="patrimonio.tipo"
                color="primary"
                depressed
                @click="tratarEventoContinuar">
                Continuar
                <v-icon size="15" class="ml-1">fas fa-arrow-right</v-icon>
            </v-btn>
            <v-tooltip
                v-else
                top
                nudge-top="30"
                max-width="200">
                <template v-slot:activator="{ on }">
                    <div v-on="on" :class="{'btn-disabled': !patrimonio.tipo}">
                        <v-btn
                            :disabled="!patrimonio.tipo"
                            color="primary"
                            depressed>
                            Continuar
                            <v-icon size="15" class="ml-1">fas fa-arrow-right</v-icon>
                        </v-btn>
                    </div>
                </template>
               É necessário selecionar um tipo de bem intangível para continuar.
            </v-tooltip>
        </div>
    </div>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import TipoSelecao from './TipoSelecao'

    export default {
        components: { TipoSelecao },
        data() {
            return {
                patrimonio: {
                    tipo: null
                },
                bemSelecionado: null
            }
        },
        mounted() {
            this.$gtag.event('CadastroTipoPatrimonio', { method: 'Google' })
        },
        methods: {
            tratarEventoSelecaoTipoBem(bem) {

                this.patrimonio.tipo = bem
                this.$store.commit(mutationTypes.PATRIMONIO.SET_PATRIMONIO, this.patrimonio)
            },
            async tratarEventoContinuar() {
                this.patrimonio.tipo = await this.$store.state.patrimonio.patrimonio.tipo
                const retorno = await this.$store.dispatch(actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO, this.patrimonio)
                await this.$router.push({
                    name: 'DadosDeEntradaEdicao',
                    params: { id: retorno.id },
                })
            },
            tratarEventoVoltar() {
                this.$router.push({
                    name: 'TodosPatrimonios',
                })
            }
        }
    }
</script>

<style lang="stylus">
</style>
