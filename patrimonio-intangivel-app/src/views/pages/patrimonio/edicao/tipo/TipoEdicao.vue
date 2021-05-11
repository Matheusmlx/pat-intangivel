<template>
    <div>
        <tipo-selecao
            @setarPatrimonio="tratarEventoSelecaoTipoBem"
            v-model="patrimonio.tipo"/>

        <div class="az-actions-form">
            <v-btn @click="tratarEventoVoltar" color="grey" text>Cancelar</v-btn>
            <v-btn
                 v-if="patrimonio.tipo"
                :disabled="!patrimonio.tipo"
                @click="tratarEventoContinuar"
                color="primary"
                depressed>
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
                            depressed
                            @click="tratarEventoContinuar">
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
    import {actionTypes} from '@/core/constants'
    import TipoSelecao from './TipoSelecao'

    export default {
        components: { TipoSelecao },
        data() {
            return {
                patrimonioId: null,
                patrimonio: {},
            }
        },
        mounted() {
            this.$gtag.event('EdicaoTipoPatrimonio', { method: 'Google' })
            this.setPatrimonioId()
            this.buscarPatrimonio()
        },
        methods: {
            setPatrimonioId() {
                this.patrimonioId = this.$route.params.id
            },
            async buscarPatrimonio() {
                await this.$store.dispatch(
                    actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID,
                    this.patrimonioId
                )
                this.patrimonio = this.$store.state.patrimonio.patrimonio
            },
            async tratarEventoSelecaoTipoBem(bem) {
                this.patrimonio.tipo = bem
                this.$store.dispatch(
                    actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO,
                    this.patrimonio
                )
            },
            async tratarEventoContinuar() {
                await this.$router.push({
                    name: 'DadosDeEntradaEdicao',
                    params: { id: this.$route.params.id },
                })
            },
            tratarEventoVoltar() {
                this.$router.push({
                    name: 'TodosPatrimonios',
                })
            },
        },
    }
</script>

<style lang="stylus">
</style>
