<template>
    <div class="az-actions-form">
        <v-btn @click="tratarEventoVoltar" color="grey" outlined text>
            <v-icon class="mr-1" size="15">fas fa-arrow-left</v-icon>
            VOLTAR
        </v-btn>
        <v-spacer/>
        <v-btn
            @click="tratarEventoAtivar"
            color="primary"
            depressed
            :disabled="!verificaPermissaoEdicao"
            v-if="patrimonio.estado === 'PRONTO_PARA_USO'">
            <v-icon class="mr-1" size="15">fas fa-check</v-icon>
            Ativar
        </v-btn>
        <v-tooltip
            max-width="250"
            nudge-left="-10"
            top
            v-else>
            <template v-slot:activator="{ on }">
                <div v-on="on" :class="{'btn-disabled': patrimonio.estado !== 'PRONTO_PARA_USO'}">
                    <v-btn
                        :disabled="patrimonio.estado !== 'PRONTO_PARA_USO' || !verificaPermissaoEdicao"
                        color="primary"
                        depressed>
                        <v-icon class="mr-1" size="15">fas fa-check</v-icon>
                        Ativar
                    </v-btn>
                </div>
            </template>
            Patrimônios em desenvolvimento não podem ser ativados.
        </v-tooltip>
    </div>
</template>

<script>
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        props: {
            patrimonio: {
                type: Object,
                default: () => {
                    return {}
                }
            }
        },
        methods: {
            tratarEventoAtivar() {
                this.$emit('ativar')
            },
            tratarEventoVoltar() {
                this.$emit('voltar')
            }
        },
        computed: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            },
        }
    }
</script>
