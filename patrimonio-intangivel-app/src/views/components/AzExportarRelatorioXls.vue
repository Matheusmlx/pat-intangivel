<template>
    <div>
        <v-tooltip nudge-top="0" top transition="scale-transition" origin="center center" max-width="300">
           <template v-slot:activator="{ on }">
                <v-btn class="styleButton" small v-on="on" @click="emitirEventoExportar">{{mensagem}}</v-btn>
           </template>
           Clique para exportar todos os dados da lista de registro
        </v-tooltip>
    </div>
</template>

<script>
    export default {
        props: {
            mensagem: {
                type: String,
            },
        },
        methods: {
            emitirEventoExportar() {
                this.$emit('exportarXls')
            },
        },

    }
</script>

<style lang="stylus" scoped>
 .styleButton
    margin-left: 20px;
    margin-top: -20px;
</style>
