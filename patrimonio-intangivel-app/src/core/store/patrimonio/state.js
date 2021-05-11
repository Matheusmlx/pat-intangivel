export default {
    todosPatrimonios: {
        paginacao: {
            page: 1,
            rowsPerPage: 12,
            sortDesc: [false],
            sortBy: ['situacao']
        },
        filtros: {
            tipoPatrimonio: {
                value: null,
                default: null,
            },
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa'
            }
        }
    },
    patrimonio: {
        tipo: null,
        fornecedor: {
            nome: ''
        }
    },
    alert:{},
    camposPersonalizados:{},
    situacaoVindora: '',
    isLista: true,
    documentos: [],
    documentoBackup:{},
    tipoDocumento: [],
    orgaos: {},
    setores: {},
    ativacaoCanceladaPatrimonioId: null

}
