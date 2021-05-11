<template>
    <az-container class="contaContabilListagemTabela">
        <az-form>
            <v-data-table :headers="colunas"
                          :items="itens"
                          :loading="false"
                          :options.sync="paginacaoInterna"
                          :server-items-length="totalItens"
                          class="az-table-list"
                          hide-default-footer
                          no-data-text="Não há contas contábeis cadastradas">

                <template v-slot:item.tipo="{ item }">
                    <az-text-view-edit-select
                        v-model="item.tipo"
                        name="Tipo"
                        placeholder="Selecione"
                        :iconClose="true"
                        :items="tipoContaContabil"
                        validate="required"
                        @change="salvaContaContabil($event,item)"/>
                </template>

                <template v-slot:item.metodo="{ item }">
                    {{item.metodo | azEnum(metodoCalculoContaContabil)}}
                </template>
            </v-data-table>

            <div class="az-pagination">
                <v-pagination
                    v-if="paginas > 1"
                    v-model="paginacaoInterna.page"
                    :length="paginas"/>

                <v-spacer v-if="paginas <= 1"/>

                <div class="az-select-pagination">
                    <span>Linhas por página:</span>
                    <v-select :items="linhasPorPagina" v-model="paginacaoInterna.rowsPerPage"/>
                </div>
            </div>
        </az-form>
    </az-container>
</template>

<script>
    import {mapState} from 'vuex'
    import AzTextViewEditSelect from '@/views/components/AzTextViewEditSelect'
    import {createNamespacedHelpers} from 'vuex'
    import moment from 'moment'

    const {mapGetters} = createNamespacedHelpers('CamposPersonalizados')

    export default {
        name: 'ContaContabilListagemTabela',
        components: {AzTextViewEditSelect},
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        
        data() {
            return {
                colunas: [
                    {
                        text: 'codigoContabil',
                        value: 'codigo',
                        sortable: false,
                        align: 'left',
                        width: '15%',
                        class: 'black--text'
                    },
                    {
                        text: 'contaContabil',
                        value: 'nome',
                        sortable: false,
                        align: 'left',
                        width: '40%',
                        class: 'black--text'
                    },
                    {
                        text: 'tipoDaConta',
                        value: 'tipo',
                        sortable: false,
                        align: 'left',
                        width: '25%',
                        class: 'black--text'
                    },
                    {
                        text: 'metoDeCalculo',
                        value: 'metodo',
                        sortable: false,
                        align: 'left',
                        width: '20%',
                        class: 'black--text'
                    }
                ],
                paginacaoInterna: this.paginacao,
                linhasPorPagina: [10, 25, 50, 100]
            }
        },
        computed: {
            ...mapState(['loki']),
            ...mapGetters(['getNomeCamposPersonalizados'])
        },
        created(){
            this.setarCampos()
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true
            }
        },
        methods: {
            setarCampos(){
                this.colunas.forEach(element => {
                    let labelCampo = this.getNomeCamposPersonalizados(element.text,'CONTAS_CONTABEIS')
                    element.text = labelCampo
                });
            },
            salvaContaContabil(tipo, contaContabil) {
                this.mostrarNotificacaoAviso('Atenção: alterar o tipo da conta contábil não afeta patrimônios ativados anteriormente!')
                contaContabil.tipo = tipo
                if(contaContabil.idConfigAmortizacao){
                    this.$emit('editar', contaContabil)
                }else{
                    this.$emit('salvar', contaContabil)
                }

            }
        }
    }
</script>

<style lang="stylus" >
    .contaContabilListagemTabela
        .az-table-list
            .v-input__slot
                border none
                font-size 0.8em
                color black
                border-bottom 1px solid #CCCCCC
                padding-left 0px
                padding-right 0px
                max-height 35px
            .v-input
                padding-top 0px


        .input >>> .v-input__control
            background-color white

        .input >>> .v-input
            max-width 300px


        .v-data-table >>> tbody tr:hover
            cursor pointer !important

        @media (max-width: 720px)
            .az-table-list
                .mobile
                    display inline-block
                    margin-left 3px

        br
            display none
</style>

