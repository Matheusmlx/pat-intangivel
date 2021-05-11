export default {
    movimentacao: {},
    documentos:[],
    movimentacaoPendente:{},
    documentoBackup:{},
    todasMovimentacoes: {
        paginacao: {
            page: 1,
            rowsPerPage: 12,
            sortDesc: [true],
            sortBy: ['situacao']
        },
        filtros: {
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa'
            },
            conteudoExtra: {
                value: null,
                default: null,
                label: 'Pesquisa'
            }
        }
    },
    todasMovimentacoesPendentes: {
        paginacao: {
            page: 1,
            rowsPerPage: 12,
            sortDesc: [true],
            sortBy: ['situacao']
        },
        filtros: {
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa'
            },
            conteudoExtra: {
                value: 'AGUARDANDO_RECEBIMENTO',
                default: null,
                label: 'Pesquisa'
            }
        }
    }
}
