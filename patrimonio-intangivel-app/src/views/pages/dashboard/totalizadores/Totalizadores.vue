<template>
        <v-layout wrap>
            <v-flex sm4 lg4 py-6>
                <v-card class="mx-auto mr-3 ml-6  mr-sm-1 ml-sm-4"
                        min-height="110"
                        flat
                        @click="filtrarPorTodos">
                    <div class="barra"/>
                    <v-list-item three-line class="mt-1">
                        <v-list-item-content>
                            <v-list-item-title class="text--darken-2 text-lg-h4 font-weight-bold grey--text headline mb-1">
                                {{ totalizadores.totalDeRegistros }}
                            </v-list-item-title>
                            <v-list-item-subtitle class="text--darken-2 font-weight-bold grey--text text-sm-body-2 mb-1">
                                TOTAL DE REGISTROS
                            </v-list-item-subtitle>
                        </v-list-item-content>
                        <v-icon size="35" color="#BDBDBD">
                            add_circle_outline
                        </v-icon>
                    </v-list-item>
                </v-card>
            </v-flex>
            <v-flex lg4 sm4 py-6>
                <v-card class="mx-auto mr-3 ml-3 mr-sm-2 ml-sm-2"
                        min-height="110"
                        flat
                        @click="filtrarPorEmElaboracao">
                    <div class="barra"/>
                    <v-list-item three-line class="mt-1">
                        <v-list-item-content>
                            <v-list-item-title class="text--darken-2 text-lg-h4 font-weight-bold grey--text headline mb-1">
                                {{ totalizadores.emElaboracao }}
                            </v-list-item-title>
                            <v-list-item-subtitle class="text--darken-2 font-weight-bold grey--text text-sm-body-2 mb-1">
                                EM ELABORAÇÃO
                            </v-list-item-subtitle>
                        </v-list-item-content>
                        <v-icon size="35" color="#BDBDBD">
                            far fa-edit
                        </v-icon>
                    </v-list-item>
                </v-card>
            </v-flex>
            <v-flex lg4 sm4 py-6>
                <v-card class="mx-auto mr-6 ml-3 mr-sm-4 ml-sm-2"
                        min-height="110"
                        flat
                        @click="filtrarPorAtivos">
                    <div class="barra"/>
                    <v-list-item three-line class="mt-1">
                        <v-list-item-content>
                            <v-list-item-title class="text--darken-2 text-lg-h4 font-weight-bold grey--text headline mb-1">
                               {{ totalizadores.ativos }}
                            </v-list-item-title>
                            <v-list-item-subtitle class="text--darken-2 text-sm-body-2 font-weight-bold grey--text mb-1">
                                ATIVOS
                            </v-list-item-subtitle>
                        </v-list-item-content>
                        <v-icon size="35" color="#BDBDBD">
                            far fa-check-circle
                        </v-icon>
                    </v-list-item>
                </v-card>
            </v-flex>
        </v-layout>
</template>

<script>
    export default {
        name: 'Totalizadores',
        props: ['totalizadores'],
        methods: {
            filtrarPorTodos() {
                this.$emit('filtrarPatrimonioPorTotalizadores')
            },
            filtrarPorEmElaboracao() {
                this.$emit('filtrarPatrimonioPorTotalizadores', 'Elaboração')
            },
            filtrarPorAtivos() {
                this.$emit('filtrarPatrimonioPorTotalizadores', 'Ativo')
            }
        }
    }
</script>

<style scoped lang="stylus">
    .barra
        background #487b9c
        color #487b9c
        height 5px
</style>
