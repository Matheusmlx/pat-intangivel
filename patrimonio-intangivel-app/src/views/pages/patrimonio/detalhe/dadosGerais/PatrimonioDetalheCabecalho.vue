<template>
    <v-row class="header ml-0 mr-0">
        <v-row class="pl-5" cols="12" md="6">
            <v-col class="headerItems headerPatrimonio " md="2" sm="2" xs="6">
                <span class="font-weight-bold">{{getNomeCamposPersonalizados('numeroPatrimonio','VISUALIZACAO')}}</span>
                <p class="mb-0">{{patrimonio.numero}}</p>
            </v-col>
            <v-col :class="{headerTipoReceitas : patrimonio.tipo === 'RECEITAS_FORMULAS_PROJETOS'}"
                   class="headerItems " md="3" sm="4" xs="6">
                <span class="font-weight-bold">{{getNomeCamposPersonalizados('tipo','VISUALIZACAO')}}</span>
                <p class="mb-0">{{patrimonio.tipo | azEnum(tipoPatrimonio)}}</p>
            </v-col>
            <v-col class="headerItems " md="2" sm="2" xs="6">
                <span class="font-weight-bold">{{getNomeCamposPersonalizados('situacao','VISUALIZACAO')}}</span>
                <p class="mb-0">{{patrimonio.situacao | azEnum(situacaoPatrimonio)}}</p>
            </v-col>
        </v-row>

        <v-row class="d-flex justify-end" cols="6" md="6">
            <v-col class=" d-flex justify-end align-center" md="4" sm="12">
                <memorando-button :patrimonioId="patrimonio.id"/>
            </v-col>
            <v-col class=" d-flex justify-end align-center" md="4" sm="12"
                   v-if="patrimonio.permitirDesativacao && verificaPermissaoEdicao()">
                <modal-cancelar-ativacao @cancelarAtivacao='tratarEventoCancelarAtivacao'/>
            </v-col>
        </v-row>
    </v-row>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import {createNamespacedHelpers} from 'vuex'
    import MemorandoButton from '@/views/components/MemorandoButton'

    const {mapGetters} = createNamespacedHelpers('CamposPersonalizados')
    export default {
        name: 'PatrimonioDetalheCabecalho',
        components: {MemorandoButton},
        props: {
            patrimonio: {
                required: true
            }
        },
        data() {
            return {
                rotaOrigem: this.$store.state.rota.origem
            }
        },
        methods: {
            tratarEventoCancelarAtivacao() {
                this.$emit('cancelarAtivacao')
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            }
        },
        computed: {
            ...mapGetters(['getNomeCamposPersonalizados'])
        }
    }
</script>

<style lang="stylus">

    .headerItems {
        font-size: 0.9em;
        color: white;
        max-height: 65px;
        min-width: 170px;
        max-width: 170px;
    }

    .headerPatrimonio {
        min-width: 180px;
        max-width: 180px;
    }

    .headerValor {
        min-width: 220px;
        max-width: 220px;
    }

    .headerItemFicha {
        max-width: 180px;
        min-width: 180px;
    }

    .headerTipoReceitas {
        min-width: 270px;
        max-width: 270px;
    }

    .header {
        background: #487b9c;
    }

    .header span {
        color: aliceblue
    }

    .outlineClass {
        max-height: 52%;
        padding-left: 2%;
        padding-right: 2%;
        border: #a0a0a0 solid 1px;
        border-radius: 5px;
    }

</style>
