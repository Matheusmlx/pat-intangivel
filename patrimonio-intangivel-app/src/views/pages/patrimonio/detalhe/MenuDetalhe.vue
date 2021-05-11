<template>
    <div>
        <az-back-button :route="{name: 'TodosPatrimonios'}" text="Voltar para listagem"/>
        <div class="az-form-content">
            <v-tabs v-model="tabs">
                <v-tab v-for="rota in rotas" :key="rota.nome" @click="redireciona(rota.rota)">
                    {{rota.nome}}
                </v-tab>
            </v-tabs>

            <v-tabs-items v-model="tabs">
                <v-tab-item v-for="rota in rotas" :key="rota.nome">
                    <router-view/>
                </v-tab-item>
            </v-tabs-items>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'MenuDetalhe',
        data() {
            return {
                rotas: [{nome: 'DADOS GERAIS', rota: 'FichaPatrimonio'}, {nome: 'MOVIMENTAÇÕES', rota: 'NovaMovimentacao'}],
                tabs: null,
                rotaOrigem: this.$store.state.rota.origem
            }
        },
        mounted() {
            this.buscarTabNaRota()
        },
        methods: {
            redireciona(rota) {
                this.$router.push({name: rota})
            },
            buscarTabNaRota() {
                const tab = this.$router.currentRoute.matched[1].props.default.tabs
                if (tab !== null && typeof tab !== 'undefined') {
                    this.tabs = tab
                }
            }
        }
    }
</script>

<style scoped lang="stylus">
    @media (max-width 500px)
        .az-form-content
            padding 0 !important
</style>
