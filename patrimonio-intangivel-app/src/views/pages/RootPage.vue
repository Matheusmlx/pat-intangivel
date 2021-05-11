<template>
    <az-template-default id="template">
        <router-view v-if="retornaPermissao"/>

        <chat :url='$store.state.parametros.codigoChat' v-if="retornaPermissao"/>

        <acesso-negado v-if="!retornaPermissao"/>
    </az-template-default>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import {mutationTypes} from '@/core/constants'
    import AcessoNegado from './commons/acesso-negado/AcessoNegado'

    export default {
        components: {AcessoNegado},
        mounted() {
            this.redirecionaSeAcessoNegado()
        },
        methods: {
            redirecionaSeAcessoNegado() {
                if (!this.retornaPermissao) {
                    this.$store.commit(mutationTypes.COMUM.SET_RETRAIR_MENU)
                    this.$router.push({name: 'AcessoNegado'})
                }
            }
        },
        computed: {
            retornaPermissao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Consulta'])
            }
        }
    }
</script>

<style lang="stylus">
    html, body, .application
        font-family 'Open Sans', sans-serif

    .notification i.white--text
        color rgb(119, 119, 119) !important

    .az-table-list tbody tr
        border-bottom 1px solid #e9e9e9

        td
            font-weight 400
            height 40px
            color #777
            font-size 13px

    .az-menu .v-list-item__action i
        font-size 15px

    .az-container .az-table-list thead tr:first-child th
        white-space normal

    .table-actions
        text-align center !important

        a
            color #777

        i
            font-size 16px

    .v-tooltip__content
        padding 10px
        font-size 12px
        opacity 1 !important
        background #333333 !important
        text-align justify !important
        line-height 1.4

    .v-tooltip__content.menuable__content__active
        max-width 500px

    .az-search .input-search
        background-color #fff !important

    .az-logo .main
        width 125px
        transition all .2s linear

    .symbol
        width 45px !important
        transition all .2s linear

    .az-about__version
        font-size 9px

    .az-checkbox
        .v-icon.material-icons.theme--light
            margin-left 1.8px
            margin-top 1.8px

        .v-input--selection-controls__ripple
            margin-top 0
            margin-left 0

    .az-form-content
        margin-top 0

        .v-text-field
            .v-label
                font-size 14px !important
                top -5px

        .az-date
            .v-text-field
                .v-label
                    top 0

        .v-input--radio-group__input
            .v-label
                font-size 14px !important
                top -5px

        .col
            padding 8px 12px !important

        .v-textarea textarea
            max-height 100px
            overflow-y auto

    .area-conteudo .v-stepper
        box-shadow none

        &__header
            align-items center
            flex-wrap nowrap
            position relative
            box-shadow none

        &__step
            text-align center
            flex-direction column

        &__step__step
            height 40px
            min-width 40px
            width 40px
            font-size 16px
            margin 0 0 5px

        &__step--active .v-stepper__label, &__step--complete .v-stepper__label
            color var(--v-primary-base) !important
            font-weight 600
            text-shadow none !important

    .az-table-list .v-text-field > .v-input__control > .v-input__slot:before
        border-color transparent


    .simple-table
        th, td
            border 1px solid #e7e7e7
            border-left none

        td
            border-top none

        th:first-child, td:first-child
            border-left 1px solid #e7e7e7

        th
            height 38px

        tr:hover
            background unset !important

    @media (max-width 500px)
        .area-conteudo__linha
            .v-stepper__header
                height 102px

            .v-stepper__step
                padding 0

        .az-container
            margin 0

        .az-pagination .az-select-pagination
            height 50px

        .az-back-button
            margin-bottom 25px

        .az-form-content, .az-form
            padding 10px

        .v-content
            padding-bottom 0

    .btn-disabled
        cursor not-allowed

    .erro-inicializacao
        width 100%
        padding 0
        text-align center
        color #555
        height 80vh
        display flex
        justify-content center
        align-items center

        i
            font-size 36px

        p
            font-size 18px
            margin 10px 0 0
            font-weight 600

    .az-template-default .toolbar
        -webkit-box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.2) !important
        box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.2) !important
        background-color: #fff !important
</style>
