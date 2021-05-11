import mutations from './mutations'
import { mutationTypes } from '@/core/constants'

describe('Mutations', () => {
    it('Deve chamar a mutation SET_PATRIMONIO e setar o state patrimonio', () => {
        const state = {
            patrimonio: {},
        }
        const tipoPatrimonio = 'SOFTWARES'

        mutations[mutationTypes.PATRIMONIO.SET_PATRIMONIO](state, tipoPatrimonio)

        expect(state.patrimonio).toEqual(tipoPatrimonio)
    })

    it('Deve chamar a mutation SET_FILTROS e setar a filtros de todosPatrimonio', () => {
        const state = {
            todosPatrimonios: {
                filtros: {},
            },
        }

        const filtros = {
            tipoPatrimonio: {
                value: null,
                default: null,
            },
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa',
            }
        }

        mutations[mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS](state, filtros)

        expect(state.todosPatrimonios.filtros).toEqual(filtros)
    })
    it('Deve chamar a mutation SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS e setar paginacao de todosPatrimonios', () => {
        const state = {
            todosPatrimonios: {
                paginacao: {},
            },
        }
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
        mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS](state, paginacao)
        expect(state.todosPatrimonios.paginacao).toEqual(paginacao)
    })

    it('Deve chamar a mutation [mutationTypes.PATRIMONIO.SET_DOCUMENTOS e setar o documento', () => {
        const state = {
            documentos: [],
        }
        const documento = {}
        mutations[mutationTypes.PATRIMONIO.SET_DOCUMENTOS](state, documento)
        expect(state.documentos).toEqual({})
    })

    it('Deve chamar a mutation PATRIMONIO.REMOVER_DOCUMENTOS', () => {
        const state = {
            documentos: [],
        }
        const documento = {}
        mutations[mutationTypes.PATRIMONIO.REMOVER_DOCUMENTOS](state, documento)
        expect(state.documentos).toEqual([])
    })
})
