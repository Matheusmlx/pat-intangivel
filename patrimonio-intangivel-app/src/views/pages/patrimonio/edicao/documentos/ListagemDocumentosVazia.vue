<template>
    <v-row
        align="center"
        justify="center"
        class="patrimonio-container-conteudo">
        <div class="text-center">
            <p class="text-info">Nenhum documento adicionado, clique para adicionar</p>
            <v-btn :disabled="!verificaPermissaoEdicao" @click="novoDocumento" color="primary" depressed>Novo</v-btn>
        </div>
    </v-row>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        methods: {
            novoDocumento() {
                this.$emit('novoDocumento')
            }
        },
        computed: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            },
        }
    }
</script>

<style lang="stylus" scoped>
    .text-info
        color #777 !important
        font-size 14px
</style>
