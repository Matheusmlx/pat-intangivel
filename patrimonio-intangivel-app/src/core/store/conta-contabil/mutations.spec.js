import { mutationTypes } from '@/core/constants'
import mutations from './mutations'

let state = {
    todasContasContabeis: {
        paginacao: {},
        filtros: {},
    },
}

describe('Mutations', () => {
    it('Deve setar conta contabil no state', () => {
        const contaContabil = {
            tipo: 'DEPRECIAVEL',
            metodo: 'QUOTAS_CONSTANTES',
            percentualResidual: '15',
            vidaUtil: '45',
            sistema: 'INTANGIVEL',
        }
        mutations[mutationTypes.CONTA_CONTABIL.SET_CONTA_CONTABIL](state, contaContabil)
        expect(state.contaContabil).toEqual(contaContabil)
    })

    it('Deve setar a paginacao no state todasContasContabeis', () => {
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            descending: false,
            sortBy: [''],
        }
        mutations[mutationTypes.CONTA_CONTABIL.SET_PAGINACAO_BUSCA_TODAS_CONTAS_CONTABEIS](state, paginacao)
        expect(state.todasContasContabeis.paginacao).toEqual(paginacao)
    })

    it('Deve setar o filtro ', () => {
        const filtros = {
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa',
            },
        }
        mutations[mutationTypes.CONTA_CONTABIL.SET_FILTROS_BUSCA_TODAS_CONTAS_CONTABEIS](state, filtros)
        expect(state.todasContasContabeis.filtros).toEqual(filtros)
    })
})
