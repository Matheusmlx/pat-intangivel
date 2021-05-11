<template>
    <v-card flat min-height="305" class="mr-1">
        <v-card-title class="text--darken-2 font-weight-bold grey--text text-sm-body-2">
            Próximas Licenças a Vencer
        </v-card-title>
        <v-divider/>
        <v-simple-table class="mr-3 ml-3">
            <tbody class="text--darken-2 grey--text text-sm-body-2 styleCard">
                <tr @click="acessarLicenca(patrimonio.id)"
                    v-for="(patrimonio,index) in licencas"
                    :key="index">
                    <td>
                        <span class="d-inline-block text-truncate"
                              style="max-width: 15vw;">
                            {{ patrimonio.nome }}
                        </span>
                    </td>
                    <td :class="patrimonio.diasParaVencer <= 30 ? 'red--text text-right' : 'text-right'">
                        {{ patrimonio.diasParaVencer | formatarText() }}
                    </td>
                </tr>
            </tbody>
        </v-simple-table>
    </v-card>
</template>

<script>
    export default {
        name: 'Dashboard',
        props: ['licencas'],
        filters: {
            formatarText(qtdDias) {
                return qtdDias === 1 ? `${qtdDias}  dia` : `${qtdDias}  dias`
            }
        },
        methods: {
            acessarLicenca(idLicenca) {
                this.$emit('acessarLicenca', idLicenca)
            }
        }
    }
</script>

<style lang="stylus">
    .styleCard
        cursor: pointer
</style>
