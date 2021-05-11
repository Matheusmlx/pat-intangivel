<template>
    <v-menu
        v-if="verificaPermissaoEdicao"
        offset-y
        top
        left
        nudge-top="5"
        content-class="elevation-0 az-bnt-remove">
        <template v-slot:activator="{on}">
            <v-btn :icon="!textButton" @click="value = !value" :class="stylus" v-on="on" :text="text">
                <v-icon :color="color" :size="size" @mouseleave="estadoBotao" @mouseover="estadoBotao">{{icon}}</v-icon>
                {{textButton}}
            </v-btn>
        </template>
        <span class="az-bnt-remove-content">
            {{message}}
            <a @click="remove" class="remove ml-1">{{textButtonConfirm}}</a>
            <a class="cancel ml-1">{{textButtonCancel}}</a>
        </span>
    </v-menu>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'botaoExcluir',
        props: {
            text: {
                type: Boolean,
                default: false
            },
            size: {
                type: String,
                default: '15'
            },
            stylus:{
                type:String
            },
            colorPrimary: {
                type: String,
                default: 'grey'
            },
            colorSecondary: {
                type: String,
                default: 'red'
            },
            icon: {
                type: String,
                default: 'far fa-trash-alt'
            },
            textButton: {
                type: String,
                default: ''
            },
            message: {
                type: String,
                default: 'Você tem certeza?'
            },
            textButtonConfirm: {
                type: String,
                default: 'Remover'
            },
            textButtonCancel: {
                type: String,
                default: 'Cancelar'
            }
        },
        data() {
            return {
                color : this.colorPrimary
            }
        },
        methods: {
            estadoBotao() {
                if(this.color === this.colorPrimary){
                    this.color = this.colorSecondary
                    return
                }
                this.color = this.colorPrimary
            },
            remove() {
                this.$emit('remover')
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
    .az-bnt-remove
        min-width 240px!important
        max-width max-content
        overflow-y hidden

        &-content
            position: relative
            border-radius: .4em
            pointer-events: initial
            padding 5px
            background-color black
            font-size small
            color white

        .remove
            font-size small
            text-transform capitalize
            color red

        .cancel
            font-size small
            text-transform capitalize
            color grey

    .styleButton
        font-size 10px
</style>
