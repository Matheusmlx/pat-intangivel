<template>
    <div class="az-form-content">
        <p class="selecao-titulo">Selecione o tipo de bem intangível:</p>

        <div class="selecao-linha">
            <v-card
                :class="{ primary: value === bem.nome}"
                :key="bem.id"
                @click="selecionarBem(bem)"
                class="selecao-card elevation-0"
                height="100%"
                min-height="170px"
                v-for="bem in bens1">
                <v-icon
                    :color="value === bem.nome ? 'white' : 'gray'"
                    size="50px">
                    {{ bem.icon }}
                </v-icon>
                <v-card-text
                    :class="{ selecionado: value === bem.nome }"
                    class="selecao-card__titulo">
                    {{ bem.label }}
                </v-card-text>
            </v-card>
        </div>

        <div class="selecao-linha">
            <v-card
                :class="{ primary: value === bem.nome}"
                :key="bem.id"
                @click="selecionarBem(bem)"
                class="selecao-card elevation-0"
                height="100%"
                hover
                min-height="170px"
                v-for="bem in bens2">
                <v-icon
                    :color="value === bem.nome ? 'white' : 'gray'"
                    size="50px">
                    {{ bem.icon }}
                </v-icon>
                <v-card-text
                    :class="{ selecionado: value === bem.nome }"
                    class="selecao-card__titulo">
                    {{ bem.label }}
                </v-card-text>
            </v-card>
        </div>
    </div>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        props: {
            value: {
                required: true
            }
        },
        data() {
            return {
                bens1: [
                    {
                        id: 1,
                        label: 'SOFTWARES',
                        nome: 'SOFTWARES',
                        icon: 'mouse',
                        selecionado: false
                    },
                    {
                        id: 2,
                        label: 'DIREITOS AUTORAIS',
                        nome: 'DIREITOS_AUTORAIS',
                        icon: 'far fa-copyright',
                        selecionado: false
                    },
                    {
                        id: 3,
                        label: 'LICENÇAS',
                        nome: 'LICENCAS',
                        icon: 'fas fa-key',
                        selecionado: false
                    }
                ],
                bens2: [
                    {
                        id: 4,
                        label: 'MARCAS',
                        nome: 'MARCAS',
                        icon: 'far fa-registered',
                        selecionado: false
                    },
                    {
                        id: 5,
                        label: 'TÍTULOS DE PUBLICAÇÃO',
                        nome: 'TITULOS_DE_PUBLICACAO',
                        icon: 'fas fa-book-open',
                        selecionado: false
                    },
                    {
                        id: 6,
                        label: 'RECEITAS, FÓRMULAS E PROJETOS',
                        nome: 'RECEITAS_FORMULAS_PROJETOS',
                        icon: 'fas fa-file-alt',
                        selecionado: false
                    }
                ]
            }
        },
        methods: {
            selecionarBem(bem) {
                if (this.verificaPermissaoEdicao) {
                    let tipo = ''
                    tipo = bem.nome
                    this.$emit('setarPatrimonio', tipo)
                }
            }
        },
        computed: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            }
        }
    }
</script>

<style lang="stylus">
    .selecionado, .selecao-card__titulo.selecionado
        color #fff !important

    .selecao
        &-titulo
            font-size 16px
            color #999
            font-weight bold
            text-align center
            margin-bottom 25px !important

        &-linha
            margin 0 auto 30px
            width 80%
            display flex
            justify-content space-between
            align-items center

        &-card
            align-items center
            text-align center
            display flex
            justify-content center
            flex-direction column
            width 28%
            border 1px solid rgba(0, 0, 0, .12) !important

            &__titulo
                text-transform uppercase
                font-size 15px


    .selecao-card:hover
        color #7f7f7f


    @media (min-width 1400px)
        .selecao-linha
            width 70%

    @media (max-width 998px)
        .selecao-linha
            width 90%

    @media (max-width 768px)
        .selecao
            &-linha
                width 100%

            &-card
                width 30%

    @media (max-width 600px)
        .selecao
            &-linha
                flex-direction column

            &-card
                width 100%
                margin 10px 0
</style>
