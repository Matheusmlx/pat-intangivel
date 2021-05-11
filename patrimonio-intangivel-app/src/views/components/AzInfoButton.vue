<template>
    <span class="az-info">
        <v-menu
            max-width="500"
            open-on-hover
            offset-y
            top
            nudge-top="5"
            content-class="elevation-0">
            <template v-slot:activator="{on}">
                <v-icon class="ml-1 mb-1" size="15" v-on="on">help_outline</v-icon>
            </template>
            <div class="az-info-content">
                <slot/>
            </div>
        </v-menu>
    </span>
</template>

<script>
    export default {
        props: {
            isDisabled: Boolean
        }
    }
</script>

<style lang="stylus" scoped>
    .az-info
        .v-menu__content
            background red
        &-content
            padding 10px
            font-size 12px
            opacity 1 !important
            background #333333 !important
            text-align justify !important
            line-height 1.4
            color white
</style>
