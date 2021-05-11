import {mutationTypes} from '@/core/constants'
import mutations from './mutations'

const state = {
    movimentacao: {},
    todasMovimentacoes: {
        filtros: {},
        paginacao: {}
    }
}

describe('Mutations', () => {
    it('Deve setar movimentação na state', () => {
        const movimentacao = {
            id: 1,
            tipo: 'TRANSFERENCIA_DEFINITIVA',
            patrimonioId: 1
        }
        mutations[mutationTypes.MOVIMENTACAO.SET_MOVIMENTACAO](state, movimentacao)
        expect(state.movimentacao).toEqual(movimentacao)
    })

    it('Deve chamar a mutation SET_FILTROS e setar os filtros de todasMovimentacoes', () => {
        const filtros = {
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa',
            }
        }

        mutations[mutationTypes.MOVIMENTACAO.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES](state, filtros)

        expect(state.todasMovimentacoes.filtros).toEqual(filtros)
    })
    it('Deve chamar a mutation SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES e setar paginacao de todasMovimentacoes', () => {
        const paginacao = {
            page: 1,
            itemsPerPage: 10,
            sortBy: ['nome'],
            sortDesc: [],
            groupBy: [],
            gtoupDesc: [],
            mustSort: false,
            multiSort: false,
            rowsPerPage: 10,
            descending: false,
        }
        mutations[mutationTypes.MOVIMENTACAO.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES](state, paginacao)
        expect(state.todasMovimentacoes.paginacao).toEqual(paginacao)
    })

    it('Deve chamar a mutation SET_DOCUMENTO', async () => {
        const documentos = [
            {id:1,numero:'488',idPatrimonio:2,idMovimentacao:3,idTipoDocumento:5},
            {id:1,numero:'487',idPatrimonio:2,idMovimentacao:3,idTipoDocumento:5}
        ]

        mutations[mutationTypes.MOVIMENTACAO.DOCUMENTO.SET_DOCUMENTOS_MOVIMENTACOES](state, documentos)
        expect(state.documentos).toEqual(documentos)
    })
})
