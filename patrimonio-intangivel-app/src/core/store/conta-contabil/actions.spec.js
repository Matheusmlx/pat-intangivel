import actions from './actions'
import { actionTypes, mutationTypes } from '@/core/constants'

let url, body, state, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
    put(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
    get(_url) {
        return new Promise(resolve => {
            url = _url
            resolve({ data: mockResponseData })
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            url = _url
            resolve()
        })
    },
}))

describe('Actions', () => {
    const commit = jest.fn()

    beforeEach(() => {
        url = ''
        body = undefined
        mockResponseData = true
        state = {}
    })

    it('deve salvar conta contabil', async () => {
        const contaContabil = {
            id: 1,
            tipo: 'DEPRECIAVEL',
            metodo: 'QUOTAS_CONSTANTES',
            percentualResidual: '15',
            vidaUtil: '45',
            sistema: 'INTANGIVEL',
        }

        returnedResponse = await actions[actionTypes.CONTA_CONTABIL.SALVAR_CONTA_CONTABIL]({ commit }, contaContabil)

        expect(url).toBe(`api/configuracao/contas-contabeis/${contaContabil.id}/config-depreciacao`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.CONTA_CONTABIL.SET_CONTA_CONTABIL, true)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('deve buscar buscar conta contabil por id', async () => {
        const idContaContabil = 1

        returnedResponse = await actions[actionTypes.CONTA_CONTABIL.BUSCAR_CONTA_CONTABIL_POR_ID]({ commit }, idContaContabil)
        expect(url).toBe(`api/configuracao/contas-contabeis/${idContaContabil}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.CONTA_CONTABIL.SET_CONTA_CONTABIL, true)
        expect(returnedResponse).toBeTruthy()
    })

    it('deve buscar todas as contas contabeis', async () => {
        const state = {
            todasContasContabeis: {
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    descending: false,
                    sortBy: [''],
                },
                filtros: {
                    conteudo: {
                        value: null,
                        default: null,
                        label: 'Pesquisa',
                    },
                },
            },
        }
        returnedResponse = await actions[actionTypes.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS]({ state })
        expect(url).toBe('api/configuracao/contas-contabeis?page=1&size=10&sort=&direction=ASC')
    })

    it('deve editar conta contabil', async () => {
        const contaContabil = {
            id: 1,
            tipo: 'DEPRECIAVEL',
            metodo: 'QUOTAS_CONSTANTES',
            percentualResidual: '15',
            vidaUtil: '45',
            sistema: 'INTANGIVEL',
            idConfigAmortizacao: 2,
        }

        returnedResponse = await actions[actionTypes.CONTA_CONTABIL.EDITAR_CONTA_CONTABIL]({ commit }, contaContabil)

        expect(url).toBe(`api/configuracao/contas-contabeis/${contaContabil.id}/config-amortizacao/${contaContabil.idConfigAmortizacao}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.CONTA_CONTABIL.SET_CONTA_CONTABIL, true)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })
})
