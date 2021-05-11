<template>
    <v-row class="white pl-3 pr-3 ml-0 mr-0 pb-5">
        <v-col cols="12">
            <v-card class="documentos">
                <v-expansion-panels v-model="expandir" flat multiple>
                    <v-expansion-panel>
                        <v-expansion-panel-header class="documentos-panel-header">
                            <h4 class="session-title">Patrimônios (1)</h4>
                        </v-expansion-panel-header>
                        <v-expansion-panel-content class="mt-4">
                            <v-col cols="12" md="12" class="mt-0 pt-0">
                                <v-simple-table class="simple-table">
                                    <thead>
                                    <tr>
                                        <th class="font-weight-bold" width="200">
                                            N° Patrimônio
                                        </th>
                                        <th class="font-weight-bold">
                                            Nome Patrimônio
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody id="listagemTabela">
                                    <tr>
                                        <td>
                                            <az-text-view
                                                class="text-left mt-4"
                                                :text="patrimonio.numero"/>
                                        </td>
                                        <td>
                                            <az-text-view
                                                class="text-left mt-4"
                                                :text="patrimonio.nome"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </v-simple-table>
                            </v-col>
                        </v-expansion-panel-content>
                    </v-expansion-panel>
                </v-expansion-panels>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
    export default {
        name: 'MovimentacaoDetalhePatrimonios',
        props: ['patrimonio'],
        data() {
            return {
                expandir: [0]
            }
        }
    }
</script>

<style scoped>

</style>
