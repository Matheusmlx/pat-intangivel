<template>
    <v-container fluid>
        <div>
            <az-desfazer :show="showDesfazer" text='Documento removido!' @desfazer="desfazerExclusao" @closeButtom="showDesfazer=false"/>
        </div>
        <listagem-documentos-vazia
            v-if="documentos.length === 0"
            @novoDocumento="novoDocumento"/>

        <div v-else class="patrimonio-container-conteudo">
            <div class="d-flex mt-5 pr-5 justify-end">
                <v-btn
                    v-if="verificaPermissaoEdicao"
                    color="primary"
                    depressed
                    @click="novoDocumento">
                    Novo
                </v-btn>
            </div>
            <div :key="documento.id" v-for="(documento, index) in documentos">
                <listagem-documentos-item
                    v-model="documentos[index]"
                    :tipoDocumento="tiposDocumento"
                    :index="index"
                    class="ml-9"
                    @baixarAnexo="baixarAnexo"
                    @validarUnicoNumero="validarNumeroUnico(documentos[index],index)"
                    @removerDocumento="remover"
                    @salvar="tratarEventoSalvar"
                    @salvarAnexo="tratarEventoSalvarAnexo(documentos[index],index)"/>
            </div>
        </div>

         <v-row align-content="end">
             <v-col cols="12">
                <listagem-documentos-acoes
                    :patrimonio="patrimonio"
                    @voltar="tratarEventoVoltar"
                    @ativar="tratarEventoAtivar"/>
             </v-col>
        </v-row>
    </v-container>
</template>

<script>
    import ListagemDocumentosAcoes from './ListagemDocumentosAcoes'
    import ListagemDocumentosVazia from './ListagemDocumentosVazia'
    import ListagemDocumentosItem from './ListagemDocumentosItem'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        data() {
            return {
                documentos: [],
                tiposDocumento: [],
                rotaOrigem: this.$store.state.rota.origem,
                patrimonio: {},
                documentoVazio:true,
                showDesfazer:false
            }
        },
        components: {ListagemDocumentosAcoes, ListagemDocumentosItem, ListagemDocumentosVazia},
        async mounted() {
            this.$gtag.event('PatrimonioCadastroListagemDocumentos', { method: 'Google' })
            await this.buscaPatrimonio()
            await this.buscarTipoDocumentos()
            await this.buscarDocumentos()
        },
        computed: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Patrimonio.Normal'])
            },
        },
        methods: {

            async baixarAnexo(anexo) {
                await this.$store.dispatch(actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_DOWNLOAD, anexo)
            },
            async tratarEventoSalvarAnexo(anexo, index) {
                this.desabilitarLoadingGlobal()
                if (anexo.uriAnexo) {
                    this.documentos[index].uriAnexo = await this.$store.dispatch(actionTypes.PATRIMONIO.DOCUMENTO.DOCUMENTO_UPLOAD, anexo)
                }
                this.habilitarLoadingGlobal()
            },
            async novoDocumento() {
                if(this.verificaQuantidadeDeDocumentosExcedida()){
                    this.mostrarNotificacaoErro('A quantidade máxima de documentos cadastrados para este patrimônio foi atingida.')
                }else{
                    if(this.validarCadastrosFinalizados()){
                        this.documentos.push({
                            idPatrimonio: this.$route.params.id
                        })
                        this.documentoVazio = false
                    }else{
                        this.mostrarNotificacaoAviso('Finalize o cadastro para cadastrar novos documentos')
                    }
                }
            },
            verificaQuantidadeDeDocumentosExcedida(){
                if(this.documentos.length === 30){
                    return true
                }
                return false
            },
            validarCadastrosFinalizados(){
                return this.documentoVazio
            },
            async tratarEventoAtivar() {
                await this.$store.dispatch(actionTypes.PATRIMONIO.ATIVA_PATRIMONIO, this.$route.params.id)
                this.mostrarNotificacaoSucessoDefault()
                this.verificaSePatrimonioJaCanceladoAtivacao(this.$route.params.id)
                await this.$router.push({name: 'FichaPatrimonio', params: {id: this.$route.params.id}})
            },
            verificaSePatrimonioJaCanceladoAtivacao(patrimonioId){
                if(this.$store.state.patrimonio.ativacaoCanceladaPatrimonioId === patrimonioId){
                    this.emiteMensagemAvisoMemorando()
                }
            },
            emiteMensagemAvisoMemorando(){
                setTimeout(()=>{
                    this.mostrarNotificacaoAviso('Alterações feitas impactam o memorando. Gere novamente esse memorando!')
                },1500)
            },
            async remover(documento) {
                if (typeof documento.numero !== 'undefined' && typeof documento.id !== 'undefined') {
                    await this.$store.dispatch(actionTypes.PATRIMONIO.DOCUMENTO.DELETAR_DOCUMENTO, documento)
                    this.showDesfazer = true
                    setTimeout(()=>{
                        this.showDesfazer=false
                    },6000)
                } else {
                    this.$store.commit(mutationTypes.PATRIMONIO.REMOVER_DOCUMENTOS, documento)
                    this.documentoVazio = true
                }
            },
            async desfazerExclusao() {
                const documentoBackup =this.$store.state.patrimonio.documentoBackup
                await this.$store.dispatch(actionTypes.PATRIMONIO.DOCUMENTO.CADASTRAR_DOCUMENTO, documentoBackup)
                this.documentoVazio =true
                await this.buscarDocumentos()
                this.showDesfazer =await false
            },

            async validarNumeroUnico(documentoValidar, index){
                this.desabilitarLoadingGlobal()
                if(!documentoValidar.id || documentoValidar.id){
                    const documentosCadastrados = await this.$store.dispatch(actionTypes.PATRIMONIO.DOCUMENTO.VALIDAR_UNICO_NUMERO_DOCUMENTO, this.$route.params.id)
                    const documentoEncontrado = documentosCadastrados.find(element => {
                        return element.numero === documentoValidar.numero && element.idTipoDocumento === documentoValidar.idTipoDocumento && element.id !== documentoValidar.id
                    })
                    if(!documentoEncontrado){
                        return
                    }else{
                        this.documentos[index].numero = null
                        this.mostrarNotificacaoErro('Já existe um documento com esse número')
                    }
                }
                this.habilitarLoadingGlobal()
            },
            async buscarTipoDocumentos() {
                this.tiposDocumento = await this.$store.dispatch(actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO)
            },
            async buscarDocumentos() {
                this.documentos = await this.$store.dispatch(actionTypes.PATRIMONIO.DOCUMENTO.BUSCAR_DOCUMENTOS, this.$route.params.id)
            },
            async tratarEventoSalvar(documento) {
                this.desabilitarLoadingGlobal()
                if(documento.valor === 0){
                    documento.valor=null
                }
                if (typeof documento.id == 'undefined') {
                    await this.$store.dispatch(actionTypes.PATRIMONIO.DOCUMENTO.CADASTRAR_DOCUMENTO, documento)
                    this.documentoVazio =true
                    this.buscarDocumentos()
                } else {
                    await this.$store.dispatch(actionTypes.PATRIMONIO.DOCUMENTO.ATUALIZAR_DOCUMENTO, documento)
                    this.documentoVazio =true
                }
                this.habilitarLoadingGlobal()
            },
            async buscaPatrimonio() {
                this.patrimonio = await this.$store.dispatch(actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID, this.$route.params.id)
            },
            tratarEventoVoltar() {
                this.$router.push({
                    name: 'DadosDeEntradaEdicao',
                    params: {id: this.$route.params.id},
                })
            }
        }
    }
</script>

