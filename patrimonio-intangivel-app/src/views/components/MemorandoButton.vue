<template>
    <div v-if="movimentacao">
        <v-tooltip top v-if="tooltip">
            <template v-slot:activator="{ on, attrs }">
                <v-btn
                    color="white"
                    icon
                    v-bind="attrs"
                    v-on="on"
                    @click="tratarEventoGerarMemorando"
                    :disabled="carregando"
                    :loading="carregando">
                    <v-icon :size="sizeButton">description</v-icon>
                </v-btn>
            </template>
            <span>Memorandos</span>
        </v-tooltip>
        <v-btn v-else
               color="grey"
               text
               outlined
               class="text-body-2"
               width="150"
               @click="tratarEventoGerarMemorando"
               :disabled="carregando || disabled"
               :loading="carregando">
            <v-icon :size="sizeButton" left>description</v-icon>
            Memorando
        </v-btn>
    </div>
    <div v-else>
        <v-tooltip v-if="tooltip"
                   nudge-top="0"
                   top
                   transition="scale-transition"
                   origin="center center"
                   max-width="300">
            <template v-slot:activator="{ on }">
                <v-btn
                    small
                    @click="tratarEventoGerarMemorando"
                    v-on="on"
                    :dark="dark"
                    :text="text"
                    :outlined="outlined"
                    :disabled="carregando"
                    :loading="carregando"
                    class="buttonMemorando">
                    <span class="text-capitalize">Memorando</span>
                </v-btn>
            </template>
            Memorando ainda não tem valor contábil. Ele terá valor contábil após ativação do patrimônio.
        </v-tooltip>
        <v-btn v-else
               @click="tratarEventoGerarMemorando"
               :dark="dark"
               :text="text"
               :outlined="outlined"
               :disabled="carregando"
               :loading="carregando"
               class="buttonMemorando mr-12">
            <span :class="{ apagaBotaoClass: apagaBotao === true }" class="text-capitalize">Memorando</span>
        </v-btn>
    </div>
</template>

<script>
    import {actionTypes} from '@/core/constants'

    export default {
        name: 'MemorandoButton',
        data() {
            return {
                apagaBotao:false,
                carregando:false
            }
        },
        props:{
            text:{
                type: Boolean,
                default: true
            },
            dark:{
                type: Boolean,
                default : true
            },
            outlined:{
                type: Boolean,
                default : false
            },
            sizeButton:{
                type:Number,
                default:10
            },
            tooltip:{
                type: Boolean,
                default: false
            },
            patrimonioId:{
                required: false
            },
            movimentacao: {
                type: Boolean,
                default: false
            },
            movimentacaoId: {
                type: Number,
                required: false
            },
            disabled: {
                type: Boolean,
                default: false
            }
        },
        watch:{
            patrimonioId(){
                if(!this.tooltip){
                    this.verificaSePatrimonioJaCanceladoAtivacao(this.patrimonioId)
                }
            }
        },
        methods:{
            async tratarEventoGerarMemorando() {
                this.desabilitarLoadingGlobal()
                this.setaCarregando(true)
                try{
                    if (this.movimentacao) {
                        await this.$store.dispatch(actionTypes.RELATORIO.BAIXAR_RELATORIO_MEMORANDO_MOVIMENTACAO_PDF, this.movimentacaoId)
                    } else {
                        await this.$store.dispatch(actionTypes.RELATORIO.BAIXAR_RELATORIO_MEMORANDO_PDF, this.patrimonioId)
                    }
                    this.setaCarregando(false)
                }catch (e) {
                    this.setaCarregando(false)
                    this.mostraErro(e)
                }
                this.habilitarLoadingGlobal()
            },
            mostraErro(e){
                var dataView = new DataView(e.response.data)
                var decoder = new TextDecoder('utf8')
                var response = JSON.parse(decoder.decode(dataView))
                var message = response['message']
                this.mostrarNotificacaoErro(message)
            },
            setaCarregando(value){
                this.carregando = value
            },
            verificaSePatrimonioJaCanceladoAtivacao(patrimonioId){
                if(this.$store.state.patrimonio.ativacaoCanceladaPatrimonioId === patrimonioId){
                    setTimeout(()=>{
                        this.emiteModoAlerta(0)
                    },5000)
                }
            },
            emiteModoAlerta(quantidade){
                if(quantidade < 10){
                    this.apagaBotao  = !this.apagaBotao
                    setTimeout(()=>{
                        this.emiteModoAlerta(quantidade+1)
                    },1000)
                }
            }
        }
    }
</script>

<style lang="stylus" scoped>
    .buttonMemorando
        opacity 0.3

    .apagaBotaoClass
        visibility hidden
</style>
