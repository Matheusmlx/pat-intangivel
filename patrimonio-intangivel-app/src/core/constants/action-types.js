export default {
    COMUM: {
        BUSCAR_LINK_EDITAR_USUARIO: 'buscarLinkEditarUsuario',
        BUSCAR_LINK_SIGAPATRIMONIO: 'buscarLikSigaPatrimonio',
        BUSCAR_PRODUTO_POR_NOME: 'buscarProdutoPorNome',
        BUSCAR_USUARIO_LOGADO: 'buscarUsuarioLogado',
        BUSCAR_TODOS_ORGAOS: 'buscarTodosOrgaos',
        BUSCAR_SETORES: 'buscarSetores',
        BUSCAR_FORNECEDORES: 'buscarFornecedores',
        BUSCAR_FORNECEDOR_POR_ID: 'buscarFornecedorPorId',
        ATUALIZAR_SESSAO_DOMINIO: 'atualizarSessaoDominio',
        BUSCAR_PARAMETROS: 'buscarParametros',
        BUSCAR_FUSO_HORARIO: 'buscarFusoHorario'
    },
    PATRIMONIO: {
        BUSCAR_PATRIMONIO_POR_ID: 'buscarPatrimonio',
        BUSCAR_TODOS_PATRIMONIOS: 'buscarTodosPatrimonios',
        CADASTRAR_PATRIMONIO: 'cadastrarPatrimonio',
        ATUALIZAR_PATRIMONIO: 'atualizarPatrimonio',
        DELETAR_PATRIMONIO: 'deletarPatrimonio',
        DELETAR_PATRIMONIO_NAO_ALTERADO: 'deletarPatrimonioNaoAlterado',
        REVERTER_ATIVACAO: 'reverterAtivacao',
        ATIVA_PATRIMONIO: 'ativaPatrimonio',
        VISUALIZAR_PATRIMONIO_POR_ID: 'visualizarPatrimonioPorId',
        BUSCAR_AMORTIZACOES: 'buscarAmortizacoes',
        BAIXAR_RELATORIO_LISTAGEM_PATRIMONIO_XLS: 'baixarRelatorioListagemPatrimonioXls',
        DOCUMENTO: {
            BUSCAR_DOCUMENTOS: 'buscarDocumentos',
            ATUALIZAR_DOCUMENTO: 'atualizarDocumento',
            BUSCAR_TIPO_DOCUMENTO: 'buscarTipoDocumento',
            CADASTRAR_DOCUMENTO: 'cadastrarDocumento',
            DELETAR_DOCUMENTO: 'deletarDocumento',
            DOCUMENTO_UPLOAD: 'documentoUpload',
            DOCUMENTO_DOWNLOAD: 'documentoDownload',
            VALIDAR_UNICO_NUMERO_DOCUMENTO: 'validarUnicoNumeroDocumento'
        }
    },
    CONTA_CONTABIL: {
        BUSCAR_TODAS_CONTAS_CONTABEIS: 'buscarTodasContasContabeis',
        BUSCAR_CONTA_CONTABIL_POR_ID: 'buscarContaContabilPorId',
        SALVAR_CONTA_CONTABIL: 'salvarContaContabil',
        EDITAR_CONTA_CONTABIL: 'editarContaContabil'
    },
    AMORTIZACAO: {
        GERAR_AMORTIZACAO_MANUAL: 'gerarAmortizacaoManual'
    },
    RELATORIO: {
        BAIXAR_RELATORIO_INVENTARIO_ANALITICO_PDF: 'baixarRelatorioInventarioAnaliticoPdf',
        BAIXAR_RELATORIO_INVENTARIO_ANALITICO_XLS: 'baixarRelatorioInventarioAnaliticoXls',
        BAIXAR_RELATORIO_INVENTARIO_SINTETICO_PDF: 'baixarRelatorioInventarioSinteticoPdf',
        BAIXAR_RELATORIO_INVENTARIO_SINTETICO_XLS: 'baixarRelatorioInventarioSinteticoXls',
        BAIXAR_RELATORIO_MEMORANDO_PDF: 'baixarRelatorioMemorandoPdf',
        BAIXAR_RELATORIO_MEMORANDO_MOVIMENTACAO_PDF:'baixarRelatorioMemorandoMovimentacaoPdf'
    },
    CAMPOS_PERSONALIZADOS: {
        GET_ALL_CAMPOS_PERSONALIZADOS: 'getAllCamposPersonlizados',
    },
    MOVIMENTACAO: {
        ATUALIZAR_MOVIMENTACAO: 'atualizarMovimentacao',
        BUSCAR_MOVIMENTACAO_POR_PATRIMONIO: 'buscarMovimentacaoPorPatrimonio',
        CADASTRAR_MOVIMENTACAO: 'cadastrarMovimentacao',
        ENVIAR_MOVIMENTACAO: 'enviarMovimentacao',
        BUSCAR_ORGAOS_DESTINO: 'buscarOrgaosDestino',
        DELETAR_MOVIMENTACAO: 'deletarMovimentacao',
        BUSCAR_MOVIMENTACAO_POR_ID:'buscarMovimentacaoPorId',
        BUSCAR_TODAS_MOVIMENTACOES: 'buscarTodasMovimentacoes',
        BUSCAR_MOVIMENTACOES_PENDENTES: 'buscarMovimentacoesPendentes',
        BUSCAR_PATRIMONIO_POR_MOVIMENTACAO: 'buscarPatrimonioPorMovimentacao',
        RECEBER_MOVIMENTACAO:'receberMovimentacao',
        REJEITAR_MOVIMENTACAO: 'rejeitarMovimentacao',
        DOCUMENTO: {
            CADASTRAR_DOCUMENTO_MOVIMENTACAO: 'cadastrarDocumentoMovimentacao',
            ATUALIZAR_DOCUMENTO_MOVIMENTACAO: 'atualizarDocumentoMovimentacao',
            DOCUMENTO_UPLOAD_MOVIMENTACAO: 'documentoUploadMovimentacao',
            BUSCAR_TIPO_DOCUMENTO_MOVIMENTACAO: 'buscarTipoDocumentoMovimentacao',
            BUSCAR_DOCUMENTOS_MOVIMENTACAO:'buscarDocumentosMovimentacao',
            DELETAR_DOCUMENTO_MOVIMENTACAO: 'deletarDocumentoMovimentacao'
        }
    },
    DASHBOARD: {
        BUSCAR_TOTALIZADORES: 'buscarTotalizadores',
        BUSCAR_LICENCAS_PROXIMAS_A_VENCER: 'buscarLicencasProximasAVencer',
        BUSCAR_INTANGIVEIS_POR_ORGAO: 'buscarIntangiveisPorOrgao',
        BUSCAR_INTANGIEIS_POR_TIPO:'buscarIntangiveisPorTipo'
    }
}
