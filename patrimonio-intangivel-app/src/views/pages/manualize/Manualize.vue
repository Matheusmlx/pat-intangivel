<template>
    <iframe scrolling="no" :src="manualizeURL"
            style="border: 0;margin-left: 0;height: 1100px;margin-top: 0px;width: 100%;"></iframe>
</template>

<script>
    import {mutationTypes} from '@/core/constants'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'Manualize',
        mounted() {
            this.$store.commit(mutationTypes.COMUM.SET_RETRAIR_MENU)
        },
        methods: {
            verificaPermissaoRetroativa() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Retroativo'])
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            },
            verificaPermissaoConfiguracao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['ContaContabil.Normal'])
            },
        },
        computed:{
            manualizeURL() {
                if (this.verificaPermissaoRetroativa() && this.verificaPermissaoEdicao() && !this.verificaPermissaoConfiguracao()) {
                    return 'https://manualize.com.br/categoria_patrimonio_intangivel/retroativo'
                }else if(!this.verificaPermissaoRetroativa() && this.verificaPermissaoEdicao()) {
                    return 'https://manualize.com.br/categoria_patrimonio_intangivel/servidor/'
                }else if(this.verificaPermissaoRetroativa()){
                    return 'https://manualize.com.br/categoria_patrimonio_intangivel/administrador/'
                }else{
                    return 'https://manualize.com.br/categoria_patrimonio_intangivel/consultor/'
                }
            },
        }
    }
</script>

<style scoped>

</style>
