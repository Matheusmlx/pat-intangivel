<template>
    <v-dialog
        v-model="dialog"
        width="500">
        <template v-slot:activator="{ on }">
            <v-tooltip nudge-top="0" top transition="scale-transition" origin="center center">
                <template v-slot:activator="{ on }">
                    <v-btn @click="dialog = true" dark text v-on="on" class="buttonCancelarAtivacao mr-12">
                        <span class="text-capitalize">Cancelar ativação</span>
                    </v-btn>
                </template>
                Cancelamento permitido dentro do mês corrente.
            </v-tooltip>
        </template>

        <v-card>
            <v-card-title
                class="body-2 font-weight-bold primary white--text"
                primary-title>
                Cancelar Ativação de Software
                <v-spacer/>
                <v-btn
                    @click="dialog = false"
                    icon
                    small
                        dark>
                    <v-icon>fas fa-times</v-icon>
                </v-btn>
            </v-card-title>

            <v-card-text class="title font-weight-bold d-flex justify-center align-center pa-11 my-2">
                Tem certeza que deseja cancelar ?
            </v-card-text>

            <v-divider></v-divider>

            <v-card-actions>
                <v-spacer/>
                    <v-col cols="12" md="12" class=" row justify-center align-center">
                        <v-btn
                            depressed
                            @click="dialog = false"
                            class=" lighten-2 buttonAction"
                            color="primary">
                            Não
                        </v-btn>
                        <v-col md="1"> <v-spacer/></v-col>
                        <v-btn
                            outlined
                            depressed
                            class="buttonAction"
                            @click="dialog = false, $emit('cancelarAtivacao')"
                            color="grey">
                            Sim
                        </v-btn>
                    </v-col>
                <v-spacer/>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    export default {
        name: 'ModalCancelarAtivacao',
        data() {
            return {
                dialog: false
            }
        },
    }
</script>

<style scoped>

    .buttonAction {
        min-width: 110px !important;
    }
    .buttonCancelarAtivacao{
        opacity: 0.3;
    }

</style>
